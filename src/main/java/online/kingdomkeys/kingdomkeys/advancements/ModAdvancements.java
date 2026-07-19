package online.kingdomkeys.kingdomkeys.advancements;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.util.function.BiConsumer;

public class ModAdvancements
{
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, KingdomKeys.MODID);

    public static final Id levelUp = registerGeneric("level_up");

    public static final DeferredHolder<CriterionTrigger<?>, KKMunnyTrigger> MUNNY_REACHED = TRIGGERS.register("munny_reached", KKMunnyTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, KKDualWieldTrigger> DUAL_WIELD = TRIGGERS.register("dual_wield", KKDualWieldTrigger::new);

    public static void triggerMunnyReached(ServerPlayer player, int currentMunny) {
        MUNNY_REACHED.get().trigger(player, currentMunny);
    }

    public static void triggerDualWield(ServerPlayer player, net.minecraft.world.item.Item mainHandItem, net.minecraft.world.item.Item offHandItem) {
        DUAL_WIELD.get().trigger(player, mainHandItem, offHandItem);
    }

    public static Id registerGeneric(String name) {
        return new Id(TRIGGERS.register(name, KKLevelUpTrigger::new));
    }


    public record Id(DeferredHolder<CriterionTrigger<?>, KKLevelUpTrigger> holder) {
        public void trigger(ServerPlayer player, int level) {
            holder.value().trigger(player,level);
        }
    }

    /**
     * This is so indirect because we want to have a top-level {@code trigger(T1)}, but we also don't want to have an ugly double-generic
     * on the {@code Id1<T extends CriterionTrigger<?>, T1>}
     */
    public record Id1<T1>(DeferredHolder<CriterionTrigger<?>, ? extends CriterionTrigger<?>> holder, BiConsumer<ServerPlayer, T1> triggerFunction) {
        static <E extends CriterionTrigger<?>, T1> Id1<T1> of(DeferredHolder<CriterionTrigger<?>, E> holder, Function3<E, ServerPlayer, T1> triggerFunction) {
            return new Id1<>(holder, (player, t1) -> triggerFunction.apply(holder.get(), player, t1));
        }

        public void trigger(ServerPlayer player, T1 t1) {
            triggerFunction.accept(player, t1);
        }
    }

    interface Function3<T1, T2, T3> {
        void apply(T1 t1, T2 t2, T3 t3);
    }
}