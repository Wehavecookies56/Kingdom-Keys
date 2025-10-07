package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

public class GummiShipEntity extends Entity implements IEntityWithComplexSpawn {// PigEntity {

	public final static int MAX_TICKS = 30;

	CompoundTag data;
	public GummiStructure structure;

	public GummiShipEntity(EntityType<? extends Entity> type, Level world) {
		super(type, world);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
		pBuilder.define(DATA, new CompoundTag());
	}

	public GummiShipEntity(Level world, GummiStructure gummiStruct) {
		this(ModEntities.TYPE_GUMMI_SHIP.get(), world);
		structure = gummiStruct;
		this.setData(structure.serializeNBT(level().registryAccess()));
	}

	@Override
	public void tick() {
		super.tick();
		//this.kill();
	}

	public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1000.0D)
                .add(Attributes.FOLLOW_RANGE, 0.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
				.add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                ;
    }
	private static final EntityDataAccessor<CompoundTag> DATA = SynchedEntityData.defineId(GummiShipEntity.class, EntityDataSerializers.COMPOUND_TAG);

	public CompoundTag getData() {
		return data;
	}

	public void setData(CompoundTag struct) {
		this.entityData.set(DATA, struct);
		if (structure == null)
			structure = new GummiStructure(7, 7, 7);
		structure.deserializeNBT(level().registryAccess(), struct);
	}


	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		super.onSyncedDataUpdated(key);
		if (key.equals(DATA)) {
			CompoundTag tag = this.entityData.get(DATA);
			if (structure == null)
				structure = new GummiStructure(7, 7, 7);
			structure.deserializeNBT(level().registryAccess(), tag);
		}
	}


	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		compound.put("data",structure.serializeNBT(this.level().registryAccess()));

	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		this.setData(compound.getCompound("data"));

	}

	public CompoundTag getDataManager() {
		return this.entityData.get(DATA);
	}

	@Override
	public void writeSpawnData(RegistryFriendlyByteBuf buf) {
		CompoundTag nbt = new CompoundTag();
		nbt = structure.serializeNBT(level().registryAccess());
		buf.writeNbt(nbt);
	}

	@Override
	public void readSpawnData(RegistryFriendlyByteBuf buf) {
		CompoundTag nbt = buf.readNbt();
		if (nbt != null) {
			if (structure == null)
				structure = new GummiStructure(7, 7, 7);
			structure.deserializeNBT(level().registryAccess(), nbt);
			this.setData(nbt);
		}
	}


	public static class GummiStructure implements INBTSerializable<CompoundTag> {
		public BlockState[][][] blocks;
		public int width, height, depth;

		public GummiStructure(int width, int height, int depth) {
			this.width = width;
			this.height = height;
			this.depth = depth;
			blocks = new BlockState[width][height][depth];
		}

		public GummiStructure(int width, int height, int depth, Level level, BlockPos pos) {
			this(width, height, depth);
			BlockPos.MutableBlockPos mutableBlockPos = pos.mutable();
			for (int z = 0; z < depth; ++z) {
				for (int y = 0; y < height; ++y) {
					for (int x = 0; x < width; ++x) {
						mutableBlockPos.move(x, y, z);
						blocks[x][y][z] = level.getBlockState(mutableBlockPos);
					}
				}
			}
		}

		@Override
		public CompoundTag serializeNBT(HolderLookup.Provider provider) {
			CompoundTag tag = new CompoundTag();
			tag.putInt("width", width);
			tag.putInt("height", height);
			tag.putInt("depth", depth);

			int index = 0;
			for (int z = 0; z < depth; ++z) {
				for (int y = 0; y < height; ++y) {
					for (int x = 0; x < width; ++x) {
						BlockState state = blocks[x][y][z];
						if (state != null) {
							tag.put("block_" + index, NbtUtils.writeBlockState(state));
						}
						index++;
					}
				}
			}
			return tag;
		}

		@Override
		public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
			width = tag.getInt("width");
			height = tag.getInt("height");
			depth = tag.getInt("depth");
			blocks = new BlockState[width][height][depth];

			int index = 0;
			for (int z = 0; z < depth; ++z) {
				for (int y = 0; y < height; ++y) {
					for (int x = 0; x < width; ++x) {
						String key = "block_" + index;
						if (tag.contains(key, Tag.TAG_COMPOUND)) {
							blocks[x][y][z] = NbtUtils.readBlockState(provider.lookupOrThrow(Registries.BLOCK), tag.getCompound(key));
						} else {
							blocks[x][y][z] = null;
						}
						index++;
					}
				}
			}
		}

	}

}
