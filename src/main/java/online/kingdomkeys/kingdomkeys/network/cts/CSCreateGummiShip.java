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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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
import online.kingdomkeys.kingdomkeys.util.Utils;

public record CSCreateGummiShip(String name, int containerID) implements Packet {

	public static final Type<CSCreateGummiShip> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_create_gummi_ship"));

	public static final StreamCodec<FriendlyByteBuf, CSCreateGummiShip> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8,
			CSCreateGummiShip::name,
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
		ItemStack stack = container.getItems().getFirst();

		BlockPos origin = container.TE.getBlockPos();
		Level level = player.level();

		int size = 7;
		BlockState hangar = level.getBlockState(origin);
		GummiStructure struct = Utils.getGummiStructureWithFacing(level, origin, hangar.getValue(GummiEditorBlock.FACING), size);
		GummiShipEntity shipEntity = new GummiShipEntity(level, struct);
		switch (hangar.getValue(GummiEditorBlock.FACING)) {
			default -> {
				shipEntity.setPos(new Vec3(origin.getX()+0.5F, origin.getY(), origin.getZ()+4.5F));
				shipEntity.setYRot(180);
			}
			case SOUTH -> {
				shipEntity.setPos(new Vec3(origin.getX()+0.5F, origin.getY(), origin.getZ()-3.5F));
				shipEntity.setYRot(0);
			}
			case EAST -> {
				shipEntity.setPos(new Vec3(origin.getX()-3.5F, origin.getY(), origin.getZ()+0.5F));
				shipEntity.setYRot(90);
			}
			case WEST -> {
				shipEntity.setPos(new Vec3(origin.getX()+4.5F, origin.getY(), origin.getZ() + 0.5F));
				shipEntity.setYRot(270);
			}
		}

		level.addFreshEntity(shipEntity);
		removeBlocks(level, origin, hangar.getValue(GummiEditorBlock.FACING), size);

		if (stack.is(ModItems.gummiShipBlueprint.get())) {
			stack.set(ModComponents.GUMMI_STRUCTURE, struct);
			stack.set(ModComponents.BLUEPRINT_NAME, name);
		}
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
