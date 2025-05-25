package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.handler.KeyboardHelper;
import online.kingdomkeys.kingdomkeys.lib.Constants;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.Map;

//TODO cleanup + comments
public class ShortcutsGui extends OverlayBase {
	public static final ShortcutsGui INSTANCE = new ShortcutsGui();
	IPlayerCapabilities playerData;

	private ShortcutsGui() {
		super();
	}

	@Override
	public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int width, int height) {
		super.render(gui, guiGraphics, partialTick, width, height);
		Player player = minecraft.player;

		if(KeyboardHelper.isScrollActivatorDown() && CommandMenuGui.INSTANCE.currentSubmenu == CommandMenuGui.INSTANCE.root) {
			playerData = ModCapabilities.getPlayer(minecraft.player);
			int i = 0;
			for (Map.Entry<Integer, String> entry : playerData.getShortcutsMap().entrySet()) {
				String[] data = entry.getValue().split(",");
				Magic magic = ModMagic.registry.get().getValue(new ResourceLocation(data[0]));
				double cost = magic.getCost(Integer.parseInt(data[1]), minecraft.player);
				int colour = playerData.getMP() > cost ? 0xFFFFFF : 0xFF9900;

				if (playerData.isAbilityEquipped(Strings.extraCast) && cost > playerData.getMP() && playerData.getMP() > 1 && cost < 300) {
					colour = 0xFFFFFF;
				}
				DriveForm form = ModDriveForms.registry.get().getValue(ResourceLocation.parse(playerData.getActiveDriveForm()));

				if (playerData.getMaxMP() == 0 || playerData.getRecharge() || cost > playerData.getMaxMP() && cost < 300 || cost < 300 && cost >= playerData.getMP() && playerData.isAbilityEquipped(Strings.mpSafety) || playerData.getMagicCasttimeTicks() > 0 || playerData.getMagicCooldownTicks() > 0 || !form.canUseMagic()) {
					colour = 0x888888;
				}

				drawString(guiGraphics, minecraft.font, Utils.translateToLocal(InputHandler.Keybinds.SCROLL_ACTIVATOR.keybinding.getKey().getName()) + " + " + (entry.getKey() + 1) + ": " + Utils.translateToLocal(magic.getTranslationKey(Integer.parseInt(data[1]))), (int) (5 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset, 4 + i * 10, colour);
				i++;
			}
		}

	}
}
