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
public class HPGui extends Screen {
    int hpBarWidth;
    int guiHeight = 10;

    int counter = 0;

    public HPGui() {
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
            mc.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));

            int screenWidth = mc.getMainWindow().getScaledWidth();
            int screenHeight = mc.getMainWindow().getScaledHeight();

            float scale = 1f;
            switch(mc.gameSettings.guiScale){
                case Constants.SCALE_AUTO:
                    scale = 0.85F;
                    break;
            }
            float scaleFactor = 1.5F;

            hpBarWidth = (int) ((int) player.getHealth() * scaleFactor);
           	int hpBarMaxWidth = (int) ((int) player.getMaxHealth() * scaleFactor);

           // hpBarWidth = (int) (60 * scaleFactor);
            //int hpBarMaxWidth = (int)(120 * scaleFactor);

            GL11.glPushMatrix();
            {
                GL11.glTranslatef((screenWidth - hpBarMaxWidth * scale) - 8 * scale, (screenHeight - guiHeight * scale) - 2 * scale, 0);
                GL11.glScalef(scale, scale, scale);
                drawHPBarBack(0, 0, hpBarMaxWidth, scale);
            }
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            {
                GL11.glTranslatef((screenWidth - (hpBarWidth) * scale) - 8 * scale, (screenHeight - (guiHeight) * scale) - 1 * scale -0.1F, 0);
                GL11.glScalef(scale, scale, scale);
                drawHPBarTop(0, 0, (int) Math.ceil(hpBarWidth), scale, player);
            }
            GL11.glPopMatrix();
        }
    }

    public void drawHPBarBack(int posX, int posY, int width, float scale) {
        Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
        GL11.glPushMatrix();
        {
            //Left
            GL11.glPushMatrix();
            {
                GL11.glTranslatef(scale * posX, scale * posY, 0);
                GL11.glScalef(scale, scale, 0);
                blit(0, 0, 0, 0, 2, 12);
            }
            GL11.glPopMatrix();

            //Middle
            GL11.glPushMatrix();
            {
                GL11.glTranslatef((posX + 2) * scale, posY * scale, 0);
                GL11.glScalef(width, scale, 0);
                blit(0, 0, 2, 0, 1, 12);
            }
            GL11.glPopMatrix();

            //Right
            GL11.glPushMatrix();
            {
                GL11.glTranslatef((posX + 2) * scale + width, scale * posY, 0);
                GL11.glScalef(scale, scale, 0);
                blit(0, 0, 3, 0, 2, 12);
            }
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();

    }

    public void drawHPBarTop(int posX, int posY, int width, float scale, PlayerEntity player) {
        Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
        GL11.glPushMatrix();
        {
            GL11.glTranslatef((posX + 2) * scale, (posY + 2) * scale, 0);
            GL11.glScalef(width, scale, 0);
            int v = player.getHealth() >= player.getMaxHealth() / 4 ? 12 : 22;
            blit(0, -1, 2, v, 1, 8);
        }
        GL11.glPopMatrix();

    }

    /*public void drawHPBarTopRed(int posX, int posY, int width, float scale) {
        Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/hpbar.png"));
        GL11.glPushMatrix();
        {
            GL11.glPushMatrix();
            {
                GL11.glTranslatef(scale * (posX + 2), scale * (posY + 2), 0);
                GL11.glScalef(scale, scale, 0);
                drawTexturedModalRect(0, 0, 2, 22, 2, 6);
            }
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            {
                GL11.glTranslatef((posX + 4) * scale, (posY + 2) * scale, 0);
                GL11.glScalef(width, scale, 0);
                drawTexturedModalRect(0, 0, 3, 22, 1, 6);
            }
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            {
                GL11.glTranslatef((posX + 4) * scale + width, scale * (posY + 2), 0);
                GL11.glScalef(scale, scale, 0);
                drawTexturedModalRect(0, 0, 4, 22, 2, 6);
            }
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }*/
}
