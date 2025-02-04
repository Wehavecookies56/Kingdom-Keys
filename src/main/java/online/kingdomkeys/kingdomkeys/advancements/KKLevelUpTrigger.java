package online.kingdomkeys.kingdomkeys.advancements;

import com.mojang.serialization.Codec;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;
import java.util.function.Predicate;

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
			//TODO trigger based on level
			System.out.println("Player: "+player);
			System.out.println("Level up detected "+level);

			Predicate<TriggerInstance> test = instance -> {
				System.out.println("Leveled up to " + level);
				System.out.println(instance.level);
				return instance.level == level;
			};
			trigger(player, test);
		}

		record TriggerInstance(Optional<ContextAwarePredicate> player, Integer level) implements SimpleInstance {}
	}