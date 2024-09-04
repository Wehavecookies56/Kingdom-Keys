package online.kingdomkeys.kingdomkeys.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.util.Utils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PauldronItem extends Item implements IItemCategory {
	String textureName;

	Item[] items;

	public PauldronItem(Properties properties, String textureName, Item[] items) {
		super(properties);
		this.textureName = textureName;
		this.items = items;
	}

	@Override
	public void onUseTick(Level levelIn, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
		if(pRemainingUseDuration <= getUseDuration(pStack, pLivingEntity)-20) {
			if (pLivingEntity instanceof Player player) {
				player.stopUsingItem();
				ItemEnchantments helmet = ItemEnchantments.EMPTY, chestplate = ItemEnchantments.EMPTY, leggings = ItemEnchantments.EMPTY, boots = ItemEnchantments.EMPTY;
                for (int i = 0; i < 4; i++) {
					ItemStack armorPieceStack = player.getInventory().getItem(36 + i);
		
					if (!ItemStack.isSameItem(armorPieceStack, ItemStack.EMPTY)) {
						if (armorPieceStack.isEnchanted() && !Utils.hasArmorID(armorPieceStack)) {
							switch (i) {
								case 0 -> boots = armorPieceStack.get(DataComponents.ENCHANTMENTS);
								case 1 -> leggings = armorPieceStack.get(DataComponents.ENCHANTMENTS);
								case 2 -> chestplate = armorPieceStack.get(DataComponents.ENCHANTMENTS);
								case 3 -> helmet = armorPieceStack.get(DataComponents.ENCHANTMENTS);
							}

							levelIn.playSound(player, player.blockPosition(), ModSounds.unsummon_armor.get(), SoundSource.MASTER, 1.0f, 1.0f);
							armorPieceStack.remove(DataComponents.ENCHANTMENTS);
						}
					}
				}
				pStack.set(ModComponents.PAULDRON_ENCHANTMENTS, new PauldronEnchantments(helmet, chestplate, leggings, boots));
			}
		}
		
		super.onUseTick(levelIn, pLivingEntity, pStack, pRemainingUseDuration);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level levelIn, Player playerIn, InteractionHand handIn) {
		boolean shouldSuck = false;
		for (int i = 0; i < 4; i++) {
			ItemStack armorPieceStack = playerIn.getInventory().getItem(36 + i);
			if (!ItemStack.isSameItem(armorPieceStack, ItemStack.EMPTY)) {
				if(armorPieceStack.isEnchanted()) {
					shouldSuck = true;
				}
			}
		}
		if(shouldSuck)
			playerIn.startUsingItem(handIn);
		
		return super.use(levelIn, playerIn, handIn);
	}

	@Override
	public int getUseDuration(ItemStack pStack, LivingEntity pEntity) {
		return 72000;
	}

	/**
	 * Returns the action that specifies what animation to play when the item is
	 * being used.
	 */
	@Override
	public UseAnim getUseAnimation(ItemStack pStack) {
		return UseAnim.BOW;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!stack.has(ModComponents.ARMOR_ID)) {
			stack.set(ModComponents.ARMOR_ID, UUID.randomUUID());
		}
		if (stack.has(ModComponents.PAULDRON_ENCHANTMENTS)) {
			PauldronEnchantments enchantments = stack.get(ModComponents.PAULDRON_ENCHANTMENTS);
			if (!enchantments.isEmpty()) {
				stack.set(DataComponents.RARITY, Rarity.EPIC);
			}
		}
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@Override
	public boolean isFoil(ItemStack pStack) {
		return pStack.getRarity() == Rarity.EPIC;
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.EQUIPMENT;
	}

	public String getTextureName() {
		return textureName;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
		if (stack.has(ModComponents.PAULDRON_ENCHANTMENTS)) {
			PauldronEnchantments enchantments = stack.get(ModComponents.PAULDRON_ENCHANTMENTS);
			appendEnchantmentNames(Component.translatable("kingdomkeys.helmet").getString() + ":", tooltip, enchantments.helmet);
			appendEnchantmentNames(Component.translatable("kingdomkeys.chestplate").getString() + ":", tooltip, enchantments.chestplate);
			appendEnchantmentNames(Component.translatable("kingdomkeys.leggings").getString() + ":", tooltip, enchantments.leggings);
			appendEnchantmentNames(Component.translatable("kingdomkeys.boots").getString() + ":", tooltip, enchantments.boots);
			if (flagIn.isAdvanced()) {
				if (stack.has(ModComponents.ARMOR_ID)) {
					tooltip.add(Component.translatable(ChatFormatting.RED + "DEBUG:"));
					tooltip.add(Component.translatable(ChatFormatting.WHITE + stack.get(ModComponents.ARMOR_ID).toString()));
				}
			}
		}
	}

	public Item getArmor(int slot) {
		return items[slot];
	}

	@Override
	public boolean canGrindstoneRepair(ItemStack stack) {
		return isFoil(stack);
	}

	public void appendEnchantmentNames(String text, List<Component> tooltip, ItemEnchantments data) {
		tooltip.add(Component.translatable(text));
		data.keySet().forEach(enchantmentHolder -> {
			enchantmentHolder.value();
			tooltip.add(Component.literal(ChatFormatting.GRAY + "- " + Enchantment.getFullname(enchantmentHolder, data.getLevel(enchantmentHolder)).getString()));
		});
	}

	public record PauldronEnchantments(ItemEnchantments helmet, ItemEnchantments chestplate, ItemEnchantments leggings, ItemEnchantments boots) {
		public static final Codec<PauldronEnchantments> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ItemEnchantments.CODEC.fieldOf("helmet").forGetter(PauldronEnchantments::helmet),
				ItemEnchantments.CODEC.fieldOf("chestplate").forGetter(PauldronEnchantments::chestplate),
				ItemEnchantments.CODEC.fieldOf("leggings").forGetter(PauldronEnchantments::leggings),
				ItemEnchantments.CODEC.fieldOf("boots").forGetter(PauldronEnchantments::boots)
		).apply(instance, PauldronEnchantments::new));

		public static final StreamCodec<RegistryFriendlyByteBuf, PauldronEnchantments> STREAM_CODEC = StreamCodec.composite(
				ItemEnchantments.STREAM_CODEC,
				PauldronEnchantments::helmet,
				ItemEnchantments.STREAM_CODEC,
				PauldronEnchantments::chestplate,
				ItemEnchantments.STREAM_CODEC,
				PauldronEnchantments::leggings,
				ItemEnchantments.STREAM_CODEC,
				PauldronEnchantments::boots,
				PauldronEnchantments::new
		);

		public boolean isEmpty() {
			return !helmet.isEmpty() || !chestplate.isEmpty() || !leggings.isEmpty() || !boots.isEmpty();
		}

		public int size() {
			return helmet.size() + chestplate.size() + leggings.size() + boots.size();
		}
	}
}
