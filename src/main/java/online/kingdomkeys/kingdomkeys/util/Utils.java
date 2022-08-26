package online.kingdomkeys.kingdomkeys.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategoryRegistry;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.item.KKAccessoryItem;
import online.kingdomkeys.kingdomkeys.item.KKArmorItem;
import online.kingdomkeys.kingdomkeys.item.KKResistanceType;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.limit.Limit;
import online.kingdomkeys.kingdomkeys.limit.ModLimits;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncWorldCapability;
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;

/**
 * Created by Toby on 19/07/2016.
 */
public class Utils {

	public static int getSlotFor(Inventory inv, ItemStack stack) {
		for (int i = 0; i < inv.getContainerSize(); ++i) {
			if (!inv.getItem(i).isEmpty() && ItemStack.matches(stack, inv.getItem(i))) {
				return i;
			}
		}
		return -1;
	}
	
	public static boolean isNumber(char c) {
		try {
			Integer.parseInt(String.valueOf(c));
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static int getInt(String num) {
		int number;
		try {
			number = Integer.parseInt(num);
			return number;
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static double getDouble(String num) {
		double number;
		try {
			number = Double.parseDouble(num);
			return number;
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static int clamp(int value, int min, int max) {
		return Math.min(Math.max(value, min), max);
	}
	
	public static float clamp(float value, float min, float max) {
		return Math.min(Math.max(value, min), max);
	}
	
	public static double clamp(double value, double min, double max) {
		return Math.min(Math.max(value, min), max);
	}
	

	/**
	 * Method for generating random integer between the 2 parameters, The order of
	 * min and max do not matter.
	 *
	 * @param min minimum value that the random integer can be
	 * @param max maximum value that the random integer can be
	 * @return a random integer
	 */
	public static int randomWithRange(int min, int max) {
		int range = Math.abs(max - min) + 1;
		return (int) (Math.random() * range) + (Math.min(min, max));
	}

	/**
	 * Method for generating random doubles between the 2 parameters, The order of
	 * min and max do not matter.
	 *
	 * @param min minimum value that the random double can be
	 * @param max maximum value that the random double can be
	 * @return a random double
	 */
	public static double randomWithRange(double min, double max) {
		double range = Math.abs(max - min);
		return (Math.random() * range) + (Math.min(min, max));
	}

	/**
	 * Method for generating random floats between the 2 parameters, The order of
	 * min and max do not matter.
	 *
	 * @param min minimum value that the random float can be
	 * @param max maximum value that the random float can be
	 * @return a random float
	 */
	public static float randomWithRange(float min, float max) {
		float range = Math.abs(max - min) + 1;
		return (float) (Math.random() * range) + (Math.min(min, max));
	}

	/**
	 * Replacement for
	 * the old i8n format method
	 * 
	 * @param name   the unlocalized string to translate
	 * @param format the format of the string
	 * @return the translated string
	 */
	public static String translateToLocalFormatted(String name, Object... format) {
		TranslatableComponent translation = new TranslatableComponent(name, format);
		return translation.getString();
	}

	/**
	 * Replacement for the old i8n translate to local method
	 * 
	 * @param name the unlocalized string to translate
	 * @return the translated string
	 */
	public static String translateToLocal(String name, Object... args) {
		TranslatableComponent translation = new TranslatableComponent(name, args);
		return translation.getString();
	}

	/**
	 * Get the ItemStack of the item that made the DamageSource
	 * 
	 * @param damageSource
	 * @param player
	 * @return
	 */
	public static ItemStack getWeaponDamageStack(DamageSource damageSource, Player player) {
		switch (damageSource.msgId) {
		case "player":
			if (player.getMainHandItem() != null && player.getMainHandItem().getItem() instanceof KeybladeItem || player.getMainHandItem().getItem() instanceof IOrgWeapon) {
				return player.getMainHandItem();
			}
			break;
		case "keybladeOffhand":
			if (player.getOffhandItem() != null && player.getOffhandItem().getItem() instanceof KeybladeItem || player.getOffhandItem().getItem() instanceof IOrgWeapon) {
				return player.getOffhandItem();
			}
		}
		return null;

	}

	public static enum OrgMember {
		NONE, XEMNAS, XIGBAR, XALDIN, VEXEN, LEXAEUS, ZEXION, SAIX, AXEL, DEMYX, LUXORD, MARLUXIA, LARXENE, ROXAS
	}

	public static int getDriveFormLevel(Map<String, int[]> map, String driveForm) {
		if (driveForm.equals(Strings.Form_Anti))
			return 7;
		return map.get(driveForm)[0];
	}

	public static LinkedHashMap<Material, Integer> getSortedMaterials(Map<Material, Integer> materials) {
		ArrayList<Material> list = new ArrayList<>();

		for (Material m : materials.keySet()) {
			list.add(m);
		}
		list.sort(Comparator.comparing(Material::getMaterialName));

		LinkedHashMap<Material, Integer> map = new LinkedHashMap<>();
		for (Material k : list) {
			map.put(k, materials.get(k));
		}
		return map;
	}

	public static LinkedHashMap<String, int[]> getSortedAbilities(LinkedHashMap<String, int[]> abilities) {
		List<Ability> list = new ArrayList<>();

		Iterator<String> it = abilities.keySet().iterator();
		while (it.hasNext()) {
			String entry = it.next();
			list.add(ModAbilities.registry.get().getValue(new ResourceLocation(entry)));
		}

		Collections.sort(list, Comparator.comparingInt(Ability::getOrder));

		LinkedHashMap<String, int[]> map = new LinkedHashMap<>();
		for (int i = 0; i < list.size(); i++) {
			map.put(list.get(i).getRegistryName().toString(), abilities.get(list.get(i).getRegistryName().toString()));
		}
		return map;
	}

	public static LinkedHashMap<String, int[]> getSortedDriveForms(LinkedHashMap<String, int[]> driveFormsMap) {
		List<DriveForm> list = new ArrayList<>();

		Iterator<String> it = driveFormsMap.keySet().iterator();
		while (it.hasNext()) {
			String entry = it.next();
			list.add(ModDriveForms.registry.get().getValue(new ResourceLocation(entry)));
		}

		Collections.sort(list, Comparator.comparingInt(DriveForm::getOrder));

		LinkedHashMap<String, int[]> map = new LinkedHashMap<>();
		for (int i = 0; i < list.size(); i++) {
			map.put(list.get(i).getRegistryName().toString(), driveFormsMap.get(list.get(i).getRegistryName().toString()));
		}

		return map;
	}

	public static LinkedHashMap<String, int[]> getSortedMagics(LinkedHashMap<String, int[]> magicsMap) {
		List<Magic> list = new ArrayList<>();

		Iterator<String> it = magicsMap.keySet().iterator();
		while (it.hasNext()) {
			String entry = it.next();
			list.add(ModMagic.registry.get().getValue(new ResourceLocation(entry)));
		}

		Collections.sort(list, Comparator.comparingInt(Magic::getOrder));

		LinkedHashMap<String, int[]> map = new LinkedHashMap<>();
		for (int i = 0; i < list.size(); i++) {
			map.put(list.get(i).getRegistryName().toString(), magicsMap.get(list.get(i).getRegistryName().toString()));
		}

		return map;
	}
	
	public static List<Limit> getPlayerLimitAttacks(Player player) {
//		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		List<Limit> limits = new ArrayList<Limit>();
		limits.addAll(ModLimits.registry.get().getValues());
		//TODO change when we have more member limits
       /* for(Limit val : ModLimits.registry.getValues()) {
        	System.out.println(val.getName());
        	if(val.getOwner() == playerData.getAlignment()) {
        		limits.add(val);
        		break;
        	}
        }*/
        return limits;
	}
	
	public static List<Limit> getSortedLimits(List<Limit> list) {
		Collections.sort(list, Comparator.comparingInt(Limit::getOrder));
		return list;
	}

	public static List<String> getSortedShotlocks(List<String> list) {
		Collections.sort(list, (Comparator.comparingInt(a -> ModShotlocks.registry.get().getValue(new ResourceLocation(a)).getOrder())));
		return list;
	}
	
	
	
	public static Player getPlayerByName(Level world, String name) {
		for (Player p : world.players()) {
			if (p.getDisplayName().getString().equals(name)) {
				return p;
			}
		}
		return null;
	}
	
	public static Player getClosestPlayer(Entity e) {
		Player nearest = null;
		if(e.getServer() == null) {
			return null;
		}
		for(Player p : getAllPlayers(e.getServer())){
			if(nearest == null) {
				nearest = p;
			}
			
			if(p.distanceTo(e) < nearest.distanceTo(e)) {
				nearest = p;
			}
		}
		return nearest;
	}

	public static List<Player> getAllPlayers(MinecraftServer ms) {
		List<Player> list = new ArrayList<Player>();
		java.util.Iterator<ServerLevel> it = ms.getAllLevels().iterator();
		while (it.hasNext()) {
			ServerLevel world = it.next();
			for (Player p : world.players()) {
				list.add(p);
			}
		}
		return list;
	}

	public static List<LivingEntity> getLivingEntitiesInRadius(Entity entity, float radius) {
		List<Entity> list = entity.level.getEntities(entity, entity.getBoundingBox().inflate(radius), Entity::isAlive);
		List<LivingEntity> elList = new ArrayList<LivingEntity>();
		for (Entity e : list) {
			if (e instanceof LivingEntity) {
				elList.add((LivingEntity) e);
			}
		}

		return elList;
	}
	
	public static List<Entity> removePartyMembersFromList(Player player, List<Entity> list){
		Party casterParty = ModCapabilities.getWorld(player.level).getPartyFromMember(player.getUUID());

		if(casterParty != null && !casterParty.getFriendlyFire()) {
			for(Member m : casterParty.getMembers()) {
				list.remove(player.level.getPlayerByUUID(m.getUUID()));
			}
		} else {
			list.remove(player);
		}
		return list;
	}

	public static List<LivingEntity> getLivingEntitiesInRadiusExcludingParty(Player player, float radius) {
		List<Entity> list = player.level.getEntities(player, player.getBoundingBox().inflate(radius), Entity::isAlive);
		Party casterParty = ModCapabilities.getWorld(player.level).getPartyFromMember(player.getUUID());

		if (casterParty != null && !casterParty.getFriendlyFire()) {
			for (Member m : casterParty.getMembers()) {
				list.remove(player.level.getPlayerByUUID(m.getUUID()));
			}
		} else {
			list.remove(player);
		}

		List<LivingEntity> elList = new ArrayList<LivingEntity>();
		for (Entity e : list) {
			if (e instanceof LivingEntity) {
				elList.add((LivingEntity) e);
			}
		}

		return elList;
	}
	
	/**
	 * Gets entities in radius from the entity param
	 * @param player to ignore from the list
	 * @param entity where to check with radius
	 * @param radiusX
	 * @param radiusY
	 * @param radiusZ
	 * @return
	 */
	public static List<LivingEntity> getLivingEntitiesInRadiusExcludingParty(Player player, Entity entity, float radiusX, float radiusY, float radiusZ) {
		List<Entity> list = player.level.getEntities(player, entity.getBoundingBox().inflate(radiusX,radiusY,radiusZ), Entity::isAlive);
		Party casterParty = ModCapabilities.getWorld(player.level).getPartyFromMember(player.getUUID());

		if (casterParty != null && !casterParty.getFriendlyFire()) {
			for (Member m : casterParty.getMembers()) {
				list.remove(player.level.getPlayerByUUID(m.getUUID()));
			}
		} else {
			list.remove(player);
		}
		
		list.remove(entity);
		
		List<LivingEntity> elList = new ArrayList<LivingEntity>();
		for (Entity e : list) {
			if (e instanceof LivingEntity) {
				elList.add((LivingEntity) e);
			}
		}

		return elList;
	}

	public static String getResourceName(String text) {
		return text.replaceAll("[ \\t]+$", "").replaceAll("\\s+", "_").replaceAll("[\\'\\:\\-\\,\\#]", "").replaceAll("\\&", "and").toLowerCase();
	}

	public static boolean hasID(ItemStack stack) {
		if (stack.getItem() instanceof KeybladeItem || stack.getItem() instanceof IKeychain) {
			if (stack.getTag() != null) {
				if (stack.getTag().hasUUID("keybladeID")) {
					return true;
				}
			}
		}
		return false;
	}

	public static UUID getID(ItemStack stack) {
		if (hasID(stack)) {
			return stack.getTag().getUUID("keybladeID");
		}
		return null;
	}

	// Returns the inv slot if summoned keychain is found
	public static int findSummoned(Inventory inv, ItemStack chain, boolean orgWeapon) {
		if (!ItemStack.matches(chain, ItemStack.EMPTY)) {
			for (int i = 0; i < inv.getContainerSize(); i++) {
				ItemStack slotStack = inv.getItem(i);
				// Only check the tag for keyblades
				if (slotStack.getItem() instanceof KeybladeItem) {
					if (orgWeapon) {
						return i;
					}
					// Make sure it has a tag
					if (hasID(slotStack)) {
						// Compare the ID with the chain's
						if (slotStack.getTag().getUUID("keybladeID").equals(chain.getTag().getUUID("keybladeID"))) {
							return i;
						}
					}
				} else if (slotStack.getItem() instanceof IOrgWeapon) {
					return i;
				}
			}
		}
		return -1;
	}

	public static void swapStack(Inventory inv, int stack1, int stack2) {
		ItemStack tempStack = inv.getItem(stack2);
		inv.setItem(stack2, inv.getItem(stack1));
		inv.setItem(stack1, tempStack);
	}

	// Returns the category for the stack from the IItemCategory interface, the registry, else it returns MISC
	public static ItemCategory getCategoryForStack(ItemStack stack) {
		ItemCategory category = ItemCategory.MISC;
		if (stack.getItem() instanceof IItemCategory) {
			category = ((IItemCategory) stack.getItem()).getCategory();
		} else if (ItemCategoryRegistry.hasCategory(stack.getItem())) {
			category = ItemCategoryRegistry.getCategory(stack.getItem());
		}
		return category;
	}

	public static ItemCategory getCategoryForRecipe(ResourceLocation location) {
		if (RecipeRegistry.getInstance().containsKey(location)) {
			return getCategoryForStack(new ItemStack(RecipeRegistry.getInstance().getValue(location).getResult()));
		} else {
			return ItemCategory.MISC;
		}
	}
	
	public static ItemCategory getCategoryForShop(ResourceLocation stackRL) {
		return getCategoryForStack(new ItemStack(ForgeRegistries.ITEMS.getValue(stackRL)));
	}
	
	public static int getAccessoriesStat(IPlayerCapabilities playerData, String type) {
		int res = 0;
		for(Entry<Integer, ItemStack> entry : playerData.getEquippedAccessories().entrySet()) {
			if(!ItemStack.matches(entry.getValue(), ItemStack.EMPTY)) {
				KKAccessoryItem accessory = (KKAccessoryItem)entry.getValue().getItem();
				switch(type) {
				case "ap":
					res += accessory.getAp();
					break;
				case "str":
					res += accessory.getStr();
					break;
				case "mag":
					res += accessory.getMag();
					break;
				}
			}
		}
		return res;
	}
	
	public static List<String> getAccessoriesAbilities(IPlayerCapabilities playerData) {
		List<String> res = new ArrayList<String>();
		for(Entry<Integer, ItemStack> entry : playerData.getEquippedAccessories().entrySet()) {
			if(!ItemStack.matches(entry.getValue(), ItemStack.EMPTY)) {
				KKAccessoryItem accessory = (KKAccessoryItem)entry.getValue().getItem();
				res.addAll(accessory.getAbilities());
			}
		}
		return res;
	}

	public static int getArmorsStat(IPlayerCapabilities playerData, String type) {
		int res = 0;
		for(Entry<Integer, ItemStack> entry : playerData.getEquippedArmors().entrySet()) {
			if(!ItemStack.matches(entry.getValue(), ItemStack.EMPTY)) {
				KKArmorItem kkArmorItem = (KKArmorItem) entry.getValue().getItem();
				switch(type) {
					case "def":
						res += kkArmorItem.getDefense();
						break;
					case "darkness":
						if(kkArmorItem.CheckKey(KKResistanceType.darkness))
							res+= kkArmorItem.GetResValue(KKResistanceType.darkness);
						break;
					case "ice":
						if(kkArmorItem.CheckKey(KKResistanceType.ice))
							res+= kkArmorItem.GetResValue(KKResistanceType.ice);
						break;

					case "lightning":
						if(kkArmorItem.CheckKey(KKResistanceType.lightning))
							res+= kkArmorItem.GetResValue(KKResistanceType.lightning);
						break;
					case "fire":
						if(kkArmorItem.CheckKey(KKResistanceType.fire))
							res+= kkArmorItem.GetResValue(KKResistanceType.fire);
						break;
				}
			}
		}
		return res;
	}
	public static int getConsumedAP(IPlayerCapabilities playerData) {
		int ap = 0;
		LinkedHashMap<String, int[]> map = playerData.getAbilityMap();
		Iterator<Entry<String, int[]>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, int[]> entry = it.next();
			Ability a = ModAbilities.registry.get().getValue(new ResourceLocation(entry.getKey()));
			ap += a.getAPCost() * Integer.bitCount(entry.getValue()[1]);
		}
		return ap;
	}

	public static double getMPHasteValue(IPlayerCapabilities playerData) {
		int val = 0;
		val += (2 * playerData.getNumberOfAbilitiesEquipped(Strings.mpHaste));
		val += (4 * playerData.getNumberOfAbilitiesEquipped(Strings.mpHastera));
		val += (6 * playerData.getNumberOfAbilitiesEquipped(Strings.mpHastega));
		return val;
	}

	public static void RefreshAbilityAttributes(Player player, IPlayerCapabilities playerData){
		if (player.level.isClientSide)
			return;

		Multimap<Attribute, AttributeModifier> map = HashMultimap.create();

		//Luck - affects things like chest loot, separate from looting or fortune.
		AttributeModifier attributemodifier = new AttributeModifier(UUID.fromString("7faaa8a8-fee1-422c-8f85-6794042e8f09"), Strings.luckyLucky, playerData.getNumberOfAbilitiesEquipped(Strings.luckyLucky), AttributeModifier.Operation.ADDITION);
		map.put(Attributes.LUCK, attributemodifier);


		player.getAttributes().addTransientAttributeModifiers(map);
	}

	public static boolean isWearingOrgRobes(Player player) {
		if(!ModConfigs.orgEnabled)
			return false;
		
		boolean wearingOrgCloak = true;
		for (int i = 0; i < player.getInventory().armor.size(); ++i) {
			ItemStack itemStack = player.getInventory().armor.get(i);
			if (itemStack.isEmpty() || !itemStack.getItem().getRegistryName().getPath().startsWith("organization_") && !itemStack.getItem().getRegistryName().getPath().startsWith("xemnas_") && !itemStack.getItem().getRegistryName().getPath().startsWith("anticoat_")) {
				wearingOrgCloak = false;
				break;
			}
		}
		return wearingOrgCloak;
	}

	public static int getBagCosts(int bagLevel) {
		switch (bagLevel) {
		case 0:
			return 10000;
		case 1:
			return 20000;
		case 2:
			return 40000;
		}
		return 0;
	}

	public static String snakeToCamel(String str) {
		// Capitalize first letter of string
		str = str.substring(0, 1).toUpperCase() + str.substring(1);

		// Run a loop till string string contains underscore
		while (str.contains("_")) {
			// Replace the first occurrence of letter that present after the underscore, to capitalize form of next letter of underscore
			str = str.replaceFirst("_[a-z]", String.valueOf(Character.toUpperCase(str.charAt(str.indexOf("_") + 1))));
		}
		str = str.substring(0, 1).toLowerCase() + str.substring(1);
		// Return string
		return str;
	}
	
	 /**
     * From {@link //LookController#getTargetPitch()}
     * @param target
     * @param entity
     * @return
     */
	public static float getTargetPitch(Entity entity, Entity target) {
        double xDiff = target.getX() - entity.getX();
        double yDiff = target.getY() - entity.getY();
        double zDiff = target.getZ() - entity.getZ();
        double distance = Mth.sqrt((float) (xDiff * xDiff + zDiff * zDiff));
        return (float) (-(Mth.atan2(yDiff, distance) * (double)(180F / (float)Math.PI)));
    }

    /**
     * From {@link //LookController#getTargetYaw()}
	 * @param target
	 * @param entity
     * @return
     */
	public static float getTargetYaw(Entity entity, Entity target) {
        double d0 = target.getX() - entity.getX();
        double d1 = target.getZ() - entity.getZ();
        return (float)-(Mth.atan2(d1, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
    }
	
	public static Shotlock getPlayerShotlock(Player player) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		return ModShotlocks.registry.get().getValue(new ResourceLocation(playerData.getEquippedShotlock()));
	}

	public static boolean isPlayerLowHP(Player player) {
		return player.getHealth() < player.getMaxHealth() / 4;	
	}

	
	public static void syncWorldData(Level world, IWorldCapabilities worldData) {
		world.getServer().getAllLevels().forEach(sw -> {
			CompoundTag nbt = new CompoundTag();
			ModCapabilities.getWorld(sw).read(worldData.write(nbt));
		});
		PacketHandler.sendToAllPlayers(new SCSyncWorldCapability(worldData));
	}

	//Gets items excluding AIR
	public static Map<Integer, ItemStack> getEquippedItems(Map<Integer, ItemStack> equippedItems) {
		Map<Integer, ItemStack> finalMap = new HashMap<Integer, ItemStack>(equippedItems);
		for(Entry<Integer, ItemStack> entry : equippedItems.entrySet()) {
			ItemStack stack = entry.getValue();
			if(ItemStack.matches(stack, ItemStack.EMPTY)){
				finalMap.remove(entry.getKey());
			}
		}
		
		return finalMap;
	}

	/*public static IPlayerCapabilities chooseAutoForm(PlayerEntity player) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		
		List<String> reactionCommands = playerData.getReactionCommands();
		//Get if u already have an auto reaction command
		
		boolean hasAlready = false;
		for(int i=0;i<reactionCommands.size();i++) {
			ReactionCommand command = ModReactionCommands.registry.getValue(new ResourceLocation(reactionCommands.get(i)));
			if(command instanceof ReactionAutoForm) {
				hasAlready = true;
				break;
			}
		}
		
		if(!hasAlready) {
			playerData.addReactionCommand(KingdomKeys.MODID+":"+Strings.autoFinalRC, player);
			
			playerData.addReactionCommand(KingdomKeys.MODID+":"+Strings.autoMasterRC, player);
			playerData.addReactionCommand(KingdomKeys.MODID+":"+Strings.autoLimitRC, player);
			
			playerData.addReactionCommand(KingdomKeys.MODID+":"+Strings.autoWisdomRC, player);
			playerData.addReactionCommand(KingdomKeys.MODID+":"+Strings.autoValorRC, player);

		}

		return playerData;
	}*/

	public static boolean isEntityInParty(Party party, Entity e) {
		if(party == null)
			return false;
		List<Member> list = party.getMembers();
		for(Member m : list) {
			if(m.getUUID().equals(e.getUUID())) {
				return true;
			}
		}
		return false;
	}

	public static List<Entity> removeFriendlyEntities(List<Entity> list) {
		List<Entity> list2 = new ArrayList<Entity>();
		for(Entity e : list) {
			if(e instanceof Monster || e instanceof Player) {
				list2.add((LivingEntity)e);
			}
		}
		return list2;
	}

	public static boolean isHostile(Entity e) {
		return e instanceof Monster || e instanceof Player;
	}

	public static List<String> getKeybladeAbilitiesAtLevel(Item item, int level) {
		ArrayList<String> abilities = new ArrayList<String>();
		KeybladeItem keyblade = null;
		if(item instanceof IKeychain) {
			keyblade = ((IKeychain) item).toSummon();
		} else if(item instanceof KeybladeItem) {
			keyblade = ((KeybladeItem) item);
		}
		
		if(keyblade != null) {
			for (int i = 0; i <= level; i++) {
				String a = keyblade.data.getLevelAbility(i);
				if(a != null) {
					abilities.add(a);
				}
			}
		}
		return abilities;
	}

	public static void restartLevel(IPlayerCapabilities playerData, Player player) { //sets player level to base
		playerData.setLevel(1);
		playerData.setExperience(0);
		playerData.setMaxHP(20);
        player.setHealth(playerData.getMaxHP());
		player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(playerData.getMaxHP());
        playerData.setMaxMP(0);
        playerData.setMP(playerData.getMaxMP());
        playerData.setMaxAP(10);
        
        playerData.setStrength(1);
        playerData.setMagic(1);
        playerData.setDefense(1);
		SoAState.applyStatsForChoices(playerData, false);

		playerData.setEquippedShotlock("");
		playerData.getShotlockList().clear();
		
		playerData.clearAbilities();
        playerData.addAbility(Strings.zeroExp, false);
	}
	
	public static void restartLevel2(IPlayerCapabilities playerData, Player player) { //calculates drive forms
		LinkedHashMap<String, int[]> driveForms = playerData.getDriveFormMap();
		Iterator<Entry<String, int[]>> it = driveForms.entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, int[]> entry = it.next();
			int dfLevel = entry.getValue()[0];
			DriveForm form = ModDriveForms.registry.get().getValue(new ResourceLocation(entry.getKey()));
			if(!form.getRegistryName().equals(DriveForm.NONE) && !form.getRegistryName().equals(DriveForm.SYNCH_BLADE)) {
				for(int i=1;i<=dfLevel;i++) {
					String baseAbility = form.getBaseAbilityForLevel(i);
			     	if(baseAbility != null && !baseAbility.equals("")) {
			     		playerData.addAbility(baseAbility, false);
			     	}
				}
			}
		}
		
		player.heal(playerData.getMaxHP());
		playerData.setMP(playerData.getMaxMP());
	}

	public static String getTierFromInt(int tier) {
		return switch(tier) {
		case 1 -> "D";
		case 2 -> "C";
		case 3 -> "B";
		case 4 -> "A";
		case 5 -> "S";
		case 6 -> "SS";
		case 7 -> "SSS";
		default -> "Unknown: "+tier;
		};
	}

	public static int getFreeSlotsForPlayer(Player player) {
		int free = 0;
		for (ItemStack stack : player.getInventory().items) {
			if (ItemStack.matches(ItemStack.EMPTY, stack)) {
				free++;
			}
		}
		return free;
	}

	public static int stacksForItemAmount(ItemStack item, int amount) {
		return (int) Math.round(Math.ceil((double)amount / (double)item.getMaxStackSize()));
	}

	public static int getLootingLevel(Player player) {
		int lvl = 0;
		if(!ItemStack.isSame(player.getMainHandItem(),ItemStack.EMPTY) && player.getMainHandItem().isEnchanted()){
            lvl += EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MOB_LOOTING, player.getMainHandItem());
		}
		if(!ItemStack.isSame(player.getOffhandItem(),ItemStack.EMPTY) && player.getOffhandItem().isEnchanted()){
            lvl += EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MOB_LOOTING, player.getOffhandItem());
		}
		lvl += ModCapabilities.getPlayer(player).getNumberOfAbilitiesEquipped(Strings.luckyLucky);
		return lvl;
	}
	
	/*public void attackTargetEntityWithHandItem(PlayerEntity player, Entity targetEntity, Hand hand) {
	      if (!net.minecraftforge.common.ForgeHooks.onPlayerAttackTarget(player, targetEntity)) return;
	      if (targetEntity.canBeAttackedWithItem()) {
	         if (!targetEntity.hitByEntity(player)) {
	            float f = (float)player.getAttributeValue(Attributes.ATTACK_DAMAGE);
	            float f1;
	            if (targetEntity instanceof LivingEntity) {
	               f1 = EnchantmentHelper.getModifierForCreature(player.getHeldItem(hand), ((LivingEntity)targetEntity).getCreatureAttribute());
	            } else {
	               f1 = EnchantmentHelper.getModifierForCreature(player.getHeldItem(hand), CreatureAttribute.UNDEFINED);
	            }

	            float f2 = player.getCooledAttackStrength(0.5F);
	            f = f * (0.2F + f2 * f2 * 0.8F);
	            f1 = f1 * f2;
	            player.resetCooldown();
	            if (f > 0.0F || f1 > 0.0F) {
	               boolean flag = f2 > 0.9F;
	               boolean flag1 = false;
	               int i = 0;
	               i = i + EnchantmentHelper.getKnockbackModifier(player);
	               if (player.isSprinting() && flag) {
	                  player.world.playSound((PlayerEntity)null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, player.getSoundCategory(), 1.0F, 1.0F);
	                  ++i;
	                  flag1 = true;
	               }

	               boolean flag2 = flag && player.fallDistance > 0.0F && !player.isOnGround() && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(Effects.BLINDNESS) && !player.isPassenger() && targetEntity instanceof LivingEntity;
	               flag2 = flag2 && !player.isSprinting();
	               net.minecraftforge.event.entity.player.CriticalHitEvent hitResult = net.minecraftforge.common.ForgeHooks.getCriticalHit(player, targetEntity, flag2, flag2 ? 1.5F : 1.0F);
	               flag2 = hitResult != null;
	               if (flag2) {
	                  f *= hitResult.getDamageModifier();
	               }

	               f = f + f1;
	               boolean flag3 = false;
	               double d0 = (double)(player.distanceWalkedModified - player.prevDistanceWalkedModified);
	               if (flag && !flag2 && !flag1 && player.isOnGround() && d0 < (double)player.getAIMoveSpeed()) {
	                  ItemStack itemstack = player.getHeldItem(hand);
	                  if (itemstack.getItem() instanceof SwordItem) {
	                     flag3 = true;
	                  }
	               }

	               float f4 = 0.0F;
	               boolean flag4 = false;
	               int j = EnchantmentHelper.getFireAspectModifier(player);
	               if (targetEntity instanceof LivingEntity) {
	                  f4 = ((LivingEntity)targetEntity).getHealth();
	                  if (j > 0 && !targetEntity.isBurning()) {
	                     flag4 = true;
	                     targetEntity.setFire(1);
	                  }
	               }

	               Vector3d vector3d = targetEntity.getMotion();
	               boolean flag5 = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(player), f);
	               if (flag5) {
	                  if (i > 0) {
	                     if (targetEntity instanceof LivingEntity) {
	                        ((LivingEntity)targetEntity).applyKnockback((float)i * 0.5F, (double)MathHelper.sin(player.rotationYaw * ((float)Math.PI / 180F)), (double)(-MathHelper.cos(player.rotationYaw * ((float)Math.PI / 180F))));
	                     } else {
	                        targetEntity.addVelocity((double)(-MathHelper.sin(player.rotationYaw * ((float)Math.PI / 180F)) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(player.rotationYaw * ((float)Math.PI / 180F)) * (float)i * 0.5F));
	                     }

	                     player.setMotion(player.getMotion().mul(0.6D, 1.0D, 0.6D));
	                     player.setSprinting(false);
	                  }

	                  if (flag3) {
	                     float f3 = 1.0F + EnchantmentHelper.getSweepingDamageRatio(player) * f;

	                     for(LivingEntity livingentity : player.world.getEntitiesWithinAABB(LivingEntity.class, targetEntity.getBoundingBox().grow(1.0D, 0.25D, 1.0D))) {
	                        if (livingentity != player && livingentity != targetEntity && !player.isOnSameTeam(livingentity) && (!(livingentity instanceof ArmorStandEntity) || !((ArmorStandEntity)livingentity).hasMarker()) && player.getDistanceSq(livingentity) < 9.0D) {
	                           livingentity.applyKnockback(0.4F, (double)MathHelper.sin(player.rotationYaw * ((float)Math.PI / 180F)), (double)(-MathHelper.cos(player.rotationYaw * ((float)Math.PI / 180F))));
	                           livingentity.attackEntityFrom(DamageSource.causePlayerDamage(player), f3);
	                        }
	                     }

	                     player.world.playSound((PlayerEntity)null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
	                     player.spawnSweepParticles();
	                  }

	                  if (targetEntity instanceof ServerPlayerEntity && targetEntity.velocityChanged) {
	                     ((ServerPlayerEntity)targetEntity).connection.sendPacket(new SEntityVelocityPacket(targetEntity));
	                     targetEntity.velocityChanged = false;
	                     targetEntity.setMotion(vector3d);
	                  }

	                  if (flag2) {
	                     player.world.playSound((PlayerEntity)null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, player.getSoundCategory(), 1.0F, 1.0F);
	                     player.onCriticalHit(targetEntity);
	                  }

	                  if (!flag2 && !flag3) {
	                     if (flag) {
	                        player.world.playSound((PlayerEntity)null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, player.getSoundCategory(), 1.0F, 1.0F);
	                     } else {
	                        player.world.playSound((PlayerEntity)null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, player.getSoundCategory(), 1.0F, 1.0F);
	                     }
	                  }

	                  if (f1 > 0.0F) {
	                     player.onEnchantmentCritical(targetEntity);
	                  }

	                  player.setLastAttackedEntity(targetEntity);
	                  if (targetEntity instanceof LivingEntity) {
	                     EnchantmentHelper.applyThornEnchantments((LivingEntity)targetEntity, player);
	                  }

	                  EnchantmentHelper.applyArthropodEnchantments(player, targetEntity);
	                  ItemStack itemstack1 = player.getHeldItem(hand);
	                  Entity entity = targetEntity;
	                  if (targetEntity instanceof net.minecraftforge.entity.PartEntity) {
	                     entity = ((net.minecraftforge.entity.PartEntity<?>) targetEntity).getParent();
	                  }

	                  if (!player.world.isRemote && !itemstack1.isEmpty() && entity instanceof LivingEntity) {
	                     ItemStack copy = itemstack1.copy();
	                     itemstack1.hitEntity((LivingEntity)entity, player);
	                     if (itemstack1.isEmpty()) {
	                        net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, copy, Hand.MAIN_HAND);
	                        player.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
	                     }
	                  }

	                  if (targetEntity instanceof LivingEntity) {
	                     float f5 = f4 - ((LivingEntity)targetEntity).getHealth();
	                     player.addStat(Stats.DAMAGE_DEALT, Math.round(f5 * 10.0F));
	                     if (j > 0) {
	                        targetEntity.setFire(j * 4);
	                     }

	                     if (player.world instanceof ServerWorld && f5 > 2.0F) {
	                        int k = (int)((double)f5 * 0.5D);
	                        ((ServerWorld)player.world).spawnParticle(ParticleTypes.DAMAGE_INDICATOR, targetEntity.getPosX(), targetEntity.getPosYHeight(0.5D), targetEntity.getPosZ(), k, 0.1D, 0.0D, 0.1D, 0.2D);
	                     }
	                  }

	                  player.addExhaustion(0.1F);
	               } else {
	                  player.world.playSound((PlayerEntity)null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, player.getSoundCategory(), 1.0F, 1.0F);
	                  if (flag4) {
	                     targetEntity.extinguish();
	                  }
	               }
	            }

	         }
	      }
	   }*/
	
}
