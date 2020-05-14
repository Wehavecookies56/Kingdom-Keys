package online.kingdomkeys.kingdomkeys.capability;

public interface IGlobalCapabilities {
	//Stop
	int getStoppedTicks();
	void setStoppedTicks(int time);
	void subStoppedTicks(int time);
	
	//Stop damage
	int getDamage();
	void setDamage(int dmg);
	void addDamage(int dmg);
	
	
	//Gravity Flat
	int getFlatTicks();
	void setFlatTicks(int time);
	void subFlatTicks(int time);

	
}
