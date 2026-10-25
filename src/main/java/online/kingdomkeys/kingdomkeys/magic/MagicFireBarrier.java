package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.magic.FireBarrierEntity;

public class MagicFireBarrier extends Magic {
	private final FireBarrierEntity.Tier barrier;

	public MagicFireBarrier(ResourceLocation registryName, int tier, ResourceLocation gmAbility, FireBarrierEntity.Tier barrier) {
		super(registryName, false, gmAbility);
		setTier(tier);
		this.barrier = barrier;
	}

	@Override
	public void magicUse(LivingEntity player, LivingEntity caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmgMult = (getRealDamageMult(caster) + abilityStacks(caster, ModAbilities.FIRE_BOOST) * 0.2F) * fullMPBlastMult;
		player.level().addFreshEntity(new FireBarrierEntity(player.level(), player, dmgMult, barrier));
		caster.swing(InteractionHand.MAIN_HAND);
	}

	@Override
	public void playMagicCastSound(LivingEntity player, LivingEntity caster) {
		player.level().playSound(null, player.getX(), player.getY(), player.getZ(), switch (getTier()) {
			case 0 -> ModSounds.fire.get();
			case 1 -> ModSounds.fira.get();
			default -> ModSounds.firaga.get();
		}, SoundSource.PLAYERS, 1F, 1F);
	}
}
