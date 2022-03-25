package online.kingdomkeys.kingdomkeys.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Proxy interface for both client and server
 */
public interface IProxy {
    //Setup event for the proxy
    void setup(final FMLCommonSetupEvent event);

	Player getClientPlayer();
	Level getClientWorld();
	Minecraft getClientMCInstance();

}
