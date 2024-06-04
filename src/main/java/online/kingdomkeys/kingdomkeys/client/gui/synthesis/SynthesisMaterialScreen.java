package online.kingdomkeys.kingdomkeys.client.gui.synthesis;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map.Entry;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterable;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuStockItem;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSCloseMoogleGUI;
import online.kingdomkeys.kingdomkeys.network.cts.CSDepositMaterials;
import online.kingdomkeys.kingdomkeys.network.cts.CSSyncAllClientDataPacket;
import online.kingdomkeys.kingdomkeys.network.cts.CSTakeMaterials;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.synthesis.material.ModMaterials;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class SynthesisMaterialScreen extends MenuFilterable {
		
	MenuButton deposit,back;
	Button prev,next, take;
	MenuBox boxL, boxR;
	EditBox amountBox;
	
	int page = 0;
	int itemsPerPage = 14;
		
	SynthesisScreen parent;

	public SynthesisMaterialScreen(SynthesisScreen parent) {
		super(Strings.Gui_Synthesis_Materials, new Color(0,255,0));
		drawPlayerInfo = true;
		this.parent = parent;
	}
	
	public SynthesisMaterialScreen(String inv, String name, int moogle) {
		this(new SynthesisScreen(inv, name, moogle));
	}

	@Override
    public void action(ResourceLocation stackRL, ItemStack stack) {
    	super.action(stackRL, stack);
    	Material mat = ModMaterials.registry.get().getValue(new ResourceLocation(KingdomKeys.MODID,"mat_"+stackRL.getPath()));
    	if(mat == null)
    		return;
    	int amount = ModCapabilities.getPlayer(minecraft.player).getMaterialAmount(mat);
		amountBox.setValue(""+Math.min(64, amount));
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		if (delta > 0 && prev.visible) {
			action("prev");
			return true;
		} else if (delta < 0 && next.visible) {
			action("next");
			return true;
		}

		return false;
	}

	protected void action(String string) {
		switch(string) {
		case "prev":
			page--;
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			break;
		case "next":
			page++;
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			break;
		case "deposit":
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			
			LocalPlayer player = minecraft.player;
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			try {
				for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
					ItemStack stack = player.getInventory().getItem(i);

					if (!ItemStack.matches(stack, ItemStack.EMPTY)) {

						if (ModMaterials.registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "mat_" + Utils.getItemRegistryName(stack.getItem()).getPath())) != null) {
							Material mat = ModMaterials.registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "mat_" + Utils.getItemRegistryName(stack.getItem()).getPath()));
							playerData.addMaterial(mat, stack.getCount());
							player.getInventory().setItem(i, ItemStack.EMPTY);
						}
					}
				}
			} catch (ConcurrentModificationException e) {
				e.printStackTrace();
			}
			PacketHandler.sendToServer(new CSDepositMaterials(parent.invFile, parent.name, parent.moogle));
			break;
		case "back":
			minecraft.setScreen(new SynthesisScreen(parent.invFile, parent.name, parent.moogle));
			break;
		case "take":
			ItemStack selectedItemstack = new ItemStack(ForgeRegistries.ITEMS.getValue(selectedRL));

			if(!ItemStack.isSameItem(selectedItemstack, ItemStack.EMPTY) && minecraft.player.getInventory().getFreeSlot() > -1) {
				try { 
					Integer.parseInt(amountBox.getValue());
					PacketHandler.sendToServer(new CSTakeMaterials(selectedItemstack.getItem(), Integer.parseInt(amountBox.getValue()), parent.invFile, parent.name == null ? "" : parent.name, parent.moogle));
				} catch (NumberFormatException e) {
					KingdomKeys.LOGGER.error("NaN "+amountBox.getValue());
				}
			}
			break;
		}
		
		updateButtons();
	}

	private void updateButtons() {

	}

	
	@Override
	public void init() {
		float boxPosX = (float) width * 0.2F;
		float topBarHeight = (float) height * 0.17F;
		float boxWidth = (float) width * 0.33F;
		float middleHeight = (float) height * 0.6F;
		boxL = new MenuBox((int) boxPosX, (int) topBarHeight, (int) boxWidth, (int) middleHeight, new Color(4, 4, 68));
		boxR = new MenuBox(boxL.getX() + boxL.getWidth(), (int) topBarHeight, (int) (boxWidth), (int) middleHeight, new Color(4, 4, 68));
		
		super.init();
		initItems();

		itemsPerPage = (int) (middleHeight / 14);
	}
	

	@Override
	public void initItems() {
		float buttonPosX = (float) width * 0.008F;
		int button_statsY = (int) topBarHeight + 10;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		float invPosX = boxL.getX();
		float invPosY = (float) height * 0.1851F;
		
		inventory.clear();
		children().clear();
		renderables.clear();
		//filterBar.buttons.forEach(this::addButton);

		List<ItemStack> items = new ArrayList<>();
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);

		for (Entry<String, Integer> mat : playerData.getMaterialMap().entrySet()) {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(mat.getKey()));
			items.add(new ItemStack(item, mat.getValue()));
		}
		items.sort(Comparator.comparing(Utils::getCategoryForStack).thenComparing(ItemStack::getDescriptionId));

		for (int i = 0; i < items.size(); i++) {
			inventory.add(new MenuStockItem(this, items.get(i), (int) invPosX, (int) invPosY + (i * 14), (int)(width * 0.3255F), true));
		}
		
		inventory.forEach(this::addWidget);

		addRenderableWidget(prev = Button.builder(Component.translatable("<--"), (e) -> {
			action("prev");
		}).bounds((int) buttonPosX + 10, (int)(height * 0.1F), 30, 20).build());

		addRenderableWidget(next = Button.builder(Component.translatable("-->"), (e) -> {
			action("next");
		}).bounds((int) buttonPosX + 10 + 76, (int)(height * 0.1F), 30, 20).build());
		
		prev.visible = false;
		next.visible = false;
		addRenderableWidget(deposit = new MenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Synthesis_Materials_Deposit), ButtonType.BUTTON, (e) -> { action("deposit"); }));
		addRenderableWidget(back = new MenuButton((int) buttonPosX, button_statsY + (1 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		addRenderableWidget(amountBox = new EditBox(minecraft.font, boxR.getX()+50, (int) (topBarHeight + middleHeight - 20), minecraft.font.width("#####"), 16, Component.translatable("test")) {
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c)) {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Integer.parseInt(text) > 64) {
						return false;
					}
				} else {
					return false;
				}
				return super.charTyped(c, i);
			}
		});
		addRenderableWidget(take = Button.builder(Component.translatable(Utils.translateToLocal(Strings.Gui_Synthesis_Materials_Take)), (e) -> {
			action("take");
		}).bounds((int) amountBox.getX() + amountBox.getWidth()+5, (int) (topBarHeight + middleHeight - 22), 80, 20).build());
		
		take.visible = false;
		updateButtons();
	}

	

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		PoseStack matrixStack = gui.pose();
		drawMenuBackground(gui, mouseX, mouseY, partialTicks);
		boxL.renderWidget(gui, mouseX, mouseY, partialTicks);
		boxR.renderWidget(gui, mouseX, mouseY, partialTicks);
		gui.setColor(1, 1, 1, 1);
		super.render(gui, mouseX, mouseY, partialTicks);

		prev.visible = page > 0;
		next.visible = page < inventory.size() / itemsPerPage;
		
		if(minecraft.player.getInventory().getFreeSlot() == -1) { //TODO somehow make this detect in singleplayer the inventory changes
			take.active = false;
			take.setMessage(Component.translatable(Strings.Gui_Shop_NoSpace));
		}
		
		matrixStack.pushPose();
		{
			matrixStack.translate(width * 0.008F + 45, (height * 0.15) - 18, 1);
			RenderSystem.setShaderColor(1, 1, 1, 1);
			gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Shop_Page)+" " + (page + 1), 0, 10, 0xFF9900);
		}
		matrixStack.popPose();
		
		for (int i = 0; i < inventory.size(); i++) {
			inventory.get(i).active = false;
		}

		for (int i = page * itemsPerPage; i < page * itemsPerPage + itemsPerPage; i++) {
			if (i < inventory.size() && i >= 0) {
				if (inventory.get(i) != null) {
					inventory.get(i).visible = true;
					inventory.get(i).setY((int) (topBarHeight) + (i % itemsPerPage) * 14 + 5); // 6 = offset
					inventory.get(i).render(gui, mouseX, mouseY, partialTicks);
					inventory.get(i).active = true;
				}
			}
		}
		
		prev.render(gui, mouseX,  mouseY,  partialTicks);
		next.render(gui, mouseX,  mouseY,  partialTicks);
		deposit.render(gui, mouseX,  mouseY,  partialTicks);
		back.render(gui, mouseX,  mouseY,  partialTicks);
	}

	@Override
	protected void renderSelectedData(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		PoseStack matrixStack = gui.pose();
		amountBox.render(gui, mouseX,  mouseY,  partialTicks);
		take.render(gui, mouseX, mouseY, partialTicks);
		
		//amountBox.setWidth(minecraft.fontRenderer.getStringWidth(amountBox.getText()));
		
		take.visible = true;

		float iconPosX = boxR.getX();
		float iconPosY = boxR.getY() + 15;

		matrixStack.pushPose();
		{
			String name = selectedItemStack.getHoverName().getString();
			matrixStack.translate(boxR.getX() + (boxR.getWidth() / 2) - minecraft.font.width(name)/2, boxR.getY()+3, 1);
			gui.drawString(minecraft.font, Utils.translateToLocal(name), 0, 0, 0xFF9900);
		}
		matrixStack.popPose();

		matrixStack.pushPose();
		{
			float size = 80;
			double offset = (boxR.getWidth()*0.1F);
			matrixStack.translate(boxR.getX() + offset/2, iconPosY, 1);
			matrixStack.translate(boxR.getWidth()*0.7F / 2,boxR.getHeight()/2 - size / 2,0);
			ClientUtils.drawItemAsIcon(selectedItemStack, matrixStack, 0, 0, (int)size);
		}
		matrixStack.popPose();

	}
	
	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void onClose() {
		if (parent.moogle != -1) {
			PacketHandler.sendToServer(new CSCloseMoogleGUI(parent.moogle));
		}
		super.onClose();
	}

}
