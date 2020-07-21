package online.kingdomkeys.kingdomkeys.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Proxy interface for both client and server
 */
public interface IProxy {
    //Setup event for the proxy
    void setup(final FMLCommonSetupEvent event);

	PlayerEntity getClientPlayer();
	World getClientWorld();

}
