package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public class MagicReflect extends Magic {

	public MagicReflect(ResourceLocation registryName, int tier, ResourceLocation gmAbility) {
		super(registryName, false, gmAbility);
		setTier(tier);
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
		PlayerData playerData = PlayerData.get(caster);
		playerData.setReflectTicks((int) (40 + (getRealDamageMult(caster) * 5)), getTier());
		PacketHandler.syncToAllAround(caster, playerData);
		player.swing(InteractionHand.MAIN_HAND);
	}

	@Override
	public void playMagicCastSound(LivingEntity player, Player caster) {
		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.reflect1.get(), SoundSource.PLAYERS, 1F, 1F);
	}

}
