package online.kingdomkeys.kingdomkeys.advancements;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Optional;

	public class KKLevelUpTrigger extends SimpleCriterionTrigger<KKLevelUpTrigger.TriggerInstance> {
		@Override
		public Codec<KKLevelUpTrigger.TriggerInstance> codec() {
			return KKLevelUpTrigger.TriggerInstance.CODEC;
		}

		public void trigger(ServerPlayer pPlayer, ItemStack pItem) {
			this.trigger(pPlayer, p_23687_ -> p_23687_.matches(pItem));
		}

		public static record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item) implements SimpleCriterionTrigger.SimpleInstance {
			public static final Codec<KKLevelUpTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(
					p_337348_ -> p_337348_.group(
									EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(KKLevelUpTrigger.TriggerInstance::player),
									ItemPredicate.CODEC.optionalFieldOf("item").forGetter(KKLevelUpTrigger.TriggerInstance::item)
							)
							.apply(p_337348_, KKLevelUpTrigger.TriggerInstance::new)
			);

			public static Criterion<net.minecraft.advancements.critereon.ConsumeItemTrigger.TriggerInstance> usedItem() {
				return CriteriaTriggers.CONSUME_ITEM.createCriterion(new net.minecraft.advancements.critereon.ConsumeItemTrigger.TriggerInstance(Optional.empty(), Optional.empty()));
			}

			public static Criterion<net.minecraft.advancements.critereon.ConsumeItemTrigger.TriggerInstance> usedItem(ItemLike pItem) {
				return usedItem(ItemPredicate.Builder.item().of(pItem.asItem()));
			}

			public static Criterion<net.minecraft.advancements.critereon.ConsumeItemTrigger.TriggerInstance> usedItem(ItemPredicate.Builder pItem) {
				return CriteriaTriggers.CONSUME_ITEM.createCriterion(new net.minecraft.advancements.critereon.ConsumeItemTrigger.TriggerInstance(Optional.empty(), Optional.of(pItem.build())));
			}

			public boolean matches(ItemStack pItem) {
				return this.item.isEmpty() || this.item.get().test(pItem);
			}
		}
	}
