package online.kingdomkeys.kingdomkeys.integration.epicfight.init;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.DualChoices;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.KKStyles;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.SingleChoices;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.lib.KKSupplier;
import yesman.epicfight.api.ex_cap.provider.ProviderConditional;
import yesman.epicfight.registry.deferred.ProviderConditionalRegister;
import yesman.epicfight.registry.deferred.holders.DeferredConditional;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class KKProviderConditionals  {
    public static final ProviderConditionalRegister CONDITIONALS = ProviderConditionalRegister.create(KingdomKeys.MODID);

    private static Player playerOf(LivingEntityPatch<?> livingEntityPatch) {
        return livingEntityPatch != null && livingEntityPatch.getOriginal() instanceof Player player ? player : null;
    }

    private static boolean inForm(LivingEntityPatch<?> livingEntityPatch, KKSupplier<DriveForm> form) {
        Player player = playerOf(livingEntityPatch);

        if (player == null) {
            return false;
        }

        PlayerData data = PlayerData.get(player);
        return data != null && data.isFormActive(form) && armed(player);
    }

    // Single wield style (with no form up and nothing in the off-hand)
    private static boolean inSingleStyle(LivingEntityPatch<?> livingEntityPatch, SingleChoices choice) {
        Player player = playerOf(livingEntityPatch);

        if (player == null) {
            return false;
        }

        PlayerData data = PlayerData.get(player);

        return data != null && data.getSingleStyle() == choice && !(player.getOffhandItem().getItem() instanceof KeybladeItem) && data.noFormActive();
    }

    // Dual wield, needs offhand holding a keyblade
    private static boolean inDualStyle(LivingEntityPatch<?> livingEntityPatch, DualChoices choice) {
        Player player = playerOf(livingEntityPatch);

        if (player == null) {
            return false;
        }

        PlayerData data = PlayerData.get(player);

        return data != null && data.getDualStyle() == choice && player.getOffhandItem().getItem() instanceof KeybladeItem && data.noFormActive();
    }

    private static boolean armed(Player player) {
        return isWeapon(player.getMainHandItem()) || isWeapon(player.getOffhandItem());
    }

    private static boolean isWeapon(ItemStack stack) {
        return stack.getItem() instanceof KeybladeItem || stack.getItem() instanceof IOrgWeapon;
    }

    public static final DeferredConditional FINAL_FORM_STYLE = CONDITIONALS.registerConditional("final_form_style", () ->
            ProviderConditional.createCustom(KKStyles.FINAL_FORM, livingEntityPatch -> inForm(livingEntityPatch, ModDriveForms.FINAL), true)
    );

    public static final DeferredConditional MASTER_FORM_STYLE = CONDITIONALS.registerConditional("master_form_style", () ->
            ProviderConditional.createCustom(KKStyles.MASTER_FORM, livingEntityPatch -> inForm(livingEntityPatch, ModDriveForms.MASTER), true)
    );

    public static final DeferredConditional LIMIT_FORM_STYLE = CONDITIONALS.registerConditional("limit_form_style", () ->
            ProviderConditional.createCustom(KKStyles.LIMIT_FORM, livingEntityPatch -> inForm(livingEntityPatch, ModDriveForms.LIMIT), true)
    );

    public static final DeferredConditional WISDOM_FORM_STYLE = CONDITIONALS.registerConditional("wisdom_form_style", () ->
            ProviderConditional.createCustom(KKStyles.WISDOM_FORM, livingEntityPatch -> inForm(livingEntityPatch, ModDriveForms.WISDOM), true)
    );

    public static final DeferredConditional VALOR_FORM_STYLE = CONDITIONALS.registerConditional("valor_form_style", () ->
            ProviderConditional.createCustom(KKStyles.VALOR_FORM, livingEntityPatch -> inForm(livingEntityPatch, ModDriveForms.VALOR), true)
    );

    public static final DeferredConditional ANTI_FORM_STYLE = CONDITIONALS.registerConditional("anti_form_style", () ->
            ProviderConditional.createCustom(KKStyles.ANTI_FORM, livingEntityPatch -> {
                Player player = playerOf(livingEntityPatch);
                PlayerData data = player == null ? null : PlayerData.get(player);
                return data != null && data.isFormActive(ModDriveForms.ANTI);
            }, true)
    );

    public static final DeferredConditional SORA_STYLE = CONDITIONALS.registerConditional("sora_style", () ->
            ProviderConditional.createCustom(KKStyles.SORA,
            livingEntityPatch -> inSingleStyle(livingEntityPatch, SingleChoices.SORA), true)
    );

    public static final DeferredConditional RIKU_STYLE = CONDITIONALS.registerConditional("riku_style", () ->
            ProviderConditional.createCustom(KKStyles.RIKU,
            livingEntityPatch -> inSingleStyle(livingEntityPatch, SingleChoices.RIKU), true)
    );

    public static final DeferredConditional ROXAS_STYLE = CONDITIONALS.registerConditional("roxas_style", () ->
            ProviderConditional.createCustom(KKStyles.ROXAS, livingEntityPatch -> inSingleStyle(livingEntityPatch, SingleChoices.ROXAS), true)
    );

    public static final DeferredConditional AQUA_STYLE = CONDITIONALS.registerConditional("aqua_style", () ->
            ProviderConditional.createCustom(KKStyles.AQUA, livingEntityPatch -> inSingleStyle(livingEntityPatch, SingleChoices.AQUA), true)
    );

    public static final DeferredConditional TERRA_STYLE = CONDITIONALS.registerConditional("terra_style", () ->
            ProviderConditional.createCustom(KKStyles.TERRA, livingEntityPatch -> inSingleStyle(livingEntityPatch, SingleChoices.TERRA), true)
    );

    public static final DeferredConditional VENTUS_STYLE = CONDITIONALS.registerConditional("ventus_style", () ->
            ProviderConditional.createCustom(KKStyles.VENTUS, livingEntityPatch -> inSingleStyle(livingEntityPatch, SingleChoices.VENTUS), true)
    );

    public static final DeferredConditional KH2_ROXAS_DUAL_STYLE = CONDITIONALS.registerConditional("kh2_roxas_dual_style", () ->
            ProviderConditional.createCustom(KKStyles.KH2_ROXAS_DUAL, livingEntityPatch -> inDualStyle(livingEntityPatch, DualChoices.KH2_ROXAS_DUAL), true)
    );

    public static final DeferredConditional DAYS_ROXAS_DUAL_STYLE = CONDITIONALS.registerConditional("days_roxas_dual_style", () ->
            ProviderConditional.createCustom(KKStyles.DAYS_ROXAS_DUAL, livingEntityPatch -> inDualStyle(livingEntityPatch, DualChoices.DAYS_ROXAS_DUAL), true)
    );
}
