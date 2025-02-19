package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.container.PauldronContainer;
import online.kingdomkeys.kingdomkeys.container.PauldronInventory;
import online.kingdomkeys.kingdomkeys.container.SynthesisBagContainer;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

import javax.annotation.Nullable;
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
		if(pRemainingUseDuration <= getUseDuration(pStack)-20) {
			if (pLivingEntity instanceof Player player) {
				player.stopUsingItem();
                for (int i = 0; i < 4; i++) {
					ItemStack armorPieceStack = player.getInventory().getItem(36 + i);
		
					if (!ItemStack.isSameItem(armorPieceStack, ItemStack.EMPTY)) {
						if (armorPieceStack.isEnchanted() && !Utils.hasArmorID(armorPieceStack)) {
							switch (i) {
								case 0 -> pStack.getTag().put("boots", armorPieceStack.getTag());
								case 1 -> pStack.getTag().put("leggings", armorPieceStack.getTag());
								case 2 -> pStack.getTag().put("chestplate", armorPieceStack.getTag());
								case 3 -> pStack.getTag().put("helmet", armorPieceStack.getTag());
							}

							levelIn.playSound(player, player.blockPosition(), ModSounds.unsummon_armor.get(), SoundSource.MASTER, 1.0f, 1.0f);
							armorPieceStack.setTag(new CompoundTag());
						}
					}
				}
			}
		}
		
		super.onUseTick(levelIn, pLivingEntity, pStack, pRemainingUseDuration);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (!level.isClientSide) {
			PacketHandler.sendTo(new SCSyncCapabilityPacket(ModCapabilities.getPlayer(player)), (ServerPlayer)player);
			MenuProvider container = new SimpleMenuProvider((w, p, pl) -> new PauldronContainer(w, p, stack), stack.getHoverName());
			NetworkHooks.openScreen((ServerPlayer) player, container, buf -> {
				buf.writeBoolean(hand == InteractionHand.MAIN_HAND);
			});
		}

		/**
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
		 **/
		
		return InteractionResultHolder.success(stack);
	}
	
	@Override
	public int getUseDuration(ItemStack pStack) {
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
		if (stack.getTag() != null) {
			if (!stack.getTag().hasUUID("armorID"))
				stack.setTag(setID(stack.getTag()));
		} else {
			stack.setTag(setID(new CompoundTag()));
		}
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	public CompoundTag setID(CompoundTag nbt) {
		nbt.putUUID("armorID", UUID.randomUUID());
		return nbt;
	}

	@Override
	public Rarity getRarity(ItemStack pStack) {
		if (pStack.getTag() == null)
			return super.getRarity(pStack);

		if (pStack.getTag().get("boots") != null || pStack.getTag().get("leggings") != null || pStack.getTag().get("chestplate") != null || pStack.getTag().get("helmet") != null) {
			return Rarity.EPIC; // Item enchant effect when any single enchantment is in
		}
		return super.getRarity(pStack);
	}

	@Override
	public boolean isFoil(ItemStack pStack) {
		return getRarity(pStack) == Rarity.EPIC;
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
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		stack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
			if (!iItemHandler.getStackInSlot(0).isEmpty()) {
				tooltip.add(Component.literal(Component.translatable("kingdomkeys.helmet").getString() + ": " + iItemHandler.getStackInSlot(0).getHoverName().getString()));
			}
			if (!iItemHandler.getStackInSlot(1).isEmpty()) {
				tooltip.add(Component.literal(Component.translatable("kingdomkeys.chestplate").getString() + ": " + iItemHandler.getStackInSlot(1).getHoverName().getString()));
			}
			if (!iItemHandler.getStackInSlot(2).isEmpty()) {
				tooltip.add(Component.literal(Component.translatable("kingdomkeys.leggings").getString() + ": " + iItemHandler.getStackInSlot(2).getHoverName().getString()));
			}
			if (!iItemHandler.getStackInSlot(3).isEmpty()) {
				tooltip.add(Component.literal(Component.translatable("kingdomkeys.boots").getString() + ": " + iItemHandler.getStackInSlot(3).getHoverName().getString()));
			}
		});

		if (flagIn.isAdvanced()) {
			if (stack.getTag().hasUUID("armorID")) {
				tooltip.add(Component.translatable(ChatFormatting.RED + "DEBUG:"));
				tooltip.add(Component.translatable(ChatFormatting.WHITE + stack.getTag().getUUID("armorID").toString()));
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

	@Override
	public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return new PauldronInventory();
	}
}
