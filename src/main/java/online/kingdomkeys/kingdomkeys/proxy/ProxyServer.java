package online.kingdomkeys.kingdomkeys.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ProxyServer implements IProxy {
    @Override
    public void setup(FMLCommonSetupEvent event) {
        //Nothing to do here yet


    }

	@Override
	public Player getClientPlayer() {
		return null;
	}

	@Override
	public Level getClientWorld() {
		return null;
	}

	@Override
	public Minecraft getClientMCInstance() {
		return null;
	}
}
