package online.kingdomkeys.kingdomkeys.dialogue;

import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class ModDialogue {

    public static final DeferredRegister<DialogueCondition.Type<?>> DIALOGUE_CONDITIONS = DeferredRegister.create(KingdomKeys.rl("dialogue_conditions"), KingdomKeys.MODID);
    public static final Registry<DialogueCondition.Type<?>> CONDITIONS = DIALOGUE_CONDITIONS.makeRegistry(builder -> builder.sync(true));

    public static final DeferredRegister<DialogueAction.Type<?>> DIALOGUE_ACTIONS = DeferredRegister.create(KingdomKeys.rl("dialogue_actions"), KingdomKeys.MODID);
    public static final Registry<DialogueAction.Type<?>> ACTIONS = DIALOGUE_ACTIONS.makeRegistry(builder -> builder.sync(true));

    public static final DeferredHolder<DialogueCondition.Type<?>, DialogueCondition.Type<DialogueCondition.AtLevel>> AT_LEVEL = DIALOGUE_CONDITIONS.register("level", () -> new DialogueCondition.Type<>(DialogueCondition.AtLevel.CODEC));
    public static final DeferredHolder<DialogueCondition.Type<?>, DialogueCondition.Type<DialogueCondition.InUnion>> IN_UNION = DIALOGUE_CONDITIONS.register("union", () -> new DialogueCondition.Type<>(DialogueCondition.InUnion.CODEC));

    public static final DeferredHolder<DialogueAction.Type<?>, DialogueAction.Type<DialogueAction.OpenShop>> OPEN_SHOP = DIALOGUE_ACTIONS.register("open_shop", () -> new DialogueAction.Type<>(DialogueAction.OpenShop.CODEC));
    public static final DeferredHolder<DialogueAction.Type<?>, DialogueAction.Type<DialogueAction.StartEncounter>> START_ENCOUNTER = DIALOGUE_ACTIONS.register("start_encounter", () -> new DialogueAction.Type<>(DialogueAction.StartEncounter.CODEC));
    public static final DeferredHolder<DialogueAction.Type<?>, DialogueAction.Type<DialogueAction.GiveItem>> GIVE_ITEM = DIALOGUE_ACTIONS.register("give_item", () -> new DialogueAction.Type<>(DialogueAction.GiveItem.CODEC));
    public static final DeferredHolder<DialogueAction.Type<?>, DialogueAction.Type<DialogueAction.Close>> CLOSE = DIALOGUE_ACTIONS.register("close", () -> new DialogueAction.Type<>(DialogueAction.Close.CODEC));
}
