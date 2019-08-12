package online.kingdomkeys.kingdomkeys.capability;

public interface ILevelCapabilities {
	int getLevel();
	void setLevel(int level);
	
	int getExperience();
	void setExperience(int exp);
	
	int getExperienceGiven();
	void setExperienceGiven(int exp);
	
	int getStrength();
	void setStrength(int str);
	
	int getMagic();
	void setMagic(int mag);
	
	int getDefense();
	void setDefense(int def);
}
