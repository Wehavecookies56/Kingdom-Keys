package online.kingdomkeys.kingdomkeys.world.worldmap;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import javax.annotation.Nullable;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.worldmap.WorldMarkerEntity;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WorldMap {

	// How far out markers are looked for before their own approach range is applied. Only an upper
	// bound on the search, not the range at which the command appears.
	private static final double MARKER_SEARCH = 128;

	// Ticks to check if markers
	private static final int CHECK_INTERVAL = 100;

	public static boolean isWorldMap(Entity entity) {
		return entity.level().dimension().equals(ModDimensions.WORLDMAP);
	}

	/** Whether this player is the one at the controls of a gummi ship, rather than sitting in the back */
	public static boolean isPilot(Player player) {
		return player.getVehicle() instanceof GummiShipEntity ship && ship.getControllingPassenger() == player;
	}

	/**
	 * Whether this world is closed to building for this player.
	 *
	 * Only laying and taking blocks is ever stopped: doors, chests, buttons, levers and everything else that
	 * answers to a right click keeps working, so a locked world is still a place you can walk into rather than
	 * a museum behind glass. Operators are never held back, which is what leaves the world editable in place.
	 */
	public static boolean isLocked(Player player) {
		if (player == null || player.hasPermissions(2)) {
			return false;
		}

		GummiWorld world = GummiWorldLoader.forDimension(player.level().dimension());
		return world != null && !world.build();
	}

	// Answered on both sides, so a blocked placement never flickers in before the server takes it back
	private static boolean deny(Player player) {
		if (!isLocked(player)) {
			return false;
		}

		player.displayClientMessage(Component.translatable("kingdomkeys.worldmap.no_building"), true);
		return true;
	}

	@SubscribeEvent
	public void breakBlock(BlockEvent.BreakEvent event) {
		if (deny(event.getPlayer())) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void placeBlock(BlockEvent.EntityPlaceEvent event) {
		if (event.getEntity() instanceof Player player && deny(player)) {
			event.setCanceled(true);
		}
	}

	// A bed or a door comes through here instead, and the bus is not guaranteed to hand a subclass to the
	// listener of its parent
	@SubscribeEvent
	public void placeBlocks(BlockEvent.EntityMultiPlaceEvent event) {
		if (event.getEntity() instanceof Player player && deny(player)) {
			event.setCanceled(true);
		}
	}

	// Buckets are the one placement that never reaches EntityPlaceEvent: they are deliberately left out of the
	// snapshot capture that fires it, because they act from Item#use rather than from a block placement
	@SubscribeEvent
	public void useBucket(PlayerInteractEvent.RightClickItem event) {
		if (event.getItemStack().getItem() instanceof BucketItem && deny(event.getEntity())) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void tick(LevelTickEvent.Post event) {
		if (event.getLevel() instanceof ServerLevel level && level.dimension().equals(ModDimensions.WORLDMAP) && level.getGameTime() % CHECK_INTERVAL == 0) {
			ensureMarkers(level);
		}
	}

	// Brings the markers back in line with the datapack
	public static void ensureMarkers(ServerLevel worldmap) {
		Set<String> present = new HashSet<>();

		for (WorldMarkerEntity marker : worldmap.getEntities(ModEntities.TYPE_WORLD_MARKER.get(), marker -> true)) {
			// Duplicates and markers whose entry has gone: neither should be left standing.
			if (marker.getWorld() == null || !present.add(marker.getWorldId())) {
				marker.discard();
			}
		}

		for (Map.Entry<ResourceLocation, GummiWorld> entry : GummiWorldLoader.all().entrySet()) {
			Vec3 at = entry.getValue().worldmapPosition();
			ChunkPos chunk = new ChunkPos(BlockPos.containing(at));

			// An entity in an unloaded chunk neither ticks nor renders, so as far as anyone flying past is concerned the world isn't there. Forcing its chunk is what keeps it around for good.
			worldmap.setChunkForced(chunk.x, chunk.z, true);

			// Nothing may be added until the chunk is actually in memory: spawning now would put a second marker next to the one still sitting on disk.
			if (present.contains(entry.getKey().toString()) || !worldmap.hasChunk(chunk.x, chunk.z)) {
				continue;
			}

			WorldMarkerEntity marker = new WorldMarkerEntity(ModEntities.TYPE_WORLD_MARKER.get(), worldmap);
			marker.setWorldId(entry.getKey().toString());
			marker.setPos(at.x, at.y, at.z);
			worldmap.addFreshEntity(marker);
		}
	}

	// The marker the player is currently close enough to. Each world sets its own approach range, so a big world can be gotten from further out than a small one.
	public static WorldMarkerEntity nearestMarker(Player player) {
		WorldMarkerEntity closest = null;
		double closestDistance = Double.MAX_VALUE;

		for (Entity entity : player.level().getEntities(player, player.getBoundingBox().inflate(MARKER_SEARCH), entity -> entity instanceof WorldMarkerEntity)) {
			WorldMarkerEntity marker = (WorldMarkerEntity) entity;
			GummiWorld world = marker.getWorld();
			if (world == null) {
				continue;
			}

			double distance = marker.distanceToSqr(player);
			if (distance <= world.approachRange() * world.approachRange() && distance < closestDistance) {
				closestDistance = distance;
				closest = marker;
			}
		}
		return closest;
	}

	// Moves the player and the ship to another dimension and puts them back in the pilot's seat, the dismount has to happen first
	public static void travel(ServerPlayer player, ServerLevel destination, Vec3 position, @Nullable Vec2 look) {
		Entity vehicle = player.getVehicle();
		float yRot = look != null ? look.x : player.getYRot();
		float xRot = look != null ? look.y : player.getXRot();

		if (!(vehicle instanceof GummiShipEntity ship)) {
			player.changeDimension(transition(destination, position, yRot, xRot));
			return;
		}

		List<Entity> riders = new ArrayList<>(ship.getPassengers());
		riders.forEach(Entity::stopRiding);

		// Same dimension: nothing to recreate, so just move everyone and sit back down.
		if (player.serverLevel() == destination) {
			ship.teleportTo(position.x, position.y, position.z);
			ship.setYRot(yRot);
			ship.setYBodyRot(yRot);

			for (Entity rider : riders) {
				move(rider, position, yRot, xRot);
				rider.startRiding(ship, true);
			}

			return;
		}

		Entity movedShip = ship.changeDimension(transition(destination, position, yRot, xRot));

		for (Entity rider : riders) {
			Entity moved = rider.changeDimension(transition(destination, position, yRot, xRot));

			if (moved != null && !moved.isRemoved() && movedShip != null && !movedShip.isRemoved()) {
				moved.startRiding(movedShip, true);
			}
		}
	}

	private static void move(Entity entity, Vec3 position, float yRot, float xRot) {
		if (entity instanceof ServerPlayer player) {
			player.connection.teleport(position.x, position.y, position.z, yRot, xRot);
		} else {
			entity.teleportTo(position.x, position.y, position.z);
		}
	}

	private static DimensionTransition transition(ServerLevel level, Vec3 position, float yRot, float xRot) {
		return new DimensionTransition(level, position, Vec3.ZERO, yRot, xRot, entity -> {
		});
	}
}
