package online.kingdomkeys.kingdomkeys.data;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.util.function.Supplier;

public class ModData {

	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, KingdomKeys.MODID);
	public static final Supplier<AttachmentType<PlayerData>> PLAYER_DATA = ATTACHMENT_TYPES.register("drive", () -> AttachmentType.serializable(PlayerData::new).copyOnDeath().build());
	public static final Supplier<AttachmentType<GlobalData>> GLOBAL_DATA = ATTACHMENT_TYPES.register("global", () -> AttachmentType.serializable(GlobalData::new).copyOnDeath().build());

}
