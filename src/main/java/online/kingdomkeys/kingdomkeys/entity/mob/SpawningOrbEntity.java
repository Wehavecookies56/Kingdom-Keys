package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class SpawningOrbEntity extends MonsterEntity {

	MonsterEntity mob;
	//Natural
	public SpawningOrbEntity(EntityType<? extends SpawningOrbEntity> type, World worldIn) {
		super(type, worldIn);
		PlayerEntity player = Utils.getClosestPlayer(this);				
		
		if(player != null) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			this.mob = ModEntities.getRandomEnemy(playerData.getLevel(), world);
			setEntityType(((IKHMob)this.mob).getMobType().name());
			
			if(ModConfigs.mobLevelingUp) {
				int avgLevel = playerData.getLevel();
				
				if(ModCapabilities.getWorld(world).getPartyFromMember(player.getUniqueID())!= null) {
					Party p = ModCapabilities.getWorld(world).getPartyFromMember(player.getUniqueID());
					int total = 0;
					int membersOnline = 0;
					for(Member m : p.getMembers()) {
						if(Utils.getPlayerByName(worldIn, m.getUsername())!= null){
							total += ModCapabilities.getPlayer(Utils.getPlayerByName(worldIn, m.getUsername())).getLevel();
							membersOnline++;
						}
					}
					avgLevel = total / membersOnline;
				}
				
				int level = avgLevel - world.rand.nextInt(6) + 2;
				level = Math.max(1, Math.min(100, level));
				this.mob.setCustomName(new TranslationTextComponent(this.mob.getDisplayName().getString()+" Lv."+level));
				this.mob.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Math.max(this.mob.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue() * (level * ModConfigs.mobLevelStats / 100), this.mob.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue()));
				this.mob.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.max(this.mob.getMaxHealth() * (level * ModConfigs.mobLevelStats / 100), this.mob.getMaxHealth()));
			}
		}
	}

	//Command
	public SpawningOrbEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_SPAWNING_ORB.get(), world);
	}
	
	public SpawningOrbEntity(World world) {
		super(ModEntities.TYPE_SPAWNING_ORB.get(), world);
	}
	
	@Override
    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
    	return ModCapabilities.getWorld((World)worldIn).getHeartlessSpawnLevel() > 0;
    }
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return false;
	}
	
	@Override
	public void tick() {
		//System.out.println(getPosition());
		if(ticksExisted == 1 && !world.isRemote && this.mob != null) {
			setEntityType(((IKHMob)this.mob).getMobType().name());
		}
		BasicParticleType particle = getEntityType().equals(MobType.NOBODY.name()) ? ParticleTypes.END_ROD : ParticleTypes.DRAGON_BREATH;

		if(ticksExisted > 10 && ticksExisted < 60) {
			double x = getPosX() + (world.rand.nextDouble() - 0.5) * 2;
			double y = getPosY() + (world.rand.nextDouble() - 0.5) * 2 + 1;
			double z = getPosZ() + (world.rand.nextDouble() - 0.5) * 2;
			world.addParticle(particle, x, y, z, 0.0D, 0.0D, 0.0D);
		}
		
		if(ticksExisted == 70) {
			if(!world.isRemote) {
				if(this.mob != null) {
					this.mob.setPosition(this.getPosX(),this.getPosY(),this.getPosZ());
					this.mob.heal(this.mob.getMaxHealth());
					world.addEntity(this.mob);
				}
			} else {
				float radius = 0.5F;
				double X = getPosX();
				double Y = getPosY();
				double Z = getPosZ();

				for (int t = 1; t < 360; t += 20) {
					for (int s = 1; s < 360 ; s += 20) {
						double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
						double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
						double y = Y + (radius * Math.cos(Math.toRadians(t))) +1;
						world.addParticle(particle, x, y, z, (world.rand.nextDouble()-0.5) / 4,  (world.rand.nextDouble()-0.5) / 4,  (world.rand.nextDouble()-0.5) / 4);
					}
				}
			}
		}
		
		if(ticksExisted >= 100) {
			remove();
		}

		super.tick();
	}
	
	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MobEntity.registerAttributes()
				.createMutableAttribute(Attributes.FOLLOW_RANGE, 35.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0D)
				.createMutableAttribute(Attributes.MAX_HEALTH, 50.0D)
				.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1000.0D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D)
				.createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 1.0D)
				;
	}

	private static final DataParameter<String> ENTITY_TYPE = EntityDataManager.createKey(SpawningOrbEntity.class, DataSerializers.STRING);

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		if (this.dataManager.get(ENTITY_TYPE) != null) {
			compound.putString("entity", this.dataManager.get(ENTITY_TYPE));
		}
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.dataManager.set(ENTITY_TYPE, compound.getString("entity"));
	}

	public String getEntityType() {
		return this.getDataManager().get(ENTITY_TYPE);
	}

	public void setEntityType(String type) {
		this.dataManager.set(ENTITY_TYPE, type);
	}

	@Override
	protected void registerData() {
		super.registerData();
		this.dataManager.register(ENTITY_TYPE, "");
	}

	@Override
	public boolean onLivingFall(float distance, float damageMultiplier) {
		return false;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}


}
