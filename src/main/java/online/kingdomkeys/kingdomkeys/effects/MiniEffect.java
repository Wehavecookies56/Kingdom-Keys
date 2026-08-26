package online.kingdomkeys.kingdomkeys.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class MiniEffect extends MobEffect {

	public MiniEffect(MobEffectCategory category, int color) {
		super(category, color);
		addAttributeModifier(Attributes.SCALE, KingdomKeys.rl("mini_scale"), -0.6D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
		addAttributeModifier(Attributes.MOVEMENT_SPEED, KingdomKeys.rl("mini_speed"), -0.3D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return false;
	}
}