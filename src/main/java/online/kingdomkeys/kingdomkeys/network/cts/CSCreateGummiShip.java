package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.GummiHangarBlock;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;
import online.kingdomkeys.kingdomkeys.menu.GummiHangarMenu;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.ArrayList;
import java.util.stream.Collectors;

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

		GummiHangarMenu container = (GummiHangarMenu) player.containerMenu;
		BlockPos origin = container.TE.getBlockPos();
		Level level = player.level();
		BlockState hangar = level.getBlockState(origin);

		int size = GummiHangarBlock.getSize(hangar.getValue(GummiHangarBlock.LEVEL));
		ArrayList<Block> bannedBlocks = Utils.getBannedBlocks(level,origin,hangar.getValue(GummiHangarBlock.FACING), size);
		if(bannedBlocks != null && !bannedBlocks.isEmpty()) {
			String bannedBlocksNames = bannedBlocks.stream().map(block -> block.asItem().getDescription().getString()).collect(Collectors.joining(", "));
			player.sendSystemMessage(Component.translatable("Structure contains banned blocks: ").append(Component.literal(bannedBlocksNames)));
			return;
		}
		if(Utils.getAmountOfGummiShipsInBuildPlate(level, origin, hangar.getValue(GummiHangarBlock.FACING), size) > 0){
			return;
		}

		GummiStructure struct = Utils.getGummiStructureWithFacing(player.getUUID(), name, level, origin, hangar.getValue(GummiHangarBlock.FACING), size);
		GummiShipEntity shipEntity = new GummiShipEntity(level, struct);

        boolean xEven = Utils.isStructureEven(struct)[0];
        boolean zEven = Utils.isStructureEven(struct)[1];

		switch (hangar.getValue(GummiHangarBlock.FACING)) {
			default -> {
				shipEntity.setPos(new Vec3(origin.getX()+0.5F+(xEven ? 0.5F : 0), origin.getY(), origin.getZ()+(size/2F)+(zEven ? 0.5F : 1)));
				shipEntity.setYRot(0);
			}
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
		}

		level.addFreshEntity(shipEntity);
		Utils.removeBlocks(level, origin, hangar.getValue(GummiHangarBlock.FACING), size);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
