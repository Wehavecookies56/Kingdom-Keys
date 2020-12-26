package online.kingdomkeys.kingdomkeys.world.dimension;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;

@Mod.EventBusSubscriber
public class DiveToTheHeartDimension extends Dimension {


    public DiveToTheHeartDimension(World world, DimensionType type) {
        super(world, type, 0);
    }

    @Override
    public ChunkGenerator<?> createChunkGenerator() {
        return new DiveToTheHeartChunkGenerator(world, new DiveToTheHeartBiomeProvider());
    }

    @Nullable
    @Override
    public BlockPos findSpawn(ChunkPos chunkPosIn, boolean checkValid) {
        return null;
    }

    @Nullable
    @Override
    public BlockPos findSpawn(int posX, int posZ, boolean checkValid) {
        return null;
    }

    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        return 0;
    }

    @Override
    public boolean isSurfaceWorld() {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {
        return null;
    }

    @Override
    public Vec3d getFogColor(float celestialAngle, float partialTicks) {
        return Vec3d.ZERO;
    }

    @Override
    public double getVoidFogYFactor() {
        return 0;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean isSkyColored() {
        return false;
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    @Override
    public boolean doesXZShowFog(int x, int z) {
        return true;
    }

    @Override
    public SleepResult canSleepAt(PlayerEntity player, BlockPos pos) {
        return SleepResult.DENY;
    }

    //Event Listeners//

    //Set the fog density to fade out the bottom of the platform
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void renderFog(EntityViewRenderEvent.FogDensity event) {
        World world = event.getInfo().getRenderViewEntity().world;
        if (world != null) {
            if (world.getDimension().getType().equals(ModDimensions.DIVE_TO_THE_HEART_TYPE)) {
                event.setDensity(0.06F);
                event.setCanceled(true);
            }
        }
    }

    //Prevent taking damage in this dimension
    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            if (!((PlayerEntity)event.getEntityLiving()).isCreative()) {
                if (event.getEntityLiving().world.getDimension().getType().equals(ModDimensions.DIVE_TO_THE_HEART_TYPE)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    //Prevent player from falling off the platform
    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (!event.player.isCreative()) {
            if (event.player.world.getDimension().getType().equals(ModDimensions.DIVE_TO_THE_HEART_TYPE)) {
                if (event.player.getPosY() < 10) {
                    event.player.setPositionAndUpdate(0, 25, 0);
                }
            }
        }
    }

    @SubscribeEvent
    public static void breakBlock(BlockEvent.BreakEvent event) {
        if (!event.getPlayer().isCreative()) {
            if (event.getWorld().getDimension().getType().equals(ModDimensions.DIVE_TO_THE_HEART_TYPE)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void placeBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getPlayer().isCreative()) {
            if (event.getWorld().getDimension().getType().equals(ModDimensions.DIVE_TO_THE_HEART_TYPE)) {
                if (event.getWorld().getBlockState(event.getPos()).getBlock() == ModBlocks.pedestal.get()) {
                    if (event.getPlayer().isSneaking()) {
                        event.setCanceled(true);
                    }
                } else {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void useItem(PlayerInteractEvent.RightClickItem event) {
        if (!event.getPlayer().isCreative()) {
            if (event.getWorld().getDimension().getType().equals(ModDimensions.DIVE_TO_THE_HEART_TYPE)) {
                event.setCanceled(true);
            }
        }
    }
}
