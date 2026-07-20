package online.kingdomkeys.kingdomkeys.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

/**
 * Fires whenever the player crafts a player head (skull) whose profile name matches the {@code name}
 * given in the advancement JSON. Generic/reusable so any number of "craft a tribute head" advancements
 * can be added later just by writing a new JSON file, without touching Java.
 */
public class KKCraftProfileHeadTrigger extends SimpleCriterionTrigger<KKCraftProfileHeadTrigger.TriggerInstance> {
	public static final Codec<KKCraftProfileHeadTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(
			EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(c -> c.player),
			Codec.STRING.fieldOf("name").forGetter(c -> c.name)
	).apply(i, TriggerInstance::new));

	@Override
	public Codec<TriggerInstance> codec() {
		return CODEC;
	}

	public void trigger(ServerPlayer player, String craftedProfileName) {
		trigger(player, instance -> instance.name.equalsIgnoreCase(craftedProfileName));
	}

	public record TriggerInstance(Optional<ContextAwarePredicate> player, String name) implements SimpleInstance {}
}
