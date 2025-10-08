package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.GummiEditorBlock;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.entity.block.GummiEditorTileEntity;
import online.kingdomkeys.kingdomkeys.item.GummiShipItem;
import online.kingdomkeys.kingdomkeys.menu.GummiEditorMenu;
import online.kingdomkeys.kingdomkeys.network.Packet;
import org.joml.Vector3f;

public record CSCreateGummiShip(int containerID) implements Packet {

	public static final Type<CSCreateGummiShip> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_create_gummi_ship"));

	public static final StreamCodec<FriendlyByteBuf, CSCreateGummiShip> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			CSCreateGummiShip::containerID,
			CSCreateGummiShip::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		if (player.containerMenu.containerId != containerID)
			return;

		GummiEditorMenu container = (GummiEditorMenu) player.containerMenu;
		ItemStack stack = container.getItems().get(0);
		if (stack.isEmpty())
			return;

		BlockPos origin = container.TE.getBlockPos();
		Level level = player.level();

		int size = 7;
		GummiShipEntity.GummiStructure struct = new GummiShipEntity.GummiStructure(size, size, size);
		BlockState hangar = level.getBlockState(origin);
		struct = copyStructureWithFacing(level, origin, hangar.getValue(GummiEditorBlock.FACING), size);

		CompoundTag tag = struct.serializeNBT(level.registryAccess());
		//stack.getOrCreateTag().put("data", tag);
		((GummiShipItem)stack.getItem()).gummiStruct.deserializeNBT(player.level().registryAccess(),tag);
		container.setItem(0,0, stack); // sincroniza el slot con el cliente
	}

	public static GummiShipEntity.GummiStructure copyStructureWithFacing(Level level, BlockPos origin, Direction facing, int size) {
		GummiShipEntity.GummiStructure struct = new GummiShipEntity.GummiStructure(size, size, size);

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
					struct.blocks[x][y][z] = level.getBlockState(target);
				}
			}
		}
		return struct;
	}



	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
