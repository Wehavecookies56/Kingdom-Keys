package online.kingdomkeys.kingdomkeys.driveform;

import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class ModDriveForms {

	public static DeferredRegister<DriveForm> DRIVE_FORMS = DeferredRegister.create(new ResourceLocation(KingdomKeys.MODID, "driveforms"), KingdomKeys.MODID);

	public static Supplier<IForgeRegistry<DriveForm>> registry = DRIVE_FORMS.makeRegistry(RegistryBuilder::new);

	public static int order = 0;

	public static final RegistryObject<DriveForm>
			NONE = DRIVE_FORMS.register(DriveForm.NONE.getPath(), () -> new DriveFormNone(DriveForm.NONE.toString(), order++, true)),
			SYNCH_BLADE = DRIVE_FORMS.register(DriveForm.SYNCH_BLADE.getPath(), () -> new DriveFormNone(DriveForm.SYNCH_BLADE.toString(), order++, true)),
			VALOR = DRIVE_FORMS.register(Strings.DF_Prefix + "valor", () -> new DriveFormValor(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "valor",order++, new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/valor.png"), true)),
			WISDOM = DRIVE_FORMS.register(Strings.DF_Prefix + "wisdom", () -> new DriveFormWisdom(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "wisdom",order++, new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/wisdom.png"), false)),
			LIMIT = DRIVE_FORMS.register(Strings.DF_Prefix + "limit", () -> new DriveFormLimit(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "limit",order++, new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/limit.png"), false)),
			MASTER = DRIVE_FORMS.register(Strings.DF_Prefix + "master", () -> new DriveFormMaster(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "master",order++, new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/master.png"), true)),
			FINAL = DRIVE_FORMS.register(Strings.DF_Prefix + "final", () -> new DriveFormFinal(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "final",order++, new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/final.png"), true)),
			ANTI = DRIVE_FORMS.register(Strings.DF_Prefix + "anti", () -> new DriveFormAnti(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "anti", order++, new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/anti.png"), false))
			;
}
