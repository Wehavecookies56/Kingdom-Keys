package online.kingdomkeys.kingdomkeys.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

/**
 * Fires whenever a player's munny total changes, and is satisfied once that amount reaches at least
 * the {@code amount} specified in the advancement JSON (e.g. reach 1,000,000 munny). Uses {@code >=}
 * rather than an exact match, since munny can jump past the threshold in a single transaction
 * (selling several items at once, a shop refund, etc).
 */
public class KKMunnyTrigger extends SimpleCriterionTrigger<KKMunnyTrigger.TriggerInstance> {
	public static final Codec<KKMunnyTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(
			EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(c -> c.player),
			Codec.intRange(0, Integer.MAX_VALUE).fieldOf("amount").forGetter(c -> c.amount)
	).apply(i, TriggerInstance::new));

	@Override
	public Codec<TriggerInstance> codec() {
		return CODEC;
	}

	public void trigger(ServerPlayer player, int currentMunny) {
		trigger(player, instance -> currentMunny >= instance.amount);
	}

	public record TriggerInstance(Optional<ContextAwarePredicate> player, Integer amount) implements SimpleInstance {}
}
