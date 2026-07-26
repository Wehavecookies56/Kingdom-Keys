package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.entity.organization.ArrowgunShotEntity;
import online.kingdomkeys.kingdomkeys.entity.organization.KKThrowableEntity;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.item.tier.KeybladeItemTier;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSAttackOffhandPacket;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeData;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.Recipe;
import online.kingdomkeys.kingdomkeys.util.IExtendedReach;
import online.kingdomkeys.kingdomkeys.util.IOffHandRange;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

public class KeybladeItem extends SwordItem implements IItemCategory, IExtendedReach, ICreativeTab {

	// Level 0 = no upgrades, will use base stats in the data file
	public KeybladeData data;

	private final Item.Properties properties;
	
	public Recipe recipe;

	// TODO remove attack damage
	public KeybladeItem(Item.Properties properties) {
		super(new KeybladeItemTier(0), properties.attributes(SwordItem.createAttributes(new KeybladeItemTier(0), 0, -2.4F)).component(DataComponents.UNBREAKABLE, new Unbreakable(false)));
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
		if(stack.has(ModComponents.KEYBLADE_LEVEL)) {
			return stack.get(ModComponents.KEYBLADE_LEVEL);
		}
		return 0;
	}

	public float setCritChance(float critChance){
		return 10;
	}

	public void setKeybladeLevel(ItemStack stack, int level) {
		stack.set(ModComponents.KEYBLADE_LEVEL, level);
	}

	public int getMaxLevel(){
		return data.getMaxLevel();
	}

	public float getCritChance() {return data.getCritChance();}

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
				PlayerData playerData = PlayerData.get(player);
				if(playerData != null) {
					ItemStack mainChain = playerData.getEquippedKeychain(DriveForm.NONE);
					if (playerData.getAlignment() != Utils.OrgMember.NONE) {
						mainChain = playerData.getEquippedWeapon();
					}
					if (mainChain != null) {
						ItemStack formChain = null;
						if (!playerData.noFormActive()) {
							formChain = playerData.getEquippedKeychain(playerData.getActiveDriveForm());
						} else {
							if(playerData.isAbilityEquipped(ModAbilities.SYNCH_BLADE)) {
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
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		PlayerData playerData = PlayerData.get(player);

		if (player.isCrouching() && playerData.isAbilityEquipped(ModAbilities.STRIKE_RAID)) { //Throw keyblade
			int slot = hand == InteractionHand.OFF_HAND ? player.getInventory().getContainerSize() - 1 : player.getInventory().selected;

			if (itemstack != null && !playerData.getRecharge()) {
				int cost = 10;
	    		cost -= (int) (cost * playerData.getNumberOfAbilitiesEquipped(ModAbilities.MP_THRIFT) * 0.2);
				playerData.remMP(Math.max(1, cost));
				
				if (!level.isClientSide) {
					level.playSound(null, player.blockPosition(), ModSounds.strike_raid.get(), SoundSource.PLAYERS, 1, 1);

					KKThrowableEntity entity = new KKThrowableEntity(level);
					switch (BuiltInRegistries.ITEM.getKey(itemstack.getItem()).getPath()) {
						case Strings.retribution:
							entity.setRotationPoint(0);
							break;
						default:
							entity.setRotationPoint(1);
					}
					entity.setData(DamageCalculation.getKBStrengthDamage(player, itemstack)*0.7F, player.getUUID(), slot, itemstack);
					entity.setPos(player.position().x, player.getEyePosition().y, player.position().z);

					//entity.setItem(itemstack);
					entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3F, 0F);
					level.addFreshEntity(entity);
					player.getCooldowns().addCooldown(itemstack.getItem(), 15);
				} else {
					player.swing(slot == 40 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
				}
				return InteractionResultHolder.success(itemstack);

			}
        } else { //Attack offhand and wisdom attack
			if (!player.getOffhandItem().isEmpty() && player.getOffhandItem().getItem() instanceof KeybladeItem) { // offhand kb attacking
				if (level.isClientSide && !player.getOffhandItem().isEmpty() && player.getOffhandItem().getItem() instanceof KeybladeItem) { // if kb in offhand
					HitResult rtr = InputHandler.pickExtend(player, ((IOffHandRange)player).kingdom_Keys$getOffHandEntityInteractionRange());
					if (rtr != null) {
						if (rtr.getType() == Type.ENTITY) {
							if (!ItemStack.matches(player.getItemInHand(InteractionHand.OFF_HAND), ItemStack.EMPTY) && player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof KeybladeItem && hand == InteractionHand.OFF_HAND) {
								EntityHitResult ertr = (EntityHitResult) rtr;
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
				if(playerData.isFormActive(ModDriveForms.WISDOM)) {
					player.swing(hand);
					if(!level.isClientSide) {
						ArrowgunShotEntity shot = new ArrowgunShotEntity(player.level(), player, DamageCalculation.getMagicDamage(player) * 0.1F);
						shot.setShotType(1);
						shot.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 3F, 0);
						level.addFreshEntity(shot);
						player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.wisdom_shot.get(), SoundSource.PLAYERS, 1F, 1F);

					}
				}
			}
        }
        return super.use(level, player, hand);
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
	public void appendHoverText(ItemStack stack, TooltipContext pContext, List<Component> tooltip, TooltipFlag flagIn) {
		if (data != null) {
			tooltip = ClientUtils.getTooltip(tooltip, pContext, stack);
			if(recipe != null) {
				Iterator<Entry<Item, Integer>> it = recipe.getMaterials().entrySet().iterator();
				while(it.hasNext()) {
					Entry<Item, Integer> mat = it.next();
					tooltip.add(Component.translatable(ChatFormatting.WHITE+ "" + new ItemStack(mat.getKey()).getHoverName() + " x"+mat.getValue()));
				}
			}
		} else {
			tooltip.add(Component.translatable("kingdomkeys.keyblade.data_missing.title").withStyle(ChatFormatting.RED));
			tooltip.add(Component.translatable("kingdomkeys.keyblade.data_missing.desc1").withStyle(ChatFormatting.RED));
			ResourceLocation key = BuiltInRegistries.ITEM.getKey(stack.getItem());
			tooltip.add(Component.translatable(ChatFormatting.RED + "It should be located in data/" + key.getNamespace() + "/keyblades/" + key.getPath() + ".json"));
			tooltip.add(Component.translatable("kingdomkeys.keyblade.data_missing.desc2").withStyle(ChatFormatting.RED));
		}
		if (flagIn.isAdvanced()) {
			UUID id = stack.get(ModComponents.KEYBLADE_ID);
			if (id != null) {
				tooltip.add(Component.translatable(ChatFormatting.RED + "DEBUG:"));
				tooltip.add(Component.translatable(ChatFormatting.WHITE + id.toString()));
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

	@Override
	public Tab getTab() {
		return Tab.KEYBLADES;
	}

	@EventBusSubscriber
	public static class KeybladeEvents {
		@SubscribeEvent
		public static void onItemToss(ItemTossEvent event) {
			ItemStack droppedItem = event.getEntity().getItem();
			UUID droppedID = Utils.getKeybladeID(droppedItem);
			if (droppedID != null && droppedItem.getItem() instanceof KeybladeItem) {
				event.setCanceled(true);
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
