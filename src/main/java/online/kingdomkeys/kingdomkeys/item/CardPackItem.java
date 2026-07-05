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
import online.kingdomkeys.kingdomkeys.item.card.CardCategory;
import online.kingdomkeys.kingdomkeys.item.card.MapCardItem;
import online.kingdomkeys.kingdomkeys.lib.ModTags;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenCardPack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CardPackItem extends Item implements ICreativeTab{
	private final @Nullable CardCategory category;

	public CardPackItem(Properties properties, @Nullable CardCategory type) {
		super(properties);
		this.category = type;
	}

	private List<Item> generateCards(ServerPlayer player) {
		List<Item> cards = new ArrayList<>();

		for (int i = 0; i < 5; i++) {
			cards.add(randomCard(player));
		}
		return cards;
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

		List<Item> cards = generateCards(serverPlayer);
		for (Item item : cards) {
			ItemStack card = new ItemStack(item);

			if (!serverPlayer.getInventory().add(card)) {
				serverPlayer.drop(card, false);
			}
		}

		PacketHandler.sendTo(new SCOpenCardPack(cards.stream().map(item -> item.builtInRegistryHolder().key().location()).toList()), serverPlayer);
		pack.shrink(1);
		return InteractionResultHolder.consume(pack);
	}

	@Override
	public Tab getTab() {
		return Tab.CARDS;
	}
}