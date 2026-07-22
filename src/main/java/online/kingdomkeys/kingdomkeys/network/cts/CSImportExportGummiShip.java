package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiHangarBlock;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;
import online.kingdomkeys.kingdomkeys.menu.GummiHangarMenu;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.util.Utils;

import static online.kingdomkeys.kingdomkeys.block.gummi.GummiHangarBlock.DISPLAY_BLUEPRINT;

public record CSImportExportGummiShip(String name, int containerID, boolean export) implements Packet {

	public static final Type<CSImportExportGummiShip> TYPE = new Type<>(KingdomKeys.rl("cs_import_export_gummi_ship"));

	public static final StreamCodec<FriendlyByteBuf, CSImportExportGummiShip> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8,
			CSImportExportGummiShip::name,
			ByteBufCodecs.INT,
			CSImportExportGummiShip::containerID,
			ByteBufCodecs.BOOL,
			CSImportExportGummiShip::export,
			CSImportExportGummiShip::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		if (player.containerMenu.containerId != containerID)
			return;

		GummiHangarMenu container = (GummiHangarMenu) player.containerMenu;
		ItemStack stack = container.getItems().getFirst();

		BlockPos origin = container.TE.getBlockPos();
		Level level = player.level();

		BlockState hangar = level.getBlockState(origin);
		GummiStructure struct = Utils.getGummiStructureWithFacing(player.getUUID(), name, level, origin, hangar.getValue(GummiHangarBlock.FACING), GummiHangarBlock.getSize(hangar.getValue(GummiHangarBlock.LEVEL)));

		if(export) {
			if (stack.is(ModItems.gummiShipBlueprint.get())) {
				stack.set(ModComponents.GUMMI_STRUCTURE, struct);
				stack.set(ModComponents.BLUEPRINT_NAME, name);
			}
		} else {
			//IMPORT
			level.setBlockAndUpdate(origin,hangar.setValue(DISPLAY_BLUEPRINT, !hangar.getValue(DISPLAY_BLUEPRINT)));
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}