package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import online.kingdomkeys.kingdomkeys.item.card.CardCategory;
import online.kingdomkeys.kingdomkeys.item.card.MapCardItem;
import online.kingdomkeys.kingdomkeys.lib.ModTags;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenCardPack;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CardPackItem extends Item implements ICreativeTab{
	private final @Nullable CardCategory category;

	public CardPackItem(Properties properties, @Nullable CardCategory type) {
		super(properties);
		this.category = type;
	}

	private List<ItemStack> generateCards(ServerPlayer player) {
		List<ItemStack> result = new ArrayList<>();

		for (int i = 0; i < 5; i++) {
			Item item = randomCard(player);
			ItemStack stack = new ItemStack(item);

			if (item instanceof MapCardItem card) {
				card.initialize(stack);
			}
			result.add(stack);
		}

		return result;
	}

	private Item randomCard(ServerPlayer player) {
		HolderSet.Named<Item> tag = player.level().registryAccess().lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.MAP_CARD);

		List<Item> cards = tag.stream()
				.map(holder -> holder.value())
				.filter(item -> {
					if (!(item instanceof MapCardItem card))
						return false;
					if (category == null)
						return card.category != CardCategory.YELLOW;
					return card.category == category;
				}).toList();

		if (cards.isEmpty())
			return ItemStack.EMPTY.getItem();

		return cards.get(player.getRandom().nextInt(cards.size()));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack pack = player.getItemInHand(hand);
		if (!(player instanceof ServerPlayer serverPlayer))
			return InteractionResultHolder.success(pack);

		List<ItemStack> cards = generateCards(serverPlayer);
		for (ItemStack stack : cards) {
			giveCard(serverPlayer, stack.copy());
		}

		PacketHandler.sendTo(new SCOpenCardPack(cards), serverPlayer);
		pack.shrink(1);
		return InteractionResultHolder.consume(pack);
	}

	private void giveCard(ServerPlayer player, ItemStack stack) {
		if (Utils.hasOnlyOneBag(player, BagItem.Type.CARDS_BAG)) {
			ItemStack bag = player.getInventory().getItem(Utils.getBagSlot(player, BagItem.Type.CARDS_BAG));
			IItemHandler inv = bag.getCapability(Capabilities.ItemHandler.ITEM);

			if (inv != null) {
				ItemStack remaining = stack.copy();

				for (int i = 0; i < inv.getSlots() && !remaining.isEmpty(); i++) {
					remaining = inv.insertItem(i, remaining, false);
				}

				if (remaining.isEmpty()) {
					return;
				}

				stack = remaining;
			}
		}

		if (!player.getInventory().add(stack)) {
			player.drop(stack, false);
		}
	}

	@Override
	public Tab getTab() {
		return Tab.CARDS;
	}
}