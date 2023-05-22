package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.item.ShoulderArmorItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

public class CSSummonArmor {

	boolean forceDesummon;

	public CSSummonArmor() {
		forceDesummon = false;
	}

	public CSSummonArmor(boolean forceDesummon) {
		this.forceDesummon = forceDesummon;
	}

	//Don't pass none please
	public CSSummonArmor(ResourceLocation formToSummonFrom) {
		forceDesummon = false;
	}

	public CSSummonArmor(ResourceLocation formToSummonFrom, boolean forceDesummon) {
		this.forceDesummon = forceDesummon;
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeBoolean(forceDesummon);
	}

	public static CSSummonArmor decode(FriendlyByteBuf buffer) {
		CSSummonArmor msg = new CSSummonArmor();
		msg.forceDesummon = buffer.readBoolean();
		return msg;
	}

	public static void handle(CSSummonArmor message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			
			ItemStack kbArmorItem = playerData.getEquippedKBArmor(0);
			if(kbArmorItem.getItem() == Items.AIR) //if empty abort
				return;
			
			if(kbArmorItem.getItem() instanceof ShoulderArmorItem kbArmor) { //If it's a valid shoulder armor
				UUID KBArmorUUID = kbArmorItem.getTag().getUUID("armorID");
				ItemStack[] armor = {player.getInventory().getArmor(3),player.getInventory().getArmor(2),player.getInventory().getArmor(1),player.getInventory().getArmor(0)};
				
				int correctArmor = 0;
				
				//Check if the armor it's wearing it's correct
				for(ItemStack stack : armor) {
					System.out.println(stack);
					if(stack.getItem() != Items.AIR) {
						System.out.println("Is a helmet");
						if(stack.getTag().getUUID("armorID") != null) {
							System.out.println("Has UUID");
							if(stack.getTag().getUUID("armorID") == KBArmorUUID){
								System.out.println("Correct UUID");
								correctArmor++;
							} 
						} 
					} 
				}
				
				if(correctArmor == 4) { //If it's wearing the full correct armor
					//Desummon
					for(ItemStack stack : armor) {
						player.getInventory().setItem(39, ItemStack.EMPTY);
						player.getInventory().setItem(38, ItemStack.EMPTY);
						player.getInventory().setItem(37, ItemStack.EMPTY);
						player.getInventory().setItem(36, ItemStack.EMPTY);
						player.level.playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
					}
				} else {
					//If it's not wearing any armor equip it
					if(armor[0].getItem() == Items.AIR && armor[1].getItem() == Items.AIR && armor[2].getItem() == Items.AIR && armor[3].getItem() == Items.AIR) {
						ItemStack newHelmet = new ItemStack(ModItems.terra_Helmet.get());
						newHelmet.setTag(new CompoundTag());
						newHelmet.getTag().putUUID("armorID", KBArmorUUID);
						System.out.println(newHelmet.getTag().getUUID("armorID"));
						ItemStack newChestplate = new ItemStack(ModItems.terra_Chestplate.get());
						newHelmet.setTag(new CompoundTag());
						newHelmet.getTag().putUUID("armorID", KBArmorUUID);
						ItemStack newLeggings = new ItemStack(ModItems.terra_Leggings.get());
						newHelmet.setTag(new CompoundTag());
						newHelmet.getTag().putUUID("armorID", KBArmorUUID);
						ItemStack newBoots = new ItemStack(ModItems.terra_Boots.get());
						newHelmet.setTag(new CompoundTag());
						newHelmet.getTag().putUUID("armorID", KBArmorUUID);
						
						player.getInventory().setItem(39, newHelmet);
						player.getInventory().setItem(38, newChestplate);
						player.getInventory().setItem(37, newLeggings);
						player.getInventory().setItem(36, newBoots);
						
						player.level.playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.summon.get(), SoundSource.MASTER, 1.0f, 1.0f);

					} else {//If it's wearing some other armor despawn all legitimate armor if any, warn and abort
						System.out.println("now what");
						if (Utils.hasArmorID(player.getInventory().getItem(36)) && Utils.getArmorID(player.getInventory().getItem(36)).equals(KBArmorUUID)) {
							player.getInventory().setItem(36, ItemStack.EMPTY);
							player.level.playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
						}

						if (Utils.hasArmorID(player.getInventory().getItem(37)) && Utils.getArmorID(player.getInventory().getItem(37)).equals(KBArmorUUID)) {
							player.getInventory().setItem(37, ItemStack.EMPTY);
							player.level.playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
						}

						if (Utils.hasArmorID(player.getInventory().getItem(38)) && Utils.getArmorID(player.getInventory().getItem(38)).equals(KBArmorUUID)) {
							player.getInventory().setItem(38, ItemStack.EMPTY);
							player.level.playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
						}

						if (Utils.hasArmorID(player.getInventory().getItem(39)) && Utils.getArmorID(player.getInventory().getItem(39)).equals(KBArmorUUID)) {
							player.getInventory().setItem(39, ItemStack.EMPTY);
							player.level.playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
						}
						
						if(armor[0].getItem() == Items.AIR && armor[1].getItem() == Items.AIR && armor[2].getItem() == Items.AIR && armor[3].getItem() == Items.AIR) {
							//Fine
						} else {
							player.sendSystemMessage(Component.translatable("You should unequip your armor in order to summon the Keyblade Armor"));
						}

					}
				}
				
			}
			
			
		});
		ctx.get().setPacketHandled(true);
	}
	
	@Mod.EventBusSubscriber
	public static class Events {

		@SubscribeEvent
		public static void containerClose(PlayerContainerEvent.Close event) {
			ServerPlayer player = (ServerPlayer) event.getEntity();
			AbstractContainerMenu openContainer = event.getContainer();
			AbstractContainerMenu playerContainer = player.inventoryMenu;

			if (!openContainer.equals(playerContainer)) {
				openContainer.slots.forEach(slot -> {
					ItemStack stack = slot.getItem();
					IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
					if (slot.container != player.getInventory()) {
						if ((Utils.hasKeybladeID(stack) && stack.getItem() instanceof KeybladeItem) || (playerData.getAlignment() != Utils.OrgMember.NONE) && ((stack.getItem() instanceof IOrgWeapon || (playerData.getEquippedWeapon().getItem() == stack.getItem())))) {
							slot.set(ItemStack.EMPTY);
							if(stack.getItem() instanceof IOrgWeapon || (playerData.getAlignment() != Utils.OrgMember.NONE)) {
								Set<ItemStack> weapons = playerData.getWeaponsUnlocked();
								for(ItemStack weapon : weapons) {
									if(ItemStack.isSame(weapon, stack)) {
										weapon.setTag(stack.getTag());
										break;
									}
								}
								playerData.setWeaponsUnlocked(weapons);
							}
							openContainer.broadcastChanges();
							player.level.playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
						}
					}
				});
			}
		}

		@SubscribeEvent
		public static void dropItem(ItemTossEvent event) {
			ItemStack droppedItem = event.getEntity().getItem();
			//If it doesn't have an ID it was not summoned unless it's an org weapon
			Player player = event.getPlayer();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			if(playerData != null) {
				if (droppedItem != null) {
					if (playerData.getEquippedWeapon() != null) {
						if ((droppedItem.getItem() instanceof IOrgWeapon || (droppedItem.getItem() instanceof KeybladeItem && playerData.getAlignment() != OrgMember.NONE) || (playerData.getEquippedWeapon().getItem() == droppedItem.getItem()))) {
							if(droppedItem.getItem() instanceof IOrgWeapon || (droppedItem.getItem() instanceof KeybladeItem && playerData.getAlignment() != OrgMember.NONE)) { //If weapon is org weapon || a keyblade && org member
								Set<ItemStack> weapons = playerData.getWeaponsUnlocked();
								for(ItemStack weapon : weapons) {
									if(ItemStack.isSame(weapon, droppedItem)) {
										weapon.setTag(droppedItem.getTag());
										break;
									}
								}
								playerData.setWeaponsUnlocked(weapons);
							}
							player.level.playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
							event.setCanceled(true);
							return;
						}
					}
					if ((Utils.hasKeybladeID(droppedItem) && droppedItem.getItem() instanceof KeybladeItem)) {
						player.level.playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
						event.setCanceled(true);
					}
				}
			}
		}
	}

}
