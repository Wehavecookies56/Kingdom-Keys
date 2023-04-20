package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag.Default;
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
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.item.KKAccessoryItem;
import online.kingdomkeys.kingdomkeys.item.KKArmorItem;
import online.kingdomkeys.kingdomkeys.item.KKPotionItem;
import online.kingdomkeys.kingdomkeys.item.KKResistanceType;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MenuEquipmentButton extends Button {

    public Screen toOpen;
    ItemStack stack;
    Shotlock shotlock;
    int colour, labelColour;
    MenuEquipmentScreen parent;
    String label;
    boolean hasLabel;
    ItemCategory category;

    public MenuEquipmentButton(ItemStack stack, int x, int y, int colour, Screen toOpen, ItemCategory category, MenuEquipmentScreen parent) {
        super(new Builder(Component.literal(""), b -> {
            if (b.visible && b.active) {
                Minecraft.getInstance().setScreen(((MenuEquipmentButton)b).toOpen);
            }
        }).bounds(x, y, (int) (parent.width * 0.264f), 14));
        
        this.stack = stack;
        this.colour = colour;
        this.toOpen = toOpen;
        this.parent = parent;
        this.hasLabel = false;
        this.category = category;
    }

    public MenuEquipmentButton(ItemStack stack, int x, int y, int colour, Screen toOpen, ItemCategory category, MenuEquipmentScreen parent, String label, int labelColour) {
        this(stack, x, y, colour, toOpen, category, parent);
        this.hasLabel = true;
        this.labelColour = labelColour;
        this.label = label;
    }
    
    public MenuEquipmentButton(String shotlock, int x, int y, int colour, Screen toOpen, ItemCategory category, MenuEquipmentScreen parent) {
    	super(new Builder(Component.literal(""), b -> {
            if (b.visible && b.active) {
                Minecraft.getInstance().setScreen(((MenuEquipmentButton)b).toOpen);
            }
        }).bounds(x, y, (int) (parent.width * 0.264f), 14));
    	
    	this.stack = null;
        this.shotlock = ModShotlocks.registry.get().getValue(new ResourceLocation(shotlock));
        this.colour = colour;
        this.toOpen = toOpen;
        this.parent = parent;
        hasLabel = false;
        this.category = category;
    }

    public MenuEquipmentButton(String shotlock, int x, int y, int colour, Screen toOpen, ItemCategory category, MenuEquipmentScreen parent, String label, int labelColour) {
        this(shotlock, x, y, colour, toOpen, category, parent);
        this.hasLabel = true;
        this.labelColour = labelColour;
        this.label = label;
    }

    @Override
    public void playDownSound(SoundManager soundHandler) {
        soundHandler.play(SimpleSoundInstance.forUI(ModSounds.menu_select.get(), 1.0F, 1.0F));
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        Font fr = mc.font;

        float itemY = parent.height * 0.1907F;
        float bottomY = parent.height - (parent.height * 0.25F);
        if (this.getY() < itemY - 1 || this.getY() > bottomY - 1) return;

        isHovered = mouseX > getX() && mouseY >= getY() && mouseX < getX() + width && mouseY < getY() + height;
        Color col = Color.decode(String.valueOf(colour));
        RenderSystem.setShaderColor(1, 1, 1, 1);
        float labelWidth = parent.width * 0.142F;
        float gradientWidth = parent.width * 0.175F;
        if (visible) {
            float itemWidth = parent.width * 0.264F;
            RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
            matrixStack.pushPose();
            {
                RenderSystem.enableBlend();
                
                Lighting.setupForFlatItems();
                RenderSystem.setShaderColor(col.getRed() / 255F, col.getGreen() / 255F, col.getBlue() / 255F, 1);
                matrixStack.translate(getX() + 0.6F, getY(), 0);
                matrixStack.scale(0.5F, 0.5F, 1);
                //Gradient Background
                for (int i = 0; i < height * 2; i++) {
                    matrixStack.pushPose();
                    {
                        matrixStack.scale(((gradientWidth + itemWidth + 5) * 2) / (32F), 1.1F, 1);
                        blit(matrixStack, -14, i - 1, 166, 63, 32, 1);
                    }
                    matrixStack.popPose();
                }

                //Left item slot
                blit(matrixStack, 0, 0, 166, 34, 18, 28);
                //Middle item slot
                for (int i = 0; i < (itemWidth * 2) - (17 * 2); i++) {
                    blit(matrixStack, 16 + i, 0, 186, 34, 2, 28);
                }
                //Right item slot
                blit(matrixStack, (int)(itemWidth * 2) - 17, 0, 186, 34, 17, 28);
                RenderSystem.setShaderColor(1, 1, 1, 1);
                //Icon
                blit(matrixStack, 6, 4, category.getU(), category.getV(), 20, 20);
            }
            matrixStack.popPose();
            if (stack != null) {
                String itemName = stack.getHoverName().getString();
                if (stack.getItem() instanceof IKeychain) {
                    itemName = new ItemStack(((IKeychain) stack.getItem()).toSummon()).getHoverName().getString();
                } else if (ItemStack.matches(stack, ItemStack.EMPTY)) {
                    itemName = "---";
                }
                drawString(matrixStack, fr, itemName, getX() + 15, getY() + 3, 0xFFFFFF);
            } else {
            	if(shotlock != null) {
            		drawString(matrixStack, fr, Utils.translateToLocal(shotlock.getTranslationKey()), getX() + 15, getY() + 3, 0xFFFFFF);
            	} else {
            		drawString(matrixStack, fr, "---", getX() + 15, getY() + 3, 0xFFFFFF);	
            	}
            }
            if (isHovered) {
                RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
                matrixStack.pushPose();
                {
                    matrixStack.translate(getX() + 0.6F, getY(), 0);
                    matrixStack.scale(0.5F, 0.5F, 1);
                    //Left selected
                    blit(matrixStack, 0, 0, 128, 34, 18, 28);
                    //Middle selected
                    for (int i = 0; i < (itemWidth * 2) - (17 * 2); i++) {
                        blit(matrixStack, 16 + i, 0, 148, 34, 2, 28);
                    }
                    //Right selected
                    blit(matrixStack, (int)(itemWidth * 2) - 17, 0, 148, 34, 17, 28);
                }
                matrixStack.popPose();
                float iconPosX = parent.width * 0.6374F;
                float iconPosY = parent.height * 0.17F;
                float iconHeight = parent.height * 0.3148F;
                if (stack != null) {
                	ItemStack item;
                    if (stack.getItem() instanceof IKeychain) {
                    	item = new ItemStack(((IKeychain) stack.getItem()).toSummon());
                    } else {
                    	item = stack;
                    }
                    
                    matrixStack.pushPose();
                    {
                        
                        matrixStack.translate(iconPosX, iconPosY, 0);
                        matrixStack.scale((float) (0.075F * iconHeight), (float) (0.075F * iconHeight), 1);
                        //mc.getItemRenderer().renderAndDecorateItem(item, 0, 0);
                        ClientUtils.drawItemAsIcon(item, matrixStack, 0,0,16);
                    }
                    matrixStack.popPose();

                    float strPosX = parent.width * 0.634F;
                    float posY = parent.height * 0.56F;
                    float strNumPosX = parent.width * 0.77F;
                    float abiPosX = parent.width * 0.685F;
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
						if(orgWeapon.getAbilities() != null)
							abilities = Lists.newArrayList(orgWeapon.getAbilities());
                    } else if (stack.getItem() instanceof KKArmorItem) {
                    	KKArmorItem armorItem = (KKArmorItem) stack.getItem();
                        resistances = armorItem.getResList();
                    	showData = true;
                    } else if (stack.getItem() instanceof KKPotionItem) {
                     	showData = true;
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
	                    
	                    IPlayerCapabilities playerData = ModCapabilities.getPlayer(mc.player);
	                    int totalStrength = playerData.getStrength(true) + strength;
	                    int totalMagic = playerData.getMagic(true) + magic;
	                    int totalAP =  playerData.getMaxAP(true) + ap;
	                    String openBracket = " [ ";
	                   
	                    String totalStrengthStr = String.valueOf(totalStrength);
	                    String totalMagicStr = String.valueOf(totalMagic);
	                    String totalAPStr = String.valueOf(totalAP);
	                    
	                    String totalFireResStr = resistances == null ? "" : String.valueOf(resistances.get(KKResistanceType.fire));
	                    String totalIceResStr = resistances == null ? "" : String.valueOf(resistances.get(KKResistanceType.ice));
	                    String totalLightningResStr = resistances == null ? "" : String.valueOf(resistances.get(KKResistanceType.lightning));
	                    String totalDarknessResStr = resistances == null ? "" : String.valueOf(resistances.get(KKResistanceType.darkness));
	                    
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
	                    	showStr = strength != 0;
	                    	showMag = magic != 0;
	                    } else if(stack.getItem() instanceof KKArmorItem) {
	                    	showAP = false;
	                    	showStr = false;
	                    	showMag = false;
	                    	showResistances = true;
	                    } else {
	                    	showAP = false;
	                    	showStr = true;
	                    	showMag = true;
	                    }
	                    
	                    if(stack.getItem() instanceof KKPotionItem) {
	                    	showAP = false;
	                    	showStr = false;
	                    	showMag = false;
	                    }
	                    
	                    if(showAP) {
		                    drawString(matrixStack, fr, Component.translatable(Strings.Gui_Menu_Status_AP).getString(), (int) strPosX, (int) posY, 0xEE8603);
							drawString(matrixStack, fr, apStr, (int) strNumPosX, (int) posY, 0xFFFFFF);
							drawString(matrixStack, fr, openBracket, (int) strNumPosX + fr.width(apStr), (int) posY, 0xBF6004);
							drawString(matrixStack, fr, playerData.getMaxAP(true)+"", (int) strNumPosX + fr.width(apStr) + fr.width(openBracket), (int) posY, 0xFBEA21);
							drawString(matrixStack, fr, "]", (int) strNumPosX + fr.width(apStr) + fr.width(openBracket) + fr.width(totalAPStr), (int) posY, 0xBF6004);
							posY+=10;
	                    }
	                    
	                    if(showStr) {
							drawString(matrixStack, fr, Component.translatable(Strings.Gui_Menu_Status_Strength).getString(), (int) strPosX, (int) posY, 0xEE8603);
							drawString(matrixStack, fr, strengthStr, (int) strNumPosX, (int) posY, 0xFFFFFF);
							drawString(matrixStack, fr, openBracket, (int) strNumPosX + fr.width(strengthStr), (int) posY, 0xBF6004);
							drawString(matrixStack, fr, totalStrengthStr, (int) strNumPosX + fr.width(strengthStr) + fr.width(openBracket), (int) posY, 0xFBEA21);
							drawString(matrixStack, fr, "]", (int) strNumPosX + fr.width(strengthStr) + fr.width(openBracket) + fr.width(totalStrengthStr), (int) posY, 0xBF6004);
							posY+=10;
	                    }
	                    
	                    if(showMag) {
							drawString(matrixStack, fr, Component.translatable(Strings.Gui_Menu_Status_Magic).getString(), (int) strPosX, (int) posY, 0xEE8603);
							drawString(matrixStack, fr, magicStr, (int) strNumPosX, (int) posY, 0xFFFFFF);
							drawString(matrixStack, fr, openBracket, (int) strNumPosX + fr.width(magicStr), (int) posY, 0xBF6004);
							drawString(matrixStack, fr, totalMagicStr, (int) strNumPosX + fr.width(magicStr) + fr.width(openBracket), (int) posY, 0xFBEA21);
							drawString(matrixStack, fr, "]", (int) strNumPosX + fr.width(magicStr) + fr.width(openBracket) + fr.width(totalMagicStr), (int) posY, 0xBF6004);
							posY+=10;
	                    }
	                    
	                    if(showResistances && resistances != null) {
	                    	int pos = 0;
	                    	{
		                    	String resVal = ((KKArmorItem) stack.getItem()).getDefense()+"";
								drawString(matrixStack, fr, Component.translatable(Strings.Gui_Menu_Status_Defense).getString(), (int) strPosX, (int) posY + 10 * pos, 0xEE8603);
								drawString(matrixStack, fr, resVal, (int) strNumPosX, (int) posY + 10 * pos, 0xFFFFFF);
								drawString(matrixStack, fr, openBracket, (int) strNumPosX + fr.width(resVal), (int) posY + 10 * pos, 0xBF6004);
								drawString(matrixStack, fr, playerData.getDefense(true) + "", (int) strNumPosX + fr.width(resVal) + fr.width(openBracket), (int) posY + 10 * pos, 0xFFFF00);
								drawString(matrixStack, fr, "]", (int) strNumPosX + fr.width(resVal) + fr.width(openBracket) + fr.width(totalFireResStr), (int) posY + 10 * pos++, 0xBF6004);
	                    	}
							if(resistances.containsKey(KKResistanceType.fire)) {
								String resVal = resistances.get(KKResistanceType.fire).toString();
								drawString(matrixStack, fr, Component.translatable(Strings.Gui_Menu_Status_FireResShort).getString(), (int) strPosX, (int) posY + 10 * pos, 0xEE8603);
								drawString(matrixStack, fr, resVal, (int) strNumPosX, (int) posY + 10 * pos, 0xFFFFFF);
								drawString(matrixStack, fr, openBracket, (int) strNumPosX + fr.width(resVal), (int) posY + 10 * pos, 0xBF6004);
								drawString(matrixStack, fr, Utils.getArmorsStat(playerData, KKResistanceType.fire.toString()) + "", (int) strNumPosX + fr.width(resVal) + fr.width(openBracket), (int) posY + 10 * pos, 0xFFFF00);
								drawString(matrixStack, fr, "]", (int) strNumPosX + fr.width(resVal) + fr.width(openBracket) + fr.width(totalFireResStr), (int) posY + 10 * pos++, 0xBF6004);
							}
							if(resistances.containsKey(KKResistanceType.ice)) {
								String resVal = resistances.get(KKResistanceType.ice).toString();
								drawString(matrixStack, fr, Component.translatable(Strings.Gui_Menu_Status_BlizzardResShort).getString(), (int) strPosX, (int) posY + 10 * pos, 0xEE8603);
								drawString(matrixStack, fr, resVal, (int) strNumPosX, (int) posY + 10 * pos, 0xFFFFFF);
								drawString(matrixStack, fr, openBracket, (int) strNumPosX + fr.width(resVal), (int) posY + 10 * pos, 0xBF6004);
								drawString(matrixStack, fr, Utils.getArmorsStat(playerData, KKResistanceType.ice.toString()) + "", (int) strNumPosX + fr.width(resVal) + fr.width(openBracket), (int) posY + 10 * pos, 0xFFFF00);
								drawString(matrixStack, fr, "]", (int) strNumPosX + fr.width(resVal) + fr.width(openBracket) + fr.width(totalIceResStr), (int) posY + 10 * pos++, 0xBF6004);
							}
							if(resistances.containsKey(KKResistanceType.lightning)) {
								String resVal = resistances.get(KKResistanceType.lightning).toString();
								drawString(matrixStack, fr, Component.translatable(Strings.Gui_Menu_Status_ThunderResShort).getString(), (int) strPosX, (int) posY + 10 * pos, 0xEE8603);
								drawString(matrixStack, fr, resVal, (int) strNumPosX, (int) posY + 10 * pos, 0xFFFFFF);
								drawString(matrixStack, fr, openBracket, (int) strNumPosX + fr.width(resVal), (int) posY + 10 * pos, 0xBF6004);
								drawString(matrixStack, fr, Utils.getArmorsStat(playerData, KKResistanceType.lightning.toString()) + "", (int) strNumPosX + fr.width(resVal) + fr.width(openBracket), (int) posY + 10 * pos, 0xFFFF00);
								drawString(matrixStack, fr, "]", (int) strNumPosX + fr.width(resVal) + fr.width(openBracket) + fr.width(totalLightningResStr), (int) posY + 10 * pos++, 0xBF6004);
							}
							if(resistances.containsKey(KKResistanceType.darkness)) {
								String resVal = resistances.get(KKResistanceType.darkness).toString();
								drawString(matrixStack, fr, Component.translatable(Strings.Gui_Menu_Status_DarkResShort).getString(), (int) strPosX, (int) posY + 10 * pos, 0xEE8603);
								drawString(matrixStack, fr, resVal, (int) strNumPosX, (int) posY + 10 * pos, 0xFFFFFF);
								drawString(matrixStack, fr, openBracket, (int) strNumPosX + fr.width(resVal), (int) posY + 10 * pos, 0xBF6004);
								drawString(matrixStack, fr, Utils.getArmorsStat(playerData, KKResistanceType.darkness.toString()) + "", (int) strNumPosX + fr.width(resVal) + fr.width(openBracket), (int) posY + 10 * pos, 0xFFFF00);
								drawString(matrixStack, fr, "]", (int) strNumPosX + fr.width(resVal) + fr.width(openBracket) + fr.width(totalDarknessResStr), (int) posY + 10 * pos++, 0xBF6004);
							}
	                    }
	                    
						if(abilities.size() > 0) {
							drawString(matrixStack, fr, ChatFormatting.UNDERLINE + Component.translatable(Strings.Gui_Menu_Status_Abilities).getString(), (int) abiPosX, (int) posY, 0xEE8603);	
							for(int i = 0; i < abilities.size();i++) {
								Ability ability = ModAbilities.registry.get().getValue(new ResourceLocation(abilities.get(i)));
				                RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
			                    blit(matrixStack, (int) strPosX-2, (int) posY + ((i+1)*12)-4, 73, 102, 12, 12);
								drawString(matrixStack, fr, Utils.translateToLocal(ability.getTranslationKey()), (int) strPosX+14, (int) posY + ((i+1)*12)-1, 0xFFFFFF);
							}
						}
						
						if(stack.getItem() instanceof KeychainItem) {
	                    	ClientUtils.drawSplitString(matrixStack, fr,((IKeychain) stack.getItem()).toSummon().getDesc(), (int) MenuBackground.tooltipPosX, (int) MenuBackground.tooltipPosY, (int)(parent.width * 0.46875F), 0x43B5E9);
						} else if(stack.getItem() instanceof KeybladeItem) {
                            ClientUtils.drawSplitString(matrixStack, fr,((KeybladeItem) stack.getItem()).getDesc(), (int) MenuBackground.tooltipPosX, (int) MenuBackground.tooltipPosY, (int)(parent.width * 0.46875F), 0x43B5E9);
						} else if(stack.getItem() instanceof KKAccessoryItem) {
	                    	//Utils.drawSplitString(fr,stack.getTooltip(Minecraft.getInstance().player, TooltipFlags.NORMAL).toString(), (int) tooltipPosX + 3, (int) tooltipPosY + 3, (int)(parent.width * 0.46875F), 0x43B5E9);
						} else if(stack.getItem() instanceof KKPotionItem) {
                            ClientUtils.drawSplitString(matrixStack, fr, stack.getTooltipLines(mc.player, Default.NORMAL).get(1).getString(), (int) MenuBackground.tooltipPosX, (int) MenuBackground.tooltipPosY, (int)(parent.width * 0.46875F), 0x43B5E9);
						}
                    }
                } 
                
            }
            Lighting.setupFor3DItems();
            RenderSystem.setShaderColor(1, 1, 1, 1);
            if (hasLabel) {
                RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
                matrixStack.pushPose();
                {
                    
                    Lighting.setupForFlatItems();
                    RenderSystem.setShaderColor(col.getRed() / 255F, col.getGreen() / 255F, col.getBlue() / 255F, 1);
                    matrixStack.translate(getX() - labelWidth, getY(), 0);
                    matrixStack.scale(0.5F, 0.5F, 1);

                    //Left label
                    blit(matrixStack, 0, 0, 166, 34, 17, 28);
                    //Middle label
                    for (int i = 0; i  < (labelWidth * 2) - (17 + 14); i++) {
                        blit(matrixStack, 17 + i, 0, 184, 34, 1, 28);
                    }
                    //Right label
                    blit(matrixStack, (int)(labelWidth * 2) - 14, 0, 204, 34, 14, 28);
                }
                matrixStack.popPose();
                float centerX = (labelWidth / 2) - (fr.width(Utils.translateToLocal(label)) / 2);
    			RenderSystem.setShaderColor(1,1,1,1);
                drawString(matrixStack, fr, Utils.translateToLocal(label), (int) (getX() - labelWidth + centerX), getY() + 3, labelColour);
            }
        }


    }
}