package online.kingdomkeys.kingdomkeys.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Constants;

//TODO cleanup + comments
public class MPGui extends Screen {
    int guiWidth = 173;
    int mpBarWidth;
    int guiHeight = 10;
    int noborderguiwidth = 171;

    int counter = 0;

    public MPGui() {
        super(new TranslationTextComponent(""));
    }

    @SubscribeEvent
    public void onRenderOverlayPost(RenderGameOverlayEvent event) {
       // if (!MainConfig.displayGUI())
        //    return;
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
       //if (!player.getCapability(ModCapabilities.PLAYER_STATS, null).getHudMode())
        //    return;
        if (event.getType().equals(RenderGameOverlayEvent.ElementType.HEALTH) && event.isCancelable()) {
            // if (!MainConfig.client.hud.EnableHeartsOnHUD)
           // event.setCanceled(true);
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            mc.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/mpbar.png"));

            int screenWidth = width;// mc.mainWindow.getScaledWidth();
            int screenHeight = height;//mc.mainWindow.getScaledHeight();

            float scale = 1f;
            switch(mc.gameSettings.guiScale){
                case Constants.SCALE_AUTO:
                    scale = 0.85F;
                    break;
            }
            float scaleFactor = 0.9F;

            mpBarWidth = (int) ((int) 40 * scaleFactor);
            int mpBarMaxWidth = (int) (120 * scaleFactor);

            GL11.glPushMatrix();
            {
                GL11.glTranslatef((screenWidth - mpBarMaxWidth * scale) - 80 * scale, (screenHeight - guiHeight * scale) - 9 * scale, 0);
                GL11.glScalef(scale, scale/1.3F, scale);
                drawMPBarBack(0, 0, mpBarMaxWidth, scale);
            }

            GL11.glPopMatrix();
            {
                GL11.glPushMatrix();
                GL11.glTranslatef((screenWidth - ((int)mpBarWidth) * scale) - 80 * scale, (screenHeight - (guiHeight) * scale) - 9 * scale, 0);
                GL11.glScalef(scale, scale/1.3F, scale);
                drawMPBarTop(0, 0, (int) Math.ceil(mpBarWidth), scale);
            }
            GL11.glPopMatrix();
        }
    }

    public void drawMPBarBack(int posX, int posY, int width, float scale) {
        Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/mpbar.png"));
        GL11.glPushMatrix();
        {
            //Left Margin
            GL11.glPushMatrix();
            {
                GL11.glTranslatef(scale * posX, scale * posY, 0);
                GL11.glScalef(scale, scale, 0);
                blit(0, 0, 0, 0, 2, 12);
            }
            GL11.glPopMatrix();

            //Background
            GL11.glPushMatrix();
            {
                GL11.glTranslatef((posX + 2) * scale, posY * scale, 0);
                GL11.glScalef(width, scale, 0);

               // int v=STATS.getRecharge() ? 8 : 2;
                int v = 2;

                blit(0, 0, v, 0, 1, 12);
            }
            GL11.glPopMatrix();

            //Right Margin
            GL11.glPushMatrix();
            {
                GL11.glTranslatef((posX + 2) * scale + width, scale * posY, 0);
                GL11.glScalef(scale, scale, 0);
                blit(0, 0, 3, 0, 2, 12);
            }
            GL11.glPopMatrix();

            //MP Icon
            GL11.glPushMatrix();
            {
               // int v=STATS.getRecharge() ? 45 : 32;
               int v = 32;
                GL11.glTranslatef((posX + 2) * scale + width+1, scale * posY, 0);
                GL11.glScalef(scale*0.8F, scale, 1);
                blit(0, 0, 0, v, 23, 12);
            }
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();

    }

    public void drawMPBarTop(int posX, int posY, int width, float scale) {
        Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/mpbar.png"));
        GL11.glPushMatrix();
        {
            GL11.glTranslatef((posX + 2) * scale, (posY + 2) * scale, 0);
            GL11.glScalef(width, scale, 0);
           // int v = STATS.getRecharge() ? 22 : 12;
           // int h = STATS.getRecharge() ? 8 : 7;
            int v = 12, h = 7;
            blit(0, 0, 2, v, 1, h);
        }
        GL11.glPopMatrix();

    }
}
