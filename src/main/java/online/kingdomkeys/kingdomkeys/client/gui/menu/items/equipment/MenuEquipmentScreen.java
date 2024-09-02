package online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuEquipmentButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.MenuItemsScreen;
import online.kingdomkeys.kingdomkeys.client.gui.organization.WeaponTreeSelectionScreen;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MenuEquipmentScreen extends MenuBackground {

    MenuBox listBox, detailsBox;
    Button back, showKeybladesButton;
    MenuScrollBar scrollBar;

    public MenuEquipmentScreen() {
        super(Strings.Gui_Menu_Items_Equipment, new Color(0,0,255));
        drawSeparately = true;
    }
    
    public MenuEquipmentScreen(boolean showingKeyblades) {
        super(Strings.Gui_Menu_Items_Equipment, new Color(0,0,255));
        drawSeparately = true;
        this.showingKeyblades = showingKeyblades;
    }

    boolean showingKeyblades = false;
    @Override
    public void init() {
        super.init();
        renderables.clear();
        children().clear();

        buttonWidth = ((float)width * 0.07F);
        float listBoxX = width * 0.16F;
        float boxY = height * 0.174F;
        float topBarHeight = height * 0.17F;
        float listBoxWidth = width * 0.452F;
        float boxHeight = height * 0.5972F;
        float detailsWidth = width * 0.2588F;
        float detailsX = listBoxX + listBoxWidth;
        listBox = new MenuBox((int) listBoxX, (int) boxY, (int) listBoxWidth, (int) boxHeight, new Color(76, 76, 76));
        detailsBox = new MenuBox((int) detailsX, (int) boxY, (int) detailsWidth, (int) boxHeight, new Color(76, 76, 76));
        
        int itemHeight = 14;
        
        float itemsX = width * 0.31F;
        float itemsY = height * 0.1907F;
        buttonPosY = (int) (topBarHeight+5);
        buttonPosX = 15.4F;
        
        PlayerData playerData = PlayerData.get(minecraft.player);

        addRenderableWidget(back = new MenuButton((int)buttonPosX, playerData.getAlignment() == OrgMember.NONE ? buttonPosY : buttonPosY+20, (int)buttonWidth, Component.translatable(Strings.Gui_Menu_Back).getString(), MenuButton.ButtonType.BUTTON, b -> minecraft.setScreen(new MenuItemsScreen())));

        Map<ResourceLocation, ItemStack> keychains = playerData.getEquippedKeychains();
        List<String> shotlocks = Utils.getSortedShotlocks(playerData.getShotlockList());
        Map<Integer, ItemStack> items = playerData.getEquippedItems();
        Map<Integer, ItemStack> accessories = playerData.getEquippedAccessories();
        Map<Integer, ItemStack> kbArmor = playerData.getEquippedKBArmors();
        Map<Integer, ItemStack> armor = playerData.getEquippedArmors();

        AtomicInteger offset = new AtomicInteger();
        AtomicInteger hidden = new AtomicInteger(0);

        int lastButtonY = 0;
        
        if (playerData.getAlignment() != Utils.OrgMember.NONE) { //ORG
            MenuEquipmentButton orgWeaponSlot = new MenuEquipmentButton(playerData.getEquippedWeapon(), (int) itemsX, (int) itemsY + offset.get() + itemHeight * offset.getAndIncrement(), 0x555555, new WeaponTreeSelectionScreen(playerData.getAlignment()), ItemCategory.TOOL, this, Strings.Gui_Menu_Items_Equipment_Weapon, 0xAAAAAA);
            addRenderableWidget(orgWeaponSlot);

            addRenderableWidget(showKeybladesButton = new MenuButton((int)buttonPosX, buttonPosY, 45, Component.translatable(Strings.Gui_Menu_Items_Equipment_Weapon_Keyblades).getString(), MenuButton.ButtonType.BUTTON, b -> {showingKeyblades = !showingKeyblades; init();}));
            
            if(keychains.get(DriveForm.SYNCH_BLADE) != null && playerData.isAbilityEquipped(Strings.synchBlade) && (playerData.getEquippedWeapon() != null)) {
            	if(playerData.getEquippedWeapon().getItem() instanceof KeybladeItem) { // Synch blade button when org member (should only appear when using Roxas weapon)
            		MenuEquipmentButton sbSlot = new MenuEquipmentButton(keychains.get(DriveForm.SYNCH_BLADE), (int) itemsX, (int) itemsY +  (offset.get() - hidden.get()) + itemHeight * (offset.getAndIncrement() - hidden.get()), 0x880000, new MenuEquipmentSelectorScreen(DriveForm.SYNCH_BLADE, new Color(112, 31, 35), 0x880000), ItemCategory.TOOL, this, "ability.ability_synch_blade.name", 0xFE8185);
                    addRenderableWidget(sbSlot);
            	} else { //Synch blade button when org member (should only appear when not using Roxas weapon, inside the keyblades button)
            		MenuEquipmentButton sbSlot = new MenuEquipmentButton(keychains.get(DriveForm.SYNCH_BLADE), (int) itemsX, (int) itemsY +  (offset.get()) + itemHeight * (offset.getAndIncrement() ), 0x880000, new MenuEquipmentSelectorScreen(DriveForm.SYNCH_BLADE, new Color(112, 31, 35), 0x880000), ItemCategory.TOOL, this, "ability.ability_synch_blade.name", 0xFE8185);
                    addRenderableWidget(sbSlot);
                    
                    sbSlot.active = showingKeyblades;
                    sbSlot.visible = showingKeyblades;

                    hidden.getAndIncrement();            		
            	}
            }
           
        } else {
        	showingKeyblades = true;
        }
        
        //Slot main keyblade
        if (keychains.get(DriveForm.NONE) != null) {
            MenuEquipmentButton firstSlot = new MenuEquipmentButton(keychains.get(DriveForm.NONE), (int) itemsX, (int) itemsY + offset.get() + itemHeight * offset.getAndIncrement(), 0x880000, new MenuEquipmentSelectorScreen(DriveForm.NONE, new Color(112, 31, 35), 0x880000), ItemCategory.TOOL, this, Strings.Gui_Menu_Items_Equipment_Weapon, 0xFE8185);
            addRenderableWidget(firstSlot);

            firstSlot.active = showingKeyblades;
            firstSlot.visible = showingKeyblades;
            hidden.getAndIncrement();
            
            //Synch blade
            if (playerData.getAlignment() == Utils.OrgMember.NONE && playerData.getEquippedAbilityLevel(Strings.synchBlade)[1] > 0) {
            	MenuEquipmentButton sbSlot = new MenuEquipmentButton(keychains.get(DriveForm.SYNCH_BLADE), (int) itemsX, (int) itemsY +  (offset.get()) + itemHeight * (offset.getAndIncrement() ), 0x880000, new MenuEquipmentSelectorScreen(DriveForm.SYNCH_BLADE, new Color(112, 31, 35), 0x880000), ItemCategory.TOOL, this, "ability.ability_synch_blade.name", 0xFE8185);
                addRenderableWidget(sbSlot);
                
                sbSlot.active = showingKeyblades;
                sbSlot.visible = showingKeyblades;

                hidden.getAndIncrement();
            }
        }
        
        //Form keyblades
        Comparator<Map.Entry<ResourceLocation, ItemStack>> sortByFormOrder = Comparator.comparingInt(f -> ModDriveForms.registry.get(f.getKey()).getOrder());
        keychains.entrySet().stream().sorted(sortByFormOrder).forEachOrdered((entry) -> {
            ResourceLocation form = entry.getKey();
            ItemStack keychain = entry.getValue();
            if (!form.equals(DriveForm.NONE) && !form.equals(DriveForm.SYNCH_BLADE) && ModDriveForms.registry.get(form).isSlotVisible(minecraft.player)) {
            	MenuEquipmentButton button = new MenuEquipmentButton(keychain, (int) itemsX, (int) itemsY + offset.get() + itemHeight * offset.getAndIncrement(), 0x006666, new MenuEquipmentSelectorScreen(form, new Color(10, 22, 22), 0x006666), ItemCategory.TOOL, this, ModDriveForms.registry.get(form).getTranslationKey(), 0x00BBBB);
                addRenderableWidget(button);

                hidden.getAndIncrement();

                button.active = showingKeyblades;
                button.visible = showingKeyblades;
            }
        });
        
        if(!showingKeyblades)
        	offset.set(offset.get() - hidden.get());
                
        if (shotlocks != null) {
            MenuEquipmentButton shotlockSlot = new MenuEquipmentButton(playerData.getEquippedShotlock(), (int) itemsX, (int) itemsY + offset.get() + itemHeight * offset.getAndIncrement(), 0x11FF44, new MenuShotlockSelectorScreen(new Color(17, 255, 100), 0x44FF99), ItemCategory.SHOTLOCK, this, Strings.Gui_Menu_Items_Equipment_Shotlock, 0x81FEAA);
            addRenderableWidget(shotlockSlot);
        }
        
        if(kbArmor != null) {
        	kbArmor.entrySet().stream().forEachOrdered((entry) -> {
	           // int slot = entry.getKey();
	            ItemStack item = entry.getValue();
	            MenuEquipmentButton kbArmorSlot = new MenuEquipmentButton(item, (int) itemsX, (int) itemsY + offset.get() + itemHeight * offset.getAndIncrement(), 0xFF7200, new MenuKeybladeArmorSelectorScreen(0, new Color(255, 127, 0), 0xFF7200), ItemCategory.KBARMOR, this, Utils.translateToLocal(Strings.Gui_Menu_Items_Equipment_Pauldron), 0xFF9A3D);
	            addRenderableWidget(kbArmorSlot);
        	});
         }        
        
		if (accessories != null) {
			int c = 1;
			for (Map.Entry<Integer, ItemStack> entry : accessories.entrySet()) {
				if (c > playerData.getMaxAccessories())
					break;
				int slot = entry.getKey();
				ItemStack item = entry.getValue();
				MenuEquipmentButton accessorySlot;
				if (slot == 0) {
					accessorySlot = new MenuEquipmentButton(item, (int) itemsX, (int) itemsY + offset.get() + itemHeight * offset.getAndIncrement(), 0x0055AA, new MenuAccessorySelectorScreen(slot, new Color(31, 35, 112), 0x44AAFF), ItemCategory.ACCESSORIES, this, Utils.translateToLocal(Strings.Gui_Menu_Items_Equipment_Accessories), 0x42ceff);
				} else {
					accessorySlot = new MenuEquipmentButton(item, (int) itemsX, (int) itemsY + offset.get() + itemHeight * offset.getAndIncrement(), 0x0055AA, new MenuAccessorySelectorScreen(slot, new Color(31, 35, 112), 0x44AAFF), ItemCategory.ACCESSORIES, this);
				}
				addRenderableWidget(accessorySlot);
				c++;
			}
			

		}
            
		if (armor != null) {
			int c = 1;
			for (Map.Entry<Integer, ItemStack> entry : armor.entrySet()) {
				if (c > playerData.getMaxArmors())
					break;
				int slot = entry.getKey();
				ItemStack item = entry.getValue();
				MenuEquipmentButton armorSlot;
				if (slot == 0) {
					armorSlot = new MenuEquipmentButton(item, (int) itemsX, (int) itemsY + offset.get() + itemHeight * offset.getAndIncrement(), 0xAAAA00, new MenuArmorSelectorScreen(slot, new Color(255, 247, 0), 0x444400), ItemCategory.EQUIPMENT, this, Utils.translateToLocal(Strings.Gui_Menu_Items_Equipment_Armor), 0xFFFF00);
				} else {
					armorSlot = new MenuEquipmentButton(item, (int) itemsX, (int) itemsY + offset.get() + itemHeight * offset.getAndIncrement(), 0xAAAA00, new MenuArmorSelectorScreen(slot, new Color(255, 247, 0), 0x444400), ItemCategory.EQUIPMENT, this);
				}
				addRenderableWidget(armorSlot);
				c++;
			}
		}
		
        if(items != null) {
        	 items.entrySet().stream().forEachOrdered((entry) -> {
                int slot = entry.getKey();
                ItemStack item = entry.getValue();
                MenuEquipmentButton potionSlot;
                if(slot == 0) {
                	potionSlot = new MenuEquipmentButton(item, (int) itemsX, (int) itemsY + offset.get() + itemHeight * offset.getAndIncrement(), 0x007700, new MenuPotionSelectorScreen(slot, new Color(31, 112, 35), 0x22FF22), ItemCategory.CONSUMABLE, this, Utils.translateToLocal(Strings.Gui_Menu_Items_Equipment_Items)+" ["+items.size()+"]", 0x81FE85);
                } else {
                	potionSlot = new MenuEquipmentButton(item, (int) itemsX, (int) itemsY + offset.get() + itemHeight * offset.getAndIncrement(), 0x007700, new MenuPotionSelectorScreen(slot, new Color(31, 112, 35), 0x22FF22), ItemCategory.CONSUMABLE, this);
                }
                addRenderableWidget(potionSlot);
             });
        }

        for (Renderable renderable : renderables) {
            if (renderable instanceof MenuEquipmentButton button) {
                lastButtonY = Math.max(button.getY(), lastButtonY);
            }
        }

        addRenderableWidget(scrollBar = new MenuScrollBar((int) (listBoxX+listBoxWidth-MenuScrollBar.WIDTH-2), (int) topBarHeight + 4, (int) (middleHeight + topBarHeight)-4, (int) middleHeight, (int) ((lastButtonY+28) - itemsY)));
        
    }

    public void updateScroll() {
        renderables.forEach(renderable -> {
            if (renderable instanceof MenuEquipmentButton button) {
                button.offsetY = (int) scrollBar.scrollOffset;
            }
        });
    }

    @Override
    public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        drawMenuBackground(gui, mouseX, mouseY, partialTicks);
		listBox.renderWidget(gui, mouseX, mouseY, partialTicks);
		detailsBox.renderWidget(gui, mouseX, mouseY, partialTicks);
        for(Renderable renderable : this.renderables) {
            if (renderable instanceof MenuEquipmentButton) {
                gui.enableScissor(listBox.getX() + 2, scrollBar.getY() + 2, listBox.getX() + listBox.getWidth() + detailsBox.getWidth(), scrollBar.getHeight());
                renderable.render(gui, mouseX, mouseY, partialTicks);
                gui.disableScissor();
            } else {
                renderable.render(gui, mouseX, mouseY, partialTicks);
            }
        }
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double deltaX, double deltaY) {
        scrollBar.mouseScrolled(pMouseX, pMouseY, deltaX, deltaY);
        updateScroll();
        return super.mouseScrolled(pMouseX, pMouseY, deltaX, deltaY);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        scrollBar.mouseClicked(pMouseX, pMouseY, pButton);
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        scrollBar.mouseReleased(pMouseX, pMouseY, pButton);
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        scrollBar.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        updateScroll();
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }
}
