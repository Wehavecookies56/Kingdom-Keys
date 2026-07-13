package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.item.PauldronItem;
import online.kingdomkeys.kingdomkeys.menu.PauldronInventory;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.UUID;

public record CSSummonArmor(boolean forceDesummon) implements Packet {

	public static final Type<CSSummonArmor> TYPE = new Type<>(KingdomKeys.rl("cs_summon_armor"));

	public static final StreamCodec<FriendlyByteBuf, CSSummonArmor> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL,
			CSSummonArmor::forceDesummon,
			CSSummonArmor::new
	);

	public CSSummonArmor() {
		this(false);
	}
	
	/**
	 * Checks all armor slots and if at least one unequips returns true
	 * @param player
	 * @param KBArmorUUID
	 * @return
	 */
	private static boolean checkAllArmorSlots(Player player, UUID KBArmorUUID, PlayerData playerData) {
		boolean unequipped = false;
		unequipped = checkAndEmptyArmorSlot(36, player, KBArmorUUID, playerData) || unequipped;
		unequipped = checkAndEmptyArmorSlot(37, player, KBArmorUUID, playerData) || unequipped;
		unequipped = checkAndEmptyArmorSlot(38, player, KBArmorUUID, playerData) || unequipped;
		unequipped = checkAndEmptyArmorSlot(39, player, KBArmorUUID, playerData) || unequipped;
		return unequipped;
	}

	private static boolean checkAndEmptyArmorSlot(int i, Player player, UUID KBArmorUUID, PlayerData playerData) {
		if (Utils.hasArmorID(player.getInventory().getItem(i)) && Utils.getArmorID(player.getInventory().getItem(i)).equals(KBArmorUUID)) {
			Utils.desummonArmour(playerData, player, player.getInventory().getItem(i), i, true, false);
			return true;
		}
		return false;
	}

	private static ItemStack getNewItemWithUUID(ItemStack item, UUID uuid) {
		ItemStack newItem = item.copy();
		newItem.setDamageValue(item.getDamageValue());
		newItem.set(ModComponents.ARMOR_ID, uuid);
		return newItem;
	}
	
	private static void spawnArmorParticles(Player summoner) {
		Vec3 userPos = new Vec3(summoner.getX(), summoner.getY(), summoner.getZ());
        ((ServerLevel)summoner.level()).sendParticles(ParticleTypes.FIREWORK, userPos.x, summoner.getY() + 1, userPos.z, 300, 0,0,0, 0.2);
	}

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		PlayerData playerData = PlayerData.get(player);

		ItemStack kbArmorItem = playerData.getEquippedKBArmor(0);
		if(ItemStack.isSameItem(kbArmorItem, ItemStack.EMPTY))
			return;

		PauldronInventory pauldronInventory = (PauldronInventory) kbArmorItem.getCapability(Capabilities.ItemHandler.ITEM);

		int checkSlots = pauldronInventory.getSlots();
		for (int i = 0; i < pauldronInventory.getSlots(); ++i) {
			if (pauldronInventory.getStackInSlot(i).isEmpty()) {
				checkSlots--;
			}
		}

		if(kbArmorItem.getItem() == Items.AIR) //if empty abort
			return;

		if(kbArmorItem.getItem() instanceof PauldronItem kbArmor) { //If it's a valid shoulder armor
			UUID KBArmorUUID = kbArmorItem.get(ModComponents.ARMOR_ID);
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
			if(forceDesummon) {
				checkAllArmorSlots(player,KBArmorUUID, playerData);
			} else {
				if(correctArmor == checkSlots) { //If it's wearing the full correct armor or has to remove it
					//Desummon
					for(int i=36;i<40;i++) {
						Utils.desummonArmour(playerData, player, player.getInventory().getItem(i), i, true, false);
					}
					player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon_armor.get(), SoundSource.MASTER, 0.3f, 1.0f);
				} else {
					//If it's wearing any armor unequip it
					if(!(armor[0].getItem() == Items.AIR && armor[1].getItem() == Items.AIR && armor[2].getItem() == Items.AIR && armor[3].getItem() == Items.AIR)) {

						if(checkAllArmorSlots(player, KBArmorUUID, playerData)) {
							player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon_armor.get(), SoundSource.MASTER, 0.3f, 1.0f);
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
						ItemStack newHelmet = getNewItemWithUUID(pauldronInventory.getStackInSlot(0), KBArmorUUID);
						ItemStack newChestplate = getNewItemWithUUID(pauldronInventory.getStackInSlot(1), KBArmorUUID);
						ItemStack newLeggings = getNewItemWithUUID(pauldronInventory.getStackInSlot(2), KBArmorUUID);
						ItemStack newBoots = getNewItemWithUUID(pauldronInventory.getStackInSlot(3), KBArmorUUID);

						player.getInventory().setItem(39, newHelmet);
						player.getInventory().setItem(38, newChestplate);
						player.getInventory().setItem(37, newLeggings);
						player.getInventory().setItem(36, newBoots);
						player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.summon_armor.get(), SoundSource.MASTER, 0.2f, 1.0f);
						spawnArmorParticles(player);
					}
				}
			}

		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
