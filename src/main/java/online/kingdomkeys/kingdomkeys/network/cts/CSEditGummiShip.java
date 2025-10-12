package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.GummiHangarBlock;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;
import online.kingdomkeys.kingdomkeys.menu.GummiHangarMenu;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.util.Utils;

public record CSEditGummiShip(String name, int containerID) implements Packet {

	public static final Type<CSEditGummiShip> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_edit_gummi_ship"));

	public static final StreamCodec<FriendlyByteBuf, CSEditGummiShip> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8,
			CSEditGummiShip::name,
			ByteBufCodecs.INT,
			CSEditGummiShip::containerID,
			CSEditGummiShip::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		if (player.containerMenu.containerId != containerID)
			return;

		GummiHangarMenu container = (GummiHangarMenu) player.containerMenu;

		BlockPos origin = container.TE.getBlockPos();
		Level level = player.level();

		BlockState hangar = level.getBlockState(origin);
		int size = hangar.getValue(GummiHangarBlock.SIZE);
		GummiShipEntity gummi = Utils.getGummiShipInBuildPlate(level, origin, hangar.getValue(GummiHangarBlock.FACING), size);
		if(gummi != null){
			GummiStructure struct = gummi.structure;

			int max = size - 1;
			Direction facing = hangar.getValue(GummiHangarBlock.FACING);

			int offsetX = 0;
			int offsetZ = 0;
			switch (facing) {
				case NORTH -> { offsetX = -3; offsetZ = 1; }
				case SOUTH -> { offsetX = -3; offsetZ = -7; }
				case EAST  -> { offsetX = -7; offsetZ = -3; }
				case WEST  -> { offsetX = 1;  offsetZ = -3; }
			}

			Rotation rotation = switch (facing) {
				case SOUTH -> Rotation.NONE;
				case NORTH -> Rotation.CLOCKWISE_180;
				case WEST  -> Rotation.CLOCKWISE_90;
				case EAST  -> Rotation.COUNTERCLOCKWISE_90;
				default -> Rotation.NONE;
			};


			for (int x = 0; x < size; x++) {
				for (int y = 0; y < size; y++) {
					for (int z = 0; z < size; z++) {
						BlockState blockToPlace = struct.getBlocks()[x][y][z];
						if (blockToPlace == null) continue;

						int rx = x, rz = z;
						switch (facing) {
							case NORTH -> { rx = max - x; rz = max - z; }
							case SOUTH -> { rx = x; rz = z; }
							case EAST  -> { rx = z; rz = max - x; }
							case WEST  -> { rx = max - z; rz = x; }
						}

						BlockPos target = origin.offset(offsetX + rx, y, offsetZ + rz);
						BlockState rotatedState = blockToPlace.rotate(rotation);

						level.setBlockAndUpdate(target, rotatedState);
					}
				}
			}
			gummi.kill();
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
