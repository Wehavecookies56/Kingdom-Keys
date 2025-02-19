package online.kingdomkeys.kingdomkeys.container;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

public class PauldronSlot extends SlotItemHandler {

    private int index;

    public PauldronSlot(IItemHandlerModifiable inv, int index, int x, int y) {
        super((IItemHandler) inv, index, x, y);
        this.index = index;
    }

    @Override
    public boolean mayPlace (ItemStack stack) {
        if (stack.getItem() instanceof ArmorItem armorItem) {
            return switch (index) {
                case 0 -> armorItem.getType().equals(ArmorItem.Type.HELMET);
                case 1 -> armorItem.getType().equals(ArmorItem.Type.CHESTPLATE);
                case 2 -> armorItem.getType().equals(ArmorItem.Type.LEGGINGS);
                case 3 -> armorItem.getType().equals(ArmorItem.Type.BOOTS);
                default -> false;
            };
        }
        return stack.getItem() instanceof ArmorItem;
    }

    @Override
    public void setChanged() {
        container.setChanged();
    }

    static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[]{InventoryMenu.EMPTY_ARMOR_SLOT_HELMET, InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS, InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS};

    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(InventoryMenu.BLOCK_ATLAS, TEXTURE_EMPTY_SLOTS[index]);
    }

}
