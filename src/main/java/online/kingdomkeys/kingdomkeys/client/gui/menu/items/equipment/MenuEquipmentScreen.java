package online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuEquipmentButton;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;

import java.awt.Color;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MenuEquipmentScreen extends MenuBackground {

    MenuBox listBox, detailsBox;

    public MenuEquipmentScreen() {
        super("Equipment", new Color(0,0,255));
        drawSeparately = true;
    }

    int scrollOffset = 0;

    @Override
    public void init() {
        super.init();
        float listBoxX = width * 0.1463F;
        float boxY = height * 0.174F;
        float listBoxWidth = width * 0.452F;
        float boxHeight = height * 0.5972F;
        float detailsWidth = width * 0.2588F;
        float detailsX = listBoxX + listBoxWidth;
        listBox = new MenuBox((int) listBoxX, (int) boxY, (int) listBoxWidth, (int) boxHeight, new Color(76, 76, 76));
        detailsBox = new MenuBox((int) detailsX, (int) boxY, (int) detailsWidth, (int) boxHeight, new Color(76, 76, 76));

        float itemsX = width * 0.2869F;
        float itemsY = height * 0.1907F;

        Minecraft mc = Minecraft.getInstance();
        IPlayerCapabilities playerData = ModCapabilities.getPlayer(mc.player);
        Map<ResourceLocation, ItemStack> keychains = playerData.getEquippedKeychains();


        int itemHeight = 14;

        AtomicInteger offset = new AtomicInteger();

        MenuEquipmentButton firstslot = new MenuEquipmentButton(keychains.get(DriveForm.NONE), (int) itemsX, (int) itemsY + offset.get() + itemHeight * offset.getAndIncrement() - scrollOffset, 0x3C0002, new MenuEquipmentSelectorScreen(DriveForm.NONE, new Color(112, 31, 35), 0x3C0000), ItemCategory.TOOL, this, "Weapon", 0xFE8185);
        addButton(firstslot);

        Comparator<Map.Entry<ResourceLocation, ItemStack>> sortByFormOrder = Comparator.comparingInt(f -> ModDriveForms.registry.getValue(f.getKey()).getOrder());

        keychains.entrySet().stream().sorted(sortByFormOrder).forEachOrdered((entry) -> {
            ResourceLocation form = entry.getKey();
            ItemStack keychain = entry.getValue();
            if (!form.equals(DriveForm.NONE)) {
                addButton(new MenuEquipmentButton(keychain, (int) itemsX, (int) itemsY + offset.get() + itemHeight * offset.getAndIncrement() - scrollOffset, 0x003231, new MenuEquipmentSelectorScreen(form, new Color(10, 22, 22), 0x032F3C), ItemCategory.TOOL, this, ModDriveForms.registry.getValue(form).getTranslationKey(), 0x069293));
            }
        });

        //TODO the other slots, items, accesories, etc.
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        drawAll(mouseX, mouseY, partialTicks);
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
