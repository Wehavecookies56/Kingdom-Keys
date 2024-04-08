package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.ITeleporter;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class WayfinderItem extends Item {
	public WayfinderItem(Properties pProperties) {
		super(pProperties);
	}



	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (entityIn instanceof Player player) {
			if (stack.getTag() != null) {
				if (!stack.getTag().hasUUID("ownerUUID"))
					stack.setTag(setID(stack.getTag(), player));
			} else {
				stack.setTag(setID(new CompoundTag(), player));
			}
			super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
		}
	}

	public void teleport(Player player, Entity owner, BlockPos pos, ResourceKey<Level> dimension){
		owner = null;
		ServerPlayer itemUser = (ServerPlayer) player;
		ServerPlayer wayfinderOwner = (ServerPlayer) owner;

		if (itemUser.level().dimension() != owner.level().dimension()){
			ServerLevel destionationWorld = itemUser.getServer().getLevel(dimension);
			itemUser.changeDimension(destionationWorld);
			itemUser.teleportTo(owner.getX()+0.5,owner.getY(),owner.getZ()+0.5);
			itemUser.setDeltaMovement(0,0,0);
		} else {
			itemUser.teleportTo(owner.getX()+0.5,owner.getY(),owner.getZ()+0.5);
			itemUser.setDeltaMovement(0,0,0);
		}
	}

	public CompoundTag setID(CompoundTag nbt, Player player) {
		nbt.putUUID("ownerUUID", player.getUUID());
		nbt.putString("ownerName", player.getDisplayName().getString());
		return nbt;
	}


	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		return super.use(world, player, hand);

	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if (stack.getTag() != null) {
			tooltip.add(Component.translatable(ChatFormatting.GRAY + "Owner: " + stack.getTag().getString("ownerName").toString()));
		}
	}

	@Override
	public boolean isEnchantable(ItemStack pStack) {
		return false;
	}

}
