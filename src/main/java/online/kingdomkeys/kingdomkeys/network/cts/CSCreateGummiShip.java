package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.entity.block.GummiEditorTileEntity;
import online.kingdomkeys.kingdomkeys.menu.GummiEditorMenu;
import online.kingdomkeys.kingdomkeys.network.Packet;

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

		Container container = (Container) player.containerMenu;
		ItemStack stack = container.getItem(0);
		if (stack.isEmpty())
			return;

		BlockPos origin = ((GummiEditorMenu)container).getTileEntity().getBlockEntity()).getBlockPos();
		Level level = player.level();

		int size = 7;
		GummiShipEntity.GummiStructure struct = new GummiShipEntity.GummiStructure(size, size, size);
		struct.blocks = new BlockState[size][size][size];
		BlockPos pos;

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				for (int z = 0; z < size; z++) {
					pos = new BlockPos(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
					struct.blocks[x][y][z] = level.getBlockState(pos);
				}
			}
		}

		CompoundTag tag = struct.serializeNBT(level.registryAccess());
		stack.getOrCreateTag().put("data", tag);
		container.setItem(0, stack); // sincroniza el slot con el cliente
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
