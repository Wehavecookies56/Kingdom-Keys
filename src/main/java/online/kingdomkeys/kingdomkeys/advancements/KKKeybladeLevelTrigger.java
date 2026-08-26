package online.kingdomkeys.kingdomkeys.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

/** Fires whenever a keyblade's level goes up, satisfied once it reaches at least the given level. */
public class KKKeybladeLevelTrigger extends SimpleCriterionTrigger<KKKeybladeLevelTrigger.TriggerInstance> {
	public static final Codec<KKKeybladeLevelTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(
			EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(c -> c.player),
			Codec.intRange(0, 100).fieldOf("level").forGetter(c -> c.level)
	).apply(i, TriggerInstance::new));

	@Override
	public Codec<TriggerInstance> codec() {
		return CODEC;
	}

	public void trigger(ServerPlayer player, int currentLevel) {
		trigger(player, instance -> currentLevel >= instance.level);
	}

	public record TriggerInstance(Optional<ContextAwarePredicate> player, Integer level) implements SimpleInstance {}
}
