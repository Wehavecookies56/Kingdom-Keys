package online.kingdomkeys.kingdomkeys.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;

import java.util.Optional;

/**
 * Fires whenever a player's held items change (main hand or off hand), and is satisfied if the two
 * items given in the advancement JSON ({@code item_a}/{@code item_b}) are currently held one in each
 * hand, in either order (e.g. Oblivion in one hand and Oathkeeper in the other, regardless of which
 * hand holds which).
 */
public class KKDualWieldTrigger extends SimpleCriterionTrigger<KKDualWieldTrigger.TriggerInstance> {
	public static final Codec<KKDualWieldTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(
			EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(c -> c.player),
			BuiltInRegistries.ITEM.byNameCodec().fieldOf("item_a").forGetter(c -> c.itemA),
			BuiltInRegistries.ITEM.byNameCodec().fieldOf("item_b").forGetter(c -> c.itemB)
	).apply(i, TriggerInstance::new));

	@Override
	public Codec<TriggerInstance> codec() {
		return CODEC;
	}

	public void trigger(ServerPlayer player, Item mainHandItem, Item offHandItem) {
		trigger(player, instance ->
				(instance.itemA == mainHandItem && instance.itemB == offHandItem) ||
				(instance.itemA == offHandItem && instance.itemB == mainHandItem)
		);
	}

	public record TriggerInstance(Optional<ContextAwarePredicate> player, Item itemA, Item itemB) implements SimpleInstance {}
}
