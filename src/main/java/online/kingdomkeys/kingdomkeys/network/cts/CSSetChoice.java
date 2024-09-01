package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.event.ChoiceEvent;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record CSSetChoice(SoAState state, SoAState choice, BlockPos pedestal, boolean confirm) implements Packet {

    public static final Type<CSSetChoice> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_set_choice"));

    public static final StreamCodec<FriendlyByteBuf, CSSetChoice> STREAM_CODEC = StreamCodec.composite(
            SoAState.STREAM_CODEC,
            CSSetChoice::state,
            SoAState.STREAM_CODEC,
            CSSetChoice::choice,
            BlockPos.STREAM_CODEC,
            CSSetChoice::pedestal,
            ByteBufCodecs.BOOL,
            CSSetChoice::confirm,
            CSSetChoice::new
    );

    public CSSetChoice(SoAState state, SoAState choice, BlockPos pedestal) {
        this(state, choice, pedestal, false);
    }

    public CSSetChoice(SoAState state, boolean confirm) {
        this(state, SoAState.NONE, new BlockPos(0, 0, 0), confirm);
    }

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        PlayerData playerData = PlayerData.get(player);
        if (state == SoAState.CONFIRM) {
            //go back to choices or continue to complete state and leave dimension
            if (confirm) {
                //travel back
                playerData.setSoAState(SoAState.COMPLETE);
                ServerLevel dimension = player.level().getServer().getLevel(playerData.getReturnDimension());
                player.changeDimension(new DimensionTransition(dimension, new Vec3(playerData.getReturnLocation().x, playerData.getReturnLocation().y, playerData.getReturnLocation().z), Vec3.ZERO, player.getYRot(), player.getXRot(), pEntity -> {}));
                SoAState.applyStatsForChoices(player, playerData, false);
                NeoForge.EVENT_BUS.post(new ChoiceEvent(player, playerData.getChosen(), playerData.getSacrificed()));
            } else {
                //reset to before choice
                playerData.setChoicePedestal(new BlockPos(0, 0, 0));
                playerData.setSacrificePedestal(new BlockPos(0, 0, 0));
                playerData.setChoice(SoAState.NONE);
                playerData.setSacrifice(SoAState.NONE);
                playerData.setSoAState(SoAState.CHOICE);
            }
        } else {
            if (state == SoAState.CHOICE) {
                playerData.setChoicePedestal(pedestal);
                playerData.setChoice(choice);
                playerData.setSoAState(SoAState.SACRIFICE);
            } else if (state == SoAState.SACRIFICE) {
                playerData.setSacrificePedestal(pedestal);
                playerData.setSacrifice(choice);
                playerData.setSoAState(SoAState.CONFIRM);
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
