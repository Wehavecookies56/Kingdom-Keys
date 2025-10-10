package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.GummiEditorBlock;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;
import online.kingdomkeys.kingdomkeys.menu.GummiEditorMenu;
import online.kingdomkeys.kingdomkeys.network.Packet;

import java.util.List;

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

		GummiEditorMenu container = (GummiEditorMenu) player.containerMenu;

		BlockPos origin = container.TE.getBlockPos();
		Level level = player.level();

		int size = 7;
		BlockState hangar = level.getBlockState(origin);
		GummiShipEntity gummi = getGummiShipInBuildPlate(level, origin, hangar.getValue(GummiEditorBlock.FACING), size);
		if(gummi != null){
			System.out.println(gummi.structure);
			GummiStructure struct = gummi.structure;

			int max = size - 1;

			int offsetX = 0;
			int offsetZ = 0;
			Direction facing = hangar.getValue(GummiEditorBlock.FACING);
			switch (facing) {
				case NORTH -> { offsetX = -3; offsetZ = 1; }
				case SOUTH -> { offsetX = -3; offsetZ = -7; }
				case EAST  -> { offsetX = -7; offsetZ = -3; }
				case WEST  -> { offsetX = 1;  offsetZ = -3; }
			}

			for (int x = 0; x < size; x++) {
				for (int y = 0; y < size; y++) {
					for (int z = 0; z < size; z++) {
						int rx = x;
						int rz = z;

						switch (facing) {
							case NORTH -> { rx = x; rz = z; }
							case SOUTH -> { rx = max - x; rz = max - z; }
							case EAST  -> { rx = z; rz = max - x; }
							case WEST  -> { rx = max - z; rz = x; }
						}

						BlockPos target = origin.offset(offsetX + rx, y, offsetZ + rz);
						BlockState blockToPlace = struct.getBlocks()[x][y][z];
						if(blockToPlace != null) {
							level.setBlockAndUpdate(target, struct.getBlocks()[x][y][z]);
						}
					}
				}
			}
			gummi.kill();
		}
	}

	public GummiShipEntity getGummiShipInBuildPlate(Level level, BlockPos origin, Direction facing, int size) {
		int offsetX = 0;
		int offsetZ = 0;

		switch (facing) {
			case NORTH -> { offsetX = -3; offsetZ = 1; }
			case SOUTH -> { offsetX = -3; offsetZ = -7; }
			case EAST  -> { offsetX = -7; offsetZ = -3; }
			case WEST  -> { offsetX = 1;  offsetZ = -3; }
		}

		AABB box = new AABB(origin.getX()+offsetX, origin.getY(), origin.getZ()+offsetZ, origin.getX()+offsetX+size, origin.getY() + size, origin.getZ()+offsetZ+size);
		List<GummiShipEntity> entities = level.getEntitiesOfClass(GummiShipEntity.class, box);
		System.out.println(entities);

		//Only return if one single ship is detected
		if(entities.size() == 1){
			return entities.getFirst();
		}

		//None or more than 1 ship detected
		return null;
	}

	public static void removeBlocks(Level level, BlockPos origin, Direction facing, int size) {
		int max = size - 1;

		int offsetX = 0;
		int offsetZ = 0;

		switch (facing) {
			case NORTH -> { offsetX = -3; offsetZ = 1; }
			case SOUTH -> { offsetX = -3; offsetZ = -7; }
			case EAST  -> { offsetX = -7; offsetZ = -3; }
			case WEST  -> { offsetX = 1;  offsetZ = -3; }
		}

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				for (int z = 0; z < size; z++) {
					int rx = x;
					int rz = z;

					switch (facing) {
						case NORTH -> { rx = x; rz = z; }
						case SOUTH -> { rx = max - x; rz = max - z; }
						case EAST  -> { rx = z; rz = max - x; }
						case WEST  -> { rx = max - z; rz = x; }
					}

					BlockPos target = origin.offset(offsetX + rx, y, offsetZ + rz);
					if (level.getBlockState(target).getBlock() != Blocks.AIR) {
						level.setBlockAndUpdate(target, Blocks.AIR.defaultBlockState());
					}
				}
			}
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
