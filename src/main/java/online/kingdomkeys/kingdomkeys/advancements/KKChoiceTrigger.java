package online.kingdomkeys.kingdomkeys.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

/**
 * Fires when the player finalises their Station of Awakening choice (sword/shield/staff).
 * Doesn't care which one was picked - just that a real choice (not NONE, i.e. not a reset) was made.
 */
public class KKChoiceTrigger extends SimpleCriterionTrigger<KKChoiceTrigger.TriggerInstance> {
	public static final Codec<KKChoiceTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(
			EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(c -> c.player)
	).apply(i, TriggerInstance::new));

	@Override
	public Codec<TriggerInstance> codec() {
		return CODEC;
	}

	public void trigger(ServerPlayer player) {
		trigger(player, instance -> true);
	}

	public record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleInstance {}
}
