package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class LevelCapabilities implements ILevelCapabilities {

	public static class Storage implements IStorage<ILevelCapabilities> {
		@Override
		public INBT writeNBT(Capability<ILevelCapabilities> capability, ILevelCapabilities instance, Direction side) {
			CompoundNBT props = new CompoundNBT();
			props.putInt("level", instance.getLevel());
			props.putInt("experience", instance.getExperience());
			props.putInt("experience_given", instance.getExperienceGiven());
			props.putInt("strength", instance.getStrength());
			props.putInt("magic", instance.getMagic());
			props.putInt("defense", instance.getDefense());
			return props;
		}

		@Override
		public void readNBT(Capability<ILevelCapabilities> capability, ILevelCapabilities instance, Direction side, INBT nbt) {
			CompoundNBT properties = (CompoundNBT) nbt;
			instance.setLevel(properties.getInt("level"));
			instance.setExperience(properties.getInt("experience"));
			instance.setExperienceGiven(properties.getInt("experience_given"));
			instance.setStrength(properties.getInt("strength"));
			instance.setMagic(properties.getInt("magic"));
			instance.setDefense(properties.getInt("defense"));
		}
	}

	private int level = 0,
		exp = 0,
		expGiven = 0,
		strength = 0,
		magic = 0,
		defense = 0;

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
}
