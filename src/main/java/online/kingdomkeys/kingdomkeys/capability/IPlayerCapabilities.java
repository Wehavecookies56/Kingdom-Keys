package online.kingdomkeys.kingdomkeys.capability;

import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.PlayerEntity;
import online.kingdomkeys.kingdomkeys.lib.PortalCoords;

public interface IPlayerCapabilities {
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
	
	int getConsumedAP();
	void setConsumedAP(int ap);
	void addConsumedAP(int ap);
	
	int getMaxAP();
	void setMaxAP(int ap);
	void addMaxAP(int ap);
	
    void levelUpStatsAndDisplayMessage(PlayerEntity player);
    void clearMessages();
	void setMessages(List<String> messages);
    List<String> getMessages();
    
	int getExpNeeded(int level, int currentExp);
	
	void setActiveDriveForm(String form);
	String getActiveDriveForm();
	
	void setReflectTicks(int ticks);
	void remReflectTicks(int ticks);
	int getReflectTicks();
	void setReflectActive(boolean active);
	boolean getReflectActive();
	
	void setRecharge(boolean b);
	boolean getRecharge();
	
	void setMunny(int amount);
	int getMunny();
	
	Map<String,Integer> getDriveFormsMap();
	void setDriveFormsMap(Map<String,Integer> map);
	
	int getDriveFormLevel(String name);
	void setDriveFormLevel(String name, int level);
	
	List<String> getMagicsList();
	void setMagicsList(List<String> list);
	void addMagicToList(String magic);
	void removeMagicFromList(String magic);
	
	int getAntiPoints();
	void setAntiPoints(int points);
	
	//Drive forms
	boolean getIsGliding();
	void setIsGliding(boolean b);
	
	int getAerialDodgeTicks();
	void setAerialDodgeTicks(int ticks);
	boolean hasJumpedAerialDodge();
	void setHasJumpedAerialDodge(boolean b);
	
	
	List<PortalCoords> getPortalList();
	PortalCoords getPortalCoords(byte pID);
    void setPortalCoords(byte pID, PortalCoords coords);
}
