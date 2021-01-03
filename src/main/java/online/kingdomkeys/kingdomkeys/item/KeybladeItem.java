package online.kingdomkeys.kingdomkeys.item;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.DoorBlock;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.particle.ExpParticleData;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSAttackOffhandPacket;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeData;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.Recipe;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class KeybladeItem extends SwordItem implements IItemCategory {

	// Level 0 = no upgrades, will use base stats in the data file
	public KeybladeData data;

	private Item.Properties properties;
	
	public Recipe recipe;

	// TODO remove attack damage
	public KeybladeItem(Item.Properties properties) {
		super(new KeybladeItemTier(0), 0, 1, properties);
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

	public String getDescription() {
		return data.getDescription();
	}

	public void setKeybladeData(KeybladeData data) {
		this.data = data;
	}

	public int getKeybladeLevel(ItemStack stack) {
		if(stack.hasTag()) {
			if(stack.getTag().contains("level")) {
				return stack.getTag().getInt("level");
			}			
		}
		return 0;
	}

	public void setKeybladeLevel(ItemStack stack, int level) {
		if(!stack.hasTag()) {
			stack.setTag(new CompoundNBT());
		}
		stack.getTag().putInt("level", level);
	}

	public Item.Properties getProperties() {
		return properties;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (entityIn instanceof PlayerEntity) {
			if (Utils.hasID(stack)) {
				PlayerEntity player = (PlayerEntity) entityIn;
				//Stupid workaround for itemSlot being 0 for offhand slot
				int slot = itemSlot;
				if (slot == 0) {
					if (ItemStack.areItemStacksEqual(stack, player.getHeldItemOffhand())) {
						slot = 40;
					}
				}
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
				if(playerData != null) {
					ItemStack mainChain = playerData.getEquippedKeychain(DriveForm.NONE);
					ItemStack formChain = null;
					if (!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()))
						formChain = playerData.getEquippedKeychain(new ResourceLocation(playerData.getActiveDriveForm()));
					if (formChain == null)
						formChain = ItemStack.EMPTY;
					UUID stackID = Utils.getID(stack);
					if (!ItemStack.areItemStacksEqual(mainChain, ItemStack.EMPTY) || !ItemStack.areItemStacksEqual(formChain, ItemStack.EMPTY)) {
						UUID mainChainID = Utils.getID(mainChain);
						UUID formChainID = Utils.getID(formChain);
						if (mainChainID == null)
							mainChainID = new UUID(0, 0);
						if (formChainID == null)
							formChainID = new UUID(0, 0);

						if (!(mainChainID.equals(stackID) || formChainID.equals(stackID))) {
							//This is either not your keychain or from an inactive form, either way it should not be here
							player.inventory.setInventorySlotContents(slot, ItemStack.EMPTY);
							player.world.playSound(null, player.getPosition(), ModSounds.unsummon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
						}
					} else {
						player.inventory.setInventorySlotContents(slot, ItemStack.EMPTY);
						player.world.playSound(null, player.getPosition(), ModSounds.unsummon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
					}

					//Check for dupes
					for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
						slot = itemSlot;
						if (i == 40) {
							if (ItemStack.areItemStacksEqual(stack, player.getHeldItemOffhand())) {
								slot = 40;
							}
						}
						if (i != slot) {
							UUID id = Utils.getID(player.inventory.getStackInSlot(i));
							if (id != null && player.inventory.getStackInSlot(i).getItem() instanceof KeybladeItem) {
								if (id.equals(stackID) && i != player.inventory.currentItem) {
									player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
									player.world.playSound(null, player.getPosition(), ModSounds.unsummon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
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
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		if (world.isRemote && player.getHeldItemOffhand() != null && player.getHeldItemOffhand().getItem() instanceof KeybladeItem) {
			RayTraceResult rtr = Minecraft.getInstance().objectMouseOver;
			if (rtr != null) {
				player.swingArm(Hand.OFF_HAND);

				if (rtr.getType() == Type.ENTITY) {
					EntityRayTraceResult ertr = (EntityRayTraceResult) rtr;
					if (!ItemStack.areItemStacksEqual(player.getHeldItem(Hand.OFF_HAND), ItemStack.EMPTY) && player.getHeldItem(Hand.OFF_HAND).getItem() instanceof KeybladeItem && hand == Hand.OFF_HAND) {
						if (ertr.getEntity() != null) {
							PacketHandler.sendToServer(new CSAttackOffhandPacket(ertr.getEntity().getEntityId()));
							return ActionResult.resultSuccess(itemstack);
						}
						return ActionResult.resultFail(itemstack);
					}
				}
			}
		}
		return super.onItemRightClick(world, player, hand);
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getPos();
		PlayerEntity player = context.getPlayer();
		
		SoundEvent sound;
		if (world.getBlockState(pos).getBlock() instanceof DoorBlock) {
		      DoubleBlockHalf doubleblockhalf = world.getBlockState(pos).get(DoorBlock.HALF);

			if (doubleblockhalf == DoubleBlockHalf.UPPER) {
				world.setBlockState(pos.down(), world.getBlockState(pos.down()).with(DoorBlock.OPEN, !world.getBlockState(pos.down()).get(DoorBlock.OPEN)));
				sound = world.getBlockState(pos.down()).get(DoorBlock.OPEN) ? SoundEvents.BLOCK_IRON_DOOR_CLOSE : SoundEvents.BLOCK_IRON_DOOR_OPEN;
			} else {
				world.setBlockState(pos, world.getBlockState(pos).with(DoorBlock.OPEN, !world.getBlockState(pos).get(DoorBlock.OPEN)));
				sound = world.getBlockState(pos).get(DoorBlock.OPEN) ? SoundEvents.BLOCK_IRON_DOOR_CLOSE : SoundEvents.BLOCK_IRON_DOOR_OPEN;
			}
			world.playSound(player, pos, sound, SoundCategory.BLOCKS, 1.0f, 1.0f);
			return ActionResultType.SUCCESS;

		} else if(world.getBlockState(pos).getBlock() instanceof TrapDoorBlock) {
			world.setBlockState(pos, world.getBlockState(pos).with(TrapDoorBlock.OPEN, !world.getBlockState(pos).get(TrapDoorBlock.OPEN)));
			sound = world.getBlockState(pos).get(TrapDoorBlock.OPEN) ? SoundEvents.BLOCK_IRON_DOOR_CLOSE : SoundEvents.BLOCK_IRON_DOOR_OPEN;
			world.playSound(player, pos, sound, SoundCategory.BLOCKS, 1.0f, 1.0f);
			return ActionResultType.SUCCESS;

		}
		return ActionResultType.PASS;
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (data != null) {
			if(getKeybladeLevel(stack) > 0)
				tooltip.add(new TranslationTextComponent(TextFormatting.YELLOW+"Level %s", getKeybladeLevel(stack)));
			tooltip.add(new TranslationTextComponent(TextFormatting.RED+"Strength %s", getStrength(getKeybladeLevel(stack))+DamageCalculation.getSharpnessDamage(stack)+" ["+DamageCalculation.getKBStrengthDamage(Minecraft.getInstance().player,stack)+"]"));
			tooltip.add(new TranslationTextComponent(TextFormatting.BLUE+"Magic %s", getMagic(getKeybladeLevel(stack))+" ["+DamageCalculation.getMagicDamage(Minecraft.getInstance().player,1,stack)+"]"));
			tooltip.add(new TranslationTextComponent(TextFormatting.WHITE+""+TextFormatting.ITALIC + getDescription()));
			if(recipe != null) {
				Iterator<Entry<Material, Integer>> it = recipe.getMaterials().entrySet().iterator();
				while(it.hasNext()) {
					Entry<Material, Integer> mat = it.next();
					tooltip.add(new TranslationTextComponent(TextFormatting.WHITE+""+ mat.getKey().getMaterialName()+" x"+mat.getValue()));
				}
			}
		}
		if (flagIn.isAdvanced()) {
			if (stack.getTag() != null) {
				if (stack.getTag().hasUniqueId("keybladeID")) {
					tooltip.add(new TranslationTextComponent(TextFormatting.RED + "DEBUG:"));
					tooltip.add(new TranslationTextComponent(TextFormatting.WHITE + stack.getTag().getUniqueId("keybladeID").toString()));
				}
			}
		}
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.TOOL;
	}

	@Mod.EventBusSubscriber
	public static class Events {

		@SubscribeEvent
		public static void onItemDropped(EntityJoinWorldEvent event) {
			if (event.getEntity() instanceof ItemEntity) {
				ItemStack droppedItem = ((ItemEntity)event.getEntity()).getItem();
				UUID droppedID = Utils.getID(droppedItem);
				if (droppedID != null && droppedItem.getItem() instanceof KeybladeItem) {
					event.setCanceled(true);
				}
			}
		}

	}
}
