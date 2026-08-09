package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiCoreBlock;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiHangarBlock;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.entity.block.GummiCoreTileEntity;
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

		BlockPos origin = container.TE.getBlockPos();
		Level level = player.level();

		BlockState hangar = level.getBlockState(origin);
		int size = GummiHangarBlock.getSize(hangar.getValue(GummiHangarBlock.LEVEL));
		GummiShipEntity gummi = Utils.getGummiShipInBuildPlate(level, origin, hangar.getValue(GummiHangarBlock.FACING), size);
		if(gummi != null){
			GummiStructure struct = gummi.structure;

            // When we change from entity to blocks we want to set the textbox name with the struc name
            container.TE.setLastShipName(struct.getName());

            //Vec3i shipSize = Utils.getRealGummiStructureSize(struct);
			//If gummi ship trying to be turned into blocks is bigger than the build plate complain about it
			//if(shipSize.getX() > size || shipSize.getY() > size || shipSize.getZ() > size){
			if(struct.getWidth() > size){
				KingdomKeys.LOGGER.debug("Can't resize a ship from "+gummi.structure.getWidth()+" to "+size);
				player.sendSystemMessage(Component.translatable("container.gummi_hangar.shiptoobig"));

			} else {
				//If ship is smaller allow it in and adapt it's array
				//KingdomKeys.LOGGER.debug("Resizing ship size from "+gummi.structure.getWidth()+" to "+size);
				struct = Utils.resizeStructure(struct,size);
			}

			Utils.placeGummiStructure(level, origin, hangar.getValue(GummiHangarBlock.FACING), size, struct, gummi);

            gummi.kill();
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
