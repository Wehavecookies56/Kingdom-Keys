package online.kingdomkeys.kingdomkeys.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiHangarBlock;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.entity.block.GummiHangarTileEntity;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;
import online.kingdomkeys.kingdomkeys.lib.LineDisplay;

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
            int size = GummiHangarBlock.getSize(state.getValue(GummiHangarBlock.LEVEL));
            VertexConsumer vertexLines = bufferIn.getBuffer(RenderType.LINES);

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

            float dist = (float) Math.sqrt(Minecraft.getInstance().player.distanceToSqr(TE.getBlockPos().getX(), TE.getBlockPos().getY(), TE.getBlockPos().getZ()));
            LineDisplay perimeter = state.getValue(GummiHangarBlock.SHOW_LINES);
            if(dist < 80) {
                float a = (80-dist)/100F;
                if(perimeter == LineDisplay.ODD) {
                    float r = 0.3F, g = 0.8F, b = 1F;

                    LevelRenderer.renderLineBox(matrixStackIn, vertexLines, origin.x(), origin.y(), origin.z(), dest.x(), dest.y(), dest.z(), r, g, b, a);
                    // X shape
                    ClientUtils.drawLine(vertexLines, matrixStackIn, origin.x(), origin.y(), origin.z(), dest.x(), origin.y(), dest.z(), r, g, b, a);
                    ClientUtils.drawLine(vertexLines, matrixStackIn, dest.x(), origin.y(), origin.z(), origin.x(), origin.y(), dest.z(), r, g, b, a);

                } else if(perimeter == LineDisplay.EVEN) {
                    float r = 1F, g = 0.4F, b = 1F;
                    switch(facing){ //It has to make the box based on rotation
                        case NORTH -> {
                            LevelRenderer.renderLineBox(matrixStackIn, vertexLines, origin.x()+1, origin.y(), origin.z(), dest.x(), dest.y(), dest.z(), r, g, b, a);
                            // X shape
                            ClientUtils.drawLine(vertexLines, matrixStackIn, origin.x()+1, origin.y(), origin.z(), dest.x(), origin.y(), dest.z(), r, g, b, a);
                            ClientUtils.drawLine(vertexLines, matrixStackIn, dest.x(), origin.y(), origin.z(), origin.x()+1, origin.y(), dest.z(), r, g, b, a);
                        }
                        case SOUTH -> {
                            LevelRenderer.renderLineBox(matrixStackIn, vertexLines, origin.x(), origin.y(), origin.z(), dest.x()-1, dest.y(), dest.z(), r, g, b, a);
                            // X shape
                            ClientUtils.drawLine(vertexLines, matrixStackIn, origin.x(), origin.y(), origin.z(), dest.x()-1, origin.y(), dest.z(), r, g, b, a);
                            ClientUtils.drawLine(vertexLines, matrixStackIn, dest.x()-1, origin.y(), origin.z(), origin.x(), origin.y(), dest.z(), r, g, b, a);
                        }
                        case WEST -> {
                            LevelRenderer.renderLineBox(matrixStackIn, vertexLines, origin.x(), origin.y(), origin.z(), dest.x(), dest.y(), dest.z()-1, r, g, b, a);
                            // X shape
                            ClientUtils.drawLine(vertexLines, matrixStackIn, origin.x(), origin.y(), origin.z(), dest.x(), origin.y(), dest.z()-1, r, g, b, a);
                            ClientUtils.drawLine(vertexLines, matrixStackIn, dest.x(), origin.y(), origin.z(), origin.x(), origin.y(), dest.z()-1, r, g, b, a);
                        }
                        case EAST -> {
                            LevelRenderer.renderLineBox(matrixStackIn, vertexLines, origin.x(), origin.y(), origin.z()+1, dest.x(), dest.y(), dest.z(), r, g, b, a);
                            // X shape
                            ClientUtils.drawLine(vertexLines, matrixStackIn, origin.x(), origin.y(), origin.z()+1, dest.x(), origin.y(), dest.z(), r, g, b, a);
                            ClientUtils.drawLine(vertexLines, matrixStackIn, dest.x(), origin.y(), origin.z()+1, origin.x(), origin.y(), dest.z(), r, g, b, a);
                        }
                    }
                }
            }

            if(state.getValue(GummiHangarBlock.DISPLAY_BLUEPRINT)) {
                ItemStack stack = TE.inventory.get().getStackInSlot(0);
                if (stack.is(ModItems.gummiShipBlueprint.get())) {
                    GummiStructure struct = stack.get(ModComponents.GUMMI_STRUCTURE);
                    if (struct != null) {
                        int offsetX = 0;
                        int offsetZ = 0;

                        switch (facing) {
                            case NORTH -> { offsetX = -(size/2)-1; offsetZ = -size-1; }
                            case SOUTH -> { offsetX = -(size/2); offsetZ = -size; }
                            case EAST  -> { offsetX = -(size/2)-1; offsetZ = -size;
                                matrixStackIn.mulPose(Axis.YP.rotationDegrees(180));}
                            case WEST  -> { offsetX = -(size/2);  offsetZ =-size-1;
                                matrixStackIn.mulPose(Axis.YP.rotationDegrees(180));}
                        }

                        int w = struct.getWidth();
                        int h = struct.getHeight();
                        int d = struct.getDepth();
                        matrixStackIn.mulPose(Axis.YP.rotationDegrees(state.getValue(GummiHangarBlock.FACING).toYRot()));
                        matrixStackIn.translate(offsetX,0,offsetZ);

                        for (int x = 0; x < w; x++) {
                            for (int y = 0; y < h; y++) {
                                for (int z = 0; z < d; z++) {
                                    BlockState expected = struct.getBlocks()[x][y][z];
                                    if (expected == null || expected.isAir())
                                        continue;

                                    BlockPos worldPos = TE.getBlockPos().offset(offsetX + x, y, offsetZ + z);
                                    BlockState current = TE.getLevel().getBlockState(worldPos);
                                    if (current.equals(expected))
                                        continue;

                                    matrixStackIn.pushPose();
                                    {
                                        matrixStackIn.translate(x, y, z);
                                        ClientUtils.renderSingleBlock(expected, matrixStackIn, bufferIn, 0xF000F0, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, 0.75F);
                                    }
                                    matrixStackIn.popPose();
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
    public AABB getRenderBoundingBox(GummiHangarTileEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.offset(-10,-10,-10).getCenter(), pos.offset(10,10,10).getCenter());
    }
}
