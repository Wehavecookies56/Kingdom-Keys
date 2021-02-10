package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public class MagicReflect extends Magic {

	public MagicReflect(String registryName, int cost, int order) {
		super(registryName, cost, false, order);
		this.name = registryName;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onUse(PlayerEntity player, PlayerEntity caster) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		playerData.setReflectTicks(40);
		player.world.playSound(null, player.getPosition(), ModSounds.reflect1.get(), SoundCategory.PLAYERS, 1F, 1F);
		PacketHandler.syncToAllAround(player, playerData);
		player.swingArm(Hand.MAIN_HAND);
	}

}
