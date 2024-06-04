package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment.MenuAccessorySelectorScreen;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment.MenuEquipmentScreen;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.item.KKAccessoryItem;
import online.kingdomkeys.kingdomkeys.item.KKPotionItem;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSEquipAccessories;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MenuSelectAccessoryButton extends MenuButtonBase {

	ItemStack stack;
	boolean selected;
	int colour, labelColour;
	MenuAccessorySelectorScreen parent;
	int slot;
	Minecraft minecraft;

	ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");

	public MenuSelectAccessoryButton(ItemStack stack, int slot, int x, int y, int widthIn, MenuAccessorySelectorScreen parent, int colour) {
		super(x, y, widthIn, 20, "", b -> {
			if (b.visible && b.active) {
				if (slot != -1) {
					Player player = Minecraft.getInstance().player;
					IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
					PacketHandler.sendToServer(new CSEquipAccessories(parent.slot, slot));
					int oldItemAP = 0;
					int newItemAP = 0;
					
		            if(!ItemStack.matches(playerData.getEquippedAccessory(parent.slot),ItemStack.EMPTY)){
		              	oldItemAP = ((KKAccessoryItem)playerData.getEquippedAccessory(parent.slot).getItem()).getAp();
		            }
		            
		            if(!ItemStack.matches(player.getInventory().getItem(slot),ItemStack.EMPTY)){
		            	newItemAP = ((KKAccessoryItem)player.getInventory().getItem(slot).getItem()).getAp();
		            }

		            if(playerData.getMaxAP(true) - oldItemAP + newItemAP >= Utils.getConsumedAP(playerData)) { 
						ItemStack stackToEquip = player.getInventory().getItem(slot);
						ItemStack stackPreviouslyEquipped = playerData.equipAccessory(parent.slot, stackToEquip);
						player.getInventory().setItem(slot, stackPreviouslyEquipped);
		            } else {
						player.playSound(ModSounds.error.get(), 1, 1);
		            }
				} else {
					Minecraft.getInstance().setScreen(new MenuEquipmentScreen());
				}
			}
		});
		this.stack = stack;
		width = (int) (parent.width * 0.3F);
		height = 14;
		this.colour = colour;
		this.labelColour = 0xFFEB1C;
		this.parent = parent;
		this.slot = slot;
		minecraft = Minecraft.getInstance();
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        Font fr = minecraft.font;
		isHovered = mouseX > getX() && mouseY >= getY() && mouseX < getX() + width && mouseY < getY() + height;
		Color col = Color.decode(String.valueOf(colour));
		RenderSystem.setShaderColor(1, 1, 1, 1);
		ItemCategory category = ItemCategory.ACCESSORIES;

		PoseStack matrixStack = gui.pose();
				
		KKAccessoryItem accessory;
		if(ItemStack.matches(stack, ItemStack.EMPTY) || !(stack.getItem() instanceof KKAccessoryItem)) {
			accessory = null;
		} else {
			accessory = (KKAccessoryItem) stack.getItem();
		}
		if (visible) { ;
			Lighting.setupForFlatItems();
			float itemWidth = parent.width * 0.3F;
			matrixStack.pushPose();
			RenderSystem.enableBlend();
			
			RenderSystem.setShaderColor(col.getRed() / 255F, col.getGreen() / 255F, col.getBlue() / 255F, 1);
			matrixStack.translate(getX() + 0.6F, getY(), 0);
			matrixStack.scale(0.5F, 0.5F, 1);
			gui.blit(texture, 0, 0, 166, 34, 18, 28);
			gui.blit(texture, 16, 0, (int) ((itemWidth * 2) - (17 + 17))+2, 28, 186, 34, 2, 28, 256, 256);
			gui.blit(texture, (int) ((itemWidth * 2) - 17), 0, 186, 34, 17, 28);
			RenderSystem.setShaderColor(1, 1, 1, 1);
			gui.blit(texture, 6, 4, category.getU(), category.getV(), 20, 20);
			matrixStack.popPose();
			String accessoryName;
			if (accessory == null) { //Name to display
				accessoryName = "---";
			} else {
				accessoryName = stack.getHoverName().getString();
				String amount = "x"+parent.addedAccessoriesList.get(stack.getItem());
				gui.drawString(minecraft.font,ChatFormatting.YELLOW+ amount, getX() + width - minecraft.font.width(amount)-3, getY() + 3, 0xFFFFFF);
			}
			gui.drawString(minecraft.font, accessoryName, getX() + 15, getY() + 3, 0xFFFFFF);
			if (selected || isHovered) { //Render stuff on the right
				matrixStack.pushPose();
				{
					RenderSystem.enableBlend();
					
					matrixStack.translate(getX() + 0.6F, getY(), 0);
					matrixStack.scale(0.5F, 0.5F, 1);
					gui.blit(texture, 0, 0, 128, 34, 18, 28);
					gui.blit(texture, 16, 0, (int) ((itemWidth * 2) - (17 * 2))+1, 28, 148, 34, 2, 28, 256, 256);
					gui.blit(texture, (int) ((itemWidth * 2) - 17), 0, 148, 34, 17, 28);
				}
				matrixStack.popPose();
				
				if(accessory != null) {
					float iconPosX = parent.width * 0.565F;
					float iconPosY = parent.height * 0.20F;
					float iconHeight = parent.height * 0.3148F;
					Lighting.setupForFlatItems();
					matrixStack.pushPose();
                    {
                        
                        matrixStack.translate(iconPosX, iconPosY, 0);
                        matrixStack.scale((float) (0.0625F * iconHeight), (float) (0.0625F * iconHeight), 1);
                        ClientUtils.drawItemAsIcon(stack, matrixStack, 0,0,16);

                    }
                    matrixStack.popPose();
                    float strPosX = parent.width * 0.57F;
                    float posY = parent.height * 0.55F;
                    float strNumPosX = parent.width * 0.67F;
                    float abiPosX = parent.width * 0.62F;
					int strength = 0;
					int magic = 0;
					int ap = 0;
					List<String> abilities = new ArrayList<String>();
					boolean showData = true;
					if (stack.getItem() instanceof IKeychain) {
						strength = ((IKeychain) stack.getItem()).toSummon().getStrength(stack);
						magic = ((IKeychain) stack.getItem()).toSummon().getMagic(stack);
						int level = ((IKeychain) stack.getItem()).toSummon().getKeybladeLevel(stack);
						abilities = Utils.getKeybladeAbilitiesAtLevel(stack.getItem(),level);
					} else if (stack.getItem() instanceof KeybladeItem) {
						strength = ((KeybladeItem) stack.getItem()).getStrength(stack);
						magic = ((KeybladeItem) stack.getItem()).getMagic(stack);
						int level = ((KeybladeItem) stack.getItem()).getKeybladeLevel(stack);
						abilities = Utils.getKeybladeAbilitiesAtLevel(stack.getItem(), level);
					} else if (stack.getItem() instanceof IOrgWeapon) {
						final IOrgWeapon orgWeapon = (IOrgWeapon) stack.getItem();
						strength = orgWeapon.getStrength();
						magic = orgWeapon.getMagic();
                    } else if (stack.getItem() instanceof ArmorItem) {
                        //ArmorItem armour = (ArmorItem) stack.getItem();
                        //int armourAmount = armour.getArmorMaterial().getDamageReductionAmount(armour.get)
                    	showData = false;
                    } else if (stack.getItem() instanceof KKPotionItem) {
                     	showData = false;
                    } else if (stack.getItem() instanceof KKAccessoryItem) {
                     	ap = ((KKAccessoryItem)stack.getItem()).getAp();
                     	strength = ((KKAccessoryItem)stack.getItem()).getStr();
                     	magic = ((KKAccessoryItem)stack.getItem()).getMag();
                     	abilities = ((KKAccessoryItem)stack.getItem()).getAbilities();
                    } else {
                    	showData = false;
                    }
                    if(showData) {
                    	boolean showStr = true, showMag= true, showAP=true;
                    	abilities.remove(null);
	                    String strengthStr = String.valueOf(strength);
	                    String magicStr = String.valueOf(magic);
	                    String apStr = String.valueOf(ap);
	                    
	                    int oldAP=0,oldStr=0,oldMag=0;
	                    IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
                    	ItemStack replacedItem = playerData.getEquippedAccessory(parent.slot);
                    	if(!ItemStack.matches(replacedItem, ItemStack.EMPTY) && replacedItem.getItem() instanceof KKAccessoryItem){
                    		KKAccessoryItem oldAccessory = (KKAccessoryItem) replacedItem.getItem();
                    		oldAP = oldAccessory.getAp();
            				oldStr = oldAccessory.getStr();
    						oldMag = oldAccessory.getMag();	
                    	}
                    	
	                    int totalStrength = playerData.getStrength(true) + strength;
	                    int totalMagic = playerData.getMagic(true) + magic;
	                    int totalAP =  playerData.getMaxAP(true) + ap;
	                    String openBracket = " [ ";
	                   
	                    String totalStrengthStr = String.valueOf(totalStrength);
	                    String totalMagicStr = String.valueOf(totalMagic);
	                    String totalAPStr = String.valueOf(totalAP);
	                    
	                    if (totalStrengthStr.length() == 1) {
	                        openBracket += " ";
	                    }
	                    if (totalMagicStr.length() == 1) {
	                        openBracket += " ";
	                    }
	                    
	                    if (totalAPStr.length() == 1) {
	                        openBracket += " ";
	                    }
	                               
	                    if(stack.getItem() instanceof KKAccessoryItem) {
	                    	showAP = true;
	                    	showStr = strength != oldStr || strength != 0;
	                    	showMag = magic != oldMag || magic != 0;
	                    } else {
	                    	showAP = false;
	                    	showStr = true;
	                    	showMag = true;
	                    }
	                    
	                    if(showAP) {
		                    gui.drawString(fr, Component.translatable(Strings.Gui_Menu_Status_AP).getString(), (int) strPosX, (int) posY, 0xEE8603);
							gui.drawString(fr, apStr, (int) strNumPosX, (int) posY, 0xFFFFFF);
							gui.drawString(fr, openBracket, (int) strNumPosX + fr.width(apStr), (int) posY, 0xBF6004);
							gui.drawString(fr, (totalAP - oldAP)+"", (int) strNumPosX + fr.width(apStr) + fr.width(openBracket), (int) posY, oldAP > ap ? 0xFF0000 : oldAP == ap ? 0xFFFF00 : 0x00AAFF);
							gui.drawString(fr, "]", (int) strNumPosX + fr.width(apStr) + fr.width(openBracket) + fr.width(totalAPStr), (int) posY, 0xBF6004);
							posY+=10;
	                    }
	                    
	                    if(showStr) {
							gui.drawString(fr, Component.translatable(Strings.Gui_Menu_Status_Strength).getString(), (int) strPosX, (int) posY, 0xEE8603);
							gui.drawString(fr, strengthStr, (int) strNumPosX, (int) posY, 0xFFFFFF);
							gui.drawString(fr, openBracket, (int) strNumPosX + fr.width(strengthStr), (int) posY, 0xBF6004);
							gui.drawString(fr, (totalStrength - oldStr)+"", (int) strNumPosX + fr.width(strengthStr) + fr.width(openBracket), (int) posY, oldStr > strength ? 0xFF0000 : oldStr == strength ? 0xFFFF00 : 0x00AAFF);
							gui.drawString(fr, "]", (int) strNumPosX + fr.width(strengthStr) + fr.width(openBracket) + fr.width(totalStrengthStr), (int) posY, 0xBF6004);
							posY+=10;
	                    }
	                    
	                    if(showMag) {
							gui.drawString(fr, Component.translatable(Strings.Gui_Menu_Status_Magic).getString(), (int) strPosX, (int) posY, 0xEE8603);
							gui.drawString(fr, magicStr, (int) strNumPosX, (int) posY, 0xFFFFFF);
							gui.drawString(fr, openBracket, (int) strNumPosX + fr.width(magicStr), (int) posY, 0xBF6004);
							gui.drawString(fr, (totalMagic - oldMag)+"", (int) strNumPosX + fr.width(magicStr) + fr.width(openBracket), (int) posY, oldMag > magic ? 0xFF0000 : oldMag == magic ? 0xFFFF00 : 0x00AAFF);
							gui.drawString(fr, "]", (int) strNumPosX + fr.width(magicStr) + fr.width(openBracket) + fr.width(totalMagicStr), (int) posY, 0xBF6004);
							posY+=10;
	                    }
	                    
						if(abilities.size() > 0) {
							gui.drawString(fr, Component.translatable(Strings.Gui_Menu_Status_Abilities).getString(), (int) abiPosX, (int) posY, 0xEE8603);
							for(int i = 0; i < abilities.size();i++) {
								Ability ability = ModAbilities.registry.get().getValue(new ResourceLocation(abilities.get(i)));
			                    gui.blit(texture, (int) strPosX-2, (int) posY + ((i+1)*12)-4, 73, 102, 12, 12);
								gui.drawString(fr, Utils.translateToLocal(ability.getTranslationKey()), (int) strPosX+14, (int) posY + ((i+1)*12)-1, 0xFFFFFF);
							}
						}

						//Utils.drawSplitString(minecraft.fontRenderer, stack.getTooltip(minecraft.player, TooltipFlags.NORMAL).get(1).getString(), (int) tooltipPosX + 3, (int) tooltipPosY + 3, (int) (parent.width * 0.46875F), 0x43B5E9);
                    }
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
