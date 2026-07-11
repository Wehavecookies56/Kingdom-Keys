package online.kingdomkeys.kingdomkeys.item.card;

import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.item.ModItems;

public enum KeycardType {
    BEGINNINGS, GUIDANCE, TRUTH, REWARDS;

    public Item getCardForType() {
        return switch (this) {
            case BEGINNINGS -> ModItems.keyOfBeginnings.get();
            case GUIDANCE -> ModItems.keyOfGuidance.get();
            case TRUTH -> ModItems.keyToTruth.get();
            case REWARDS -> ModItems.keyToRewards.get();
        };
    }
}
