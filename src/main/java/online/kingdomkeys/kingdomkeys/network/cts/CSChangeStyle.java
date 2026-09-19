package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.integration.epicfight.EpicFightEvents;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.HandStyle;
import online.kingdomkeys.kingdomkeys.integration.epicfight.style.KKFightingStyle;
import online.kingdomkeys.kingdomkeys.integration.epicfight.style.KKStyleRegistry;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public record CSChangeStyle(ResourceLocation style) implements Packet {

    public static final Type<CSChangeStyle> TYPE = new Type<>(KingdomKeys.rl("cs_change_style"));

    public static final StreamCodec<FriendlyByteBuf, CSChangeStyle> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, CSChangeStyle::style,
            CSChangeStyle::new
    );

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        PlayerData playerData = PlayerData.get(player);

        if (playerData == null) {
            return;
        }

        KKFightingStyle chosen = KKStyleRegistry.get(style);
        if (chosen == null || !chosen.isUnlocked(playerData)) {
            return;
        }

        if (chosen.getHand() == HandStyle.DUAL) {
            playerData.setDualStyle(style);
        } else {
            playerData.setSingleStyle(style);
        }

        PacketHandler.syncToAllAround(player, playerData);

        if (KingdomKeys.efmLoaded) {
            EpicFightEvents.refreshLivingMotions(player);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
