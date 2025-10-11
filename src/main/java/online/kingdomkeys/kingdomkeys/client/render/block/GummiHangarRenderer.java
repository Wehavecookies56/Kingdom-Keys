package online.kingdomkeys.kingdomkeys.client.render.block;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.block.GummiHangarBlock;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.entity.block.GummiHangarTileEntity;

public class GummiHangarRenderer implements BlockEntityRenderer<GummiHangarTileEntity> {

	public GummiHangarRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public boolean shouldRender(GummiHangarTileEntity blockEntity, Vec3 cameraPos) {
        return true;
    }

    @Override
    public boolean shouldRenderOffScreen(GummiHangarTileEntity blockEntity) {
        return true;
    }

    @Override
    public void render(GummiHangarTileEntity TE, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();
        {
            BlockState state = Minecraft.getInstance().level.getBlockState(TE.getBlockPos());
            if(state.getBlock() != ModBlocks.gummiHangar.get()) {
                matrixStackIn.popPose();
                return;
            }

            Direction facing = state.getValue(GummiHangarBlock.FACING);
            int size = 7;
            VertexConsumer a = bufferIn.getBuffer(RenderType.LINES);

            Vec3 origin = new Vec3(0,0,0);
            Vec3 dest = new Vec3(size,size,size);
            switch(facing){
                case NORTH -> {
                    origin = new Vec3(-size/2,0,1);
                    dest = new Vec3(size/2+1,size,size+1);
                }
                case SOUTH -> {
                    origin = new Vec3(-size/2,0,0);
                    dest = new Vec3(size/2+1,size,-size);
                }
                case WEST -> {
                    origin = new Vec3(1,0,-size/2);
                    dest = new Vec3(size+1,size,size/2+1);
                }
                case EAST -> {
                    origin = new Vec3(-size,0,-size/2);
                    dest = new Vec3(0,size,size/2+1);
                }
            }

            if(state.getValue(GummiHangarBlock.SHOW_LINES))
                LevelRenderer.renderLineBox(matrixStackIn,a,origin.x(),origin.y(),origin.z(),dest.x(),dest.y(),dest.z(),0.3F,0.9F,1,0.3F);

           /* size= 9;
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
                                        //Minecraft.getInstance().getBlockRenderer().renderSingleBlock(counter % 2 == 1 ? Blocks.YELLOW_WOOL.defaultBlockState() : Blocks.BLACK_WOOL.defaultBlockState(), matrixStackIn, bufferIn, combinedLightIn, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.cutoutMipped());
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
            }*/
        }
        matrixStackIn.popPose();
    }

    @Override
    public AABB getRenderBoundingBox(GummiHangarTileEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.offset(-10,-10,-10).getCenter(), pos.offset(10,10,10).getCenter());
    }
}
