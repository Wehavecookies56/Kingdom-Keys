package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.menu.BagInventory;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.util.Utils;

public record CSConsumeCard(BlockPos doorTE, int slot) implements Packet {

    public static final Type<CSConsumeCard> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_consume_card"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CSConsumeCard> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            CSConsumeCard::doorTE,
            ByteBufCodecs.INT,
            CSConsumeCard::slot,
            CSConsumeCard::new
    );

    @Override
    public void handle(IPayloadContext context) {
        BlockEntity blockEntity = context.player().level().getBlockEntity(doorTE);

        if (!(blockEntity instanceof CardDoorTileEntity cardDoorTileEntity))
            return;

        if (isBagSlot(slot)) {
            ItemStack cardsBag = Utils.getItemInInventory(context.player(), ModItems.cardsBag.get());

            if (cardsBag.isEmpty())
                return;

            if (cardsBag.getCapability(Capabilities.ItemHandler.ITEM) instanceof BagInventory bagInv) {
                ItemStack stack = bagInv.getStackInSlot(getBagIndex(slot));

                if (stack.isEmpty())
                    return;

                if (cardDoorTileEntity.consumeCard(stack.copy())) {
                    bagInv.setStackInSlot(getBagIndex(slot), ItemStack.EMPTY);
                }
            }

        } else {
            ItemStack stack = context.player().getInventory().getItem(slot);

            if (stack.isEmpty())
                return;

            if (cardDoorTileEntity.consumeCard(stack.copy())) {
                context.player().getInventory().removeItem(slot, 1);
            }
        }
    }

    private static boolean isBagSlot(int slot) {
        return slot <= -1000;
    }

    private static int getBagIndex(int slot) {
        return -1000 - slot;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
