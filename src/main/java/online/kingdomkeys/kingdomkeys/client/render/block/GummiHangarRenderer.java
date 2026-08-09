package online.kingdomkeys.kingdomkeys.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiBlockBase;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiHangarBlock;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.entity.block.GummiHangarTileEntity;
import online.kingdomkeys.kingdomkeys.item.GummiShipBlueprintItem;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;
import online.kingdomkeys.kingdomkeys.lib.LineDisplay;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class GummiHangarRenderer implements BlockEntityRenderer<GummiHangarTileEntity> {

	private GummiStructure fittedSource;
	private GummiStructure fitted;
	private int fittedSize;

	public GummiHangarRenderer(BlockEntityRendererProvider.Context context) {

    }

	private GummiStructure fitted(GummiStructure blueprint, int size) {
		if (fittedSource != blueprint || fittedSize != size) {
			fittedSource = blueprint;
			fittedSize = size;
			fitted = Utils.resizeStructure(blueprint, size);
		}

		return fitted;
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

                    double x1 = origin.x(), x2 = dest.x(), z1 = origin.z(), z2 = dest.z();
                    switch (facing) {
                        case NORTH -> { x1 += 1; z2 -= 1; }
                        case SOUTH -> { x2 -= 1; z2 += 1; }
                        case EAST -> { x1 += 1; z1 += 1; }
                        case WEST -> { x2 -= 1; z2 -= 1; }
                    }

                    LevelRenderer.renderLineBox(matrixStackIn, vertexLines, x1, origin.y(), z1, x2, dest.y(), z2, r, g, b, a);
                    // X shape
                    ClientUtils.drawLine(vertexLines, matrixStackIn, x1, origin.y(), z1, x2, origin.y(), z2, r, g, b, a);
                    ClientUtils.drawLine(vertexLines, matrixStackIn, x2, origin.y(), z1, x1, origin.y(), z2, r, g, b, a);
                }
            }

            if(state.getValue(GummiHangarBlock.DISPLAY_BLUEPRINT)) {
                ItemStack stack = TE.inventory.get().getStackInSlot(0);
                if (GummiShipBlueprintItem.isBlueprint(stack)) {
                    GummiStructure blueprint = stack.get(ModComponents.GUMMI_STRUCTURE);
                    int[] offsets = Utils.getShipOffset(facing, size);

                    if (blueprint != null && offsets != null && blueprint.getWidth() <= size) {
                        GummiStructure struct = fitted(blueprint, size);
                        Rotation rotation = switch (facing) {
                            case NORTH -> Rotation.CLOCKWISE_180;
                            case WEST -> Rotation.CLOCKWISE_90;
                            case EAST -> Rotation.COUNTERCLOCKWISE_90;
                            default -> Rotation.NONE;
                        };

                        int max = size - 1;

                        for (int x = 0; x < size; x++) {
                            for (int y = 0; y < size; y++) {
                                for (int z = 0; z < size; z++) {
                                    BlockState expected = struct.getBlocks()[x][y][z];
                                    if (expected == null || expected.isAir())
                                        continue;

                                    expected = Utils.rotateBlock(expected, rotation);

                                    int rx = x, rz = z;
                                    switch (facing) {
                                        case NORTH -> { rx = max - x; rz = max - z; }
                                        case EAST -> { rx = z; rz = max - x; }
                                        case WEST -> { rx = max - z; rz = x; }
                                    }

                                    BlockPos worldPos = TE.getBlockPos().offset(offsets[0] + rx, y, offsets[1] + rz);
                                    BlockState current = TE.getLevel().getBlockState(worldPos);
                                    // Not equals: several orientations of the same piece are indistinguishable
                                    // once placed, and marking those as missing would be wrong
                                    if (GummiBlockBase.sameAppearance(current, expected))
                                        continue;

                                    matrixStackIn.pushPose();
                                    {
                                        matrixStackIn.translate(offsets[0] + rx, y, offsets[1] + rz);
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
