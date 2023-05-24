package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.item.ShoulderArmorItem;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSSummonArmor {

	boolean forceDesummon;

	public CSSummonArmor() {
		forceDesummon = false;
	}

	public CSSummonArmor(boolean forceDesummon) {
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
				
				//Check if the armor it's wearing it's correct and count how many
				for(ItemStack stack : armor) {
					if(stack.getItem() != Items.AIR) {
						if(Utils.hasArmorID(stack)) {
							if(Utils.getArmorID(stack).equals(KBArmorUUID)){
								correctArmor++;
							} 
						} 
					} 
				}
				
				boolean hasRoom = true;
				if(message.forceDesummon) {
					checkAllArmorSlots(player,KBArmorUUID);
				} else {
					if(correctArmor == 4) { //If it's wearing the full correct armor or has to remove it
						//Desummon
						for(int i=36;i<40;i++) {
							player.getInventory().setItem(i, ItemStack.EMPTY);
						}
						player.level.playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon_armor.get(), SoundSource.MASTER, 1.0f, 1.0f);
					} else {
						//If it's wearing any armor unequip it
						if(!(armor[0].getItem() == Items.AIR && armor[1].getItem() == Items.AIR && armor[2].getItem() == Items.AIR && armor[3].getItem() == Items.AIR)) {
							System.out.println("now what");
							
							if(checkAllArmorSlots(player, KBArmorUUID)) {
								player.level.playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon_armor.get(), SoundSource.MASTER, 1.0f, 1.0f);
							}
							
							armor = new ItemStack[]{player.getInventory().getArmor(3),player.getInventory().getArmor(2),player.getInventory().getArmor(1),player.getInventory().getArmor(0)};
							
							//Check the amount of other armors
							int otherArmor = 0;
							for(int i=0;i<armor.length;i++) {
								if(armor[i].getItem() != Items.AIR) {
									otherArmor++;
								}
							}
							
							//If player has enough free slots to swap the armor
							if(Utils.getFreeSlotsForPlayer(player) >= otherArmor) {
								//swap
								for(int i=0;i<armor.length;i++) {
									if(armor[i].getItem() != Items.AIR) {
										Utils.swapStack(player.getInventory(), player.getInventory().getFreeSlot(), 39-i);
									}
								}
							} else { //If player has more armor to swap than free slots
								//complain
								hasRoom = false;
							}
							
							armor = new ItemStack[]{player.getInventory().getArmor(3),player.getInventory().getArmor(2),player.getInventory().getArmor(1),player.getInventory().getArmor(0)};
	
							if(armor[0].getItem() != Items.AIR || armor[1].getItem() != Items.AIR || armor[2].getItem() != Items.AIR || armor[3].getItem() != Items.AIR) {
								player.displayClientMessage(Component.translatable("gui.summonarmor.notenoughspace"), true);
							}
						}
						
						if(hasRoom) {
							ItemStack newHelmet = getNewItemWithUUID(((ShoulderArmorItem)kbArmorItem.getItem()).getArmor(3), KBArmorUUID);
							ItemStack newChestplate = getNewItemWithUUID(((ShoulderArmorItem)kbArmorItem.getItem()).getArmor(2), KBArmorUUID);
							ItemStack newLeggings = getNewItemWithUUID(((ShoulderArmorItem)kbArmorItem.getItem()).getArmor(1), KBArmorUUID);
							ItemStack newBoots = getNewItemWithUUID(((ShoulderArmorItem)kbArmorItem.getItem()).getArmor(0), KBArmorUUID);
							
							if(kbArmorItem.getTag() != null) {
								Tag bootsTag = kbArmorItem.getTag().get("boots");
								Tag legginsTag = kbArmorItem.getTag().get("leggings");
								Tag chestplateTag = kbArmorItem.getTag().get("chestplate");
								Tag helmetTag = kbArmorItem.getTag().get("helmet");
								if(bootsTag != null) {
									newBoots.getTag().put(ItemStack.TAG_ENCH, bootsTag);
								}
								
								if(legginsTag != null) {
									newLeggings.getTag().put(ItemStack.TAG_ENCH, legginsTag);
								}
								
								if(chestplateTag != null) {
									newChestplate.getTag().put(ItemStack.TAG_ENCH, chestplateTag);
								}
								
								if(helmetTag != null) {
									newHelmet.getTag().put(ItemStack.TAG_ENCH, helmetTag);
								}
							}
							
							player.getInventory().setItem(39, newHelmet);
							player.getInventory().setItem(38, newChestplate);
							player.getInventory().setItem(37, newLeggings);
							player.getInventory().setItem(36, newBoots);
							
							player.level.playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.summon_armor.get(), SoundSource.MASTER, 1.0f, 1.0f);
							spawnArmorParticles(player);
						}
					}
				}
				
			}
			
			
		});
		ctx.get().setPacketHandled(true);
	}
	
	/**
	 * Checks all armor slots and if at least one unequips returns true
	 * @param player
	 * @param KBArmorUUID
	 * @return
	 */
	private static boolean checkAllArmorSlots(Player player, UUID KBArmorUUID) {
		boolean unequipped = false;
		unequipped = checkAndEmptyArmorSlot(36, player, KBArmorUUID) || unequipped;
		unequipped = checkAndEmptyArmorSlot(37, player, KBArmorUUID) || unequipped;
		unequipped = checkAndEmptyArmorSlot(38, player, KBArmorUUID) || unequipped;
		unequipped = checkAndEmptyArmorSlot(39, player, KBArmorUUID) || unequipped;
		return unequipped;
	}

	private static boolean checkAndEmptyArmorSlot(int i, Player player, UUID KBArmorUUID) {
		if (Utils.hasArmorID(player.getInventory().getItem(i)) && Utils.getArmorID(player.getInventory().getItem(i)).equals(KBArmorUUID)) {
			player.getInventory().setItem(i, ItemStack.EMPTY);
			return true;
		}
		return false;
	}

	private static ItemStack getNewItemWithUUID(Item item, UUID uuid) {
		ItemStack newItem = new ItemStack(item);
		newItem.setTag(new CompoundTag());
		newItem.getTag().putUUID("armorID", uuid);
		return newItem;
	}
	
	private static void spawnArmorParticles(Player summoner) {
		Vec3 userPos = new Vec3(summoner.getX(), summoner.getY(), summoner.getZ());
        ((ServerLevel)summoner.level).sendParticles(ParticleTypes.FIREWORK, userPos.x, summoner.getY() + 1, userPos.z, 300, 0,0,0, 0.2);        
	}

}
