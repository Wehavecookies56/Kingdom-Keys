package online.kingdomkeys.kingdomkeys.entity.mob;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData.DataItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenSynthesisGui;

//TODO make moogle float
public class MoogleEntity extends PathfinderMob {

	String inv = "kingdomkeys:default";
	
    public MoogleEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
        super(type, worldIn);
    }

    private boolean fakeMoogle = false;

    public MoogleEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
        super(ModEntities.TYPE_MOOGLE.get(), world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(0, new PanicGoal(this, 0.5D));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                ;
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
        if (!player.level.isClientSide) {
        	PacketHandler.sendTo(new SCOpenSynthesisGui(inv), (ServerPlayer)player);
        }
        return super.interactAt(player, vec, hand);
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.kupoliving.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 600;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
    	return true;
    }

    public boolean isFakeMoogle() {
        return fakeMoogle;
    }

    public void setFakeMoogle(boolean fake) {
        this.fakeMoogle = fake;
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
    	super.addAdditionalSaveData(pCompound);
    	pCompound.putString("inv", inv);
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
    	super.readAdditionalSaveData(pCompound);
    	System.out.println(pCompound.getString("inv"));
    	inv = pCompound.getString("inv");

    }
}
