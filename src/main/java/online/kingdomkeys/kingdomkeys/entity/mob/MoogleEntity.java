package online.kingdomkeys.kingdomkeys.entity.mob;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenSynthesisGui;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopList;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopListRegistry;
import online.kingdomkeys.kingdomkeys.synthesis.shop.names.NamesListRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;

//TODO make moogle float
public class MoogleEntity extends PathfinderMob implements IEntityWithComplexSpawn {

	String inv;
    String name;
    Player interacting;
	
    public MoogleEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
        super(type, worldIn);
        if (Utils.randomWithRange(0, 100) >= 98) {
            inv = "kingdomkeys:special";
        } else {
            inv = "kingdomkeys:default";
        }
        setRandomName();
        if (name == null) {
            name = "";
        }
    }

    public void setRandomName() {
        ShopList shop = ShopListRegistry.getInstance().getValue(ResourceLocation.parse(inv));
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
        if (name != null && !name.isEmpty()) {
            return Component.translatable(name);
        }
        return super.getCustomName();
    }

    @Override
    public boolean hasCustomName() {
        if (name != null && !name.isEmpty()) {
            return true;
        }
        return super.hasCustomName();
    }

    private boolean fakeMoogle = false;

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
    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
        if (!player.level().isClientSide) {
        	if(!player.isCrouching()) {
	        	ItemStack itemstack = player.getItemInHand(hand);
	        	if(!ItemStack.isSameItem(itemstack, ItemStack.EMPTY) && itemstack.getItem() == ModItems.winnerStick.get()) {
	        		IPlayerData playerData = ModData.getPlayer(player);
	        		int reward = 500;
	        		playerData.setMunny(playerData.getMunny() + reward);
	        		itemstack.shrink(1);
					player.sendSystemMessage(Component.translatable(ChatFormatting.YELLOW + "You have been rewarded with " + reward + " munny!"));
					return InteractionResult.FAIL;
	        	} else {
	        		PacketHandler.sendTo(new SCOpenSynthesisGui(inv, name, this.getId()), (ServerPlayer)player);
                    interacting = player;
                    goalSelector.removeAllGoals(Objects::nonNull);
                    goalSelector.addGoal(0, new LookAtInteractingPlayerGoal(this));
                    return InteractionResult.SUCCESS;
	        	}
	        }
	        return super.interactAt(player, vec, hand);
        }
    	ItemStack itemstack = player.getItemInHand(hand);
    	if(!ItemStack.isSameItem(itemstack, ItemStack.EMPTY) && itemstack.getItem() == ModItems.winnerStick.get()) {
    		return InteractionResult.SUCCESS;
    	} else {
	        return super.interactAt(player, vec, hand);
    	}

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
    public void addAdditionalSaveData(CompoundTag pCompound) {
    	super.addAdditionalSaveData(pCompound);
    	pCompound.putString("inv", inv);
        pCompound.putString("name", name);
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
    	super.readAdditionalSaveData(pCompound);
        inv = pCompound.getString("inv");
        name = pCompound.getString("name");
        if (name.isEmpty()) {
            setRandomName();
        }
    }
}
