package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.mob.MarluxiaEntity;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.joml.Vector3f;

import java.util.List;

public class WarpEntity extends BaseMagicProjectile {
	int maxTicks = 100;
	float chanceMulti = 1;

	public WarpEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public WarpEntity(Level world, LivingEntity player, float dmgMult) {
		super(ModEntities.TYPE_WARP.get(), player, world);
		this.chanceMulti = dmgMult;
	}

	@Override
	protected double getDefaultGravity() {
		return 0;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {

	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		}

		if (tickCount > 2) level().addParticle(ParticleTypes.DRAGON_BREATH, getX(), getY(), getZ(), 0, 0, 0);

		super.tick();
	}


	// This is where the fun begins.
	@Override
	protected void onHit(HitResult rtRes) {
		if (!level().isClientSide) {
			float radius = 4F;
			double X = getX();
			double Y = getY();
			double Z = getZ();

			// Pretty Stuff
			for (int t = 1; t < 360; t += 20) {
				for (int s = 1; s < 360; s += 20) {
					double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double y = Y + (radius * Math.cos(Math.toRadians(t)));
					((ServerLevel) level()).sendParticles(ParticleTypes.DRAGON_BREATH, x, y + 1, z, 1, 0, 0, 0, 0);
					((ServerLevel) level()).sendParticles(new DustParticleOptions(new Vector3f(0F, 0F, 0F), 6F), x, y + 1, z, 1, 0, 0, 0, 0);
					((ServerLevel) level()).sendParticles(new DustParticleOptions(new Vector3f(0.45F, 0.35F, 0F), 6F), x, y + 1, z, 1, 0, 0, 0, 0);
				}
			}

			WorldData worldData = WorldData.get(level().getServer());
			if (getOwner() != null && worldData != null) {
				List<Entity> list = level().getEntities(getOwner(), getBoundingBox().inflate(radius));
				Party casterParty = worldData.getPartyFromMember(getOwner().getUUID());

				if (casterParty != null && !casterParty.getFriendlyFire()) {
					for (Party.Member m : casterParty.getMembers()) {
						list.remove(level().getPlayerByUUID(m.getUUID()));
					}
				} else {
					list.remove(getOwner());
				}

				for (Entity e : list) {
					if (!(e instanceof LivingEntity)) {
						continue;
					}

					if (!(Utils.isHostile(e) || e instanceof ServerPlayer)) {
						continue;
					}

					if (e instanceof MarluxiaEntity || e instanceof EnderDragon || e instanceof WitherBoss || e instanceof Warden) {
						continue;
					}

					//boolean chance = level().random.nextFloat() < (0.10F + localLevel * 0.1F);
					boolean chance = level().random.nextFloat() < chanceMulti;
					if (chance) {
						if (e instanceof ServerPlayer player) {
							player.level().playSound(null, player.blockPosition(), ModSounds.warpHitPlayer.get(), SoundSource.PLAYERS, 1F, 1F);
							player.teleportRelative(Math.floor(level().random.nextDouble() * 100), 50, Math.floor(level().random.nextDouble() * 100));
						} else {
							e.kill();
						}
					}
				}
				remove(RemovalReason.KILLED);
			}
		}
	}
}

