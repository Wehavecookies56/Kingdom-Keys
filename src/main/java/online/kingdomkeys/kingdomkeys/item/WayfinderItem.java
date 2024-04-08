package online.kingdomkeys.kingdomkeys.item;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.joml.Vector3f;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
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
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.utils.BaseTeleporter;

public class WayfinderItem extends Item {
	Player owner;
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
			
			if(owner == null && !worldIn.isClientSide) {
				owner = getOwner((ServerLevel) player.level(), stack.getTag());
			}
			super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		if (!world.isClientSide) {
			ServerLevel serverLevel = (ServerLevel) world;
			ItemStack stack = player.getItemInHand(hand);

			owner = getOwner(serverLevel, stack.getTag());
			if (owner == null) {
				player.sendSystemMessage(Component.translatable("Player " + stack.getTag().getString("ownerName").toString() + " not found"));
				return super.use(world, player, hand);
			}

			teleport(player, owner);
			//pretty Stuff

			player.level().playSound(null, player.blockPosition(), ModSounds.unsummon_armor.get(), SoundSource.PLAYERS,1f,1f);

			((ServerLevel)player.level()).sendParticles(new DustParticleOptions(new Vector3f(1F,1F,1F),6F),player.getX(),player.getY() + 1.5,player.getZ(),150,0,0,0,0.2);
			((ServerLevel)player.level()).sendParticles(new DustParticleOptions(new Vector3f(1F,1F,1F),6F),player.getX(),player.getY() + 1,player.getZ(),150,0,0,0,0.2);
			((ServerLevel)player.level()).sendParticles(new DustParticleOptions(new Vector3f(1F,1F,1F),6F),player.getX(),player.getY() + 0.5,player.getZ(),150,0,0,0,0.2);
			((ServerLevel)player.level()).sendParticles(ParticleTypes.FIREWORK, player.getX(), player.getY() +1, player.getZ(), 300, 0,0,0, 0.2);
			//((ServerLevel)player.level()).sendParticles(ParticleTypes.END_ROD, player.getX(), player.getY() +1, player.getZ(), 300, 0,0,0, 0.2);

			//player.getCooldowns().addCooldown(this, 300 * 20);

		}
		return super.use(world, player, hand);
	}

	public void teleport(Player player, Entity owner) {
		if (player.level().dimension() != owner.level().dimension()) {
			ServerLevel destiinationWorld = owner.getServer().getLevel(owner.level().dimension());
			player.changeDimension(destiinationWorld, new BaseTeleporter(owner.getX() + 0.5, owner.getY(), owner.getZ() + 0.5));
		}

		player.teleportTo(owner.getX() + 0.5, owner.getY(), owner.getZ() + 0.5);
		player.setDeltaMovement(0, 0, 0);

	}

	public CompoundTag setID(CompoundTag nbt, Player player) {
		nbt.putUUID("ownerUUID", player.getUUID());
		nbt.putString("ownerName", player.getDisplayName().getString());
		return nbt;
	}

	public Player getOwner(ServerLevel level, CompoundTag nbt) {
		if (nbt == null)
			return null;

		UUID playerUUID = nbt.getUUID("ownerUUID");

		for (Player p : Utils.getAllPlayers(level.getServer())) {
			if (p.getUUID().equals(playerUUID)) {
				return p;
			}
		}
		return null;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if (stack.getTag() != null && owner != null) {
			tooltip.add(Component.translatable(ChatFormatting.GRAY + "Owner: " + stack.getTag().getString("ownerName").toString()));
			tooltip.add(Component.translatable(ChatFormatting.GRAY + "Cooldown: " + (int) (Minecraft.getInstance().player.getCooldowns().getCooldownPercent(this, 0) * 100) + "%"));
		}
	}

	@Override
	public boolean isEnchantable(ItemStack pStack) {
		return false;
	}
	/*
	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		// TODO Auto-generated method stub
		consumer.accept(new IClientItemExtensions() {
			@Override
			public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
				CompoundTag tag = itemInHand.getTag();
				if(owner != null) {
					IPlayerCapabilities playerData = ModCapabilities.getPlayer(owner);	
					if(playerData != null) {
						Color color = new Color(playerData.getNotifColor());
						RenderSystem.setShaderColor(color.getRed()/255F,color.getGreen()/255F,color.getBlue()/255F,1);
					}
					
				}

				return IClientItemExtensions.super.applyForgeHandTransform(poseStack, player, arm, itemInHand, partialTick, equipProcess, swingProcess);
			}
		});
		super.initializeClient(consumer);
	}*/


}
