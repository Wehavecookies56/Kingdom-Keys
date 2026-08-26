package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.goal.NeoshadowGoal;

public class NeoshadowEntity extends BaseKHEntity {

	public NeoshadowGoal neoshadowGoal;

	public float shadowAnim = 0;
	public float prevShadowAnim = 0;

	public NeoshadowEntity(EntityType<? extends Monster> type, Level worldIn) {
		super(type, worldIn);
		xpReward = 12;
	}

	@Override
	public void tick() {
		super.tick();
		prevShadowAnim = shadowAnim;

		float target = getState() == NeoshadowGoal.STATE_SUBMERGED ? 1F : 0F;
		shadowAnim += (target - shadowAnim) * 0.5F;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (neoshadowGoal != null && source.is(KKDamageTypes.WATER) && neoshadowGoal.isSubmerged()) {
			setState(NeoshadowGoal.STATE_IDLE);
			neoshadowGoal.surface();
			if (source.getEntity() != null) {
				source.getEntity().invulnerableTime = 10;
			}
		}
		return super.hurt(source, amount);
	}

	@OnlyIn(Dist.CLIENT)
	public ResourceLocation getTexture() {
		return KingdomKeys.rl("textures/entity/mob/neoshadow.png");
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
		this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Villager.class, true));
		neoshadowGoal = new NeoshadowGoal(this);
		this.targetSelector.addGoal(4, neoshadowGoal);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createLivingAttributes()
				.add(Attributes.FOLLOW_RANGE, 35.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.3D)
				.add(Attributes.MAX_HEALTH, 80.0D)
				.add(Attributes.ATTACK_DAMAGE, 5.0D)
				.add(Attributes.ATTACK_KNOCKBACK, 1.0D);
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 3;
	}

	@Override
	public EntityHelper.MobType getKHMobType() {
		return EntityHelper.MobType.HEARTLESS_PUREBLOOD;
	}
}
