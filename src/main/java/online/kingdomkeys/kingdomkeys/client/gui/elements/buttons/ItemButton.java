package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class ItemButton extends Button {
	private final ItemStack icon;
	private final Supplier<Boolean> selected;

	public static final float SCALE = 0.6F;
	String name;

	public ItemButton(int x, int y, String name, ItemStack icon, Supplier<Boolean> selected, OnPress onPress) {
		super(x, y, (int)(20 * SCALE), (int)(20 * SCALE), Component.empty(), onPress, DEFAULT_NARRATION);
		this.icon = icon;
		this.selected = selected;
		this.name = name;
	}

	@Override
	protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		int bgColor = 0xFFAAAAAA;

		if (selected.get()) {
			bgColor = 0xFF666666;
		} else if (isHovered()) {
			bgColor = 0xFFDDDDDD;
			guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.translatable("itemGroup.kingdomkeys_"+name.toLowerCase()), mouseX, mouseY);
		}

		int x1 = getX();
		int y1 = getY();
		int x2 = x1 + width;
		int y2 = y1 + height;


		guiGraphics.fill(x1 + 1, y1 + 1, x2 - 1, y2 - 1, bgColor);
		int light = 0xFFFFFFFF;
		int dark = 0xFF444444;

		guiGraphics.fill(x1 + 1, y1, x2 - 1, y1 + 1, light);
		guiGraphics.fill(x1, y1 + 1, x1 + 1, y2 - 1, light);
		guiGraphics.fill(x1 + 1, y2 - 1, x2 - 1, y2, dark);
		guiGraphics.fill(x2 - 1, y1 + 1, x2, y2 - 1, dark);

		guiGraphics.pose().pushPose();
		{
			guiGraphics.pose().translate(getX() + SCALE * 2F, getY() + SCALE * 2F, 0);
			guiGraphics.pose().scale(SCALE, SCALE, 1);

			guiGraphics.renderItem(icon, 0, 0);
		}
		guiGraphics.pose().popPose();
	}
}