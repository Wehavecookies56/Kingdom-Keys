package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.util.Mth;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.mob.goal.DuelGoal;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Union;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenUnionScreen;
import online.kingdomkeys.kingdomkeys.world.DialogueHandler;

public class ForetellerEntity extends PathfinderMob implements Dueller, RaysOnDefeat {
    private static final EntityDataAccessor<Byte> UNION = SynchedEntityData.defineId(ForetellerEntity.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Boolean> SPARRING = SynchedEntityData.defineId(ForetellerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DUEL_LEVEL = SynchedEntityData.defineId(ForetellerEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DEATH_SEQUENCE = SynchedEntityData.defineId(ForetellerEntity.class, EntityDataSerializers.INT);

    private static final int RETURNS_AT = RaysOnDefeat.DEATH_SEQUENCE_TICKS / 2;

    private static final ResourceLocation DIALOGUE = KingdomKeys.rl("foreteller");

    private static final String FORETELLER_KEY = "kingdomkeys.foreteller.";

    private static final ResourceLocation DUEL_BOOST = KingdomKeys.rl("duel_boost");

    private ResourceLocation dialogue = DIALOGUE;

    public ForetellerEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.setPersistenceRequired();
        this.setInvulnerable(true);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(UNION, Union.NONE.get());
        builder.define(SPARRING, false);
        builder.define(DUEL_LEVEL, Dueller.LOWEST);
        builder.define(DEATH_SEQUENCE, 0);
    }

    private final Duel duel = new Duel(this);
    private final KeybladeSummon summon = new KeybladeSummon(this);

    @Override
    public Duel duel() {
        return duel;
    }

    @Override
    public KeybladeSummon keybladeSummon() {
        return summon;
    }

    @Override
    public ItemStack keybladeToCall() {
        Item keyblade = keybladeFor(getUnion());
        return keyblade == null ? ItemStack.EMPTY : new ItemStack(keyblade);
    }

    @Override
    public int callingRank() {
        return KeybladeSummon.MASTERED;
    }

    @Override
    public int getDuelLevel() {
        return entityData.get(DUEL_LEVEL);
    }

    public boolean isSparring() {
        return entityData.get(SPARRING);
    }

    @Override
    public void beginDuel(Player pupil, int duelLevel) {
        if (pupil == null || level().isClientSide) {
            return;
        }

        bowOut();

        entityData.set(SPARRING, true);
        entityData.set(DUEL_LEVEL, Mth.clamp(duelLevel, Dueller.LOWEST, Dueller.HIGHEST));

        setDuelist(pupil);
        setTarget(pupil);

        Dueller.lendWorth(this, getDuelLevel(), DUEL_BOOST);
        setHealth(getMaxHealth());

        syncLevel();
    }

    @Override
    public boolean fallsWhenBeaten() {
        return false;
    }

    @Override
    public void onBeaten() {
        entityData.set(DEATH_SEQUENCE, 1);
        level().playSound(null, blockPosition(), ModSounds.bossKill.get(), SoundSource.HOSTILE, 1F, 1F);
    }

    @Override
    public int getDeathSequence() {
        return entityData.get(DEATH_SEQUENCE);
    }

    @Override
    public float deathAlpha(float partialTick) {
        return 1F;
    }

    private void tickLight() {
        int at = getDeathSequence();

        if (at <= 0) {
            return;
        }

        if (level() instanceof ServerLevel server) {
            float progress = (float) at / RaysOnDefeat.DEATH_SEQUENCE_TICKS;
            double height = getBbHeight();
            double width = getBbWidth() * 0.5D;

            server.sendParticles(ParticleTypes.END_ROD, getX(), getY() + height * 0.5D, getZ(), 2 + Math.round(progress * 8), width, height * 0.4D, width, 0.02D);
        }

        if (at >= RETURNS_AT) {
            discard();
            return;
        }

        entityData.set(DEATH_SEQUENCE, at + 1);
    }

    @Override
    public void bowOut() {
        entityData.set(SPARRING, false);
        entityData.set(DEATH_SEQUENCE, 0);
        duel.clear();
        summon.cancel();

        Dueller.takeBack(this, DUEL_BOOST);
        setNoAi(false);
        setInvulnerable(true);
        setTarget(null);
        setHealth(getMaxHealth());

        wearUnionRobes();
    }

    private void syncLevel() {
        GlobalData data = GlobalData.get(this);

        if (data != null) {
            data.setLevel(getDuelLevel());
            PacketHandler.syncToAllAround(this, data);
        }
    }

    public Union getUnion() {
        return Union.fromByte(this.entityData.get(UNION));
    }

    public void setUnion(Union union) {
        this.entityData.set(UNION, union.get());
    }

    public ResourceLocation getDialogue() {
        return dialogue;
    }

    public void setDialogue(ResourceLocation dialogue) {
        this.dialogue = dialogue == null ? DIALOGUE : dialogue;
    }

    public void wearUnionRobes() {
        Item[] robes = robesFor(getUnion());
        if (robes == null)
            return;

        setItemSlot(EquipmentSlot.HEAD, new ItemStack(robes[0]));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(robes[1]));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(robes[2]));
        setItemSlot(EquipmentSlot.FEET, new ItemStack(robes[3]));

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            setDropChance(slot, 0.0F);
        }
    }

    /** Helmet, chestplate, leggings, boots of the Foreteller who leads each union. */
    public static Item[] robesFor(Union union) {
        return switch (union) {
            case UNICORNIS -> new Item[] { ModItems.ira_Helmet.get(), ModItems.ira_Chestplate.get(), ModItems.ira_Leggings.get(), ModItems.ira_Boots.get() };
            case LEOPARDOS -> new Item[] { ModItems.gula_Helmet.get(), ModItems.gula_Chestplate.get(), ModItems.gula_Leggings.get(), ModItems.gula_Boots.get() };
            case VULPES -> new Item[] { ModItems.ava_Helmet.get(), ModItems.ava_Chestplate.get(), ModItems.ava_Leggings.get(), ModItems.ava_Boots.get() };
            case ANGUIS -> new Item[] { ModItems.invi_Helmet.get(), ModItems.invi_Chestplate.get(), ModItems.invi_Leggings.get(), ModItems.invi_Boots.get() };
            case URSUS -> new Item[] { ModItems.aced_Helmet.get(), ModItems.aced_Chestplate.get(), ModItems.aced_Leggings.get(), ModItems.aced_Boots.get() };
            case NONE -> null;
        };
    }

    public static String nameKeyFor(Union union) {
        return switch (union) {
            case UNICORNIS -> FORETELLER_KEY + Strings.ira;
            case LEOPARDOS -> FORETELLER_KEY + Strings.gula;
            case VULPES -> FORETELLER_KEY + Strings.ava;
            case ANGUIS -> FORETELLER_KEY + Strings.invi;
            case URSUS -> FORETELLER_KEY + Strings.aced;
            case NONE -> union.getTranslationKey();
        };
    }

    public static Item keybladeFor(Union union) {
        return switch (union) {
            case UNICORNIS -> ModItems.irasKeyblade.get();
            case LEOPARDOS -> ModItems.gulasKeyblade.get();
            case VULPES -> ModItems.avasKeyblade.get();
            case ANGUIS -> ModItems.invisKeyblade.get();
            case URSUS -> ModItems.acedsKeyblade.get();
            case NONE -> null;
        };
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new DuelGoal<>(this));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 40.0D);
    }

    @Override
    public Component getName() {
        Union union = getUnion();
        return Component.translatable(nameKeyFor(union)).withStyle(style -> style.withColor(TextColor.fromRgb(union.getColour())));
    }

    @Override
    public void aiStep() {
        updateSwingTime();
        super.aiStep();
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide || !isSparring()) {
            return;
        }

        summon.tick();
        duel.tick();
        tickLight();

        if (isDuelling() && getTarget() != getDuelist()) {
            setTarget(getDuelist());
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (level().isClientSide || hand != InteractionHand.MAIN_HAND)
            return InteractionResult.SUCCESS;
        if (!(player instanceof ServerPlayer serverPlayer))
            return InteractionResult.FAIL;

        if (isSparring())
            return InteractionResult.FAIL;

        PlayerData playerData = PlayerData.get(player);
        if (playerData == null)
            return InteractionResult.FAIL;

        if (playerData.isOrgMember())
            return InteractionResult.FAIL;

        if (playerData.getSoAState() == SoAState.UNION && !playerData.hasUnion()) { // SOA join union screen
            PacketHandler.sendTo(new SCOpenUnionScreen(getUnion()), serverPlayer);
            return InteractionResult.SUCCESS;
        }

        if (!playerData.hasUnion())
            return InteractionResult.FAIL;

        DialogueHandler.start(serverPlayer, this, dialogue);
        return InteractionResult.SUCCESS;
    }

    private boolean vulnerableTo(DamageSource source) {
        return isSparring() && !isPreparing() && !isSettled() && source.getEntity() instanceof Player pupil && isDuelist(pupil);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!vulnerableTo(source)) {
            return false;
        }

        return super.hurt(source, amount);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return !vulnerableTo(source);
    }

    @Override
    public boolean canBeHitByProjectile() {
        return isSparring();
    }

    @Override
    public void knockback(double strength, double x, double z) {
        if (isSparring()) {
            super.knockback(strength, x, z);
        }
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void push(Entity entity) {
    }

    @Override
    protected void pushEntities() {
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putByte("union", getUnion().get());
        tag.putString("dialogue", dialogue.toString());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setUnion(Union.fromByte(tag.getByte("union")));
        setDialogue(tag.contains("dialogue") ? ResourceLocation.tryParse(tag.getString("dialogue")) : null);

        entityData.set(SPARRING, false);
        duel.clear();
        setNoAi(false);
        setInvulnerable(true);
        setInvisible(false);
        wearUnionRobes();
    }
}
