package online.kingdomkeys.kingdomkeys.data;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.util.INBTSerializable;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.Ability.AbilityType;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.advancements.ModAdvancements;
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.DualChoices;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.SingleChoices;
import online.kingdomkeys.kingdomkeys.item.*;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.leveling.ModLevels;
import online.kingdomkeys.kingdomkeys.leveling.Stat;
import online.kingdomkeys.kingdomkeys.lib.*;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowOverlayPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.Recipe;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;
import online.kingdomkeys.kingdomkeys.util.Utils.castMagic;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.*;
import java.util.Map.Entry;

public class PlayerData implements INBTSerializable<CompoundTag> {
	public static final int DATA_VERSION = 1; //CHANGE THIS TO FORCE ALL PLAYERS TO GET THEIR STATS FIXED

	protected PlayerData() {}

	public static PlayerData get(CompoundTag nbt, Player player) {
		//Only deserialize on client, there shouldn't be a reason to do this on the server
		if (FMLEnvironment.dist.isClient()) {
			PlayerData data = new PlayerData();
			data.deserializeNBT(player.level().registryAccess(), nbt);
			if (player != null) {
				player.setData(ModData.PLAYER_DATA, data);
			}
			return data;
		} else {
			return get(player);
		}
	}

	public static PlayerData fromNBT(RegistryAccess registryAccess, CompoundTag nbt) {
		PlayerData data = new PlayerData();
		data.deserializeNBT(registryAccess, nbt);
		return data;
	}

    @Nullable
	public static PlayerData get(Player player) {
		if (!player.hasData(ModData.PLAYER_DATA)) {
			player.setData(ModData.PLAYER_DATA, new PlayerData());
		}
		return player.getData(ModData.PLAYER_DATA);
	}

    @Override
	public CompoundTag serializeNBT(HolderLookup.Provider provider) {
		CompoundTag storage = new CompoundTag();
		storage.putInt("data_ver", this.getVer());
		storage.putInt("level", this.getLevel());
		storage.putInt("experience", this.getExperience());
		storage.putInt("experience_given", this.getExperienceGiven());
		strength.serialize(storage);
		magic.serialize(storage);
		defense.serialize(storage);
		maxAP.serialize(storage);
		storage.putInt("max_hp", this.getMaxHP());
		storage.putDouble("mp", this.getMP());
		storage.putDouble("max_mp", this.getMaxMP());
		storage.putDouble("focus", this.getFocus());
		storage.putDouble("max_focus", this.getMaxFocus());
		storage.putBoolean("recharge", this.getRecharge());
		storage.putDouble("dp", this.getDP());
		storage.putDouble("max_dp", this.getMaxDP());
		storage.putDouble("fp", this.getFP());
		storage.putString("drive_form", this.getActiveDriveForm().toString());
		storage.putInt("anti_points", this.getAntiPoints());
		storage.putInt("reflect_ticks", this.getReflectTicks());
		storage.putInt("reflect_level", this.getReflectLevel());
		storage.putBoolean("reflect_active", this.getReflectActive());
		storage.putInt("munny", this.getMunny());
		storage.putByte("soa_state", this.getSoAState().get());
		storage.putByte("soa_choice", this.getChosen().get());
		storage.putByte("soa_sacrifice", this.getSacrificed().get());
		CompoundTag returnCompound = new CompoundTag();
		Vec3 pos = this.getReturnLocation();
		returnCompound.putDouble("x", pos.x);
		returnCompound.putDouble("y", pos.y);
		returnCompound.putDouble("z", pos.z);
		storage.put("soa_return_pos", returnCompound);
		storage.putString("soa_return_dim", this.getReturnDimension().location().toString());
		CompoundTag choicePedestalCompound = new CompoundTag();
		BlockPos choicePos = this.getChoicePedestal();
		choicePedestalCompound.putInt("x", choicePos.getX());
		choicePedestalCompound.putInt("y", choicePos.getY());
		choicePedestalCompound.putInt("z", choicePos.getZ());
		storage.put("soa_choice_pedestal", choicePedestalCompound);
		CompoundTag sacrificePedestalCompound = new CompoundTag();
		BlockPos sacrificePos = this.getSacrificePedestal();
		sacrificePedestalCompound.putInt("x", sacrificePos.getX());
		sacrificePedestalCompound.putInt("y", sacrificePos.getY());
		sacrificePedestalCompound.putInt("z", sacrificePos.getZ());
		storage.put("soa_sacrifice_pedestal", sacrificePedestalCompound);

		CompoundTag recipes = new CompoundTag();
		for (ResourceLocation recipe : this.getKnownRecipeList()) {
			recipes.putString(recipe.toString(), recipe.toString());
		}
		storage.put("recipes", recipes);

		CompoundTag magics = new CompoundTag();
		for (Entry<ResourceLocation, Integer> pair : this.getMagicsCastMap().entrySet()) {
			magics.putInt(pair.getKey().toString(), pair.getValue());
		}
		storage.put("magic_casts", magics);

		storage.put("equipped_shotlock", this.getEquippedShotlock().saveOptional(provider));

        CompoundTag targetShotlocks = new CompoundTag();
        for (int i=0; i< this.getShotlockEnemies().size(); i++) {
            Utils.ShotlockPosition shotlock = getShotlockEnemies().get(i);
            CompoundTag tag1 = new CompoundTag();
            tag1.putInt("id", shotlock.id());
            tag1.putFloat("x", shotlock.x());
            tag1.putFloat("y", shotlock.y());
            tag1.putFloat("z", shotlock.z());
            targetShotlocks.put("target"+i, tag1);
        }//TODO a
        storage.put("target_shotlocks", targetShotlocks);

		CompoundTag forms = new CompoundTag();
		for (Entry<ResourceLocation, int[]> pair : this.getDriveFormMap().entrySet()) {
			forms.putIntArray(pair.getKey().toString(), pair.getValue());
		}
		storage.put("drive_forms", forms);

		CompoundTag abilities = new CompoundTag();
		for (Entry<ResourceLocation, int[]> pair : this.getAbilityMap().entrySet()) {
			abilities.putIntArray(pair.getKey().toString(), pair.getValue());
		}
		storage.put("abilities", abilities);

		ListTag list = new ListTag();
		for (ResourceLocation ab : this.getPAbilitiesList()) {
			list.add(StringTag.valueOf(ab.toString()));
		}
		storage.put("permanent_abilities", list);

		ListTag rcList = new ListTag();

		for (Entry<ResourceLocation, Integer> pair : this.getReactionCommands().entrySet()) {
			CompoundTag entryTag = new CompoundTag();
			entryTag.putString("key", pair.getKey().toString());
			entryTag.putInt("value", pair.getValue());
			rcList.add(entryTag);
		}
		storage.put("reaction_commands", rcList);

		CompoundTag keychains = new CompoundTag();
		this.getEquippedKeychains().forEach((form, chain) -> keychains.put(form.toString(), chain.saveOptional(provider)));
		storage.put("keychains", keychains);

		CompoundTag items = new CompoundTag();
		this.getEquippedItems().forEach((slot, item) -> items.put(slot.toString(), item.saveOptional(provider)));
		storage.put("items", items);

		CompoundTag accessories = new CompoundTag();
		this.getEquippedAccessories().forEach((slot, accessory) -> accessories.put(slot.toString(), accessory.saveOptional(provider)));
		storage.put("accessories", accessories);
		
		CompoundTag kbArmors = new CompoundTag();
		this.getEquippedKBArmors().forEach((slot, kbArmor) -> kbArmors.put(slot.toString(), kbArmor.saveOptional(provider)));
		storage.put("kbarmors", kbArmors);

		CompoundTag armors = new CompoundTag();
		this.getEquippedArmors().forEach((slot, armor) -> armors.put(slot.toString(), armor.saveOptional(provider)));
		storage.put("armors", armors);

	    CompoundTag equippedMagics = new CompoundTag();
	    this.getEquippedMagics().forEach((slot, magic) -> equippedMagics.put(slot.toString(), magic.saveOptional(provider)));
	    storage.put("equipped_magics", equippedMagics);

		storage.putInt("max_accessories", this.getMaxAccessories());
		storage.putInt("max_armors", this.getMaxArmors());
	    storage.putInt("max_magics", this.getMaxMagics());

		storage.putInt("hearts", this.getHearts());
		storage.putInt("org_alignment", this.getAlignmentIndex());
		storage.put("org_equipped_weapon", this.getEquippedWeapon().saveOptional(provider));

		CompoundTag unlockedWeapons = new CompoundTag();
		this.getWeaponsUnlocked().forEach(weapon -> unlockedWeapons.put(Utils.getItemRegistryName(weapon.getItem()).toString(), weapon.saveOptional(provider)));
		storage.put("org_weapons_unlocked", unlockedWeapons);

		CompoundTag parties = new CompoundTag();
		for (int i=0;i<this.getPartiesInvited().size();i++) {
			parties.putInt(this.getPartiesInvited().get(i),i);
		}
		storage.put("parties", parties);

		CompoundTag mats = new CompoundTag();
		for (Entry<ResourceLocation, Integer> pair : this.getMaterialMap().entrySet()) {
			mats.putInt(pair.getKey().toString(), pair.getValue());
			if (mats.getInt(pair.getKey().toString()) == 0 && pair.getKey() != null)
				mats.remove(pair.getKey().toString());
		}
		storage.put("materials", mats);

	    CompoundTag totalMats = new CompoundTag();
	    for (Entry<ResourceLocation, Integer> pair : this.getTotalMaterialMap().entrySet()) {
		    totalMats.putInt(pair.getKey().toString(), pair.getValue());
		    if (totalMats.getInt(pair.getKey().toString()) == 0 && pair.getKey() != null)
			    totalMats.remove(pair.getKey().toString());
	    }
	    storage.put("total_materials", totalMats);
		storage.putInt("limitCooldownTicks", this.getLimitCooldownTicks());

		CompoundTag shortcuts = new CompoundTag();
		for (Entry<Integer, Integer> pair : this.getShortcutsMap().entrySet()) {
			shortcuts.putInt(pair.getKey().toString(), pair.getValue());
		}
		storage.put("shortcuts", shortcuts);

		storage.putInt("synth_level", synthLevel);
		storage.putInt("synth_exp", synthExp);
		storage.putString("single_style", singleStyle.toString());
		storage.putString("dual_style", dualStyle.toString());

		storage.putInt("armor_color", armorColor);
		storage.putBoolean("armor_glint", armorGlint);
		
		storage.putBoolean("respawn_rod", respawnROD);
		
		storage.putInt("notif_color", notifColor);
		storage.putString("crown", this.crown);

		ListTag unlockedCrownsList = new ListTag();
		for (String unlocked : this.unlockedCrowns) {
			unlockedCrownsList.add(StringTag.valueOf(unlocked));
		}
		storage.put("unlocked_crowns", unlockedCrownsList);

		storage.putFloat("crown_offset_x", this.crownOffsetX);
		storage.putFloat("crown_offset_y", this.crownOffsetY);
		storage.putFloat("crown_offset_z", this.crownOffsetZ);
		storage.putFloat("crown_rotation_x", this.crownRotationX);
		storage.putFloat("crown_rotation_y", this.crownRotationY);
		storage.putFloat("crown_rotation_z", this.crownRotationZ);

		CompoundTag airstepCompound = new CompoundTag();
		Vec3 airstepVec = this.getAirStep().getCenter();
		airstepCompound.putDouble("x", airstepVec.x);
		airstepCompound.putDouble("y", airstepVec.y);
		airstepCompound.putDouble("z", airstepVec.z);
		storage.put("airstep_pos_compound", airstepCompound);

		CompoundTag savePoints = new CompoundTag();
		discoveredSavePoints.forEach((uuid, instant) -> {
			CompoundTag timeTag = new CompoundTag();
			timeTag.putLong("second", instant.getEpochSecond());
			timeTag.putInt("nano", instant.getNano());
			savePoints.put(uuid.toString(), timeTag);
		});
		storage.put("save_points", savePoints);

		CompoundTag synthedRecipes = new CompoundTag();
		for (String rec : this.getSynthesisedRecipes()) {
			synthedRecipes.putInt(rec, 0);
		}
		storage.put("synthesised_recipes",synthedRecipes);

		storage.putInt("aerial_dodge_ticks",this.aerialDodgeTicks);
		storage.putBoolean("aerial_dodge_jumped",this.hasJumpedAerialDodge());

		storage.putBoolean("is_gliding",this.isGliding);

		storage.putInt("cast_ticks", magicCasttime);
		storage.putInt("cd_ticks", magicCooldown);

		CompoundTag magicCooldownsTag = new CompoundTag();
		this.magicCooldowns.forEach((magic, ticks) -> magicCooldownsTag.putInt(magic.toString(), ticks));
		storage.put("magic_cds", magicCooldownsTag);

		storage.putInt("hanging_wall", hangingWallTicks);
		storage.putInt("wall_grabs", wallGrabs);
		storage.putBoolean("air_dashed", airDashed);
		storage.putBoolean("flowmotion", flowmotion);
		storage.putBoolean("bounced", bounced);

		storage.putInt("tutorial_flags", tutorialFlags);

		storage.put("overflow", ItemStack.CODEC.listOf().encodeStart(NbtOps.INSTANCE, overflow.stream().toList()).getPartialOrThrow());
		return storage;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
		this.setVer(nbt.getInt("data_ver"));
		this.setLevel(nbt.getInt("level"));
		this.setExperience(nbt.getInt("experience"));
		this.setExperienceGiven(nbt.getInt("experience_given"));
		strength = Stat.deserializeNBT("strength", nbt);
		if (nbt.contains("boost_strength")) {
			int boost = nbt.getInt("boost_strength");
			nbt.remove("boost_strength");
			strength.addModifier("legacy_boosts", boost, false, false);
		}
		magic = Stat.deserializeNBT("magic", nbt);
		if (nbt.contains("boost_magic")) {
			int boost = nbt.getInt("boost_magic");
			nbt.remove("boost_magic");
			magic.addModifier("legacy_boosts", boost, false, false);
		}
		defense = Stat.deserializeNBT("defense", nbt);
		if (nbt.contains("boost_defense")) {
			int boost = nbt.getInt("boost_defense");
			nbt.remove("boost_defense");
			defense.addModifier("legacy_boosts", boost, false, false);
		}
		maxAP = Stat.deserializeNBT("max_ap", nbt);
		if (nbt.contains("boost_max_ap")) {
			int boost = nbt.getInt("boost_max_ap");
			nbt.remove("boost_max_ap");
			maxAP.addModifier("legacy_boosts", boost, false, false);
		}
		this.setMaxHP(nbt.getInt("max_hp"));
		this.setMP(nbt.getDouble("mp"));
		this.setMaxMP(nbt.getDouble("max_mp"));
		this.setFocus(nbt.getDouble("focus"));
		this.setMaxFocus(nbt.getDouble("max_focus"));
		this.setRecharge(nbt.getBoolean("recharge"));
		this.setDP(nbt.getDouble("dp"));
		this.setMaxDP(nbt.getDouble("max_dp"));
		this.setFP(nbt.getDouble("fp"));
		this.setActiveDriveForm(KingdomKeys.rl(nbt.getString("drive_form")));
		this.setAntiPoints(nbt.getInt("anti_points"));
		this.setReflectTicks(nbt.getInt("reflect_ticks"), nbt.getInt("reflect_level"));
		this.setReflectActive(nbt.getBoolean("reflect_active"));
		this.setMunny(nbt.getInt("munny"));
		this.setSoAState(SoAState.fromByte(nbt.getByte("soa_state")));
		this.setChoice(SoAState.fromByte(nbt.getByte("soa_choice")));
		this.setSacrifice(SoAState.fromByte(nbt.getByte("soa_sacrifice")));
		CompoundTag returnCompound = nbt.getCompound("soa_return_pos");
		this.setReturnLocation(new Vec3(returnCompound.getDouble("x"), returnCompound.getDouble("y"), returnCompound.getDouble("z")));
		this.setReturnDimension(ResourceKey.create(Registries.DIMENSION, KingdomKeys.rl(nbt.getString("soa_return_dim"))));
		CompoundTag choicePedestal = nbt.getCompound("soa_choice_pedestal");
		this.setChoicePedestal(new BlockPos(choicePedestal.getInt("x"), choicePedestal.getInt("y"), choicePedestal.getInt("z")));
		CompoundTag sacrificePedestal = nbt.getCompound("soa_sacrifice_pedestal");
		this.setSacrificePedestal(new BlockPos(sacrificePedestal.getInt("x"), sacrificePedestal.getInt("y"), sacrificePedestal.getInt("z")));

		recipeList.clear();
		for (String key : nbt.getCompound("recipes").getAllKeys()) {
			this.getKnownRecipeList().add(KingdomKeys.rl(key));
		}
		Collections.sort(recipeList);

		magicCastMap.clear();
		for (String magicName : nbt.getCompound("magic_casts").getAllKeys()) {
			int casts = nbt.getCompound("magic_casts").getInt(magicName);
			ResourceLocation location = KingdomKeys.rl(magicName);
			if (ModMagic.registry.containsKey(location)) {
				this.getMagicsCastMap().put(location, casts);
			}
		}

		this.equippedShotlock = ItemStack.parseOptional(provider, nbt.getCompound("equipped_shotlock"));

        shotlockEnemies.clear();
        int targetsSize = nbt.getCompound("target_shotlocks").getAllKeys().size();
        for (int i = 0; i < targetsSize; i++) {
            CompoundTag tag1 = nbt.getCompound("target_shotlocks").getCompound("target"+i);
            Utils.ShotlockPosition pos = new Utils.ShotlockPosition(tag1.getInt("id"),tag1.getFloat("x"),tag1.getFloat("y"),tag1.getFloat("z"));
            this.shotlockEnemies.add(pos);
        }

		driveForms.clear();
		for (String driveFormName : nbt.getCompound("drive_forms").getAllKeys()) {
			ResourceLocation location = KingdomKeys.rl(driveFormName);
			if (ModDriveForms.registry.containsKey(location)) {
				this.getDriveFormMap().put(location, nbt.getCompound("drive_forms").getIntArray(driveFormName));
			}
		}

		abilityMap.clear();
		for (String abilityName : nbt.getCompound("abilities").getAllKeys()) {
			ResourceLocation location = KingdomKeys.rl(abilityName);
			if (ModAbilities.registry.containsKey(location)) {
				this.getAbilityMap().put(location, nbt.getCompound("abilities").getIntArray(abilityName));
			}
		}

		pAbilitiesList.clear();
		ListTag list = nbt.getList("permanent_abilities", Tag.TAG_STRING);

		for (int i = 0; i < list.size(); i++) {
			String abilityName = list.getString(i);
			ResourceLocation id = KingdomKeys.rl(abilityName);
			if (ModAbilities.registry.containsKey(id)) {
				pAbilitiesList.add(id);
			}
		}

		reactionMap.clear();
		ListTag rcList = nbt.getList("reaction_commands", Tag.TAG_COMPOUND);
		for (int i = 0; i < rcList.size(); i++) {
			CompoundTag entryTag = rcList.getCompound(i);
			String key = entryTag.getString("key");
			int value = entryTag.getInt("value");
			this.getReactionCommands().put(KingdomKeys.rl(key), value);
		}

		equippedKeychains.clear();
		CompoundTag keychainsNBT = nbt.getCompound("keychains");
		keychainsNBT.getAllKeys().forEach((chain) -> this.setNewKeychain(KingdomKeys.rl(chain), ItemStack.parseOptional(provider, keychainsNBT.getCompound(chain))));

		equippedItems.clear();
		CompoundTag itemsNBT = nbt.getCompound("items");
		itemsNBT.getAllKeys().forEach((slot) -> this.setNewItem(Integer.parseInt(slot), ItemStack.parseOptional(provider, itemsNBT.getCompound(slot))));

		equippedAccessories.clear();
		CompoundTag accessoriesNBT = nbt.getCompound("accessories");
		accessoriesNBT.getAllKeys().forEach((slot) -> this.setNewAccessory(Integer.parseInt(slot), ItemStack.parseOptional(provider, accessoriesNBT.getCompound(slot))));

		equippedKBArmors.clear();
		CompoundTag kbArmorsNBT = nbt.getCompound("kbarmors");
		kbArmorsNBT.getAllKeys().forEach((slot) -> this.setNewKBArmor(Integer.parseInt(slot), ItemStack.parseOptional(provider, kbArmorsNBT.getCompound(slot))));

		equippedArmors.clear();
		CompoundTag armorsNBT = nbt.getCompound("armors");
		armorsNBT.getAllKeys().forEach((slot) -> this.setNewArmor(Integer.parseInt(slot), ItemStack.parseOptional(provider, armorsNBT.getCompound(slot))));

		equippedMagics.clear();
		CompoundTag magicsNBT = nbt.getCompound("equipped_magics");
		magicsNBT.getAllKeys().forEach((slot) -> this.setNewMagic(Integer.parseInt(slot), ItemStack.parseOptional(provider, magicsNBT.getCompound(slot))));

		this.setMaxAccessories(nbt.getInt("max_accessories"));
		this.setMaxArmors(nbt.getInt("max_armors"));
		this.setMaxMagics(nbt.getInt("max_magics"));
		
		this.setHearts(nbt.getInt("hearts"));
		this.setAlignment(nbt.getInt("org_alignment"));
		this.equipWeapon(ItemStack.parseOptional(provider, nbt.getCompound("org_equipped_weapon")));
		CompoundTag unlocksCompound = nbt.getCompound("org_weapons_unlocked");
		weaponUnlocks.clear();
		unlocksCompound.getAllKeys().forEach(key -> this.unlockWeapon(ItemStack.parseOptional(provider, unlocksCompound.getCompound(key))));

		partyList.clear();
		for (String key : nbt.getCompound("parties").getAllKeys()) {
			this.getPartiesInvited().add(key);
		}

		materials.clear();
		for (String mat : nbt.getCompound("materials").getAllKeys()) {
			this.getMaterialMap().put(KingdomKeys.rl(mat), nbt.getCompound("materials").getInt(mat));
		}

		totalMaterials.clear();
		for (String mat : nbt.getCompound("total_materials").getAllKeys()) {
			this.getTotalMaterialMap().put(KingdomKeys.rl(mat), nbt.getCompound("total_materials").getInt(mat));
		}

		this.setLimitCooldownTicks(nbt.getInt("limitCooldownTicks"));

		shortcutsMap.clear();
		for (String s : nbt.getCompound("shortcuts").getAllKeys()) {
			int shortcutPos = Integer.parseInt(s);
			this.getShortcutsMap().put(shortcutPos, nbt.getCompound("shortcuts").getInt(shortcutPos + ""));
		}

		this.setSynthLevel(nbt.getInt("synth_level"));
		this.setSynthExperience(nbt.getInt("synth_exp"));
		String s = nbt.getString("single_style");
		if(!s.isEmpty())
			this.setSingleStyle(SingleChoices.valueOf(s));
		s=nbt.getString("dual_style");
		if(!s.isEmpty())
			this.setDualStyle(DualChoices.valueOf(s));
		this.setArmorColor(nbt.getInt("armor_color"));
		this.setArmorGlint(nbt.getBoolean("armor_glint"));
		this.setRespawnROD(nbt.getBoolean("respawn_rod"));
		this.setNotifColor(nbt.getInt("notif_color"));
		this.setCrown(nbt.getString("crown"));

		this.unlockedCrowns.clear();
		ListTag unlockedCrownsList = nbt.getList("unlocked_crowns", Tag.TAG_STRING);
		for (int i = 0; i < unlockedCrownsList.size(); i++) {
			this.unlockedCrowns.add(unlockedCrownsList.getString(i));
		}
		// Saves from before crowns were unlockable only stored the one being worn. Whatever they had on
		// is theirs to keep, so grandfather it in rather than taking it away on login.
		if (this.unlockedCrowns.isEmpty() && !this.getCrown().isEmpty()) {
			this.unlockedCrowns.add(this.getCrown());
		}

		this.crownOffsetX = nbt.getFloat("crown_offset_x");
		this.crownOffsetY = nbt.getFloat("crown_offset_y");
		this.crownOffsetZ = nbt.getFloat("crown_offset_z");
		this.crownRotationX = nbt.getFloat("crown_rotation_x");
		// "crown_rotation" was the old single-axis key; fall back to it so existing saves keep their spin.
		this.crownRotationY = nbt.contains("crown_rotation_y") ? nbt.getFloat("crown_rotation_y") : nbt.getFloat("crown_rotation");
		this.crownRotationZ = nbt.getFloat("crown_rotation_z");

		CompoundTag airStepCompound = nbt.getCompound("airstep_pos_compound");
		this.setAirStep(new BlockPos((int)airStepCompound.getDouble("x"), (int)airStepCompound.getDouble("y"), (int)airStepCompound.getDouble("z")));

		discoveredSavePoints.clear();
		CompoundTag savePoints = nbt.getCompound("save_points");
		for (String key : savePoints.getAllKeys()) {
			UUID uuid = UUID.fromString(key);
			CompoundTag time = savePoints.getCompound(key);
			addDiscoveredSavePoint(uuid, Instant.ofEpochSecond(time.getLong("second"), time.getInt("nano")));
		}

		synthesisedRecipes.clear();
		for (String key : nbt.getCompound("synthesised_recipes").getAllKeys()) {
			if (RecipeRegistry.getInstance().getRegistry().containsKey(KingdomKeys.rl(key))) {
				this.getSynthesisedRecipes().add(key);
			}
		}

		this.setAerialDodgeTicks(nbt.getInt("aerial_dodge_ticks"));
		this.setHasJumpedAerialDodge(nbt.getBoolean("aerial_dodge_jumped"));
		this.setIsGliding(nbt.getBoolean("is_gliding"));

		this.setMagicCasttimeTicks(nbt.getInt("cast_ticks"));
		this.setMagicCooldownTicks(nbt.getInt("cd_ticks"));

		this.magicCooldowns.clear();
		CompoundTag magicCooldownsTag = nbt.getCompound("magic_cds");
		for (String key : magicCooldownsTag.getAllKeys()) {
			this.magicCooldowns.put(ResourceLocation.parse(key), magicCooldownsTag.getInt(key));
		}
		this.setHangingWallTicks(nbt.getInt("hanging_wall"));
		this.setWallGrabs(nbt.getInt("wall_grabs"));
		this.setAirDashed(nbt.getBoolean("air_dashed"));
		this.setFlowmotion(nbt.getBoolean("flowmotion"));
		this.setBounced(nbt.getBoolean("bounced"));

		this.setTutorialFlags(nbt.getInt("tutorial_flags"));

		if (nbt.contains("overflow")) {
			this.overflow = new ArrayDeque<>(ItemStack.CODEC.listOf().parse(NbtOps.INSTANCE, nbt.get("overflow")).getPartialOrThrow());
		} else {
			this.overflow = new ArrayDeque<>();
		}
	}

	private int ver = -1, level = 1, exp = 0, expGiven = 0, maxHp = 20, remainingExp = 0, reflectTicks = 0, reflectLevel = 0, magicCasttime = 0, magicCooldown = 0, munny = 0, antipoints = 0, aerialDodgeTicks, synthLevel=1, synthExp, remainingSynthExp = 0,hangingWallTicks, wallGrabs, tutorialFlags;
	private BlockPos airStepPos = new BlockPos(0,0,0);
	Stat strength = new Stat("strength", 1);
	Stat magic = new Stat("magic",1);
	Stat defense = new Stat("defense", 1);
	Stat maxAP = new Stat("max_ap", 0);

	private ResourceLocation driveForm = DriveForm.NONE;
	LinkedHashMap<ResourceLocation, int[]> driveForms = new LinkedHashMap<>(); //Key = name, value=  {level, experience}
	LinkedHashMap<ResourceLocation, Integer> magicCastMap = new LinkedHashMap<>(); //Key = name, value=  {level, uses_in_combo}
	List<Utils.ShotlockPosition> shotlockEnemies = new ArrayList<>();
	boolean hasShotMaxShotlock = false;
	List<ResourceLocation> recipeList = new ArrayList<>();
	LinkedHashMap<ResourceLocation, int[]> abilityMap = new LinkedHashMap<>(); //Key = name, value = {level, equipped},
    private TreeMap<ResourceLocation, Integer> materials = new TreeMap<>();
	private TreeMap<ResourceLocation, Integer> totalMaterials = new TreeMap<>();

	private final Map<ResourceLocation, Integer> magicCooldowns = new HashMap<>();

	LinkedHashMap<ResourceLocation, Integer> reactionMap = new LinkedHashMap<>();

	private final List<ResourceLocation> reactionTickScratch = new ArrayList<>();
	List<ResourceLocation> pAbilitiesList = new ArrayList<>();

	List<String> partyList = new ArrayList<>();
	ItemStack equippedShotlock = ItemStack.EMPTY;

	LinkedHashMap<Integer,Integer> shortcutsMap = new LinkedHashMap<>(); //Key = magic name, value=  {position, level}

	private double mp = 0, maxMP = 0, dp = 0, maxDP = 1000, fp = 0, focus = 100, maxFocus = 100;

	private boolean recharge, reflectActive, isGliding, hasJumpedAerealDodge = false, airDashed, bounced, flowmotion;

	private Vec3 returnPos = Vec3.ZERO;
	private ResourceKey<Level> returnDim = Level.OVERWORLD;

	SoAState soAState = SoAState.NONE, choice = SoAState.NONE, sacrifice = SoAState.NONE;

	private BlockPos choicePedestal = new BlockPos(0, 0, 0), sacrificePedestal = new BlockPos(0, 0, 0);

	private List<String> messages = new ArrayList<>();
	private List<String> bfMessages = new ArrayList<>();
	private List<String> dfMessages = new ArrayList<>();

	private Utils.OrgMember alignment = Utils.OrgMember.NONE;
	private int hearts = 0;
	private Set<ItemStack> weaponUnlocks = new HashSet<>();
	private int limitCooldownTicks = 0;


	private ItemStack equippedWeapon = ItemStack.EMPTY;

	private Map<ResourceLocation, ItemStack> equippedKeychains = new HashMap<>();
	private Map<Integer, ItemStack> equippedItems = new HashMap<>();
	private Map<Integer, ItemStack> equippedAccessories = new HashMap<>();
	private Map<Integer, ItemStack> equippedArmors = new HashMap<>();
	private Map<Integer, ItemStack> equippedKBArmors = new HashMap<>();
	private Map<Integer, ItemStack> equippedMagics = new HashMap<>();

	private int maxAccessories = 0;
	private int maxArmors = 0;
	private int maxMagics = 0;

	private int armorColor = 16777215;
	private boolean armorGlint = true;

	private SingleChoices singleStyle = SingleChoices.SORA;
	private DualChoices dualStyle = DualChoices.KH2_ROXAS_DUAL;

	private boolean respawnROD = false;
	private int notifColor = 16777215;
	/** Cosmetic crown texture name ("bronze", "silver", "gold"...). Empty = no crown.
	 * Stored as a plain string so adding a new crown only needs a PNG, no code change. */
	private String crown = "";
	/** Crowns this player has earned. What they wear ({@link #crown}) is a choice among these. */
	private final Set<String> unlockedCrowns = new LinkedHashSet<>();
	/** Where the crown sits on top of the head, in model units (16 = one block). Only X (left/right)
	 * and Z (forward/back) - the height is always the top of the head, so there is no Y here.
	 * The tilt lives in the crownRotationX/Y/Z fields below. */
	private float crownOffsetX = 0F, crownOffsetY = 0F, crownOffsetZ = 0F;
	/** Crown tilt on all three axes, in degrees: X pitches it forward/back, Y spins it, Z rolls it. */
	private float crownRotationX = 0F, crownRotationY = 0F, crownRotationZ = 0F;

	private Map<UUID, Instant> discoveredSavePoints = new HashMap<>();

	Utils.castMagic castMagic = null;

	private Set<String> synthesisedRecipes = new HashSet<>();

	private Queue<ItemStack> overflow = new ArrayDeque<>();

	public int getVer(){
		return ver;
	}

	public void setVer(int ver) {
		this.ver = ver;
	}

	//region Main stats, level, exp, str, mag, ap

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExperience() {
		return exp;
	}

	public void setExperience(int exp) {
		this.exp = exp;
	}

	public void addExperience(Player player, int exp, boolean shareXP, boolean sound) {
		if (player != null && getSoAState() == SoAState.COMPLETE) {
			if (this.level < 100) {//TODO change 100 for the actual max level in the config file?
				Party party = WorldData.get(player.getServer()).getPartyFromMember(player.getUUID());
				if(party != null && shareXP) { //If player is in a party and first to get EXP
					double sharedXP = (exp * ((ModConfigs.SERVER.partyXPShare.get() / 100F) * 2F)); // exp * share% * 2 (2 being to apply the formula from the 2 player party as mentioned in the config)
					//sharedXP /= party.getMembers().size(); //Divide by the total amount of party players

					if(sharedXP > 0) {
						for(Member member : party.getMembers()) {
							for(ResourceKey<Level> worldKey : player.level().getServer().levelKeys()) {
								Player ally = player.getServer().getLevel(worldKey).getPlayerByUUID(member.getUUID());
								if(ally != null && ally != player) { //If the ally is not this player give him exp (he will already get the full exp)
									PlayerData.get(ally).addExperience(ally, (int) sharedXP, false, true); //Give EXP to other players with the false param to prevent getting in a loop
									PacketHandler.sendTo(new SCSyncPlayerData(ally), (ServerPlayer)ally);
								}
							}
						}
					}

					//Adding the exp here as it will iterate through the party and this user won't get it there
					if(getNumberOfAbilitiesEquipped(ModAbilities.EXPERIENCE_BOOST) > 0 && player.getHealth() <= player.getMaxHealth() / 2) {
						exp *= (1 + getNumberOfAbilitiesEquipped(ModAbilities.EXPERIENCE_BOOST));
					}

                } else { //Player not in a party or shareXP is false (command)
					if(getNumberOfAbilitiesEquipped(ModAbilities.EXPERIENCE_BOOST) > 0 && player.getHealth() <= player.getMaxHealth() / 2 && shareXP) { //if shareXP is false means it gets here because of the command
						exp *= (1 + getNumberOfAbilitiesEquipped(ModAbilities.EXPERIENCE_BOOST));
					}
                }
                this.exp += exp;

                while (this.getExpNeeded(this.getLevel(), this.exp) <= 0 && this.getLevel() != 100) {
					setLevel(this.getLevel() + 1);
					if(player instanceof ServerPlayer svPlayer) {
				       ModAdvancements.levelUp.trigger(svPlayer, this.getLevel());
					}
					levelUpStatsAndDisplayMessage(player, sound);
					PacketHandler.sendTo(new SCShowOverlayPacket("levelup", player.getUUID(), player.getGameProfile().getName(), getLevel(), getNotifColor(), new ArrayList<>(getMessages())), (ServerPlayer) player);
				}
				PacketHandler.sendTo(new SCShowOverlayPacket("exp"), (ServerPlayer) player);
			}
		}
	}

	public int getExperienceGiven() {
		return expGiven;
	}

	public void setExperienceGiven(int exp) {
		this.expGiven = exp;
	}

	public int getStrength(boolean combined) {
		return (int) (combined ? ((ModConfigs.SERVER.allowBoosts.get() ? strength.getStat() : strength.get()) + Utils.getAccessoriesStat(this, "str") + (getRecharge()? getNumberOfAbilitiesEquipped(ModAbilities.BERSERK_CHARGE) * 2 : 0) ) * ModConfigs.SERVER.statsMultiplier.get().get(0) / 100 : strength.get());
	}

	public void setStrength(int level) {
		this.strength.set(level);
	}

	public int getMagic(boolean combined) {
		return (int) (combined ? ((ModConfigs.SERVER.allowBoosts.get() ? magic.getStat() : magic.get()) + Utils.getAccessoriesStat(this, "mag")) * ModConfigs.SERVER.statsMultiplier.get().get(1) / 100 : magic.get());
	}

	public void setMagic(int level) {
		this.magic.set(level);
	}

	public int getDefense(boolean combined) {
		return (int) (combined ? ((ModConfigs.SERVER.allowBoosts.get() ? defense.getStat() : defense.get()) + Utils.getArmorsStat(this, "def")) * ModConfigs.SERVER.statsMultiplier.get().get(2) / 100 : defense.get());
	}

	public void setDefense(int level) {
		this.defense.set(level);
	}

	public int getExpNeeded(int level, int currentExp) {
		if (level == 100)
			return 0;
		double nextLevel = (level + 300.0 * (Math.pow(2.0, (level / 7.0)))) * (level * 0.25);
		this.remainingExp = ((int) nextLevel - currentExp);
		return remainingExp;
	}

	public void addStrength(int str) {
		this.strength.add(str);
		messages.add(Strings.Stats_LevelUp_Str);
	}

	public Stat getStrengthStat() {
		return strength;
	}

	public void setStrengthStat(Stat stat) {
		this.strength = stat;
	}

	public void addMagic(int mag) {
		this.magic.add(mag);
		messages.add(Strings.Stats_LevelUp_Magic);
	}

	public Stat getMagicStat() {
		return magic;
	}

	public void setMagicStat(Stat stat) {
		this.magic = stat;
	}

	public void addDefense(int def) {
		this.defense.add(def);
		messages.add(Strings.Stats_LevelUp_Def);
	}

	public Stat getDefenseStat() {
		return defense;
	}

	public void setDefenseStat(Stat stat) {
		this.defense = stat;
	}

	public int getMaxHP() {
		return maxHp;
	}

	public void setMaxHP(int hp) {
		this.maxHp = hp;
	}

	public void addMaxHP(int hp) {
		this.maxHp += hp;
		messages.add(Strings.Stats_LevelUp_HP);
	}

	public int getMaxAP(boolean combined) {
		return (int) (combined ? (maxAP.getStat() + getAccessoriesAP("ap")) : maxAP.get());
	}

	public Stat getMaxAPStat() {
		return maxAP;
	}

	public void setMaxAPStat(Stat stat) {
		this.maxAP = stat;
	}

	private int getAccessoriesAP(String type) {
		int res = 0;
		for(Entry<Integer, ItemStack> accessory : getEquippedAccessories().entrySet()) {
			if(!ItemStack.matches(accessory.getValue(), ItemStack.EMPTY) && accessory.getValue().getItem() instanceof KKAccessoryItem a) {
                switch(type) {
				case "ap":
					res += a.getAp();
					break;
				case "str":
					res += a.getStr();
					break;
				case "mag":
					res += a.getMag();
					break;
				}

			}
		}
		return res;
	}

	public void setMaxAP(int ap) {
		this.maxAP.set(ap);
	}

	public void addMaxAP(int ap) {
		this.maxAP.add(ap);
		messages.add(Strings.Stats_LevelUp_AP);
	}

	//endregion

	//region Level messages including Drive

	public List<String> getMessages() {
		return this.messages;
	}

	public void clearMessages() {
		this.getMessages().clear();
	}

	public List<String> getBFMessages() {
		return this.bfMessages;
	}

	public void clearBFMessages() {
		this.getBFMessages().clear();
	}

	public void setBFMessages(List<String> messages) {
		this.bfMessages = messages;
	}

	public List<String> getDFMessages() {
		return this.dfMessages;
	}

	public void clearDFMessages() {
		this.getDFMessages().clear();
	}

	public void setDFMessages(List<String> messages) {
		this.dfMessages = messages;
	}

	public void levelUpStatsAndDisplayMessage(Player player, boolean sound) {
		this.getMessages().clear();
		LevelStats.applyStatsForLevel(this.level, player, this);

		Party party = WorldData.get(player.getServer()).getPartyFromMember(player.getUUID());
		if(party != null) {
			for(Member member : party.getMembers()) {
				for(ResourceKey<Level> worldKey : player.level().getServer().levelKeys()) {
					Player ally = player.getServer().getLevel(worldKey).getPlayerByUUID(member.getUUID());
					if(ally != null && ally != player) { //If the ally is not this player give him exp (he will already get the full exp)
						PacketHandler.sendTo(new SCShowOverlayPacket("levelup", player.getUUID(), player.getGameProfile().getName(), getLevel(), getNotifColor(), new ArrayList<>(getMessages())), (ServerPlayer) ally);
						PacketHandler.syncToAllAround(player, this);
					}
				}
			}
		}

		if(sound)
			player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.levelup.get(), SoundSource.MASTER, 0.5f, 1.0f);
		player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getMaxHP());
		PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
		PacketHandler.syncToAllAround(player, this);

		AABB radius = new AABB(player.getX(), player.getY(), player.getZ(), player.getX() + 1, player.getY() + 1, player.getZ() + 1).inflate(50, 50, 50);
		List<TamableAnimal> tamedAnimals = player.level().getNearbyEntities(TamableAnimal.class, TargetingConditions.forNonCombat(), player, radius);
		if (!tamedAnimals.isEmpty()) {
			tamedAnimals.forEach(tamableAnimal -> {
				GlobalData animalData = GlobalData.get(tamableAnimal);
				animalData.setLevel(getLevel());
				Utils.applyMobLevel(tamableAnimal, level);
				tamableAnimal.heal(tamableAnimal.getMaxHealth());
			});
		}
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public void displayDriveFormLevelUpMessage(Player player, ResourceLocation driveForm) {
		this.getBFMessages().clear();
		this.getDFMessages().clear();

		dfMessages.add(Strings.Stats_LevelUp_FormGauge);
		DriveForm form = ModDriveForms.registry.get(driveForm);

		form.getDFAbilityForLevel(getDriveFormLevel(driveForm)).ifPresent(driveformAbility -> {
			Ability a = ModAbilities.registry.get(driveformAbility);
			String name = a.getTranslationKey();
			if(a.getType() == AbilityType.GROWTH) {
				int level = (getEquippedAbilityLevel(driveformAbility)[0]+2); //+2 Because it's not set yet, it should be +1 if the ability was already upgraded at the time of generating this message
				name = (new StringBuilder(name).insert(name.lastIndexOf('.'), "_"+level)).toString();
			}
			dfMessages.add("A_"+name);
		});

		form.getBaseAbilityForLevel(getDriveFormLevel(driveForm)).ifPresent(baseAbility -> {
			Ability a = ModAbilities.registry.get(baseAbility);
			String name = a.getTranslationKey();
			if(a.getType() == AbilityType.GROWTH) {
				name = (new StringBuilder(name).insert(name.lastIndexOf('.'), "_"+(getEquippedAbilityLevel(baseAbility)[0]+1))).toString();
			}
			addAbility(baseAbility,name, true);
		});

		player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.levelup.get(), SoundSource.MASTER, 0.5f, 1.0f);

		PacketHandler.sendTo(new SCShowOverlayPacket("drivelevelup", driveForm, new ArrayList<>(bfMessages), new ArrayList<>(dfMessages)), (ServerPlayer) player);
	}

	//endregion

	//region Drive forms, DP FP

	public double getDP() {
		return dp;
	}

	public void setDP(double dp) {
		this.dp = dp;
	}

	public void addDP(double dp) {
		this.dp = Utils.clamp(this.dp + dp, 0, this.maxDP);
	}

	public void addDP(Player player, double dp) {
		int oldDrive = (int) (this.dp / 100);
		this.dp = Utils.clamp(this.dp + dp, 0, this.maxDP);
		int newDP = (int) (this.dp / 100);
		if(oldDrive < newDP) {
			player.level().playSound(null,player.position().x,player.position().y, player.position().z, ModSounds.driveUp.get(), SoundSource.PLAYERS, 0.2F,1);
		}
	}

	public void remDP(double dp) {
		this.dp = Utils.clamp(this.dp - dp, 0, this.maxDP);
	}

	public double getMaxDP() {
		return maxDP;
	}

	public void setMaxDP(double dp) {
		this.maxDP = Math.min(this.maxDP + dp, 1000);
	}

	public double getFP() {
		return fp;
	}

	public void setFP(double fp) {
		this.fp = fp;
	}

	public void addFP(double fp) {
		double max = (200 + Utils.getDriveFormLevel(getDriveFormMap(), getActiveDriveForm()) * 100);
		this.fp = Math.min(this.fp + fp, max);
	}

	public void remFP(double fp) {
		this.fp = Math.max(this.fp - fp, 0);
	}

	public void setActiveDriveForm(ResourceLocation form) {
		driveForm = form;
	}

	public ResourceLocation getActiveDriveForm() {
		return driveForm;
	}

	public boolean noFormActive() {
		return isFormActive(ModDriveForms.NONE);
	}

	public boolean isFormActive(KKSupplier<DriveForm> form) {
		return getActiveDriveForm().equals(form.location());
	}

	public LinkedHashMap<ResourceLocation, int[]> getDriveFormMap() {
		return driveForms;//Utils.getSortedDriveForms(driveForms);
	}

	public void setDriveFormMap(LinkedHashMap<ResourceLocation, int[]> map) {
		this.driveForms = map;
	}

	public int getDriveFormLevel(ResourceLocation name) {
		return driveForms.containsKey(name) ? driveForms.get(name)[0] : 0;
	}

	public void setDriveFormLevel(ResourceLocation name, int level) {
		DriveForm form = ModDriveForms.registry.get(name);
		if(Utils.getFakeForms().contains(name)){
			driveForms.put(name, new int[] {level, 1});
		} else {
			if(level == 0) {
				driveForms.remove(name);
			} else {
				if(level <= form.getMaxLevel()) {
					int experience = form.getLevelUpCost(level);
					driveForms.put(name, new int[] {level, experience});
				}
			}
		}
	}

	public int getDriveFormExp(ResourceLocation name) {
		return driveForms.get(name)[1];
	}

	public void setDriveFormExp(Player player, ResourceLocation name, int exp) {
		DriveForm form = ModDriveForms.registry.get(name);
		int oldLevel = getDriveFormLevel(name);
		int driveLevel = form.getLevelFromExp(exp);
		if(driveLevel <= form.getMaxLevel()) {
			driveForms.put(name, new int[] {driveLevel, exp});
			if(driveLevel > oldLevel) {
				displayDriveFormLevelUpMessage(player, name);
				if(driveLevel == form.getMaxLevel()) {
					setMaxDP(getMaxDP() + 100);
				}
				PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer)player);
			}
		}
	}

	public void addDriveFormExperience(ResourceLocation drive, ServerPlayer player, int value) {
		DriveForm form = ModDriveForms.registry.get(drive);
		int oldLevel = getDriveFormLevel(drive);
		int driveLevel = form.getLevelFromExp(exp+value);

		if(driveLevel <= form.getMaxLevel()) {
			driveForms.put(drive, new int[] {driveLevel, exp+value});
			if(driveLevel > oldLevel) {
				displayDriveFormLevelUpMessage(player, drive);
				if(driveLevel == form.getMaxLevel()) {
					setMaxDP(getMaxDP() + 100);
				}
				PacketHandler.sendTo(new SCSyncPlayerData(player), player);
			}
		}
	}

	public int getAntiPoints() {
		return antipoints;
	}

	public void setAntiPoints(int points) {
		this.antipoints = points;
	}

	//endregion

	//region Magic, MP

	public double getMP() {
		return mp;
	}

	public void setMP(double mp) {
		this.mp = mp;
	}

	public void addMP(double mp) {
		this.mp = Utils.clamp(this.mp + mp, 0, this.maxMP);
	}

	public double getMaxMP() {
		return maxMP;
	}

	public void setMaxMP(double mp) {
		this.maxMP = mp;
	}

	public void addMaxMP(double mp) {
		this.maxMP += mp;
		setMP(getMaxMP());
		messages.add(Strings.Stats_LevelUp_MP);
	}

	public void remMP(double amount) {
		if(isAbilityEquipped(ModAbilities.EXTRA_CAST)) {
			if(amount >= this.maxMP) {
				this.mp = Math.max(this.mp - amount, 0);
			} else {
				if(this.mp > 1 && this.mp - amount < 1) {
					this.mp = 1;
				}else {
					this.mp = Math.max(this.mp - amount, 0);
				}
			}
		} else {
			this.mp = Math.max(this.mp - amount, 0);
		}
	}

	public double getFocus() {
		return focus;
	}

	public void setFocus(double focus) {
		this.focus = focus;
	}

	public void addFocus(double focus) {
		this.focus = Math.min(this.focus+focus, getMaxFocus());
	}

	public void remFocus(double cost) {
		this.focus = Math.max(focus-cost, 0);
	}

	public double getMaxFocus() {
		return maxFocus;
	}

	public void setMaxFocus(double maxFocus) {
		this.maxFocus = maxFocus;
	}

	public void addMaxFocus(double focus) {
		this.focus += focus;
	}

	public void setShotlockEnemies(List<Utils.ShotlockPosition> list) {
		this.shotlockEnemies = list;
	}

	public List<Utils.ShotlockPosition> getShotlockEnemies() {
		return shotlockEnemies;
	}

	public void addShotlockEnemy(Utils.ShotlockPosition shotlockPos) {
		this.shotlockEnemies.add(shotlockPos);
	}

	public boolean hasShotMaxShotlock() {
		return hasShotMaxShotlock;
	}

	public void setHasShotMaxShotlock(boolean val) {
		this.hasShotMaxShotlock = val;
	}

	public void setRecharge(boolean b) {
		this.recharge = b;
	}

	public boolean getRecharge() {
		return this.recharge;
	}

	public int getReflectLevel() {
		return reflectLevel;
	}

	public void setReflectLevel(int level) {
		this.reflectLevel = level;
	}

	public void setReflectTicks(int ticks, int level) {
		reflectTicks = ticks;
		reflectLevel = level;
	}

	public void remReflectTicks(int ticks) {
		reflectTicks -= ticks;
	}

	public int getReflectTicks() {
		return reflectTicks;
	}

	public void setReflectActive(boolean active) {
		reflectActive = active;
	}

	public boolean getReflectActive() {
		return reflectActive;
	}

	public LinkedHashMap<ResourceLocation, Integer> getMagicsCastMap() {
		return magicCastMap;
	}

	public void setMagicsMap(LinkedHashMap<ResourceLocation, Integer> map) {
		this.magicCastMap = map;
	}

	public int getMagicUses(ResourceLocation name) {
		if(!magicCastMap.containsKey(name)) {
			magicCastMap.put(name, 0);
		}
		return magicCastMap.get(name);
	}

	public void setMagicUses(ResourceLocation name, int uses) {
		magicCastMap.put(name, uses);
	}

	public void addMagicUses(ResourceLocation name, int uses) {
		setMagicUses(name, getMagicUses(name) + uses);
	}

	public void remMagicUses(ResourceLocation name, int uses) {
		setMagicUses(name, getMagicUses(name) - uses);
	}

	public void setMunny(int amount) {
		this.munny = amount;
	}

	public void setMunny(int amount, ServerPlayer player) {
		setMunny(amount);
		if (player != null) {
			ModAdvancements.triggerMunnyReached(player, amount);
		}
	}

	public int getMunny() {
		return munny;
	}

	//endregion

	//region Keyblade
	public Map<ResourceLocation, ItemStack> getEquippedKeychains() {
		return equippedKeychains;
	}

	public ItemStack equipKeychain(ResourceLocation form, ItemStack stack) {
		//Keychain can be empty stack to unequip
		if (canEquipKeychain(form, stack)) {
			ItemStack previous = getEquippedKeychain(form);
			equippedKeychains.put(form, stack);
			return previous;
		}
		return null;
	}

	public ItemStack getEquippedKeychain(ResourceLocation form) {
		if (equippedKeychains.containsKey(form)) {
			return equippedKeychains.get(form);
		}
		return ItemStack.EMPTY;
	}

	public void equipAllKeychains(Map<ResourceLocation, ItemStack> keychains, boolean force) {
		//Any keychains that cannot be equipped will be removed
		if(!force)
			keychains.replaceAll((k,v) -> canEquipKeychain(k,v) ? v : ItemStack.EMPTY);
		equippedKeychains = keychains;
	}

	public boolean canEquipKeychain(ResourceLocation form, ItemStack stack) {
		if (getEquippedKeychain(form) != null) {
			if (ItemStack.matches(stack, ItemStack.EMPTY) | stack.getItem() instanceof IKeychain) {
				//If there is more than 1 item in the stack don't handle it
                return stack.getCount() <= 1;
			}
		}
		return false;
	}

	public void setNewKeychain(ResourceLocation form, ItemStack stack) {
		if (!equippedKeychains.containsKey(form)) {
			equippedKeychains.put(form, stack);
		}
	}

	//endregion

	//region Items

	public Map<Integer, ItemStack> getEquippedItems() {
		return equippedItems;
	}

	public ItemStack equipItem(int slot, ItemStack stack) {
		//Item can be empty stack to unequip
		if (canEquipItem(slot, stack)) {
			ItemStack previous = getEquippedItem(slot);
			equippedItems.put(slot, stack);
			return previous;
		}
		return null;
	}

	public ItemStack getEquippedItem(int slot) {
		if (equippedItems.containsKey(slot)) {
			return equippedItems.get(slot);
		}
		return null;
	}

	public void equipAllItems(Map<Integer, ItemStack> Items, boolean force) {
		//Any Items that cannot be equipped will be removed
		if(!force)
			Items.replaceAll((k,v) -> canEquipItem(k,v) ? v : ItemStack.EMPTY);
		equippedItems = Items;
	}

	public boolean canEquipItem(int slot, ItemStack stack) {
		if (getEquippedItem(slot) != null) {
			if (ItemStack.matches(stack, ItemStack.EMPTY) || stack.getItem() instanceof KKPotionItem) {
				//If there is more than 1 item in the stack don't handle it
                return stack.getCount() <= 1;
			}
		}
		return false;
	}

	public void setNewItem(int slot, ItemStack stack) {
		if (!equippedItems.containsKey(slot)) {
			equippedItems.put(slot, stack);
		}
	}

	public Queue<ItemStack> getOverflow() {
		return overflow;
	}

	public ItemStack takeNextOverflow() {
		return overflow.poll();
	}

	public ItemStack checkNextOverflow() { return overflow.peek(); }

	public static final int OVERFLOW_CAPACITY = 100;

	public boolean overflowFull() {
		return overflow.size() >= OVERFLOW_CAPACITY;
	}

	public boolean addToOverflow(ItemStack stack) {
		if (!overflowFull()) {
			overflow.add(stack);
			return true;
		} else {
			return false;
		}
	}

	public List<ItemStack> getOverflowForDisplay() {
		return new ArrayList<>(overflow);
	}

	//endregion

	//region Accessories

	public Map<Integer, ItemStack> getEquippedAccessories() {
		return equippedAccessories;
	}

	public ItemStack equipAccessory(int slot, ItemStack stack) {
		//Item can be empty stack to unequip
		if (canEquipAccessory(slot, stack)) {
			ItemStack previous = getEquippedAccessory(slot);
			equippedAccessories.put(slot, stack);
			return previous;
		}
		return null;
	}

	public ItemStack getEquippedAccessory(int slot) {
		if (equippedAccessories.containsKey(slot)) {
			return equippedAccessories.get(slot);
		}
		return null;
	}

	public void equipAllAccessories(Map<Integer, ItemStack> accessories, boolean force) {
		//Any Accessories that cannot be equipped will be removed
		if(!force)
			accessories.replaceAll((k,v) -> canEquipAccessory(k,v) ? v : ItemStack.EMPTY);
		equippedAccessories = accessories;
	}

	public boolean canEquipAccessory(int slot, ItemStack stack) {
		if (getEquippedAccessory(slot) != null) {
			if (ItemStack.matches(stack, ItemStack.EMPTY) || stack.getItem() instanceof KKAccessoryItem) {
				//If there is more than 1 item in the stack don't handle it
                return stack.getCount() <= 1;
			}
		}
		return false;
	}

	public void setNewAccessory(int slot, ItemStack stack) {
		if (!equippedAccessories.containsKey(slot)) {
			equippedAccessories.put(slot, stack);
		}
	}

	//endregion

	//region KBArmor

	public Map<Integer, ItemStack> getEquippedKBArmors() {
		return equippedKBArmors;
	}

	public ItemStack equipKBArmor(int slot, ItemStack stack) {
		//Item can be empty stack to unequip
		if (canEquipKBArmor(slot, stack)) {
			ItemStack previous = getEquippedKBArmor(slot);
			equippedKBArmors.put(slot, stack);
			return previous;
		}
		return null;
	}

	public ItemStack getEquippedKBArmor(int slot) {
		if (equippedKBArmors.containsKey(slot)) {
			return equippedKBArmors.get(slot);
		}
		return null;
	}

	public void equipAllKBArmor(Map<Integer, ItemStack> KBArmors, boolean force) {
		//Any KBArmor that cannot be equipped will be removed
		if(!force)
			KBArmors.replaceAll((k,v) -> canEquipKBArmor(k,v) ? v : ItemStack.EMPTY);
		equippedKBArmors = KBArmors;
	}

	public boolean canEquipKBArmor(int slot, ItemStack stack) {
		if (getEquippedKBArmor(slot) != null) {
			if (ItemStack.matches(stack, ItemStack.EMPTY) || stack.getItem() instanceof PauldronItem) {
				//If there is more than 1 item in the stack don't handle it
                return stack.getCount() <= 1;
			}
		}
		return false;
	}

	public void setNewKBArmor(int slot, ItemStack stack) {
		if (!equippedKBArmors.containsKey(slot)) {
			equippedKBArmors.put(slot, stack);
		}
	}

	public int getNotifColor() {
		// TODO Auto-generated method stub
		return notifColor;
	}

	public void setNotifColor(int color) {
		this.notifColor = color;
	}

	public String getCrown() {
		return crown == null ? "" : crown;
	}

	public void setCrown(String crown) {
		this.crown = crown == null ? "" : crown;
	}

	public Set<String> getUnlockedCrowns() {
		return this.unlockedCrowns;
	}

	public boolean hasUnlockedCrown(String crown) {
		return crown != null && this.unlockedCrowns.contains(crown);
	}

	public boolean unlockCrown(String crown) {
		if (crown == null || crown.isEmpty()) {
			return false;
		}
		return this.unlockedCrowns.add(crown);
	}

	public void setUnlockedCrowns(Collection<String> crowns) {
		this.unlockedCrowns.clear();
		if (crowns != null) {
			this.unlockedCrowns.addAll(crowns);
		}
	}

	public float getCrownOffsetX() {
		return crownOffsetX;
	}

	public float getCrownOffsetY() {
		return crownOffsetY;
	}

	public float getCrownOffsetZ() {
		return crownOffsetZ;
	}

	public float getCrownRotationX() {
		return crownRotationX;
	}

	public float getCrownRotationY() {
		return crownRotationY;
	}

	public float getCrownRotationZ() {
		return crownRotationZ;
	}

	public void setCrownOffset(float x, float y, float z) {
		this.crownOffsetX = x;
		this.crownOffsetY = y;
		this.crownOffsetZ = z;
	}

	public void setCrownRotation(float x, float y, float z) {
		this.crownRotationX = x;
		this.crownRotationY = y;
		this.crownRotationZ = z;
	}

	public int getArmorColor() {
		return armorColor;
	}

	public void setArmorColor(int color) {
		this.armorColor = color;
	}

	public boolean getArmorGlint() {
		return armorGlint;
	}

	public void setArmorGlint(boolean glint) {
		this.armorGlint = glint;
	}

	public Map<Integer, ItemStack> getEquippedArmors() {
		return equippedArmors;
	}
	public ItemStack getEquippedArmor(int slot) {
		if (equippedArmors.containsKey(slot)) {
			return equippedArmors.get(slot);
		}
		return null;
	}

	public ItemStack equipArmor(int slot, ItemStack stack) {
		//Item can be empty stack to unequip
		if (canEquipArmor(slot, stack)) {
			ItemStack previous = getEquippedArmor(slot);
			equippedArmors.put(slot, stack);
			return previous;
		}
		return null;
	}

	public boolean canEquipArmor(int slot, ItemStack stack) {
		if (getEquippedArmor(slot) != null) {
			if (ItemStack.matches(stack, ItemStack.EMPTY) || stack.getItem() instanceof KKArmorItem) {
				//If there is more than 1 item in the stack don't handle it
                return stack.getCount() <= 1;
			}
		}
		return false;
	}

	public void equipAllArmors(Map<Integer, ItemStack> armors, boolean force) {
		//Any Armors that cannot be equipped will be removed
		if(!force)
			armors.replaceAll((k,v) -> canEquipArmor(k,v) ? v : ItemStack.EMPTY);
		equippedArmors = armors;
	}

	public void setNewArmor(int slot, ItemStack stack) {
		if (!equippedArmors.containsKey(slot)) {
			equippedArmors.put(slot, stack);
		}
	}

//Magic equippables
	public Map<Integer, ItemStack> getEquippedMagics() {
		return equippedMagics;
	}

	public ItemStack getEquippedMagic(int slot) {
		if (equippedMagics.containsKey(slot)) {
			return equippedMagics.get(slot);
		}
		return null;
	}

	public ItemStack equipMagic(int slot, ItemStack stack) {
		//Item can be empty stack to unequip
		if (canEquipMagic(slot, stack)) {
			ItemStack previous = getEquippedMagic(slot);
			equippedMagics.put(slot, stack);
			return previous;
		}
		return null;
	}

	public boolean canEquipMagic(int slot, ItemStack stack) {
		if (getEquippedMagic(slot) != null) {
			if (ItemStack.matches(stack, ItemStack.EMPTY) || stack.getItem() instanceof MagicSpellItem) {
				//If there is more than 1 item in the stack don't handle it
				return stack.getCount() <= 1;
			}
		}
		return false;
	}

	public void equipAllMagics(Map<Integer, ItemStack> magics, boolean force) {
		//Any Magics that cannot be equipped will be removed
		if(!force)
			magics.replaceAll((k,v) -> canEquipMagic(k,v) ? v : ItemStack.EMPTY);
		equippedMagics = magics;
	}

	public void setNewMagic(int slot, ItemStack stack) {
		if (!equippedMagics.containsKey(slot)) {
			equippedMagics.put(slot, stack);
		}
	}
	//endregion

	//region Organization

	public int getHearts() {
		return this.hearts;
	}

	public void setHearts(int hearts) {
		this.hearts = Math.max(0, hearts);
	}

	public void addHearts(int hearts) {
		this.hearts = Mth.clamp(this.hearts + hearts, 0, Integer.MAX_VALUE);
	}

	public void removeHearts(int hearts) {
		addHearts(-hearts);
	}

	public Utils.OrgMember getAlignment() {
		return this.alignment;
	}

	public int getAlignmentIndex() {
		return this.alignment.ordinal();
	}

	public void setAlignment(int index) {
		this.alignment = Utils.OrgMember.values()[index];
	}

	public void setAlignment(Utils.OrgMember member) {
		this.alignment = member;
	}

	public boolean isWeaponUnlocked(Item weapon) {
		for (ItemStack stack : weaponUnlocks) {
			if (stack.getItem() == weapon) return true;
		}
		return false;
	}

	public void unlockWeapon(ItemStack weapon) {
		if (!weaponUnlocks.contains(weapon)) {
			this.weaponUnlocks.add(weapon);
		}
	}

	public void unlockWeapon(String registryName) {
		Item weapon = BuiltInRegistries.ITEM.get(KingdomKeys.rl(registryName));
		if (weapon != null) {
			ItemStack weaponUnlock = new ItemStack(weapon);
			if (!weaponUnlocks.contains(weaponUnlock)) {
				this.weaponUnlocks.add(weaponUnlock);
			}
		}
	}

	public ItemStack getEquippedWeapon() {
		return this.equippedWeapon;
	}

	public void equipWeapon(ItemStack weapon) {
		this.equippedWeapon = weapon;
	}

	public Set<ItemStack> getWeaponsUnlocked() {
		return this.weaponUnlocks;
	}

	public void setWeaponsUnlocked(Set<ItemStack> unlocks) {
		this.weaponUnlocks = unlocks;
	}

	public int getLimitCooldownTicks() {
		return limitCooldownTicks;
	}

	public void setLimitCooldownTicks(int ticks) {
		this.limitCooldownTicks = ticks;
	}

	//endregion

	//region Abilities

	public boolean getIsGliding() {
		return isGliding;
	}

	public void setIsGliding(boolean b) {
		this.isGliding = b;
	}

	public int getAerialDodgeTicks() {
		return aerialDodgeTicks;
	}

	public void setAerialDodgeTicks(int ticks) {
		this.aerialDodgeTicks = ticks;
	}

	public boolean hasJumpedAerialDodge() {
		return hasJumpedAerealDodge;
	}

	public void setHasJumpedAerialDodge(boolean b) {
		hasJumpedAerealDodge = b;
	}

	public LinkedHashMap<ResourceLocation, int[]> getAbilityMap() {
		return abilityMap;
	}

	public void setAbilityMap(LinkedHashMap<ResourceLocation, int[]> map) {
		this.abilityMap = map;
	}

	public List<ResourceLocation> getPAbilitiesList() {
		return pAbilitiesList;
	}

	public void setPAbilitiesList(List<ResourceLocation> set) {
		this.pAbilitiesList = set;
	}

	public void addPAbility(ResourceLocation ability) {
		pAbilitiesList.add(ability);
		addAbility(ability,false);
	}

	public void addAbility(ResourceLocation ability, boolean notification) {
		Ability a = ModAbilities.registry.get(ability);
		if(notification) {
			messages.add("A_"+a.getTranslationKey());
		}

		if(abilityMap.containsKey(ability)) {
			abilityMap.put(ability, new int[]{abilityMap.get(ability)[0]+1,abilityMap.get(ability)[1]});
		} else {//If not already present in the map set it to level 1 and fully unequipped
			abilityMap.put(ability, new int[]{1,0});
		}
	}

	public void addAbility(ResourceLocation ability, String displayName, boolean dfLevelUp) {
		if(dfLevelUp) {
			bfMessages.add("A_"+displayName);
		} else {
			messages.add("A_"+displayName);
		}

		if(abilityMap.containsKey(ability)) {
			abilityMap.put(ability, new int[]{abilityMap.get(ability)[0]+1,abilityMap.get(ability)[1]});
		} else {//If not already present in the map set it to level 1 and fully unequipped
			abilityMap.put(ability, new int[]{1,0});
		}
	}

	public void removePAbility(ResourceLocation ability) {
		pAbilitiesList.remove(ability);
	}

	public void removeAbility(ResourceLocation ability) {
		if(abilityMap.containsKey(ability)) {
			if(abilityMap.get(ability)[0] <= 1)
				abilityMap.remove(ability);
			else
				abilityMap.put(ability, new int[] { abilityMap.get(ability)[0] - 1, 0 });
		}
	}

	public int[] getEquippedAbilityLevel(ResourceLocation ability) {
		if(abilityMap.containsKey(ability)) {
			return abilityMap.get(ability);
		}
		return new int[] {0,0};
	}

	public boolean isAbilityEquipped(KKSupplier<Ability> ability) {// First checks for weapon abilities
		return isAbilityEquipped(ability.location());
	}

	public boolean isAbilityEquipped(ResourceLocation ability) {// First checks for weapon abilities
		return getNumberOfAbilitiesEquipped(ability) > 0;
	}

	public int getNumberOfAbilitiesEquipped(KKSupplier<Ability> ability) {
		return getNumberOfAbilitiesEquipped(ability.location());
	}

	public int getNumberOfAbilitiesEquipped(ResourceLocation ability) {
		int amount = 0;
		//First check for keyblades having them
		if (getAlignment() == OrgMember.NONE) {
			if(getEquippedKeychain(DriveForm.NONE) != null && !ItemStack.matches(getEquippedKeychain(DriveForm.NONE), ItemStack.EMPTY)) { // Main keyblade)
				ItemStack stack = getEquippedKeychain(DriveForm.NONE);
				IKeychain weapon = (IKeychain) stack.getItem();
				int level = weapon.toSummon().getKeybladeLevel(stack);
				List<ResourceLocation> abilities = Utils.getKeybladeAbilitiesAtLevel(weapon.toSummon(), level);
				amount += Collections.frequency(abilities, ability);
			}
		} else { //Org members
			if(getEquippedWeapon() != null && !ItemStack.matches(getEquippedWeapon(), ItemStack.EMPTY)) { // Main keyblade)
				if(getEquippedWeapon().getItem() instanceof KeybladeItem) {
					List<ResourceLocation> abilities = Utils.getKeybladeAbilitiesAtLevel(getEquippedWeapon().getItem(), 0);
					amount += Collections.frequency(abilities, ability);
				} else if(getEquippedWeapon().getItem() instanceof IOrgWeapon){ //Org (hopefully)
					ResourceLocation[] abilitiesArray = ((IOrgWeapon)getEquippedWeapon().getItem()).getAbilities();
					if(abilitiesArray != null) {
						List<ResourceLocation> a = Lists.newArrayList(abilitiesArray);
						amount += Collections.frequency(a, ability);
						if(abilityMap.containsKey(ModAbilities.SYNCH_BLADE.location()) && abilityMap.get(ModAbilities.SYNCH_BLADE.location())[1] > 0) { //Org synch blade
							amount *= 2;
						}
					}
				}
			}
		}

		//SB Keyblade if user is base form
		if (isFormActive(ModDriveForms.NONE)) {
			// Check for synch blade ability to be equiped from the abilities menu
			if (abilityMap.containsKey(ModAbilities.SYNCH_BLADE.location()) && abilityMap.get(ModAbilities.SYNCH_BLADE.location())[1] > 0 && !ItemStack.matches(getEquippedKeychain(DriveForm.SYNCH_BLADE), ItemStack.EMPTY)) {
				ItemStack stack = getEquippedKeychain(DriveForm.SYNCH_BLADE);
				IKeychain weapon = (IKeychain) getEquippedKeychain(DriveForm.SYNCH_BLADE).getItem();
				int level = weapon.toSummon().getKeybladeLevel(stack);
				List<ResourceLocation> abilities = Utils.getKeybladeAbilitiesAtLevel(weapon.toSummon(), level);
				amount += Collections.frequency(abilities, ability);
			}
		} else { //DF Keyblades if user is in their form
			ItemStack stack = getEquippedKeychain(getActiveDriveForm());
			if (stack != null && !ItemStack.matches(stack, ItemStack.EMPTY)) {
				IKeychain weapon = (IKeychain) getEquippedKeychain(getActiveDriveForm()).getItem();
				int level = weapon.toSummon().getKeybladeLevel(stack);
				List<ResourceLocation> abilities = Utils.getKeybladeAbilitiesAtLevel(weapon.toSummon(), level);
				amount += Collections.frequency(abilities, ability);
			}

			//Drive form passive abilities
			DriveForm form = ModDriveForms.registry.get(getActiveDriveForm());
			List<ResourceLocation> list = form.getDriveFormData().getAbilities();
			if(list != null && !list.isEmpty()) {
				amount += Collections.frequency(list, ability);
			}
		}

		amount += Collections.frequency(Utils.getAccessoriesAbilities(this), ability);

		if (ModAbilities.registry.get(ability).getType() != AbilityType.GROWTH) {
			return amount + (abilityMap.containsKey(ability) ? Integer.bitCount(abilityMap.get(ability)[1]) : 0);
		} else {
			return abilityMap.containsKey(ability) ? abilityMap.get(ability)[1] : 0;
		}
	}

	public boolean isAbilityEquipped(ResourceLocation ability, int index) {
		int indexConvert = (int) Math.pow(2, index);
		if (abilityMap.containsKey(ability)) {
			if (abilityMap.get(ability)[0] < index) {
				return false;
			} else {
				return (abilityMap.get(ability)[1] & indexConvert) == indexConvert;
			}
		}
		return false;
	}

	public void equipAbilityToggle(ResourceLocation ability, int index) {
		int indexConvert = (int) Math.pow(2, index);
		if (abilityMap.containsKey(ability)) {
			if (abilityMap.get(ability)[0] >= index) {
				abilityMap.get(ability)[1] ^= indexConvert;
			}
		}
	}

	public void equipAbility(ResourceLocation ability, int index) {
		int indexConvert = (int) Math.pow(2, index);
		if (abilityMap.containsKey(ability)) {
			if (abilityMap.get(ability)[0] >= index) {
				abilityMap.get(ability)[1] |= indexConvert;
			}
		}
	}

	public void unequipAbility(ResourceLocation ability, int index) {
		int indexConvert = (int) Math.pow(2, index);
		if (abilityMap.containsKey(ability)) {
			if (abilityMap.get(ability)[0] >= indexConvert) {
				abilityMap.get(ability)[1] &= (~indexConvert);
			}
		}
	}

	public void addEquippedAbilityLevel(ResourceLocation ability, int level) {
		abilityMap.put(ability, new int[] {abilityMap.get(ability)[0], abilityMap.get(ability)[1]+level});
	}

	public int getAbilityQuantity(ResourceLocation ability) {
		if (ModAbilities.registry.get(ability).getType() != AbilityType.GROWTH) {
			return abilityMap.get(ability)[0]+1;
		} else {
			return 1;
		}
	}

	public void clearAbilities() {
		this.abilityMap.clear();
	}

	//endregion

	//region Parties

	public List<String> getPartiesInvited() {
		return partyList;
	}

	public void setPartiesInvited(List<String> list) {
		this.partyList = list;
	}

	public void addPartiesInvited(String partyName) {
		this.partyList.add(partyName);
	}

	public void removePartiesInvited(String partyName) {
		this.partyList.remove(partyName);
	}

	//endregion

	//region Synthesis, Recipes, Materials

	public void setSynthesisedRecipes(Set<String> synthesisedRecipes) {
		this.synthesisedRecipes = synthesisedRecipes;
	}
	public void addSynthesisedRecipe(String recipe){
		if(RecipeRegistry.getInstance().containsKey(KingdomKeys.rl(recipe)))
			this.synthesisedRecipes.add(recipe);
		else
			KingdomKeys.LOGGER.warn("Recipe does not exist");
	}

	public Set<String> getSynthesisedRecipes(){
		return this.synthesisedRecipes;
	}

	public List<ResourceLocation> getKnownRecipeList() {
		return recipeList;
	}

	public void setKnownRecipeList(List<ResourceLocation> list) {
		this.recipeList = list;
	}

	public boolean hasKnownRecipe(ResourceLocation recipe) {
		return this.recipeList.contains(recipe);
	}

	public void addKnownRecipe(ResourceLocation recipe) {
		if(!recipeList.contains(recipe)) {
			this.recipeList.add(recipe);
			Collections.sort(recipeList);
		}
	}

	public void removeKnownRecipe(ResourceLocation recipe) {
		if(this.recipeList.contains(recipe)) {
			this.recipeList.remove(recipe);
			Collections.sort(recipeList);
		}
	}

	public void clearRecipes(String type) {
		switch(type) {
		case "all":
			this.recipeList.clear();
			break;
		case "keyblade":
			List<ResourceLocation> list = new ArrayList<>();
			for(ResourceLocation rl : recipeList) {
				Recipe r = RecipeRegistry.getInstance().getValue(rl);
				if(r.getType().equals("keyblade")) {
					list.add(rl);
				}
			}
			recipeList.removeAll(list);
			break;
			
		case "item":
			List<ResourceLocation> list2 = new ArrayList<>();
			for(ResourceLocation rl : recipeList) {
				Recipe r = RecipeRegistry.getInstance().getValue(rl);
				if(r.getType().equals("item")) {
					list2.add(rl);
				}
			}
			recipeList.removeAll(list2);
			break;
		}
	}

	public TreeMap<ResourceLocation, Integer> getMaterialMap() {
		return materials;
	}

	public void setMaterialMap(TreeMap<ResourceLocation, Integer> materialMap) {
		this.materials = materialMap;
	}

	public int getMaterialAmount(Item material) {
		ResourceLocation regName = BuiltInRegistries.ITEM.getKey(material);
		if (materials.containsKey(regName)) {
            return materials.get(regName);
		}
		return 0;
	}

	public void addMaterial(Item material, int amount) {
		ResourceLocation regName = BuiltInRegistries.ITEM.getKey(material);
		if (materials.containsKey(regName)) {
			int currAmount = materials.get(regName);
			if (amount <= 0) {
	 			materials.remove(regName);
			} else {
	 			materials.replace(regName, currAmount + amount);
			}
		} else {
			if (amount <= 0) {
	 			materials.remove(regName);
			} else {
	 			materials.put(regName, amount);
			}
		}
	}

	public void setMaterial(Item material, int amount) {
		ResourceLocation regName = BuiltInRegistries.ITEM.getKey(material);
		if (materials.containsKey(regName)) {
	 		if (amount <= 0)
		 		materials.remove(regName);
	 		else
	 			materials.replace(regName, amount);
		} else {
	 		if (amount <= 0)
		 		materials.remove(regName);
	 		else
		 		materials.put(regName, amount);
		}
	}

	public void removeMaterial(Item material, int amount) {
		ResourceLocation regName = BuiltInRegistries.ITEM.getKey(material);
		if (materials.containsKey(regName)) {
			int currAmount = materials.get(regName);
			if (amount > currAmount)
				amount = currAmount;
			materials.replace(regName, currAmount - amount);
			if (materials.get(regName) <= 0) {
				materials.remove(regName);
			}
		}
	}

	public void clearMaterials() {
		this.materials.clear();
	}



	public TreeMap<ResourceLocation, Integer> getTotalMaterialMap() {
		return totalMaterials;
	}

	public void setTotalMaterialMap(TreeMap<ResourceLocation, Integer> materialMap) {
		this.totalMaterials = new TreeMap<>(materialMap);
	}

	public int getTotalMaterialAmount(Item material) {
		ResourceLocation regName = BuiltInRegistries.ITEM.getKey(material);
		if (totalMaterials.containsKey(regName)) {
			return totalMaterials.get(regName);
		}
		return 0;
	}

	public void addTotalMaterial(Item material, int amount) {
		ResourceLocation regName = BuiltInRegistries.ITEM.getKey(material);
		if (totalMaterials.containsKey(regName)) {
			int currAmount = totalMaterials.get(regName);
			if (amount <= 0) {
				totalMaterials.remove(regName);
			} else {
				totalMaterials.replace(regName, currAmount + amount);
			}
		} else {
			if (amount <= 0) {
				totalMaterials.remove(regName);
			} else {
				totalMaterials.put(regName, amount);
			}
		}
	}

	public void setTotalMaterial(Item material, int amount) {
		ResourceLocation regName = BuiltInRegistries.ITEM.getKey(material);
		if (totalMaterials.containsKey(regName)) {
			if (amount <= 0)
				totalMaterials.remove(regName);
			else
				totalMaterials.replace(regName, amount);
		} else {
			if (amount <= 0)
				totalMaterials.remove(regName);
			else
				totalMaterials.put(regName, amount);
		}
	}

	public void removeTotalMaterial(Item material, int amount) {
		ResourceLocation regName = BuiltInRegistries.ITEM.getKey(material);
		if (totalMaterials.containsKey(regName)) {
			int currAmount = totalMaterials.get(regName);
			if (amount > currAmount)
				amount = currAmount;
			totalMaterials.replace(regName, currAmount - amount);
			if (totalMaterials.get(regName) <= 0) {
				totalMaterials.remove(regName);
			}
		}
	}

	public void clearTotalMaterials() {
		this.totalMaterials.clear();
	}


	//endregion

	//region SoA stuff

	public Vec3 getReturnLocation() {
		return this.returnPos;
	}

	public void setReturnLocation(Player playerEntity) {
		setReturnLocation(playerEntity.position());
	}

	public void setReturnLocation(Vec3 location) {
		this.returnPos = location;
	}

	public ResourceKey<Level> getReturnDimension() {
		return this.returnDim;
	}

	public void setReturnDimension(Player playerEntity) {
		setReturnDimension(playerEntity.level().dimension());
	}

	public void setReturnDimension(ResourceKey<Level> type) {
		this.returnDim = type;
	}

	public SoAState getSoAState() {
		return this.soAState;
	}

	public void setSoAState(SoAState state) {
		this.soAState = state;
	}

	public SoAState getChosen() {
		return this.choice;
	}

	public void setChoice(SoAState choice) {
		this.choice = choice;

		//If choice is actually a real choice (warrior, mystic, guardian)
		if(choice.get() > 4) {
			//After choice has been set, set the player's version to the default value from the datapack, as if it's a new player no fixing is needed, and if it's an old player who changed it with command he should have been fixed by it (?)
			//KingdomKeys.LOGGER.debug("[DEBUG] Set choice: "+choice);
			online.kingdomkeys.kingdomkeys.leveling.Level levelData = ModLevels.registry.get(KingdomKeys.rl(choice.getSerializedName()));
			if(levelData == null) {
				//KingdomKeys.LOGGER.debug("[DEBUG] levelData is null, choice "+choice+" does not exist or there was an error");
				return;
			}

			if (getVer() == -1)
				setVer(levelData.getVersion());
		}
	}

	public SoAState getSacrificed() {
		return this.sacrifice;
	}

	public void setSacrifice(SoAState sacrifice) {
		this.sacrifice = sacrifice;
	}

	public BlockPos getChoicePedestal() {
		return choicePedestal;
	}

	public void setChoicePedestal(BlockPos pos) {
		choicePedestal = pos;
	}

	public BlockPos getSacrificePedestal() {
		return sacrificePedestal;
	}

	public void setSacrificePedestal(BlockPos pos) {
		sacrificePedestal = pos;
	}
	//endregion

	//region Shotlock, cast time, reaction commands

	public ItemStack getEquippedShotlock() {
		return equippedShotlock;
	}

	public ItemStack equipShotlock(ItemStack stack) {
		//Item can be empty stack to unequip
		if (canEquipShotlock(stack)) {
			ItemStack previous = getEquippedShotlock();
			equippedShotlock = stack;
			return previous;
		}
		return null;
	}

	public boolean canEquipShotlock(ItemStack stack) {
		if (ItemStack.matches(stack, ItemStack.EMPTY) || stack.getItem() instanceof online.kingdomkeys.kingdomkeys.item.ShotlockItem) {
			//If there is more than 1 item in the stack don't handle it
			return stack.getCount() <= 1;
		}
		return false;
	}

	public void setMagicCasttimeTicks(int ticks) {
		this.magicCasttime = ticks;
	}

	public void remMagicCasttimeTicks(int ticks) {
		this.magicCasttime = Math.max(magicCasttime - ticks, 0);
	}

	public int getMagicCasttimeTicks() {
		return this.magicCasttime;
	}

	public void setMagicCooldownTicks(int ticks) {
		this.magicCooldown = ticks;
	}

	public void remMagicCooldownTicks(int ticks) {
		this.magicCooldown = Math.max(magicCooldown - ticks, 0);
	}

	public int getMagicCooldownTicks() {
		return this.magicCooldown;
	}

	//Individual cooldowns
	public int getMagicCooldownTicks(ResourceLocation magic) {
		if (!Utils.perMagicCooldown()) {
			return this.magicCooldown;
		}
		return magic == null ? 0 : this.magicCooldowns.getOrDefault(magic, 0);
	}

	public void setMagicCooldownTicks(ResourceLocation magic, int ticks) {
		if (magic == null) {
			return;
		}
		if (ticks <= 0) {
			this.magicCooldowns.remove(magic);
		} else {
			this.magicCooldowns.put(magic, ticks);
		}
	}

	public Map<ResourceLocation, Integer> getMagicCooldowns() {
		return this.magicCooldowns;
	}

	public boolean tickMagicCooldowns() {
		if (this.magicCooldowns.isEmpty()) {
			return false;
		}

		boolean expired = false;
		Iterator<Map.Entry<ResourceLocation, Integer>> it = this.magicCooldowns.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<ResourceLocation, Integer> entry = it.next();
			int remaining = entry.getValue() - 1;

			if (remaining <= 0) {
				it.remove();
				expired = true;
			} else {
				entry.setValue(remaining);
			}
		}

		return expired;
	}

	public LinkedHashMap<ResourceLocation, Integer> getReactionCommands() {
		return reactionMap;
	}

	public void setReactionCommands(LinkedHashMap<ResourceLocation, Integer> list) {
		this.reactionMap = list;
	}

	/**
	 * Ticks every active reaction command.
	 *
	 * <p>A command's tick may add or remove entries, so the keys have to be walked from a snapshot.
	 * The snapshot reuses {@link #reactionTickScratch} instead of allocating a list every tick: this
	 * runs for every player on both sides, twenty times a second. The buffer belongs to this player's
	 * data and is only touched during their own tick, so there is nothing to share.</p>
	 */
	public void tickReactionCommands(Player player) {
		if (this.reactionMap.isEmpty()) {
			return;
		}

		this.reactionTickScratch.clear();
		this.reactionTickScratch.addAll(this.reactionMap.keySet());

		for (int i = 0; i < this.reactionTickScratch.size(); i++) {
			ReactionCommand rc = ModReactionCommands.registry.get(this.reactionTickScratch.get(i));
			if (rc != null) {
				rc.tick(player);
			}
		}
	}

	/**
	 * Adds a command the caller has already resolved and already checked the conditions of, refreshing
	 * its duration if it was there. Returns true only when it was not present before.
	 *
	 * <p>The {@link #addReactionCommand(ResourceLocation, Player)} path costs two registry lookups and
	 * a second {@code conditionsToAppear} call, which the per-tick constant checks were paying for
	 * nothing.</p>
	 */
	public boolean addCheckedReactionCommand(ReactionCommand command) {
		return this.reactionMap.put(command.getRegistryName(), command.getDuration()) == null;
	}

	public boolean addReactionCommand(ResourceLocation command, Player player) {
		ReactionCommand reactionCommand = ModReactionCommands.registry.get(command);
		return addReactionCommand(command,player,reactionCommand.getDuration());
	}

	public boolean addReactionCommand(ResourceLocation command, Player player, int duration) {
		ReactionCommand reactionCommand = ModReactionCommands.registry.get(command);
		if (reactionCommand == null) {
			KingdomKeys.LOGGER.error("Unknown reaction command: " + command);
			return false;
		}

		if(this.reactionMap.containsKey(command)) {
			this.reactionMap.put(command, duration);
			return false;
		} else {
			if(reactionCommand.conditionsToAppear(player, player)) {
				this.reactionMap.put(command, duration);
				return true;
			} else {
				return false;
			}
		}
	}


	public boolean removeReactionCommand(ResourceLocation command) {
		if(this.reactionMap.containsKey(command)) {
			this.reactionMap.remove(command);
			return true;
		} else {
			return false;
		}
	}

	//endregion

	//region Shortcuts

	public LinkedHashMap<Integer,Integer> getShortcutsMap() {
		return shortcutsMap;
	}

	public void setShortcutsMap(LinkedHashMap<Integer,Integer> map) {
		this.shortcutsMap = map;
	}

	public void changeShortcut(int position, int slot) {
		this.shortcutsMap.put(position, slot);
	}

	public void removeShortcut(int position) {
		this.shortcutsMap.remove(position);
	}

	//endregion

	//region Synth levelling

	public int getSynthLevel() {
		return synthLevel;
	}

	public void setSynthLevel(int level) {
		synthLevel = level;
	}

	public int getSynthExperience() {
		return synthExp;
	}

	public void setSynthExperience(int exp) {
		synthExp = exp;
	}

	public void addSynthExperience(int exp) {
		/*this.synthExp = exp;
		this.synthLevel=1;*/
		if (this.synthLevel < 7) {
			this.synthExp += exp;
			while (this.getSynthExpNeeded(this.getSynthLevel(), this.synthExp) <= 0 && this.getSynthLevel() <= 7) {
				setSynthLevel(this.getSynthLevel() + 1);
			}
		}
	}

	public int getSynthExpNeeded(int level, int currentExp) {
		if (level > 7)
			return 0;
		double nextLevel = (level + 300.0 * (Math.pow(2.0, (level / 8.0)))) * (level * 0.25);
		remainingSynthExp = ((int) nextLevel - currentExp);
		return remainingSynthExp;
	}

	//endregion

	//region ROD

	public boolean getRespawnROD() {
		return respawnROD;
	}

	public void setRespawnROD(boolean respawn) {
		this.respawnROD = respawn;
	}

	//endregion

	//region EFM styles

	public SingleChoices getSingleStyle() {
		return singleStyle;
	}

	public void setSingleStyle(SingleChoices singleStyle) {
		this.singleStyle = singleStyle;
	}

	public DualChoices getDualStyle() {
		return dualStyle;
	}

	public void setDualStyle(DualChoices dualStyle) {
		this.dualStyle = dualStyle;
	}

	public int getMaxAccessories() {
		return maxAccessories;
	}

	public void setMaxAccessories(int num) {
		this.maxAccessories = num;
	}

	public void addMaxAccessories(int num) {
		this.maxAccessories += num;
		messages.add("C_"+Strings.Stats_LevelUp_MaxAccessories);
	}

	public int getMaxArmors() {
		return maxArmors;
	}

	public void setMaxArmors(int num) {
		this.maxArmors = num;
	}

	public void addMaxArmors(int num) {
		this.maxArmors += num;
		messages.add("R_"+Strings.Stats_LevelUp_MaxArmors);
	}

	public int getMaxMagics() {
		return maxMagics;
	}

	public void setMaxMagics(int num) {
		this.maxMagics = num;
	}

	public void addMaxMagics(int num) {
		this.maxMagics += num;
		messages.add("M_"+Strings.Stats_LevelUp_MaxMagics);
	}
	//endregion

	public void setCastedMagic(Utils.castMagic castMagic) {
		this.castMagic = castMagic;

		if(castMagic != null) //If null it means we removing the magic so it doesnt fire more times, we don't need to set the casttime and crash in the attempt
			this.magicCasttime = castMagic.magic().getCasttimeTicks();
	}

	public castMagic getCastedMagic() {
		return castMagic;
	}

	public BlockPos getAirStep() {
		return airStepPos;
	}

	public void setAirStep(BlockPos pos) {
		this.airStepPos = pos;
	}

	public Map<UUID, Instant> discoveredSavePoints() {
		return discoveredSavePoints;
	}

	public void addDiscoveredSavePoint(UUID id, Instant time) {
		discoveredSavePoints.put(id, time);
	}

	public void setDiscoveredSavePoints(Map<UUID, Instant> list) {
		discoveredSavePoints = list;
	}

	//region Flowmotion

	public void setHangingWallTicks(int ticks) {
		this.hangingWallTicks = ticks;
	}

	public int getHangingInWallTicks() {
		return hangingWallTicks;
	}
	public void setWallGrabs(int grabs){
		this.wallGrabs = grabs;
	}
	public int getWallGrabs(){
		return wallGrabs;
	}

	public void setAirDashed(boolean airDashed) {
		this.airDashed = airDashed;
	}
	public boolean hasAirDashed() {
		return airDashed;
	}

	public void setBounced(boolean bounced) {
		this.bounced = bounced;
	}
	public boolean hasBounced() {
		return bounced;
	}

	public void setFlowmotion(boolean flowmotion) {
		this.flowmotion = flowmotion;
	}
	public boolean inFlowmotion() {
		return flowmotion;
	}

	//endregion

	public int getTutorialFlags(){
		return tutorialFlags;
	}
	public void setTutorialFlags(int tutorialFlags) {
		this.tutorialFlags = tutorialFlags;
	}
	public boolean hasSeenTutorial(int id) {
		return (tutorialFlags & (1 << id)) != 0;
	}
	public void setSeenTutorial(int id) {
		this.tutorialFlags = tutorialFlags | (1 << id);
	}
}