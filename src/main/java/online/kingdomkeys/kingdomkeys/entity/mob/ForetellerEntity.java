package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.lib.Union;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenForetellerScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenUnionScreen;

public class ForetellerEntity extends PathfinderMob {
    private static final EntityDataAccessor<Byte> UNION = SynchedEntityData.defineId(ForetellerEntity.class, EntityDataSerializers.BYTE);

    public ForetellerEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.setPersistenceRequired();
        this.setInvulnerable(true);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(UNION, Union.NONE.get());
    }

    public Union getUnion() {
        return Union.fromByte(this.entityData.get(UNION));
    }

    public void setUnion(Union union) {
        this.entityData.set(UNION, union.get());
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
    private static Item[] robesFor(Union union) {
        return switch (union) {
            case UNICORNIS -> new Item[] { ModItems.ira_Helmet.get(), ModItems.ira_Chestplate.get(), ModItems.ira_Leggings.get(), ModItems.ira_Boots.get() };
            case LEOPARDOS -> new Item[] { ModItems.gula_Helmet.get(), ModItems.gula_Chestplate.get(), ModItems.gula_Leggings.get(), ModItems.gula_Boots.get() };
            case VULPES -> new Item[] { ModItems.ava_Helmet.get(), ModItems.ava_Chestplate.get(), ModItems.ava_Leggings.get(), ModItems.ava_Boots.get() };
            case ANGUIS -> new Item[] { ModItems.invi_Helmet.get(), ModItems.invi_Chestplate.get(), ModItems.invi_Leggings.get(), ModItems.invi_Boots.get() };
            case URSUS -> new Item[] { ModItems.aced_Helmet.get(), ModItems.aced_Chestplate.get(), ModItems.aced_Leggings.get(), ModItems.aced_Boots.get() };
            case NONE -> null;
        };
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 0.0D);
    }

    @Override
    public Component getName() {
        Union union = getUnion();
        return Component.translatable(union.getTranslationKey()).withStyle(style -> style.withColor(TextColor.fromRgb(union.getColour())));
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (level().isClientSide || hand != InteractionHand.MAIN_HAND)
            return InteractionResult.SUCCESS;
        if (!(player instanceof ServerPlayer serverPlayer))
            return InteractionResult.FAIL;

        PlayerData playerData = PlayerData.get(player);
        if (playerData == null)
            return InteractionResult.FAIL;

        if (playerData.getSoAState() == SoAState.UNION && !playerData.hasUnion()) { // SOA join union screen
            PacketHandler.sendTo(new SCOpenUnionScreen(getUnion()), serverPlayer);
            return InteractionResult.SUCCESS;
        }

        if (!playerData.hasUnion())
            return InteractionResult.FAIL;
        // Pupil - master screen
        PacketHandler.sendTo(new SCOpenForetellerScreen(getUnion(), playerData.serializeNBT(level().registryAccess())), serverPlayer);
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return true;
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
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setUnion(Union.fromByte(tag.getByte("union")));
    }
}
