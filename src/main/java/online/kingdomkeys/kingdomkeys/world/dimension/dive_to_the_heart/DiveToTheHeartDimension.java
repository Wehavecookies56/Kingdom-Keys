package online.kingdomkeys.kingdomkeys.world.dimension.dive_to_the_heart;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.mob.ForetellerEntity;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.lib.Union;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;

@EventBusSubscriber
public class DiveToTheHeartDimension{
    //Event Listeners//

    //Set the fog density to fade out the bottom of the platform
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void renderFog(ViewportEvent.RenderFog event) {
        Level world = Minecraft.getInstance().level;
        if (world != null) {
            if (world.dimension().equals(ModDimensions.DIVE_TO_THE_HEART)) {
                RenderSystem.setShaderFogStart(0.0F);
                RenderSystem.setShaderFogEnd(30);
            }
        }
    }

    //Prevent taking damage in this dimension
    @SubscribeEvent
    public static void onHurt(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (!((Player)event.getEntity()).isCreative()) {
                if (event.getEntity().level().dimension().equals(ModDimensions.DIVE_TO_THE_HEART)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    //Prevent player from falling off the platform
    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Pre event) {
        if (!event.getEntity().isCreative()) {
            if (event.getEntity().level().dimension().equals(ModDimensions.DIVE_TO_THE_HEART)) {
                PlayerData playerData = PlayerData.get(event.getEntity());
                if (playerData == null)
                    return;

                if (playerData.getSoAState() == SoAState.NONE) {
                    // Anyone who already has a union goes straight to the weapon choice
                    playerData.setSoAState(playerData.hasUnion() ? SoAState.CHOICE : SoAState.UNION);
                }

                if (playerData.getSoAState() == SoAState.UNION) {
                    if (!event.getEntity().level().isClientSide() && event.getEntity().tickCount % 40 == 0) {
                        ensureForetellers((ServerLevel) event.getEntity().level());
                    }

                    // Bridge is always solid
                    if (!DiveToTheHeartChunkGenerator.onUnionPlatform(event.getEntity().getX(), event.getEntity().getZ())) {
                        returnToPlatform(event.getEntity(), false);
                    }
                }

                if (event.getEntity().getY() < 10) {
                    if (playerData.getSoAState() == SoAState.COMPLETE) {
                        if (!event.getEntity().level().isClientSide()) {
                            event.getEntity().resetFallDistance();
                            ServerLevel dimension = event.getEntity().level().getServer().getLevel(playerData.getReturnDimension());
                            event.getEntity().changeDimension(new DimensionTransition(dimension, new Vec3(playerData.getReturnLocation().x, playerData.getReturnLocation().y, playerData.getReturnLocation().z), Vec3.ZERO, event.getEntity().getYRot(), event.getEntity().getXRot(), entity -> {}));
                        }
                    } else {
                        returnToPlatform(event.getEntity(), playerData.hasUnion());
                    }
                }
            }
        }
    }

    private static void returnToPlatform(Player player, boolean hasUnion) {
        BlockPos back = DiveToTheHeartChunkGenerator.spawnFor(hasUnion);
        player.resetFallDistance();
        player.teleportTo(back.getX() + 0.5D, back.getY(), back.getZ() + 0.5D);
    }

    public static void ensureForetellers(ServerLevel level) {
        Union[] unions = Union.choosable();
        for (int i = 0; i < unions.length; i++) {
            Union union = unions[i];
            BlockPos pos = DiveToTheHeartChunkGenerator.foretellerPos(i);

            if (!level.getEntitiesOfClass(ForetellerEntity.class, new AABB(pos).inflate(3.0D), f -> f.getUnion() == union).isEmpty())
                continue;

            ForetellerEntity foreteller = ModEntities.TYPE_FORETELLER.get().create(level);
            if (foreteller == null)
                continue;

            foreteller.setUnion(union);
            foreteller.wearUnionRobes();

            // Facing the middle of the platform, so the five of them look inwards at the player
            double dx = DiveToTheHeartChunkGenerator.UNION_CX - pos.getX();
            double dz = DiveToTheHeartChunkGenerator.UNION_CZ - pos.getZ();
            float yaw = (float) (Mth.atan2(dz, dx) * (180D / Math.PI)) - 90.0F;

            foreteller.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, yaw, 0.0F);
            foreteller.setYHeadRot(yaw);
            foreteller.setYBodyRot(yaw);
            level.addFreshEntity(foreteller);
        }
    }

    @SubscribeEvent
    public static void breakBlock(BlockEvent.BreakEvent event) {
        if (!event.getPlayer().isCreative()) {
            if (event.getPlayer().level().dimension().equals(ModDimensions.DIVE_TO_THE_HEART)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void placeBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getEntity().isCreative()) {
            if (event.getLevel().dimension().equals(ModDimensions.DIVE_TO_THE_HEART)) {
                if (event.getLevel().getBlockState(event.getPos()).getBlock() == ModBlocks.pedestal.get()) {
                    if (event.getEntity().isShiftKeyDown()) {
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
        if (!event.getEntity().isCreative()) {
            if (event.getLevel().dimension().equals(ModDimensions.DIVE_TO_THE_HEART)) {
                event.setCanceled(true);
            }
        }
    }
}
