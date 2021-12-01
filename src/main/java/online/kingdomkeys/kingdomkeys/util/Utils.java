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

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategoryRegistry;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.item.organization.OrgWeaponItem;
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

/**
 * Created by Toby on 19/07/2016.
 */
public class Utils {

	public static class ModelAnimation {
		public ModelRenderer model;
		public ModelRenderer modelCounterpart;
		public float defVal;
		public float minVal;
		public float maxVal;
		public float actVal;
		public boolean increasing;

		public ModelAnimation(ModelRenderer model, float defVal, float minVal, float maxVal, float actVal, boolean increasing, @Nullable ModelRenderer counterpart) {
			this.model = model;
			this.defVal = defVal;
			this.minVal = minVal;
			this.maxVal = maxVal;
			this.actVal = actVal;
			this.increasing = increasing;
			this.modelCounterpart = counterpart;
		}

		@Override
		public String toString() {
			return defVal + ": " + actVal + " " + increasing;
		}
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
	 * {@link net.minecraft.client.resources.I18n#format(String, Object...)}
	 * 
	 * @param name   the unlocalized string to translate
	 * @param format the format of the string
	 * @return the translated string
	 */
	public static String translateToLocalFormatted(String name, Object... format) {
		TranslationTextComponent translation = new TranslationTextComponent(name, format);
		return translation.getString();
	}

	/**
	 * Replacement for {@link }
	 * 
	 * @param name the unlocalized string to translate
	 * @return the translated string
	 */
	public static String translateToLocal(String name, Object... args) {
		TranslationTextComponent translation = new TranslationTextComponent(name, args);
		return translation.getString();
	}

	/**
	 * Get the ItemStack of the item that made the DamageSource
	 * 
	 * @param damageSource
	 * @param player
	 * @return
	 */
	public static ItemStack getWeaponDamageStack(DamageSource damageSource, PlayerEntity player) {
		switch (damageSource.damageType) {
		case "player":
			if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof KeybladeItem || player.getHeldItemMainhand().getItem() instanceof IOrgWeapon) {
				return player.getHeldItemMainhand();
			}
			break;
		case "keybladeOffhand":
			if (player.getHeldItemOffhand() != null && player.getHeldItemOffhand().getItem() instanceof KeybladeItem || player.getHeldItemOffhand().getItem() instanceof IOrgWeapon) {
				return player.getHeldItemOffhand();
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
			list.add(ModAbilities.registry.getValue(new ResourceLocation(entry)));
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
			list.add(ModDriveForms.registry.getValue(new ResourceLocation(entry)));
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
			list.add(ModMagic.registry.getValue(new ResourceLocation(entry)));
		}

		Collections.sort(list, Comparator.comparingInt(Magic::getOrder));

		LinkedHashMap<String, int[]> map = new LinkedHashMap<>();
		for (int i = 0; i < list.size(); i++) {
			map.put(list.get(i).getRegistryName().toString(), magicsMap.get(list.get(i).getRegistryName().toString()));
		}

		return map;
	}

	public static List<String> getSortedShotlocks(List<String> list) {
		Collections.sort(list, (Comparator.comparingInt(a -> ModShotlocks.registry.getValue(new ResourceLocation(a)).getOrder())));
		return list;
	}
	
	public static PlayerEntity getPlayerByName(World world, String name) {
		for (PlayerEntity p : world.getPlayers()) {
			if (p.getDisplayName().getString().equals(name)) {
				return p;
			}
		}
		return null;
	}

	public static List<PlayerEntity> getAllPlayers(MinecraftServer ms) {
		List<PlayerEntity> list = new ArrayList<PlayerEntity>();
		java.util.Iterator<ServerWorld> it = ms.getWorlds().iterator();
		while (it.hasNext()) {
			ServerWorld world = it.next();
			for (PlayerEntity p : world.getPlayers()) {
				list.add(p);
			}
		}
		return list;
	}

	public static List<LivingEntity> getLivingEntitiesInRadius(Entity entity, float radius) {
		List<Entity> list = entity.world.getEntitiesInAABBexcluding(entity, entity.getBoundingBox().grow(radius), Entity::isAlive);
		List<LivingEntity> elList = new ArrayList<LivingEntity>();
		for (Entity e : list) {
			if (e instanceof LivingEntity) {
				elList.add((LivingEntity) e);
			}
		}

		return elList;
	}

	public static List<LivingEntity> getLivingEntitiesInRadiusExcludingParty(PlayerEntity player, float radius) {
		List<Entity> list = player.world.getEntitiesInAABBexcluding(player, player.getBoundingBox().grow(radius), Entity::isAlive);
		Party casterParty = ModCapabilities.getWorld(player.world).getPartyFromMember(player.getUniqueID());

		if (casterParty != null && !casterParty.getFriendlyFire()) {
			for (Member m : casterParty.getMembers()) {
				list.remove(player.world.getPlayerByUuid(m.getUUID()));
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

	public static String getResourceName(String text) {
		return text.replaceAll("[ \\t]+$", "").replaceAll("\\s+", "_").replaceAll("[\\'\\:\\-\\,\\#]", "").replaceAll("\\&", "and").toLowerCase();
	}

	@OnlyIn(Dist.CLIENT)
	public static void blitScaled(MatrixStack matrixStack, AbstractGui gui, float x, float y, int u, int v, int width, int height, float scaleX, float scaleY) {
		matrixStack.push();
		matrixStack.translate(x, y, 0);
		matrixStack.scale(scaleX, scaleY, 1);
		gui.blit(matrixStack, 0, 0, u, v, width, height);
		matrixStack.pop();
	}

	@OnlyIn(Dist.CLIENT)
	public static void blitScaled(MatrixStack matrixStack, AbstractGui gui, float x, float y, int u, int v, int width, int height, float scaleXY) {
		blitScaled(matrixStack, gui, x, y, u, v, width, height, scaleXY, scaleXY);
	}

	@OnlyIn(Dist.CLIENT)
	public static void drawStringScaled(MatrixStack matrixStack, AbstractGui gui, float x, float y, String text, int colour, float scaleX, float scaleY) {
		matrixStack.push();
		matrixStack.translate(x, y, 0);
		matrixStack.scale(scaleX, scaleY, 1);
		gui.drawString(matrixStack, Minecraft.getInstance().fontRenderer, text, 0, 0, colour);
		matrixStack.pop();
	}

	@OnlyIn(Dist.CLIENT)
	public static void drawStringScaled(MatrixStack matrixStack, AbstractGui gui, float x, float y, String text, int colour, float scaleXY) {
		drawStringScaled(matrixStack, gui, x, y, text, colour, scaleXY, scaleXY);
	}

	public static boolean hasID(ItemStack stack) {
		if (stack.getItem() instanceof KeybladeItem || stack.getItem() instanceof IKeychain) {
			if (stack.getTag() != null) {
				if (stack.getTag().hasUniqueId("keybladeID")) {
					return true;
				}
			}
		}
		return false;
	}

	public static UUID getID(ItemStack stack) {
		if (hasID(stack)) {
			return stack.getTag().getUniqueId("keybladeID");
		}
		return null;
	}

	// Returns the inv slot if summoned keychain is found
	public static int findSummoned(PlayerInventory inv, ItemStack chain, boolean orgWeapon) {
		if (!ItemStack.areItemStacksEqual(chain, ItemStack.EMPTY)) {
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				ItemStack slotStack = inv.getStackInSlot(i);
				// Only check the tag for keyblades
				if (slotStack.getItem() instanceof KeybladeItem) {
					if (orgWeapon) {
						return i;
					}
					// Make sure it has a tag
					if (hasID(slotStack)) {
						// Compare the ID with the chain's
						if (slotStack.getTag().getUniqueId("keybladeID").equals(chain.getTag().getUniqueId("keybladeID"))) {
							return i;
						}
					}
				} else if (slotStack.getItem() instanceof OrgWeaponItem) {
					return i;
				}
			}
		}
		return -1;
	}

	public static void swapStack(PlayerInventory inv, int stack1, int stack2) {
		ItemStack tempStack = inv.getStackInSlot(stack2);
		inv.setInventorySlotContents(stack2, inv.getStackInSlot(stack1));
		inv.setInventorySlotContents(stack1, tempStack);
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

	public static int getConsumedAP(IPlayerCapabilities playerData) {
		int ap = 0;
		LinkedHashMap<String, int[]> map = playerData.getAbilityMap();
		Iterator<Entry<String, int[]>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, int[]> entry = it.next();
			Ability a = ModAbilities.registry.getValue(new ResourceLocation(entry.getKey()));
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

	public static boolean isWearingOrgRobes(PlayerEntity player) {
		boolean wearingOrgCloak = true;
		for (int i = 0; i < player.inventory.armorInventory.size(); ++i) {
			ItemStack itemStack = player.inventory.armorInventory.get(i);
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

	public static void drawSplitString(FontRenderer fontRenderer, String text, int x, int y, int len, int color) {
		fontRenderer.func_238418_a_(ITextProperties.func_240652_a_(text), x, y, len, color);
	}
	public static int getSlotFor(PlayerInventory inv, ItemStack stack) {
		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			if (!inv.getStackInSlot(i).isEmpty() && ItemStack.areItemStacksEqual(stack, inv.getStackInSlot(i))) {
				return i;
			}
		}

		return -1;
	}
	
	 /**
     * From {@link LookController#getTargetPitch()}
     * @param target
     * @param entity
     * @return
     */
	public static float getTargetPitch(Entity entity, Entity target) {
        double xDiff = target.getPosX() - entity.getPosX();
        double yDiff = target.getPosY() - entity.getPosY();
        double zDiff = target.getPosZ() - entity.getPosZ();
        double distance = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff);
        return (float) (-(MathHelper.atan2(yDiff, distance) * (double)(180F / (float)Math.PI)));
    }

    /**
     * From {@link LookController#getTargetYaw()}
     * @param target
     * @param playerEntity
     * @return
     */
	public static float getTargetYaw(Entity entity, Entity target) {
        double d0 = target.getPosX() - entity.getPosX();
        double d1 = target.getPosZ() - entity.getPosZ();
        return (float)-(MathHelper.atan2(d1, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
    }

	public static List<Limit> getPlayerLimitAttacks(PlayerEntity player) {
//		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		List<Limit> limits = new ArrayList<Limit>();
		limits.addAll(ModLimits.registry.getValues());
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
	
	public static Shotlock getPlayerShotlock(PlayerEntity player) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		return ModShotlocks.registry.getValue(new ResourceLocation(playerData.getEquippedShotlock()));
	}

	public static boolean isPlayerLowHP(PlayerEntity player) {
		return player.getHealth() < player.getMaxHealth() / 4;	
	}

	
	public static void syncWorldData(World world, IWorldCapabilities worldData) {
		world.getServer().getWorlds().forEach(sw -> {
			CompoundNBT nbt = new CompoundNBT();
			ModCapabilities.getWorld(sw).read(worldData.write(nbt));
		});
		PacketHandler.sendToAllPlayers(new SCSyncWorldCapability(worldData));
	}

	//Gets items excluding AIR
	public static Map<Integer, ItemStack> getEquippedItems(Map<Integer, ItemStack> equippedItems) {
		Map<Integer, ItemStack> finalMap = new HashMap<Integer, ItemStack>(equippedItems);
		for(Entry<Integer, ItemStack> entry : equippedItems.entrySet()) {
			ItemStack stack = entry.getValue();
			if(ItemStack.areItemStacksEqual(stack, ItemStack.EMPTY)){
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
		List<Member> list = party.getMembers();
		for(Member m : list) {
			if(m.getUUID().equals(e.getUniqueID())) {
				return true;
			}
		}
		return false;
	}

	public static List<Entity> removeFriendlyEntities(List<Entity> list) {
		List<Entity> list2 = new ArrayList<Entity>();
		for(Entity e : list) {
			if(e instanceof MonsterEntity || e instanceof PlayerEntity) {
				list2.add((LivingEntity)e);
			}
		}
		return list2;
	}

	public static boolean isHostile(Entity e) {
		return e instanceof MonsterEntity || e instanceof PlayerEntity;
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
				//System.out.println(keyblade.getName().getString()+" "+ i);
				String a = keyblade.data.getLevelAbility(i);
				if(a != null)
					abilities.add(a);
			}
		}
		return abilities;
	}

	public static void restartLevel(IPlayerCapabilities playerData, PlayerEntity player) {
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
		SoAState.applyStatsForChoices(playerData);

		playerData.setEquippedShotlock("");
		playerData.getShotlockList().clear();
		
		playerData.clearAbilities();
        playerData.addAbility(Strings.zeroExp, false);
	}
	
	public static void restartLevel2(IPlayerCapabilities playerData, PlayerEntity player) {
		LinkedHashMap<String, int[]> driveForms = playerData.getDriveFormMap();
		Iterator<Entry<String, int[]>> it = driveForms.entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, int[]> entry = it.next();
			int dfLevel = entry.getValue()[0];
			DriveForm form = ModDriveForms.registry.getValue(new ResourceLocation(entry.getKey()));
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
