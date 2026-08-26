package online.kingdomkeys.kingdomkeys.integration.epicfight;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class EpicFightUtils {
    private EpicFightUtils() {}

    public static boolean isAttacking() {
        if (KingdomKeys.efmLoaded) {
            return Minecraft.getInstance().mouseHandler.isLeftPressed() && Minecraft.getInstance().screen == null;
        } else {
            return Minecraft.getInstance().options.keyAttack.isDown();
        }
    }

    /** Whether the player has Epic Fight's combat stance up, in which case its own guard is in charge */
    public static boolean isInEpicFightMode(Player player) {
        if (!KingdomKeys.efmLoaded) {
            return false;
        }

        PlayerPatch patch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
        return patch != null && patch.isEpicFightMode();
    }

    public static float getCritMulti(Player player) {
        PlayerData data = PlayerData.get(player);
        float critBoost = data.getNumberOfAbilitiesEquipped(ModAbilities.CRITICAL_BOOST) * 0.1f;

        boolean isEpicFightMode = false;

        if (KingdomKeys.efmLoaded) {
            PlayerPatch patch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
            isEpicFightMode = patch != null && patch.isEpicFightMode();
        }

        if (isEpicFightMode) {
            if (player.getRandom().nextFloat() < critBoost) {
                return (float) ModConfigs.critMult;
            }
            return 1f;
        } else {
            if (Utils.isVanillaCrit(player)) {
                float base = (float) ModConfigs.critMult;
                return base * (1 + critBoost);
            }
        }

        return 1f;
    }

    public static boolean isPlayerSummoning(LivingEntityPatch<?> playerPatch) {
        return Minecraft.getInstance().player.getId() == playerPatch.getOriginal().getId();
    }

    public static void refreshLivingMotions(Player player) {
        if (!KingdomKeys.efmLoaded || player == null || player.level().isClientSide) {
            return;
        }

        ServerPlayerPatch patch = EpicFightCapabilities.getEntityPatch(player, ServerPlayerPatch.class);
        if (patch != null) {
            patch.modifyLivingMotionByCurrentItem();
        }
    }
}