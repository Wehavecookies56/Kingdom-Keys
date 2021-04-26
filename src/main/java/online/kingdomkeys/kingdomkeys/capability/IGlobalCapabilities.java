package online.kingdomkeys.kingdomkeys.capability;

public interface IGlobalCapabilities {
	//Stop
	int getStoppedTicks();
	void setStoppedTicks(int time);
	void subStoppedTicks(int time);
	
	//Stop damage
	float getDamage();
	void setDamage(float dmg);
	void addDamage(float dmg);
	void setStopCaster(String name);
	String getStopCaster();
	
	
	//Gravity Flat
	int getFlatTicks();
	void setFlatTicks(int time);
	void subFlatTicks(int time);

	
}
