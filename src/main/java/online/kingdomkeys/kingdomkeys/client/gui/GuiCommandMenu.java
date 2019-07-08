package online.kingdomkeys.kingdomkeys.client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.handler.EntityEvents;
import online.kingdomkeys.kingdomkeys.lib.Utils;

//TODO cleanup
public class GuiCommandMenu extends Screen {

    public GuiCommandMenu() {
        super(new TranslationTextComponent(""));
    }

    Minecraft mc = Minecraft.getInstance();

    public static final int TOP = 5, ATTACK = 4, MAGIC = 3, ITEMS = 2, DRIVE = 1;

    // int selected = ATTACK;

    int TOP_WIDTH = 70;
    int TOP_HEIGHT = 15;

    int MENU_WIDTH = 71;
    int MENU_HEIGHT = 15;

    int iconWidth = 10;

    int textX = 0;

   /* public static List<PortalCoords> portalCommands;
    public static List<String> driveCommands, spells, items;
    public static List<Ability> attackCommands;*/

    public static final int SUB_MAIN = 0, SUB_MAGIC = 1, SUB_ITEMS = 2, SUB_DRIVE = 3, SUB_PORTALS = 4, SUB_ATTACKS = 5;

    public static final int NONE = 0;
    public static int selected = ATTACK;
    public static int submenu = 0, magicselected = 0, potionselected = 0, driveselected = 0, portalSelected = 0, attackSelected = 0;

    ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/commandmenu.png");

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onRenderOverlayPost(RenderGameOverlayEvent event) {
        //if (mc.player.getCapability(ModCapabilities.PLAYER_STATS, null).getHudMode()) {
            if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT){// && !mc.ingameGUI.getChatGUI().getChatOpen()) {
                GL11.glPushMatrix();
                {
                    drawCommandMenu(mc.mainWindow.getScaledWidth(),mc.mainWindow.getScaledHeight());
                }
                GL11.glPopMatrix();
            }
       // }
    }

    int alpha = 255;
    float scale = 1.05f;
    byte[] orgColor = { (byte) 200, (byte) 200, (byte) 200 };
    byte[] attackMenuColor = { (byte) 255, (byte) 200, (byte) 60 };
    byte[] portalMenuColor = { (byte) 100, (byte) 100, (byte) 100 };
    byte[] combatModeColor = { (byte) 255, (byte) 0, (byte) 0 };
    byte[] normalModeColor = { (byte)10, (byte)60, (byte)255 };
    byte[] magicMenuColor = { (byte) 100, (byte) 0, (byte) 255 };
    byte[] itemsMenuColor = { (byte) 70, (byte) 255, (byte) 80 };
    byte[] driveMenuColor = { (byte) 0, (byte) 255, (byte) 255 };


    private void paintWithColorArray(byte[] array, byte alpha) {
        if (EntityEvents.isHostiles)
            GL11.glColor4ub(combatModeColor[0],combatModeColor[1],combatModeColor[2], (byte) alpha);
        else {
      /*  if (Minecraft.getMinecraft().player.getCapability(ModCapabilities.ORGANIZATION_XIII, null).getMember() != Utils.OrgMember.NONE && array == normalModeColor) {
            GL11.glColor4ub(orgColor[0], orgColor[1], orgColor[2], (byte) alpha);
        } else {*/
            GL11.glColor4ub(normalModeColor[0],normalModeColor[1],normalModeColor[2], (byte) alpha);
            // }
        }

    }

    public void drawCommandMenu(int width, int height) {
        drawTop(width, height);
        drawAttack(width, height);
        drawMagic(width, height);
        drawItems(width, height);
        drawDrive(width, height);
    }

    public void drawTop(int width, int height){
        GL11.glPushMatrix();
        {
            GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) alpha);
            mc.textureManager.bindTexture(texture);
            GL11.glTranslatef(0, (height - MENU_HEIGHT * scale * TOP), 0);
            GL11.glScalef(scale, scale, scale);
            if (submenu != 0)
                GL11.glColor4ub((byte) 80, (byte) 80, (byte) 80, (byte) alpha);

            textX = 0;
            paintWithColorArray(normalModeColor, (byte) alpha);
            blit(0, 0, 0, 0, TOP_WIDTH, TOP_HEIGHT);
            if (this.submenu == 0) {
                drawString(mc.fontRenderer, I18n.format("COMMANDS"), 6, 4, 0xFFFFFF);
            }
        }
        GL11.glPopMatrix();
    }

    public void drawAttack(int width, int height){
        GL11.glPushMatrix();
        {
            GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) alpha);
            mc.textureManager.bindTexture(texture);

            int u;
            int v = 0;
            int x = 0;

            GL11.glTranslatef(x, (height - MENU_HEIGHT * scale * ATTACK), 0);
            GL11.glScalef(scale, scale, scale);

            if (submenu != 0)
                GL11.glColor4ub((byte) 80, (byte) 80, (byte) 80, (byte) alpha);

            if (selected == ATTACK) { // Selected
                textX = 5;
                paintWithColorArray(normalModeColor, (byte) alpha);

                // Draw slot
                blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);

                GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) alpha);

                // Draw Icon
              //  if (organization) {
                //    drawTexturedModalRect(60, 2, 140 + ((selected + 1) * iconWidth) - iconWidth, 18, iconWidth, iconWidth);
                //} else {
                    blit(60, 2, 140 + (selected * iconWidth) - iconWidth, 18, iconWidth, iconWidth);
                //}

            } else { // Not selected
                textX = 0;
                paintWithColorArray(normalModeColor, (byte) alpha);
                blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
            }

            if (this.submenu == 0) {
                drawString(mc.fontRenderer, I18n.format("Attack"), 6 + textX, 4, 0xFFFFFF);

                /*if(Minecraft.getInstance().player.getCapability(ModCapabilities.ORGANIZATION_XIII, null).getMember() == Utils.OrgMember.XIGBAR) {
                    if(player.getHeldItemMainhand() != null) {
                        if(player.getHeldItemMainhand().getItem() instanceof ItemArrowguns) {
                            ItemStack weapon = player.getHeldItemMainhand();
                            if(weapon.hasTagCompound()) {
                                if(weapon.getTagCompound().hasKey("ammo")) {
                                    int ammo = weapon.getTagCompound().getInteger("ammo");
                                    drawString(mc.fontRenderer, ammo+"", textX+TOP_WIDTH, 4, 0xFFFFFF);
                                }
                            }

                        }
                    }
                }*/
            }

        }
        GL11.glPopMatrix();
    }

    public void drawMagic(int width, int height){
        GL11.glPushMatrix();
        {
            GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) alpha);
            mc.textureManager.bindTexture(texture);

            int u;
            int v = 0;
            int x = 0;

            GL11.glTranslatef(x, (height - MENU_HEIGHT * scale * MAGIC), 0);
            GL11.glScalef(scale, scale, scale);

            if (submenu != 0)
                GL11.glColor4ub((byte) 80, (byte) 80, (byte) 80, (byte) alpha);

            if (selected == MAGIC) { // Selected
                textX = 5;
                paintWithColorArray(normalModeColor, (byte) alpha);
                // Draw slot
                blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);
                GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) alpha);
                blit(60, 2, 140 + (selected * iconWidth) - iconWidth, 18, iconWidth, iconWidth);

            } else { // Not selected
                textX = 0;
                paintWithColorArray(normalModeColor, (byte) alpha);
                blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
            }

            if (this.submenu == 0) {
                drawString(mc.fontRenderer, I18n.format("Magic"), 6 + textX, 4, 0xFFFFFF);
            }

        }
        GL11.glPopMatrix();
    }

    public void drawItems(int width, int height){
        GL11.glPushMatrix();
        {
            GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) alpha);
            mc.textureManager.bindTexture(texture);

            int u;
            int v = 0;
            int x = 0;

            GL11.glTranslatef(x, (height - MENU_HEIGHT * scale * ITEMS), 0);
            GL11.glScalef(scale, scale, scale);

            if (submenu != 0)
                GL11.glColor4ub((byte) 80, (byte) 80, (byte) 80, (byte) alpha);

            if (selected == ITEMS) { // Selected
                textX = 5;
                paintWithColorArray(normalModeColor, (byte) alpha);
                // Draw slot
                blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);
                GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) alpha);
                blit(60, 2, 140 + (selected * iconWidth) - iconWidth, 18, iconWidth, iconWidth);

            } else { // Not selected
                textX = 0;
                paintWithColorArray(normalModeColor, (byte) alpha);
                blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
            }

            if (this.submenu == 0) {
                drawString(mc.fontRenderer, I18n.format("Items"), 6 + textX, 4, 0xFFFFFF);
            }

        }
        GL11.glPopMatrix();
    }

    public void drawDrive(int width, int height){
        GL11.glPushMatrix();
        {
            GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) alpha);
            mc.textureManager.bindTexture(texture);

            int u;
            int v = 0;
            int x = 0;

            GL11.glTranslatef(x, (height - MENU_HEIGHT * scale * DRIVE), 0);
            GL11.glScalef(scale, scale, scale);

            if (submenu != 0)
                GL11.glColor4ub((byte) 80, (byte) 80, (byte) 80, (byte) alpha);

            if (selected == DRIVE) { // Selected
                textX = 5;
                paintWithColorArray(normalModeColor, (byte) alpha);
                // Draw slot
                blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);
                GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) alpha);
                blit(60, 2, 140 + (selected * iconWidth) - iconWidth, 18, iconWidth, iconWidth);

            } else { // Not selected
                textX = 0;
                paintWithColorArray(normalModeColor, (byte) alpha);
                blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
            }

            if (this.submenu == 0) {
                drawString(mc.fontRenderer, I18n.format("Drive"), 6 + textX, 4, 0xFFFFFF);
            }

        }
        GL11.glPopMatrix();
    }
}
