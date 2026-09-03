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

import java.util.EnumMap;
import java.util.Map;

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
        Player player = event.getEntity();
        if (!player.level().dimension().equals(ModDimensions.DIVE_TO_THE_HEART))
            return;

        PlayerData playerData = PlayerData.get(player);
        if (playerData == null)
            return;

        if (playerData.getSoAState() == SoAState.NONE) {
            // Anyone who already has a union goes straight to the weapon choice
            playerData.setSoAState(playerData.hasUnion() ? SoAState.CHOICE : SoAState.UNION);
        }

        if (!player.level().isClientSide() && playerData.getSoAState() == SoAState.UNION && player.tickCount % 40 == 0 && DiveToTheHeartChunkGenerator.onUnionPlatform(player.getX(), player.getZ())) {
            ensureForetellers((ServerLevel) player.level());
        }

        if (player.isCreative())
            return;

        // Bridge is always solid
        if (playerData.getSoAState() == SoAState.UNION && !DiveToTheHeartChunkGenerator.onUnionPlatform(player.getX(), player.getZ())) {
            returnToPlatform(player, false);
        }

        if (player.getY() < 10) {
            if (playerData.getSoAState() == SoAState.COMPLETE) {
                if (!player.level().isClientSide()) {
                    player.resetFallDistance();
                    ServerLevel dimension = player.level().getServer().getLevel(playerData.getReturnDimension());
                    player.changeDimension(new DimensionTransition(dimension, new Vec3(playerData.getReturnLocation().x, playerData.getReturnLocation().y, playerData.getReturnLocation().z), Vec3.ZERO, player.getYRot(), player.getXRot(), entity -> {}));
                }
            } else {
                returnToPlatform(player, playerData.hasUnion());
            }
        }
    }

    private static void returnToPlatform(Player player, boolean hasUnion) {
        BlockPos back = DiveToTheHeartChunkGenerator.spawnFor(hasUnion);
        player.resetFallDistance();
        player.teleportTo(back.getX() + 0.5D, back.getY(), back.getZ() + 0.5D);
    }

    private static AABB unionPlatformBounds() {
        int r = DiveToTheHeartChunkGenerator.PLATFORM_RADIUS;
        int y = DiveToTheHeartChunkGenerator.spawnFor(false).getY();
        return new AABB(
                DiveToTheHeartChunkGenerator.UNION_CX - r, y - 4, DiveToTheHeartChunkGenerator.UNION_CZ - r,
                DiveToTheHeartChunkGenerator.UNION_CX + r + 1, y + 6, DiveToTheHeartChunkGenerator.UNION_CZ + r + 1
        );
    }

    public static void ensureForetellers(ServerLevel level) {
        Map<Union, ForetellerEntity> found = new EnumMap<>(Union.class);
        for (ForetellerEntity foreteller : level.getEntitiesOfClass(ForetellerEntity.class, unionPlatformBounds())) {
            if (found.putIfAbsent(foreteller.getUnion(), foreteller) != null) {
                foreteller.discard();
            }
        }

        Union[] unions = Union.choosable();
        for (int i = 0; i < unions.length; i++) {
            Union union = unions[i];
            if (found.containsKey(union))
                continue;

            Vec3 pos = DiveToTheHeartChunkGenerator.foretellerPos(i);

            ForetellerEntity foreteller = ModEntities.TYPE_FORETELLER.get().create(level);
            if (foreteller == null)
                continue;

            foreteller.setUnion(union);
            foreteller.wearUnionRobes();

            // Facing the middle of the platform, so the five of them look inwards at the player
            double dx = DiveToTheHeartChunkGenerator.UNION_CENTRE_X - pos.x;
            double dz = DiveToTheHeartChunkGenerator.UNION_CENTRE_Z - pos.z;
            float yaw = (float) (Mth.atan2(dz, dx) * (180D / Math.PI)) - 90.0F;

            foreteller.moveTo(pos.x, pos.y, pos.z, yaw, 0.0F);
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
