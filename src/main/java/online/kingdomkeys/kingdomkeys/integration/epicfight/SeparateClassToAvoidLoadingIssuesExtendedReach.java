package online.kingdomkeys.kingdomkeys.integration.epicfight;

import net.minecraft.client.Minecraft;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class SeparateClassToAvoidLoadingIssuesExtendedReach {
    private SeparateClassToAvoidLoadingIssuesExtendedReach() {}

    public static boolean isAttacking() {
        if (KingdomKeys.efmLoaded) {
            return Minecraft.getInstance().mouseHandler.isLeftPressed();
        } else {
            return Minecraft.getInstance().options.keyAttack.isDown();
        }
    }

}
