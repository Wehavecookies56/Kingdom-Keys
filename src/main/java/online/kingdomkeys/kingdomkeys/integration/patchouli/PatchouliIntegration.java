package online.kingdomkeys.kingdomkeys.integration.patchouli;

import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import vazkii.patchouli.api.PatchouliAPI;

public class PatchouliIntegration {

    public static final ResourceLocation BOOK_ID = KingdomKeys.rl("jiminys_journal");

    public static void openJournal() {
        PatchouliAPI.get().openBookGUI(BOOK_ID);
    }
}
