package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.menu.BagInventory;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowRareMeld;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.synthesis.melding.Melding;
import online.kingdomkeys.kingdomkeys.synthesis.melding.MeldingRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;

public record CSMeldRecipe(ResourceLocation recipe, int selected1, int selected2) implements Packet {

	public static final Type<CSMeldRecipe> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_meld_recipe"));

	public static final StreamCodec<FriendlyByteBuf, CSMeldRecipe> STREAM_CODEC = StreamCodec.composite(ResourceLocation.STREAM_CODEC, CSMeldRecipe::recipe, ByteBufCodecs.INT, CSMeldRecipe::selected1, ByteBufCodecs.INT, CSMeldRecipe::selected2, CSMeldRecipe::new);

	private static void consumeMagic(Player player, PlayerData playerData, int slot) {
		if (slot == -1) return;

		if (isEquippedSlot(slot)) {

			int equippedSlot = getEquippedIndex(slot);
			playerData.equipMagic(equippedSlot, ItemStack.EMPTY);

		} else if (isBagSlot(slot)) {

			int bagSlot = getBagIndex(slot);

			ItemStack magicBag = Utils.getItemInInventory(player, ModItems.magicsBag.get());
			if (!magicBag.isEmpty()) {
				if (magicBag.getCapability(Capabilities.ItemHandler.ITEM) instanceof BagInventory bagInv) {
					bagInv.setStackInSlot(bagSlot, ItemStack.EMPTY);
				}
			}
		} else {
			player.getInventory().removeItem(slot, 1);
		}
	}

	private static boolean isEquippedSlot(int slot) {
		return slot <= -1000 && slot > -2000;
	}

	private static boolean isBagSlot(int slot) {
		return slot <= -2000;
	}

	private static int getEquippedIndex(int slot) {
		return -1000 - slot;
	}

	private static int getBagIndex(int slot) {
		return -2000 - slot;
	}

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();

		Melding melding = MeldingRegistry.getInstance().getValue(recipe);

		if (melding == null) return;

		PlayerData playerData = PlayerData.get(player);

		if (playerData.getMunny() < melding.getCost()) return;

		consumeMagic(player, playerData, selected1);
		consumeMagic(player, playerData, selected2);

		playerData.setMunny(playerData.getMunny() - melding.getCost());

		ItemStack result;
		if (melding.hasBonus()) {
			int rand = (int) (Math.random() * 100);
			KingdomKeys.LOGGER.debug(melding.getRegistryName());
			KingdomKeys.LOGGER.debug("Number: " + rand + " Bonus chance: " + melding.getBonusChance());
			if (rand < melding.getBonusChance()) {
				result = new ItemStack(melding.getBonusResult(), melding.getBonusAmount());
				KingdomKeys.LOGGER.debug("Rare Meld!");
				PacketHandler.sendTo(new SCShowRareMeld(result.copy(), Strings.Gui_Menu_Items_Melding_RareItemAcquired), (ServerPlayer) player);
			} else {
				result = new ItemStack(melding.getResult(), melding.getAmount());
				PacketHandler.sendTo(new SCShowRareMeld(result.copy(), Strings.Gui_Menu_Items_Melding_ItemAcquired), (ServerPlayer) player);
			}
		} else {
			result = new ItemStack(melding.getResult(), melding.getAmount());
			PacketHandler.sendTo(new SCShowRareMeld(result.copy(), Strings.Gui_Menu_Items_Melding_ItemAcquired), (ServerPlayer) player);
		}

		player.getInventory().add(result);

		PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}