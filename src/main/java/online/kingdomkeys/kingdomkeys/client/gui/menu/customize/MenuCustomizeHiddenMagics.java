package online.kingdomkeys.kingdomkeys.client.gui.menu.customize;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.MagicSpellItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MenuCustomizeHiddenMagics extends MenuBackground {

	public MenuCustomizeHiddenMagics() {
		super(Strings.Gui_Menu_Customize_Magic, new Color(0, 0, 255));
	}

	@Override
	public void init() {
		super.init();
		Player player = Minecraft.getInstance().player;

		if (player == null)
			return;

		playerData = PlayerData.get(player);
		Map<Integer, ItemStack> magics = playerData.getEquippedMagics();

		int startY = (int) topBarHeight + 10;

		int rightX = (int)(buttonPosX) + width / 2 + (5 / 2);
		int leftX = rightX - (int)(width * 0.2F) - 20;


		int index = 0;

		for (Map.Entry<Integer, ItemStack> entry : magics.entrySet()) {
			int slot = entry.getKey();

			if (slot >= playerData.getMaxMagics())
				continue;

			ItemStack stack = entry.getValue();
			String magicName = "---";

			if(!stack.isEmpty() && stack.getItem() instanceof MagicSpellItem spell) {
				Magic magicInstance = ModMagic.registry.get(spell.getMagic());
				magicName = Utils.translateToLocal(magicInstance.getTranslationKey());
			}

			int column = index % 2;
			int row = index / 2;
			int x = column == 0 ? leftX : rightX;
			int y = startY + (row * 19); //22

			MenuButton button = new MenuButton(x, y, (int) (width * 0.2F), getButtonText(slot, magicName), MenuButton.ButtonType.ROUNDBUTTON, b -> toggleMagic((MenuButton) b));
			button.setData(slot + ";" + magicName);
			addRenderableWidget(button);

			index++;
		}

		addRenderableWidget(new MenuButton((int) buttonPosX, buttonPosY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), MenuButton.ButtonType.BUTTON, (e) -> Minecraft.getInstance().setScreen(new MenuCustomizeScreen())));
	}

	private void toggleMagic(MenuButton button) {
		String[] data = button.getData().split(";");

		int slot = Integer.parseInt(data[0]);
		String magicName = data[1];

		boolean hidden = !isMagicSlotHidden(slot);
		setMagicSlotHidden(slot, hidden);
		button.setMessage(Component.literal(getButtonText(slot, magicName)));
	}

	private String getButtonText(int slot, String magicName) {
		boolean hidden = isMagicSlotHidden(slot);
		String state = hidden ? ChatFormatting.RED + "✖" : ChatFormatting.GREEN + "✔";
		return ChatFormatting.GRAY + "[" + state + ChatFormatting.GRAY + "] " + ChatFormatting.YELLOW + (slot + 1) + ". " + ChatFormatting.WHITE + magicName;
	}

	private boolean isMagicSlotHidden(int slot) {
		return ModConfigs.hiddenMagic.contains(slot);
	}

	private void setMagicSlotHidden(int slot, boolean hidden) {

		List<Integer> list = new ArrayList<>(ModConfigs.hiddenMagic);

		if (hidden) {
			if (!list.contains(slot)) {
				list.add(slot);
			}
		} else {
			list.removeIf(i -> i == slot);
		}

		ModConfigs.setHiddenMagic(list);
	}

	@Override
	public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
		renderBackground(gui, mouseX, mouseY, partialTick);
		super.render(gui, mouseX, mouseY, partialTick);
	}
}