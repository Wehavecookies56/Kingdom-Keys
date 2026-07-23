package online.kingdomkeys.kingdomkeys.entity.organization;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ScytheDashCoreEntity extends Entity {
	private static final int DURATION_TICKS = 40; // ~2s dash
	private static final int DAMAGE_INTERVAL_TICKS = 4;
	private static final float HIT_RADIUS = 1.3F;

	private float dmg;

	private static final EntityDataAccessor<Optional<UUID>> CASTER = SynchedEntityData.defineId(ScytheDashCoreEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<ItemStack> VISUAL_ITEM = SynchedEntityData.defineId(ScytheDashCoreEntity.class, EntityDataSerializers.ITEM_STACK);

	public ScytheDashCoreEntity(EntityType<?> type, Level level) {
		super(type, level);
		this.noPhysics = true;
		this.noCulling = true;
	}

	public ScytheDashCoreEntity(EntityType<?> type, Level level, Player caster, float dmg, ItemStack visualItem) {
		this(type, level);
		this.dmg = dmg;
		this.entityData.set(CASTER, Optional.of(caster.getUUID()));
		this.entityData.set(VISUAL_ITEM, visualItem);
		this.setPos(caster.getX(), caster.getY(), caster.getZ());
	}

	public Player getCaster() {
		if (this.entityData.get(CASTER).isEmpty()) return null;
		UUID uuid = this.entityData.get(CASTER).get();
		if (level() instanceof ServerLevel serverLevel) {
			Entity e = serverLevel.getEntity(uuid);
			return e instanceof Player player ? player : null;
		}

		for (Player p : level().players()) {
			if (p.getUUID().equals(uuid))
				return p;
		}
		return null;
	}

	public ItemStack getVisualItem() {
		return this.entityData.get(VISUAL_ITEM);
	}

	@Override
	public void tick() {
		super.tick();

		Player caster = getCaster();
		if (caster == null || !caster.isAlive() || this.tickCount > DURATION_TICKS) {
			if (!level().isClientSide) {
				if (caster != null) {
					caster.removeEffect(MobEffects.INVISIBILITY);
					caster.removeEffect(MobEffects.MOVEMENT_SPEED);
				}
				this.discard();
			}
			return;
		}

		this.setPos(caster.getX(), caster.getY(), caster.getZ());

		if (!level().isClientSide && tickCount % DAMAGE_INTERVAL_TICKS == 0) {
			damageNearby(caster);
		}
	}

	private void damageNearby(Player caster) {
		List<LivingEntity> nearby = Utils.getLivingEntitiesInRadiusExcludingParty(caster, this, HIT_RADIUS, HIT_RADIUS, HIT_RADIUS);
		for (LivingEntity target : nearby) {
			target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.DARKNESS, this, caster), dmg);
			target.invulnerableTime = 0;
		}
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(CASTER, Optional.empty());
		builder.define(VISUAL_ITEM, ItemStack.EMPTY);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
	}

	@Override
	public boolean isPickable() {
		return false;
	}
}
