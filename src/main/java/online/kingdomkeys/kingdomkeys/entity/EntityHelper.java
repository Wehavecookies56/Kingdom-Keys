package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class EntityHelper {

	public enum Dir {
		SOUTH, SOUTH_EAST, EAST, NORTH, NORTH_EAST, NORTH_WEST, WEST, SOUTH_WEST
    }

	public enum MobType {
		HEARTLESS_PUREBLOOD, HEARTLESS_EMBLEM, NOBODY, NPC, BOSS
    }

	public static Dir get8Directions(Entity e) {
        return switch (Mth.floor(e.getYRot() * 8.0F / 360.0F + 0.5D) & 7) {
            case 0 -> Dir.SOUTH;
            case 1 -> Dir.SOUTH_WEST;
            case 2 -> Dir.WEST;
            case 3 -> Dir.NORTH_WEST;
            case 4 -> Dir.NORTH;
            case 5 -> Dir.NORTH_EAST;
            case 6 -> Dir.EAST;
            case 7 -> Dir.SOUTH_EAST;
            default -> null;
        };
    }

	public static double[] generateAnimationArray(double startPos, double minPos, double maxPos, double frameSkip, int framesPerSlot) {
		int framesCount = 0;
		double currentFrame = startPos;
		boolean hasReachedMaxPos = false;
		boolean hasReachedMinPos = false;

		for (double i = startPos; i <= maxPos; i += frameSkip)
			framesCount++;

		for (double i = maxPos; i > minPos; i -= frameSkip)
			framesCount++;

		for (double i = minPos; i <= startPos; i += frameSkip)
			framesCount++;

		framesCount *= framesPerSlot;

		framesCount -= framesPerSlot;

		double[] animation = new double[framesCount];

		for (int j = 0; j < framesCount; j++) {
			for (int i = 0; i < framesPerSlot; i++) {
				if (j + 1 < framesCount) {
					if (i > 0)
						j++;
					animation[j] = currentFrame;
				}
			}
			if (!hasReachedMaxPos && currentFrame < maxPos)
				currentFrame += frameSkip;
			else if (!hasReachedMinPos && hasReachedMaxPos && currentFrame > minPos)
				currentFrame -= frameSkip;
			else if (hasReachedMinPos && currentFrame < startPos)
				currentFrame += frameSkip;

			if (currentFrame >= maxPos)
				hasReachedMaxPos = true;
			if (currentFrame <= minPos)
				hasReachedMinPos = true;
		}

		return animation;
	}

	public static List<LivingEntity> getEntitiesNear(Entity e, double radius) {
		AABB aabb = new AABB(e.getX(), e.getY(), e.getZ(), e.getX() + 1, e.getY() + 1, e.getZ() + 1).inflate(radius, radius, radius);
		List<LivingEntity> list = e.level().getEntitiesOfClass(LivingEntity.class, aabb);
		list.remove(e);
		return list;
	}
}