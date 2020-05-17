package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;

public class EntityHelper {

	public static final DataParameter<Integer> ANIMATION = EntityDataManager.<Integer>createKey(MobEntity.class, DataSerializers.VARINT);
	public static final DataParameter<Integer> STATE = EntityDataManager.<Integer>createKey(MobEntity.class, DataSerializers.VARINT);

	public static void setState(Entity e, int i) {
		e.getDataManager().set(STATE, i); 
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
}