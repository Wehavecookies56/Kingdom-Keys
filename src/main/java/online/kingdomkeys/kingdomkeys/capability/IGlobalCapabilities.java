package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IGlobalCapabilities extends INBTSerializable<CompoundTag> {
	//Stop
	int getStoppedTicks();
	void setStoppedTicks(int time);
	void subStoppedTicks(int time);
	
	//Stop damage
	float getStopDamage();
	void setStopDamage(float dmg);
	void addDamage(float dmg);
	void setStopCaster(String name);
	String getStopCaster();
	
	
	//Gravity Flat
	int getFlatTicks();
	void setFlatTicks(int time);
	void subFlatTicks(int time);
	
	//Aero
	int getAeroLevel();
	void setAeroLevel(int level);
	int getAeroTicks();
	void setAeroTicks(int i, int level);
	void remAeroTicks(int ticks);

	//Castle Oblivion
	boolean getCastleOblivionMarker();
	void setCastleOblivionMarker(boolean marker);
	
	void setLevel(int lvl);
	int getLevel();
	
	//Ticks to display stop animation
	int getStopModelTicks();
	void setStopModelTicks(int ticks);
	
	boolean isKO();
	void setKO(boolean ko);
}
