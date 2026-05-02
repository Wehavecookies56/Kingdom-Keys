package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.handler.KeyboardHelper;
import online.kingdomkeys.kingdomkeys.item.MagicSpellItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.Map;

//TODO cleanup + comments
public class ShortcutsGui extends OverlayBase {

	public static final ShortcutsGui INSTANCE = new ShortcutsGui();
	PlayerData playerData;

	private ShortcutsGui() {
		super();
	}

	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		super.render(guiGraphics, deltaTracker);
		if(KeyboardHelper.isScrollActivatorDown() && CommandMenuGui.INSTANCE.currentSubmenu == CommandMenuGui.INSTANCE.root && Minecraft.getInstance().screen == null) {
			playerData = PlayerData.get(minecraft.player);
			int i = 0;
			for (Map.Entry<Integer, Integer> entry : playerData.getShortcutsMap().entrySet()) {
				int slot = entry.getValue();

				ItemStack stack = playerData.getEquippedMagics().get(slot);
				if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof MagicSpellItem spell)) {
					continue;
				}

				String magicId = spell.getMagic();
				int level = spell.getLevel();

				Magic magic = ModMagic.registry.get(ResourceLocation.parse(magicId));

				double cost = magic.getCost(level, minecraft.player);
				int colour = playerData.getMP() > cost ? 0xFFFFFF : 0xFF9900;

				if (playerData.isAbilityEquipped(Strings.extraCast) && cost > playerData.getMP() && playerData.getMP() > 1 && cost < 300) {
					colour = 0xFFFFFF;
				}
				DriveForm form = ModDriveForms.registry.get(ResourceLocation.parse(playerData.getActiveDriveForm()));

				if (playerData.getMaxMP() == 0 || playerData.getRecharge() || (cost > playerData.getMaxMP() && cost < 300) || (cost < 300 && cost >= playerData.getMP() && playerData.isAbilityEquipped(Strings.mpSafety)) || playerData.getMagicCasttimeTicks() > 0 || playerData.getMagicCooldownTicks() > 0 || !form.canUseMagic()) {
					colour = 0x888888;
				}

				drawString(guiGraphics, minecraft.font, Utils.translateToLocal(InputHandler.Keybinds.SCROLL_ACTIVATOR.keybinding.getKey().getName()) + " + " + (entry.getKey() + 1) + ": " + Utils.translateToLocal(magic.getTranslationKey(level)), (int) (5) + ModConfigs.cmTextXOffset, 4 + i * 10, colour);
				i++;
			}
		}

	}

}
