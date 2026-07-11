package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class MagnetEntity extends BaseMagicProjectile {

	float dmgMult = 1;
	
	public MagnetEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
	}

	public MagnetEntity(Level world, LivingEntity player, float dmgMult) {
		super(ModEntities.TYPE_MAGNET.get(), player, world);
		this.dmgMult = dmgMult;
		setDamageType(KKDamageTypes.LIGHT);
	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks || getOwner() == null) {
			this.remove(RemovalReason.KILLED);
		}

		if(level() == null || WorldData.get(level().getServer()) == null)
			return;

		float radius = 2F;

		if (tickCount >= 3) {
			if(!level().isClientSide) {
				if(tickCount < 20) {
					radius = tickCount / 10F;
				}
				if(tickCount > maxTicks - 20) {
					radius = (maxTicks - tickCount) / 10F;
				}
			}
			this.setDeltaMovement(0, 0, 0);
			this.hurtMarked = true;
			
			
			List<Entity> list = level().getEntities(getOwner(), getBoundingBox().inflate(radius,radius*2,radius));
			list = Utils.removePartyMembersFromList((Player) getOwner(), list);

			if (!list.isEmpty()) {
				for (Entity e : list) {
					double d = e.getX() - getX();
					double d1 = e.getZ() - getZ();
					if (e.getY() < this.getY() - 0.5) {
						e.setDeltaMovement(0, 0.5F, 0);
					}
					e.setDeltaMovement(d * -0.1, e.getDeltaMovement().y, d1 * -0.1);
				}
			}
		}
		
		if(tickCount == maxTicks-20) {
			getOwner().level().playSound(null, getOwner().blockPosition(), ModSounds.magnet2.get(), SoundSource.PLAYERS, 1F, 1.1F);
		}

		super.tick();
	}
}
