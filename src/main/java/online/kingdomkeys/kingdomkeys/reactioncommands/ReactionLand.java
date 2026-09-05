package online.kingdomkeys.kingdomkeys.reactioncommands;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.worldmap.WorldMarkerEntity;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowWarning;
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

		PlayerData playerData = PlayerData.get(serverPlayer);
		if (playerData == null || !playerData.knowsWorld(world)) {
			player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.error, SoundSource.MASTER, 1.0f, 1.0f);

			SCShowWarning.send(serverPlayer, Component.translatable("kingdomkeys.worldmap.locked"));
			return;
		}
		player.level().playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.portal, SoundSource.MASTER, 1.0f, 1.0f);
		WorldMap.travel(serverPlayer, destination, world.landingSpawn(), world.landingLook());
	}

	@Override
	public boolean conditionsToAppear(Player player, LivingEntity target) {
		return WorldMap.isWorldMap(player) && WorldMap.isPilot(player) && WorldMap.nearestMarker(player) != null;
	}

	@Override
	public SoundEvent getUseSound(Player player, LivingEntity target) {
		return null;
	}
}
