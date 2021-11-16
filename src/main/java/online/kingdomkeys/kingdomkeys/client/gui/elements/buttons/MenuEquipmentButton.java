package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment.MenuEquipmentScreen;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.item.KKPotionItem;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.item.organization.OrgWeaponItem;
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
        super(x, y, (int) (parent.width * 0.264f), 14, new TranslationTextComponent(""), b -> {
            if (b.visible && b.active) {
                Minecraft.getInstance().displayGuiScreen(((MenuEquipmentButton)b).toOpen);
            }
        });
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
        super(x, y, (int) (parent.width * 0.264f), 14, new TranslationTextComponent(""), b -> {
            if (b.visible && b.active) {
                Minecraft.getInstance().displayGuiScreen(((MenuEquipmentButton)b).toOpen);
            }
        });
        this.stack = null;
        this.shotlock = ModShotlocks.registry.getValue(new ResourceLocation(shotlock));
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
    public void playDownSound(SoundHandler soundHandler) {
        soundHandler.play(SimpleSound.master(ModSounds.menu_select.get(), 1.0F, 1.0F));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        FontRenderer fr = mc.fontRenderer;

        float itemY = parent.height * 0.1907F;
        float bottomY = parent.height - (parent.height * 0.25F);
        if (this.y < itemY - 1 || this.y > bottomY - 1) return;

        isHovered = mouseX > x && mouseY >= y && mouseX < x + width && mouseY < y + height;
        Color col = Color.decode(String.valueOf(colour));
        RenderSystem.color4f(1, 1, 1, 1);
        float labelWidth = parent.width * 0.142F;
        float gradientWidth = parent.width * 0.175F;
        if (visible) {
            float itemWidth = parent.width * 0.264F;
            mc.getTextureManager().bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
            matrixStack.push();
            {
                RenderSystem.enableBlend();
                
                RenderHelper.disableStandardItemLighting();
                RenderSystem.color4f(col.getRed() / 128F, col.getGreen() / 128F, col.getBlue() / 128F, 1);
                matrixStack.translate(x + 0.6F, y, 0);
                matrixStack.scale(0.5F, 0.5F, 1);
                //Gradient Background
                for (int i = 0; i < height * 2; i++) {
                    matrixStack.push();
                    {
                        matrixStack.scale(((gradientWidth + itemWidth + 5) * 2) / (32F), 1.1F, 1);
                        blit(matrixStack, -14, i - 1, 166, 63, 32, 1);
                    }
                    matrixStack.pop();
                }

                //Left item slot
                blit(matrixStack, 0, 0, 166, 34, 18, 28);
                //Middle item slot
                for (int i = 0; i < (itemWidth * 2) - (17 * 2); i++) {
                    blit(matrixStack, 17 + i, 0, 184, 34, 2, 28);
                }
                //Right item slot
                blit(matrixStack, (int)(itemWidth * 2) - 17, 0, 186, 34, 17, 28);
                RenderSystem.color4f(1, 1, 1, 1);
                //Icon
                blit(matrixStack, 6, 4, category.getU(), category.getV(), 20, 20);
            }
            matrixStack.pop();
            if (stack != null) {
                String itemName = stack.getDisplayName().getString();
                if (stack.getItem() instanceof IKeychain) {
                    itemName = new ItemStack(((IKeychain) stack.getItem()).toSummon()).getDisplayName().getString();
                } else if (ItemStack.areItemStacksEqual(stack, ItemStack.EMPTY)) {
                    itemName = "---";
                }
                drawString(matrixStack, fr, itemName, x + 15, y + 3, 0xFFFFFF);
            } else {
            	if(shotlock != null) {
            		drawString(matrixStack, fr, Utils.translateToLocal(shotlock.getTranslationKey()), x + 15, y + 3, 0xFFFFFF);
            	} else {
            		drawString(matrixStack, fr, "---", x + 15, y + 3, 0xFFFFFF);	
            	}
            }
            if (isHovered) {
                mc.getTextureManager().bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
                matrixStack.push();
                {
                    
                    matrixStack.translate(x + 0.6F, y, 0);
                    matrixStack.scale(0.5F, 0.5F, 1);
                    //Left selected
                    blit(matrixStack, 0, 0, 128, 34, 18, 28);
                    //Middle selected
                    for (int i = 0; i < (itemWidth * 2) - (17 * 2); i++) {
                        blit(matrixStack, 17 + i, 0, 146, 34, 2, 28);
                    }
                    //Right selected
                    blit(matrixStack, (int)(itemWidth * 2) - 17, 0, 148, 34, 17, 28);
                }
                matrixStack.pop();
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
                    
                    RenderSystem.pushMatrix();
                    {
                        
                        RenderSystem.translatef(iconPosX, iconPosY, 0);
                        RenderSystem.scalef((float) (0.075F * iconHeight), (float) (0.075F * iconHeight), 1);
                        mc.getItemRenderer().renderItemAndEffectIntoGUI(item, 0, 0);
                    }
                    RenderSystem.popMatrix();

                    float strPosX = parent.width * 0.634F;
                    float strPosY = parent.height * 0.56F;
                    float strNumPosX = parent.width * 0.77F;
                    float magPosY = parent.height * 0.60F;
                    float abiPosX = parent.width * 0.685F;
                    float abiPosY = parent.height * 0.65F;
					int strength = 0;
					int magic = 0;
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
						strength = ((OrgWeaponItem) stack.getItem()).getStrength();
						magic = ((OrgWeaponItem)stack.getItem()).getMagic();
                    } else if (stack.getItem() instanceof ArmorItem) {
                        //ArmorItem armour = (ArmorItem) stack.getItem();
                        //int armourAmount = armour.getArmorMaterial().getDamageReductionAmount(armour.get)
                    	showData = false;
                    } else if (stack.getItem() instanceof KKPotionItem) {
                     	showData = false;
                    } else {
                    	showData = false;
                    }
                    if(showData) {
                    	abilities.remove(null);
	                    String strengthStr = String.valueOf(strength);
	                    String magicStr = String.valueOf(magic);
	                    
	                    IPlayerCapabilities playerData = ModCapabilities.getPlayer(mc.player);
	                    int totalStrength = playerData.getStrength(true) + strength;
	                    int totalMagic = playerData.getMagic(true) + magic;
	                    String openBracketStr = " [ ";
	                    String openBracketMag = " [ ";
	                    String totalStrengthStr = String.valueOf(totalStrength);
	                    String totalMagicStr = String.valueOf(totalMagic);
	                    if (totalStrengthStr.length() == 1) {
	                        openBracketStr += " ";
	                    }
	                    if (totalMagicStr.length() == 1) {
	                        openBracketMag += " ";
	                    }
						drawString(matrixStack, fr, new TranslationTextComponent(Strings.Gui_Menu_Status_Strength).getString(), (int) strPosX, (int) strPosY, 0xEE8603);
						drawString(matrixStack, fr, strengthStr, (int) strNumPosX, (int) strPosY, 0xFFFFFF);
						drawString(matrixStack, fr, openBracketStr, (int) strNumPosX + fr.getStringWidth(strengthStr), (int) strPosY, 0xBF6004);
						drawString(matrixStack, fr, totalStrengthStr, (int) strNumPosX + fr.getStringWidth(strengthStr) + fr.getStringWidth(openBracketStr), (int) strPosY, 0xFBEA21);
						drawString(matrixStack, fr, "]", (int) strNumPosX + fr.getStringWidth(strengthStr) + fr.getStringWidth(openBracketStr) + fr.getStringWidth(totalStrengthStr), (int) strPosY, 0xBF6004);
	
						drawString(matrixStack, fr, new TranslationTextComponent(Strings.Gui_Menu_Status_Magic).getString(), (int) strPosX, (int) magPosY, 0xEE8603);
						drawString(matrixStack, fr, magicStr, (int) strNumPosX, (int) magPosY, 0xFFFFFF);
						drawString(matrixStack, fr, openBracketMag, (int) strNumPosX + fr.getStringWidth(magicStr), (int) magPosY, 0xBF6004);
						drawString(matrixStack, fr, totalMagicStr, (int) strNumPosX + fr.getStringWidth(magicStr) + fr.getStringWidth(openBracketMag), (int) magPosY, 0xFBEA21);
						drawString(matrixStack, fr, "]", (int) strNumPosX + fr.getStringWidth(magicStr) + fr.getStringWidth(openBracketMag) + fr.getStringWidth(totalMagicStr), (int) magPosY, 0xBF6004);
	
						if(abilities.size() > 0) {
							drawString(matrixStack, fr, new TranslationTextComponent(Strings.Gui_Menu_Status_Abilities).getString(), (int) abiPosX, (int) abiPosY, 0xEE8603);	
							for(int i = 0; i < abilities.size();i++) {
								Ability ability = ModAbilities.registry.getValue(new ResourceLocation(abilities.get(i)));
				                mc.getTextureManager().bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
			                    blit(matrixStack, (int) strPosX-2, (int) abiPosY + ((i+1)*12)-4, 73, 102, 12, 12);
								drawString(matrixStack, fr, Utils.translateToLocal(ability.getTranslationKey()), (int) strPosX+14, (int) abiPosY + ((i+1)*12)-1, 0xFFFFFF);
							}
						}
						
						if(stack.getItem() instanceof KeychainItem) {
							float tooltipPosX = parent.width * 0.3333F;
	                    	float tooltipPosY = parent.height * 0.8F;
	                    	Utils.drawSplitString(fr,((IKeychain) stack.getItem()).toSummon().getDescription(), (int) tooltipPosX + 3, (int) tooltipPosY + 3, (int)(parent.width * 0.46875F), 0x43B5E9);
						} else if(stack.getItem() instanceof KeybladeItem) {
							float tooltipPosX = parent.width * 0.3333F;
	                    	float tooltipPosY = parent.height * 0.8F;
	                    	Utils.drawSplitString(fr,((KeybladeItem) stack.getItem()).getDescription(), (int) tooltipPosX + 3, (int) tooltipPosY + 3, (int)(parent.width * 0.46875F), 0x43B5E9);
						}
                    }
                } 
                
            }
            RenderHelper.enableStandardItemLighting();
            RenderSystem.color4f(1, 1, 1, 1);
            if (hasLabel) {
                mc.getTextureManager().bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
                matrixStack.push();
                {
                    
                    RenderHelper.disableStandardItemLighting();
                    RenderSystem.color4f(col.getRed() / 128F, col.getGreen() / 128F, col.getBlue() / 128F, 1);
                    matrixStack.translate(x - labelWidth, y, 0);
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
                matrixStack.pop();
                float centerX = (labelWidth / 2) - (fr.getStringWidth(Utils.translateToLocal(label)) / 2);
                drawString(matrixStack, fr, Utils.translateToLocal(label), (int) (x - labelWidth + centerX), y + 3, labelColour);
            }
        }


    }
}
