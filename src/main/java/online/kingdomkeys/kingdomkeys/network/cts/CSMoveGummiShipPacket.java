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
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;
import online.kingdomkeys.kingdomkeys.menu.GummiHangarMenu;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.util.Utils;

public record CSMoveGummiShipPacket(String direction, int containerID) implements Packet {

	public static final Type<CSMoveGummiShipPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_move_gummi_ship"));

	public static final StreamCodec<FriendlyByteBuf, CSMoveGummiShipPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8,
			CSMoveGummiShipPacket::direction,
			ByteBufCodecs.INT,
			CSMoveGummiShipPacket::containerID,
			CSMoveGummiShipPacket::new
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
		int size = GummiHangarBlock.getSize(hangar.getValue(GummiHangarBlock.LEVEL));
		Direction facing = hangar.getValue(GummiHangarBlock.FACING);
		GummiStructure struct = Utils.getGummiStructureWithFacing(level, origin, hangar.getValue(GummiHangarBlock.FACING), size);

		Direction moveDir = switch (direction.toUpperCase()) {
			case "FORWARD" -> switch (facing) {
				case NORTH -> Direction.NORTH;
				case SOUTH -> Direction.SOUTH;
				case EAST  -> Direction.EAST;
				case WEST  -> Direction.WEST;
				default -> null;
			};
			case "BACKWARD" -> switch (facing) {
				case NORTH -> Direction.SOUTH;
				case SOUTH -> Direction.NORTH;
				case EAST  -> Direction.WEST;
				case WEST  -> Direction.EAST;
				default -> null;
			};
			case "LEFT" -> switch (facing) {
				case NORTH -> Direction.WEST;
				case SOUTH -> Direction.EAST;
				case EAST  -> Direction.NORTH;
				case WEST  -> Direction.SOUTH;
				default -> null;
			};
			case "RIGHT" -> switch (facing) {
				case NORTH -> Direction.EAST;
				case SOUTH -> Direction.WEST;
				case EAST  -> Direction.SOUTH;
				case WEST  -> Direction.NORTH;
				default -> null;
			};
			case "UP" -> Direction.UP;
			case "DOWN" -> Direction.DOWN;
			default -> null;
		};

		System.out.println(moveDir);
		if (moveDir != null) {
			struct = Utils.shiftShip(struct, moveDir, facing);
		}

		Utils.removeBlocks(level, origin, hangar.getValue(GummiHangarBlock.FACING), size);

//Place the new struct

		int[] offsets = Utils.getShipOffset(facing,size);
		if(offsets == null)
			return;

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				for (int z = 0; z < size; z++) {
					BlockState blockToPlace = struct.getBlocks()[x][y][z];
					if (blockToPlace == null)
						continue;

					BlockPos target = origin.offset(offsets[0] + x, y, offsets[1] + z);
					level.setBlockAndUpdate(target, blockToPlace);
				}
			}
		}

	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
