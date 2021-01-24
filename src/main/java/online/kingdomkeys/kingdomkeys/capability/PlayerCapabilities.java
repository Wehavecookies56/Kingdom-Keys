package online.kingdomkeys.kingdomkeys.capability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.Ability.AbilityType;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.LevelStats;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.PortalData;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowOverlayPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class PlayerCapabilities implements IPlayerCapabilities {

	private int level = 1, exp = 0, expGiven = 0, strength = 1, magic = 1, defense = 1, maxHp = 20, remainingExp = 0, maxAP = 10, aeroTicks = 0, reflectTicks = 0, munny = 0, antipoints = 0, aerialDodgeTicks;

	private String driveForm = DriveForm.NONE.toString();
	LinkedHashMap<String, int[]> driveForms = new LinkedHashMap<>(); //Key = name, value=  {level, experience}
	List<String> magicList = new ArrayList<>();
	List<ResourceLocation> recipeList = new ArrayList<>();
	LinkedHashMap<String, int[]> abilityMap = new LinkedHashMap<>(); //Key = name, value = {level, equipped},
    private TreeMap<String, Integer> materials = new TreeMap<>();

	List<String> partyList = new ArrayList<>();

	private double mp = 0, maxMP = 0, dp = 0, maxDP = 1000, fp = 0;

	private boolean recharge, reflectActive, isGliding, hasJumpedAerealDodge = false;

	private Vector3d returnPos = Vector3d.ZERO;
	private RegistryKey<World> returnDim = World.OVERWORLD;

	SoAState soAState = SoAState.NONE, choice = SoAState.NONE, sacrifice = SoAState.NONE;

	private BlockPos choicePedestal = new BlockPos(0, 0, 0), sacrificePedestal = new BlockPos(0, 0, 0);

	private List<String> messages = new ArrayList<>();
	private List<String> dfMessages = new ArrayList<>();

	private UUID[] orgPortalUUIDs = { new UUID(0, 0), new UUID(0, 0), new UUID(0, 0) };

	private Utils.OrgMember alignment = Utils.OrgMember.NONE;
	private int hearts = 0;
	private Set<ItemStack> weaponUnlocks = new HashSet<>();
	//Change this back to null when unlock system done
	private ItemStack equippedWeapon = ItemStack.EMPTY;

	private Map<ResourceLocation, ItemStack> equippedKeychains = new HashMap<>();

	//region Main stats, level, exp, str, mag, ap
	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public int getExperience() {
		return exp;
	}

	@Override
	public void setExperience(int exp) {
		this.exp = exp;
	}

	@Override
	public void addExperience(PlayerEntity player, int exp, boolean shareXP) {
		if (player != null) {
			if (this.level < 100) {
				Party party = ModCapabilities.getWorld(player.world).getPartyFromMember(player.getUniqueID());
				if(party != null && shareXP) { //If player is in a party and first to get EXP
					double sharedXP = (exp * ((ModConfigs.partyXPShare / 100F) * 2F)); // exp * share% * 2 (2 being to apply the formula from the 2 player party as mentioned in the config)
					//sharedXP /= party.getMembers().size(); //Divide by the total amount of party players

					for(Member member : party.getMembers()) {
						for(RegistryKey<World> worldKey : player.world.getServer().func_240770_D_()) {
							PlayerEntity ally = player.getServer().getWorld(worldKey).getPlayerByUuid(member.getUUID());
							if(ally != null && ally != player) { //If the ally is not this player give him exp (he will already get the full exp)
								ModCapabilities.getPlayer(ally).addExperience(ally, (int) sharedXP, false); //Give EXP to other players with the false param to prevent getting in a loop
								PacketHandler.sendTo(new SCSyncCapabilityPacket(ModCapabilities.getPlayer(ally)), (ServerPlayerEntity)ally);
							}
						}
					}
					
					//Adding the exp here as it will iterate through the party and this user won't get it there
					this.exp += exp;
					
				} else {
					this.exp += exp;
				}
				while (this.getExpNeeded(this.getLevel(), this.exp) <= 0 && this.getLevel() != 100) {
					setLevel(this.getLevel() + 1);
					levelUpStatsAndDisplayMessage(player);
					PacketHandler.sendTo(new SCShowOverlayPacket("levelup"), (ServerPlayerEntity) player);
				}
				PacketHandler.sendTo(new SCShowOverlayPacket("exp"), (ServerPlayerEntity) player);
				//PacketHandler.sendTo(new SCSyncCapabilityPacket(this), (ServerPlayerEntity) player);
			}
		}
	}

	@Override
	public int getExperienceGiven() {
		return expGiven;
	}

	@Override
	public void setExperienceGiven(int exp) {
		this.expGiven = exp;
	}

	@Override
	public int getStrength() {
		return strength;
	}

	@Override
	public void setStrength(int level) {
		this.strength = level;
	}

	@Override
	public int getMagic() {
		return magic;
	}

	@Override
	public void setMagic(int level) {
		this.magic = level;
	}

	@Override
	public int getDefense() {
		return defense;
	}

	@Override
	public void setDefense(int level) {
		this.defense = level;
	}

	@Override
	public int getExpNeeded(int level, int currentExp) {
		if (level == 100)
			return 0;
		double nextLevel = (double) (((level + 1.0) + 300.0 * (Math.pow(2.0, ((level + 1.0) / 7.0)))) * ((level + 1.0) * 0.25));
		int needed = ((int) nextLevel - currentExp);
		this.remainingExp = needed;
		return remainingExp;
	}

	@Override
	public void addStrength(int str) {
		this.strength += str;
		messages.add(Strings.Stats_LevelUp_Str);
	}

	@Override
	public void addMagic(int mag) {
		this.magic += mag;
		messages.add(Strings.Stats_LevelUp_Magic);
	}

	@Override
	public void addDefense(int def) {
		this.defense += def;
		messages.add(Strings.Stats_LevelUp_Def);
	}

	@Override
	public int getMaxHP() {
		return maxHp;
	}

	@Override
	public void setMaxHP(int hp) {
		this.maxHp = hp;
	}

	@Override
	public void addMaxHP(int hp) {
		this.maxHp += hp;
		messages.add(Strings.Stats_LevelUp_HP);
	}

	@Override
	public int getMaxAP() {
		return maxAP;
	}

	@Override
	public void setMaxAP(int ap) {
		this.maxAP = ap;
	}

	@Override
	public void addMaxAP(int ap) {
		this.maxAP += ap;
		messages.add(Strings.Stats_LevelUp_AP);
	}

	//endregion

	//region Level messages including Drive

	@Override
	public List<String> getMessages() {
		return this.messages;
	}

	@Override
	public void clearMessages() {
		this.getMessages().clear();
	}
	
	@Override
	public List<String> getDFMessages() {
		return this.dfMessages;
	}

	@Override
	public void clearDFMessages() {
		this.getDFMessages().clear();
	}
	
	@Override
	public void setDFMessages(List<String> messages) {
		this.dfMessages = messages;
	}

	@Override
	public void levelUpStatsAndDisplayMessage(PlayerEntity player) {
		this.getMessages().clear();
		LevelStats.applyStatsForLevel(this.level, player, this);

		// PacketDispatcher.sendTo(new SyncUnlockedAbilities(ABILITIES),
		// (EntityPlayerMP) player);

		player.world.playSound((PlayerEntity) null, player.getPosition(), ModSounds.levelup.get(), SoundCategory.MASTER, 0.5f, 1.0f);
		player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getMaxHP());
		PacketHandler.sendTo(new SCSyncCapabilityPacket(ModCapabilities.getPlayer(player)), (ServerPlayerEntity) player);
		PacketHandler.syncToAllAround(player, this);

	}

	@Override
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	@Override
	public void displayDriveFormLevelUpMessage(PlayerEntity player, String driveForm) {
		this.getMessages().clear();
		this.getDFMessages().clear();

		dfMessages.add(Strings.Stats_LevelUp_FormGauge);
		DriveForm form = ModDriveForms.registry.getValue(new ResourceLocation(driveForm));
		String driveformAbility = form.getDFAbilityForLevel(getDriveFormLevel(driveForm));
		String baseAbility = form.getBaseAbilityForLevel(getDriveFormLevel(driveForm));

		if(!driveformAbility.equals("")) {
			Ability a = ModAbilities.registry.getValue(new ResourceLocation(driveformAbility));
			String name = a.getTranslationKey();
			if(a.getType() == AbilityType.GROWTH) {
				int level = (getEquippedAbilityLevel(driveformAbility)[0]+2); //+2 Because it's not set yet, it should be +1 if the ability was already upgraded at the time of generating this message
				name = (new StringBuilder(name).insert(name.lastIndexOf('.'), "_"+level)).toString();
			}
			dfMessages.add("A_"+name);
		}

		if(!baseAbility.equals("")) {
			Ability a = ModAbilities.registry.getValue(new ResourceLocation(baseAbility));
			String name = a.getTranslationKey();
			if(a.getType() == AbilityType.GROWTH) {
				name = (new StringBuilder(name).insert(name.lastIndexOf('.'), "_"+(getEquippedAbilityLevel(baseAbility)[0]+1))).toString();
			}
			addAbility(baseAbility,name);
		}

		player.world.playSound((PlayerEntity) null, player.getPosition(), ModSounds.levelup.get(), SoundCategory.MASTER, 0.5f, 1.0f);
		// TODO Actually add abilities and then syncing
		//addAbility(bfAbility);
		// PacketDispatcher.sendTo(new SyncDriveData(player.getCapability(ModCapabilities.DRIVE_STATE, null)), (EntityPlayerMP) player);

		PacketHandler.sendTo(new SCShowOverlayPacket("drivelevelup", driveForm), (ServerPlayerEntity) player);
	}

	//endregion

	//region Drive forms, DP FP
	@Override
	public double getDP() {
		return dp;
	}

	@Override
	public void setDP(double dp) {
		this.dp = dp;
	}

	@Override
	public void addDP(double dp) {
		this.dp = Math.min(this.dp + dp, this.maxDP);
	}

	@Override
	public void remDP(double dp) {
		this.dp -= dp;
	}

	@Override
	public double getMaxDP() {
		return maxDP;
	}

	@Override
	public void setMaxDP(double dp) {
		this.maxDP = Math.min(this.maxDP + dp, 900);
	}

	@Override
	public double getFP() {
		return fp;
	}

	@Override
	public void setFP(double fp) {
		this.fp = fp;
	}

	@Override
	public void addFP(double fp) {
		double max = (200 + Utils.getDriveFormLevel(getDriveFormMap(), getActiveDriveForm()) * 100);
		this.fp = Math.min(this.fp + fp, max);
	}

	@Override
	public void remFP(double cost) {
		this.fp -= fp;
	}

	@Override
	public void setActiveDriveForm(String form) {
		driveForm = form;
	}

	@Override
	public String getActiveDriveForm() {
		return driveForm;
	}

	@Override
	public LinkedHashMap<String, int[]> getDriveFormMap() {
		return driveForms;//Utils.getSortedDriveForms(driveForms);
	}

	@Override
	public void setDriveFormMap(LinkedHashMap<String, int[]> map) {
		this.driveForms = map;
	}

	@Override
	public int getDriveFormLevel(String name) {
		return driveForms.get(name)[0];
	}

	@Override
	public void setDriveFormLevel(String name, int level) {
		DriveForm form = ModDriveForms.registry.getValue(new ResourceLocation(name));
		if(level <= form.getMaxLevel()) {
			int experience = form.getLevelUpCost(level);
			driveForms.put(name, new int[] {level, experience});
		}
	}

	@Override
	public int getDriveFormExp(String name) {
		return driveForms.get(name)[1];
	}

	@Override
	public void setDriveFormExp(PlayerEntity player, String name, int exp) {
		DriveForm form = ModDriveForms.registry.getValue(new ResourceLocation(name));
		int oldLevel = getDriveFormLevel(name);
		int driveLevel = form.getLevelFromExp(exp);
		if(driveLevel <= form.getMaxLevel()) {
			driveForms.put(name, new int[] {driveLevel, exp});
			if(driveLevel > oldLevel) {
				displayDriveFormLevelUpMessage(player, name);
				if(driveLevel == form.getMaxLevel()) {
					setMaxDP(getMaxDP() + 100);
				}
				PacketHandler.sendTo(new SCSyncCapabilityPacket(this), (ServerPlayerEntity)player);
			}
		}
	}

	public void addDriveFormExperience(String drive, ServerPlayerEntity player, int value) {
		DriveForm form = ModDriveForms.registry.getValue(new ResourceLocation(drive));
		int oldLevel = getDriveFormLevel(drive);
		int driveLevel = form.getLevelFromExp(exp+value);

		if(driveLevel <= form.getMaxLevel()) {
			driveForms.put(drive, new int[] {driveLevel, exp+value});
			if(driveLevel > oldLevel) {
				displayDriveFormLevelUpMessage(player, drive);
				if(driveLevel == form.getMaxLevel()) {
					setMaxDP(getMaxDP() + 100);
				}
				PacketHandler.sendTo(new SCSyncCapabilityPacket(this), (ServerPlayerEntity)player);
			}
		}
	}

	@Override
	public int getAntiPoints() {
		return antipoints;
	}

	@Override
	public void setAntiPoints(int points) {
		this.antipoints = points;
	}

	//endregion

	//region Magic, MP

	@Override
	public double getMP() {
		return mp;
	}

	@Override
	public void setMP(double mp) {
		this.mp = mp;
	}

	@Override
	public void addMP(double mp) {
		this.mp = Math.min(this.mp + mp, this.maxMP);
	}

	@Override
	public double getMaxMP() {
		return maxMP;
	}

	@Override
	public void setMaxMP(double mp) {
		this.maxMP = mp;
	}

	@Override
	public void addMaxMP(double mp) {
		this.maxMP += mp;
		messages.add(Strings.Stats_LevelUp_MP);
	}

	@Override
	public void remMP(double amount) {
		// TODO CHEAT MODE
		this.mp = Math.max(this.mp - amount, 0);
	}

	@Override
	public void setRecharge(boolean b) {
		this.recharge = b;
	}

	@Override
	public boolean getRecharge() {
		return this.recharge;
	}
	
	@Override
	public int getAeroTicks() {
		return aeroTicks;
	}

	@Override
	public void setAeroTicks(int i) {
		aeroTicks = i;
	}
	
	@Override
	public void remAeroTicks(int ticks) {
		aeroTicks -= ticks;
	}

	@Override
	public void setReflectTicks(int ticks) {
		reflectTicks = ticks;
	}

	@Override
	public void remReflectTicks(int ticks) {
		reflectTicks -= ticks;
	}

	@Override
	public int getReflectTicks() {
		return reflectTicks;
	}

	@Override
	public void setReflectActive(boolean active) {
		reflectActive = active;
	}

	@Override
	public boolean getReflectActive() {
		return reflectActive;
	}

	@Override
	public List<String> getMagicList() {
		return Utils.getSortedMagics(magicList);
	}

	@Override
	public void setMagicList(List<String> list) {
		this.magicList = list;
	}

	@Override
	public void addMagicToList(String magic) {
		if (!magicList.contains(magic)) {
			magicList.add(magic);
		}
	}

	@Override
	public void removeMagicFromList(String magic) {
		if (magicList.contains(magic)) {
			magicList.remove(magic);
		}
	}

	//endregion

	//region Currencies, munny, hearts

	@Override
	public void setMunny(int amount) {
		this.munny = amount;
	}

	@Override
	public int getMunny() {
		return munny;
	}

	//endregion

	//region Keyblade
	@Override
	public Map<ResourceLocation, ItemStack> getEquippedKeychains() {
		return equippedKeychains;
	}

	@Override
	public ItemStack equipKeychain(ResourceLocation form, ItemStack stack) {
		//Keychain can be empty stack to unequip
		if (canEquipKeychain(form, stack)) {
			ItemStack previous = getEquippedKeychain(form);
			equippedKeychains.put(form, stack);
			return previous;
		}
		return null;
	}

	@Override
	public ItemStack getEquippedKeychain(ResourceLocation form) {
		if (equippedKeychains.containsKey(form)) {
			return equippedKeychains.get(form);
		}
		return null;
	}

	@Override
	public void equipAllKeychains(Map<ResourceLocation, ItemStack> keychains, boolean force) {
		//Any keychains that cannot be equipped will be removed
		if(!force)
			keychains.replaceAll((k,v) -> canEquipKeychain(k,v) ? v : ItemStack.EMPTY);
		equippedKeychains = keychains;
	}

	@Override
	public boolean canEquipKeychain(ResourceLocation form, ItemStack stack) {
		if (getEquippedKeychain(form) != null) {
			if (ItemStack.areItemStacksEqual(stack, ItemStack.EMPTY) | stack.getItem() instanceof IKeychain) {
				//If there is more than 1 item in the stack don't handle it
				if (stack.getCount() <= 1) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void setNewKeychain(ResourceLocation form, ItemStack stack) {
		if (!equippedKeychains.containsKey(form)) {
			equippedKeychains.put(form, stack);
		}
	}

	//endregion

	//region Organization

	@Override
	public UUID getPortalUUIDFromIndex(byte pID) {
		return orgPortalUUIDs[pID];
	}

	@Override
	public void setPortalCoordsUUID(byte pID, UUID uuid) {
		orgPortalUUIDs[pID] = uuid;
	}

	@Override
	public List<UUID> getPortalUUIDList() {
		List<UUID> list = new ArrayList<UUID>();
		for (byte i = 0; i < 3; i++) {
			UUID uuid = getPortalUUIDFromIndex(i);
			if (!(uuid.equals(new UUID(0,0)))) {
				list.add(uuid);
				// System.out.println(i+" Added portal: "+coords.getPID());
			}
		}
		return list;
	}
	
	@Override
	public void setPortalUUIDList(List<UUID> list) {
		for (byte i = 0; i < list.size(); i++) {
			orgPortalUUIDs[i] = list.get(i);
			/*System.out.println(list.get(i).getDimID());
			System.out.println(list.get(i).getX());
			System.out.println(list.get(i).getY());
			System.out.println(list.get(i).getZ());
			System.out.println(list.get(i).getPID());*/
		}
	}
	
	@Override
	public int getHearts() {
		return this.hearts;
	}

	@Override
	public void setHearts(int hearts) {
		this.hearts = Math.max(0, hearts);
	}

	@Override
	public void addHearts(int hearts) {
		this.hearts = MathHelper.clamp(this.hearts + hearts, 0, Integer.MAX_VALUE);
	}

	@Override
	public void removeHearts(int hearts) {
		addHearts(-hearts);
	}

	@Override
	public Utils.OrgMember getAlignment() {
		return this.alignment;
	}

	@Override
	public int getAlignmentIndex() {
		return this.alignment.ordinal();
	}

	@Override
	public void setAlignment(int index) {
		this.alignment = Utils.OrgMember.values()[index];
	}

	@Override
	public void setAlignment(Utils.OrgMember member) {
		this.alignment = member;
	}

	@Override
	public boolean isWeaponUnlocked(Item weapon) {
		for (ItemStack stack : weaponUnlocks) {
			if (stack.getItem() == weapon.getItem()) return true;
		}
		return false;
	}

	@Override
	public void unlockWeapon(ItemStack weapon) {
		if (!weaponUnlocks.contains(weapon)) {
			this.weaponUnlocks.add(weapon);
		}
	}

	@Override
	public void unlockWeapon(String registryName) {
		Item weapon = ForgeRegistries.ITEMS.getValue(new ResourceLocation(registryName));
		if (weapon != null) {
			ItemStack weaponUnlock = new ItemStack(weapon);
			if (!weaponUnlocks.contains(weaponUnlock)) {
				this.weaponUnlocks.add(weaponUnlock);
			}
		}
	}

	@Override
	public ItemStack getEquippedWeapon() {
		return this.equippedWeapon;
	}

	@Override
	public void equipWeapon(ItemStack weapon) {
		this.equippedWeapon = weapon;
	}

	@Override
	public void equipWeapon(String registryName) {
		Item weapon = ForgeRegistries.ITEMS.getValue(new ResourceLocation(registryName));
		if (weapon != null) {
			this.equippedWeapon = new ItemStack(weapon);
		} else {
			this.equippedWeapon = null;
		}
	}

	@Override
	public Set<ItemStack> getWeaponsUnlocked() {
		return this.weaponUnlocks;
	}

	@Override
	public void setWeaponsUnlocked(Set<ItemStack> unlocks) {
		this.weaponUnlocks = unlocks;
	}

	//endregion

	//region Abilities

	@Override
	public boolean getIsGliding() {
		return isGliding;
	}

	@Override
	public void setIsGliding(boolean b) {
		this.isGliding = b;
	}

	@Override
	public int getAerialDodgeTicks() {
		return aerialDodgeTicks;
	}

	@Override
	public void setAerialDodgeTicks(int ticks) {
		this.aerialDodgeTicks = ticks;
	}

	@Override
	public boolean hasJumpedAerialDodge() {
		return hasJumpedAerealDodge;
	}

	@Override
	public void setHasJumpedAerialDodge(boolean b) {
		hasJumpedAerealDodge = b;
	}

	@Override
	public LinkedHashMap<String, int[]> getAbilityMap() {
		return abilityMap;//Utils.getSortedAbilities(abilitiesMap);
	}

	@Override
	public void setAbilityMap(LinkedHashMap<String, int[]> map) {
		this.abilityMap = map;
	}

	@Override
	public void addAbility(String ability, boolean notification) {
		Ability abilityInstance = ModAbilities.registry.getValue(new ResourceLocation(ability));
		if(notification) {
			messages.add("A_"+abilityInstance.getTranslationKey());
		}
		
		if(abilityMap.containsKey(ability)) {
			abilityMap.put(ability, new int[]{abilityMap.get(ability)[0]+1,abilityMap.get(ability)[1]});
		} else {//If not already present in the map set it to level 1 and fully unequipped
			abilityMap.put(ability, new int[]{1,0});
		}
	}
	
	public void addAbility(String ability, String displayName) {
		messages.add("A_"+displayName);
		if(abilityMap.containsKey(ability)) {
			abilityMap.put(ability, new int[]{abilityMap.get(ability)[0]+1,abilityMap.get(ability)[1]});
		} else {//If not already present in the map set it to level 1 and fully unequipped
			abilityMap.put(ability, new int[]{1,0});
		}
	}

	@Override
	public int[] getEquippedAbilityLevel(String string) {
		if(abilityMap.containsKey(string)) {
			return abilityMap.get(string);
		}
		return new int[] {0,0};
	}
	

	@Override
	public boolean isAbilityEquipped(String string) {
		if(abilityMap.containsKey(string)) {
			return abilityMap.get(string)[1] > 0;
		}
		return false;
	}

	@Override
	public void addEquippedAbilityLevel(String ability, int level) {
		//System.out.println(ability+": "+abilityMap.get(ability)[0]+" : "+(abilityMap.get(ability)[1]+level));
		abilityMap.put(ability, new int[] {abilityMap.get(ability)[0], abilityMap.get(ability)[1]+level});
	}
	
	@Override
	public void clearAbilities() {
		this.abilityMap.clear();
	}

	//endregion

	//region Parties

	@Override
	public List<String> getPartiesInvited() {
		return partyList;
	}

	@Override
	public void setPartiesInvited(List<String> list) {
		this.partyList = list;
	}

	@Override
	public void addPartiesInvited(String partyName) {
		this.partyList.add(partyName);
	}

	@Override
	public void removePartiesInvited(String partyName) {
		this.partyList.remove(partyName);
	}

	//endregion

	//region Synthesis, Recipes, Materials

	@Override
	public List<ResourceLocation> getKnownRecipeList() {
		Collections.sort(recipeList);
		return recipeList;
	}

	@Override
	public void setKnownRecipeList(List<ResourceLocation> list) {
		this.recipeList = list;
	}

	@Override
	public boolean hasKnownRecipe(ResourceLocation recipe) {
		return this.recipeList.contains(recipe);
	}

	@Override
	public void addKnownRecipe(ResourceLocation recipe) {
		if(!recipeList.contains(recipe))
			this.recipeList.add(recipe);
	}
	
	@Override
	public void removeKnownRecipe(ResourceLocation recipe) {
		if(this.recipeList.contains(recipe)) {
			this.recipeList.remove(recipe);
		}
	}
	
	@Override
	public void clearRecipes() {
		this.recipeList.clear();
	}
	
	 @Override
     public TreeMap<String, Integer> getMaterialMap() {
         return materials;
     }

	 @Override
	 public void setMaterialMap(TreeMap<String, Integer> materialMap) {
		 this.materials = materialMap;
	 }
	 
     @Override
     public int getMaterialAmount(Material material) {
         if (materials.containsKey(material.getMaterialName())) {
             int currAmount = materials.get(material.getMaterialName());
             return currAmount;
         }
         return 0;
     }

     @Override
     public void addMaterial(Material material, int amount) {
         if (materials.containsKey(material.getMaterialName())) {
             int currAmount = materials.get(material.getMaterialName());
             if (amount <= 0) {
                 materials.remove(material.getMaterialName());
             } else {
                 materials.replace(material.getMaterialName(), currAmount + amount);
             }
         } else {
             if (amount <= 0) {
                 materials.remove(material.getMaterialName());
             } else {
                 materials.put(material.getMaterialName(), amount);
             }
         }
     }

     @Override
     public void setMaterial(Material material, int amount) {
         if (materials.containsKey(material.getMaterialName())) {
             if (amount <= 0)
                 materials.remove(material.getMaterialName());
             else
             materials.replace(material.getMaterialName(), amount);
         } else {
             if (amount <= 0)
                 materials.remove(material.getMaterialName());
             else
                 materials.put(material.getMaterialName(), amount);
         }
     }

     @Override
     public void removeMaterial(Material material, int amount) {
         if (materials.containsKey(material.getMaterialName())) {
             int currAmount = materials.get(material.getMaterialName());
             if (amount > currAmount) 
             	amount = currAmount;
             materials.replace(material.getMaterialName(), currAmount - amount);
         } else
             return;
     }

	@Override
	public void clearMaterials() {
		this.materials.clear();
	}

	//endregion

	//region SoA stuff

	@Override
	public Vector3d getReturnLocation() {
		return this.returnPos;
	}

	@Override
	public void setReturnLocation(PlayerEntity playerEntity) {
		setReturnLocation(playerEntity.getPositionVec());
	}

	@Override
	public void setReturnLocation(Vector3d location) {
		this.returnPos = location;
	}

	@Override
	public RegistryKey<World> getReturnDimension() {
		return this.returnDim;
	}

	@Override
	public void setReturnDimension(PlayerEntity playerEntity) {
		setReturnDimension(playerEntity.world.getDimensionKey());
	}

	@Override
	public void setReturnDimension(RegistryKey<World> type) {
		this.returnDim = type;
	}

	@Override
	public SoAState getSoAState() {
		return this.soAState;
	}

	@Override
	public void setSoAState(SoAState state) {
		this.soAState = state;
	}

	@Override
	public SoAState getChosen() {
		return this.choice;
	}

	@Override
	public void setChoice(SoAState choice) {
		this.choice = choice;
	}

	@Override
	public SoAState getSacrificed() {
		return this.sacrifice;
	}

	@Override
	public void setSacrifice(SoAState sacrifice) {
		this.sacrifice = sacrifice;
	}

	@Override
	public BlockPos getChoicePedestal() {
		return choicePedestal;
	}

	@Override
	public void setChoicePedestal(BlockPos pos) {
		choicePedestal = pos;
	}

	@Override
	public BlockPos getSacrificePedestal() {
		return sacrificePedestal;
	}

	@Override
	public void setSacrificePedestal(BlockPos pos) {
		sacrificePedestal = pos;
	}

	//endregion

}
