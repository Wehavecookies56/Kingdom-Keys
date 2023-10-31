package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.DualChoices;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.SingleChoices;
import online.kingdomkeys.kingdomkeys.leveling.Stat;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.*;

public interface IPlayerCapabilities extends INBTSerializable<CompoundTag> {
	int getLevel();
	void setLevel(int level);
	
	int getExperience();
	void setExperience(int exp);
	void addExperience(Player player, int exp, boolean shareXP, boolean sound);

	int getExperienceGiven();
	void setExperienceGiven(int exp);
	
	int getStrength(boolean combined);
	void setStrength(int str);
	void addStrength(int str);
	Stat getStrengthStat();
	void setStrengthStat(Stat stat);
	
	int getMagic(boolean combined);
	void setMagic(int mag);
	void addMagic(int mag);
	Stat getMagicStat();
	void setMagicStat(Stat stat);

	int getDefense(boolean combined);
	void setDefense(int def);
	void addDefense(int def);
	Stat getDefenseStat();
	void setDefenseStat(Stat stat);

	int getMaxHP();
	void setMaxHP(int hp);
	void addMaxHP(int hp);
	
	double getMP();
	void setMP(double mP);
	void addMP(double mp);
	void remMP(double cost);
	
	double getMaxMP();
	void setMaxMP(double maxMP);
	void addMaxMP(double mp);
	
	double getFocus();
	void setFocus(double focus);
	void addFocus(double focus);
	void remFocus(double cost);
	
	double getMaxFocus();
	void setMaxFocus(double maxMP);
	void addMaxFocus(double mp);
	
	void setShotlockEnemies(List<Integer> list);
	List<Integer> getShotlockEnemies();
	void addShotlockEnemy(Integer entity);
	boolean hasShotMaxShotlock();
	void setHasShotMaxShotlock(boolean val);
	
	double getDP();
	void setDP(double dP);
	void addDP(double dp);
	void remDP(double cost);
	
	double getFP();
	void setFP(double fp);
	void addFP(double fp);
	void remFP(double cost);
	
	double getMaxDP();
	void setMaxDP(double dP);
		
	int getMaxAP(boolean combined);
	void setMaxAP(int ap);
	void addMaxAP(int ap);
	Stat getMaxAPStat();
	void setMaxAPStat(Stat stat);

	
    void levelUpStatsAndDisplayMessage(Player player, boolean sound);
    void clearMessages();
	void setMessages(List<String> messages);
    List<String> getMessages();
    
	int getExpNeeded(int level, int currentExp);
	
	void setActiveDriveForm(String form);
	String getActiveDriveForm();
	
	void setMagicCooldownTicks(int ticks);
	void remMagicCooldownTicks(int ticks);
	int getMagicCooldownTicks();
	
	int getReflectLevel();
	void setReflectLevel(int level);
	void setReflectTicks(int ticks, int level);
	void remReflectTicks(int ticks);
	int getReflectTicks();
	void setReflectActive(boolean active);
	boolean getReflectActive();
	
	void setRecharge(boolean b);
	boolean getRecharge();
	
	void setMunny(int amount);
	int getMunny();
	
	void displayDriveFormLevelUpMessage(Player player, String driveForm);
    void clearDFMessages();
	void setDFMessages(List<String> messages);
	List<String> getDFMessages();
	
	LinkedHashMap<String, int[]> getDriveFormMap();
	void setDriveFormMap(LinkedHashMap<String,int[]> map);
	int getDriveFormLevel(String name);
	void setDriveFormLevel(String name, int level);
    int getDriveFormExp(String drive);
    void setDriveFormExp(Player player, String drive, int exp);
	void addDriveFormExperience(String form, ServerPlayer player, int driveExpNeeded);

	//Key: drive form registry key, Value: the keychain stack
    Map<ResourceLocation, ItemStack> getEquippedKeychains();
    //Return previously equipped ItemStack, returns null if the keychain was not equipped
	ItemStack equipKeychain(ResourceLocation form, ItemStack stack);
	//Returns null if the form does not exist in the map, does not return null if the slot is empty
	ItemStack getEquippedKeychain(ResourceLocation form);
	void equipAllKeychains(Map<ResourceLocation, ItemStack> keychains, boolean force);
	boolean canEquipKeychain(ResourceLocation form, ItemStack stack);
	void setNewKeychain(ResourceLocation form, ItemStack stack);

	Map<Integer, ItemStack> getEquippedItems();
	ItemStack equipItem(int slot, ItemStack stack);
	ItemStack getEquippedItem(int slot);
	void equipAllItems(Map<Integer, ItemStack> Items, boolean force);
	boolean canEquipItem(int slot, ItemStack stack);
	void setNewItem(int slot, ItemStack stack);
	
	Map<Integer, ItemStack> getEquippedAccessories();
	ItemStack equipAccessory(int slot, ItemStack stack);
	ItemStack getEquippedAccessory(int slot);
	void equipAllAccessories(Map<Integer, ItemStack> accessories, boolean force);
	boolean canEquipAccessory(int slot, ItemStack stack);
	void setNewAccessory(int slot, ItemStack stack);
	
	Map<Integer, ItemStack> getEquippedKBArmors();
	ItemStack equipKBArmor(int slot, ItemStack stack);
	ItemStack getEquippedKBArmor(int slot);
	void equipAllKBArmor(Map<Integer, ItemStack> KBArmories, boolean force);
	boolean canEquipKBArmor(int slot, ItemStack stack);
	void setNewKBArmor(int slot, ItemStack stack);
	
	int getArmorColor();
	void setArmorColor(int color);
	boolean getArmorGlint();
	void setArmorGlint(boolean armorGlint);

	Map<Integer, ItemStack> getEquippedArmors();
	ItemStack equipArmor(int slot, ItemStack stack);
	ItemStack getEquippedArmor(int slot);
	boolean canEquipArmor(int slot, ItemStack stack);
	void equipAllArmors(Map<Integer, ItemStack> armors, boolean force);
	void setNewArmor(int slot, ItemStack stack);

	LinkedHashMap<String, int[]> getMagicsMap();
	void setMagicsMap(LinkedHashMap<String,int[]> map);
	int getMagicLevel(ResourceLocation name);
	void setMagicLevel(ResourceLocation name, int level, boolean notification);
	int getMagicUses(ResourceLocation name);
	void setMagicUses(ResourceLocation name, int uses);
	void addMagicUses(ResourceLocation name, int uses);
	void remMagicUses(ResourceLocation name, int uses);
	
	List<String> getShotlockList();
	void setShotlockList(List<String> list);
	void addShotlockToList(String shotlock, boolean notification);
	void removeShotlockFromList(String shotlock);
	
	String getEquippedShotlock();
	void setEquippedShotlock(String shotlock);

	
	LinkedHashMap<String, int[]> getAbilityMap();
	void setAbilityMap(LinkedHashMap<String,int[]> map);
	void addAbility(String ability, boolean notification);
	void removeAbility(String ability);
	int[] getEquippedAbilityLevel(String string); 
	void addEquippedAbilityLevel(String ability, int level);
	boolean isAbilityEquipped(String string);
	boolean isAbilityEquipped(String ability, int index);
	void clearAbilities();
	void equipAbilityToggle(String ability, int index);
	void equipAbility(String ability, int index);
	void unequipAbility(String ability, int index);
	int getAbilityQuantity(String ability);
	int getNumberOfAbilitiesEquipped(String ability);

	int getAntiPoints();
	void setAntiPoints(int points);
	
	//Drive forms
	boolean getIsGliding();
	void setIsGliding(boolean b);
	
	int getAerialDodgeTicks();
	void setAerialDodgeTicks(int ticks);
	boolean hasJumpedAerialDodge();
	void setHasJumpedAerialDodge(boolean b);
	
    List<String> getPartiesInvited();
    void setPartiesInvited(List<String> list);
    void addPartiesInvited(String partyName);
    void removePartiesInvited(String partyName);
	
    List<ResourceLocation> getKnownRecipeList();
    boolean hasKnownRecipe(ResourceLocation recipe);
    void setKnownRecipeList(List<ResourceLocation> list);
	void addKnownRecipe(ResourceLocation recipe);
	void removeKnownRecipe(ResourceLocation recipe);
	void clearRecipes(String type);
	
	TreeMap<String, Integer> getMaterialMap();
	void setMaterialMap(TreeMap<String, Integer> materialsMap);
	int getMaterialAmount(Material material);
	void addMaterial(Material material, int amount);
	void setMaterial(Material material, int amount);
	void removeMaterial(Material material, int amount);
	void clearMaterials();

	//SoA choices

	Vec3 getReturnLocation();
	void setReturnLocation(Player playerEntity);
	void setReturnLocation(Vec3 location);
	ResourceKey<Level> getReturnDimension();
	void setReturnDimension(Player playerEntity);
	void setReturnDimension(ResourceKey<Level> type);

	//The current state of the SoA
	SoAState getSoAState();
	void setSoAState(SoAState state);
	SoAState getChosen();
	void setChoice(SoAState choice);
	SoAState getSacrificed();
	void setSacrifice(SoAState sacrifice);

	//In case player leaves inbetween choices
	BlockPos getChoicePedestal();
	void setChoicePedestal(BlockPos pos);
	BlockPos getSacrificePedestal();
	void setSacrificePedestal(BlockPos pos);

	int getHearts();
	void setHearts(int hearts);
	void addHearts(int hearts);
	void removeHearts(int hearts);

	Utils.OrgMember getAlignment();
	int getAlignmentIndex();
	void setAlignment(Utils.OrgMember member);
	void setAlignment(int index);
	boolean isWeaponUnlocked(Item weapon);
	void unlockWeapon(ItemStack weapon);
	void unlockWeapon(String registryName);
	ItemStack getEquippedWeapon();
	void equipWeapon(ItemStack weapon);
	void equipWeapon(String registryName);
	Set<ItemStack> getWeaponsUnlocked();
	void setWeaponsUnlocked(Set<ItemStack> unlocks);
	int getLimitCooldownTicks();
	void setLimitCooldownTicks(int ticks);
	
	List<String> getReactionCommands();
	void setReactionCommands(List<String> list);
	boolean addReactionCommand(String command, Player player);
	boolean removeReactionCommand(String command);
	
	LinkedHashMap<Integer,String> getShortcutsMap();
	void setShortcutsMap(LinkedHashMap<Integer,String> map);
	void changeShortcut(int position, String name, int level);
	void removeShortcut(int position);
	
	int getSynthLevel();
	void setSynthLevel(int level);
	
	int getSynthExperience();
	void setSynthExperience(int exp);
	void addSynthExperience(int exp);

	public SingleChoices getSingleStyle();
	public void setSingleStyle(SingleChoices singleStyle) ;
	public DualChoices getDualStyle();
	public void setDualStyle(DualChoices dualStyle);

	boolean getRespawnROD();
	void setRespawnROD(boolean respawn);
}
