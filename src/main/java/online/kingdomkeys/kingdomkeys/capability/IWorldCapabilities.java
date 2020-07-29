package online.kingdomkeys.kingdomkeys.capability;

import java.util.HashMap;
import java.util.List;

import online.kingdomkeys.kingdomkeys.lib.Party;

public interface IWorldCapabilities {
	boolean getHeartlessSpawn();
	void setHeartlessSpawn(boolean b);
	
	void setParties(List<Party> list);
	List<Party> getParties();
}
