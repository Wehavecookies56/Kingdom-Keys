package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiHangarBlock;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;
import online.kingdomkeys.kingdomkeys.menu.GummiHangarMenu;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.util.Utils;

public record CSEditGummiShip(String name, int containerID) implements Packet {

	public static final Type<CSEditGummiShip> TYPE = new Type<>(KingdomKeys.rl("cs_edit_gummi_ship"));

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
		container.TE.setBuilding(false);
		BlockPos origin = container.TE.getBlockPos();
		Level level = player.level();

		BlockState hangar = level.getBlockState(origin);
		int size = GummiHangarBlock.getSize(hangar.getValue(GummiHangarBlock.LEVEL));
		GummiShipEntity gummi = Utils.getGummiShipInBuildPlate(level, origin, hangar.getValue(GummiHangarBlock.FACING), size);
		if(gummi != null){
			GummiStructure struct = gummi.structure;

            // When we change from entity to blocks we want to set the textbox name with the struc name
            container.TE.setLastShipName(struct.getName());

			GummiStructure fitted = Utils.resizeStructure(struct, size);

			if (fitted == null) {
				KingdomKeys.LOGGER.debug("Can't resize a ship from "+gummi.structure.getWidth()+" to "+size);
				player.sendSystemMessage(Component.translatable("container.gummi_hangar.shiptoobig"));
				return;
			}

			gummi.setFuel(Math.min(gummi.getFuel(), GummiShipEntity.getMaxFuelForSize(size)));
			Utils.placeGummiStructure(level, origin, hangar.getValue(GummiHangarBlock.FACING), size, fitted, gummi);

            gummi.kill();
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
