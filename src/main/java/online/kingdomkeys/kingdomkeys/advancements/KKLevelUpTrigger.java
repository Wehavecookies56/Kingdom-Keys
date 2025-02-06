package online.kingdomkeys.kingdomkeys.advancements;

import com.mojang.serialization.Codec;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class KKLevelUpTrigger extends SimpleCriterionTrigger<KKLevelUpTrigger.TriggerInstance> {
		public static final Codec<KKLevelUpTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(c -> c.player),
				Codec.intRange(0,1000).fieldOf("level").forGetter(c -> c.level)
		).apply(i, TriggerInstance::new));

		@Override
		public Codec<TriggerInstance> codec() {
			return CODEC;
		}

		public void trigger(ServerPlayer player, int level) {
			trigger(player, instance -> instance.level == level);
		}

		record TriggerInstance(Optional<ContextAwarePredicate> player, Integer level) implements SimpleInstance {}
	}