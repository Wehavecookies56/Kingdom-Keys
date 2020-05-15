package online.kingdomkeys.kingdomkeys.entity.magic;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.Hand;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.PacketSyncGlobalCapability;

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
	
	public static void water(PlayerEntity player) {
		ThrowableEntity shot = new EntityWater(player.world, player);
		player.world.addEntity(shot);
		shot.shoot(player, player.rotationPitch, player.rotationYaw, 0, 1F, 0);
		player.swingArm(Hand.MAIN_HAND);
	}

	public static void thunder(PlayerEntity player) {
		List<Entity> list = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().grow(8.0D, 4.0D, 8.0D).offset(-4.0D, -1.0D, -4.0D));
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Entity e = (Entity) list.get(i);
                if (e instanceof LivingEntity) {
            		ThrowableEntity shot = new EntityThunder(player.world, player, e.getPosX(),e.getPosY(),e.getPosZ());
            		shot.shoot(player, 90, player.rotationYaw, 0, 3F, 0);
            		player.world.addEntity(shot);
                }
            }
        }
		player.swingArm(Hand.MAIN_HAND);
	}
	
	public static void cure(PlayerEntity player) {
        //	PacketDispatcher.sendToAllAround(new SpawnCureParticles(this, 1), player, 64.0D);
		player.heal(7);
		
		//TODO For the Party System
		/* IPlayerCapabilities props = ModCapabilities.get(player);
		Set<LivingEntity> allies = props.getAllies();
		List<Entity> list = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().grow(16.0D, 10.0D, 16.0D).offset(-8.0D, -5.0D, -8.0D));
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Entity e = (Entity) list.get(i);
                if (e instanceof LivingEntity && allies.contains(e)) {
                	e.heal(7);
                }
            }
        }*/
		player.swingArm(Hand.MAIN_HAND);
	}
	
	public static void aero(PlayerEntity player) {
		//TODO Equip aero shield
		
		player.swingArm(Hand.MAIN_HAND);
	}
	
	public static void magnet(PlayerEntity player) {
		EntityMagnet shot = new EntityMagnet(player.world, player);
		shot.setCaster(player.getDisplayName().getFormattedText());
		player.world.addEntity(shot);
		shot.shoot(player, -90, player.rotationYaw, 0, 1F, 0);
		player.swingArm(Hand.MAIN_HAND);
	}
	
	public static void reflect(PlayerEntity player) {
		IPlayerCapabilities props = ModCapabilities.get(player);
		props.setReflectTicks(40);
		PacketHandler.syncToAllAround(player, props);
	}
	
	public static void gravity(PlayerEntity player) {
		ThrowableEntity shot = new EntityGravity(player.world, player);
		player.world.addEntity(shot);
		shot.shoot(player, player.rotationPitch, player.rotationYaw, 0, 1F, 0);
		player.swingArm(Hand.MAIN_HAND);
	}
	
	public static void stop(PlayerEntity player) {
        //	PacketDispatcher.sendToAllAround(new SpawnStopParticles(this, 1), player, 64.0D);

		List<Entity> list = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().grow(8.0D, 4.0D, 8.0D).offset(-4.0D, -1.0D, -4.0D));
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Entity e = (Entity) list.get(i);
                if (e instanceof LivingEntity) {
                	IGlobalCapabilities props = ModCapabilities.getGlobal((LivingEntity) e);
                	props.setStoppedTicks(100); //Stop
                	if(e instanceof ServerPlayerEntity)
                		PacketHandler.sendTo(new PacketSyncGlobalCapability(props), (ServerPlayerEntity) e);
                }
            }
        }
        
		player.swingArm(Hand.MAIN_HAND);
	}
	
}
