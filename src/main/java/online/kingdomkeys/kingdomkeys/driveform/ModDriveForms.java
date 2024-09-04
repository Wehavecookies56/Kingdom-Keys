package online.kingdomkeys.kingdomkeys.driveform;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class ModDriveForms {

	public static DeferredRegister<DriveForm> DRIVE_FORMS = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "driveforms"), KingdomKeys.MODID);
	public static Registry<DriveForm> registry = DRIVE_FORMS.makeRegistry(builder -> builder.sync(true).defaultKey(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "empty")));

	public static int order = 0;

	public static final Supplier<DriveForm>
		NONE = DRIVE_FORMS.register(DriveForm.NONE.getPath(), () -> new DriveFormNone(DriveForm.NONE, order++, true)),
		SYNCH_BLADE = DRIVE_FORMS.register(DriveForm.SYNCH_BLADE.getPath(), () -> new DriveFormNone(DriveForm.SYNCH_BLADE, order++, true)),
		VALOR = DRIVE_FORMS.register(Strings.DF_Prefix + "valor", () -> new DriveFormValor(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, Strings.DF_Prefix + "valor"), order++, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/models/armor/valor.png"), true, false)),
		WISDOM = DRIVE_FORMS.register(Strings.DF_Prefix + "wisdom", () -> new DriveFormWisdom(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, Strings.DF_Prefix + "wisdom"), order++, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/models/armor/wisdom.png"), false, false)),
		LIMIT = DRIVE_FORMS.register(Strings.DF_Prefix + "limit", () -> new DriveFormLimit(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, Strings.DF_Prefix + "limit"), order++, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/models/armor/limit.png"), false, false)),
		MASTER = DRIVE_FORMS.register(Strings.DF_Prefix + "master", () -> new DriveFormMaster(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, Strings.DF_Prefix + "master"), order++, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/models/armor/master.png"), true, false)),
		FINAL = DRIVE_FORMS.register(Strings.DF_Prefix + "final", () -> new DriveFormFinal(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, Strings.DF_Prefix + "final"), order++, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/models/armor/final.png"), true, false)),
		ANTI = DRIVE_FORMS.register(Strings.DF_Prefix + "anti", () -> new DriveFormAnti(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, Strings.DF_Prefix + "anti"), order++, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/models/armor/anti.png"), false, false))
		;
}
