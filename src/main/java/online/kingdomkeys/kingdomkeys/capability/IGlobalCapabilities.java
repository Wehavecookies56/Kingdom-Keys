package online.kingdomkeys.kingdomkeys.capability;

public interface IGlobalCapabilities {
	void setStoppedTicks(int time);
	int getStoppedTicks();
	void subStoppedTicks(int time);
	
	void setStopped(boolean b);
	boolean getStopped();
}
