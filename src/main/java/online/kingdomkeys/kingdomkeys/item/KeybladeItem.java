package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.entity.organization.ArrowgunShotEntity;
import online.kingdomkeys.kingdomkeys.entity.organization.KKThrowableEntity;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.item.tier.KeybladeItemTier;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSAttackOffhandPacket;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeData;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.Recipe;
import online.kingdomkeys.kingdomkeys.util.IExtendedReach;
import online.kingdomkeys.kingdomkeys.util.Utils;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

public class KeybladeItem extends SwordItem implements IItemCategory, IExtendedReach {

	// Level 0 = no upgrades, will use base stats in the data file
	public KeybladeData data;

	private Item.Properties properties;
	
	public Recipe recipe;

	// TODO remove attack damage
	public KeybladeItem(Item.Properties properties) {
		super(new KeybladeItemTier(0), 0, -1F, properties);
		this.properties = properties;
	}

	// Get strength from the data based on the specified level
	public int getStrength(int level) {
		return data.getStrength(level);
	}

	// Get magic from the data based on the specified level
	public int getMagic(int level) {
		return data.getMagic(level);
	}

	// Get strength from the data based on actual level
	public int getStrength(ItemStack stack) {
		return data.getStrength(getKeybladeLevel(stack));
	}

	// Get magic from the data based on actual level
	public int getMagic(ItemStack stack) {
		return data.getMagic(getKeybladeLevel(stack));
	}

	public String getDesc() {
		return Utils.translateToLocal(data.getDescription());
	}

	public void setKeybladeData(KeybladeData data) {
		this.data = data;
	}

	public int getKeybladeLevel(ItemStack stack) {
		if(stack.hasTag() && stack.getTag().contains("level")) {
			return stack.getTag().getInt("level");
		}
		return 0;
	}

	public void setKeybladeLevel(ItemStack stack, int level) {
		if(!stack.hasTag()) {
			stack.setTag(new CompoundTag());
		}
		stack.getTag().putInt("level", level);
	}

	public int getMaxLevel(){
		return data.getMaxLevel();
	}

	public Item.Properties getProperties() {
		return properties;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (entityIn instanceof Player && !worldIn.isClientSide) {
			if (Utils.hasKeybladeID(stack)) {
				Player player = (Player) entityIn;
				//Stupid workaround for itemSlot being 0 for offhand slot
				int slot = itemSlot;
				if (slot == 0) {
					if (ItemStack.matches(stack, player.getOffhandItem())) {
						slot = 40;
					}
				}
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
				if(playerData != null) {
					ItemStack mainChain = playerData.getEquippedKeychain(DriveForm.NONE);
					if (playerData.getAlignment() != Utils.OrgMember.NONE) {
						mainChain = playerData.getEquippedWeapon();
					}
					if (mainChain != null) {
						ItemStack formChain = null;
						if (!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
							formChain = playerData.getEquippedKeychain(new ResourceLocation(playerData.getActiveDriveForm()));
						} else {
							if(playerData.isAbilityEquipped(Strings.synchBlade)) {
								formChain = playerData.getEquippedKeychain(DriveForm.SYNCH_BLADE);
							}
						}
						if (formChain == null)
							formChain = ItemStack.EMPTY;
						UUID stackID = Utils.getKeybladeID(stack);
						if (!ItemStack.matches(mainChain, ItemStack.EMPTY) || !ItemStack.matches(formChain, ItemStack.EMPTY)) {
							UUID mainChainID = Utils.getKeybladeID(mainChain);
							UUID formChainID = Utils.getKeybladeID(formChain);
							if (mainChainID == null)
								mainChainID = new UUID(0, 0);
							if (formChainID == null)
								formChainID = new UUID(0, 0);

							if (!(mainChainID.equals(stackID) || formChainID.equals(stackID))) {
								//This is either not your keychain or from an inactive form, either way it should not be here
								//System.out.println(formChainID);
								//if(playerData.isAbilityEquipped(Strings.synchBlade))
								player.getInventory().setItem(slot, ItemStack.EMPTY);
								player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
							}
						} else {
							player.getInventory().setItem(slot, ItemStack.EMPTY);
							player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
						}

						//Check for dupes
						for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
							slot = itemSlot;
							if (i == 40) {
								if (ItemStack.matches(stack, player.getOffhandItem())) {
									slot = 40;
								}
							}
							if (i != slot) {
								UUID id = Utils.getKeybladeID(player.getInventory().getItem(i));
								if (id != null && player.getInventory().getItem(i).getItem() instanceof KeybladeItem) {
									if (id.equals(stackID) && i != player.getInventory().selected) {
										player.getInventory().setItem(i, ItemStack.EMPTY);
										player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
									}
								}
							}
						}
					}
				}
			}
		}
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		Level level = player.level();
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		
		if (player.isCrouching() && playerData.isAbilityEquipped(Strings.strikeRaid)) { //Throw keyblade
			int slot = hand == InteractionHand.OFF_HAND ? player.getInventory().getContainerSize() - 1 : player.getInventory().selected;

			if (itemstack != null && !playerData.getRecharge()) {
				int cost = 10;
	    		cost -= cost * playerData.getNumberOfAbilitiesEquipped(Strings.mpThrift) * 0.2;
				playerData.remMP(Math.max(1, cost));
				
				if (!level.isClientSide) {
					level.playSound(null, player.blockPosition(), ModSounds.strike_raid.get(), SoundSource.PLAYERS, 1, 1);

					KKThrowableEntity entity = new KKThrowableEntity(level);
					entity.setData(DamageCalculation.getKBStrengthDamage(player, itemstack)*0.7F, player.getUUID(), slot, itemstack);
					entity.setPos(player.position().x, player.getEyePosition().y, player.position().z);

					entity.getEntityData().set(KKThrowableEntity.ITEMSTACK, itemstack);
					entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3F, 0F);
					level.addFreshEntity(entity);
					player.getCooldowns().addCooldown(itemstack.getItem(), 15);
				} else {
					player.swing(slot == 40 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
				}
				return InteractionResultHolder.success(itemstack);

			}
			return super.use(world, player, hand);
		} else { //Attack offhand and wisdom attack
			if (!player.getOffhandItem().isEmpty() && player.getOffhandItem().getItem() instanceof KeybladeItem) { // offhand kb attacking
				if (world.isClientSide && !player.getOffhandItem().isEmpty() && player.getOffhandItem().getItem() instanceof KeybladeItem) { // if kb in offhand
					HitResult rtr = player.getOffhandItem().getItem() instanceof IExtendedReach item ? InputHandler.getMouseOverExtended(item.getReach()) : Minecraft.getInstance().hitResult;
					if (rtr != null) {
						if (rtr.getType() == Type.ENTITY) {
							EntityHitResult ertr = (EntityHitResult) rtr;
							if (!ItemStack.matches(player.getItemInHand(InteractionHand.OFF_HAND), ItemStack.EMPTY) && player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof KeybladeItem && hand == InteractionHand.OFF_HAND) {
								if (ertr.getEntity() != null) {
									PacketHandler.sendToServer(new CSAttackOffhandPacket(ertr.getEntity().getId()));
									return InteractionResultHolder.success(itemstack);
								}
								return InteractionResultHolder.fail(itemstack);
							}
						} else {
							player.swing(InteractionHand.OFF_HAND);
						}
					}
				}
			} else { //Wisdom attack
				if(playerData.getActiveDriveForm().equals(Strings.Form_Wisdom)) {
					player.swing(hand);
					if(!world.isClientSide) {
						//System.out.println(DamageCalculation.getMagicDamage(player) * 0.1);
						ArrowgunShotEntity shot = new ArrowgunShotEntity(player.level(), player, DamageCalculation.getMagicDamage(player) * 0.1F);
						shot.setShotType(1);
						shot.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 3F, 0);
						world.addFreshEntity(shot);
						player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.wisdom_shot.get(), SoundSource.PLAYERS, 1F, 1F);

					}
				}
			}
			return super.use(world, player, hand);
		}
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if(ModConfigs.keybladeOpenDoors) {
			Level world = context.getLevel();
			BlockPos pos = context.getClickedPos();
			Player player = context.getPlayer();
	
			SoundEvent sound;
			if (world.getBlockState(pos).getBlock() instanceof DoorBlock) {
				DoubleBlockHalf doubleblockhalf = world.getBlockState(pos).getValue(DoorBlock.HALF);
	
				if (doubleblockhalf == DoubleBlockHalf.UPPER) {
					world.setBlockAndUpdate(pos.below(), world.getBlockState(pos.below()).setValue(DoorBlock.OPEN, !world.getBlockState(pos.below()).getValue(DoorBlock.OPEN)));
					sound = world.getBlockState(pos.below()).getValue(DoorBlock.OPEN) ? SoundEvents.IRON_DOOR_CLOSE : SoundEvents.IRON_DOOR_OPEN;
				} else {
					world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(DoorBlock.OPEN, !world.getBlockState(pos).getValue(DoorBlock.OPEN)));
					sound = world.getBlockState(pos).getValue(DoorBlock.OPEN) ? SoundEvents.IRON_DOOR_CLOSE : SoundEvents.IRON_DOOR_OPEN;
				}
				world.playSound(player, pos, sound, SoundSource.BLOCKS, 1.0f, 1.0f);
				return InteractionResult.SUCCESS;
	
			} else if (world.getBlockState(pos).getBlock() instanceof TrapDoorBlock) {
				world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(TrapDoorBlock.OPEN, !world.getBlockState(pos).getValue(TrapDoorBlock.OPEN)));
				sound = world.getBlockState(pos).getValue(TrapDoorBlock.OPEN) ? SoundEvents.IRON_DOOR_CLOSE : SoundEvents.IRON_DOOR_OPEN;
				world.playSound(player, pos, sound, SoundSource.BLOCKS, 1.0f, 1.0f);
				return InteractionResult.SUCCESS;
	
			}
		}
		return InteractionResult.PASS;
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if (data != null) {
			tooltip = ClientUtils.getTooltip(tooltip,stack);
			if(recipe != null) {
				Iterator<Entry<Material, Integer>> it = recipe.getMaterials().entrySet().iterator();
				while(it.hasNext()) {
					Entry<Material, Integer> mat = it.next();
					tooltip.add(Component.translatable(ChatFormatting.WHITE+""+ mat.getKey().getMaterialName()+" x"+mat.getValue()));
				}
			}
		} else {
			tooltip.add(Component.translatable(ChatFormatting.RED + "KEYBLADE DATA MISSING"));
			tooltip.add(Component.translatable(ChatFormatting.RED + "If you see this then either the keyblade json is missing or failed to load"));
			ResourceLocation key = ForgeRegistries.ITEMS.getKey(stack.getItem());
			tooltip.add(Component.translatable(ChatFormatting.RED + "It should be located in data/" + key.getNamespace() + "/keyblades/" + key.getPath() + ".json"));
			tooltip.add(Component.translatable(ChatFormatting.RED + "If the file exists check the syntax, see builtin keyblades for examples"));
		}
		if (flagIn.isAdvanced()) {
			if (stack.getTag() != null) {
				if (stack.getTag().hasUUID("keybladeID")) {
					tooltip.add(Component.translatable(ChatFormatting.RED + "DEBUG:"));
					tooltip.add(Component.translatable(ChatFormatting.WHITE + stack.getTag().getUUID("keybladeID").toString()));
				}
			}
		}
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.TOOL;
	}
	
	@Override
	public float getReach() {
		return data.getReach();
	}

	@Override
	public boolean isEnchantable(ItemStack pStack) {
		return true;
	}
	
	@Mod.EventBusSubscriber
	public static class KeybladeEvents {
		@SubscribeEvent
		public static void onItemToss(ItemTossEvent event) {
			ItemStack droppedItem = event.getEntity().getItem();
			UUID droppedID = Utils.getKeybladeID(droppedItem);
			if (droppedID != null && droppedItem.getItem() instanceof KeybladeItem) {
				Utils.summonKeyblade(event.getPlayer(), true, null);
			}
		}

		@SubscribeEvent
		public static void onItemDropped(EntityJoinLevelEvent event) {
			if (event.getEntity() instanceof ItemEntity iEntity) {
				ItemStack droppedItem = iEntity.getItem();
				UUID droppedID = Utils.getKeybladeID(droppedItem);
				if (droppedID != null && droppedItem.getItem() instanceof KeybladeItem) {
					iEntity.level().playSound(null, iEntity.position().x(),iEntity.position().y(),iEntity.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
					event.setCanceled(true);
				}
			}
		}
	}
}
