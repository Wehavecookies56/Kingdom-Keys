package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class EntityHelper {

	public static final DataParameter<Integer> ANIMATION = EntityDataManager.<Integer>createKey(MobEntity.class, DataSerializers.VARINT);
	public static final DataParameter<Integer> STATE = EntityDataManager.<Integer>createKey(MobEntity.class, DataSerializers.VARINT);

	public static void setState(Entity e, int i) {
		e.getDataManager().set(STATE, i);
	}

	public static double percentage(double i, double j) {
		return (i * 100) / j;
	}

	public static int getState(Entity e) {
		return e.getDataManager().get(STATE);
	}

	public static void setAnimation(Entity e, int i) {
		e.getDataManager().set(ANIMATION, i);
	}

	public static int getAnimation(Entity e) {
		return e.getDataManager().get(ANIMATION);
	}

	public enum Dir {
		SOUTH, SOUTH_EAST, EAST, NORTH, NORTH_EAST, NORTH_WEST, WEST, SOUTH_WEST;
	}

	public enum MobType {
		HEARTLESS_PUREBLOOD, HEARTLESS_EMBLEM, NOBODY, NPC;
	}

	public static Dir get8Directions(Entity e) {
		switch (MathHelper.floor(e.rotationYaw * 8.0F / 360.0F + 0.5D) & 7) {
		case 0:
			return Dir.SOUTH;
		case 1:
			return Dir.SOUTH_WEST;
		case 2:
			return Dir.WEST;
		case 3:
			return Dir.NORTH_WEST;
		case 4:
			return Dir.NORTH;
		case 5:
			return Dir.NORTH_EAST;
		case 6:
			return Dir.EAST;
		case 7:
			return Dir.SOUTH_EAST;
		}
		return null;
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

		framesCount -= 1 * framesPerSlot;

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
		AxisAlignedBB aabb = new AxisAlignedBB(e.getPosX(), e.getPosY(), e.getPosZ(), e.getPosX() + 1, e.getPosY() + 1, e.getPosZ() + 1).grow(radius, radius, radius);
		List<LivingEntity> list = e.world.getEntitiesWithinAABB(LivingEntity.class, aabb);
		list.remove(e);
		return list;
	}
}