package online.kingdomkeys.kingdomkeys.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ProxyServer implements IProxy {
    @Override
    public void setup(FMLCommonSetupEvent event) {
        //Nothing to do here yet


    }

	@Override
	public PlayerEntity getClientPlayer() {
		return null;
	}

	@Override
	public World getClientWorld() {
		return null;
	}

	@Override
	public Minecraft getClientMCInstance() {
		return null;
	}
}
