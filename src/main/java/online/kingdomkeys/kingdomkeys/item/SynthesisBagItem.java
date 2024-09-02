package online.kingdomkeys.kingdomkeys.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.LevelLoadStatusManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.menu.SynthesisBagMenu;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class SynthesisBagItem extends Item implements IItemCategory {

	public SynthesisBagItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (!level.isClientSide) {
			PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer)player);
			MenuProvider container = new SimpleMenuProvider((w, p, pl) -> new SynthesisBagMenu(w, p, stack), stack.getHoverName());
			player.openMenu(container, buf -> {
				buf.writeBoolean(hand == InteractionHand.MAIN_HAND);
			});
		}
		return InteractionResultHolder.consume(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
		BagLevel level = stack.get(ModComponents.SYNTH_BAG_LEVEL);
		if (level != null) {
			int bagLevel = level.level;
			tooltip.add(Component.translatable(Utils.translateToLocal(Strings.Gui_Menu_Status_Level)+" "+(bagLevel+1)));
		}
		super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.TOOL;
	}

	public record Inventory(ItemContainerContents inventory) {
		public static final Codec<Inventory> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						ItemContainerContents.CODEC.fieldOf("inventory").forGetter(Inventory::inventory)
				).apply(instance, Inventory::new)
		);
		public static final StreamCodec<RegistryFriendlyByteBuf, Inventory> STREAM_CODEC = StreamCodec.composite(
				ItemContainerContents.STREAM_CODEC,
				Inventory::inventory,
				Inventory::new
		);
	}

	public static class BagLevel {
		int level, maxLevel;
		int baseCost;

		public BagLevel(int level, int maxLevel, int baseCost) {
			this.level = level;
			this.baseCost = baseCost;
			this.maxLevel = maxLevel;
		}

		public int level() {
			return level;
		}

		public int baseCost() {
			return baseCost;
		}

		public int maxLevel() {
			return maxLevel;
		}

		public static final Codec<BagLevel> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						Codec.INT.fieldOf("level").forGetter(BagLevel::level),
						Codec.INT.fieldOf("max_level").forGetter(BagLevel::maxLevel),
						Codec.INT.fieldOf("base_cost").forGetter(BagLevel::baseCost)
				).apply(instance, BagLevel::new)
		);

		public static final StreamCodec<RegistryFriendlyByteBuf, BagLevel> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.INT,
				BagLevel::level,
				ByteBufCodecs.INT,
				BagLevel::maxLevel,
				ByteBufCodecs.INT,
				BagLevel::baseCost,
				BagLevel::new
		);

		public void levelUp() {
			if (level < maxLevel-1) {
				level++;
			} else {
				level = maxLevel-1;
			}
		}

		public int getCost() {
			return (int) Math.pow(baseCost * 2, level);
		}
	}
}
