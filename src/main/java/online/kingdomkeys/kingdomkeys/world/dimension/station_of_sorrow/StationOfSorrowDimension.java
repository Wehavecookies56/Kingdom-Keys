package online.kingdomkeys.kingdomkeys.world.dimension.station_of_sorrow;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.entity.mob.MarluxiaEntity;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;

@Mod.EventBusSubscriber
public class StationOfSorrowDimension{
    //Event Listeners//

    //Set the fog density to fade out the bottom of the platform
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void renderFog(ViewportEvent.RenderFog event) {
        Level world = Minecraft.getInstance().level;
        if (world != null) {
            if (world.dimension().equals(ModDimensions.STATION_OF_SORROW)) {
                RenderSystem.setShaderFogStart(0.0F);
                RenderSystem.setShaderFogEnd(30);
            }
        }
    }

    //Prevent player from falling off the platform
    @SubscribeEvent
    public static void entityTick(LivingTickEvent event) {
        if (event.getEntity().level().dimension().equals(ModDimensions.STATION_OF_SORROW)) {
        	if(event.getEntity() instanceof Player player) {
    			if (!player.isCreative()) {
	                if (player.getY() < 10) {
	                    player.teleportTo(0, 25, 0);
	                }
	            }
	        }
        	
        	if(event.getEntity() instanceof MarluxiaEntity marluxia) {
	            if (marluxia.getY() < 10) {
	            	marluxia.teleportTo(0, 25, 0);
	            }
	        }
    	}
    }

    @SubscribeEvent
    public static void breakBlock(BlockEvent.BreakEvent event) {
        if (!event.getPlayer().isCreative()) {
            if (event.getPlayer().level().dimension().equals(ModDimensions.STATION_OF_SORROW)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void placeBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getEntity().isCreative()) {
            if (event.getLevel().dimension().equals(ModDimensions.STATION_OF_SORROW)) {
                if (event.getLevel().getBlockEntity(event.getPos()) != null) { //If is a TE
                    if (event.getEntity().isShiftKeyDown()) { //If the player is shifting cancel it (places blocks)
                        event.setCanceled(true);
                    }
                } else {
                    event.setCanceled(true);
                }
            }
        }
    }
}
