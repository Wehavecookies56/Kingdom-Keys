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

public class TripleBlizzagaControllerEntity extends BaseMagicProjectile {

	public TripleBlizzagaControllerEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
	}

	public TripleBlizzagaControllerEntity(Level world, LivingEntity player, float dmgMult, LivingEntity lockedOnEntity) {
		super(ModEntities.TYPE_TRIPLE_BLIZZAGA_CONTROLLER.get(), player, world);
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

		float radius = 2F;

		if (!level().isClientSide && getOwner() != null) { // Only calculate and spawn lightning bolts server side
			if (tickCount % 5 == 0) {
				if(getOwner() instanceof Player player) {
					ThrowableProjectile blizzaga = new BlizzardEntity(level(), player, dmgMult, 100);
					player.level().addFreshEntity(blizzaga);
					blizzaga.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
					Magic blizzard = ModMagic.registry.get(ResourceLocation.parse(Strings.Magic_Blizzard));
					blizzard.playMagicCastSound2(player,player,1);
				}
				/*if(fire != null) {
					if(getOwner() instanceof Player p) {
						//fire.onUse(p,p,0,lockOnEntity);
						fire.magicUse(p, p, 0, 1, lockOnEntity);
						fire.playMagicCastSound(p,p,0,0);
					}
				}*/
				//float dmg = getTotalDamage();
				/*ThunderBoltEntity shot = new ThunderBoltEntity(getOwner().level(), (LivingEntity) getOwner(), posX, getOwner().level().getHeight(Heightmap.Types.WORLD_SURFACE, posX, posZ), posZ, dmg);
				level().addFreshEntity(shot);

				BlockPos pos = new BlockPos(posX, getOwner().level().getHeight(Heightmap.Types.WORLD_SURFACE, posX, posZ), posZ);
				LightningBolt lightningBoltEntity = EntityType.LIGHTNING_BOLT.create(this.level());
				lightningBoltEntity.moveTo(Vec3.atBottomCenterOf(pos));
				lightningBoltEntity.setVisualOnly(true);
				lightningBoltEntity.setCause(getOwner() instanceof ServerPlayer ? (ServerPlayer) getOwner() : null);
				this.level().addFreshEntity(lightningBoltEntity);*/

			}
		}

		super.tick();
	}

	@Override
	protected void onHit(HitResult result) {

	}
}