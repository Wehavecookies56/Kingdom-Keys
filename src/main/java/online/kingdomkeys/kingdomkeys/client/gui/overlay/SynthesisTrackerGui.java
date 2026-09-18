package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.Recipe;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.awt.*;
import java.util.Map;

public class SynthesisTrackerGui extends OverlayBase {

	public static final SynthesisTrackerGui INSTANCE = new SynthesisTrackerGui();

	private static final int ICON = 16;
	private static final int ICON_GAP = 3;
	private static final int NAME_GAP = 6;

	private static final int ROW_HEIGHT = 14;

	private static final int HEADING_GAP = 3;

	private static final int PADDING = 6;

	private static final int MIN_BOX = 48;

	private static final float BOX_ALPHA = 0.55F;
	private static final Color BOX_COLOUR = new Color(6, 6, 24);

	private static final int DONE = 0x77FF77;
	private static final int WANTED = 0xFFFFFF;
	private static final int HEADING = 0xFFF200;

	private MenuBox box;
	private int boxWidth, boxHeight;

	private Component name(Item material) {
		return Component.literal(new ItemStack(material).getHoverName().getString()).withStyle(ClientUtils.KK_Font_MENU);
	}

	private Component count(PlayerData playerData, Map.Entry<Item, Integer> material) {
		String held = Utils.getFormattedNumber(playerData.getMaterialAmount(material.getKey()));
		return Component.literal("x" + material.getValue() + " (" + held + ")").withStyle(ClientUtils.KK_Font_MENU);
	}

	@Override
	public void render(GuiGraphics gui, DeltaTracker deltaTracker) {
		super.render(gui, deltaTracker);

		if (minecraft == null || minecraft.player == null || minecraft.options.hideGui) {
			return;
		}

		PlayerData playerData = PlayerData.get(minecraft.player);

		if (playerData == null) {
			return;
		}

		ResourceLocation following = playerData.getTrackedRecipe();

		if (following == null || !RecipeRegistry.getInstance().containsKey(following)) {
			return;
		}

		Recipe recipe = RecipeRegistry.getInstance().getValue(following);
		Map<Item, Integer> materials = recipe == null ? null : recipe.getMaterials();

		if (materials == null || materials.isEmpty() || recipe.getResult() == null) {
			return;
		}

		int screenWidth = minecraft.getWindow().getGuiScaledWidth();
		int screenHeight = minecraft.getWindow().getGuiScaledHeight();

		ItemStack result = new ItemStack(recipe.getResult());
		if (result.getItem() instanceof KeychainItem keychain) {
			result = new ItemStack(keychain.getKeyblade());
		}

		Component title = Component.literal(result.getHoverName().getString()).withStyle(ClientUtils.KK_Font_MENU);

		int nameColumn = 0;
		int countColumn = 0;
		for (Map.Entry<Item, Integer> material : materials.entrySet()) {
			nameColumn = Math.max(nameColumn, font.width(name(material.getKey())));
			countColumn = Math.max(countColumn, font.width(count(playerData, material)));
		}

		int rowWidth = ICON + ICON_GAP + nameColumn + NAME_GAP + countColumn;
		int width = Math.max(MIN_BOX, PADDING * 2 + Math.max(font.width(title), rowWidth));
		int height = Math.max(MIN_BOX, PADDING * 2 + font.lineHeight + HEADING_GAP + materials.size() * ROW_HEIGHT);

		if (box == null || width != boxWidth || height != boxHeight) {
			box = new MenuBox(0, 0, width, height, BOX_ALPHA, BOX_COLOUR);
			boxWidth = width;
			boxHeight = height;
		}

		int drift = ClientUtils.SYNTHESIS_TRACKER_ELEMENT.width - width;

		ClientUtils.SYNTHESIS_TRACKER_ELEMENT.applyTransform(gui, screenWidth, screenHeight);
		gui.pose().pushPose();
		gui.pose().translate(drift, 0, 0);
		{
			box.renderWidget(gui, 0, 0, deltaTracker.getGameTimeDeltaPartialTick(true));
			gui.drawString(font, title, PADDING, PADDING, HEADING, true);

			int countLeft = width - PADDING - countColumn;
			int nameLeft = countLeft - NAME_GAP - nameColumn;
			int iconX = nameLeft - ICON_GAP - ICON;

			int y = PADDING + font.lineHeight + HEADING_GAP;

			for (Map.Entry<Item, Integer> material : materials.entrySet()) {
				boolean covered = playerData.getMaterialAmount(material.getKey()) >= material.getValue();
				int colour = covered ? DONE : WANTED;

				RenderSystem.enableBlend();
				gui.renderItem(new ItemStack(material.getKey()), iconX, y);
				gui.flush();
				RenderSystem.disableBlend();

				int textY = y + (ICON - font.lineHeight) / 2;

				gui.drawString(font, name(material.getKey()), nameLeft, textY, colour, true);

				Component count = count(playerData, material);
				gui.drawString(font, count, countLeft + (countColumn - font.width(count)), textY, colour, true);

				y += ROW_HEIGHT;
			}
		}
		gui.pose().popPose();
		ClientUtils.SYNTHESIS_TRACKER_ELEMENT.endTransform(gui);
	}
}
