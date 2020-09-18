package online.kingdomkeys.kingdomkeys.capability;

import java.util.*;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.Ability.AbilityType;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.PortalData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowOverlayPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;

public class PlayerCapabilities implements IPlayerCapabilities {

	public static class Storage implements IStorage<IPlayerCapabilities> {
		@Override
		public INBT writeNBT(Capability<IPlayerCapabilities> capability, IPlayerCapabilities instance, Direction side) {
			CompoundNBT storage = new CompoundNBT();
			storage.putInt("level", instance.getLevel());
			storage.putInt("experience", instance.getExperience());
			storage.putInt("experience_given", instance.getExperienceGiven());
			storage.putInt("strength", instance.getStrength());
			storage.putInt("magic", instance.getMagic());
			storage.putInt("defense", instance.getDefense());
			storage.putInt("max_hp", instance.getMaxHP());
			storage.putInt("max_ap", instance.getMaxAP());
			storage.putDouble("mp", instance.getMP());
			storage.putDouble("max_mp", instance.getMaxMP());
			storage.putBoolean("recharge", instance.getRecharge());
			storage.putDouble("dp", instance.getDP());
			storage.putDouble("max_dp", instance.getMaxDP());
			storage.putDouble("fp", instance.getFP());
			storage.putString("drive_form", instance.getActiveDriveForm());
			storage.putInt("anti_points", instance.getAntiPoints());
			storage.putInt("aero_ticks", instance.getAeroTicks());
			storage.putInt("reflect_ticks", instance.getReflectTicks());
			storage.putBoolean("reflect_active", instance.getReflectActive());
			storage.putInt("munny", instance.getMunny());
			
			CompoundNBT recipes = new CompoundNBT();
			for (ResourceLocation recipe : instance.getKnownRecipeList()) {
				recipes.putString(recipe.toString(), recipe.toString());
			}
			storage.put("recipes", recipes);

			CompoundNBT magics = new CompoundNBT();
			for (String magic : instance.getMagicList()) {
				magics.putInt(magic, 0);
			}
			storage.put("magics", magics);

			CompoundNBT forms = new CompoundNBT();
			Iterator<Map.Entry<String, int[]>> driveFormsIt = instance.getDriveFormMap().entrySet().iterator();
			while (driveFormsIt.hasNext()) {
				Map.Entry<String, int[]> pair = (Map.Entry<String, int[]>) driveFormsIt.next();
				forms.putIntArray(pair.getKey().toString(), pair.getValue());
			}
			storage.put("drive_forms", forms);
			
			CompoundNBT abilities = new CompoundNBT();
			Iterator<Map.Entry<String, int[]>> abilitiesIt = instance.getAbilityMap().entrySet().iterator();
			while (abilitiesIt.hasNext()) {
				Map.Entry<String, int[]> pair = (Map.Entry<String, int[]>) abilitiesIt.next();
				abilities.putIntArray(pair.getKey().toString(), pair.getValue());
			}
			storage.put("abilities", abilities);

			CompoundNBT keychains = new CompoundNBT();
			instance.getEquippedKeychains().forEach((form, chain) -> keychains.put(form.toString(), chain.serializeNBT()));
			storage.put("keychains", keychains);

			for (byte i = 0; i < 3; i++) {
				storage.putByte("Portal" + i + "N", instance.getPortalCoords(i).getPID());
				storage.putDouble("Portal" + i + "X", instance.getPortalCoords(i).getX());
				storage.putDouble("Portal" + i + "Y", instance.getPortalCoords(i).getY());
				storage.putDouble("Portal" + i + "Z", instance.getPortalCoords(i).getZ());
				storage.putInt("Portal" + i + "D", instance.getPortalCoords(i).getDimID());
			}

			CompoundNBT parties = new CompoundNBT();
			for (int i=0;i<instance.getPartiesInvited().size();i++) {
				parties.putInt(instance.getPartiesInvited().get(i),i);
			}
			storage.put("parties", parties);
			
			CompoundNBT mats = new CompoundNBT();
			Iterator<Map.Entry<String, Integer>> materialsIt = instance.getMaterialMap().entrySet().iterator();
			while (materialsIt.hasNext()) {
				Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>) materialsIt.next();
				mats.putInt(pair.getKey().toString(), pair.getValue());
				if(mats.getInt(pair.getKey()) == 0 && pair.getKey().toString() != null)
					mats.remove(pair.getKey().toString());
			}
			storage.put("materials", mats);

			return storage;
		}

		@Override
		public void readNBT(Capability<IPlayerCapabilities> capability, IPlayerCapabilities instance, Direction side, INBT nbt) {
			CompoundNBT storage = (CompoundNBT) nbt;
			instance.setLevel(storage.getInt("level"));
			instance.setExperience(storage.getInt("experience"));
			instance.setExperienceGiven(storage.getInt("experience_given"));
			instance.setStrength(storage.getInt("strength"));
			instance.setMagic(storage.getInt("magic"));
			instance.setDefense(storage.getInt("defense"));
			instance.setMaxHP(storage.getInt("max_hp"));
			instance.setMaxAP(storage.getInt("max_ap"));
			instance.setMP(storage.getDouble("mp"));
			instance.setMaxMP(storage.getDouble("max_mp"));
			instance.setRecharge(storage.getBoolean("recharge"));
			instance.setDP(storage.getDouble("dp"));
			instance.setMaxDP(storage.getDouble("max_dp"));
			instance.setFP(storage.getDouble("fp"));
			instance.setActiveDriveForm(storage.getString("drive_form"));
			instance.setAntiPoints(storage.getInt("anti_points"));
			instance.setAeroTicks(storage.getInt("aero_ticks"));
			instance.setReflectTicks(storage.getInt("reflect_ticks"));
			instance.setReflectActive(storage.getBoolean("reflect_active"));
			instance.setMunny(storage.getInt("munny"));

			Iterator<String> recipesIt = storage.getCompound("recipes").keySet().iterator();
			while (recipesIt.hasNext()) {
				String key = (String) recipesIt.next();
				instance.getKnownRecipeList().add(new ResourceLocation(key));
			}
			
			Iterator<String> magicIt = storage.getCompound("magics").keySet().iterator();
			while (magicIt.hasNext()) {
				String key = (String) magicIt.next();
				//System.out.println("Read: " + key);
				instance.getMagicList().add(key.toString());
			}

			Iterator<String> driveFormsIt = storage.getCompound("drive_forms").keySet().iterator();
			while (driveFormsIt.hasNext()) {
				String driveFormName = (String) driveFormsIt.next();
				//System.out.println("Read: " + driveFormName);
				instance.getDriveFormMap().put(driveFormName.toString(), storage.getCompound("drive_forms").getIntArray(driveFormName));
			}


			
			Iterator<String> abilitiesIt = storage.getCompound("abilities").keySet().iterator();
			while (abilitiesIt.hasNext()) {
				String abilityName = (String) abilitiesIt.next();
				//System.out.println("Read: " + abilityName);
				instance.getAbilityMap().put(abilityName.toString(), storage.getCompound("abilities").getIntArray(abilityName));
			}

			CompoundNBT keychainsNBT = storage.getCompound("keychains");
			keychainsNBT.keySet().forEach((chain) -> instance.setNewKeychain(new ResourceLocation(chain), ItemStack.read(keychainsNBT.getCompound(chain))));

			for (byte i = 0; i < 3; i++) {
				instance.setPortalCoords(i, new PortalData(storage.getByte("Portal" + i + "N"), storage.getDouble("Portal" + i + "X"), storage.getDouble("Portal" + i + "Y"), storage.getDouble("Portal" + i + "Z"), storage.getInt("Portal" + i + "D")));
			}
			
			Iterator<String> partyIt = storage.getCompound("parties").keySet().iterator();
			while (partyIt.hasNext()) {
				String key = (String) partyIt.next();
				instance.getPartiesInvited().add(key.toString());
			}
			
			Iterator<String> materialsIt = storage.getCompound("materials").keySet().iterator();
			while (materialsIt.hasNext()) {
				String mat = (String) materialsIt.next();
				instance.getMaterialMap().put(mat.toString(), storage.getCompound("materials").getInt(mat));
			}
		}
	}

	private int level = 1, exp = 0, expGiven = 0, strength = 0, magic = 0, defense = 0, maxHp = 20, remainingExp = 0, maxAP = 10, aeroTicks = 0, reflectTicks = 0, munny = 0, antipoints = 0, aerialDodgeTicks;

	private String driveForm = DriveForm.NONE.toString();
	LinkedHashMap<String, int[]> driveForms = new LinkedHashMap<>(); //Key = name, value=  {level, experience}
	List<String> magicList = new ArrayList<>();
	List<ResourceLocation> recipeList = new ArrayList<>();
	LinkedHashMap<String, int[]> abilityMap = new LinkedHashMap<>(); //Key = name, value = {level, equipped},
    private TreeMap<String, Integer> materials = new TreeMap<>();


	List<String> partyList = new ArrayList<>();

	private double mp = 0, maxMP = 0, dp = 0, maxDP = 300, fp = 0;

	private boolean recharge, reflectActive, isGliding, hasJumpedAerealDodge = false;

	private List<String> messages = new ArrayList<>();
	private List<String> dfMessages = new ArrayList<>();

	private PortalData[] orgPortalCoords = { new PortalData((byte) 0, 0, 0, 0, 0), new PortalData((byte) 0, 0, 0, 0, 0), new PortalData((byte) 0, 0, 0, 0, 0) };

	private Map<ResourceLocation, ItemStack> equippedKeychains = new HashMap<>();

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
	public void addExperience(PlayerEntity player, int exp) {
		if (player != null) {
			if (this.level < 100) {
				this.exp += exp;
				while (this.getExpNeeded(this.getLevel(), this.exp) <= 0 && this.getLevel() != 100) {
					setLevel(this.getLevel() + 1);
					levelUpStatsAndDisplayMessage(player);
					PacketHandler.sendTo(new SCShowOverlayPacket("levelup"), (ServerPlayerEntity) player);
				}
			}
			PacketHandler.sendTo(new SCShowOverlayPacket("exp"), (ServerPlayerEntity) player);
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

	@Override
	public void levelUpStatsAndDisplayMessage(PlayerEntity player) {
		this.getMessages().clear();
		switch (this.level) {
		case 2:
			this.addDefense(1);
			addAbility(Strings.scan, true);
			break;
		case 3:
			//addAbility(Strings.highJump);
			this.addStrength(1);
			break;
		case 4:
			this.addDefense(1);
			break;
		case 5:
			this.addStrength(1);
			this.addMaxHP(5);
			this.addMaxMP(5);
			// ABILITIES.unlockAbility(ModAbilities.guard);
			break;
		case 6:
			this.addMagic(1);
			this.addDefense(1);
			break;
		case 7:
			this.addStrength(1);
			break;
		case 8:
			this.addMagic(1);
			break;
		case 9:
			this.addStrength(1);
			break;
		case 10:
			this.addMagic(1);
			this.addDefense(1);
			this.addMaxHP(5);
			this.addMaxMP(5);
			// ABILITIES.unlockAbility(ModAbilities.mpHaste);
			break;
		case 11:
			this.addStrength(1);
			break;
		case 12:
			this.addMagic(1);
			this.addAbility(Strings.mpHaste, true);
			break;
		case 13:
			this.addStrength(1);
			break;
		case 14:
			this.addMagic(1);
			this.addDefense(1);
			break;
		case 15:
			this.addStrength(1);
			this.addMaxHP(5);
			this.addMaxMP(5);
			this.addAbility(Strings.damageDrive, true);
			// ABILITIES.unlockAbility(ModAbilities.formBoost);
			break;
		case 16:
			this.addMagic(1);
			this.addAbility(Strings.mpRage, true);
			break;
		case 17:
			this.addStrength(1);
			break;
		case 18:
			this.addMagic(1);
			this.addDefense(1);
			break;
		case 19:
			this.addStrength(1);
			break;
		case 20:
			this.addMagic(1);
			this.addMaxHP(5);
			this.addMaxMP(5);
			// ABILITIES.unlockAbility(ModAbilities.mpHastera);
			break;
		case 21:
			this.addStrength(1);
			break;
		case 22:
			this.addMagic(1);
			this.addDefense(1);
			this.addAbility(Strings.formBoost, true);
			break;
		case 23:
			this.addStrength(1);
			break;
		case 24:
			this.addMagic(1);
			break;
		case 25:
			this.addStrength(1);
			this.addMaxHP(5);
			this.addAbility(Strings.driveBoost, true);
			break;
		case 26:
			this.addMagic(1);
			this.addDefense(1);
			break;
		case 27:
			this.addStrength(1);
			this.addMagic(1);
			break;
		case 28:
			this.addMagic(1);
			break;
		case 29:
			this.addStrength(1);
			break;
		case 30:
			this.addMagic(1);
			this.addDefense(1);
			this.addMaxHP(5);
			break;
		case 31:
			this.addStrength(1);
			break;
		case 32:
			this.addStrength(1);
			this.addMagic(1);
			break;
		case 33:
			this.addStrength(1);
			// ABILITIES.unlockAbility(ModAbilities.driveConverter);
			break;
		case 34:
			this.addMagic(1);
			this.addDefense(1);
			addAbility(Strings.mpHastera, true);
			break;
		case 35:
			this.addStrength(1);
			this.addMaxHP(5);
			break;
		case 36:
			this.addMagic(1);
			break;
		case 37:
			this.addStrength(1);
			break;
		case 38:
			this.addMagic(1);
			this.addDefense(1);
			break;
		case 39:
			this.addStrength(1);
			break;
		case 40:
			this.addMagic(1);
			this.addMaxHP(5);
			break;
		case 41:
			this.addStrength(1);
			break;
		case 42:
			this.addMagic(1);
			this.addDefense(1);
			break;
		case 43:
			this.addStrength(1);
			this.addMagic(1);
			break;
		case 44:
			this.addMagic(1);
			break;
		case 45:
			this.addStrength(1);
			this.addMaxHP(5);
			break;
		case 46:
			this.addMagic(1);
			this.addDefense(1);
			break;
		case 47:
			this.addStrength(1);
			break;
		case 48:
			this.addStrength(1);
			this.addMagic(1);
			// ABILITIES.unlockAbility(ModAbilities.sonicBlade);
			break;
		case 49:
			this.addStrength(1);
			break;
		case 50:
			this.addMagic(1);
			this.addDefense(1);
			this.addMaxHP(5);
			// ABILITIES.unlockAbility(ModAbilities.mpHastega);
			break;
		case 51:
			this.addStrength(1);
			break;
		case 52:
			this.addMagic(1);
			break;
		case 53:
			this.addStrength(1);
			break;
		case 54:
			this.addMagic(1);
			this.addDefense(1);
			break;
		case 55:
			this.addStrength(1);
			this.addMaxHP(5);
			// ABILITIES.unlockAbility(ModAbilities.strikeRaid);
			break;
		case 56:
			this.addMagic(1);
			break;
		case 57:
			this.addStrength(1);
			break;
		case 58:
			this.addMagic(1);
			this.addDefense(1);
			break;
		case 59:
			this.addStrength(1);
			break;
		case 60:
			this.addMagic(1);
			this.addMaxHP(5);
			break;
		case 61:
			this.addStrength(1);
			break;
		case 62:
			this.addMagic(1);
			this.addDefense(1);
			break;
		case 63:
			this.addStrength(1);
			break;
		case 64:
			this.addMagic(1);
			break;
		case 65:
			this.addStrength(1);
			this.addMaxHP(5);
			break;
		case 66:
			this.addMagic(1);
			this.addDefense(1);
			break;
		case 67:
			this.addStrength(1);
			break;
		case 68:
			this.addMagic(1);
			break;
		case 69:
			this.addStrength(1);
			break;
		case 70:
			this.addMagic(1);
			this.addDefense(1);
			this.addMaxHP(5);
			break;
		case 71:
			this.addStrength(1);
			break;
		case 72:
			this.addMagic(1);
			break;
		case 73:
			this.addStrength(1);
			break;
		case 74:
			this.addMagic(1);
			this.addDefense(1);
			break;
		case 75:
			this.addStrength(1);
			this.addMaxHP(5);
			break;
		case 76:
			this.addMagic(1);
			break;
		case 77:
			this.addStrength(1);
			break;
		case 78:
			this.addMagic(1);
			this.addDefense(1);
			break;
		case 79:
			this.addStrength(1);
			break;
		case 80:
			this.addMagic(1);
			this.addMaxHP(5);
			break;
		case 81:
			this.addStrength(1);
			break;
		case 82:
			this.addMagic(1);
			this.addDefense(1);
			break;
		case 83:
			this.addStrength(1);
			break;
		case 84:
			this.addMagic(1);
			break;
		case 85:
			this.addStrength(1);
			this.addMaxHP(5);
			break;
		case 86:
			this.addMagic(1);
			this.addDefense(1);
			break;
		case 87:
			this.addStrength(1);
			break;
		case 88:
			this.addMagic(1);
			break;
		case 89:
			this.addStrength(1);
			break;
		case 90:
			this.addMagic(1);
			this.addDefense(1);
			this.addMaxHP(5);
			break;
		case 91:
			this.addStrength(1);
			break;
		case 92:
			this.addMagic(1);
			break;
		case 93:
			this.addStrength(1);
			break;
		case 94:
			this.addMagic(1);
			this.addDefense(1);
			break;
		case 95:
			this.addStrength(1);
			this.addMaxHP(5);
			break;
		case 96:
			this.addMagic(1);
			break;
		case 97:
			this.addStrength(1);
			break;
		case 98:
			this.addMagic(1);
			this.addDefense(1);
			break;
		case 99:
			this.addStrength(1);
			break;
		case 100:
			this.addStrength(10);
			this.addDefense(10);
			this.addMagic(10);
			this.addMaxHP(5);
			break;
		}
		if (this.level % 5 == 0) {
			player.setHealth(getMaxHP());
			player.getFoodStats().addStats(20, 0);
			this.setMP(this.getMaxMP());
			
			// player.getCapability(ModCapabilities.ORGANIZATION_XIII, null).addPoints(1);
			 this.addMaxMP(5);
			// PacketDispatcher.sendTo(new
			// SyncOrgXIIIData(player.getCapability(ModCapabilities.ORGANIZATION_XIII,
			// null)), (EntityPlayerMP) player);
		}

		if (this.level % 2 == 0) {
			this.addMaxAP(1);
		}

		// PacketDispatcher.sendTo(new SyncUnlockedAbilities(ABILITIES),
		// (EntityPlayerMP) player);

		player.world.playSound((PlayerEntity) null, player.getPosition(), ModSounds.levelup.get(), SoundCategory.MASTER, 0.5f, 1.0f);
		player.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getMaxHP());
		PacketHandler.sendTo(new SCSyncCapabilityPacket(ModCapabilities.getPlayer(player)), (ServerPlayerEntity) player);
		PacketHandler.syncToAllAround(player, this);

	}

	@Override
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	@Override
	public void remMP(double amount) {
		// TODO CHEAT MODE
		this.mp = Math.max(this.mp - amount, 0);
	}

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
	public void setMunny(int amount) {
		this.munny = amount;
	}

	@Override
	public int getMunny() {
		return munny;
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
    public void displayDriveFormLevelUpMessage(PlayerEntity player, String driveForm) { 
     	this.getMessages().clear();
     	this.getDFMessages().clear();
     	
     	dfMessages.add(Strings.Stats_LevelUp_FormGauge);
		DriveForm form = ModDriveForms.registry.getValue(new ResourceLocation(driveForm));
		String driveformAbility = form.getDFAbilityForLevel(getDriveFormLevel(driveForm));
     	String baseAbility = form.getBaseAbilityForLevel(getDriveFormLevel(driveForm));

     	if(!driveformAbility.equals("")) {
     		Ability a = ModAbilities.registry.getValue(new ResourceLocation(driveformAbility));
     		String name = a.getRegistryName().getPath();
     		if(a.getType() == AbilityType.GROWTH) {
     			int level = (getEquippedAbilityLevel(driveformAbility)[0]+2); //+2 Because it's not set yet, it should be +1 if the ability was already upgraded at the time of generating this message
     			name += "_"+level;
     		}
     		dfMessages.add(name);
     	}
     	
     	if(!baseAbility.equals("")) {
     		Ability a = ModAbilities.registry.getValue(new ResourceLocation(baseAbility));
     		String name = a.getRegistryName().getPath();
     		if(a.getType() == AbilityType.GROWTH) {
     			name += "_"+(getEquippedAbilityLevel(baseAbility)[0]+1);
     		}
     		addAbility(baseAbility,name);

     		//messages.add(name);
     	}

		player.world.playSound((PlayerEntity) null, player.getPosition(), ModSounds.levelup.get(), SoundCategory.MASTER, 0.5f, 1.0f);
		// TODO Actually add abilities and then syncing
		//addAbility(bfAbility);
		// PacketDispatcher.sendTo(new SyncDriveData(player.getCapability(ModCapabilities.DRIVE_STATE, null)), (EntityPlayerMP) player);
		
		PacketHandler.sendTo(new SCShowOverlayPacket("drivelevelup", driveForm), (ServerPlayerEntity) player);
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

	@Override
	public int getAntiPoints() {
		return antipoints;
	}

	@Override
	public void setAntiPoints(int points) {
		this.antipoints = points;
	}

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
	public PortalData getPortalCoords(byte pID) {
		return orgPortalCoords[pID];
	}

	@Override
	public void setPortalCoords(byte pID, PortalData coords) {
		orgPortalCoords[pID] = coords;
	}

	@Override
	public List<PortalData> getPortalList() {
		List<PortalData> list = new ArrayList<PortalData>();
		for (byte i = 0; i < 3; i++) {
			PortalData coords = getPortalCoords(i);
			if (!(coords.getX() == 0 && coords.getY() == 0 && coords.getZ() == 0)) {
				list.add(coords);
				// System.out.println(i+" Added portal: "+coords.getPID());
			}
		}
		return list;
	}
	
	@Override
	public void setPortalList(List<PortalData> list) {
		for (byte i = 0; i < list.size(); i++) {
			orgPortalCoords[i] = list.get(i);
			/*System.out.println(list.get(i).getDimID());
			System.out.println(list.get(i).getX());
			System.out.println(list.get(i).getY());
			System.out.println(list.get(i).getZ());
			System.out.println(list.get(i).getPID());*/
		}
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
		if(notification) {
			messages.add(ability);
		}
		if(abilityMap.containsKey(ability)) {
			abilityMap.put(ability, new int[]{abilityMap.get(ability)[0]+1,abilityMap.get(ability)[1]});
		} else {//If not already present in the map set it to level 1 and fully unequipped
			abilityMap.put(ability, new int[]{1,0});
		}
	}
	
	public void addAbility(String ability, String displayName) {
		messages.add(displayName);
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
	
}
