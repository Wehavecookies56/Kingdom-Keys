package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import java.awt.Color;
import java.util.List;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment.MenuEquipmentScreen;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment.MenuEquipmentSelectorScreen;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSEquipKeychain;
import online.kingdomkeys.kingdomkeys.network.cts.CSSummonKeyblade;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MenuSelectEquipmentButton extends MenuButtonBase {

	ItemStack stack;
	boolean selected;
	int colour, labelColour;
	MenuEquipmentSelectorScreen parent;
	int slot;
	Minecraft minecraft;

	public MenuSelectEquipmentButton(ItemStack stack, int slot, int x, int y, int widthIn, MenuEquipmentSelectorScreen parent, int colour) {
		super(x, y, widthIn, 20, "", b -> {
			if (b.visible && b.active) {
				if (slot != -1) {
					Player player = Minecraft.getInstance().player;
					IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
					if(Utils.findSummoned(player.getInventory(), playerData.getEquippedKeychain(DriveForm.NONE), false) > -1)
						PacketHandler.sendToServer(new CSSummonKeyblade(true));
					PacketHandler.sendToServer(new CSEquipKeychain(parent.form, slot));
					ItemStack stackToEquip = player.getInventory().getItem(slot);
					ItemStack stackPreviouslyEquipped = playerData.equipKeychain(parent.form, stackToEquip);
					player.getInventory().setItem(slot, stackPreviouslyEquipped);
				} else {
					Minecraft.getInstance().setScreen(new MenuEquipmentScreen());
				}
			}
		});
		this.stack = stack;
		width = (int) (parent.width * 0.29F);
		height = 14;
		this.colour = colour;
		this.labelColour = 0xFFEB1C;
		this.parent = parent;
		this.slot = slot;
		minecraft = Minecraft.getInstance();
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Font fr = minecraft.font;
		isHovered = mouseX > x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		Color col = Color.decode(String.valueOf(colour));
		RenderSystem.setShaderColor(1, 1, 1, 1);
		ItemCategory category = ItemCategory.TOOL;
		
		KeybladeItem keyblade;
		if(ItemStack.matches(stack, ItemStack.EMPTY) || !(stack.getItem() instanceof IKeychain)) {
			keyblade = null;
		} else {
			keyblade = ((IKeychain) stack.getItem()).toSummon();
		}
		if (visible) {
			Lighting.setupForFlatItems();
			float itemWidth = parent.width * 0.292F;
			RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
			matrixStack.pushPose();
			RenderSystem.enableBlend();
			
			RenderSystem.setShaderColor(col.getRed() / 255F, col.getGreen() / 255F, col.getBlue() / 255F, 1);
			matrixStack.translate(x + 0.6F, y, 0);
			matrixStack.scale(0.5F, 0.5F, 1);
			blit(matrixStack, 0, 0, 166, 34, 18, 28);
			for (int i = 0; i < (itemWidth * 2) - (17 + 17); i++) {
				blit(matrixStack, 17 + i, 0, 184, 34, 2, 28);
			}
			blit(matrixStack, (int) ((itemWidth * 2) - 17), 0, 186, 34, 17, 28);
			RenderSystem.setShaderColor(1, 1, 1, 1);
			blit(matrixStack, 6, 4, category.getU(), category.getV(), 20, 20);
			matrixStack.popPose();
			String itemName;
			if (keyblade == null) { //Name to display
				itemName = "---";
			} else {
				itemName = new ItemStack(keyblade).getHoverName().getString();
			}
			drawString(matrixStack, minecraft.font, itemName, x + 15, y + 3, 0xFFFFFF);
			String ab = "N/A";
			RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
			float labelWidth = parent.width * 0.215F;

			matrixStack.pushPose();
			{
				RenderSystem.enableBlend();
				//RenderSystem.enableAlpha();
				RenderSystem.setShaderColor(col.getRed() / 255F, col.getGreen() / 255F, col.getBlue() / 255F, 1);
				matrixStack.translate(x + width + 2.1F, y, 0);
				matrixStack.scale(0.5F, 0.5F, 1);
				blit(matrixStack, 0, 0, 219, 34, 15, 28);

				for (int i = 0; i < (labelWidth * 2) - (17 + 14); i++) {
					blit(matrixStack, 14 + i, 0, 184, 34, 2, 28);
				}
				blit(matrixStack, (int) ((labelWidth * 2) - 17), 0, 186, 34, 17, 28);
			}
			matrixStack.popPose();
			if (keyblade != null) {
				int level = keyblade.getKeybladeLevel(stack);
				List<String> abilities = Utils.getKeybladeAbilitiesAtLevel(keyblade, level);
				if (abilities.size() > 0) {
					Ability a = ModAbilities.registry.get().getValue(new ResourceLocation(abilities.get(0)));
					ab = Utils.translateToLocal(a.getTranslationKey());
					if(abilities.size() > 1) {
						ab+= " [+"+(abilities.size()-1)+"]";
					}
				}
			}

			float centerX = (labelWidth / 2) - (minecraft.font.width(ab) / 2);
			drawString(matrixStack, minecraft.font, ab, (int) (x + width + centerX + 3), y + 3, labelColour);
		
			if (selected || isHovered) { //Render stuff on the right
				RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
				matrixStack.pushPose();
				{
					RenderSystem.enableBlend();
					
					matrixStack.translate(x + 0.6F, y, 0);
					matrixStack.scale(0.5F, 0.5F, 1);
					blit(matrixStack, 0, 0, 128, 34, 18, 28);
					for (int i = 0; i < (itemWidth * 2) - (17 * 2); i++) {
						blit(matrixStack, 17 + i, 0, 146, 34, 2, 28);
					}
					blit(matrixStack, (int) ((itemWidth * 2) - 17), 0, 148, 34, 17, 28);
				}
				matrixStack.popPose();
				
				if(keyblade != null) {
					float iconPosX = parent.width * 0.69F;
					float iconPosY = parent.height * 0.1833F;
					float iconHeight = parent.height * 0.3148F;
					Lighting.setupForFlatItems();
					matrixStack.pushPose();
                    {
                        
                        matrixStack.translate(iconPosX, iconPosY, 0);
                        matrixStack.scale((float) (0.0625F * iconHeight), (float) (0.0625F * iconHeight), 1);
                        ClientUtils.drawItemAsIcon(new ItemStack(keyblade), matrixStack, 0, 0, 16);
                        
                    }
                    matrixStack.popPose();
					float strPosX = parent.width * 0.685F;
					float strPosY = parent.height * 0.5185F;
					float strNumPosX = parent.width * 0.78F;
					float magPosY = parent.height * 0.5657F;
                    float abiPosX = parent.width * 0.72F;
                    float abiPosY = parent.height * 0.62F;
					
					String strengthStr = String.valueOf(((int) keyblade.getStrength(stack)));
					String magicStr = String.valueOf(((int) keyblade.getMagic(stack)));
					IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
					int strength = playerData.getStrength(true) + ((int) keyblade.getStrength(stack));
					int magic = playerData.getMagic(true) + ((int) keyblade.getMagic(stack));
					
					String totalStrengthStr = String.valueOf(strength);
                    String totalMagicStr = String.valueOf(magic);
					String openBracketStr = " [ ";
					String openBracketMag = " [ ";
					String totalStr = String.valueOf(strength);
					String totalMag = String.valueOf(magic);
					if (totalStr.length() == 1) {
						openBracketStr += " ";
					}
					if (totalMag.length() == 1) {
						openBracketMag += " ";
					}
					
					drawString(matrixStack, fr, Component.translatable(Strings.Gui_Menu_Status_Strength).getString(), (int) strPosX, (int) strPosY, 0xEE8603);
					drawString(matrixStack, fr, strengthStr, (int) strNumPosX, (int) strPosY, 0xFFFFFF);
					drawString(matrixStack, fr, openBracketStr, (int) strNumPosX + fr.width(strengthStr), (int) strPosY, 0xBF6004);
					drawString(matrixStack, fr, totalStrengthStr, (int) strNumPosX + fr.width(strengthStr) + fr.width(openBracketStr), (int) strPosY, 0xFBEA21);
					drawString(matrixStack, fr, "]", (int) strNumPosX + fr.width(strengthStr) + fr.width(openBracketStr) + fr.width(totalStrengthStr), (int) strPosY, 0xBF6004);

					drawString(matrixStack, fr, Component.translatable(Strings.Gui_Menu_Status_Magic).getString(), (int) strPosX, (int) magPosY, 0xEE8603);
					drawString(matrixStack, fr, magicStr, (int) strNumPosX, (int) magPosY, 0xFFFFFF);
					drawString(matrixStack, fr, openBracketMag, (int) strNumPosX + fr.width(magicStr), (int) magPosY, 0xBF6004);
					drawString(matrixStack, fr, totalMagicStr, (int) strNumPosX + fr.width(magicStr) + fr.width(openBracketMag), (int) magPosY, 0xFBEA21);
					drawString(matrixStack, fr, "]", (int) strNumPosX + fr.width(magicStr) + fr.width(openBracketMag) + fr.width(totalMagicStr), (int) magPosY, 0xBF6004);
					int level = keyblade.getKeybladeLevel(stack);

					List<String> abilities = Utils.getKeybladeAbilitiesAtLevel(keyblade,level);

					if(abilities.size() > 0) {
						drawString(matrixStack, fr, Component.translatable(Strings.Gui_Menu_Status_Abilities).getString(), (int) abiPosX, (int) abiPosY, 0xEE8603);	
						for(int i = 0; i < abilities.size();i++) {
							Ability ability = ModAbilities.registry.get().getValue(new ResourceLocation(abilities.get(i)));
			                RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
		                    blit(matrixStack, (int) strPosX-2, (int) abiPosY + ((i+1)*12)-4, 73, 102, 12, 12);
							drawString(matrixStack, fr, Utils.translateToLocal(ability.getTranslationKey()), (int) strPosX+14, (int) abiPosY + ((i+1)*12)-1, 0xFFFFFF);
						}
					}
					ClientUtils.drawSplitString(minecraft.font, keyblade.getDesc(), (int) MenuBackground.tooltipPosX, (int) MenuBackground.tooltipPosY, (int) (parent.width * 0.46875F), 0x43B5E9);
				}
			}
			Lighting.setupForFlatItems();
		}
	}

	@Override
	public void playDownSound(SoundManager soundHandler) {
		soundHandler.play(SimpleSoundInstance.forUI(ModSounds.menu_in.get(), 1.0F, 1.0F));
	}
}
