package online.kingdomkeys.kingdomkeys.handler;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.InputEvent.ClickInputEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.organization.OrgWeaponItem;
import online.kingdomkeys.kingdomkeys.lib.Party;
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
			if (event.player == mc.player && cooldownTicks <= 0) { // Only run this for the local client player
				focusing = mc.gameSettings.keyBindPickBlock.isKeyDown() && event.player.getHeldItemMainhand() != null && Utils.getPlayerShotlock(mc.player) != null && (event.player.getHeldItemMainhand().getItem() instanceof KeybladeItem || event.player.getHeldItemMainhand().getItem() instanceof OrgWeaponItem);
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(event.player);
				if(playerData == null)
					return;
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
					
					if(focusGaugeTemp > 0)
						focusGaugeTemp-=0.8;

					if (focusingTicks % shotlock.getCooldown() == 1 && focusGaugeTemp > 0 && playerData.getShotlockEnemies().size() < shotlock.getMaxLocks()) {
						RayTraceResult rt = InputHandler.getMouseOverExtended(100);
						
						if (rt != null && rt instanceof EntityRayTraceResult) {
							EntityRayTraceResult ertr = (EntityRayTraceResult) rt;
							Party p = ModCapabilities.getWorld(mc.world).getPartyFromMember(event.player.getUniqueID());
							if(ertr.getEntity() instanceof LivingEntity) {
								LivingEntity target = (LivingEntity) ertr.getEntity();
	
								if (p == null || (p.getMember(target.getUniqueID()) == null || p.getFriendlyFire())) { // If caster is not in a party || the party doesn't have the target in it || the party has FF on
									playerData.addShotlockEnemy(ertr.getEntity().getEntityId());
									event.player.world.playSound(event.player, event.player.getPosition(), ModSounds.shotlock_lockon.get(), SoundCategory.PLAYERS, 1F, 1F);
									cost = playerData.getFocus() - focusGaugeTemp;
	
									if(playerData.getShotlockEnemies().size() >= shotlock.getMaxLocks()) {
										event.player.world.playSound(event.player, event.player.getPosition(), ModSounds.shotlock_lockon_all.get(), SoundCategory.PLAYERS, 1F, 1F);
									}
								}
							}
						}
					}
					
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
						playerData.setShotlockEnemies(new ArrayList<Integer>());
					}
				} else {
					focusingTicks = 0;
					focusGaugeTemp = playerData.getFocus();
					playerData.setShotlockEnemies(new ArrayList<Integer>());
				}
			} else {
				if(cooldownTicks > 0) {
					cooldownTicks--;
				}
			}
		}
	}
	
	@SubscribeEvent
	public void EntityRender(RenderLivingEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player != null && ModCapabilities.getPlayer(mc.player) != null) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(mc.player);
			//if (playerData.getShotlockEnemies() != null && playerData.getShotlockEnemies().contains(event.getEntity().getEntityId())) {
			if(true) {
				MatrixStack matrixStackIn = event.getMatrixStack();
				LivingEntity entityIn = event.getEntity();
				
				EntityRendererManager renderManager = event.getRenderer().getRenderManager();
				TranslationTextComponent displayNameIn = new TranslationTextComponent("o");
				float f = entityIn.getHeight();
				matrixStackIn.push();
				{
					matrixStackIn.translate(0.0D, (double) f, 0.0D);
					matrixStackIn.rotate(renderManager.getCameraOrientation());
					matrixStackIn.scale(-0.001F, -0.001F, 0.001F);
					event.getRenderer().getRenderManager().textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/focus2.png"));
					blit(matrixStackIn,-128,-128,0,0,256,256);
				}
				matrixStackIn.pop();
			}
		}
	}
	
	public void blit(MatrixStack matrixStack, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight) {
		blit(matrixStack, x, y, 0, (float) uOffset, (float) vOffset, uWidth, vHeight, 256, 256);
	}

	public static void blit(MatrixStack matrixStack, int x, int y, int blitOffset, float uOffset, float vOffset, int uWidth, int vHeight, int textureHeight, int textureWidth) {
		innerBlit(matrixStack, x, x + uWidth, y, y + vHeight, blitOffset, uWidth, vHeight, uOffset, vOffset, textureWidth, textureHeight);
	}

	private static void innerBlit(MatrixStack matrixStack, int x1, int x2, int y1, int y2, int blitOffset, int uWidth, int vHeight, float uOffset, float vOffset, int textureWidth, int textureHeight) {
		innerBlit(matrixStack.getLast().getMatrix(), x1, x2, y1, y2, blitOffset, (uOffset + 0.0F) / (float) textureWidth, (uOffset + (float) uWidth) / (float) textureWidth, (vOffset + 0.0F) / (float) textureHeight, (vOffset + (float) vHeight) / (float) textureHeight);
	}

	private static void innerBlit(Matrix4f matrix, int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV) {
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(matrix, (float) x1, (float) y2, (float) blitOffset).tex(minU, maxV).endVertex();
		bufferbuilder.pos(matrix, (float) x2, (float) y2, (float) blitOffset).tex(maxU, maxV).endVertex();
		bufferbuilder.pos(matrix, (float) x2, (float) y1, (float) blitOffset).tex(maxU, minV).endVertex();
		bufferbuilder.pos(matrix, (float) x1, (float) y1, (float) blitOffset).tex(minU, minV).endVertex();
		bufferbuilder.finishDrawing();
		RenderSystem.enableBlend();
		//RenderSystem.enableDepthTest();
		WorldVertexBufferUploader.draw(bufferbuilder);
	}
	
	@SubscribeEvent
	public void PlayerClick(ClickInputEvent event) {
		if(event.isPickBlock()) {
			Minecraft mc = Minecraft.getInstance();
			if(mc.player.getHeldItemMainhand() != null && Utils.getPlayerShotlock(mc.player) != null && (mc.player.getHeldItemMainhand().getItem() instanceof KeybladeItem || mc.player.getHeldItemMainhand().getItem() instanceof OrgWeaponItem)){
				event.setCanceled(true);
			}
		}	
	}

}
