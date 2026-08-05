package online.kingdomkeys.kingdomkeys.world.worldmap;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.worldmap.WorldMarkerEntity;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WorldMap {

	// How far out markers are looked for before their own approach range is applied. Only an upper
	// bound on the search, not the range at which the command appears.
	private static final double MARKER_SEARCH = 128;

	// Ticks to check if markers
	private static final int CHECK_INTERVAL = 100;

	public static boolean isWorldMap(Player player) {
		return player.level().dimension().equals(ModDimensions.WORLDMAP);
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
	public static void travel(ServerPlayer player, ServerLevel destination, Vec3 position) {
		Entity vehicle = player.getVehicle();
		float yRot = player.getYRot();
		float xRot = player.getXRot();

		if (!(vehicle instanceof GummiShipEntity ship)) {
			player.changeDimension(transition(destination, position, yRot, xRot));
			return;
		}

		player.stopRiding();

		// Same dimension: nothing to recreate, so just move both and sit back down.
		if (player.serverLevel() == destination) {
			ship.teleportTo(position.x, position.y, position.z);
			player.teleportTo(position.x, position.y, position.z);
			player.startRiding(ship, true);
			return;
		}

		Entity movedShip = ship.changeDimension(transition(destination, position, yRot, xRot));
		player.changeDimension(transition(destination, position, yRot, xRot));

		if (movedShip != null && !movedShip.isRemoved()) {
			player.startRiding(movedShip, true);
		}
	}

	private static DimensionTransition transition(ServerLevel level, Vec3 position, float yRot, float xRot) {
		return new DimensionTransition(level, position, Vec3.ZERO, yRot, xRot, entity -> {
		});
	}
}
