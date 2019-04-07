package online.kingdomkeys.kingdomkeys.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Constants;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import org.lwjgl.opengl.GL11;

public class GuiPlayerPortrait extends GuiScreen {
    @SubscribeEvent
    public void onRenderOverlayPost(RenderGameOverlayEvent event) {
//        if (!MainConfig.displayGUI())
        //          return;
        Minecraft mc = Minecraft.getInstance();
        //if(!mc.player.getCapability(ModCapabilities.PLAYER_STATS, null).getHudMode()) return;
        int screenWidth = mc.mainWindow.getScaledWidth();
        int screenHeight = mc.mainWindow.getScaledHeight();
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            GL11.glColor3f(1, 1, 1);
            ResourceLocation skin = mc.player.getLocationSkin();
            mc.getTextureManager().bindTexture(skin);
            float scale = 0.5f;
            switch (mc.gameSettings.guiScale) {
                case Constants.SCALE_AUTO:
                    scale = 0.85f;
                    break;
                case Constants.SCALE_NORMAL:
                    scale = 0.85f;
                    break;
                default:
                    scale = 0.65f;
                    break;
            }

           /* DriveStateCapability.IDriveState DS = mc.player.getCapability(ModCapabilities.DRIVE_STATE, null);
            if (DS.getActiveDriveName().equals(Strings.Form_Anti))
                GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) this.alpha);
*/

            GL11.glPushMatrix();
            {
                GL11.glTranslatef(-5,-1, 0);

                // HEAD
                int headWidth = 32;
                int headHeight = 32;
                float headPosX = 16;
                float headPosY = 32;
                float scaledHeadPosX = headPosX * scale;
                float scaledHeadPosY = headPosY * scale;

                GL11.glPushMatrix();
                {
                    GL11.glTranslatef((screenWidth - headWidth * scale) - scaledHeadPosX, (screenHeight - headHeight * scale) - scaledHeadPosY, 0);
                    GL11.glScalef(scale, scale, scale);
                    this.drawTexturedModalRect(0, 0, 32, 32, headWidth, headHeight);
                }
                GL11.glPopMatrix();

                // HAT
                int hatWidth = 32;
                int hatHeight = 32;
                float hatPosX = 16;
                float hatPosY = 32;
                float scaledHatPosX = hatPosX * scale;
                float scaledHatPosY = hatPosY * scale;

                GL11.glPushMatrix();
                {
                    GL11.glTranslatef((screenWidth - hatWidth * scale) - scaledHatPosX, (screenHeight - hatHeight * scale) - scaledHatPosY, 0);
                    GL11.glScalef(scale, scale, scale);
                    this.drawTexturedModalRect(0, 0, 160, 32, hatWidth, hatHeight);
                }
                GL11.glPopMatrix();

                // BODY
                int bodyWidth = 32;
                int bodyHeight = 64;
                float bodyPosX = 16;
                float bodyPosY = -32;
                float scaledBodyPosX = bodyPosX * scale;
                float scaledBodyPosY = bodyPosY * scale;

                GL11.glPushMatrix();
                {
                    GL11.glTranslatef((screenWidth - bodyWidth * scale) - scaledBodyPosX, (screenHeight - bodyHeight * scale) - scaledBodyPosY, 0);
                    GL11.glScalef(scale, scale, scale);
                    this.drawTexturedModalRect(0, 0, 80, 80, bodyWidth, bodyHeight);
                }
                GL11.glPopMatrix();

                // JACKET
                int jacketWidth = 32;
                int jacketHeight = 64;
                float jacketPosX = 16;
                float jacketPosY = -32;
                float scaledjacketPosX = jacketPosX * scale;
                float scaledjacketPosY = jacketPosY * scale;

                GL11.glPushMatrix();
                {
                    GL11.glTranslatef((screenWidth - bodyWidth * scale) - scaledBodyPosX, (screenHeight - bodyHeight * scale) - scaledBodyPosY, 0);
                    GL11.glScalef(scale, scale, scale);
                    this.drawTexturedModalRect(0, 0, 80, 148, bodyWidth, bodyHeight);
                }
                GL11.glPopMatrix();

                // ARMS
                int armWidth = 16;
                int armHeight = 64;
                float armRPosX = 48;
                float armRPosY = -32;
                float scaledArmRPosX = armRPosX * scale;
                float scaledArmRPosY = armRPosY * scale;
                float armLPosX = 0;
                float armLPosY = -32;
                float scaledArmLPosX = armLPosX * scale;
                float scaledArmLPosY = armLPosY * scale;

                GL11.glPushMatrix();
                {
                    GL11.glTranslatef((screenWidth - armWidth * scale) - scaledArmRPosX, (screenHeight - armHeight * scale) - scaledArmRPosY, 0);
                    GL11.glScalef(scale, scale, scale);
                    this.drawTexturedModalRect(0, 0, 176, 80, armWidth, armHeight);
                }
                GL11.glPopMatrix();

                GL11.glPushMatrix();
                {
                    GL11.glTranslatef((screenWidth - armWidth * scale) - scaledArmLPosX, (screenHeight - armHeight * scale) - scaledArmLPosY, 0);
                    GL11.glScalef(scale, scale, scale);
                    this.drawTexturedModalRect(0, 0, 176, 80, armWidth, armHeight);
                }
                GL11.glPopMatrix();
                GL11.glColor4f(100.0F, 1.0F, 1.0F, 1.0F);

                // GLOVES
                int gloveWidth = 16;
                int gloveHeight = 64;
                float gloveRPosX = 48;
                float gloveRPosY = -32;
                float scaledgloveRPosX = gloveRPosX * scale;
                float scaledgloveRPosY = gloveRPosY * scale;
                float gloveLPosX = 0;
                float gloveLPosY = -32;
                float scaledgloveLPosX = gloveLPosX * scale;
                float scaledgloveLPosY = gloveLPosY * scale;

                GL11.glPushMatrix();
                {
                    GL11.glTranslatef((screenWidth - gloveWidth * scale) - scaledgloveRPosX, (screenHeight - gloveHeight * scale) - scaledgloveRPosY, 0);
                    GL11.glScalef(scale, scale, scale);
                    this.drawTexturedModalRect(0, 0, 176, 150, gloveWidth, gloveHeight);
                }
                GL11.glPopMatrix();

                GL11.glPushMatrix();
                {
                    GL11.glTranslatef((screenWidth - gloveWidth * scale) - scaledgloveLPosX, (screenHeight - gloveHeight * scale) - scaledgloveLPosY, 0);
                    GL11.glScalef(scale, scale, scale);
                    this.drawTexturedModalRect(0, 0, 176, 150, gloveWidth, gloveHeight);
                }
                GL11.glPopMatrix();

                GL11.glColor4f(100.0F, 1.0F, 1.0F, 1.0F);

                /*if (DS.isDriveForm) {
                    ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/armour/"+ds.getActiveDriveform()+".png");
                    mc.textureManager.bindTexture(texture);

                    GL11.glPushMatrix();
                    {
                        GL11.glTranslatef((screenWidth - 32 * scale) - 16 * scale, (screenHeight - 80 * scale) - -48 * scale, 0);
                        GL11.glScalef(2, 1, 1);
                        GL11.glScalef(0.5f, 0.5f, 0.5f);
                        GL11.glScalef(scale, scale, scale);
                        this.drawTexturedModalRect(0, 0, 80, 140, 32, 80);
                    }
                    GL11.glPopMatrix();

                    GL11.glPushMatrix();
                    {
                        GL11.glTranslatef((screenWidth - 16 * scale) - 48 * scale, (screenHeight - 80 * scale) - -48 * scale, 0);
                        GL11.glScalef(2, 1, 1);
                        GL11.glScalef(0.5f, 0.5f, 0.5f);
                        GL11.glScalef(scale, scale, scale);
                        this.drawTexturedModalRect(0, 0, 64, 140, 16, 80);
                    }
                    GL11.glPopMatrix();

                    GL11.glPushMatrix();
                    {
                        GL11.glTranslatef((screenWidth - 16 * scale) - 0 * scale, (screenHeight - 80 * scale) - -48 * scale, 0);
                        GL11.glScalef(2, 1, 1);
                        GL11.glScalef(0.5f, 0.5f, 0.5f);
                        GL11.glScalef(scale, scale, scale);
                        this.drawTexturedModalRect(0, 0, 112, 140, 16, 80);
                    }
                    GL11.glPopMatrix();

                } */
            }
            GL11.glPopMatrix();
        }
    }
}

