package online.kingdomkeys.kingdomkeys.integration.epicfight;

import net.minecraft.client.Minecraft;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class EpicFightUtils {
    private EpicFightUtils() {}

    public static boolean isAttacking() {
        if (KingdomKeys.efmLoaded) {
            return Minecraft.getInstance().mouseHandler.isLeftPressed();
        } else {
            return Minecraft.getInstance().options.keyAttack.isDown();
        }
    }

    public static boolean isPlayerSummoning(LivingEntityPatch<?> playerPatch) {
        return Minecraft.getInstance().player.getId() == playerPatch.getOriginal().getId();
    }
}