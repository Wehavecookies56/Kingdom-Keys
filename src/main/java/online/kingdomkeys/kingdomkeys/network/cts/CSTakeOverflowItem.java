package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowWarning;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

public record CSTakeOverflowItem() implements Packet {

    public static final Type<CSTakeOverflowItem> TYPE = new Type<>(KingdomKeys.rl("cs_take_overflow_item"));

    public static final StreamCodec<FriendlyByteBuf, CSTakeOverflowItem> STREAM_CODEC = StreamCodec.of((buffer, value) -> {}, buffer -> new CSTakeOverflowItem());

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        PlayerData playerData = PlayerData.get(player);

        if (playerData == null || playerData.checkNextOverflow() == null) {
            return;
        }

        if (player.getInventory().getFreeSlot() < 0) {
            Component warning = Component.translatable(Strings.WarningStockFull);
            player.displayClientMessage(warning, true);
            SCShowWarning.send(player, warning);
            return;
        }

        ItemStack stack = playerData.takeNextOverflow();
        if (!player.getInventory().add(stack)) {
            // If for some reason it reaches this part (shouldn't since client blocks it)
            player.drop(stack, false);
        }

        if (player instanceof ServerPlayer serverPlayer) {
            PacketHandler.sendTo(new SCSyncPlayerData(player), serverPlayer);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
