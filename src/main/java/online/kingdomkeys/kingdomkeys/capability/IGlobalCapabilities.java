package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IGlobalCapabilities extends INBTSerializable<CompoundTag> {
	
	//Stop damage
	float getStopDamage();
	void setStopDamage(float dmg);
	void addDamage(float dmg);
	void setStopCaster(String name);
	String getStopCaster();

	//Castle Oblivion
	boolean getCastleOblivionMarker();
	void setCastleOblivionMarker(boolean marker);
	
	void setLevel(int lvl);
	int getLevel();
	
	//Ticks to display stop animation
	int getStopModelTicks();
	void setStopModelTicks(int ticks);
}
