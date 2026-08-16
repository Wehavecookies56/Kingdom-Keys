package online.kingdomkeys.kingdomkeys.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.worldmap.WorldMarkerEntity;
import online.kingdomkeys.kingdomkeys.world.worldmap.GummiWorld;
import online.kingdomkeys.kingdomkeys.world.worldmap.WorldMap;

// Landing in a world
public class ReactionLand extends ReactionCommand {

	public ReactionLand(ResourceLocation registryName) {
		super(registryName, true, -1);
	}

	@Override
	public void onUse(Player player, LivingEntity target, LivingEntity lockedOnEntity) {
		if (!(player instanceof ServerPlayer serverPlayer) || !conditionsToAppear(player, target)) {
			return;
		}

		WorldMarkerEntity marker = WorldMap.nearestMarker(player);
		if (marker == null) {
			return;
		}

		GummiWorld world = marker.getWorld();
		ServerLevel destination = serverPlayer.getServer().getLevel(world.dimension());
		if (destination == null) {
			return;
		}

		WorldMap.travel(serverPlayer, destination, world.landingSpawn(), world.landingLook());
	}

	@Override
	public boolean conditionsToAppear(Player player, LivingEntity target) {
		return WorldMap.isWorldMap(player) && WorldMap.isPilot(player) && WorldMap.nearestMarker(player) != null;
	}

	@Override
	public SoundEvent getUseSound(Player player, LivingEntity target) {
		return ModSounds.portal.get();
	}
}
