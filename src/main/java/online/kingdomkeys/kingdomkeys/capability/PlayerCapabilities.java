package online.kingdomkeys.kingdomkeys.capability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.PortalCoords;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Utils;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.packet.PacketSyncCapability;
import online.kingdomkeys.kingdomkeys.network.packet.ShowOverlayPacket;

public class PlayerCapabilities implements IPlayerCapabilities {

	public static class Storage implements IStorage<IPlayerCapabilities> {
		@Override
		public INBT writeNBT(Capability<IPlayerCapabilities> capability, IPlayerCapabilities instance, Direction side) {
			CompoundNBT props = new CompoundNBT();
			props.putInt("level", instance.getLevel());
			props.putInt("experience", instance.getExperience());
			props.putInt("experience_given", instance.getExperienceGiven());
			props.putInt("strength", instance.getStrength());
			props.putInt("magic", instance.getMagic());
			props.putInt("defense", instance.getDefense());
			props.putInt("max_hp", instance.getMaxHP());
			props.putDouble("mp", instance.getMP());
			props.putDouble("max_mp", instance.getMaxMP());
			props.putBoolean("recharge", instance.getRecharge());
			props.putDouble("dp", instance.getDP());
			props.putDouble("max_dp", instance.getMaxDP());
			props.putDouble("fp", instance.getFP());
			props.putString("drive_form", instance.getActiveDriveForm());
			props.putInt("anti_points", instance.getAntiPoints());
			props.putInt("reflect_ticks", instance.getReflectTicks());
			props.putBoolean("reflect_active", instance.getReflectActive());
			props.putInt("munny", instance.getMunny());

			CompoundNBT magics = new CompoundNBT();
			for (String magic : instance.getMagicsList()) {
				magics.putInt(magic, 0);
			}
			props.put("magics", magics);

			CompoundNBT forms = new CompoundNBT();
			Iterator<Map.Entry<String, int[]>> it = instance.getDriveFormsMap().entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, int[]> pair = (Map.Entry<String, int[]>) it.next();
				// System.out.println("Write: "+pair.getKey()+" "+pair.getValue());
				forms.putIntArray(pair.getKey().toString(), pair.getValue());
			}
			props.put("drive_forms", forms);

			for (byte i = 0; i < 3; i++) {
				props.putByte("Portal" + i + "N", instance.getPortalCoords(i).getPID());
				props.putDouble("Portal" + i + "X", instance.getPortalCoords(i).getX());
				props.putDouble("Portal" + i + "Y", instance.getPortalCoords(i).getY());
				props.putDouble("Portal" + i + "Z", instance.getPortalCoords(i).getZ());
				props.putInt("Portal" + i + "D", instance.getPortalCoords(i).getDimID());
			}

			return props;
		}

		@Override
		public void readNBT(Capability<IPlayerCapabilities> capability, IPlayerCapabilities instance, Direction side, INBT nbt) {
			CompoundNBT properties = (CompoundNBT) nbt;
			instance.setLevel(properties.getInt("level"));
			instance.setExperience(properties.getInt("experience"));
			instance.setExperienceGiven(properties.getInt("experience_given"));
			instance.setStrength(properties.getInt("strength"));
			instance.setMagic(properties.getInt("magic"));
			instance.setDefense(properties.getInt("defense"));
			instance.setMaxHP(properties.getInt("max_hp"));
			instance.setMP(properties.getDouble("mp"));
			instance.setMaxMP(properties.getDouble("max_mp"));
			instance.setRecharge(properties.getBoolean("recharge"));
			instance.setDP(properties.getDouble("dp"));
			instance.setMaxDP(properties.getDouble("max_dp"));
			instance.setFP(properties.getDouble("fp"));
			instance.setActiveDriveForm(properties.getString("drive_form"));
			instance.setAntiPoints(properties.getInt("anti_points"));
			instance.setReflectTicks(properties.getInt("reflect_ticks"));
			instance.setReflectActive(properties.getBoolean("reflect_active"));
			instance.setMunny(properties.getInt("munny"));

			Iterator<String> magicIt = properties.getCompound("magics").keySet().iterator();
			while (magicIt.hasNext()) {
				String key = (String) magicIt.next();
				System.out.println("Read: " + key);
				instance.getMagicsList().add(key.toString());
				/*
				 * if (properties.getCompound("magics").getInt(key) == 0 && key.toString() !=
				 * null) instance.getMagicsList().remove(key.toString());
				 */
			}

			Iterator<String> driveFormsIt = properties.getCompound("drive_forms").keySet().iterator();
			while (driveFormsIt.hasNext()) {
				String driveFormName = (String) driveFormsIt.next();
				System.out.println("Read: " + driveFormName);
				instance.getDriveFormsMap().put(driveFormName.toString(), properties.getCompound("drive_forms").getIntArray(driveFormName));
				
				if (properties.getCompound("drive_forms").getIntArray(driveFormName)[0] == 0 && driveFormName.toString() != null)
					instance.getDriveFormsMap().remove(driveFormName.toString());
			}

			for (byte i = 0; i < 3; i++) {
				instance.setPortalCoords(i, new PortalCoords(properties.getByte("Portal" + i + "N"), properties.getDouble("Portal" + i + "X"), properties.getDouble("Portal" + i + "Y"), properties.getDouble("Portal" + i + "Z"), properties.getInt("Portal" + i + "D")));
			}
		}
	}

	private int level = 1, exp = 0, expGiven = 0, maxEXP = 1000000, strength = 0, magic = 0, defense = 0, maxHp = 20, remainingExp = 0, ap, maxAP, reflectTicks = 0, munny = 0, antipoints = 0, aerialDodgeTicks;

	private String driveForm = "";
	Map<String, int[]> driveForms = new HashMap<String, int[]>();
	List<String> magicList = new ArrayList<String>();

	private double mp = 0, maxMP = 10, dp = 0, maxDP = 300, fp = 0;

	private boolean recharge, reflectActive, isGliding, hasJumpedAerealDodge = false;

	private List<String> messages = new ArrayList<String>();
	private List<String> dfMessages = new ArrayList<String>();

	private PortalCoords[] orgPortalCoords = { new PortalCoords((byte) 0, 0, 0, 0, 0), new PortalCoords((byte) 0, 0, 0, 0, 0), new PortalCoords((byte) 0, 0, 0, 0, 0) };

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
			if (this.exp + exp <= this.maxEXP) {
				this.exp += exp;
				while (this.getExpNeeded(this.getLevel(), this.exp) <= 0 && this.getLevel() != 100) {
					this.setLevel(this.getLevel() + 1);
					this.levelUpStatsAndDisplayMessage(player);
					PacketHandler.sendTo(new ShowOverlayPacket("levelup"), (ServerPlayerEntity) player);
				}
			} else {
				this.exp = this.maxEXP;
			}
			// System.out.println(getExpNeeded(this.getLevel(), this.exp));

			PacketHandler.sendTo(new ShowOverlayPacket("exp"), (ServerPlayerEntity) player);
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
		if (this.mp + mp > this.maxMP) {
			this.mp = this.maxMP;
		} else {
			this.mp += mp;
		}
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
		// IAbilities ABILITIES = player.getCapability(ModCapabilities.ABILITIES, null);
		this.getMessages().clear();
		switch (this.level) {
		case 2:
			this.addDefense(1);
			// ABILITIES.unlockAbility(ModAbilities.scan);
			break;
		case 3:
			this.addStrength(1);
			break;
		case 4:
			this.addDefense(1);
			break;
		case 5:
			this.addStrength(1);
			this.addMaxHP(5);
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
			// ABILITIES.unlockAbility(ModAbilities.mpHaste);
			break;
		case 11:
			this.addStrength(1);
			break;
		case 12:
			this.addMagic(1);
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
			// ABILITIES.unlockAbility(ModAbilities.formBoost);
			break;
		case 16:
			this.addMagic(1);
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
			// ABILITIES.unlockAbility(ModAbilities.mpHastera);
			break;
		case 21:
			this.addStrength(1);
			break;
		case 22:
			this.addMagic(1);
			this.addDefense(1);
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
			// ABILITIES.unlockAbility(ModAbilities.damageDrive);
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
			// player.getCapability(ModCapabilities.ORGANIZATION_XIII, null).addPoints(1);
			this.addMaxMP(5);
			this.setMP(this.getMaxMP());
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
		PacketHandler.sendTo(new PacketSyncCapability(ModCapabilities.get(player)), (ServerPlayerEntity) player);
	}

	@Override
	public int getConsumedAP() {
		return ap;
	}

	@Override
	public void setConsumedAP(int ap) {
		this.ap = ap;
	}

	@Override
	public void addConsumedAP(int ap) {
		this.ap += ap;
	}

	@Override
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	@Override
	public void remMP(double amount) {
		// TODO CHEAT MODE
		if (this.mp - amount < 0)
			this.mp = 0;
		else
			this.mp -= amount;
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
		if (this.dp + dp > this.maxDP) {
			this.dp = this.maxDP;
		} else {
			this.dp += dp;
		}
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
		this.maxDP = dp;
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
		double max = (200 + Utils.getDriveFormLevel(getDriveFormsMap(), getActiveDriveForm()) * 100);
		if(this.fp + fp > max) {
			this.fp = max;
		} else {
			this.fp += fp;
		}
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
	public Map<String, int[]> getDriveFormsMap() {
		return driveForms;
	}

	@Override
	public void setDriveFormsMap(Map<String, int[]> map) {
		this.driveForms = map;
	}

	@Override
	public int getDriveFormLevel(String name) {
		return driveForms.get(name)[0];
	}

	@Override
	public void setDriveFormLevel(String name, int level) {
		if(level <= ModDriveForms.registry.getValue(new ResourceLocation(name)).getMaxLevel()) {
			int experience = ModDriveForms.registry.getValue(new ResourceLocation(name)).getLevelUpCost(level);
			driveForms.put(name, new int[] {level, experience});
		}
	}

	@Override
	public int getDriveFormExp(String name) {
		return driveForms.get(name)[1];
	}

	@Override
	public void setDriveFormExp(PlayerEntity player, String name, int exp) {
		int oldLevel = getDriveFormLevel(name);
		int driveLevel = ModDriveForms.registry.getValue(new ResourceLocation(name)).getLevelFromExp(exp);
		if(driveLevel <= ModDriveForms.registry.getValue(new ResourceLocation(name)).getMaxLevel()) {
			driveForms.put(name, new int[] {driveLevel, exp});
			if(driveLevel > oldLevel) {
				displayDriveFormLevelUpMessage(player, name);
				if(driveLevel == ModDriveForms.registry.getValue(new ResourceLocation(name)).getMaxLevel()) {
					setMaxDP(getMaxDP() + 100);
				}
				PacketHandler.sendTo(new PacketSyncCapability(this), (ServerPlayerEntity)player);
			}
		}
	}
	
	@Override
    public void displayDriveFormLevelUpMessage(PlayerEntity player, String driveForm) { 
     	this.getMessages().clear();
     	this.getDFMessages().clear();
     	
     	dfMessages.add(Strings.Stats_LevelUp_FormGauge);
     	String dfAbility = ModDriveForms.registry.getValue(new ResourceLocation(driveForm)).getDFAbilityForLevel(getDriveFormLevel(driveForm));
     	String bfAbility = ModDriveForms.registry.getValue(new ResourceLocation(driveForm)).getBaseAbilityForLevel(getDriveFormLevel(driveForm));

     	if(!dfAbility.equals("")) {
     		dfMessages.add(dfAbility);
     	}
     	
     	if(!bfAbility.equals("")) {
     		messages.add(bfAbility);
     	}

		player.world.playSound((PlayerEntity) null, player.getPosition(), ModSounds.levelup.get(), SoundCategory.MASTER, 0.5f, 1.0f);
		// TODO Actually add abilities and then syncing
		// PacketDispatcher.sendTo(new SyncDriveData(player.getCapability(ModCapabilities.DRIVE_STATE, null)), (EntityPlayerMP) player);
		
		PacketHandler.sendTo(new ShowOverlayPacket("drivelevelup", driveForm), (ServerPlayerEntity) player);
	}

	@Override
	public List<String> getMagicsList() {
		return magicList;
	}

	@Override
	public void setMagicsList(List<String> list) {
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
	public PortalCoords getPortalCoords(byte pID) {
		return orgPortalCoords[pID];
	}

	@Override
	public void setPortalCoords(byte pID, PortalCoords coords) {
		orgPortalCoords[pID] = coords;
	}

	@Override
	public List<PortalCoords> getPortalList() {
		List<PortalCoords> list = new ArrayList<PortalCoords>();
		for (byte i = 0; i < 3; i++) {
			PortalCoords coords = getPortalCoords(i);
			if (!(coords.getX() == 0 && coords.getY() == 0 && coords.getZ() == 0)) {
				list.add(coords);
				// System.out.println(i+" Added portal: "+coords.getPID());
			}
		}
		return list;
	}

	

}
