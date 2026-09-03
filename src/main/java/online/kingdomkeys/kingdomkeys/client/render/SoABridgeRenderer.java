package online.kingdomkeys.kingdomkeys.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.block.MosaicStainedGlassBlock;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;
import online.kingdomkeys.kingdomkeys.world.dimension.dive_to_the_heart.DiveToTheHeartChunkGenerator;

@EventBusSubscriber(Dist.CLIENT)
public class SoABridgeRenderer {
    //Ticks to fully draw the bridge
    private static final int REVEAL_TICKS = 160;

    private static long revealStartTick = 0;

    public static void beginReveal(long gameTime) {
        revealStartTick = gameTime;
    }

    @SubscribeEvent
    public static void renderBridge(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS)
            return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null)
            return;
        if (!mc.level.dimension().equals(ModDimensions.DIVE_TO_THE_HEART))
            return;

        Player player = mc.player;
        PlayerData playerData = PlayerData.get(player);
        if (playerData == null || !playerData.hasUnion())
            return;

        // The state the block would have had if it were an ordinary piece of the floor
        BlockState visible = ModBlocks.mosaic_stained_glass.get().defaultBlockState().setValue(MosaicStainedGlassBlock.STRUCTURE, false);

        int from = DiveToTheHeartChunkGenerator.BRIDGE_Z_MIN;
        int to = DiveToTheHeartChunkGenerator.BRIDGE_Z_MAX;
        int length = to - from + 1;

        // Unrolls from the union platform towards the pedestals
        int drawn = length;
        if (revealStartTick > 0) {
            long elapsed = mc.level.getGameTime() - revealStartTick;
            if (elapsed < REVEAL_TICKS) {
                drawn = Mth.ceil(length * (elapsed / (float) REVEAL_TICKS));
            }
        }
        if (drawn <= 0)
            return;

        Vec3 camera = event.getCamera().getPosition();
        PoseStack pose = event.getPoseStack();
        MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();

        int half = DiveToTheHeartChunkGenerator.BRIDGE_HALF_WIDTH;
        int y = DiveToTheHeartChunkGenerator.FLOOR_Y;

        for (int step = 0; step < drawn; step++) {
            // Counting down from the union end, which is the far z
            int z = to - step;
            for (int x = DiveToTheHeartChunkGenerator.UNION_CX - half; x <= DiveToTheHeartChunkGenerator.UNION_CX + half; x++) {
                BlockPos pos = new BlockPos(x, y, z);

                pose.pushPose();
                pose.translate(pos.getX() - camera.x, pos.getY() - camera.y, pos.getZ() - camera.z);
                mc.getBlockRenderer().renderSingleBlock(visible, pose, buffers, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
                pose.popPose();
            }
        }
        buffers.endBatch();
    }

    private SoABridgeRenderer() {
    }
}
