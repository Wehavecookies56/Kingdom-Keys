package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.block.ApprenticeClothStationTileEntity;
import online.kingdomkeys.kingdomkeys.item.UnionApprenticeArmorItem;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record CSApprenticeClothDesign(BlockPos pos, int design) implements Packet {
	public static final Type<CSApprenticeClothDesign> TYPE = new Type<>(KingdomKeys.rl("cs_apprentice_cloth_design"));

	public static final StreamCodec<FriendlyByteBuf, CSApprenticeClothDesign> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, CSApprenticeClothDesign::pos,
			ByteBufCodecs.VAR_INT, CSApprenticeClothDesign::design,
			CSApprenticeClothDesign::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		Level level = player.level();
		if (!(level.getBlockEntity(pos) instanceof ApprenticeClothStationTileEntity te)) {
			return;
		}

		if (player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > 64.0) {
			return;
		}
		te.setSelectedDesign(UnionApprenticeArmorItem.clampDesign(design));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
