package online.kingdomkeys.kingdomkeys.client.gui.menu.struggle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Struggle;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class MenuStruggle extends MenuBackground {
	BlockPos boardPos;

	public MenuStruggle(BlockPos pos) {
		super("Menu", new Color(252, 173, 3));
		minecraft = Minecraft.getInstance();
		boardPos = pos;
	}
	

	public enum buttons {
		CREATE, JOIN, SETTINGS;
	}

	MenuButton create, join, settings;

	final ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");

	protected void action(buttons buttonID) {
		switch (buttonID) {
			case CREATE -> minecraft.setScreen(new StruggleCreate(boardPos));
			case JOIN -> minecraft.setScreen(new StruggleJoin(boardPos));
			case SETTINGS -> minecraft.setScreen(new StruggleSettings(boardPos));
			
		}
		updateButtons();
	}

	@Override
	public void init() {
		super.width = width;
		super.height = height;
		super.init();
		float topBarHeight = (float) height * 0.17F;
		int start = (int)topBarHeight + 5;
		int pos = 0;

		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 22;

		addRenderableWidget(create = new MenuButton((int) buttonPosX, start, (int) buttonWidth, "Create match", ButtonType.BUTTON, true, (e) -> {action(buttons.CREATE);}));
		addRenderableWidget(join = new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, "Join match", ButtonType.BUTTON, true, (e) -> {action(buttons.JOIN);}));
		addRenderableWidget(settings = new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, "Struggle Settings", ButtonType.BUTTON, true, (e) -> {action(buttons.SETTINGS);}));

		Struggle s = ModCapabilities.getWorld(minecraft.level).getStruggleFromParticipant(minecraft.player.getUUID());
		if(s != null) {
			System.out.println(s.getOwner().getUsername());
		}
		updateButtons();
	}

	private void updateButtons() {
		create.visible = true; //TODO change to show only if configured
		join.visible = true;
		Struggle s = ModCapabilities.getWorld(minecraft.level).getStruggleFromParticipant(minecraft.player.getUUID());
		if (s != null) {
			settings.visible = s.getOwner().getUUID().equals(minecraft.player.getUUID());
		}
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mouseX, mouseY, partialTicks);
		drawPlayer(gui);
	}
	
	public void drawPlayer(GuiGraphics gui) {
		PoseStack matrixStack = gui.pose();
		float playerHeight = height * 0.45F;
		float playerPosX = width * 0.5229F;
		float playerPosY = height * 0.7F;
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		if (playerData != null) {
			matrixStack.pushPose();
			{
				Player player = minecraft.player;
				ClientUtils.renderPlayerNoAnims(matrixStack, (int) playerPosX, (int) playerPosY, (int) playerHeight / 2, 0, 0, player);
			}
			matrixStack.popPose();
			matrixStack.pushPose();
			RenderSystem.setShaderColor(1, 1, 1, 1);
			matrixStack.translate(1, 1, 100);

			RenderSystem.enableBlend();
			int infoBoxWidth = (int) ((width * 0.1385F) - 14); // This might be wrong cuz I had to convert from float to int
			int infoBoxPosX = (int) (width * 0.4354F);
			int infoBoxPosY = (int) (height * 0.54F);
			gui.blit(texture, infoBoxPosX, infoBoxPosY, 123, 67, 11, 22);
			for (int i = 0; i < infoBoxWidth; i++) {
				gui.blit(texture, infoBoxPosX + 11 + i, infoBoxPosY, 135, 67, 1, 22);
			}
			gui.blit(texture, infoBoxPosX + 11 + infoBoxWidth, infoBoxPosY, 137, 67, 3, 22);
			gui.blit(texture, infoBoxPosX, infoBoxPosY + 22, 123, 90, 3, 35);
			for (int i = 0; i < infoBoxWidth + 8; i++) {
				gui.blit(texture, infoBoxPosX + 3 + i, infoBoxPosY + 22, 127, 90, 1, 35);
			}
			gui.blit(texture, infoBoxPosX + 3 + infoBoxWidth + 8, infoBoxPosY + 22, 129, 90, 3, 35);

			RenderSystem.disableBlend();
			matrixStack.popPose();
			matrixStack.pushPose();
			{
				matrixStack.translate(2, 2, 100);

				matrixStack.pushPose();
				{
					matrixStack.translate((int) infoBoxPosX + 8, (int) infoBoxPosY + ((22 / 2) - (minecraft.font.lineHeight / 2)), 1);
					// matrixStack.scale(0.75F, 0.75F, 1);
					gui.drawString(minecraft.font, minecraft.player.getDisplayName().getString(), 0, 0, 0xFFFFFF);
				}
				matrixStack.popPose();

				gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_Level) + ": " + playerData.getLevel(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26), 0xFFD900);
				gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_HP) + ": " + (int) minecraft.player.getHealth() + "/" + (int) minecraft.player.getMaxHealth(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26) + minecraft.font.lineHeight, 0x00FF00);
				gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_MP) + ": " + (int) playerData.getMP() + "/" + (int) playerData.getMaxMP(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26) + (minecraft.font.lineHeight * 2), 0x4444FF);

			}
			matrixStack.popPose();
		}
	}

}
