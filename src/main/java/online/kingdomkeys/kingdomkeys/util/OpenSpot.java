package online.kingdomkeys.kingdomkeys.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

public final class OpenSpot {
	private static final int[] HEIGHTS = {0, 1, -1, 2, -2, 3, -3};
	private static final int MAX_CLIMB = 12;

	public static Vec3 find(Level level, Vec3 eye, Vec3 center, float yaw, double[] distances, EntityType<?> type, double margin) {
		for (int i = 0; i < 16; i++) {
			double angle = Math.toRadians(yaw + (i + 1) / 2 * (i % 2 == 0 ? -22.5D : 22.5D));

			for (double distance : distances) {
				BlockPos column = BlockPos.containing(center.x - Math.sin(angle) * distance, center.y, center.z + Math.cos(angle) * distance);

				if (eye == null) {
					BlockPos top = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, column);
					if (Math.abs(top.getY() - column.getY()) <= MAX_CLIMB && fits(level, null, top, type, margin)) {
						return Vec3.atBottomCenterOf(top);
					}
					continue;
				}

				for (int dy : HEIGHTS) {
					if (fits(level, eye, column.above(dy), type, margin)) {
						return Vec3.atBottomCenterOf(column.above(dy));
					}
				}
			}
		}

		return null;
	}

	private static boolean fits(Level level, Vec3 eye, BlockPos pos, EntityType<?> type, double margin) {
		AABB box = type.getDimensions().makeBoundingBox(Vec3.atBottomCenterOf(pos)).inflate(margin, 0D, margin);
		return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP) && level.noCollision(box) && !level.containsAnyLiquid(box)
				&& (eye == null || level.clip(new ClipContext(eye, box.getCenter(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty())).getType() == HitResult.Type.MISS);
	}
}
