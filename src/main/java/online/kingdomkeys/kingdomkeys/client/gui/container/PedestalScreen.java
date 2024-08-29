package online.kingdomkeys.kingdomkeys.client.gui.container;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.CheckboxButton;
import online.kingdomkeys.kingdomkeys.menu.PedestalMenu;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSPedestalConfig;
import org.jetbrains.annotations.NotNull;

public class PedestalScreen extends AbstractContainerScreen<PedestalMenu> {

	private static final ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/pedestal.png");

    public PedestalScreen(PedestalMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);
		this.imageHeight = 186;
    }

    final float rotationSpeedMax = 5.0F;
    final float bobSpeedMax = 0.5F;
    final float scaleMax = 2.0F;
    final float heightMax = 3.0F;
    ExtendedSlider rotationSpeedSlider, bobSpeedSlider, scaleSlider, heightSlider;
    CheckboxButton pauseCheckbox, flippedCheckbox;
    ExtendedButton reset;

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(scaleSlider = new ExtendedSlider(leftPos + 8, topPos + 30, 50, 10, Component.translatable(""), Component.translatable(""), 0.2D, scaleMax, menu.TE.getScale(), 0, 0, false));
        addRenderableWidget(heightSlider = new ExtendedSlider(leftPos + 8, topPos + 42, 50, 10, Component.translatable(""), Component.translatable(""), 0, heightMax, menu.TE.getBaseHeight(), 0, 0, false));
        addRenderableWidget(rotationSpeedSlider = new ExtendedSlider(leftPos + 8, topPos + 54, 50, 10, Component.translatable(""), Component.translatable(""), -rotationSpeedMax, rotationSpeedMax, menu.TE.getRotationSpeed(), 0, 0, false));
        addRenderableWidget(bobSpeedSlider = new ExtendedSlider(leftPos + 8, topPos + 66, 50, 10, Component.translatable(""), Component.translatable(""), 0, bobSpeedMax, menu.TE.getBobSpeed(), 0, 0, false));
        addRenderableWidget(pauseCheckbox = new CheckboxButton(leftPos + 8, topPos + 18, "Pause", menu.TE.isPaused()));
        addRenderableWidget(flippedCheckbox = new CheckboxButton(leftPos + 60, topPos + 18, "Flip", menu.TE.isFlipped()));
        addRenderableWidget(reset = new ExtendedButton(leftPos + imageWidth - 53, topPos + 80, 45, 15, Component.translatable("Reset"), p -> {
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
            flippedCheckbox.setChecked(false);
            PacketHandler.sendToServer(new CSPedestalConfig(menu.TE.getBlockPos(), menu.TE.getRotationSpeed(), menu.TE.getBobSpeed(), menu.TE.getSavedRotation(), menu.TE.getSavedHeight(), menu.TE.getBaseHeight(), menu.TE.getScale(), menu.TE.isPaused(), menu.TE.isFlipped()));
        }));
    }

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (p_keyPressed_1_ == 256 || p_keyPressed_1_ == Minecraft.getInstance().options.keyInventory.getKey().getValue()) { //256 = Esc
        	PacketHandler.sendToServer(new CSPedestalConfig(menu.TE.getBlockPos(), menu.TE.getRotationSpeed(), menu.TE.getBobSpeed(), menu.TE.getSavedRotation(), menu.TE.getSavedHeight(), menu.TE.getBaseHeight(), menu.TE.getScale(), menu.TE.isPaused(), menu.TE.isFlipped()));
        }
        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    protected void containerTick() {
        menu.TE.setSpeed((float) rotationSpeedSlider.getValue(), (float) bobSpeedSlider.getValue());
        menu.TE.setPause(pauseCheckbox.isChecked());
        menu.TE.setFlipped(flippedCheckbox.isChecked());
        menu.TE.setScale((float) scaleSlider.getValue());
        menu.TE.setBaseHeight((float) heightSlider.getValue());
        if (pauseCheckbox.isChecked())
            menu.TE.saveTransforms(menu.TE.getCurrentRotation(), menu.TE.getCurrentHeight());
        super.containerTick();
    }

    @Override
    public void render(@NotNull GuiGraphics gui, int p_render_1_, int p_render_2_, float p_render_3_) {
    	 this.renderBackground(gui, p_render_1_, p_render_2_, p_render_3_);
         super.render(gui, p_render_1_, p_render_2_, p_render_3_);
         this.renderTooltip(gui, p_render_1_, p_render_2_);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics gui, int mouseX, int mouseY) {
        gui.drawString(font, this.title.getString(), 8.0F, 6.0F, 4210752, false);
        gui.drawString(font, "Scale " + String.format("%.2f", scaleSlider.getValue()) + "x", 60.0F, 31.0F, 4210752, false);
        gui.drawString(font, "Height " + String.format("%.2f", heightSlider.getValue()), 60.0F, 43.0F, 4210752, false);
        gui.drawString(font, "Rotation Speed " + String.format("%.2f", rotationSpeedSlider.getValue()), 60.0F, 55.0F, 4210752, false);
        gui.drawString(font, "Bob Speed " + String.format("%.2f", bobSpeedSlider.getValue()), 60.0F, 67.0F, 4210752, false);
        gui.drawString(font, this.playerInventoryTitle.getString(), 8.0F, (float)(this.imageHeight - 96 + 2), 4210752, false);
       // super.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics gui, float partialTicks, int mouseX, int mouseY) {
    	Minecraft mc = Minecraft.getInstance();
       // RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int xPos = (width - imageWidth) / 2;
		int yPos = (height / 2) - (imageHeight / 2);
		gui.blit(texture, xPos, yPos, 0, 0, imageWidth, imageHeight);
    }
    
    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
    	if(pMouseX >= scaleSlider.getX() && pMouseX <= scaleSlider.getX()+width && pMouseY >= scaleSlider.getY() && pMouseY <= scaleSlider.getY() + scaleSlider.getHeight())
    		scaleSlider.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    	if(pMouseX >= heightSlider.getX() && pMouseX <= heightSlider.getX()+width && pMouseY >= heightSlider.getY() && pMouseY <= heightSlider.getY() + heightSlider.getHeight())
    		heightSlider.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    	if(pMouseX >= rotationSpeedSlider.getX() && pMouseX <= rotationSpeedSlider.getX()+width && pMouseY >= rotationSpeedSlider.getY() && pMouseY <= rotationSpeedSlider.getY() + rotationSpeedSlider.getHeight())
    		rotationSpeedSlider.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    	if(pMouseX >= bobSpeedSlider.getX() && pMouseX <= bobSpeedSlider.getX()+width && pMouseY >= bobSpeedSlider.getY() && pMouseY <= bobSpeedSlider.getY() + bobSpeedSlider.getHeight())
    		bobSpeedSlider.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    	return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);

    }

    @Override
    public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
        return super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
    }
}
