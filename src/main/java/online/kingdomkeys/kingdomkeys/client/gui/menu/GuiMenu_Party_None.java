package online.kingdomkeys.kingdomkeys.client.gui.menu;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.ExtendedWorldData;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Utils;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartyAddMember;

public class GuiMenu_Party_None extends GuiMenu_Background {
	
	GuiMenuButton back, create, join;
		
	final IPlayerCapabilities props = ModCapabilities.get(minecraft.player);
	ExtendedWorldData worldData;

	Party party;
	
	//Not in party
	//0 = not in party
	//1 = creating (create)
	//2 = Looking for party (join)
	//In party
	//3 = Leader view
	//4 = Member view
	
	public GuiMenu_Party_None(String name) {
		super(name);
		drawPlayerInfo = true;
		worldData = ExtendedWorldData.get(minecraft.world);
	}

	protected void action(String string) {		
		switch(string) {
		case "back":
			GuiHelper.openMenu();
			break;
		case "create":
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			minecraft.displayGuiScreen(new GuiMenu_Party_Create("Create Party"));
			break;
		case "join":
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			minecraft.displayGuiScreen(new GuiMenu_Party_Join("Join Party"));
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
		this.buttons.clear();
		
		party = worldData.getPartyFromMember(minecraft.player.getUniqueID());
		
		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		addButton(create = new GuiMenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal("Create"), ButtonType.BUTTON, (e) -> { action("create"); }));
		addButton(join = new GuiMenuButton((int) buttonPosX, button_statsY + (1 * 18), (int) buttonWidth, Utils.translateToLocal("Join"), ButtonType.BUTTON, (e) -> { action("join"); }));
		addButton(back = new GuiMenuButton((int) buttonPosX, button_statsY + (2 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Status_Button_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
	
		updateButtons();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		//fill(125, ((-140 / 16) + 75) + 10, 200, ((-140 / 16) + 75) + 20, 0xFFFFFF);
		super.render(mouseX, mouseY, partialTicks);
		worldData = ExtendedWorldData.get(minecraft.world);
		RenderSystem.pushMatrix();
		{
			RenderSystem.scaled(1.5,1.5, 1);
			drawString(minecraft.fontRenderer, "PARTY", 2, 10, 0xFF9900);
		}
		RenderSystem.popMatrix();
		drawParty();
	}
	
	public void drawParty() {
		party = worldData.getPartyFromMember(minecraft.player.getUniqueID());
		if(party != null) {
			for(int i=0;i<party.getMembers().size();i++) {
				Member member = party.getMembers().get(i);
				drawPlayer(i,member);
			}
		} else {
			Member m = new Member(minecraft.player.getUniqueID(), minecraft.player.getDisplayName().getFormattedText());
			drawPlayer(0, m);
		}
	}
	
	public void drawPlayer(int order, Member member) {
		float playerHeight = height * 0.45F;
		float playerPosX = 150F+ (0.18F * (order) * width);
		float playerPosY = height * 0.7F;
		
		PlayerEntity player = Utils.getPlayerByName(minecraft.world, member.getUsername());
		
		RenderSystem.pushMatrix();
		{
			RenderSystem.pushMatrix();
			{
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				if(member != null && player != null)
					InventoryScreen.drawEntityOnScreen((int) playerPosX, (int) playerPosY, (int) playerHeight / 2, 0, 0, player);
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.75F);
			}
			RenderSystem.popMatrix();
			RenderSystem.pushMatrix();
			
				RenderSystem.color3f(1, 1, 1);
				RenderSystem.translatef(9, 1, 100);
				RenderSystem.enableAlphaTest();
				RenderSystem.enableBlend();
				minecraft.getRenderManager().textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
				int infoBoxWidth = (int) ((width * 0.1385F) - 14); // This might be wrong cuz I had to convert from float to int
				int infoBoxPosX = (int) (105F+ (0.18F * (order) * width));
				int infoBoxPosY = (int) (height * 0.54F);
				blit(infoBoxPosX, infoBoxPosY, 123, 67, 11, 22);
				for (int i = 0; i < infoBoxWidth; i++) {
					blit(infoBoxPosX + 11 + i, infoBoxPosY, 135, 67, 1, 22);
				}
				blit(infoBoxPosX + 11 + infoBoxWidth, infoBoxPosY, 137, 67, 3, 22);
				blit(infoBoxPosX, infoBoxPosY + 22, 123, 90, 3, 35);
				for (int i = 0; i < infoBoxWidth + 8; i++) {
					blit(infoBoxPosX + 3 + i, infoBoxPosY + 22, 127, 90, 1, 35);
				}
				blit(infoBoxPosX + 3 + infoBoxWidth + 8, infoBoxPosY + 22, 129, 90, 3, 35);
				RenderSystem.disableAlphaTest();
				RenderSystem.disableBlend();
			RenderSystem.popMatrix();
			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef(10, 2, 100);
				
				RenderSystem.pushMatrix();
				{
					RenderSystem.translatef((int) infoBoxPosX + 8, (int) infoBoxPosY + ((22 / 2) - (minecraft.fontRenderer.FONT_HEIGHT / 2)), 1);
					// RenderSystem.scale(0.75F, 0.75F, 1);
					drawString(minecraft.fontRenderer, member.getUsername(), 0, 0, 0xFFFFFF);
				}
				RenderSystem.popMatrix();
				if(player != null) {
					IPlayerCapabilities props = ModCapabilities.get(player);
					if (props != null) {
						drawString(minecraft.fontRenderer, "LV: " + props.getLevel(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26), 0xFFD900);
						drawString(minecraft.fontRenderer, "HP: " + (int) player.getHealth() + "/" + (int) player.getMaxHealth(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26) + minecraft.fontRenderer.FONT_HEIGHT, 0x00FF00);
						drawString(minecraft.fontRenderer, "MP: " + (int) props.getMP() + "/" + (int) props.getMaxMP(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26) + (minecraft.fontRenderer.FONT_HEIGHT * 2), 0x4444FF);
					}
				}
			}
			RenderSystem.popMatrix();
		}
		RenderSystem.popMatrix();
	}
	
}
