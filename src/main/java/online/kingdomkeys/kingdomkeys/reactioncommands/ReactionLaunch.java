package online.kingdomkeys.kingdomkeys.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;
import online.kingdomkeys.kingdomkeys.world.worldmap.GummiWorld;
import online.kingdomkeys.kingdomkeys.world.worldmap.GummiWorldLoader;
import online.kingdomkeys.kingdomkeys.world.worldmap.WorldMap;

// Take-off from a world
public class ReactionLaunch extends ReactionCommand {

	public ReactionLaunch(ResourceLocation registryName) {
		super(registryName, true, -1);
	}

	@Override
	public void onUse(Player player, LivingEntity target, LivingEntity lockedOnEntity) {
		if (!(player instanceof ServerPlayer serverPlayer) || !conditionsToAppear(player, target)) {
			return;
		}

		ServerLevel worldmap = serverPlayer.getServer().getLevel(ModDimensions.WORLDMAP);
		if (worldmap == null) {
			return;
		}

		GummiWorld world = GummiWorldLoader.forDimension(player.level().dimension());
		WorldMap.ensureMarkers(worldmap);
		WorldMap.travel(serverPlayer, worldmap, world.takeOffSpawn(), world.takeOffLook());
	}

	@Override
	public boolean conditionsToAppear(Player player, LivingEntity target) {
		if (WorldMap.isWorldMap(player) || !WorldMap.isPilot(player)) {
			return false;
		}

		GummiWorld world = GummiWorldLoader.forDimension(player.level().dimension());
		return world != null && player.getY() >= world.takeoffAltitude();
	}

	@Override
	public SoundEvent getUseSound(Player player, LivingEntity target) {
		return ModSounds.portal.get();
	}
}
