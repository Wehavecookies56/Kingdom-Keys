package online.kingdomkeys.kingdomkeys.entity.magic;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public class Magics {
	public static void fire(PlayerEntity player) {
		ThrowableEntity shot = new EntityFire(player.world, player);
		player.world.addEntity(shot);
		shot.shoot(player, player.rotationPitch, player.rotationYaw, 0, 1F, 0);
		//player.world.playSound(null, player.getPosition(), ModSounds.fistShot0, SoundCategory.MASTER, 1F, 1F);
		//player.world.playSound(null, player.getPosition(), Utils.getShootSound(player, message.charged), SoundCategory.MASTER, 1F, 1F);
		player.swingArm(Hand.MAIN_HAND);	
	}
	
	public static void blizzard(PlayerEntity player) {
		ThrowableEntity shot = new EntityBlizzard(player.world, player);
		player.world.addEntity(shot);
		shot.shoot(player, player.rotationPitch, player.rotationYaw, 0, 1F, 0);
		player.swingArm(Hand.MAIN_HAND);
	}
	
	public static void thunder(PlayerEntity player) {
		ThrowableEntity shot = new EntityBlizzard(player.world, player);
		player.world.addEntity(shot);
		shot.shoot(player, player.rotationPitch, player.rotationYaw, 0, 1F, 0);
		player.swingArm(Hand.MAIN_HAND);
	}
	
	public static void cure(PlayerEntity player) {
		ThrowableEntity shot = new EntityBlizzard(player.world, player);
		player.world.addEntity(shot);
		shot.shoot(player, player.rotationPitch, player.rotationYaw, 0, 1F, 0);
		player.swingArm(Hand.MAIN_HAND);
	}
	
	public static void magnet(PlayerEntity player) {
		ThrowableEntity shot = new EntityBlizzard(player.world, player);
		player.world.addEntity(shot);
		shot.shoot(player, player.rotationPitch, player.rotationYaw, 0, 1F, 0);
		player.swingArm(Hand.MAIN_HAND);
	}
	
	public static void reflect(PlayerEntity player) {
		ThrowableEntity shot = new EntityBlizzard(player.world, player);
		player.world.addEntity(shot);
		shot.shoot(player, player.rotationPitch, player.rotationYaw, 0, 1F, 0);
		player.swingArm(Hand.MAIN_HAND);
	}
	
	public static void gravity(PlayerEntity player) {
		ThrowableEntity shot = new EntityBlizzard(player.world, player);
		player.world.addEntity(shot);
		shot.shoot(player, player.rotationPitch, player.rotationYaw, 0, 1F, 0);
		player.swingArm(Hand.MAIN_HAND);
	}
	
	public static void stop(PlayerEntity player) {
		System.out.println("a");
		

		//player.world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 1, 1, 0);
		player.world.addParticle(ParticleTypes.SMOKE, player.getPosX(), player.getPosY(), player.getPosZ(), 0, 0, 0);
        //int rotation = 0;
        //if (!world.isRemote)
        //	PacketDispatcher.sendToAllAround(new SpawnStopParticles(this, 1), player, 64.0D);

        //this.rotationYaw = (rotation + 1) % 360;
       
        /*if (ticksExisted < 10)
            player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        else
            player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.10000000149011612D);*/

        List<Entity> list = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().grow(16.0D, 10.0D, 16.0D).offset(-8.0D, -5.0D, -8.0D));
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Entity e = (Entity) list.get(i);

                if (e instanceof LivingEntity) {
                	((LivingEntity) e).setMotion(new Vec3d(0, 0, 0));
                	//TODO
                	
                }

                /*if (!world.isRemote) {
                    if (ticksExisted < 50) {
                        ((EntityPlayerMP) player).connection.sendPacket(new SPacketEntityVelocity(e.getEntityId(), 0, 0, 0));
                    }
                }*/
            }
        }

		/*Entity shot = new EntityStop(player.world, player);
		player.world.addEntity(shot);*/
		//shot.shoot(player, player.rotationPitch, player.rotationYaw, 0, 1F, 0);
		player.swingArm(Hand.MAIN_HAND);
	}
	
}
