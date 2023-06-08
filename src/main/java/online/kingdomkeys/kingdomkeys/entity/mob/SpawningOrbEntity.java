package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.utils.BaseTeleporter;

public class SpawningOrbEntity extends Monster {

	Monster mob;
	boolean portal;
	
	//Natural
	public SpawningOrbEntity(EntityType<? extends SpawningOrbEntity> type, Level worldIn) {
		super(type, worldIn);
		Player player = Utils.getClosestPlayer(this, worldIn);
		
		if(player != null) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			this.mob = ModEntities.getRandomEnemy(playerData.getLevel(), level);
			setEntityType(((IKHMob)this.mob).getKHMobType().name());
			
			if(ModConfigs.mobLevelingUp) {
				int avgLevel = playerData.getLevel();
				
				if(ModCapabilities.getWorld(level).getPartyFromMember(player.getUUID())!= null) {
					Party p = ModCapabilities.getWorld(level).getPartyFromMember(player.getUUID());
					int total = 0;
					int membersOnline = 0;
					for(Party.Member m : p.getMembers()) {
						if(Utils.getPlayerByName(worldIn, m.getUsername())!= null){
							total += ModCapabilities.getPlayer(Utils.getPlayerByName(worldIn, m.getUsername())).getLevel();
							membersOnline++;
						}
					}
					if (membersOnline == 0) {
						avgLevel = 1;
						KingdomKeys.LOGGER.warn("0 members online for this party, this should not be happening, in world " + worldIn.dimension().location().toString());
					} else {
						avgLevel = total / membersOnline;
					}
				}
				
				int level = avgLevel - this.level.random.nextInt(6) + 2;
				level = Utils.clamp(level, 1, 100);
				
				IGlobalCapabilities mobData = ModCapabilities.getGlobal(mob);
				if(mobData != null) {
					mobData.setLevel(level);
					PacketHandler.syncToAllAround((LivingEntity) mob, mobData);
				}
			}
		}
	}

	//Command
	public SpawningOrbEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_SPAWNING_ORB.get(), world);
	}
	
	public SpawningOrbEntity(Level world) {
		super(ModEntities.TYPE_SPAWNING_ORB.get(), world);
	}
	
	@Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
    	return ModCapabilities.getWorld((Level) worldIn).getHeartlessSpawnLevel() > 0;
    }
	
	@Override
	public boolean hurt(DamageSource source, float amount) {
		return false;
	}
	
	@Override
	public void tick() {
		//System.out.println(getPosition());
		if(tickCount == 1 && !level.isClientSide && this.mob != null) {
			if(getLevel().random.nextDouble() < 0.1) {
				setPortal(true);
			}
			setEntityType(((IKHMob)this.mob).getKHMobType().name());
		}
		SimpleParticleType particle = getEntityType().equals(MobType.NOBODY.name()) ? ParticleTypes.END_ROD : ParticleTypes.DRAGON_BREATH;

		if(tickCount > 10 && tickCount < 60) {
			double x = getX() + (level.random.nextDouble() - 0.5) * 2;
			double y = getY() + (level.random.nextDouble() - 0.5) * 2 + 1;
			double z = getZ() + (level.random.nextDouble() - 0.5) * 2;
			level.addParticle(particle, x, y, z, 0.0D, 0.0D, 0.0D);
		}
		
		if(tickCount == 70) {
			if(!level.isClientSide) {
				if(this.mob != null) {
					this.mob.setPos(this.getX(),this.getY(),this.getZ());
					this.mob.heal(this.mob.getMaxHealth());
					level.addFreshEntity(this.mob);
				}
			} else {
				float radius = 0.5F;
				double X = getX();
				double Y = getY();
				double Z = getZ();

				for (int t = 1; t < 360; t += 20) {
					for (int s = 1; s < 360 ; s += 20) {
						double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
						double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
						double y = Y + (radius * Math.cos(Math.toRadians(t))) +1;
						level.addParticle(particle, x, y, z, (level.random.nextDouble()-0.5) / 4,  (level.random.nextDouble()-0.5) / 4,  (level.random.nextDouble()-0.5) / 4);
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
	public void playerTouch(Player nPlayer) {
		if(getPortal()) {
			ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, new ResourceLocation("kingdomkeys:realm_of_darkness"));
			BlockPos coords = nPlayer.getServer().getLevel(dimension).getSharedSpawnPos();
			nPlayer.changeDimension(nPlayer.getServer().getLevel(dimension), new BaseTeleporter(coords.getX(), coords.getY(), coords.getZ()));
			nPlayer.sendSystemMessage(Component.translatable("You have been teleported to " + dimension.location().toString()));
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
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(ENTITY_TYPE, "");
	}

	@Override
	public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
		return false;
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return (Packet<ClientGamePacketListener>) NetworkHooks.getEntitySpawningPacket(this);
	}
}
