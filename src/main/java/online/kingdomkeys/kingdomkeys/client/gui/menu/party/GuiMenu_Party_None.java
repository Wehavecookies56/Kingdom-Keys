package online.kingdomkeys.kingdomkeys.client.gui.menu.party;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class GuiMenu_Party_None extends MenuBackground {
	
	MenuButton back, create, join;
		
	final PlayerData playerData = PlayerData.get(minecraft.player);
	WorldData worldData;

	Party party;

	final ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");

	//Not in party
	//0 = not in party
	//1 = creating (create)
	//2 = Looking for party (join)
	//In party
	//3 = Leader view
	//4 = Member view
	
	public GuiMenu_Party_None() {
		super(Strings.Gui_Menu_Party, new Color(0,0,255));
		drawPlayerInfo = true;
		worldData = WorldData.getClient();
	}

	protected void action(String string) {		
		switch(string) {
		case "back":
			GuiHelper.openMenu();
			break;
		case "create":
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new GuiMenu_Party_Create());
			break;
		case "join":
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new GuiMenu_Party_Join());
			break;
		
		}
		
		updateButtons();
	}

	private void updateButtons() {
		create.visible = true;
		join.visible = true;
		back.visible = true;
	}

	@Override
	public void init() {
		//TODO request packet to sync other players data
		super.width = width;
		super.height = height;
		super.init();
		this.renderables.clear();
		
		party = worldData.getPartyFromMember(minecraft.player.getUUID());
		
		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		addRenderableWidget(create = new MenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Strings.Gui_Menu_Party_Create, ButtonType.BUTTON, true, (e) -> { action("create"); }));
		addRenderableWidget(join = new MenuButton((int) buttonPosX, button_statsY + (1 * 18), (int) buttonWidth, Strings.Gui_Menu_Party_Join, ButtonType.BUTTON, true, (e) -> { action("join"); }));
		addRenderableWidget(back = new MenuButton((int) buttonPosX, button_statsY + (2 * 18), (int) buttonWidth, Strings.Gui_Menu_Back, ButtonType.BUTTON, true, (e) -> { action("back"); }));
	
		updateButtons();
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		//fill(125, ((-140 / 16) + 75) + 10, 200, ((-140 / 16) + 75) + 20, 0xFFFFFF);
		super.render(gui, mouseX, mouseY, partialTicks);
		worldData = WorldData.getClient();
		drawParty(gui);
	}
	
	public void drawParty(GuiGraphics gui) {
		party = worldData.getPartyFromMember(minecraft.player.getUUID());
		if(party != null) {
			for(int i=0;i<party.getMembers().size();i++) {
				Member member = party.getMembers().get(i);
				drawPlayer(gui, i,member);
			}
		} else {
			Member m = new Member(minecraft.player.getUUID(), minecraft.player.getDisplayName().getString());
			drawPlayer(gui, 0, m);
		}
	}
	
	public void drawPlayer(GuiGraphics gui, int order, Member member) {
		PoseStack matrixStack = gui.pose();
		float playerHeight = height * 0.45F;
		float playerPosX = 150F+ (0.18F * (order) * width);
		float playerPosY = height * 0.7F;
		
		Player player = Utils.getPlayerByName(minecraft.level, member.getUsername());
		
		matrixStack.pushPose();
		{
			matrixStack.pushPose();
			{
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				if(member != null && player != null) {
					InventoryScreen.renderEntityInInventoryFollowsMouse(gui, (int) playerPosX, (int) playerPosY, 0, 0, (int) playerHeight / 2, 0, 0,0, this.minecraft.player);
				}
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.75F);
			}
			matrixStack.popPose();
			matrixStack.pushPose();
			
				RenderSystem.setShaderColor(1, 1, 1, 1);
				matrixStack.translate(9, 1, 100);
				
				RenderSystem.enableBlend();
				int infoBoxWidth = (int) ((width * 0.1385F) - 14); // This might be wrong cuz I had to convert from float to int
				int infoBoxPosX = (int) (105F+ (0.18F * (order) * width));
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
				matrixStack.translate(10, 2, 100);
				
				matrixStack.pushPose();
				{
					matrixStack.translate((int) infoBoxPosX + 8, (int) infoBoxPosY + ((22 / 2) - (minecraft.font.lineHeight / 2)), 1);
					// matrixStack.scale(0.75F, 0.75F, 1);
					gui.drawString(minecraft.font, member.getUsername(), 0, 0, 0xFFFFFF);
				}
				matrixStack.popPose();
				if(player != null) {
					PlayerData playerData = PlayerData.get(player);
					if (playerData != null) {
						gui.drawString(minecraft.font, "LV: " + playerData.getLevel(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26), 0xFFD900);
						gui.drawString(minecraft.font, "HP: " + (int) player.getHealth() + "/" + (int) player.getMaxHealth(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26) + minecraft.font.lineHeight, 0x00FF00);
						gui.drawString(minecraft.font, "MP: " + (int) playerData.getMP() + "/" + (int) playerData.getMaxMP(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26) + (minecraft.font.lineHeight * 2), 0x4444FF);
					}
				}
			}
			matrixStack.popPose();
		}
		matrixStack.popPose();
	}
	
}
