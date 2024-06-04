package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
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
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment.MenuArmorSelectorScreen;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment.MenuEquipmentScreen;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.item.*;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSEquipArmor;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MenuSelectArmorButton extends MenuButtonBase {

	ItemStack stack;
	boolean selected;
	int colour, labelColour;
	MenuArmorSelectorScreen parent;
	int slot;
	Minecraft minecraft;

	final ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");

	public MenuSelectArmorButton(ItemStack stack, int slot, int x, int y, int widthIn, MenuArmorSelectorScreen parent, int colour) {
		super(x, y, widthIn, 20, "", b -> {
			if (b.visible && b.active) {
				if (slot != -1) {
					Player player = Minecraft.getInstance().player;
					IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
					PacketHandler.sendToServer(new CSEquipArmor(parent.slot, slot));
				
					ItemStack stackToEquip = player.getInventory().getItem(slot);
					ItemStack stackPreviouslyEquipped = playerData.equipArmor(parent.slot, stackToEquip);
					player.getInventory().setItem(slot, stackPreviouslyEquipped);
					b.visible = false;
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
	public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		PoseStack matrixStack = gui.pose();
        Font fr = minecraft.font;
		isHovered = mouseX > getX() && mouseY >= getY() && mouseX < getX() + width && mouseY < getY() + height;
		Color col = Color.decode(String.valueOf(colour));
		RenderSystem.setShaderColor(1, 1, 1, 1);
		ItemCategory category = ItemCategory.EQUIPMENT;
				
		KKArmorItem armor;
		if(ItemStack.matches(stack, ItemStack.EMPTY) || !(stack.getItem() instanceof KKArmorItem)) {
			armor = null;
		} else {
			armor = (KKArmorItem) stack.getItem();
		}
		if (visible) {
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
			if (armor == null) { //Name to display
				accessoryName = "---";
			} else {
				accessoryName = stack.getHoverName().getString();
				String amount = "x"+parent.addedArmorList.get(stack.getItem());
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
				
				if(armor != null) {
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
					ImmutableMap<KKResistanceType, Integer> resistances = null;
					
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
                    } else if (stack.getItem() instanceof KKArmorItem) {
                        KKArmorItem armorItem = (KKArmorItem) stack.getItem();
                        resistances = armorItem.getResList();
                    	showData = true;
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
                    	boolean showStr = true, showMag= true, showAP=true, showResistances = false;
                    	abilities.remove(null);
	                    String strengthStr = String.valueOf(strength);
	                    String magicStr = String.valueOf(magic);
	                    String apStr = String.valueOf(ap);
	                    
	                    int oldAP=0,oldStr=0,oldMag=0;
	                    IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
                    	ItemStack replacedAccessory = playerData.getEquippedAccessory(parent.slot);
                    	if(!ItemStack.matches(replacedAccessory, ItemStack.EMPTY) && replacedAccessory.getItem() instanceof KKAccessoryItem){
                    		KKAccessoryItem oldAccessory = (KKAccessoryItem) replacedAccessory.getItem();
                    		oldAP = oldAccessory.getAp();
            				oldStr = oldAccessory.getStr();
    						oldMag = oldAccessory.getMag();	
                    	}
                    	
                    	ImmutableMap<KKResistanceType, Integer> oldResistances = null;
                    	int oldDefense = 0;
                    	ItemStack replacedArmor = playerData.getEquippedArmor(parent.slot);
                    	if(!ItemStack.matches(replacedArmor, ItemStack.EMPTY) && replacedArmor.getItem() instanceof KKArmorItem){
                    		KKArmorItem oldArmor = (KKArmorItem) replacedArmor.getItem();
                    		oldDefense = oldArmor.getDefense();
                    		oldResistances = oldArmor.getResList();	
                    	}

                    	
	                    int totalStrength = playerData.getStrength(true) + strength;
	                    int totalMagic = playerData.getMagic(true) + magic;
	                    int totalAP =  playerData.getMaxAP(true) + ap;

	                    String openBracket = " [ ";
	                   
	                    String totalStrengthStr = String.valueOf(totalStrength);
	                    String totalMagicStr = String.valueOf(totalMagic);
	                    String totalAPStr = String.valueOf(totalAP);
	                    
	                    String totalDefResStr = String.valueOf(playerData.getDefense(true) + armor.getDefense());

	                    String totalFireResStr = String.valueOf(resistances.get(KKResistanceType.fire));
	                    String totalIceResStr = String.valueOf(resistances.get(KKResistanceType.ice));
	                    String totalLightningResStr = String.valueOf(resistances.get(KKResistanceType.lightning));
	                    String totalDarknessResStr = String.valueOf(resistances.get(KKResistanceType.darkness));

	                    
	                    if (totalStrengthStr.length() == 1) {
	                        openBracket += " ";
	                    }
	                    if (totalMagicStr.length() == 1) {
	                        openBracket += " ";
	                    }
	                    
	                    if (totalAPStr.length() == 1) {
	                        openBracket += " ";
	                    }

						Map<Integer, ItemStack> equippedArmourWithSelected = new HashMap<>(playerData.getEquippedArmors());
	                               
	                    if(stack.getItem() instanceof KKAccessoryItem) {
	                    	showAP = true;
	                    	showStr = strength != oldStr || strength != 0;
	                    	showMag = magic != oldMag || magic != 0;
	                    } else if(stack.getItem() instanceof KKArmorItem) {
	                    	showAP = false;
	                    	showStr = false;
	                    	showMag = false;
	                    	showResistances = true;
							equippedArmourWithSelected.put(parent.slot, stack);
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
	                    
	                    if(showResistances && resistances != null) {
	                    	int pos = 0;
	                    	{
		                    	String resVal = ((KKArmorItem) stack.getItem()).getDefense()+"";
								gui.drawString(fr, Component.translatable(Strings.Gui_Menu_Status_Defense).getString(), (int) strPosX, (int) posY + 10 * pos, 0xEE8603);
								gui.drawString(fr, resVal, (int) strNumPosX, (int) posY + 10 * pos, 0xFFFFFF);
								gui.drawString(fr, openBracket, (int) strNumPosX + fr.width(resVal), (int) posY + 10 * pos, 0xBF6004);
								gui.drawString(fr, (playerData.getDefense(true) + ((KKArmorItem) stack.getItem()).getDefense() - oldDefense) + "", (int) strNumPosX + fr.width(resVal) + fr.width(openBracket), (int) posY + 10 * pos, 0xFFFF00);
								gui.drawString(fr, "]", (int) strNumPosX + fr.width(resVal) + fr.width(openBracket) + fr.width(totalDefResStr), (int) posY + 10 * pos++, 0xBF6004);
	                    	}
	                    	
							KKResistanceType type = KKResistanceType.fire;
							if(resistances.containsKey(type)) {
								int oldVal = (oldResistances == null || oldResistances.get(type) == null) ? 0 : oldResistances.get(type);
								String resVal = resistances.get(type).toString();
								gui.drawString(fr, Component.translatable(Strings.Gui_Menu_Status_FireResShort).getString(), (int) strPosX, (int) posY + 10 * pos, 0xEE8603);
								gui.drawString(fr, resVal, (int) strNumPosX, (int) posY + 10 * pos, 0xFFFFFF);
								gui.drawString(fr, openBracket, (int) strNumPosX + fr.width(resVal), (int) posY + 10 * pos, 0xBF6004);
								gui.drawString(fr, (Utils.getArmorsStat(equippedArmourWithSelected, type.toString())) - oldVal+"", (int) strNumPosX + fr.width(resVal) + fr.width(openBracket), (int) posY + 10 * pos, 0xFFFF00);
								gui.drawString(fr, "]", (int) strNumPosX + fr.width(resVal) + fr.width(openBracket) + fr.width(totalFireResStr), (int) posY + 10 * pos++, 0xBF6004);
							}
							
							type = KKResistanceType.ice;
							if(resistances.containsKey(type)) {
								int oldVal = (oldResistances == null || oldResistances.get(type) == null) ? 0 : oldResistances.get(type);
								String resVal = resistances.get(type).toString();
								gui.drawString(fr, Component.translatable(Strings.Gui_Menu_Status_BlizzardResShort).getString(), (int) strPosX, (int) posY + 10 * pos, 0xEE8603);
								gui.drawString(fr, resVal, (int) strNumPosX, (int) posY + 10 * pos, 0xFFFFFF);
								gui.drawString(fr, openBracket, (int) strNumPosX + fr.width(resVal), (int) posY + 10 * pos, 0xBF6004);
								gui.drawString(fr, (Utils.getArmorsStat(equippedArmourWithSelected, type.toString())) - oldVal+"", (int) strNumPosX + fr.width(resVal) + fr.width(openBracket), (int) posY + 10 * pos, 0xFFFF00);
								gui.drawString(fr, "]", (int) strNumPosX + fr.width(resVal) + fr.width(openBracket) + fr.width(totalIceResStr), (int) posY + 10 * pos++, 0xBF6004);
							}
							
							type = KKResistanceType.lightning;
							if(resistances.containsKey(type)) {
								int oldVal = (oldResistances == null || oldResistances.get(type) == null) ? 0 : oldResistances.get(type);
								String resVal = resistances.get(type).toString();
								gui.drawString(fr, Component.translatable(Strings.Gui_Menu_Status_ThunderResShort).getString(), (int) strPosX, (int) posY + 10 * pos, 0xEE8603);
								gui.drawString(fr, resVal, (int) strNumPosX, (int) posY + 10 * pos, 0xFFFFFF);
								gui.drawString(fr, openBracket, (int) strNumPosX + fr.width(resVal), (int) posY + 10 * pos, 0xBF6004);
								gui.drawString(fr, (Utils.getArmorsStat(equippedArmourWithSelected, type.toString())) - oldVal+"", (int) strNumPosX + fr.width(resVal) + fr.width(openBracket), (int) posY + 10 * pos, 0xFFFF00);
								gui.drawString(fr, "]", (int) strNumPosX + fr.width(resVal) + fr.width(openBracket) + fr.width(totalLightningResStr), (int) posY + 10 * pos++, 0xBF6004);
							}

							type = KKResistanceType.darkness;
							if(resistances.containsKey(type)) {
								int oldVal = (oldResistances == null || oldResistances.get(type) == null) ? 0 : oldResistances.get(type);
								String resVal = resistances.get(type).toString();
								gui.drawString(fr, Component.translatable(Strings.Gui_Menu_Status_DarkResShort).getString(), (int) strPosX, (int) posY + 10 * pos, 0xEE8603);
								gui.drawString(fr, resVal, (int) strNumPosX, (int) posY + 10 * pos, 0xFFFFFF);
								gui.drawString(fr, openBracket, (int) strNumPosX + fr.width(resVal), (int) posY + 10 * pos, 0xBF6004);
								gui.drawString(fr, (Utils.getArmorsStat(equippedArmourWithSelected, type.toString())) - oldVal+"", (int) strNumPosX + fr.width(resVal) + fr.width(openBracket), (int) posY + 10 * pos, 0xFFFF00);
								gui.drawString(fr, "]", (int) strNumPosX + fr.width(resVal) + fr.width(openBracket) + fr.width(totalDarknessResStr), (int) posY + 10 * pos++, 0xBF6004);
							}
	                    }
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
