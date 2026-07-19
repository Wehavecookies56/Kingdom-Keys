package online.kingdomkeys.kingdomkeys.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

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
