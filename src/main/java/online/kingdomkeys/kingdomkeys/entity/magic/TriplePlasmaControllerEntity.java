package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;

public class TriplePlasmaControllerEntity extends BaseMagicProjectile {

	public TriplePlasmaControllerEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
	}

	public TriplePlasmaControllerEntity(Level world, LivingEntity player, float dmgMult, LivingEntity lockedOnEntity) {
		super(ModEntities.TYPE_TRIPLE_PLASMA_CONTROLLER.get(), player, world);
		this.dmgMult = dmgMult;
		this.lockOnEntity = lockedOnEntity;
		this.damageType = KKDamageTypes.FIRE;
		maxTicks = 15;
	}


	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		}

		if (!level().isClientSide && getOwner() != null) { // Only calculate and spawn lightning bolts server side
			if (tickCount % 5 == 0) {
				if(getOwner() instanceof Player player) {
					ThrowableProjectile thunderShot = new ThundagaShotEntity(level(), player, dmgMult, lockOnEntity);
					player.level().addFreshEntity(thunderShot);
					thunderShot.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);

					Magic magic = ModMagic.THUNDAGA_SHOT.get();
					magic.playMagicCastSound2(player,player,1);
				}
			}
		}

		super.tick();
	}

	@Override
	protected void onHit(HitResult result) {

	}
}