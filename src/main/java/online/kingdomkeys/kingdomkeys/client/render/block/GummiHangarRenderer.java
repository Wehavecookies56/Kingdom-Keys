package online.kingdomkeys.kingdomkeys.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;
import online.kingdomkeys.kingdomkeys.block.GummiEditorBlock;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.entity.block.GummiEditorTileEntity;

public class GummiHangarRenderer implements BlockEntityRenderer<GummiEditorTileEntity> {

	public GummiHangarRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public boolean shouldRender(GummiEditorTileEntity blockEntity, Vec3 cameraPos) {
        return true;
    }

    @Override
    public boolean shouldRenderOffScreen(GummiEditorTileEntity blockEntity) {
        return true;
    }

    @Override
    public void render(GummiEditorTileEntity TE, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();
        {
            BlockState state = Minecraft.getInstance().level.getBlockState(TE.getBlockPos());
            if(state.getBlock() != ModBlocks.gummiEditor.get()) {
                matrixStackIn.popPose();
                return;
            }

            int counter = 0;
            Direction facing = state.getValue(GummiEditorBlock.FACING);
            int size = 9;
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    for (int z = 0; z < size; z++) {
                        boolean borderX = (x == 0 || x == size-1);
                        boolean borderY = (y == 0 || y == size-1);
                        boolean borderZ = (z == 0 || z == size-1);

                        counter++;
                        switch (facing) {
                            case NORTH -> {
                                if ((borderX && borderY) || (borderX && borderZ) || (borderY && borderZ)) {
                                    matrixStackIn.pushPose();
                                    {
                                        matrixStackIn.translate(-(size / 2) + x, +y - 0.9999F, z); //slightly above ground to avoid Zfighting
                                        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(counter % 2 == 1 ? Blocks.YELLOW_WOOL.defaultBlockState() : Blocks.BLACK_WOOL.defaultBlockState(), matrixStackIn, bufferIn, combinedLightIn, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.cutoutMipped());
                                    }
                                    matrixStackIn.popPose();
                                } else {
                                    if(y == 0){
                                        matrixStackIn.pushPose();
                                        {
                                            matrixStackIn.translate(-(size / 2) + x, +y - 0.9999F, z); //slightly above ground to avoid Zfighting
                                            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(Blocks.SEA_LANTERN.defaultBlockState(), matrixStackIn, bufferIn, combinedLightIn, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.cutoutMipped());
                                        }
                                        matrixStackIn.popPose();

                                    }
                                }
                            }
                            case SOUTH -> {
                                if ((borderX && borderY) || (borderX && borderZ) || (borderY && borderZ)) {
                                    matrixStackIn.pushPose();
                                    {
                                        matrixStackIn.translate(size / 2 - x, +y - 0.9999F, -z);
                                        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(counter % 2 == 1 ? Blocks.YELLOW_WOOL.defaultBlockState() : Blocks.BLACK_WOOL.defaultBlockState(), matrixStackIn, bufferIn, combinedLightIn, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.cutoutMipped());
                                    }
                                    matrixStackIn.popPose();
                                } else {
                                    if(y == 0){
                                        matrixStackIn.pushPose();
                                        {
                                            matrixStackIn.translate(size / 2 - x, +y - 0.9999F, -z);
                                            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(Blocks.SEA_LANTERN.defaultBlockState(), matrixStackIn, bufferIn, combinedLightIn, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.cutoutMipped());
                                        }
                                        matrixStackIn.popPose();

                                    }
                                }
                            }
                            case EAST -> {
                                if ((borderX && borderY) || (borderX && borderZ) || (borderY && borderZ)) {
                                    matrixStackIn.pushPose();
                                    {
                                        matrixStackIn.translate(-x, y - 0.9999F, -(size / 2) + z);
                                        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(counter % 2 == 1 ? Blocks.YELLOW_WOOL.defaultBlockState() : Blocks.BLACK_WOOL.defaultBlockState(), matrixStackIn, bufferIn, combinedLightIn, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.cutoutMipped());
                                    }
                                    matrixStackIn.popPose();
                                } else {
                                    if(y == 0){
                                        matrixStackIn.pushPose();
                                        {
                                            matrixStackIn.translate(-x, y - 0.9999F, -(size / 2) + z);
                                            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(Blocks.SEA_LANTERN.defaultBlockState(), matrixStackIn, bufferIn, combinedLightIn, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.cutoutMipped());
                                        }
                                        matrixStackIn.popPose();
                                    }
                                }
                            }
                            case WEST -> {
                                if ((borderX && borderY) || (borderX && borderZ) || (borderY && borderZ)) {
                                    matrixStackIn.pushPose();
                                    {
                                        matrixStackIn.translate(x, y - 0.9999F, -(size / 2) + z);
                                        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(counter % 2 == 1 ? Blocks.YELLOW_WOOL.defaultBlockState() : Blocks.BLACK_WOOL.defaultBlockState(), matrixStackIn, bufferIn, combinedLightIn, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.cutoutMipped());
                                    }
                                    matrixStackIn.popPose();
                                } else {
                                    if(y == 0){
                                        matrixStackIn.pushPose();
                                        {
                                            matrixStackIn.translate(x, y - 0.9999F, -(size / 2) + z);
                                            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(Blocks.SEA_LANTERN.defaultBlockState(), matrixStackIn, bufferIn, combinedLightIn, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.cutoutMipped());
                                        }
                                        matrixStackIn.popPose();

                                    }
                                }

                            }
                        }

                    }
                }
            }
        }
        matrixStackIn.popPose();
    }

    @Override
    public AABB getRenderBoundingBox(GummiEditorTileEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.offset(-10,-10,-10).getCenter(), pos.offset(10,10,10).getCenter());
    }
}
