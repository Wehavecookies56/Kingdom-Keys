package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.GummiShipBlueprintItem;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.menu.GummiHangarMenu;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowWarning;

import java.io.ByteArrayInputStream;

public record CSLoadGummiShipFile(String name, byte[] data, int containerID) implements Packet {

	public static final Type<CSLoadGummiShipFile> TYPE = new Type<>(KingdomKeys.rl("cs_load_gummi_ship_file"));

	/** A serverbound custom payload is capped at 32767 bytes by vanilla, so refuse anything near it */
	public static final int MAX_BYTES = 30000;

	/** What the tag is allowed to weigh once unpacked, matching the cap the item component sync works to */
	private static final long MAX_UNPACKED = 2097152L;

	public static final StreamCodec<FriendlyByteBuf, CSLoadGummiShipFile> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8, CSLoadGummiShipFile::name,
			ByteBufCodecs.BYTE_ARRAY, CSLoadGummiShipFile::data,
			ByteBufCodecs.INT, CSLoadGummiShipFile::containerID,
			CSLoadGummiShipFile::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		if (player.containerMenu.containerId != containerID) {
			return;
		}

		if (data.length > MAX_BYTES) {
			Component tooBig = Component.translatable(Strings.WarningFileTooBig);
			player.sendSystemMessage(tooBig);
			SCShowWarning.send(player, tooBig);
			return;
		}

		GummiHangarMenu container = (GummiHangarMenu) player.containerMenu;
		ItemStack stack = container.getItems().getFirst();

		if (!GummiShipBlueprintItem.isBlueprint(stack)) {
			return;
		}

		try {
			CompoundTag tag = NbtIo.readCompressed(new ByteArrayInputStream(data), NbtAccounter.create(MAX_UNPACKED));
			GummiStructure structure = new GummiStructure(player.registryAccess(), tag);

			stack.set(ModComponents.GUMMI_STRUCTURE, structure.withoutBlockEntities());
			stack.set(ModComponents.BLUEPRINT_NAME, name);
			player.sendSystemMessage(Component.translatable("container.gummi_hangar.file_loaded", name));
		} catch (Exception e) {
			KingdomKeys.LOGGER.error("Could not load gummi ship {} sent by {}", name, player.getName().getString(), e);
			Component unreadable = Component.translatable(Strings.WarningFileUnreadable);
			player.sendSystemMessage(unreadable);
			SCShowWarning.send(player, unreadable);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
