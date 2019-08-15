package online.kingdomkeys.kingdomkeys.capability;

import java.util.List;

import net.minecraft.entity.player.PlayerEntity;

public interface ILevelCapabilities {
	int getLevel();
	void setLevel(int level);
	
	int getExperience();
	void setExperience(int exp);
	void addExperience(PlayerEntity player,int exp);

	int getExperienceGiven();
	void setExperienceGiven(int exp);
	
	int getStrength();
	void setStrength(int str);
	void addStrength(int str);
	
	int getMagic();
	void setMagic(int mag);
	void addMagic(int mag);
	
	int getDefense();
	void setDefense(int def);
	void addDefense(int def);
	
	int getHP();
	void setHP(int hp);
	void addHP(int hp);
	
	int getMP();
	void setMP(int mp);
	void addMP(int mp);
	
	int getMaxMP();
	void setMaxMP(int mp);
	void addMaxMP(int mp);
	
	int getMaxAP();
	void setMaxAP(int ap);
	void addMaxAP(int ap);
	
    void levelUpStatsAndDisplayMessage(PlayerEntity player);
    void clearMessages();
    List<String> getMessages();
    
	int getExpNeeded(int level, int currentExp);
	

}
