package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record CSConsumeCard(BlockPos doorTE, ItemStack stack) implements Packet {

    public static final Type<CSConsumeCard> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_consume_card"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CSConsumeCard> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            CSConsumeCard::doorTE,
            ItemStack.STREAM_CODEC,
            CSConsumeCard::stack,
            CSConsumeCard::new
    );

    @Override
    public void handle(IPayloadContext context) {
        BlockEntity blockEntity = context.player().level().getBlockEntity(doorTE);
        if (blockEntity instanceof CardDoorTileEntity cardDoorTileEntity) {
            if (cardDoorTileEntity.consumeCard(stack.copy())) {
                context.player().getInventory().getItem(context.player().getInventory().findSlotMatchingItem(stack)).shrink(1);
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
