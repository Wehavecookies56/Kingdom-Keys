package online.kingdomkeys.kingdomkeys.limit;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

public class LimitPowerup extends Limit {

	private static final int DURATION_TICKS = 400; // 20s
	private static final int AMPLIFIER = 4; // Strength V

	public LimitPowerup(ResourceLocation registryName, int order, OrgMember owner) {
		super(registryName, order, owner);
	}

	@Override
	public void onUse(Player player, LivingEntity target) {
		super.onUse(player, target);

		int duration = Math.round(DURATION_TICKS * getLimitData().getDmgMult());
		player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, duration, AMPLIFIER, false, true, true));

		player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1F, 0.6F);
	}
}
