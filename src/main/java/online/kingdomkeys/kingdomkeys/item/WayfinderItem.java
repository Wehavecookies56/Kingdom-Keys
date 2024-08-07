package online.kingdomkeys.kingdomkeys.item;

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
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.utils.BaseTeleporter;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.UUID;

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
			
			if(!worldIn.isClientSide) {
				if(owner == null) 
					owner = getOwner((ServerLevel) player.level(), stack.getTag());
				
				if(owner != null) {
					IPlayerCapabilities playerData = ModCapabilities.getPlayer(owner);
					if(playerData != null) {
						if(playerData.getNotifColor() != getColor(stack)) {
							stack.getTag().putInt("color", playerData.getNotifColor());
						}
						
					}
				}
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
				player.displayClientMessage(Component.translatable("message.wayfinder.player_not_found",stack.getTag().getString("ownerName").toString()), true);
				return super.use(world, player, hand);
			}
			Party p = ModCapabilities.getWorld(world).getPartyFromMember(player.getUUID());
			
			if(owner == player) {
				player.displayClientMessage(Component.translatable("message.wayfinder.your_wayfinder").append(" ").append(ModConfigs.wayfinderOnlyParty ? Component.translatable("message.wayfinder.in_your_party").getString(): ""), true);
				return super.use(world, player, hand);
			}

			if(ModConfigs.wayfinderOnlyParty) {
				if(p == null) {
					player.displayClientMessage(Component.translatable("message.wayfinder.not_in_party"), true);
					return super.use(world, player, hand);
				}
				
				if(!Utils.isEntityInParty(p, player)) {
					player.displayClientMessage(Component.translatable("message.wayfinder.player_not_in_party",stack.getTag().getString("ownerName").toString()), true);
					return super.use(world, player, hand);
				}
			}
			teleport(player, owner, getColor(player.getItemInHand(hand)));
		}
		return super.use(world, player, hand);
	}

	public void teleport(Player player, Entity owner, int color) {
		if (player.level().dimension() != owner.level().dimension()) {
			ServerLevel destiinationWorld = owner.getServer().getLevel(owner.level().dimension());
			player.changeDimension(destiinationWorld, new BaseTeleporter(owner.getX(), owner.getY(), owner.getZ()));
		}

		player.teleportTo(owner.getX(), owner.getY(), owner.getZ());
		player.setDeltaMovement(0, 0, 0);

		player.level().playSound(null, player.blockPosition(), ModSounds.unsummon_armor.get(), SoundSource.PLAYERS,1f,1f);
		Color c = new Color(color);
		float r = c.getRed()/255F;
		float g = c.getGreen()/255F;
		float b = c.getBlue()/255F;
		((ServerLevel)player.level()).sendParticles(new DustParticleOptions(new Vector3f(r,g,b),6F),player.getX(),player.getY() + 1.5,player.getZ(),150,0,0,0,0.2);
		((ServerLevel)player.level()).sendParticles(new DustParticleOptions(new Vector3f(r,g,b),6F),player.getX(),player.getY() + 1,player.getZ(),150,0,0,0,0.2);
		((ServerLevel)player.level()).sendParticles(new DustParticleOptions(new Vector3f(r,g,b),6F),player.getX(),player.getY() + 0.5,player.getZ(),150,0,0,0,0.2);
		((ServerLevel)player.level()).sendParticles(ParticleTypes.FIREWORK, player.getX(), player.getY() +1, player.getZ(), 300, 0,0,0, 0.2);
		player.getCooldowns().addCooldown(this, 300 * 20);
	}

	public CompoundTag setID(CompoundTag nbt, Player player) {
		nbt.putUUID("ownerUUID", player.getUUID());
		nbt.putString("ownerName", player.getDisplayName().getString());
		nbt.putInt("color", Color.WHITE.getRGB());
		
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		if(playerData != null) {
			nbt.putInt("color", playerData.getNotifColor());
		}
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

	public int getColor(ItemStack stack) {
		if(stack.getTag() == null)
			return Color.WHITE.getRGB();
		
		return stack.getTag().getInt("color");
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if (stack.getTag() != null) {
			tooltip.add(Component.translatable("message.wayfinder.owner", stack.getTag().getString("ownerName").toString()));
			//tooltip.add(Component.translatable(""+new Color(stack.getTag().getInt("color"))));
			if(Minecraft.getInstance().player.getCooldowns().isOnCooldown(this))
				tooltip.add(Component.translatable("message.wayfinder.cooldown", (int) (Minecraft.getInstance().player.getCooldowns().getCooldownPercent(this, 0) * 100)));
		}
	}
	
	@Override
	public boolean isEnchantable(ItemStack pStack) {
		return false;
	}

}
