package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public class MagicReflect extends Magic {

	public MagicReflect(String registryName, int cost, int maxLevel, boolean hasRC, int order) {
		super(registryName, cost, false, maxLevel, hasRC, order);
	}

	@Override
	protected void magicUse(PlayerEntity player, PlayerEntity caster, int level) {
		IPlayerCapabilities casterData = ModCapabilities.getPlayer(caster);
		casterData.setMagicCooldownTicks(40 + 20);
		PacketHandler.sendTo(new SCSyncCapabilityPacket(casterData), (ServerPlayerEntity)caster);

		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		playerData.setReflectTicks(40 + (level * 5), level);
		player.world.playSound(null, player.getPosition(), ModSounds.reflect1.get(), SoundCategory.PLAYERS, 1F, 1F);
		PacketHandler.syncToAllAround(player, playerData);
		player.swingArm(Hand.MAIN_HAND);
	}

}
