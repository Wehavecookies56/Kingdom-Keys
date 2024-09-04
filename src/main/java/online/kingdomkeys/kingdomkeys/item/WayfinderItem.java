package online.kingdomkeys.kingdomkeys.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
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
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.joml.Vector3f;

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
			if (!stack.has(ModComponents.WAYFINDER_OWNER)) {
				setID(stack, player);
			}
			
			if(!worldIn.isClientSide) {
				if(owner == null) 
					owner = getOwner((ServerLevel) player.level(), stack);
				
				if(owner != null) {
					PlayerData playerData = PlayerData.get(owner);
					if(playerData != null) {
						if(playerData.getNotifColor() != getColor(stack)) {
							stack.set(ModComponents.WAYFINDER_COLOR, playerData.getNotifColor());
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

			WayfinderOwner ownerdata = stack.get(ModComponents.WAYFINDER_OWNER);

			owner = getOwner(serverLevel, stack);
			if (owner == null) {
				player.displayClientMessage(Component.translatable("message.wayfinder.player_not_found",ownerdata.name), true);
				return super.use(world, player, hand);
			}
			Party p = WorldData.get(world.getServer()).getPartyFromMember(player.getUUID());
			
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
					player.displayClientMessage(Component.translatable("message.wayfinder.player_not_in_party",ownerdata.name), true);
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
			player.changeDimension(new DimensionTransition(destiinationWorld, new Vec3(owner.getX(), owner.getY(), owner.getZ()), Vec3.ZERO, player.getYRot(), player.getXRot(), entity -> {}));
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

	public void setID(ItemStack stack, Player player) {
		stack.set(ModComponents.WAYFINDER_OWNER, new WayfinderOwner(player.getUUID(), player.getDisplayName().getString()));

		PlayerData playerData = PlayerData.get(player);
		stack.set(ModComponents.WAYFINDER_COLOR, playerData.getNotifColor());
	}

	public Player getOwner(ServerLevel level, ItemStack stack) {
		if (!stack.has(ModComponents.WAYFINDER_OWNER))
			return null;

		UUID playerUUID = stack.get(ModComponents.WAYFINDER_OWNER).uuid;

		for (Player p : Utils.getAllPlayers(level.getServer())) {
			if (p.getUUID().equals(playerUUID)) {
				return p;
			}
		}
		return null;
	}

	public int getColor(ItemStack stack) {
		if(!stack.has(ModComponents.WAYFINDER_COLOR))
			return Color.WHITE.getRGB();
		
		return stack.get(ModComponents.WAYFINDER_COLOR);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
		if (stack.has(ModComponents.WAYFINDER_OWNER)) {
			tooltip.add(Component.translatable("message.wayfinder.owner", stack.get(ModComponents.WAYFINDER_OWNER).name));
			//tooltip.add(Component.translatable(""+new Color(stack.getTag().getInt("color"))));
			if(Minecraft.getInstance().player.getCooldowns().isOnCooldown(this))
				tooltip.add(Component.translatable("message.wayfinder.cooldown", (int) (Minecraft.getInstance().player.getCooldowns().getCooldownPercent(this, 0) * 100)));
		}
	}
	
	@Override
	public boolean isEnchantable(ItemStack pStack) {
		return false;
	}


	public record WayfinderOwner(UUID uuid, String name) {
		public static final Codec<WayfinderOwner> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				UUIDUtil.CODEC.fieldOf("uuid").forGetter(WayfinderOwner::uuid),
				Codec.STRING.fieldOf("name").forGetter(WayfinderOwner::name)
		).apply(instance, WayfinderOwner::new));
		public static final StreamCodec<FriendlyByteBuf, WayfinderOwner> STREAM_CODEC = StreamCodec.composite(
				UUIDUtil.STREAM_CODEC,
				WayfinderOwner::uuid,
				ByteBufCodecs.STRING_UTF8,
				WayfinderOwner::name,
				WayfinderOwner::new
		);
	}
}
