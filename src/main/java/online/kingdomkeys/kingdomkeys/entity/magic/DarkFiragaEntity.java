package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

import java.util.List;

public class DarkFiragaEntity extends FiragaEntity {

	public DarkFiragaEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public DarkFiragaEntity(Level world, LivingEntity player, float dmgMult, LivingEntity lockOnEntity) {
		super(ModEntities.TYPE_DARKFIRAGA.get(), world, player, dmgMult, lockOnEntity);
	}

	@Override
	public List<SimpleParticleType> getParticles() {
		return List.of(ParticleTypes.DRAGON_BREATH, ParticleTypes.SOUL_FIRE_FLAME, ParticleTypes.SQUID_INK);
	}
}
