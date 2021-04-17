package online.kingdomkeys.kingdomkeys.handler;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.organization.OrgWeaponItem;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSShotlockShot;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class ClientEvents {

	@SubscribeEvent
	public void onRenderTick(RenderTickEvent event) { //Lock on
		//System.out.println(event.player.world.isRemote);
		PlayerEntity player = Minecraft.getInstance().player;
		if(InputHandler.lockOn != null && player != null) {
			if(InputHandler.lockOn.removed) {
                InputHandler.lockOn = null;
                return;	
			}
            LivingEntity target = InputHandler.lockOn;

            double dx = player.getPosX() - target.getPosX();
            double dz = player.getPosZ() - target.getPosZ();
            double dy = player.getPosY() - (target.getPosY() + (target.getHeight() / 2.0F)-player.getHeight());
            double angle = Math.atan2(dz, dx) * 180 / Math.PI;
            double pitch = Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)) * 180 / Math.PI;
            double distance = player.getDistance(target);
            
            float rYaw = (float) MathHelper.wrapDegrees(angle - player.rotationYaw) + 90;
            float rPitch = (float) pitch - (float) (10.0F / Math.sqrt(distance)) + (float) (distance * Math.PI / 90);
            
            float f = player.rotationPitch;
            float f1 = player.rotationYaw;
            
            player.rotationYaw = (float)((double)player.rotationYaw + (double)rYaw * 0.15D);
            player.rotationPitch = (float)((double)player.rotationPitch - (double)-(rPitch - player.rotationPitch) * 0.15D);
            player.prevRotationPitch = player.rotationPitch - f;
            player.prevRotationYaw += player.rotationYaw - f1;

            if (player.getRidingEntity() != null) {
                player.getRidingEntity().applyOrientationToEntity(player);
            }

		}
	}
	
	float yaw = 0, pitch = 0;

	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		IGlobalCapabilities globalData = ModCapabilities.getGlobal(event.getEntityLiving());
		if (globalData != null) {
			if (globalData.getStoppedTicks() > 0) {
				event.getEntityLiving().rotationPitch = pitch;
				event.getEntityLiving().rotationYaw = yaw;
				event.setCanceled(true);
			} else {
				yaw = event.getEntityLiving().rotationYaw;
				pitch = event.getEntityLiving().rotationPitch;
			}
		}
		
		if(event.getEntityLiving() == Minecraft.getInstance().player) {
			if(InputHandler.qrCooldown > 0) {
				InputHandler.qrCooldown -= 1;
			}
		}
		
	}
	
	@SubscribeEvent
	public void RenderEntity(RenderLivingEvent.Pre event) {
		if(event.getEntity() != null) {
			if(event.getEntity() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) event.getEntity();
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
				if(playerData != null) {
					// Aerial Dodge rotation
					if(playerData.getAerialDodgeTicks() > 0) {
						event.getMatrixStack().rotate(Vector3f.YP.rotationDegrees(player.ticksExisted*80));
					}
				}
			}
		}
	}
	
	public static boolean focusing = false;
	int focusingTicks = 0;
	public static double focusGaugeTemp = 100;
	double cost = 0;
	
	int cooldownTicks = 0;
	@SubscribeEvent
	public void PlayerTick(PlayerTickEvent event) {
		if (event.phase == Phase.END) {
			Minecraft mc = Minecraft.getInstance();
			if (event.player == mc.player && cooldownTicks <= 0 && event.player.getHeldItemMainhand() != null && Utils.getPlayerShotlock(mc.player) != null && (event.player.getHeldItemMainhand().getItem() instanceof KeybladeItem || event.player.getHeldItemMainhand().getItem() instanceof OrgWeaponItem) ) { // Only run this for the local client player
				focusing = mc.gameSettings.keyBindPickBlock.isKeyDown();
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(event.player);
				Shotlock shotlock = Utils.getPlayerShotlock(mc.player);
				if (focusing) {
					if (focusingTicks == 0) {
						// Has started focusing
						focusGaugeTemp = playerData.getFocus();
						playerData.setShotlockEnemies(new ArrayList<Integer>());
						event.player.world.playSound(event.player, event.player.getPosition(), ModSounds.shotlock_lockon_start.get(), SoundCategory.PLAYERS, 1F, 1F);
					}
					
					if(focusingTicks == 5) {
						event.player.world.playSound(event.player, event.player.getPosition(), ModSounds.shotlock_lockon_idle.get(), SoundCategory.PLAYERS, 1F, 1F);
					}
					focusingTicks++;

					//System.out.println(focusGaugeTemp);
					if (focusingTicks % shotlock.getCooldown() == 1 && focusGaugeTemp > 0 && playerData.getShotlockEnemies().size() < shotlock.getMaxLocks()) {
						RayTraceResult rt = InputHandler.getMouseOverExtended(100);
						
						if (rt != null && rt instanceof EntityRayTraceResult) {
							EntityRayTraceResult ertr = (EntityRayTraceResult) rt;
							//System.out.println(ertr.getEntity());
							if(ertr.getEntity() instanceof LivingEntity) {
								playerData.addShotlockEnemy(ertr.getEntity().getEntityId());
								event.player.world.playSound(event.player, event.player.getPosition(), ModSounds.shotlock_lockon.get(), SoundCategory.PLAYERS, 1F, 1F);
								cost = playerData.getFocus() - focusGaugeTemp;
								
								if(playerData.getShotlockEnemies().size() >= shotlock.getMaxLocks()) {
									event.player.world.playSound(event.player, event.player.getPosition(), ModSounds.shotlock_lockon_all.get(), SoundCategory.PLAYERS, 1F, 1F);
								}
							}
						}
					}
					
					if(focusGaugeTemp > 0)
						focusGaugeTemp-=0.8;
					
					if(mc.gameSettings.keyBindAttack.isKeyDown()) {
						if (focusingTicks > 0) {
							// Has stopped shotlocking
							// Send packet to spawn entities and track enemies
							if(!playerData.getShotlockEnemies().isEmpty()) {
								playerData.remFocus(cost);
								event.player.world.playSound(event.player, event.player.getPosition(), ModSounds.shotlock_shot.get(), SoundCategory.PLAYERS, 1F, 1F);
								PacketHandler.sendToServer(new CSShotlockShot(cost, playerData.getShotlockEnemies()));
								cooldownTicks = 100;
								focusing = false;
							}
						}
						focusingTicks = 0;
						focusGaugeTemp = playerData.getFocus();
					}
				} else {
					focusingTicks = 0;
					focusGaugeTemp = playerData.getFocus();

					
				}
			} else {
				if(cooldownTicks > 0) {
					cooldownTicks--;
				}
			}
		}
	}

}
