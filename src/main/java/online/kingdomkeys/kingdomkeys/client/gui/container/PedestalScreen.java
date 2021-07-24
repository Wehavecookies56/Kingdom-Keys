package online.kingdomkeys.kingdomkeys.client.gui.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fmlclient.gui.widget.ExtendedButton;
import net.minecraftforge.fmlclient.gui.widget.Slider;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.CheckboxButton;
import online.kingdomkeys.kingdomkeys.container.PedestalContainer;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSPedestalConfig;

public class PedestalScreen extends AbstractContainerScreen<PedestalContainer> {

	private static final String texture = KingdomKeys.MODID+":textures/gui/pedestal.png";

    public PedestalScreen(PedestalContainer container, Inventory inventory, Component title) {
        super(container, inventory, title);
		this.imageHeight = 186;

    }

    final float rotationSpeedMax = 5.0F;
    final float bobSpeedMax = 0.5F;
    final float scaleMax = 2.0F;
    final float heightMax = 2.0F;
    Slider rotationSpeedSlider, bobSpeedSlider, scaleSlider, heightSlider;
    CheckboxButton pauseCheckbox;
    ExtendedButton reset;


    @Override
    protected void init() {
        super.init();
        float current = (rotationSpeedMax / 100.0F) * (menu.TE.getRotationSpeed() * (100.0F/(PedestalTileEntity.DEFAULT_ROTATION_SPEED * rotationSpeedMax)));
        addWidget(scaleSlider = new Slider(leftPos + 8, topPos + 30, 50, 10, new TranslatableComponent(""), new TranslatableComponent(""), 0.2D, scaleMax, menu.TE.getScale(), true, false, h -> {}));
        addWidget(heightSlider = new Slider(leftPos + 8, topPos + 42, 50, 10, new TranslatableComponent(""), new TranslatableComponent(""), 0, heightMax, menu.TE.getBaseHeight(), true, false, h -> {}));
        addWidget(rotationSpeedSlider = new Slider(leftPos + 8, topPos + 54, 50, 10, new TranslatableComponent(""), new TranslatableComponent(""), -rotationSpeedMax, rotationSpeedMax, menu.TE.getRotationSpeed(), true, false, h -> { }));
        addWidget(bobSpeedSlider = new Slider(leftPos + 8, topPos + 66, 50, 10, new TranslatableComponent(""), new TranslatableComponent(""), 0, bobSpeedMax, menu.TE.getBobSpeed(), true, false, h -> {}));
        addWidget(pauseCheckbox = new CheckboxButton(leftPos + 8, topPos + 18, "Pause", menu.TE.isPaused()));
        addWidget(reset = new ExtendedButton(leftPos + imageWidth - 53, topPos + 80, 45, 15, new TranslatableComponent("Reset"), p -> {
            menu.TE.setPause(false);
            menu.TE.setCurrentTransforms(PedestalTileEntity.DEFAULT_ROTATION, PedestalTileEntity.DEFAULT_HEIGHT);
            menu.TE.setSpeed(PedestalTileEntity.DEFAULT_ROTATION_SPEED, PedestalTileEntity.DEFAULT_BOB_SPEED);
            menu.TE.setScale(PedestalTileEntity.DEFAULT_SCALE);
            menu.TE.setBaseHeight(PedestalTileEntity.DEFAULT_HEIGHT);
            rotationSpeedSlider.setValue(PedestalTileEntity.DEFAULT_ROTATION_SPEED);
            bobSpeedSlider.setValue(PedestalTileEntity.DEFAULT_BOB_SPEED);
            scaleSlider.setValue(PedestalTileEntity.DEFAULT_SCALE);
            heightSlider.setValue(PedestalTileEntity.DEFAULT_HEIGHT);
            pauseCheckbox.setChecked(false);
            PacketHandler.sendToServer(new CSPedestalConfig(menu.TE.getBlockPos(), menu.TE.getRotationSpeed(), menu.TE.getBobSpeed(), menu.TE.getSavedRotation(), menu.TE.getSavedHeight(), menu.TE.getBaseHeight(), menu.TE.getScale(), menu.TE.isPaused()));
        }));
    }

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (p_keyPressed_1_ == 256 || p_keyPressed_1_ == Minecraft.getInstance().options.keyInventory.getKey().getValue()) { //256 = Esc
            PacketHandler.sendToServer(new CSPedestalConfig(menu.TE.getBlockPos(), menu.TE.getRotationSpeed(), menu.TE.getBobSpeed(), menu.TE.getSavedRotation(), menu.TE.getSavedHeight(), menu.TE.getBaseHeight(), menu.TE.getScale(), menu.TE.isPaused()));
        }
        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    public void tick() {
        menu.TE.setSpeed((float) rotationSpeedSlider.getValue(), (float) bobSpeedSlider.getValue());
        menu.TE.setPause(pauseCheckbox.isChecked());
        menu.TE.setScale((float) scaleSlider.getValue());
        menu.TE.setBaseHeight((float) heightSlider.getValue());
        if (pauseCheckbox.isChecked())
            menu.TE.saveTransforms(menu.TE.getCurrentRotation(), menu.TE.getCurrentHeight());
        super.tick();
    }

    @Override
    public void render(PoseStack matrixStack, int p_render_1_, int p_render_2_, float p_render_3_) {
    	 this.renderBackground(matrixStack);
         super.render(matrixStack, p_render_1_, p_render_2_, p_render_3_);
         this.renderTooltip(matrixStack, p_render_1_, p_render_2_);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, this.title.getString(), 8.0F, 6.0F, 4210752);
        this.font.draw(matrixStack, "Scale " + String.format("%.2f", scaleSlider.getValue()) + "x", 60.0F, 31.0F, 4210752);
        this.font.draw(matrixStack, "Height " + String.format("%.2f", heightSlider.getValue()), 60.0F, 43.0F, 4210752);
        this.font.draw(matrixStack, "Rotation Speed " + String.format("%.2f", rotationSpeedSlider.getValue()), 60.0F, 55.0F, 4210752);
        this.font.draw(matrixStack, "Bob Speed " + String.format("%.2f", bobSpeedSlider.getValue()), 60.0F, 67.0F, 4210752);
        this.font.draw(matrixStack, this.inventory.getDisplayName().getString(), 8.0F, (float)(this.imageHeight - 96 + 2), 4210752);
       // super.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
    	Minecraft mc = Minecraft.getInstance();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindForSetup(new ResourceLocation(texture));

        int xPos = (width - imageWidth) / 2;
		int yPos = (height / 2) - (imageHeight / 2);
		blit(matrixStack, xPos, yPos, 0, 0, imageWidth, imageHeight);

    }


    @Override
    public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
        rotationSpeedSlider.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
        bobSpeedSlider.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
        scaleSlider.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
        heightSlider.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
        return super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
    }
}
