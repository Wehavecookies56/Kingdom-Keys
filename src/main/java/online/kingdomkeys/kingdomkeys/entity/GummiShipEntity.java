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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;

public class GummiShipEntity extends Entity implements IEntityWithComplexSpawn {

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
		if (structure == null || structure.getBlocks().length == 0) {
			this.kill();
		} else {
			boolean empty = true;
			for (int x = 0; x < structure.getWidth(); x++) {
				for (int y = 0; y < structure.getHeight(); y++) {
					for (int z = 0; z < structure.getDepth(); z++) {
						if (structure.getBlocks()[x][y][z] != null) {
							empty = false;
						}
					}
				}
			}
			if (empty) {
				this.kill();
			}
		}
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		player.startRiding(this);
		return super.interact(player, hand);
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
		structure = new GummiStructure(level().registryAccess(), struct);
	}


	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		super.onSyncedDataUpdated(key);
		if (key.equals(DATA)) {
			CompoundTag tag = this.entityData.get(DATA);
			structure = new GummiStructure(level().registryAccess(), tag);
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
		CompoundTag nbt = structure.serializeNBT(level().registryAccess());
		buf.writeNbt(nbt);
	}

	@Override
	public void readSpawnData(RegistryFriendlyByteBuf buf) {
		CompoundTag nbt = buf.readNbt();
		if (nbt != null) {
			structure = new GummiStructure(level().registryAccess(), nbt);
			this.setData(nbt);
		}
	}
}
