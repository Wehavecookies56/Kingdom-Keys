package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;

import java.util.ArrayList;

public class SpawningOrbEntity extends Monster {

	ArrayList<Monster> mobs = new ArrayList<>();
	boolean portal;

	//Natural
	public SpawningOrbEntity(EntityType<? extends SpawningOrbEntity> type, Level worldIn) {
		super(type, worldIn);
		Player player = Utils.getClosestPlayer(this, worldIn);

		if(player != null) {
			PlayerData playerData = PlayerData.get(player);
			if(playerData == null)
				return;

			int randomTimes = worldIn.random.nextInt(playerData.getNumberOfAbilitiesEquipped(ModAbilities.ENCOUNTER_PLUS)+1);

			for(int i=0;i<=randomTimes;i++) {
				this.mobs.add(ModEntities.getRandomEnemy(playerData.getLevel(), level(), forcedType(worldIn)));

				int randomLevel = Utils.getRandomMobLevel(player);
				GlobalData mobData = GlobalData.get(mobs.get(i));
				if(mobData != null) {
					mobData.setLevel(randomLevel);
					PacketHandler.syncToAllAround(mobs.get(i), mobData);
				}
			}

			//Portal type is based on the first mob type
			setEntityType(((IKHMob)this.mobs.getFirst()).getKHMobType().name());
		}
	}

	@Override
	public void tick() {
		if(tickCount == 1 && !level().isClientSide && this.mobs != null && !this.mobs.isEmpty()) {
			float prob = 0.8F;
			if(level().dimension().location().equals(Level.NETHER.location()))
				prob = 0.14F;
			if(level().dimension().location().equals(Level.END.location()))
				prob = 0.20F;

			if(level().random.nextDouble() < prob) {
				setPortal(true);
			}
			setEntityType(((IKHMob)this.mobs.getFirst()).getKHMobType().name());
		}
		SimpleParticleType particle = getEntityType().equals(MobType.NOBODY.name()) ? ParticleTypes.END_ROD : ParticleTypes.DRAGON_BREATH;

		if(tickCount > 10 && tickCount < 60) {
			double x = getX() + (level().random.nextDouble() - 0.5) * 2;
			double y = getY() + (level().random.nextDouble() - 0.5) * 2 + 1;
			double z = getZ() + (level().random.nextDouble() - 0.5) * 2;
			level().addParticle(particle, x, y, z, 0.0D, 0.0D, 0.0D);
		}

		if(tickCount == 70) {
			if(!level().isClientSide) {
				if (this.mobs != null && !this.mobs.isEmpty()) {
					/*if(this.mobs.getFirst() instanceof IKHMob mob){
						if(mob.getKHMobType() == MobType.NOBODY){
							level().playSound(null, blockPosition(), ModSounds.nobodySpawn.get(), SoundSource.HOSTILE, 1F, 1F);
						} else {
							level().playSound(null, blockPosition(), ModSounds.heartlessSpawn.get(), SoundSource.HOSTILE, 1F, 1F);
						}
					}*/
					level().playSound(null, blockPosition(), ModSounds.heartlessSpawn.get(), SoundSource.HOSTILE, 1F, 1F);


					for (Monster mob : mobs) {
						mob.setPos(this.getX(), this.getY(), this.getZ());
						mob.heal(mob.getMaxHealth());
						level().addFreshEntity(mob);
					}
				}
			} else {
				float radius = 0.5F;
				double X = getX();
				double Y = getY();
				double Z = getZ();

				for (int t = 1; t < 360; t += 20) {
					double radT = Math.toRadians(t);
					double sinT = Math.sin(radT);
					double y = Y + (radius * Math.cos(radT)) +1;
					for (int s = 1; s < 360 ; s += 20) {
						double radS = Math.toRadians(s);
						double x = X + (radius * Math.cos(radS) * sinT);
						double z = Z + (radius * Math.sin(radS) * sinT);
						level().addParticle(particle, x, y, z, (level().random.nextDouble()-0.5) / 4,  (level().random.nextDouble()-0.5) / 4,  (level().random.nextDouble()-0.5) / 4);
					}
				}
			}
		}

		if(tickCount >= 100) {
			remove(RemovalReason.KILLED);
		}

		super.tick();
	}

	public void setPortal(boolean portal) {
		this.portal = portal;
	}

	public boolean getPortal() {
		return portal;
	}


	@Override
	public boolean hurt(DamageSource source, float amount) {
		return false;
	}

	/** Worlds that only ever see one kind of enemy. Null anywhere else, which rolls for it as usual. */
	private static MobType forcedType(Level level) {
		return level.dimension().equals(ModDimensions.DESTINY_ISLANDS) ? MobType.HEARTLESS_PUREBLOOD : null;
	}

	/** Worlds the darkness only reaches after sundown, rather than wherever it finds a shadow to hide in */
	private static boolean nightOnly(Level level) {
		return level.dimension().equals(ModDimensions.DESTINY_ISLANDS);
	}

	@Override
	public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
		if(!(worldIn instanceof Level level))
			return true;

		// The light test alone would still let them spawn in caves
		if(nightOnly(level) && level.isDay())
			return false;

		return WorldData.get(level.getServer()).getHeartlessSpawnLevel() > 0;
	}

	@Override
	public void playerTouch(Player nPlayer) {
		if(getPortal()) {
			ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, KingdomKeys.rl("realm_of_darkness"));
			PlayerData playerData = PlayerData.get(nPlayer);
			if(playerData == null)
				return;

			playerData.setRespawnROD(true);
			if(!nPlayer.level().isClientSide()) {
				PacketHandler.sendTo(new SCSyncPlayerData(nPlayer), (ServerPlayer)nPlayer);
			}

			BlockPos coords = nPlayer.getServer().getLevel(dimension).getSharedSpawnPos();
			nPlayer.changeDimension(new DimensionTransition(nPlayer.getServer().getLevel(dimension), new Vec3(coords.getX(), coords.getY(), coords.getZ()), Vec3.ZERO, nPlayer.getYRot(), nPlayer.getXRot(), entity -> {}));
			nPlayer.sendSystemMessage(Component.translatable("kingdomkeys.teleport.teleported_to", dimension.location().getPath()));
		}
		super.playerTouch(nPlayer);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createLivingAttributes()
				.add(Attributes.FOLLOW_RANGE, 35.0D)
				.add(Attributes.MOVEMENT_SPEED, 0D)
				.add(Attributes.MAX_HEALTH, 50.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1000.0D)
				.add(Attributes.ATTACK_DAMAGE, 4.0D)
				.add(Attributes.ATTACK_KNOCKBACK, 1.0D)
				;
	}

	private static final EntityDataAccessor<String> ENTITY_TYPE = SynchedEntityData.defineId(SpawningOrbEntity.class, EntityDataSerializers.STRING);

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		if (this.entityData.get(ENTITY_TYPE) != null) {
			compound.putString("entity", this.entityData.get(ENTITY_TYPE));
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.entityData.set(ENTITY_TYPE, compound.getString("entity"));
	}

	public String getEntityType() {
		return this.getEntityData().get(ENTITY_TYPE);
	}

	public void setEntityType(String type) {
		this.entityData.set(ENTITY_TYPE, type);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
		super.defineSynchedData(pBuilder);
		pBuilder.define(ENTITY_TYPE, "");
	}

	@Override
	public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
		return false;
	}
}