package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

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
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment.MenuEquipmentScreen;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.awt.Color;

public class MenuEquipmentButton extends Button {

    public Screen toOpen;
    ItemStack stack;
    int colour, labelColour;
    MenuEquipmentScreen parent;
    String label;
    boolean hasLabel;
    ItemCategory category;

    public MenuEquipmentButton(ItemStack stack, int x, int y, int colour, Screen toOpen, ItemCategory category, MenuEquipmentScreen parent) {
        super(x, y, (int) (parent.width * 0.264f), 14, "", b -> {
            if (b.visible && b.active) {
                Minecraft.getInstance().displayGuiScreen(((MenuEquipmentButton)b).toOpen);
            }
        });
        this.stack = stack;
        this.colour = colour;
        this.toOpen = toOpen;
        this.parent = parent;
        hasLabel = false;
        this.category = category;
    }

    public MenuEquipmentButton(ItemStack stack, int x, int y, int colour, Screen toOpen, ItemCategory category, MenuEquipmentScreen parent, String label, int labelColour) {
        this(stack, x, y, colour, toOpen, category, parent);
        this.hasLabel = true;
        this.labelColour = labelColour;
        this.label = label;
    }

    @Override
    public void playDownSound(SoundHandler soundHandler) {
        soundHandler.play(SimpleSound.master(ModSounds.menu_select.get(), 1.0F, 1.0F));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        FontRenderer fr = mc.fontRenderer;

        float itemY = parent.height * 0.1907F;
        float bottomY = parent.height - (parent.height * 0.25F);
        if (this.y < itemY - 1 || this.y > bottomY - 1) return;

        isHovered = mouseX > x && mouseY >= y && mouseX < x + width && mouseY < y + height;
        Color col = Color.decode(String.valueOf(colour));
        RenderSystem.color4f(1, 1, 1, 1);
        float labelWidth = parent.width * 0.1348F;
        float gradientWidth = parent.width * 0.1515F;
        if (visible) {
            float itemWidth = parent.width * 0.264F;
            mc.getTextureManager().bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
            RenderSystem.pushMatrix();
            {
                RenderSystem.enableBlend();
                RenderSystem.enableAlphaTest();
                RenderHelper.disableStandardItemLighting();
                RenderSystem.color4f(col.getRed() / 128F, col.getGreen() / 128F, col.getBlue() / 128F, 1);
                RenderSystem.translatef(x + 0.6F, y, 0);
                RenderSystem.scalef(0.5F, 0.5F, 1);
                //Gradient Background
                for (int i = 0; i < height * 2; i++) {
                    RenderSystem.pushMatrix();
                    {
                        RenderSystem.scalef(((gradientWidth + itemWidth + 5) * 2) / (32F), 1.1F, 1);
                        blit(-13, i - 1, 166, 63, 32, 1);
                    }
                    RenderSystem.popMatrix();
                }

                //Left item slot
                blit(0, 0, 166, 34, 18, 28);
                //Middle item slot
                for (int i = 0; i < (itemWidth * 2) - (17 * 2); i++) {
                    blit(17 + i, 0, 184, 34, 2, 28);
                }
                //Right item slot
                blit((int)(itemWidth * 2) - 17, 0, 186, 34, 17, 28);
                RenderSystem.color4f(1, 1, 1, 1);
                //Icon
                blit(6, 4, category.getU(), category.getV(), 20, 20);
            }
            RenderSystem.popMatrix();
            if (stack != null) {
                String itemName = stack.getDisplayName().getString();
                if (stack.getItem() instanceof IKeychain) {
                    itemName = new ItemStack(((IKeychain) stack.getItem()).toSummon()).getDisplayName().getString();
                } else if (ItemStack.areItemStacksEqual(stack, ItemStack.EMPTY)) {
                    itemName = "---";
                }
                drawString(fr, itemName, x + 15, y + 3, 0xFFFFFF);
            } else {
                drawString(fr, "---", x + 15, y + 3, 0xFFFFFF);
            }
            if (isHovered) {
                mc.getTextureManager().bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
                RenderSystem.pushMatrix();
                {
                    RenderSystem.enableAlphaTest();
                    RenderSystem.translatef(x + 0.6F, y, 0);
                    RenderSystem.scalef(0.5F, 0.5F, 1);
                    //Left selected
                    blit(0, 0, 128, 34, 18, 28);
                    //Middle selected
                    for (int i = 0; i < (itemWidth * 2) - (17 * 2); i++) {
                        blit(17 + i, 0, 146, 34, 2, 28);
                    }
                    //Right selected
                    blit((int)(itemWidth * 2) - 17, 0, 148, 34, 17, 28);
                }
                RenderSystem.popMatrix();
                float iconPosX = parent.width * 0.6374F;
                float iconPosY = parent.height * 0.1833F;
                float iconHeight = parent.height * 0.3148F;
                if (stack != null) {
                    if (stack.getItem() instanceof IKeychain) {
                        ItemStack keyblade = new ItemStack(((IKeychain) stack.getItem()).toSummon());
                        RenderSystem.pushMatrix();
                        {
                            RenderSystem.enableAlphaTest();
                            RenderSystem.translatef(iconPosX, iconPosY, 0);
                            RenderSystem.scalef((float) (0.0625F * iconHeight), (float) (0.0625F * iconHeight), 1);
                            mc.getItemRenderer().renderItemAndEffectIntoGUI(keyblade, 0, 0);
                        }
                        RenderSystem.popMatrix();
                        float strPosX = parent.width * 0.634F;
                        float strPosY = parent.height * 0.5185F;
                        float strNumPosX = parent.width * 0.77F;
                        float magPosY = parent.height * 0.5657F;
                        int strength = ((IKeychain) stack.getItem()).toSummon().getStrength(stack);
                        int magic = ((IKeychain) stack.getItem()).toSummon().getMagic(stack);
                        String strengthStr = String.valueOf(strength);
                        String magicStr = String.valueOf(magic);
                        IPlayerCapabilities playerData = ModCapabilities.getPlayer(mc.player);
                        int totalStrength = playerData.getStrength() + strength;
                        int totalMagic = playerData.getMagic() + magic;
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
						drawString(fr, new TranslationTextComponent(Strings.Gui_Menu_Status_Strength).getFormattedText(), (int) strPosX, (int) strPosY, 0xEE8603);
						drawString(fr, strengthStr, (int) strNumPosX, (int) strPosY, 0xFFFFFF);
						drawString(fr, openBracketStr, (int) strNumPosX + fr.getStringWidth(strengthStr), (int) strPosY, 0xBF6004);
						drawString(fr, totalStrengthStr, (int) strNumPosX + fr.getStringWidth(strengthStr) + fr.getStringWidth(openBracketStr), (int) strPosY, 0xFBEA21);
						drawString(fr, "]", (int) strNumPosX + fr.getStringWidth(strengthStr) + fr.getStringWidth(openBracketStr) + fr.getStringWidth(totalStrengthStr), (int) strPosY, 0xBF6004);

						drawString(fr, new TranslationTextComponent(Strings.Gui_Menu_Status_Magic).getFormattedText(), (int) strPosX, (int) magPosY, 0xEE8603);
						drawString(fr, magicStr, (int) strNumPosX, (int) magPosY, 0xFFFFFF);
						drawString(fr, openBracketMag, (int) strNumPosX + fr.getStringWidth(magicStr), (int) magPosY, 0xBF6004);
						drawString(fr, totalMagicStr, (int) strNumPosX + fr.getStringWidth(magicStr) + fr.getStringWidth(openBracketMag), (int) magPosY, 0xFBEA21);
						drawString(fr, "]", (int) strNumPosX + fr.getStringWidth(magicStr) + fr.getStringWidth(openBracketMag) + fr.getStringWidth(totalMagicStr), (int) magPosY, 0xBF6004);

                        float tooltipPosX = parent.width * 0.3333F;
                        float tooltipPosY = parent.height * 0.8F;
                        fr.drawSplitString(((IKeychain) stack.getItem()).toSummon().getDescription(), (int) tooltipPosX + 3, (int) tooltipPosY + 3, (int)(parent.width * 0.46875F), 0x43B5E9);
                    } else if (stack.getItem() instanceof ArmorItem) {
                        //ArmorItem armour = (ArmorItem) stack.getItem();
                        //int armourAmount = armour.getArmorMaterial().getDamageReductionAmount(armour.get)
                    } else if (stack.getItem() instanceof IOrgWeapon) {
                        //TODO org
                    }
                }
            }
            RenderHelper.enableStandardItemLighting();
            RenderSystem.color4f(1, 1, 1, 1);
            if (hasLabel) {
                mc.getTextureManager().bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
                RenderSystem.pushMatrix();
                {
                    RenderSystem.enableAlphaTest();
                    RenderHelper.disableStandardItemLighting();
                    RenderSystem.color4f(col.getRed() / 128F, col.getGreen() / 128F, col.getBlue() / 128F, 1);
                    RenderSystem.translatef(x - labelWidth, y, 0);
                    RenderSystem.scalef(0.5F, 0.5F, 1);

                    //Left label
                    blit(0, 0, 166, 34, 17, 28);
                    //Middle label
                    for (int i = 0; i  < (labelWidth * 2) - (17 + 14); i++) {
                        blit(17 + i, 0, 184, 34, 1, 28);
                    }
                    //Right label
                    blit((int)(labelWidth * 2) - 14, 0, 204, 34, 14, 28);
                }
                RenderSystem.popMatrix();
                float centerX = (labelWidth / 2) - (fr.getStringWidth(Utils.translateToLocal(label)) / 2);
                drawString(fr, Utils.translateToLocal(label), (int) (x - labelWidth + centerX), y + 3, labelColour);
            }
        }


    }
}
