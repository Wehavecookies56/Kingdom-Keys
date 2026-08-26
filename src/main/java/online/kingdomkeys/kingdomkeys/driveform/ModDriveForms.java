package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.KKSupplier;
import online.kingdomkeys.kingdomkeys.lib.Strings;

import java.util.function.Supplier;

public class ModDriveForms {

	public static DeferredRegister<DriveForm> DRIVE_FORMS = DeferredRegister.create(KingdomKeys.rl("driveforms"), KingdomKeys.MODID);
	public static Registry<DriveForm> registry = DRIVE_FORMS.makeRegistry(builder -> builder.sync(true).defaultKey(KingdomKeys.rl("none")));

	public static int order = 0;

	public static final KKSupplier<DriveForm>
		NONE = register(DriveForm.NONE.getPath(), () -> new DriveFormNone(DriveForm.NONE, order++, true)),
		KB2 = register(DriveForm.KB2.getPath(), () -> new DriveFormNone(DriveForm.KB2, order++, true)),
		KB3 = register(DriveForm.KB3.getPath(), () -> new DriveFormNone(DriveForm.KB3, order++, true)),
		SYNCH_BLADE = register(DriveForm.SYNCH_BLADE.getPath(), () -> new DriveFormNone(DriveForm.SYNCH_BLADE, order++, true)),
		VALOR = register(Strings.Form_Valor, () -> new DriveFormValor(KingdomKeys.rl(Strings.Form_Valor), order++, KingdomKeys.rl("textures/models/armor/valor.png"), true, false)),
		WISDOM = register(Strings.Form_Wisdom, () -> new DriveFormWisdom(KingdomKeys.rl(Strings.Form_Wisdom), order++, KingdomKeys.rl("textures/models/armor/wisdom.png"), false, false)),
		LIMIT = register(Strings.Form_Limit, () -> new DriveFormLimit(KingdomKeys.rl(Strings.Form_Limit), order++, KingdomKeys.rl("textures/models/armor/limit.png"), false, false)),
		MASTER = register(Strings.Form_Master, () -> new DriveFormMaster(KingdomKeys.rl(Strings.Form_Master), order++, KingdomKeys.rl("textures/models/armor/master.png"), true, false)),
		FINAL = register(Strings.Form_Final, () -> new DriveFormFinal(KingdomKeys.rl(Strings.Form_Final), order++, KingdomKeys.rl("textures/models/armor/final.png"), true, false)),
		ANTI = register(Strings.Form_Anti, () -> new DriveFormAnti(KingdomKeys.rl(Strings.Form_Anti), order++, KingdomKeys.rl("textures/models/armor/anti.png"), false, false))
		;

	private static KKSupplier<DriveForm> register(String path, Supplier<DriveForm> formSupplier) {
		return new KKSupplier<>(KingdomKeys.rl(DRIVE_FORMS.getNamespace(), path), DRIVE_FORMS.register(path, formSupplier));
	}
}
