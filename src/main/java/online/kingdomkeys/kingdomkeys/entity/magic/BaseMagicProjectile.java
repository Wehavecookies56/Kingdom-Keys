package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.MagicData;

import javax.annotation.Nullable;

public abstract class BaseMagicProjectile extends ThrowableProjectile {
	int maxTicks = 100;
	float dmgMult;
	LivingEntity lockOnEntity;
	ResourceKey<DamageType> damageType;
	// The spell that cast this, which is what decides how far the projectile may reach into the world
	Magic magic;

	public BaseMagicProjectile(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public BaseMagicProjectile(EntityType<? extends ThrowableProjectile> type, LivingEntity player, Level world) {
		super(type, player, world);
	}

	public void setDamageType(ResourceKey<DamageType> type){
		this.damageType = type;
	}

	public Magic getMagic() {
		return magic;
	}

	public void setMagic(Magic magic) {
		this.magic = magic;
	}

	public boolean canInteract(MagicData.Interaction interaction) {
		return magic != null && magic.canInteract(interaction);
	}

	public float getTotalDamage(){
		float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) : 2;
		return dmg * dmgMult;
	}

	public void damageEntity(LivingEntity e){
		if(e.level().isClientSide || damageType == null) //Client side might crash cause damage-related values are only set server-wide
			return;

		e.hurt(KKDamageTypes.getElementalDamage(damageType,this, this.getOwner()), getTotalDamage());
	}

	public void interactWithBlocks(HitResult hit, float radius) {
		if (hit instanceof BlockHitResult block) {
			interactWithBlocks(block.getBlockPos(), radius, block.getDirection());
			return;
		}

		if (canInteract(MagicData.Interaction.EXTINGUISH_TNT)) {
			extinguishTnt(new AABB(blockPosition()).inflate(Math.max(radius, 1)));
		}
	}

	public void interactWithBlocks(BlockPos center, float radius, @Nullable Direction face) {
		if (level().isClientSide || magic == null) {
			return;
		}

		int reach = Mth.floor(radius);
		for (int x = -reach; x <= reach; x++) {
			for (int y = -reach; y <= reach; y++) {
				for (int z = -reach; z <= reach; z++) {
					boolean centre = x == 0 && y == 0 && z == 0;
					interactWithBlock(center.offset(x, y, z), centre ? face : null);
				}
			}
		}

		if (canInteract(MagicData.Interaction.EXTINGUISH_TNT)) {
			extinguishTnt(new AABB(center).inflate(Math.max(radius, 1)));
		}
	}

	public void interactWithBlock(BlockPos pos, @Nullable Direction face) {
		if (level().isClientSide || magic == null) {
			return;
		}

		BlockState state = level().getBlockState(pos);

		if (canInteract(MagicData.Interaction.DRY_SPONGE) && state.is(Blocks.WET_SPONGE)) {
			level().setBlockAndUpdate(pos, Blocks.SPONGE.defaultBlockState());
		}

		if (canInteract(MagicData.Interaction.WET_SPONGE) && state.is(Blocks.SPONGE)) {
			level().setBlockAndUpdate(pos, Blocks.WET_SPONGE.defaultBlockState());
		}

		if (canInteract(MagicData.Interaction.EXTINGUISH_FIRE) && state.getBlock() instanceof BaseFireBlock) {
			level().removeBlock(pos, false);
		}

		if (state.hasProperty(BlockStateProperties.LIT)) {
			boolean lit = state.getValue(BlockStateProperties.LIT);
			if (!lit && canInteract(MagicData.Interaction.LIGHT_LIGHTABLE)) {
				level().setBlock(pos, state.setValue(BlockStateProperties.LIT, true), 11);
			} else if (lit && canInteract(MagicData.Interaction.TURN_OFF_LIGHTABLE)) {
				level().setBlock(pos, state.setValue(BlockStateProperties.LIT, false), 11);
			}
		}

		if (canInteract(MagicData.Interaction.LIGHT_TNT) && state.getBlock() instanceof TntBlock) {
			state.onCaughtFire(level(), pos, face, getOwner() instanceof LivingEntity caster ? caster : null);
			level().removeBlock(pos, false);
		}

		if (canInteract(MagicData.Interaction.LIGHT_PORTAL) && state.is(Blocks.OBSIDIAN) && face != null) {
			lightPortal(pos, face);
		}

		freezeFluid(pos);
	}

	public boolean freezeFluid(BlockPos pos) {
		if (level().isClientSide || magic == null) {
			return false;
		}

		BlockState state = level().getBlockState(pos);

		if (canInteract(MagicData.Interaction.FREEZE_WATER) && state == Blocks.WATER.defaultBlockState()) {
			level().setBlockAndUpdate(pos, Blocks.ICE.defaultBlockState());
			return true;
		}

		if (canInteract(MagicData.Interaction.FREEZE_LAVA) && state == Blocks.LAVA.defaultBlockState()) {
			level().setBlockAndUpdate(pos, Blocks.OBSIDIAN.defaultBlockState());
			return true;
		}

		return false;
	}

	private void lightPortal(BlockPos pos, Direction face) {
		BlockPos front = pos.relative(face);

		if (BaseFireBlock.canBePlacedAt(level(), front, face)) {
			level().setBlock(front, BaseFireBlock.getState(level(), front), 11);
		}
	}

	private void extinguishTnt(AABB area) {
		for (PrimedTnt tnt : level().getEntitiesOfClass(PrimedTnt.class, area)) {
			BlockPos at = tnt.blockPosition();
			tnt.discard();

			if (level().getBlockState(at).canBeReplaced()) {
				level().setBlockAndUpdate(at, Blocks.TNT.defaultBlockState());
			}
		}
	}

	@Override
	protected double getDefaultGravity() {
		return 0;
	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		}
		super.tick();
	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {

	}
}
