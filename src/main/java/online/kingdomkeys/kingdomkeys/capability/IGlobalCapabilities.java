package online.kingdomkeys.kingdomkeys.capability;

public interface IGlobalCapabilities {
	void setStoppedTicks(int time);
	int getStoppedTicks();
	void subStoppedTicks(int time);
	
	//Stop damage
	int getDamage();
	void setDamage(int dmg);
	void addDamage(int dmg);
}
