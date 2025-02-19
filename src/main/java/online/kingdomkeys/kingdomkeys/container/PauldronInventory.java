package online.kingdomkeys.kingdomkeys.container;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import online.kingdomkeys.kingdomkeys.item.SynthesisItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class PauldronInventory implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    private final IItemHandler inv = new ItemStackHandler(4) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return !stack.isEmpty() && stack.getItem() instanceof ArmorItem;
        }
    };

    private final LazyOptional<IItemHandler> instance = LazyOptional.of(() -> inv);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, instance);
    }

    @Override
    public CompoundTag serializeNBT() {
        return ((ItemStackHandler)inv).serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ((ItemStackHandler)inv).deserializeNBT(nbt);
    }
}
