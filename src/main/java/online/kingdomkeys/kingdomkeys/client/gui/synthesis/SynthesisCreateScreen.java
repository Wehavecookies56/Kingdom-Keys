package online.kingdomkeys.kingdomkeys.client.gui.synthesis;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategoryRegistry;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterBar;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterable;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuStockItem;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.Recipe;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class SynthesisCreateScreen extends MenuFilterable {
		
   // MenuFilterBar filterBar;
    MenuScrollBar scrollBar;
    MenuBox boxL, boxR;
    int itemsX = 100, itemsY = 100, itemWidth = 140, itemHeight = 10;

    public SynthesisCreateScreen() {
        super("Synthesis", new Color(0,255,0));
        drawSeparately = true;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        drawMenuBackground(mouseX, mouseY, partialTicks);
    	boxL.draw();
        boxR.draw();
    	super.render(mouseX, mouseY, partialTicks);
    }
    
    @Override
	protected void renderSelectedData() {
		float tooltipPosX = width * 0.3333F;
        float tooltipPosY = height * 0.8F;

        float iconPosX = boxR.x;
        float iconPosY = boxR.y + 15;
        float iconWidth = width * 0.1F;
        float iconHeight = height * 0.1F;
        
		Minecraft mc = Minecraft.getInstance();
        RenderHelper.disableStandardItemLighting();
        RenderSystem.pushMatrix();
        {
        	RenderSystem.translated(iconPosX, iconPosY, 1);
			RenderSystem.scaled(5, 5, 5);
			itemRenderer.renderItemIntoGUI(selected, 0, 0);
        }
        RenderSystem.popMatrix();
        
        if(selected.getItem() != null && selected.getItem() instanceof KeybladeItem) {
        	KeybladeItem kb = (KeybladeItem) selected.getItem();
	        mc.fontRenderer.drawSplitString(kb.getDescription(), (int) tooltipPosX + 5, (int) tooltipPosY+5, (int) (width * 0.6F), 0xFFFFFF);
        }
	}

    @Override
    public void init() {
        float boxPosX = (float) width * 0.1437F;
        float topBarHeight = (float) height * 0.17F;
        float boxWidth = (float) width * 0.35F;
        float middleHeight = (float) height * 0.6F;
        boxL = new MenuBox((int) boxPosX, (int) topBarHeight, (int) boxWidth, (int) middleHeight, new Color(4, 4, 68));
        boxR = new MenuBox((int) boxPosX + (int)boxWidth, (int) topBarHeight, (int) boxWidth, (int) middleHeight, new Color(4, 4, 68));
        float filterPosX = width * 0.3525F;
        float filterPosY = height * 0.023F;
        filterBar = new MenuFilterBar((int) filterPosX, (int) filterPosY, this);
        filterBar.init();
        initItems();

        //addButton(scrollBar = new MenuScrollBar());
        super.init();
    }

    @Override
    public void initItems() {
        PlayerEntity player = Minecraft.getInstance().player;
        float invPosX = (float) width * 0.1494F;
        float invPosY = (float) height * 0.1851F;
        inventory.clear();
        children.clear();
    	buttons.clear();

        filterBar.buttons.forEach(this::addButton);

        
        List<ItemStack> items = new ArrayList<>();
        IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
        for (int i = 0; i < playerData.getKnownRecipeList().size(); i++) {
        	ResourceLocation itemName = playerData.getKnownRecipeList().get(i);
        	Recipe recipe = RecipeRegistry.getInstance().getValue(itemName);
        	ItemStack stack = new ItemStack(recipe.getResult());
        	
        	if(recipe.getResult() instanceof KeychainItem)
        		stack = new ItemStack(((KeychainItem)recipe.getResult()).getKeyblade());
        	
        	if (filterItem(stack)) {
                items.add(stack);
            }
        }
        items.sort(Comparator.comparing(Utils::getCategoryForStack).thenComparing(stack -> stack.getDisplayName().getUnformattedComponentText()));
        for (int i = 0; i < items.size(); i++) {
        	//Left col
            inventory.add(new MenuStockItem(this,items.get(i), (int) invPosX, (int) invPosY + (i * 14),false));
        }
        inventory.forEach(this::addButton);
    }
	
	
	
	
	/*MenuButton keyblades, items, back;
	
		
	public SynthesisCreateScreen() {
		super(Strings.Gui_Synthesis_Synthesise, new Color(0,255,0));
		drawPlayerInfo = true;
	}

	protected void action(String string) {
		switch(string) {
		case "keyblades":
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			minecraft.displayGuiScreen(new GuiMenu_Synthesis_Synthesise_Keyblades());
			break;
		case "items":
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			minecraft.displayGuiScreen(new GuiMenu_Synthesis_Synthesise_Items());
			break;
		case "back":
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			minecraft.displayGuiScreen(new SynthesisScreen());
			break;
		}
		
		updateButtons();
	}

	private void updateButtons() {

	}

	@Override
	public void init() {
		//TODO request packet to sync other players data
		super.width = width;
		super.height = height;
		super.init();
		this.buttons.clear();
				
		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		addButton(keyblades = new MenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Synthesis_Synthesise_Keyblades), ButtonType.BUTTON, (e) -> { action("keyblades"); }));
		addButton(items = new MenuButton((int) buttonPosX, button_statsY + (1 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Synthesis_Synthesise_Items), ButtonType.BUTTON, (e) -> { action("items"); }));
		addButton(back = new MenuButton((int) buttonPosX, button_statsY + (2 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		
		updateButtons();
	}
	 */
	
}
