package online.kingdomkeys.kingdomkeys.entity.organization;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CardRingCoreEntity extends ThrowableProjectile {
	private static final int CARD_COUNT = 13;
	private static final int MAX_TICKS = 300;
	private static final int CARD_INTERVAL_TICKS = 2;
	private static final int DAMAGE_INTERVAL_TICKS = 10;
	private static final float RADIUS = 5.2F;
	private static final float HALF_HEIGHT = 3.0F;
	private static final float EDGE_BAND = 1.0F;
	private static final float PUSHBACK_STRENGTH = 0.35F;

	private float dmg;
	private boolean cardsSpawned = false;
	private final List<KKThrowableEntity> cardEntities = new ArrayList<>();
	int cardCounter = 0;

	public CardRingCoreEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = false;
	}

	public CardRingCoreEntity(Level world, Player caster, float dmg) {
		super(ModEntities.TYPE_CARD_RING.get(), caster, world);
		this.dmg = dmg;
		this.setDeltaMovement(Vec3.ZERO);
	}

	@Override
	protected double getDefaultGravity() {
		return 0D;
	}

	@Override
	public void tick() {
		if (this.tickCount > MAX_TICKS || !(getOwner() instanceof Player caster) || !caster.isAlive()) {
			despawnCards();
			this.remove(RemovalReason.KILLED);
			return;
		}

		if (!level().isClientSide) {
			if (!cardsSpawned) {
				if(tickCount % 2 == 0) {
					spawnCardEntities(caster);
				}

			}

			containEntities(caster, tickCount % DAMAGE_INTERVAL_TICKS == 0);
		}

		super.tick();
	}

	private void spawnCardEntities(Player caster) {
		ItemStack fairGameStack = new ItemStack(ModItems.fairGame.get());

		//for (int i = 0; i < CARD_COUNT; i++) {
		double angle = (2 * Math.PI / CARD_COUNT) * cardCounter++;
		double x = getX() + RADIUS * Math.cos(angle);
		double z = getZ() + RADIUS * Math.sin(angle);

		// Face each card toward the center so they visually close the ring, instead of every one pointing the same default direction.
		double dx = getX() - x;
		double dz = getZ() - z;
		float yaw = (float) (Math.toDegrees(Math.atan2(dz, dx))) - 90F;

		KKThrowableEntity card = new KKThrowableEntity(level());
		card.setData(0F, caster.getUUID(), 0, fairGameStack);
		card.setStationary(true);
		card.setMaxTicks(MAX_TICKS + 40);
		card.setPos(x, getY() + HALF_HEIGHT * 0.5, z);
		card.setDeltaMovement(Vec3.ZERO);
		card.setFrozenYaw(yaw);
		level().addFreshEntity(card);
		cardEntities.add(card);

		if(cardCounter == CARD_COUNT){
			cardsSpawned = true;
		}
		//}
	}

	private void despawnCards() {
		for (KKThrowableEntity card : cardEntities) {
			card.discard();
		}
		cardEntities.clear();
	}

	private void containEntities(Player caster, boolean doDamagePulse) {
		List<LivingEntity> nearby = Utils.getLivingEntitiesInRadiusExcludingParty(caster, this, RADIUS + 2F, HALF_HEIGHT, RADIUS + 2F);

		for (LivingEntity target : nearby) {
			double dx = target.getX() - getX();
			double dz = target.getZ() - getZ();
			double horizontalDist = Math.sqrt(dx * dx + dz * dz);

			if (horizontalDist >= RADIUS - EDGE_BAND) {
				double inwardX = horizontalDist > 0.0001 ? -dx / horizontalDist : 0;
				double inwardZ = horizontalDist > 0.0001 ? -dz / horizontalDist : 0;

				target.setDeltaMovement(target.getDeltaMovement().add(inwardX * PUSHBACK_STRENGTH, 0.05D, inwardZ * PUSHBACK_STRENGTH));
				target.hurtMarked = true;

				if (doDamagePulse) {
					target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.DARKNESS, this, caster), dmg);
					target.invulnerableTime = 0;
				}
			}
		}

		if (doDamagePulse) {
			level().playSound(null, caster.blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 0.6F, 0.8F);
		}
	}

	@Override
	protected void onHit(HitResult result) {}

	private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(CardRingCoreEntity.class, EntityDataSerializers.OPTIONAL_UUID);

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		if (this.entityData.get(OWNER).isPresent()) {
			compound.putString("OwnerUUID", this.entityData.get(OWNER).get().toString());
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("OwnerUUID")) {
			this.entityData.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
		}
	}

	public Player getCaster() {
		return this.getEntityData().get(OWNER).isPresent() ? (Player) this.level().getPlayerByUUID(this.getEntityData().get(OWNER).get()) : null;
	}

	public void setCaster(UUID uuid) {
		this.entityData.set(OWNER, Optional.of(uuid));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
		pBuilder.define(OWNER, Optional.empty());
	}
}
