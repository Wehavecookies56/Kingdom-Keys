package online.kingdomkeys.kingdomkeys.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.util.Optional;

public class KKAllAdvancementsTrigger extends SimpleCriterionTrigger<KKAllAdvancementsTrigger.TriggerInstance> {

	// The advancement this trigger completes. It cannot require itself.
	public static final ResourceLocation ADVANCEMENT_ID = KingdomKeys.rl("all_advancements");

	public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(c -> c.player)).apply(i, TriggerInstance::new));

	@Override
	public Codec<TriggerInstance> codec() {
		return CODEC;
	}

	public void trigger(ServerPlayer player) {
		trigger(player, instance -> true);
	}

	public static void checkCompletion(ServerPlayer player) {
		for (AdvancementHolder holder : player.server.getAdvancements().getAllAdvancements()) {
			if (!holder.id().getNamespace().equals(KingdomKeys.MODID) || holder.id().equals(ADVANCEMENT_ID)) {
				continue;
			}

			if (holder.value().display().isEmpty()) {
				continue;
			}

			if (!player.getAdvancements().getOrStartProgress(holder).isDone()) {
				return;
			}
		}

		ModAdvancements.triggerAllAdvancements(player);
	}

	public record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleInstance {}
}
