package online.kingdomkeys.kingdomkeys.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
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
	public WayfinderItem(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(!worldIn.isClientSide && entityIn instanceof Player player && player.tickCount % 100 == 0) { //Check for color updates every 5 seconds
			Player owner = getOwner((ServerLevel) player.level(), stack);
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

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		if (!world.isClientSide) {
			ServerLevel serverLevel = (ServerLevel) world;
			ItemStack stack = player.getItemInHand(hand);

			if (!stack.has(ModComponents.WAYFINDER_OWNER)) { //Set owner once a player clicks a new wayfinder
				setID(stack, player);
				return super.use(world, player, hand);
			}

			WayfinderOwner ownerdata = stack.get(ModComponents.WAYFINDER_OWNER);
			Player owner = getOwner(serverLevel, stack);
			if (owner == null) {
				player.displayClientMessage(Component.translatable("message.wayfinder.player_not_found", ownerdata.name), true);
				return InteractionResultHolder.fail(stack);
			}

			if(owner == player) {
				player.displayClientMessage(Component.translatable("message.wayfinder.your_wayfinder").append(" ").append(ModConfigs.SERVER.wayfinderParty.get() ? Component.translatable("message.wayfinder.in_your_party") : Component.empty()), true);
				return InteractionResultHolder.fail(stack);
			}

			if(player.isCrouching()){ //Calling
				if(hasWayfinderOf(owner, player.getUUID())) {
					Component coloredPlayerName = Component.literal(player.getGameProfile().getName()).withColor(getColor(stack));
					Component coloredOwnerName = Component.literal(owner.getGameProfile().getName()).withColor(getColor(stack));

					owner.displayClientMessage(Component.translatable("message.wayfinder.calling_for_help", coloredPlayerName), true);
					player.displayClientMessage(Component.translatable("message.wayfinder.asking_other_for_help", coloredOwnerName), true);
					spawnWayfinderParticles((ServerLevel) player.level(), player, 1, getColor(stack), 35);
					player.getCooldowns().addCooldown(this, (ModConfigs.SERVER.wayfinderCD.get() * 20) / 10);
					return InteractionResultHolder.success(stack);
				} else {
					player.displayClientMessage(Component.translatable("message.wayfinder.player_has_no_wayfinder", owner.getGameProfile().getName()), true);
					return InteractionResultHolder.fail(stack);
				}
			} else { //Teleporting
				if (ModConfigs.SERVER.wayfinderParty.get()) {
					Party p = WorldData.get(world.getServer()).getPartyFromMember(player.getUUID());
					if (p == null) {
						player.displayClientMessage(Component.translatable("message.wayfinder.not_in_party"), true);
						return InteractionResultHolder.fail(stack);
					}

					if (!Utils.isEntityInParty(p, player)) {
						player.displayClientMessage(Component.translatable("message.wayfinder.player_not_in_party", ownerdata.name), true);
						return InteractionResultHolder.fail(stack);
					}
				}
				PlayerData playerData = PlayerData.get(player);
				teleport(player, owner, playerData.getNotifColor());
			}
		}
		return super.use(world, player, hand);
	}

	public void teleport(Player player, Entity owner, int color) {
		if (player.level().dimension() != owner.level().dimension()) {
			ServerLevel destinationWorld = owner.getServer().getLevel(owner.level().dimension());
			player.changeDimension(new DimensionTransition(destinationWorld, new Vec3(owner.getX(), owner.getY(), owner.getZ()), Vec3.ZERO, player.getYRot(), player.getXRot(), entity -> {}));
		}

		player.teleportTo(owner.getX(), owner.getY(), owner.getZ());
		player.setDeltaMovement(0, 0, 0);
		player.level().playSound(null, player.blockPosition(), ModSounds.unsummon_armor.get(), SoundSource.PLAYERS,1f,1f);

		spawnWayfinderParticles((ServerLevel) player.level(),player,1.5F, color, 50);
		spawnWayfinderParticles((ServerLevel) player.level(),player,1.0F, color, 50);
		spawnWayfinderParticles((ServerLevel) player.level(),player,0.5F, color, 50);
		((ServerLevel)player.level()).sendParticles(ParticleTypes.FIREWORK, player.getX(), player.getY() + 1, player.getZ(), 100, 0,0,0, 0.2);
		player.getCooldowns().addCooldown(this, (ModConfigs.SERVER.wayfinderCD.get() * 20));
	}

	public void spawnWayfinderParticles(ServerLevel level, Entity entity, float y, int color, int amount) {
		float r = ((color >> 16) & 0xFF) / 255F;
		float g = ((color >> 8) & 0xFF) / 255F;
		float b = (color & 0xFF) / 255F;

		DustParticleOptions dust = new DustParticleOptions(new Vector3f(r, g, b), 2F);
		level.sendParticles(dust, entity.getX(), entity.getY() + y, entity.getZ(), amount, 0.4, 0.6, 0.4, 0.02);
	}

	public void setID(ItemStack stack, Player player) {
		stack.set(ModComponents.WAYFINDER_OWNER, new WayfinderOwner(player.getUUID(), player.getGameProfile().getName()));
		PlayerData playerData = PlayerData.get(player);
		stack.set(ModComponents.WAYFINDER_COLOR, playerData.getNotifColor());
	}

	public Player getOwner(ServerLevel level, ItemStack stack) {
		WayfinderOwner owner = stack.get(ModComponents.WAYFINDER_OWNER);
		if (owner == null)
			return null;

		return level.getServer().getPlayerList().getPlayer(owner.uuid());
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
			Minecraft mc = Minecraft.getInstance();
			Player player = mc.player;
			tooltip.add(Component.translatable("message.wayfinder.tooltip1").withStyle(ChatFormatting.GRAY));
			tooltip.add(Component.translatable("message.wayfinder.tooltip2").withStyle(ChatFormatting.GRAY));

			tooltip.add(Component.translatable("message.wayfinder.owner", stack.get(ModComponents.WAYFINDER_OWNER).name));
			//tooltip.add(Component.translatable(""+new Color(stack.getTag().getInt("color"))));
			if(player.getCooldowns().isOnCooldown(this))
				tooltip.add(Component.translatable("message.wayfinder.cooldown", (int) (player.getCooldowns().getCooldownPercent(this, 0) * 100)));
		} else {
			tooltip.add(Component.translatable("message.wayfinder.none"));
		}
	}

	public boolean hasWayfinderOf(Player player, UUID targetUUID) {
		for (ItemStack stack : player.getInventory().items) {
			if (!(stack.getItem() instanceof WayfinderItem))
				continue;

			WayfinderOwner owner = stack.get(ModComponents.WAYFINDER_OWNER);

			if (owner != null && owner.uuid().equals(targetUUID)) {
				return true;
			}
		}

		return false;
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
