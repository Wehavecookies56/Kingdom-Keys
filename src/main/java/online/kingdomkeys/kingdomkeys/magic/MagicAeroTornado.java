package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.magic.AeroTornadoEntity;

public class MagicAeroTornado extends Magic {

	private static final double AHEAD = 4;

	private static final float[] RADIUS = {2.5F, 3.5F, 4.5F};
	private static final float[] HEIGHT = {4F, 5.5F, 7F};

	public MagicAeroTornado(ResourceLocation registryName, int tier, ResourceLocation gmAbility) {
		super(registryName, false, gmAbility);
		setTier(tier);
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
		int tier = Math.min(getTier(), RADIUS.length - 1);

		AeroTornadoEntity tornado = new AeroTornadoEntity(player.level(), player, getRealDamageMult(caster) * fullMPBlastMult, RADIUS[tier], HEIGHT[tier]);

		Vec3 at = lockOnEntity != null ? lockOnEntity.position() : ahead(player);
		tornado.setPos(at.x, at.y, at.z);

		player.level().addFreshEntity(tornado);
		player.swing(InteractionHand.MAIN_HAND);
	}

	private static Vec3 ahead(LivingEntity caster) {
		Vec3 look = caster.getLookAngle();
		Vec3 flat = new Vec3(look.x, 0, look.z);

		if (flat.lengthSqr() < 1.0E-4) {
			flat = new Vec3(0, 0, 1);
		}

		return caster.position().add(flat.normalize().scale(AHEAD));
	}

	@Override
	public void playMagicCastSound(LivingEntity player, Player caster) {
		player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.aero1.get(), SoundSource.PLAYERS, 1F, 1F);
	}
}
