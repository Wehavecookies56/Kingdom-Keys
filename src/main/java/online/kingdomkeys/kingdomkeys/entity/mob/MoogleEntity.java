package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenSynthesisGui;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopList;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopListRegistry;
import online.kingdomkeys.kingdomkeys.synthesis.shop.names.NamesListRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

//TODO make moogle float
public class MoogleEntity extends PathfinderMob implements IEntityWithComplexSpawn {

    private static final EntityDataAccessor<Integer> POMPOM_COLOR = SynchedEntityData.defineId(MoogleEntity.class, EntityDataSerializers.INT);

    public static final int NO_POMPOM_DYE = -1;

    public static final int DEFAULT_POMPOM_COLOR = DyeColor.RED.getTextureDiffuseColor();

	String inv;
    String name;
    Player interacting;
    boolean stationary = false;

    public MoogleEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
        super(type, worldIn);
        inv = Utils.randomWithRange(0, 100) >= 98 ? "kingdomkeys:special" :  "kingdomkeys:default";

        setRandomName();
        if (name == null) {
            name = "";
        }
    }

    public void setRandomName() {
        ShopList shop = ShopListRegistry.getInstance().getValue(KingdomKeys.rl(inv));
        if (shop != null) {
            List<String> names = NamesListRegistry.getInstance().getValue(shop.getNames());
            if (names != null && !names.isEmpty()) {
                name = names.get(Utils.randomWithRange(0, names.size()-1));
            }
        }
    }

    @Nullable
    @Override
    public Component getCustomName() {
        Component vanilla = super.getCustomName();
        if (vanilla != null) {
            return vanilla;
        }
        if (name != null && !name.isEmpty()) {
            return Component.translatable(name);
        }
        return null;
    }

    @Override
    public boolean hasCustomName() {
        if (super.hasCustomName()) {
            return true;
        }
        return name != null && !name.isEmpty();
    }

    private boolean fakeMoogle = false;

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(POMPOM_COLOR, DEFAULT_POMPOM_COLOR);
    }

    public int getPompomColor() {
        return this.entityData.get(POMPOM_COLOR);
    }

    public void setPompomColor(int color) {
        this.entityData.set(POMPOM_COLOR, color);
    }

    @Override
    protected void registerGoals() {
        normalGoals();
    }

    public void normalGoals() {
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

    public Player getInteracting() {
        return interacting;
    }

    public void stopInteracting() {
        this.interacting = null;
        normalGoals();
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeUtf(inv);
        buffer.writeUtf(name);
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        inv = additionalData.readUtf();
        name = additionalData.readUtf();
    }

    public static class LookAtInteractingPlayerGoal extends LookAtPlayerGoal {
        public LookAtInteractingPlayerGoal(MoogleEntity moogle) {
            super(moogle, Player.class, 8);
        }

        @Override
        public boolean canUse() {
            if (((MoogleEntity)mob).interacting != null) {
                lookAt = ((MoogleEntity)mob).interacting;
                return true;
            }
            return false;
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (interacting != null || stationary) {
            if (!this.onGround()) {
                super.travel(Vec3.ZERO);
            }
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        //Name tag
        if (itemstack.getItem() == Items.NAME_TAG) {
            return super.interactAt(player, vec, hand);
        }

        //Dyeing the pompom
        if (itemstack.getItem() instanceof DyeItem dye && !isFakeMoogle()) {
            int color = dye.getDyeColor().getTextureDiffuseColor();
            if (getPompomColor() != color) { //Only a different color than the already applied
                if (!level().isClientSide) {
                    setPompomColor(color);
                    if (!player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    level().playSound(null, this, SoundEvents.DYE_USE, getSoundSource(), 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(level().isClientSide);
            }
        }

        //GUI opening
        if (!player.level().isClientSide) {
        	if(!player.isCrouching()) {
                PlayerData.get(player).setMetMoogle(true);
                PacketHandler.sendTo(new SCOpenSynthesisGui(PlayerData.get(player).serializeNBT(player.level().registryAccess()), inv, name, this.getId()), (ServerPlayer)player);
                interacting = player;
                goalSelector.removeAllGoals(Objects::nonNull);
                goalSelector.addGoal(0, new LookAtInteractingPlayerGoal(this));
                return InteractionResult.SUCCESS;
	        }
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
    public void tick() {
        if (interacting != null) {
            if (distanceTo(interacting) > 10) {
                interacting = null;
            }
        }
        super.tick();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("inv", inv);

        if (!name.isEmpty()) {
            tag.putString("name", name);
        }
        tag.putBoolean("stationary", stationary);
        if (getPompomColor() != DEFAULT_POMPOM_COLOR) {
            tag.putInt("pompomcolor", getPompomColor());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        inv = tag.getString("inv");
        name = tag.contains("name") ? tag.getString("name") : "";
        if (name.isEmpty()) {
            setRandomName();
        }
        stationary = tag.getBoolean("stationary");
        setPompomColor(tag.contains("pompomcolor") ? tag.getInt("pompomcolor") : DEFAULT_POMPOM_COLOR);
    }
}
