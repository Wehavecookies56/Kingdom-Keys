package online.kingdomkeys.kingdomkeys.world.dimension.station_of_sorrow;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;

@Mod.EventBusSubscriber
public class StationOfSorrowDimension{
    //Event Listeners//

    //Set the fog density to fade out the bottom of the platform
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void renderFog(EntityViewRenderEvent.FogDensity event) {
        Level world = Minecraft.getInstance().level;
        if (world != null) {
            if (world.dimension().equals(ModDimensions.STATION_OF_SORROW)) {
                event.setDensity(0.03F);
                event.setCanceled(true);
            }
        }
    }

    //Prevent player from falling off the platform
    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (!event.player.isCreative()) {
            if (event.player.level.dimension().equals(ModDimensions.STATION_OF_SORROW)) {
                if (event.player.getY() < 10) {
                    event.player.teleportTo(0, 25, 0);
                }
            }
        }
    }

    @SubscribeEvent
    public static void breakBlock(BlockEvent.BreakEvent event) {
        if (!event.getPlayer().isCreative()) {
            if (event.getPlayer().level.dimension().equals(ModDimensions.STATION_OF_SORROW)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void placeBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getPlayer().isCreative()) {
            if (event.getWorld().dimension().equals(ModDimensions.STATION_OF_SORROW)) {
                if (event.getWorld().getBlockState(event.getPos()).getBlock() == ModBlocks.pedestal.get()) {
                    if (event.getPlayer().isShiftKeyDown()) {
                        event.setCanceled(true);
                    }
                } else {
                    event.setCanceled(true);
                }
            }
        }
    }
}
