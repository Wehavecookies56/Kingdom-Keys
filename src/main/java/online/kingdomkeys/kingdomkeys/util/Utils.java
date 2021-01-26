package online.kingdomkeys.kingdomkeys.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
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
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.organization.OrgWeaponItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
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
			return defVal+": "+actVal+" "+increasing;
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
		return translation.getFormattedText();
	}

	/**
	 * Replacement for {@link }
	 * 
	 * @param name the unlocalized string to translate
	 * @return the translated string
	 */
	public static String translateToLocal(String name) {
		TranslationTextComponent translation = new TranslationTextComponent(name);
		return translation.getFormattedText();
	}
	
	/**
	 * Get the ItemStack of the item that made the DamageSource
	 * @param damageSource
	 * @param player
	 * @return
	 */
	public static ItemStack getKeybladeDamageStack(DamageSource damageSource, PlayerEntity player) {
		switch (damageSource.damageType) {
		case "player":
			if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof KeybladeItem) {
				return player.getHeldItemMainhand();
			}
			break;
		case "keybladeOffhand":
			if (player.getHeldItemOffhand() != null && player.getHeldItemOffhand().getItem() instanceof KeybladeItem) {
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
		// System.out.println("UNSORTED: "+driveFormsMap);
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

		// System.out.println("SORTED: "+map);

		// Old way
		/*
		 * String[] keys = new String[driveFormsMap.size()]; int[][] values = new
		 * int[driveFormsMap.size()][2]; Iterator<Entry<String, int[]>> it =
		 * driveFormsMap.entrySet().iterator(); while(it.hasNext()) { Entry<String,
		 * int[]> entry = it.next(); int order = ModDriveForms.registry.getValue(new
		 * ResourceLocation(entry.getKey())).getOrder(); keys[order] = entry.getKey();
		 * values[order] = entry.getValue(); }
		 * 
		 * LinkedHashMap<String, int[]> map = new LinkedHashMap<String, int[]>();
		 * for(int i=0;i<keys.length;i++) { map.put(keys[i], values[i]); }
		 */

		return map;
	}

	public static List<String> getSortedMagics(List<String> list) {
		Collections.sort(list, (Comparator.comparingInt(a -> ModMagic.registry.getValue(new ResourceLocation(a)).getOrder())));
		return list;

		/*
		 * String[] keys = new String[list.size()]; int[][] values = new
		 * int[list.size()][2]; Iterator<String> it = list.iterator();
		 * while(it.hasNext()) { String entry = it.next(); int order =
		 * ModMagics.registry.getValue(new ResourceLocation(entry)).getOrder();
		 * keys[order] = entry.getKey(); values[order] = entry.getValue(); }
		 * 
		 * LinkedHashMap<String, int[]> map = new LinkedHashMap<String, int[]>();
		 * for(int i=0;i<keys.length;i++) { map.put(keys[i], values[i]); }
		 * 
		 * return map;
		 */
	}

	public static PlayerEntity getPlayerByName(World world, String name) {
		for (PlayerEntity p : world.getPlayers()) {
			if (p.getDisplayName().getFormattedText().equals(name)) {
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

	public static String getResourceName(String text) {
		return text.replaceAll("[ \\t]+$", "").replaceAll("\\s+", "_").replaceAll("[\\'\\:\\-\\,\\#]", "").replaceAll("\\&", "and").toLowerCase();
	}

	@OnlyIn(Dist.CLIENT)
	public static void blitScaled(AbstractGui gui, float x, float y, int u, int v, int width, int height, float scaleX, float scaleY) {
		RenderSystem.pushMatrix();
		RenderSystem.translatef(x, y, 0);
		RenderSystem.scalef(scaleX, scaleY, 1);
		gui.blit(0, 0, u, v, width, height);
		RenderSystem.popMatrix();
	}

	@OnlyIn(Dist.CLIENT)
	public static void blitScaled(AbstractGui gui, float x, float y, int u, int v, int width, int height, float scaleXY) {
		blitScaled(gui, x, y, u, v, width, height, scaleXY, scaleXY);
	}

	@OnlyIn(Dist.CLIENT)
	public static void drawStringScaled(AbstractGui gui, float x, float y, String text, int colour, float scaleX, float scaleY) {
		RenderSystem.pushMatrix();
		RenderSystem.translatef(x, y, 0);
		RenderSystem.scalef(scaleX, scaleY, 1);
		gui.drawString(Minecraft.getInstance().fontRenderer, text, 0, 0, colour);
		RenderSystem.popMatrix();
	}

	@OnlyIn(Dist.CLIENT)
	public static void drawStringScaled(AbstractGui gui, float x, float y, String text, int colour, float scaleXY) {
		drawStringScaled(gui, x, y, text, colour, scaleXY, scaleXY);
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
				}
				else if (slotStack.getItem() instanceof OrgWeaponItem) {
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

	// Returns the category for the stack from the IItemCategory interface, the
	// registry, else it returns MISC
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
			if (entry.getValue()[1] > 0) {
				ap += a.getAPCost();
			}
		}
		return ap;
	}

	public static double getMPHasteValue(IPlayerCapabilities playerData) {
		int val = 0;
		LinkedHashMap<String, int[]> map = playerData.getAbilityMap();
		Iterator<Entry<String, int[]>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, int[]> entry = it.next();
			if (playerData.isAbilityEquipped(entry.getKey())) {
				switch (entry.getKey()) {
				case Strings.mpHaste:
					val += 2;
					break;
				case Strings.mpHastera:
					val += 4;
					break;
				}
			}

		}
		return val;
	}
	
	public static boolean isWearingOrgRobes(PlayerEntity player) {
		boolean wearingOrgCloak = true;
		for (int i = 0; i < player.inventory.armorInventory.size(); ++i) {
			ItemStack itemStack = player.inventory.armorInventory.get(i);
			if (itemStack.isEmpty() || !itemStack.getItem().getRegistryName().getPath().startsWith("organization_")) {
				wearingOrgCloak = false;
				break;
			}
		}
		return wearingOrgCloak;
	}

	public static int getBagCosts(int bagLevel) {
		switch(bagLevel) {
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

		// Run a loop till string
		// string contains underscore
		while (str.contains("_")) {

			// Replace the first occurrence
			// of letter that present after
			// the underscore, to capitalize
			// form of next letter of underscore
			str = str.replaceFirst("_[a-z]", String.valueOf(Character.toUpperCase(str.charAt(str.indexOf("_") + 1))));
		}
		str = str.substring(0,1).toLowerCase()+str.substring(1);
		// Return string
		return str;
	}

	public static boolean isNumber(char c) {
		try {
			Integer.parseInt(String.valueOf(c));
			return true;
		} catch (NumberFormatException e) {
			return false;
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

}
