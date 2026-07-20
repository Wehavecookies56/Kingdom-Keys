package online.kingdomkeys.kingdomkeys.integration.epicfight.init;

import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.DualChoices;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.KKStyles;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.SingleChoices;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import yesman.epicfight.api.ex_cap.provider.ProviderConditional;
import yesman.epicfight.registry.deferred.ProviderConditionalRegister;
import yesman.epicfight.registry.deferred.holders.DeferredConditional;

import java.util.Objects;

public class KKProviderConditionals  {
    public static final ProviderConditionalRegister CONDITIONALS = ProviderConditionalRegister.create(KingdomKeys.MODID);

    public static final DeferredConditional FINAL_FORM_STYLE = CONDITIONALS.registerConditional(
            "final_form_style",
            () -> ProviderConditional.createCustom(KKStyles.FINAL_FORM,
                (livingEntityPatch) ->
                        PlayerData.get((Player)
                                livingEntityPatch.getOriginal()).getActiveDriveForm().equals(Strings.Form_Final)
                , true)
    );
    public static final DeferredConditional MASTER_FORM_STYLE = CONDITIONALS.registerConditional(
            "master_form_style",
            () ->  ProviderConditional.createCustom(KKStyles.MASTER_FORM,
                (livingEntityPatch) ->
                        PlayerData.get((Player)
                                livingEntityPatch.getOriginal()).getActiveDriveForm().equals(Strings.Form_Master) &&
                                livingEntityPatch.getOriginal().getOffhandItem().getItem() instanceof KeybladeItem
                , true)

    );

    public static final DeferredConditional WISDOM_FORM_STYLE = CONDITIONALS.registerConditional(
            "wisdom_form_style",
            () -> ProviderConditional.createCustom(KKStyles.WISDOM_FORM,
                (livingEntityPatch) ->
                        PlayerData.get((Player)
                                livingEntityPatch.getOriginal()).getActiveDriveForm().equals(Strings.Form_Wisdom)
                , true)
    );

    public static final DeferredConditional VALOR_FORM_STYLE = CONDITIONALS.registerConditional(
            "valor_form_style",
            () ->  ProviderConditional.createCustom(KKStyles.VALOR_FORM,
                (livingEntityPatch) ->
                        PlayerData.get((Player)
                                livingEntityPatch.getOriginal()).getActiveDriveForm().equals(Strings.Form_Valor) &&
                                livingEntityPatch.getOriginal().getOffhandItem().getItem() instanceof KeybladeItem
                , true)
    );

    public static final DeferredConditional ANTI_FORM_STYLE = CONDITIONALS.registerConditional(
            "anti_form_style",
            () ->
                 ProviderConditional.createCustom(KKStyles.ANTI_FORM,
                (livingEntityPatch) ->
                        PlayerData.get((Player)
                                livingEntityPatch.getOriginal()).getActiveDriveForm().equals(Strings.Form_Anti)
                , true)
    );

    public static final DeferredConditional SORA_STYLE = CONDITIONALS.registerConditional(
            "sora_style",
            () ->
                    ProviderConditional.createCustom(KKStyles.SORA,
                            (livingEntityPatch) ->
                                PlayerData.get((Player) livingEntityPatch.getOriginal()).getSingleStyle() == SingleChoices.SORA &&
                                        !(livingEntityPatch.getOriginal().getOffhandItem().getItem() instanceof KeybladeItem) &&
                                        PlayerData.get((Player) livingEntityPatch.getOriginal()).getActiveDriveForm().equals(DriveForm.NONE.toString())
                            , true)
    );

    public static final DeferredConditional RIKU_STYLE = CONDITIONALS.registerConditional(
            "riku_style",
            () ->
                    ProviderConditional.createCustom(KKStyles.RIKU,
                            (livingEntityPatch) ->
                                    PlayerData.get((Player) livingEntityPatch.getOriginal()).getSingleStyle() == SingleChoices.RIKU &&
                                            !(livingEntityPatch.getOriginal().getOffhandItem().getItem() instanceof KeybladeItem) &&
                                            PlayerData.get((Player) livingEntityPatch.getOriginal()).getActiveDriveForm() == DriveForm.NONE.toString()
                            , true)
    );
    public static final DeferredConditional ROXAS_STYLE = CONDITIONALS.registerConditional(
            "roxas_style",
            () ->
                    ProviderConditional.createCustom(KKStyles.ROXAS,
                            (livingEntityPatch) ->
                                    PlayerData.get((Player) livingEntityPatch.getOriginal()).getSingleStyle() == SingleChoices.ROXAS &&
                                            !(livingEntityPatch.getOriginal().getOffhandItem().getItem() instanceof KeybladeItem) &&
                                            PlayerData.get((Player) livingEntityPatch.getOriginal()).getActiveDriveForm().equals(DriveForm.NONE.toString())
                            , true)
    );
    public static final DeferredConditional AQUA_STYLE = CONDITIONALS.registerConditional(
            "aqua_style",
            () ->
                    ProviderConditional.createCustom(KKStyles.AQUA,
                            (livingEntityPatch) ->
                                    PlayerData.get((Player) livingEntityPatch.getOriginal()).getSingleStyle() == SingleChoices.AQUA &&
                                            !(livingEntityPatch.getOriginal().getOffhandItem().getItem() instanceof KeybladeItem) &&
                                            PlayerData.get((Player) livingEntityPatch.getOriginal()).getActiveDriveForm().equals(DriveForm.NONE.toString())
                            , true)
    );
    public static final DeferredConditional TERRA_STYLE = CONDITIONALS.registerConditional(
            "terra_style",
            () ->
                    ProviderConditional.createCustom(KKStyles.TERRA,
                            (livingEntityPatch) ->
                                    PlayerData.get((Player) livingEntityPatch.getOriginal()).getSingleStyle() == SingleChoices.TERRA &&
                                            !(livingEntityPatch.getOriginal().getOffhandItem().getItem() instanceof KeybladeItem) &&
                                            PlayerData.get((Player) livingEntityPatch.getOriginal()).getActiveDriveForm().equals(DriveForm.NONE.toString())
                            , true)
    );
    public static final DeferredConditional VENTUS_STYLE = CONDITIONALS.registerConditional(
            "ventus_style",
            () ->
                    ProviderConditional.createCustom(KKStyles.VENTUS,
                            (livingEntityPatch) ->
                                    PlayerData.get((Player) livingEntityPatch.getOriginal()).getSingleStyle() == SingleChoices.VENTUS &&
                                            !(livingEntityPatch.getOriginal().getOffhandItem().getItem() instanceof KeybladeItem) &&
                                            PlayerData.get((Player) livingEntityPatch.getOriginal()).getActiveDriveForm().equals(DriveForm.NONE.toString())
                            , true)
    );

    public static final DeferredConditional KH2_ROXAS_DUAL_STYLE = CONDITIONALS.registerConditional(
            "kh2_roxas_dual_style",
            () ->
                    ProviderConditional.createCustom(KKStyles.KH2_ROXAS_DUAL,
                            (livingEntityPatch) ->
                                    PlayerData.get((Player) livingEntityPatch.getOriginal()).getDualStyle() == DualChoices.KH2_ROXAS_DUAL &&
                                            (livingEntityPatch.getOriginal().getOffhandItem().getItem() instanceof KeybladeItem) &&
                                            PlayerData.get((Player) livingEntityPatch.getOriginal()).getActiveDriveForm().equals(DriveForm.NONE.toString())
                            , true)
    );

    public static final DeferredConditional DAYS_ROXAS_DUAL_STYLE = CONDITIONALS.registerConditional(
            "days_roxas_dual_style",
            () ->
                    ProviderConditional.createCustom(KKStyles.DAYS_ROXAS_DUAL,
                            (livingEntityPatch) ->
                                    PlayerData.get((Player) livingEntityPatch.getOriginal()).getDualStyle() == DualChoices.DAYS_ROXAS_DUAL &&
                                            (livingEntityPatch.getOriginal().getOffhandItem().getItem() instanceof KeybladeItem) &&
                                            PlayerData.get((Player) livingEntityPatch.getOriginal()).getActiveDriveForm().equals(DriveForm.NONE.toString())
                            , true)
    );


}
