package online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment;

import java.awt.Color;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuEquipmentButton;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.MenuItemsScreen;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class MenuEquipmentScreen extends MenuBackground {

    MenuBox listBox, detailsBox;
    Button back;

    public MenuEquipmentScreen() {
        super(Strings.Gui_Menu_Items_Equipment, new Color(0,0,255));
        drawSeparately = true;
//        minecraft = Minecraft.getInstance();
    }

    int scrollOffset = 0;

    @Override
    public void init() {
        super.init();
        buttonWidth = ((float)width * 0.07F);
        float listBoxX = width * 0.16F;
        float boxY = height * 0.174F;
        float listBoxWidth = width * 0.452F;
        float boxHeight = height * 0.5972F;
        float detailsWidth = width * 0.2588F;
        float detailsX = listBoxX + listBoxWidth;
        listBox = new MenuBox((int) listBoxX, (int) boxY, (int) listBoxWidth, (int) boxHeight, new Color(76, 76, 76));
        detailsBox = new MenuBox((int) detailsX, (int) boxY, (int) detailsWidth, (int) boxHeight, new Color(76, 76, 76));

        float itemsX = width * 0.31F;
        float itemsY = height * 0.1907F;

        addButton(back = new MenuButton((int)buttonPosX, buttonPosY, (int)buttonWidth, new TranslationTextComponent(Strings.Gui_Menu_Back).getFormattedText(), MenuButton.ButtonType.BUTTON, b -> minecraft.displayGuiScreen(new MenuItemsScreen())));

        IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
        Map<ResourceLocation, ItemStack> keychains = playerData.getEquippedKeychains();


        int itemHeight = 14;

        AtomicInteger offset = new AtomicInteger();

        if (keychains.get(DriveForm.NONE) != null) {
            MenuEquipmentButton firstslot = new MenuEquipmentButton(keychains.get(DriveForm.NONE), (int) itemsX, (int) itemsY + offset.get() + itemHeight * offset.getAndIncrement() - scrollOffset, 0x3C0002, new MenuEquipmentSelectorScreen(DriveForm.NONE, new Color(112, 31, 35), 0x3C0000), ItemCategory.TOOL, this, Strings.Gui_Menu_Items_Equipment_Weapon, 0xFE8185);
            addButton(firstslot);
        }

        Comparator<Map.Entry<ResourceLocation, ItemStack>> sortByFormOrder = Comparator.comparingInt(f -> ModDriveForms.registry.getValue(f.getKey()).getOrder());

        keychains.entrySet().stream().sorted(sortByFormOrder).forEachOrdered((entry) -> {
            ResourceLocation form = entry.getKey();
            ItemStack keychain = entry.getValue();
            if (!form.equals(DriveForm.NONE) && ModDriveForms.registry.getValue(form).hasKeychain()) {
                addButton(new MenuEquipmentButton(keychain, (int) itemsX, (int) itemsY + offset.get() + itemHeight * offset.getAndIncrement() - scrollOffset, 0x003231, new MenuEquipmentSelectorScreen(form, new Color(10, 22, 22), 0x032F3C), ItemCategory.TOOL, this, ModDriveForms.registry.getValue(form).getTranslationKey(), 0x069293));
            }
        });

        //TODO the other slots, items, accesories, etc.
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        drawMenuBackground(mouseX, mouseY, partialTicks);
        listBox.draw();
        detailsBox.draw();
        super.render(mouseX, mouseY, partialTicks);
        /*
        RenderSystem.pushMatrix();
        {
            RenderHelper.enableStandardItemLighting();

            driveKeychains.forEach(button -> button.render(mouseX, mouseY, partialTicks));
            //TODO the rest
        }
        RenderSystem.popMatrix();
        */
    }
}
