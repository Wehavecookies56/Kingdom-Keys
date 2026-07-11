package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;

public class TripleFiragaControllerEntity extends BaseMagicProjectile {

	public TripleFiragaControllerEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
	}

	public TripleFiragaControllerEntity(Level world, LivingEntity player, float dmgMult, LivingEntity lockedOnEntity) {
		super(ModEntities.TYPE_TRIPLE_FIRAGA_CONTROLLER.get(), player, world);
		this.dmgMult = dmgMult;
		this.lockOnEntity = lockedOnEntity;
		this.damageType = KKDamageTypes.FIRE;
		maxTicks = 12;
	}


	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		}

		if (!level().isClientSide && getOwner() != null) { // Only calculate and spawn lightning bolts server side
			if (tickCount % 4 == 0) {
				if(getOwner() instanceof Player player) {
					ThrowableProjectile firaga = new FiragaEntity(level(), player, dmgMult, lockOnEntity);
					player.level().addFreshEntity(firaga);
					firaga.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
					Magic fire = ModMagic.FIRE.get();
					fire.playMagicCastSound(player,player);
				}
			}
		}

		super.tick();
	}

	@Override
	protected void onHit(HitResult result) {

	}
}