package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiHangarBlock;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.entity.block.GummiCoreTileEntity;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.menu.GummiHangarMenu;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowWarning;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.ArrayList;

public record CSBuildGummiShip(String name, int containerID) implements Packet {

	public static final Type<CSBuildGummiShip> TYPE = new Type<>(KingdomKeys.rl("cs_create_gummi_ship"));

	public static final StreamCodec<FriendlyByteBuf, CSBuildGummiShip> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8, CSBuildGummiShip::name,
			ByteBufCodecs.INT, CSBuildGummiShip::containerID,
			CSBuildGummiShip::new
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
		ArrayList<Block> bannedBlocks = Utils.getBannedBlocks(level,origin,hangar.getValue(GummiHangarBlock.FACING), size);
		if(bannedBlocks != null && !bannedBlocks.isEmpty()) {
			MutableComponent bannedBlocksNames = Component.empty();
			for (int i = 0; i < bannedBlocks.size(); i++) {
				if (i > 0) {
					bannedBlocksNames.append(", ");
				}

				bannedBlocksNames.append(bannedBlocks.get(i).asItem().getDescription());
			}

			Component warning = Component.translatable(Strings.WarningBannedBlocks).append(bannedBlocksNames);
			player.sendSystemMessage(warning);
			SCShowWarning.send(player, warning);

			return;
		}
        if(Utils.getCorePos(level,origin,hangar.getValue(GummiHangarBlock.FACING), size) == null){
            Component warning = Component.translatable(Strings.WarningNoCore);
            player.displayClientMessage(warning, true);
            SCShowWarning.send(player, warning);
            return;
        }
        if(Utils.getCorePosCount(level,origin,hangar.getValue(GummiHangarBlock.FACING), size) != 1){
            Component warning = Component.translatable(Strings.WarningSingleCore).append(""+Utils.getCorePosCount(level,origin,hangar.getValue(GummiHangarBlock.FACING), size));
            player.displayClientMessage(warning, true);
            SCShowWarning.send(player, warning);
            return;
        }
		if(Utils.getAmountOfGummiShipsInBuildPlate(level, origin, hangar.getValue(GummiHangarBlock.FACING), size) > 0){
			Component warning = Component.translatable(Strings.WarningPlateOccupied);
			player.displayClientMessage(warning, true);
			SCShowWarning.send(player, warning);
			return;
		}

		container.TE.setLastShipName("");

		GummiStructure struct = Utils.getGummiStructureWithFacing(player.getUUID(), name, level, origin, hangar.getValue(GummiHangarBlock.FACING), size);
		GummiShipEntity shipEntity = new GummiShipEntity(level, struct);

        boolean xEven = Utils.isStructureEven(struct)[0];
        boolean zEven = Utils.isStructureEven(struct)[1];

		switch (hangar.getValue(GummiHangarBlock.FACING)) {
			case SOUTH -> {
				shipEntity.setPos(new Vec3(origin.getX()+(xEven ? 0F : 0.5F), origin.getY(), origin.getZ()-(size/2F)+(zEven ? 0.5F : 0)));
				shipEntity.setYRot(180);
			}
			case EAST -> {
				shipEntity.setPos(new Vec3(origin.getX()-(size/2F)+(zEven ? 0.5F : 0), origin.getY(), origin.getZ()+(xEven ? 1F : 0.5F)));
				shipEntity.setYRot(90);
			}
			case WEST -> {
				shipEntity.setPos(new Vec3(origin.getX()+(size/2F)+(zEven ? 0.5F : 1), origin.getY(), origin.getZ() + (xEven ? 0F : 0.5F)));
				shipEntity.setYRot(270);
			}
			default -> {
				shipEntity.setPos(new Vec3(origin.getX()+0.5F+(xEven ? 0.5F : 0), origin.getY(), origin.getZ()+(size/2F)+(zEven ? 0.5F : 1)));
				shipEntity.setYRot(0);
			}
		}

		level.addFreshEntity(shipEntity);
        BlockPos corePos = Utils.getCorePos(level, origin, hangar.getValue(GummiHangarBlock.FACING), size);

        if(corePos != null){
            //copy data from TE to entity
            BlockEntity te = level.getBlockEntity(corePos);
            if(te instanceof GummiCoreTileEntity core) {
                core.loadToShip(shipEntity);
            }

        }

		Utils.removeBlocks(level, origin, hangar.getValue(GummiHangarBlock.FACING), size);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
