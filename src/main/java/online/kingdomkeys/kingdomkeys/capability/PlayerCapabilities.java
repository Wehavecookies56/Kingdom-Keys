package online.kingdomkeys.kingdomkeys.capability;

import java.util.*;
import java.util.Map.Entry;

import com.google.common.collect.Lists;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.Ability.AbilityType;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.advancements.KKLevelUpTrigger;
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.DualChoices;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.SingleChoices;
import online.kingdomkeys.kingdomkeys.item.*;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.leveling.Stat;
import online.kingdomkeys.kingdomkeys.lib.LevelStats;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowOverlayPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.synthesis.material.ModMaterials;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.Recipe;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;
import online.kingdomkeys.kingdomkeys.util.Utils.castMagic;

public class PlayerCapabilities implements IPlayerCapabilities {

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag storage = new CompoundTag();
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
		storage.putString("drive_form", this.getActiveDriveForm());
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
		for (Entry<String, int[]> pair : this.getMagicsMap().entrySet()) {
			magics.putIntArray(pair.getKey(), pair.getValue());
		}
		storage.put("magics", magics);

		CompoundTag shotlocks = new CompoundTag();
		for (String shotlock : this.getShotlockList()) {
			shotlocks.putInt(shotlock, 0);
		}
		storage.put("shotlocks", shotlocks);

		storage.putString("equipped_shotlock", this.getEquippedShotlock());

		CompoundTag forms = new CompoundTag();
		for (Entry<String, int[]> pair : this.getDriveFormMap().entrySet()) {
			forms.putIntArray(pair.getKey(), pair.getValue());
		}
		storage.put("drive_forms", forms);
		
		CompoundTag visibleDriveForms = new CompoundTag();
		for (String visibleForm : this.getVisibleDriveForms()) {
			visibleDriveForms.putString(visibleForm, "");
		}
		storage.put("visible_drive_forms", visibleDriveForms);

		CompoundTag abilities = new CompoundTag();
		for (Entry<String, int[]> pair : this.getAbilityMap().entrySet()) {
			abilities.putIntArray(pair.getKey(), pair.getValue());
		}
		storage.put("abilities", abilities);

		CompoundTag keychains = new CompoundTag();
		this.getEquippedKeychains().forEach((form, chain) -> keychains.put(form.toString(), chain.serializeNBT()));
		storage.put("keychains", keychains);

		CompoundTag items = new CompoundTag();
		this.getEquippedItems().forEach((slot, item) -> items.put(slot.toString(), item.serializeNBT()));
		storage.put("items", items);

		CompoundTag accessories = new CompoundTag();
		this.getEquippedAccessories().forEach((slot, accessory) -> accessories.put(slot.toString(), accessory.serializeNBT()));
		storage.put("accessories", accessories);
		
		CompoundTag kbArmors = new CompoundTag();
		this.getEquippedKBArmors().forEach((slot, kbArmor) -> kbArmors.put(slot.toString(), kbArmor.serializeNBT()));
		storage.put("kbarmors", kbArmors);

		CompoundTag armors = new CompoundTag();
		this.getEquippedArmors().forEach((slot, armor) -> armors.put(slot.toString(), armor.serializeNBT()));
		storage.put("armors", armors);
		
		storage.putInt("max_accessories", this.getMaxAccessories());
		storage.putInt("max_armors", this.getMaxArmors());

		storage.putInt("hearts", this.getHearts());
		storage.putInt("org_alignment", this.getAlignmentIndex());
		storage.put("org_equipped_weapon", this.getEquippedWeapon().serializeNBT());

		CompoundTag unlockedWeapons = new CompoundTag();
		this.getWeaponsUnlocked().forEach(weapon -> unlockedWeapons.put(Utils.getItemRegistryName(weapon.getItem()).toString(), weapon.serializeNBT()));
		storage.put("org_weapons_unlocked", unlockedWeapons);

		CompoundTag parties = new CompoundTag();
		for (int i=0;i<this.getPartiesInvited().size();i++) {
			parties.putInt(this.getPartiesInvited().get(i),i);
		}
		storage.put("parties", parties);

		CompoundTag mats = new CompoundTag();
		for (Entry<String, Integer> pair : this.getMaterialMap().entrySet()) {
			mats.putInt(pair.getKey(), pair.getValue());
			if (mats.getInt(pair.getKey()) == 0 && pair.getKey() != null)
				mats.remove(pair.getKey());
		}
		storage.put("materials", mats);
		storage.putInt("limitCooldownTicks", this.getLimitCooldownTicks());

		CompoundTag shortcuts = new CompoundTag();
		for (Entry<Integer, String> pair : this.getShortcutsMap().entrySet()) {
			shortcuts.putString(pair.getKey().toString(), pair.getValue());
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
		return storage;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		this.setLevel(nbt.getInt("level"));
		this.setExperience(nbt.getInt("experience"));
		this.setExperienceGiven(nbt.getInt("experience_given"));
		strength = Stat.deserializeNBT("strength", nbt);
		if (nbt.contains("boost_strength")) {
			int boost = nbt.getInt("boost_strength");
			nbt.remove("boost_strength");
			strength.addModifier("legacy_boosts", boost, false);
		}
		magic = Stat.deserializeNBT("magic", nbt);
		if (nbt.contains("boost_magic")) {
			int boost = nbt.getInt("boost_magic");
			nbt.remove("boost_magic");
			magic.addModifier("legacy_boosts", boost, false);
		}
		defense = Stat.deserializeNBT("defense", nbt);
		if (nbt.contains("boost_defense")) {
			int boost = nbt.getInt("boost_defense");
			nbt.remove("boost_defense");
			defense.addModifier("legacy_boosts", boost, false);
		}
		maxAP = Stat.deserializeNBT("max_ap", nbt);
		if (nbt.contains("boost_max_ap")) {
			int boost = nbt.getInt("boost_max_ap");
			nbt.remove("boost_max_ap");
			maxAP.addModifier("legacy_boosts", boost, false);
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
		this.setActiveDriveForm(nbt.getString("drive_form"));
		this.setAntiPoints(nbt.getInt("anti_points"));
		this.setReflectTicks(nbt.getInt("reflect_ticks"), ((CompoundTag) nbt).getInt("reflect_level"));
		this.setReflectActive(nbt.getBoolean("reflect_active"));
		this.setMunny(nbt.getInt("munny"));
		this.setSoAState(SoAState.fromByte(nbt.getByte("soa_state")));
		this.setChoice(SoAState.fromByte(nbt.getByte("soa_choice")));
		this.setSacrifice(SoAState.fromByte(nbt.getByte("soa_sacrifice")));
		CompoundTag returnCompound = nbt.getCompound("soa_return_pos");
		this.setReturnLocation(new Vec3(returnCompound.getDouble("x"), returnCompound.getDouble("y"), returnCompound.getDouble("z")));
		this.setReturnDimension(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(nbt.getString("soa_return_dim"))));
		CompoundTag choicePedestal = nbt.getCompound("soa_choice_pedestal");
		this.setChoicePedestal(new BlockPos(choicePedestal.getInt("x"), choicePedestal.getInt("y"), choicePedestal.getInt("z")));
		CompoundTag sacrificePedestal = nbt.getCompound("soa_sacrifice_pedestal");
		this.setSacrificePedestal(new BlockPos(sacrificePedestal.getInt("x"), sacrificePedestal.getInt("y"), sacrificePedestal.getInt("z")));

		for (String key : nbt.getCompound("recipes").getAllKeys()) {
			this.getKnownRecipeList().add(new ResourceLocation(key));
		}
		Collections.sort(recipeList);

		for (String magicName : nbt.getCompound("magics").getAllKeys()) {
			int[] array;
			if (nbt.getCompound("magics").contains(magicName, 99)) {
				KingdomKeys.LOGGER.info("Converting " + magicName + " data");
				array = new int[]{nbt.getCompound("magics").getInt(magicName), 0};
			} else {
				array = nbt.getCompound("magics").getIntArray(magicName);
			}
			if (ModMagic.registry.get().containsKey(new ResourceLocation(magicName))) {
				this.getMagicsMap().put(magicName, array);
			}
		}

		for (String key : nbt.getCompound("shotlocks").getAllKeys()) {
			if (ModShotlocks.registry.get().containsKey(new ResourceLocation(key))) {
				this.getShotlockList().add(key);
			}
		}

		this.setEquippedShotlock(nbt.getString("equipped_shotlock"));

		for (String driveFormName : nbt.getCompound("drive_forms").getAllKeys()) {
			if (ModDriveForms.registry.get().containsKey(new ResourceLocation(driveFormName))) {
				this.getDriveFormMap().put(driveFormName, nbt.getCompound("drive_forms").getIntArray(driveFormName));
			}
		}
		
		for (String driveFormName : nbt.getCompound("visible_drive_forms").getAllKeys()) {
			if (ModDriveForms.registry.get().containsKey(new ResourceLocation(driveFormName))) { //If form exists
				this.getVisibleDriveForms().add(driveFormName);
			}
		}

		for (String abilityName : nbt.getCompound("abilities").getAllKeys()) {
			if (ModAbilities.registry.get().containsKey(new ResourceLocation(abilityName))) {
				this.getAbilityMap().put(abilityName, nbt.getCompound("abilities").getIntArray(abilityName));
			}
		}

		CompoundTag keychainsNBT = nbt.getCompound("keychains");
		keychainsNBT.getAllKeys().forEach((chain) -> this.setNewKeychain(new ResourceLocation(chain), ItemStack.of(keychainsNBT.getCompound(chain))));

		CompoundTag itemsNBT = nbt.getCompound("items");
		itemsNBT.getAllKeys().forEach((slot) -> this.setNewItem(Integer.parseInt(slot), ItemStack.of(itemsNBT.getCompound(slot))));

		CompoundTag accessoriesNBT = nbt.getCompound("accessories");
		accessoriesNBT.getAllKeys().forEach((slot) -> this.setNewAccessory(Integer.parseInt(slot), ItemStack.of(accessoriesNBT.getCompound(slot))));
		
		CompoundTag kbArmorsNBT = nbt.getCompound("kbarmors");
		kbArmorsNBT.getAllKeys().forEach((slot) -> this.setNewKBArmor(Integer.parseInt(slot), ItemStack.of(kbArmorsNBT.getCompound(slot))));

		CompoundTag armorsNBT = nbt.getCompound("armors");
		armorsNBT.getAllKeys().forEach((slot) -> this.setNewArmor(Integer.parseInt(slot), ItemStack.of(armorsNBT.getCompound(slot))));

		this.setMaxAccessories(nbt.getInt("max_accessories"));
		this.setMaxArmors(nbt.getInt("max_armors"));
		
		this.setHearts(nbt.getInt("hearts"));
		this.setAlignment(nbt.getInt("org_alignment"));
		this.equipWeapon(ItemStack.of(nbt.getCompound("org_equipped_weapon")));
		CompoundTag unlocksCompound = nbt.getCompound("org_weapons_unlocked");
		unlocksCompound.getAllKeys().forEach(key -> this.unlockWeapon(ItemStack.of(unlocksCompound.getCompound(key))));

		for (String key : nbt.getCompound("parties").getAllKeys()) {
			this.getPartiesInvited().add(key);
		}

		for (String mat : nbt.getCompound("materials").getAllKeys()) {
			ResourceLocation loc = new ResourceLocation(mat);
			if (ModMaterials.registry.get().containsKey(new ResourceLocation(loc.getNamespace(), Strings.SM_Prefix + loc.getPath()))) {
				this.getMaterialMap().put(mat, nbt.getCompound("materials").getInt(mat));
			}
		}

		this.setLimitCooldownTicks(nbt.getInt("limitCooldownTicks"));

		for (String s : nbt.getCompound("shortcuts").getAllKeys()) {
			int shortcutPos = Integer.parseInt(s);
			this.getShortcutsMap().put(shortcutPos, nbt.getCompound("shortcuts").getString(shortcutPos + ""));
		}
		
		this.setSynthLevel(nbt.getInt("synth_level"));
		this.setSynthExperience(nbt.getInt("synth_exp"));
		String s = nbt.getString("single_style");
		if(!s.equals(""))
			this.setSingleStyle(SingleChoices.valueOf(s));
		s=nbt.getString("dual_style");
		if(!s.equals(""))
			this.setDualStyle(DualChoices.valueOf(s));
		this.setArmorColor(nbt.getInt("armor_color"));
		this.setArmorGlint(nbt.getBoolean("armor_glint"));
		this.setRespawnROD(nbt.getBoolean("respawn_rod"));
		this.setNotifColor(nbt.getInt("notif_color"));
	}

	private int level = 1, exp = 0, expGiven = 0, maxHp = 20, remainingExp = 0, reflectTicks = 0, reflectLevel = 0, magicCasttime = 0, magicCooldown = 0, munny = 0, antipoints = 0, aerialDodgeTicks, synthLevel=1, synthExp, remainingSynthExp = 0;

	Stat strength = new Stat("strength", 1);
	Stat magic = new Stat("magic",1);
	Stat defense = new Stat("defense", 1);
	Stat maxAP = new Stat("max_ap", 0);

	private String driveForm = DriveForm.NONE.toString();
	LinkedHashMap<String, int[]> driveForms = new LinkedHashMap<>(); //Key = name, value=  {level, experience}
	LinkedHashSet<String> visibleDriveforms = new LinkedHashSet<>();
	LinkedHashMap<String, int[]> magicList = new LinkedHashMap<>(); //Key = name, value=  {level, uses_in_combo}
	List<String> shotlockList = new ArrayList<>();
	List<Integer> shotlockEnemies;
	boolean hasShotMaxShotlock = false;
	List<ResourceLocation> recipeList = new ArrayList<>();
	LinkedHashMap<String, int[]> abilityMap = new LinkedHashMap<>(); //Key = name, value = {level, equipped},
    private TreeMap<String, Integer> materials = new TreeMap<>();
    List<String> reactionList = new ArrayList<>();

	List<String> partyList = new ArrayList<>();
	String equippedShotlock = "";
	
	LinkedHashMap<Integer,String> shortcutsMap = new LinkedHashMap<>(); //Key = magic name, value=  {position, level}
	
	private double mp = 0, maxMP = 0, dp = 0, maxDP = 1000, fp = 0, focus = 100, maxFocus = 100;

	private boolean recharge, reflectActive, isGliding, hasJumpedAerealDodge = false;

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
	
	private int maxAccessories = 0;
	private int maxArmors = 0;
	
	private int armorColor = 16777215;
	private boolean armorGlint = true;

	private SingleChoices singleStyle = SingleChoices.SORA;
	private DualChoices dualStyle = DualChoices.KH2_ROXAS_DUAL;

	private boolean respawnROD = false;
	private int notifColor = 16777215;

	//private String armorName = "";
	
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
	public void addExperience(Player player, int exp, boolean shareXP, boolean sound) {
		if (player != null && getSoAState() == SoAState.COMPLETE) {
			if (this.level < 100) {
				Party party = ModCapabilities.getWorld(player.level()).getPartyFromMember(player.getUUID());
				if(party != null && shareXP) { //If player is in a party and first to get EXP
					double sharedXP = (exp * ((ModConfigs.partyXPShare / 100F) * 2F)); // exp * share% * 2 (2 being to apply the formula from the 2 player party as mentioned in the config)
					//sharedXP /= party.getMembers().size(); //Divide by the total amount of party players

					if(sharedXP > 0) {
						for(Member member : party.getMembers()) {
							for(ResourceKey<Level> worldKey : player.level().getServer().levelKeys()) {
								Player ally = player.getServer().getLevel(worldKey).getPlayerByUUID(member.getUUID());
								if(ally != null && ally != player) { //If the ally is not this player give him exp (he will already get the full exp)
									ModCapabilities.getPlayer(ally).addExperience(ally, (int) sharedXP, false, true); //Give EXP to other players with the false param to prevent getting in a loop
									PacketHandler.sendTo(new SCSyncCapabilityPacket(ModCapabilities.getPlayer(ally)), (ServerPlayer)ally);
								}
							}
						}
					}
					
					//Adding the exp here as it will iterate through the party and this user won't get it there
					if(getNumberOfAbilitiesEquipped(Strings.experienceBoost) > 0 && player.getHealth() <= player.getMaxHealth() / 2) {
						exp *= (1 + getNumberOfAbilitiesEquipped(Strings.experienceBoost));
					}
					this.exp += exp;
					
				} else { //Player not in a party or shareXP is false (command)
					if(getNumberOfAbilitiesEquipped(Strings.experienceBoost) > 0 && player.getHealth() <= player.getMaxHealth() / 2 && shareXP) { //if shareXP is false means it gets here because of the command
						exp *= (1 + getNumberOfAbilitiesEquipped(Strings.experienceBoost));
					}
					this.exp += exp;
				}
				while (this.getExpNeeded(this.getLevel(), this.exp) <= 0 && this.getLevel() != 100) {
					setLevel(this.getLevel() + 1);
					if(player instanceof ServerPlayer svPlayer) {
				        KKLevelUpTrigger.TRIGGER_LEVELUP.trigger(svPlayer, this.getLevel());
					}
					levelUpStatsAndDisplayMessage(player, sound);
					PacketHandler.sendTo(new SCShowOverlayPacket("levelup", player.getUUID(), player.getDisplayName().getString(), getLevel(), getNotifColor(), getMessages()), (ServerPlayer) player);
				}
				PacketHandler.sendTo(new SCShowOverlayPacket("exp"), (ServerPlayer) player);
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
	public int getStrength(boolean combined) {
		return combined ? ((ModConfigs.allowBoosts ? strength.getStat() : strength.get()) + Utils.getAccessoriesStat(this, "str") + (getRecharge()? getNumberOfAbilitiesEquipped(Strings.berserkCharge) * 2 : 0) ) * ModConfigs.statsMultiplier.get(0) / 100 : strength.get();
	}

	@Override
	public void setStrength(int level) {
		this.strength.set(level);
	}

	@Override
	public int getMagic(boolean combined) {
		return combined ? ((ModConfigs.allowBoosts ? magic.getStat() : magic.get()) + Utils.getAccessoriesStat(this, "mag")) * ModConfigs.statsMultiplier.get(1) / 100 : magic.get();
	}

	@Override
	public void setMagic(int level) {
		this.magic.set(level);
	}

	@Override
	public int getDefense(boolean combined) {
		return combined ? ((ModConfigs.allowBoosts ? defense.getStat() : defense.get()) + Utils.getArmorsStat(this, "def")) * ModConfigs.statsMultiplier.get(2) / 100 : defense.get();
	}

	@Override
	public void setDefense(int level) {
		this.defense.set(level);
	}

	@Override
	public int getExpNeeded(int level, int currentExp) {
		if (level == 100)
			return 0;
		double nextLevel = (double) ((level + 300.0 * (Math.pow(2.0, (level / 7.0)))) * (level * 0.25));
		this.remainingExp = ((int) nextLevel - currentExp);
		return remainingExp;
	}

	@Override
	public void addStrength(int str) {
		this.strength.add(str);
		messages.add(Strings.Stats_LevelUp_Str);
	}

	@Override
	public Stat getStrengthStat() {
		return strength;
	}

	@Override
	public void setStrengthStat(Stat stat) {
		this.strength = stat;
	}

	@Override
	public void addMagic(int mag) {
		this.magic.add(mag);
		messages.add(Strings.Stats_LevelUp_Magic);
	}

	@Override
	public Stat getMagicStat() {
		return magic;
	}

	@Override
	public void setMagicStat(Stat stat) {
		this.magic = stat;
	}

	@Override
	public void addDefense(int def) {
		this.defense.add(def);
		messages.add(Strings.Stats_LevelUp_Def);
	}

	@Override
	public Stat getDefenseStat() {
		return defense;
	}

	@Override
	public void setDefenseStat(Stat stat) {
		this.defense = stat;
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
	public int getMaxAP(boolean combined) {
		return combined ? (maxAP.getStat() + getAccessoriesAP("ap")) : maxAP.get();
	}
	
	@Override
	public Stat getMaxAPStat() {
		return maxAP;
	}

	@Override
	public void setMaxAPStat(Stat stat) {
		this.maxAP = stat;
	}

	private int getAccessoriesAP(String type) {
		int res = 0;
		for(Entry<Integer, ItemStack> accessory : getEquippedAccessories().entrySet()) {
			if(!ItemStack.matches(accessory.getValue(), ItemStack.EMPTY) && accessory.getValue().getItem() instanceof KKAccessoryItem) {
				KKAccessoryItem a = (KKAccessoryItem) accessory.getValue().getItem();
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
	
	

	@Override
	public void setMaxAP(int ap) {
		this.maxAP.set(ap);
	}

	@Override
	public void addMaxAP(int ap) {
		this.maxAP.add(ap);
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
	public List<String> getBFMessages() {
		return this.bfMessages;
	}

	@Override
	public void clearBFMessages() {
		this.getBFMessages().clear();
	}
	
	@Override
	public void setBFMessages(List<String> messages) {
		this.bfMessages = messages;
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
	public void levelUpStatsAndDisplayMessage(Player player, boolean sound) {
		this.getMessages().clear();
		LevelStats.applyStatsForLevel(this.level, player, this);

		Party party = ModCapabilities.getWorld(player.level()).getPartyFromMember(player.getUUID());
		if(party != null) {
			for(Member member : party.getMembers()) {
				for(ResourceKey<Level> worldKey : player.level().getServer().levelKeys()) {
					Player ally = player.getServer().getLevel(worldKey).getPlayerByUUID(member.getUUID());
					if(ally != null && ally != player) { //If the ally is not this player give him exp (he will already get the full exp)
						PacketHandler.sendTo(new SCShowOverlayPacket("levelup", player.getUUID(), player.getDisplayName().getString(), getLevel(), getNotifColor(), getMessages()), (ServerPlayer) ally);
						PacketHandler.syncToAllAround(player, this);
					}
				}
			}
		}
		
		if(sound)
			player.level().playSound((Player) null, player.position().x(),player.position().y(),player.position().z(), ModSounds.levelup.get(), SoundSource.MASTER, 0.5f, 1.0f);
		player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getMaxHP());
		PacketHandler.sendTo(new SCSyncCapabilityPacket(ModCapabilities.getPlayer(player)), (ServerPlayer) player);
		PacketHandler.syncToAllAround(player, this);

	}

	@Override
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	@Override
	public void displayDriveFormLevelUpMessage(Player player, String driveForm) {
		this.getBFMessages().clear();
		this.getDFMessages().clear();

		dfMessages.add(Strings.Stats_LevelUp_FormGauge);
		DriveForm form = ModDriveForms.registry.get().getValue(new ResourceLocation(driveForm));
		String driveformAbility = form.getDFAbilityForLevel(getDriveFormLevel(driveForm));
		String baseAbility = form.getBaseAbilityForLevel(getDriveFormLevel(driveForm));

		if(!driveformAbility.equals("")) {
			Ability a = ModAbilities.registry.get().getValue(new ResourceLocation(driveformAbility));
			String name = a.getTranslationKey();
			if(a.getType() == AbilityType.GROWTH) {
				int level = (getEquippedAbilityLevel(driveformAbility)[0]+2); //+2 Because it's not set yet, it should be +1 if the ability was already upgraded at the time of generating this message
				name = (new StringBuilder(name).insert(name.lastIndexOf('.'), "_"+level)).toString();
			}
			dfMessages.add("A_"+name);
		}

		if(!baseAbility.equals("")) {
			Ability a = ModAbilities.registry.get().getValue(new ResourceLocation(baseAbility));
			String name = a.getTranslationKey();
			if(a.getType() == AbilityType.GROWTH) {
				name = (new StringBuilder(name).insert(name.lastIndexOf('.'), "_"+(getEquippedAbilityLevel(baseAbility)[0]+1))).toString();
			}
			addAbility(baseAbility,name, true);
		}

		player.level().playSound((Player) null, player.position().x(),player.position().y(),player.position().z(), ModSounds.levelup.get(), SoundSource.MASTER, 0.5f, 1.0f);

		PacketHandler.sendTo(new SCShowOverlayPacket("drivelevelup", driveForm), (ServerPlayer) player);
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
		this.dp = Utils.clamp(this.dp + dp, 0, this.maxDP);
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
		this.maxDP = Math.min(this.maxDP + dp, 1000);
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
	public LinkedHashSet<String> getVisibleDriveForms() {
		return visibleDriveforms;
	}

	@Override
	public void setVisibleDriveForms(LinkedHashSet<String> forms) {
		this.visibleDriveforms = forms;
	}

	@Override
	public void addVisibleDriveForm(String form) {
		if(!visibleDriveforms.contains(form)) {
			this.visibleDriveforms.add(form);
		}
	}

	@Override
	public void remVisibleDriveForm(String form) {
		if(visibleDriveforms.contains(form)) {
			visibleDriveforms.remove(form);
		}
		
	}

	@Override
	public int getDriveFormLevel(String name) {
		return driveForms.containsKey(name) ? driveForms.get(name)[0] : 0;
	}

	@Override
	public void setDriveFormLevel(String name, int level) {
		DriveForm form = ModDriveForms.registry.get().getValue(new ResourceLocation(name));
		if(name.equals(DriveForm.NONE.toString()) || name.equals(DriveForm.SYNCH_BLADE.toString())){
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

	@Override
	public int getDriveFormExp(String name) {
		return driveForms.get(name)[1];
	}

	@Override
	public void setDriveFormExp(Player player, String name, int exp) {
		DriveForm form = ModDriveForms.registry.get().getValue(new ResourceLocation(name));
		int oldLevel = getDriveFormLevel(name);
		int driveLevel = form.getLevelFromExp(exp);
		if(driveLevel <= form.getMaxLevel()) {
			driveForms.put(name, new int[] {driveLevel, exp});
			if(driveLevel > oldLevel) {
				displayDriveFormLevelUpMessage(player, name);
				if(driveLevel == form.getMaxLevel()) {
					setMaxDP(getMaxDP() + 100);
				}
				PacketHandler.sendTo(new SCSyncCapabilityPacket(this), (ServerPlayer)player);
			}
		}
	}

	public void addDriveFormExperience(String drive, ServerPlayer player, int value) {
		DriveForm form = ModDriveForms.registry.get().getValue(new ResourceLocation(drive));
		int oldLevel = getDriveFormLevel(drive);
		int driveLevel = form.getLevelFromExp(exp+value);

		if(driveLevel <= form.getMaxLevel()) {
			driveForms.put(drive, new int[] {driveLevel, exp+value});
			if(driveLevel > oldLevel) {
				displayDriveFormLevelUpMessage(player, drive);
				if(driveLevel == form.getMaxLevel()) {
					setMaxDP(getMaxDP() + 100);
				}
				PacketHandler.sendTo(new SCSyncCapabilityPacket(this), (ServerPlayer)player);
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
		this.mp = Utils.clamp(this.mp + mp, 0, this.maxMP);
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
		setMP(getMaxMP());
		messages.add(Strings.Stats_LevelUp_MP);
	}

	@Override
	public void remMP(double amount) {
		if(isAbilityEquipped(Strings.extraCast)) {
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

	@Override
	public double getFocus() {
		return focus;
	}

	@Override
	public void setFocus(double focus) {
		this.focus = focus;
	}

	@Override
	public void addFocus(double focus) {
		this.focus = Math.min(this.focus+focus, getMaxFocus());
	}

	@Override
	public void remFocus(double cost) {
		this.focus = Math.max(focus-cost, 0);
	}

	@Override
	public double getMaxFocus() {
		return maxFocus;
	}

	@Override
	public void setMaxFocus(double maxFocus) {
		this.maxFocus = maxFocus;
	}

	@Override
	public void addMaxFocus(double focus) {
		this.focus += focus;
	}
	

	@Override
	public void setShotlockEnemies(List<Integer> list) {
		this.shotlockEnemies = list;
	}

	@Override
	public List<Integer> getShotlockEnemies() {
		return shotlockEnemies;
	}

	@Override
	public void addShotlockEnemy(Integer entity) {
		this.shotlockEnemies.add(entity);
	}
	
	@Override
	public boolean hasShotMaxShotlock() {
		return hasShotMaxShotlock;
	}

	@Override
	public void setHasShotMaxShotlock(boolean val) {
		this.hasShotMaxShotlock = val;
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
	public int getReflectLevel() {
		return reflectLevel;
	}

	@Override
	public void setReflectLevel(int level) {
		this.reflectLevel = level;
	}

	@Override
	public void setReflectTicks(int ticks, int level) {
		reflectTicks = ticks;
		reflectLevel = level;
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
	public LinkedHashMap<String, int[]> getMagicsMap() {
		return magicList;
	}

	@Override
	public void setMagicsMap(LinkedHashMap<String, int[]> map) {
		this.magicList = map;
	}
	
	@Override
	public int getMagicLevel(ResourceLocation name) {
		return magicList.containsKey(name.toString()) ? magicList.get(name.toString())[0] : 0;
	}

	@Override
	public void setMagicLevel(ResourceLocation name, int level, boolean notification) {
		Magic magic = ModMagic.registry.get().getValue(name);
		if(level == -1) {
			magicList.remove(name.toString());
		} else {
			if(level <= magic.getMaxLevel()) {
				int uses = magicList.containsKey(name.toString()) ? getMagicUses(name) : 0;
				magicList.put(name.toString(), new int[] {level, uses});
				
				if(notification) {
					messages.add("M_"+magic.getTranslationKey(level));
				}
			}
		}
	}

	@Override
	public int getMagicUses(ResourceLocation name) {
		return magicList.get(name.toString())[1];
	}

	@Override
	public void setMagicUses(ResourceLocation name, int uses) {
		Magic magic = ModMagic.registry.get().getValue(name);
		int level = getMagicLevel(name);
		if(level <= magic.getMaxLevel()) {
			magicList.put(name.toString(), new int[] {level, uses});
		}
	}
	
	@Override
	public void addMagicUses(ResourceLocation name, int uses) {
		setMagicUses(name, getMagicUses(name) + uses);
	}

	@Override
	public void remMagicUses(ResourceLocation name, int uses) {
		setMagicUses(name, getMagicUses(name) - uses);
	}
		
	@Override
	public List<String> getShotlockList() {
		return shotlockList;
	}

	@Override
	public void setShotlockList(List<String> list) {
		this.shotlockList = list;
	}

	@Override
	public void addShotlockToList(String shotlock, boolean notification) {
		Shotlock shotlockthis = ModShotlocks.registry.get().getValue(new ResourceLocation(shotlock));
		if(notification) {
			messages.add("S_"+shotlockthis.getTranslationKey());
		}
		
		if (!shotlockList.contains(shotlock)) {
			shotlockList.add(shotlock);
		}
	}

	@Override
	public void removeShotlockFromList(String shotlock) {
		if (shotlockList.contains(shotlock)) {
			shotlockList.remove(shotlock);
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
			if (ItemStack.matches(stack, ItemStack.EMPTY) | stack.getItem() instanceof IKeychain) {
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

	//region Items
	
	@Override
	public Map<Integer, ItemStack> getEquippedItems() {
		return equippedItems;
	}

	@Override
	public ItemStack equipItem(int slot, ItemStack stack) {
		//Item can be empty stack to unequip
		if (canEquipItem(slot, stack)) {
			ItemStack previous = getEquippedItem(slot);
			equippedItems.put(slot, stack);
			return previous;
		}
		return null;
	}

	@Override
	public ItemStack getEquippedItem(int slot) {
		if (equippedItems.containsKey(slot)) {
			return equippedItems.get(slot);
		}
		return null;
	}

	@Override
	public void equipAllItems(Map<Integer, ItemStack> Items, boolean force) {
		//Any Items that cannot be equipped will be removed
		if(!force)
			Items.replaceAll((k,v) -> canEquipItem(k,v) ? v : ItemStack.EMPTY);
		equippedItems = Items;
	}

	@Override
	public boolean canEquipItem(int slot, ItemStack stack) {
		if (getEquippedItem(slot) != null) {
			if (ItemStack.matches(stack, ItemStack.EMPTY) || stack.getItem() instanceof KKPotionItem) {
				//If there is more than 1 item in the stack don't handle it
				if (stack.getCount() <= 1) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void setNewItem(int slot, ItemStack stack) {
		if (!equippedItems.containsKey(slot)) {
			equippedItems.put(slot, stack);
		}
	}

	//endregion
	
	//region Accessories
	
	@Override
	public Map<Integer, ItemStack> getEquippedAccessories() {
		return equippedAccessories;
	}

	@Override
	public ItemStack equipAccessory(int slot, ItemStack stack) {
		//Item can be empty stack to unequip
		if (canEquipAccessory(slot, stack)) {
			ItemStack previous = getEquippedAccessory(slot);
			equippedAccessories.put(slot, stack);
			return previous;
		}
		return null;
	}

	@Override
	public ItemStack getEquippedAccessory(int slot) {
		if (equippedAccessories.containsKey(slot)) {
			return equippedAccessories.get(slot);
		}
		return null;
	}

	@Override
	public void equipAllAccessories(Map<Integer, ItemStack> accessories, boolean force) {
		//Any Accessories that cannot be equipped will be removed
		if(!force)
			accessories.replaceAll((k,v) -> canEquipAccessory(k,v) ? v : ItemStack.EMPTY);
		equippedAccessories = accessories;
	}

	@Override
	public boolean canEquipAccessory(int slot, ItemStack stack) {
		if (getEquippedAccessory(slot) != null) {
			if (ItemStack.matches(stack, ItemStack.EMPTY) || stack.getItem() instanceof KKAccessoryItem) {
				//If there is more than 1 item in the stack don't handle it
				if (stack.getCount() <= 1) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void setNewAccessory(int slot, ItemStack stack) {
		if (!equippedAccessories.containsKey(slot)) {
			equippedAccessories.put(slot, stack);
		}
	}

	//endregion
	
	//region KBArmor
	
	@Override
	public Map<Integer, ItemStack> getEquippedKBArmors() {
		return equippedKBArmors;
	}

	@Override
	public ItemStack equipKBArmor(int slot, ItemStack stack) {
		//Item can be empty stack to unequip
		if (canEquipKBArmor(slot, stack)) {
			ItemStack previous = getEquippedKBArmor(slot);
			equippedKBArmors.put(slot, stack);
			return previous;
		}
		return null;
	}

	@Override
	public ItemStack getEquippedKBArmor(int slot) {
		if (equippedKBArmors.containsKey(slot)) {
			return equippedKBArmors.get(slot);
		}
		return null;
	}

	@Override
	public void equipAllKBArmor(Map<Integer, ItemStack> KBArmors, boolean force) {
		//Any KBArmor that cannot be equipped will be removed
		if(!force)
			KBArmors.replaceAll((k,v) -> canEquipKBArmor(k,v) ? v : ItemStack.EMPTY);
		equippedKBArmors = KBArmors;
	}

	@Override
	public boolean canEquipKBArmor(int slot, ItemStack stack) {
		if (getEquippedKBArmor(slot) != null) {
			if (ItemStack.matches(stack, ItemStack.EMPTY) || stack.getItem() instanceof PauldronItem) {
				//If there is more than 1 item in the stack don't handle it
				if (stack.getCount() <= 1) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void setNewKBArmor(int slot, ItemStack stack) {
		if (!equippedKBArmors.containsKey(slot)) {
			equippedKBArmors.put(slot, stack);
		}
	}
	
	@Override
	public int getNotifColor() {
		// TODO Auto-generated method stub
		return notifColor;
	}

	@Override
	public void setNotifColor(int color) {
		this.notifColor = color;
	}
	
	@Override
	public int getArmorColor() {
		return armorColor;
	}
	
	@Override
	public void setArmorColor(int color) {
		this.armorColor = color;
	}

	@Override
	public boolean getArmorGlint() {
		return armorGlint;
	}
	
	@Override
	public void setArmorGlint(boolean glint) {
		this.armorGlint = glint;
	}

	@Override
	public Map<Integer, ItemStack> getEquippedArmors() {
		return equippedArmors;
	}
	@Override
	public ItemStack getEquippedArmor(int slot) {
		if (equippedArmors.containsKey(slot)) {
			return equippedArmors.get(slot);
		}
		return null;
	}

	@Override
	public ItemStack equipArmor(int slot, ItemStack stack) {
		//Item can be empty stack to unequip
		if (canEquipArmor(slot, stack)) {
			ItemStack previous = getEquippedArmor(slot);
			equippedArmors.put(slot, stack);
			return previous;
		}
		return null;
	}

	@Override
	public boolean canEquipArmor(int slot, ItemStack stack) {
		if (getEquippedArmor(slot) != null) {
			if (ItemStack.matches(stack, ItemStack.EMPTY) || stack.getItem() instanceof KKArmorItem) {
				//If there is more than 1 item in the stack don't handle it
				if (stack.getCount() <= 1) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void equipAllArmors(Map<Integer, ItemStack> armors, boolean force) {
		//Any Armors that cannot be equipped will be removed
		if(!force)
			armors.replaceAll((k,v) -> canEquipArmor(k,v) ? v : ItemStack.EMPTY);
		equippedArmors = armors;
	}

	@Override
	public void setNewArmor(int slot, ItemStack stack) {
		if (!equippedArmors.containsKey(slot)) {
			equippedArmors.put(slot, stack);
		}
	}
	//endregion

	//region Organization
	
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
		this.hearts = Mth.clamp(this.hearts + hearts, 0, Integer.MAX_VALUE);
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
			if (stack.getItem() == weapon) return true;
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
	public Set<ItemStack> getWeaponsUnlocked() {
		return this.weaponUnlocks;
	}

	@Override
	public void setWeaponsUnlocked(Set<ItemStack> unlocks) {
		this.weaponUnlocks = unlocks;
	}
	
	@Override
	public int getLimitCooldownTicks() {
		return limitCooldownTicks;
	}

	@Override
	public void setLimitCooldownTicks(int ticks) {
		this.limitCooldownTicks = ticks;
	}

	//endregion

	//region Abilities

	@Override
	public boolean getIsGliding() {
		return isGliding; //getEquippedAbilityLevel(Strings.glide)[1] > 0 || isGliding;
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
		Ability abilitythis = ModAbilities.registry.get().getValue(new ResourceLocation(ability));
		if(notification) {
			messages.add("A_"+abilitythis.getTranslationKey());
		}
		
		if(abilityMap.containsKey(ability)) {
			abilityMap.put(ability, new int[]{abilityMap.get(ability)[0]+1,abilityMap.get(ability)[1]});
		} else {//If not already present in the map set it to level 1 and fully unequipped
			abilityMap.put(ability, new int[]{1,0});
		}
	}
	
	public void addAbility(String ability, String displayName, boolean dfLevelUp) {
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

	public void removeAbility(String ability) {
		if(abilityMap.containsKey(ability)) {
			if(abilityMap.get(ability)[0] <= 1)
				abilityMap.remove(ability);
			else
				abilityMap.put(ability, new int[] { abilityMap.get(ability)[0] - 1, 0 });
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
	public boolean isAbilityEquipped(String string) {// First checks for weapon abilities
		return getNumberOfAbilitiesEquipped(string) > 0;
	}
	
	@Override
	public int getNumberOfAbilitiesEquipped(String ability) {
		int amount = 0;
		//First check for keyblades having them
		if (getAlignment() == OrgMember.NONE) {
			if(getEquippedKeychain(DriveForm.NONE) != null && !ItemStack.matches(getEquippedKeychain(DriveForm.NONE), ItemStack.EMPTY)) { // Main keyblade)
				ItemStack stack = getEquippedKeychain(DriveForm.NONE);
				IKeychain weapon = (IKeychain) stack.getItem();
				int level = weapon.toSummon().getKeybladeLevel(stack);
				List<String> abilities = Utils.getKeybladeAbilitiesAtLevel(weapon.toSummon(), level);
				amount += Collections.frequency(abilities, ability);
			}
		} else { //Org members
			if(getEquippedWeapon() != null && !ItemStack.matches(getEquippedWeapon(), ItemStack.EMPTY)) { // Main keyblade)
				if(getEquippedWeapon().getItem() instanceof KeybladeItem) {
					List<String> abilities = Utils.getKeybladeAbilitiesAtLevel(getEquippedWeapon().getItem(), 0);
					amount += Collections.frequency(abilities, ability);
				} else if(getEquippedWeapon().getItem() instanceof IOrgWeapon){ //Org (hopefully)
					String[] abilitiesArray = ((IOrgWeapon)getEquippedWeapon().getItem()).getAbilities();
					if(abilitiesArray != null) {
						List<String> a = Lists.newArrayList(abilitiesArray);
						amount += Collections.frequency(a, ability);
						if(abilityMap.containsKey(Strings.synchBlade) && abilityMap.get(Strings.synchBlade)[1] > 0) { //Org synch blade
							amount *= 2;
						}
					}
				}
			}
		}
		
		//SB Keyblade if user is base form
		if (getActiveDriveForm().equals(DriveForm.NONE.toString())) {
			// Check for synch blade ability to be equiped from the abilities menu
			if (abilityMap.containsKey(Strings.synchBlade) && abilityMap.get(Strings.synchBlade)[1] > 0 && !ItemStack.matches(getEquippedKeychain(DriveForm.SYNCH_BLADE), ItemStack.EMPTY)) {
				ItemStack stack = getEquippedKeychain(DriveForm.SYNCH_BLADE);
				IKeychain weapon = (IKeychain) getEquippedKeychain(DriveForm.SYNCH_BLADE).getItem();
				int level = weapon.toSummon().getKeybladeLevel(stack);
				List<String> abilities = Utils.getKeybladeAbilitiesAtLevel(weapon.toSummon(), level);
				amount += Collections.frequency(abilities, ability);
			}
		} else { //DF Keyblades if user is in their form
			ItemStack stack = getEquippedKeychain(new ResourceLocation(getActiveDriveForm()));
			if (stack != null && !ItemStack.matches(stack, ItemStack.EMPTY)) {
				IKeychain weapon = (IKeychain) getEquippedKeychain(new ResourceLocation(getActiveDriveForm())).getItem();
				int level = weapon.toSummon().getKeybladeLevel(stack);
				List<String> abilities = Utils.getKeybladeAbilitiesAtLevel(weapon.toSummon(), level);
				amount += Collections.frequency(abilities, ability);
			}
			
			//Drive form passive abilities
			DriveForm form = ModDriveForms.registry.get().getValue(new ResourceLocation(getActiveDriveForm()));
			List<String> list = form.getDriveFormData().getAbilities();
			if(list != null && !list.isEmpty()) {
				amount += Collections.frequency(list, ability);
			}
		}
		
		amount += Collections.frequency(Utils.getAccessoriesAbilities(this), ability);
				
		if (ModAbilities.registry.get().getValue(new ResourceLocation(ability)).getType() != AbilityType.GROWTH) {
			return amount + (abilityMap.containsKey(ability) ? Integer.bitCount(abilityMap.get(ability)[1]) : 0);
		} else {
			return abilityMap.containsKey(ability) ? abilityMap.get(ability)[1] : 0;
		}
	}

	@Override
	public boolean isAbilityEquipped(String ability, int index) {
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

	@Override
	public void equipAbilityToggle(String ability, int index) {
		int indexConvert = (int) Math.pow(2, index);
		if (abilityMap.containsKey(ability)) {
			if (abilityMap.get(ability)[0] >= index) {
				abilityMap.get(ability)[1] ^= indexConvert;
			}
		}
	}

	@Override
	public void equipAbility(String ability, int index) {
		int indexConvert = (int) Math.pow(2, index);
		if (abilityMap.containsKey(ability)) {
			if (abilityMap.get(ability)[0] >= index) {
				abilityMap.get(ability)[1] |= indexConvert;
			}
		}
	}

	@Override
	public void unequipAbility(String ability, int index) {
		int indexConvert = (int) Math.pow(2, index);
		if (abilityMap.containsKey(ability)) {
			if (abilityMap.get(ability)[0] >= indexConvert) {
				abilityMap.get(ability)[1] &= (~indexConvert);
			}
		}
	}

	@Override
	public void addEquippedAbilityLevel(String ability, int level) {
		//System.out.println(ability+": "+abilityMap.get(ability)[0]+" : "+(abilityMap.get(ability)[1]+level));
		abilityMap.put(ability, new int[] {abilityMap.get(ability)[0], abilityMap.get(ability)[1]+level});
	}

	@Override
	public int getAbilityQuantity(String ability) {
		if (ModAbilities.registry.get().getValue(new ResourceLocation(ability)).getType() != AbilityType.GROWTH) {
			return abilityMap.get(ability)[0]+1;
		} else {
			return 1;
		}
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
		if(!recipeList.contains(recipe)) {
			this.recipeList.add(recipe);
			Collections.sort(recipeList);
		}
	}
	
	@Override
	public void removeKnownRecipe(ResourceLocation recipe) {
		if(this.recipeList.contains(recipe)) {
			this.recipeList.remove(recipe);
			Collections.sort(recipeList);
		}
	}
	
	@Override
	public void clearRecipes(String type) {
		switch(type) {
		case "all":
			this.recipeList.clear();
			break;
		case "keyblade":
			List<ResourceLocation> list = new ArrayList<ResourceLocation>();
			for(ResourceLocation rl : recipeList) {
				Recipe r = RecipeRegistry.getInstance().getValue(rl);
				if(r.getType().equals("keyblade")) {
					list.add(rl);
				}
			}
			recipeList.removeAll(list);
			break;
			
		case "item":
			List<ResourceLocation> list2 = new ArrayList<ResourceLocation>();
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
	public Vec3 getReturnLocation() {
		return this.returnPos;
	}

	@Override
	public void setReturnLocation(Player playerEntity) {
		setReturnLocation(playerEntity.position());
	}

	@Override
	public void setReturnLocation(Vec3 location) {
		this.returnPos = location;
	}

	@Override
	public ResourceKey<Level> getReturnDimension() {
		return this.returnDim;
	}

	@Override
	public void setReturnDimension(Player playerEntity) {
		setReturnDimension(playerEntity.level().dimension());
	}

	@Override
	public void setReturnDimension(ResourceKey<Level> type) {
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

	@Override
	public String getEquippedShotlock() {
		return equippedShotlock;
	}

	@Override
	public void setEquippedShotlock(String shotlock) {
		this.equippedShotlock = shotlock;
	}

	@Override
	public void setMagicCasttimeTicks(int ticks) {
		this.magicCasttime = ticks;
	}

	@Override
	public void remMagicCasttimeTicks(int ticks) {
		this.magicCasttime = Math.max(magicCasttime - ticks, 0);
	}

	@Override
	public int getMagicCasttimeTicks() {
		return this.magicCasttime;
	}
	
	@Override
	public void setMagicCooldownTicks(int ticks) {
		this.magicCooldown = ticks;
	}

	@Override
	public void remMagicCooldownTicks(int ticks) {
		this.magicCooldown = Math.max(magicCooldown - ticks, 0);
	}

	@Override
	public int getMagicCooldownTicks() {
		return this.magicCooldown;
	}

	@Override
	public List<String> getReactionCommands() {
		return reactionList;
	}

	@Override
	public void setReactionCommands(List<String> list) {
		this.reactionList = list;
		
	}

	@Override
	public boolean addReactionCommand(String command, Player player) {
		if(this.reactionList.contains(command)) {
			return false;
		} else {
			if(ModReactionCommands.registry.get().getValue(new ResourceLocation(command)).conditionsToAppear(player, player)) {
				this.reactionList.add(command);
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public boolean removeReactionCommand(String command) {
		if(this.reactionList.contains(command)) {
			this.reactionList.remove(command);
			return true;
		} else {
			return false;
		}
	}
	//endregion

	//region Shortcuts

	@Override
	public LinkedHashMap<Integer,String> getShortcutsMap() {
		return shortcutsMap;
	}

	@Override
	public void setShortcutsMap(LinkedHashMap<Integer,String> map) {
		this.shortcutsMap = map;
	}
	
	@Override
	public void changeShortcut(int position, String name, int level) {
		this.shortcutsMap.put(position, name+","+level);
	}

	@Override
	public void removeShortcut(int position) {
		this.shortcutsMap.remove(position);
		
	}

	//endregion

	//region Synth levelling

	@Override
	public int getSynthLevel() {
		return synthLevel;
	}

	@Override
	public void setSynthLevel(int level) {
		synthLevel = level;
	}

	@Override
	public int getSynthExperience() {
		return synthExp;
	}

	@Override
	public void setSynthExperience(int exp) {
		synthExp = exp;
	}

	@Override
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
		double nextLevel = (double) ((level + 300.0 * (Math.pow(2.0, (level / 8.0)))) * (level * 0.25));
		remainingSynthExp = ((int) nextLevel - currentExp);
		return remainingSynthExp;
	}

	//endregion

	//region ROD

	@Override
	public boolean getRespawnROD() {
		return respawnROD;
	}

	@Override
	public void setRespawnROD(boolean respawn) {
		this.respawnROD = respawn;
		
	}

	//endregion

	//region EFM styles

	@Override
	public SingleChoices getSingleStyle() {
		return singleStyle;
	}

	@Override
	public void setSingleStyle(SingleChoices singleStyle) {
		this.singleStyle = singleStyle;
	}

	@Override
	public DualChoices getDualStyle() {
		return dualStyle;
	}

	@Override
	public void setDualStyle(DualChoices dualStyle) {
		this.dualStyle = dualStyle;
	}

	@Override
	public int getMaxAccessories() {
		return maxAccessories;
	}

	@Override
	public void setMaxAccessories(int num) {
		this.maxAccessories = num;
	}

	@Override
	public void addMaxAccessories(int num) {
		this.maxAccessories += num;
		messages.add("C_"+Strings.Stats_LevelUp_MaxAccessories);
	}

	@Override
	public int getMaxArmors() {
		return maxArmors;
	}

	@Override
	public void setMaxArmors(int num) {
		this.maxArmors = num;
	}

	@Override
	public void addMaxArmors(int num) {
		this.maxArmors += num;
		messages.add("R_"+Strings.Stats_LevelUp_MaxArmors);
	}
	//endregion
	
	
	Utils.castMagic castMagic = null;
	
	@Override
	public void setCastedMagic(Utils.castMagic castMagic) {
		this.castMagic = castMagic;
		
		if(castMagic != null) //If null it means we removing the magic so it doesnt fire more times, we don't need to set the casttime and crash in the attempt
			this.magicCasttime = castMagic.magic().getCasttimeTicks(castMagic.level());
	}

	@Override
	public castMagic getCastedMagic() {
		return castMagic;
	}

	

}
