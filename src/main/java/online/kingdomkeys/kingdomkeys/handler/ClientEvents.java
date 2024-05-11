package online.kingdomkeys.kingdomkeys.handler;

import java.awt.Color;
import java.util.ArrayList;

import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.eventbus.api.EventPriority;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetAirStepPacket;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;

import net.minecraft.client.CameraType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.InputEvent.InteractionKeyMappingTriggered;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.capability.CastleOblivionCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.StopGui;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.item.WayfinderItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSShotlockShot;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.kingdomkeys.kingdomkeys.sound.AlarmSoundInstance;
import online.kingdomkeys.kingdomkeys.util.IDisabledAnimations;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.Floor;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.Room;

public class ClientEvents {

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinLevelEvent e) {
		if (e.getEntity() instanceof LivingEntity ent) {
			if (e.getLevel().isClientSide) {
				Minecraft minecraft = Minecraft.getInstance();
				if (ent == minecraft.player) {
					minecraft.getSoundManager().play(new AlarmSoundInstance(minecraft.player));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onRenderTick(RenderTickEvent event) { //Lock on
		Player player = Minecraft.getInstance().player;

		if(InputHandler.lockOn != null && player != null) {
			if(InputHandler.lockOn.isRemoved()) {
                InputHandler.lockOn = null;
                return;	
			}
            LivingEntity target = InputHandler.lockOn;

            double dx = player.getX() - target.getX();
            double dz = player.getZ() - target.getZ();
            double dy = player.getY() - (target.getY() + (target.getBbHeight() / 2.0F)-player.getBbHeight());
            double angle = Math.atan2(dz, dx) * 180 / Math.PI;
            double pitch = Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)) * 180 / Math.PI;
            double distance = player.distanceTo(target);
            
            float rYaw = (float) Mth.wrapDegrees(angle - player.getYRot()) + 90;
            float rPitch = (float) pitch - (float) (10.0F / Math.sqrt(distance)) + (float) (distance * Math.PI / 90);
            
            float f = player.getXRot();
            float f1 = player.getYRot();

			player.setYRot(Mth.rotLerp(event.renderTickTime, player.getYRot(), (float)(player.getYRot() + rYaw * 0.15D)));
            player.setXRot(Mth.rotLerp(event.renderTickTime, player.getXRot(), (float)(player.getXRot() - -(rPitch - player.getXRot()) * 0.15D)));
            player.xRotO = Mth.rotLerp(event.renderTickTime, player.getXRot(), player.getXRot() - f);
            player.yRotO = Mth.rotLerp(event.renderTickTime, player.yRotO, player.yRotO + player.getYRot() - f1);

            if (player.getVehicle() != null) {
                player.getVehicle().onPassengerTurned(player);
            }
		}
	}
	
	@SubscribeEvent
	public void onLivingUpdate(LivingTickEvent event) {
		
		IGlobalCapabilities globalData = ModCapabilities.getGlobal(event.getEntity());
		if (globalData != null) {
			if (globalData.getStoppedTicks() > 0 ) {
				if(event.getEntity().level().isClientSide) {
					if(Minecraft.getInstance().screen == null)
						Minecraft.getInstance().setScreen(new StopGui());
				}
				event.setCanceled(true);
			}
			
			if(globalData.isKO()) {
				if(event.getEntity().level().isClientSide && event.getEntity() == Minecraft.getInstance().player) {
					if(Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON)
						Minecraft.getInstance().options.setCameraType(CameraType.THIRD_PERSON_FRONT);
					
					if(Minecraft.getInstance().screen == null && event.getEntity().tickCount % 10 == 0)
						Minecraft.getInstance().setScreen(new ChatScreen(""));
				}
			}
			if(event.getEntity() instanceof Player player) {
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
				if(playerData != null) {
					if(playerData.getMagicCasttimeTicks() > 0) {
						player.setDeltaMovement(0, 0, 0);
					}
				}
			}
		}
		
		if(event.getEntity() == Minecraft.getInstance().player) { //Local player
			if(InputHandler.qrCooldown > 0) {
				InputHandler.qrCooldown -= 1;
			}
		}
	}
	
	@SubscribeEvent
	public void RenderEntity(RenderLivingEvent.Post event) { //Hide the player shadow when KO'd
		if(event.getEntity() != null) {
			if(event.getEntity() instanceof Player player) {
				IGlobalCapabilities globalData = ModCapabilities.getGlobal(player);
				if(globalData != null) {
					if(globalData.isKO()) {
						event.getPoseStack().mulPose(Axis.XP.rotationDegrees(90));
						event.getPoseStack().scale(0.01F, 0.01F, 0.01F);
					}
				}
			}
		}
	}


	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void RenderEntity(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event) {
		if(event.getEntity() != null) {
			IPlayerCapabilities localPlayerData = ModCapabilities.getPlayer(Minecraft.getInstance().player);
			if(tempShotlockEntity != null && event.getEntity() == tempShotlockEntity){
				ClientUtils.drawSingleShotlockIndicator(tempShotlockEntity.getId(), event.getPoseStack(), event.getMultiBufferSource(), event.getPartialTick());
			}
			if(localPlayerData != null && localPlayerData.getShotlockEnemies() != null && !localPlayerData.getShotlockEnemies().isEmpty()) {
				LivingEntity e = event.getEntity();
				if(localPlayerData.getShotlockEnemies().stream().anyMatch(sh -> sh.id() == e.getId())){
					ClientUtils.drawShotlockIndicator(e, event.getPoseStack(), event.getMultiBufferSource(), event.getPartialTick());
				}
			}

			if(event.getEntity() instanceof Player player) {
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
				IGlobalCapabilities globalData = ModCapabilities.getGlobal(player);
				if(globalData != null) {
					if(globalData.isKO()) {
						LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer = (LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer((AbstractClientPlayer) player);
						if (!((IDisabledAnimations) renderer).isDisabled()) {
							event.getPoseStack().mulPose(Axis.XN.rotationDegrees(90));
							event.getPoseStack().mulPose(Axis.ZP.rotationDegrees(90));
							float MAX = 100;
					        double pos = player.tickCount % MAX / (MAX /2D);

							if (player.tickCount % MAX < (MAX / 2)) {
								event.getPoseStack().translate(0, 0, pos * 0.3);
							} else {
								event.getPoseStack().translate(0, 0, (MAX - player.tickCount % MAX) / (MAX / 2D) * 0.3);
							}
							event.getPoseStack().translate(0, -1, 0.8);
						}
					}
				}
				
				if(playerData != null) {
					if(!playerData.getAirStep().equals(new BlockPos(0,0,0))){
						Color c = new Color(playerData.getNotifColor());
						player.level().addParticle(new DustParticleOptions(new Vector3f(c.getRed()/255F,c.getGreen()/255F,c.getBlue()/255F),1F), player.getX(), player.getY()+1, player.getZ(), 0, 0.0, 0);
						event.setCanceled(true);
					}
					// Aerial Dodge rotation
					if(playerData.getAerialDodgeTicks() > 0) {
						LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer = (LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer((AbstractClientPlayer) player);
						if (!((IDisabledAnimations) renderer).isDisabled()) {
							event.getPoseStack().mulPose(Axis.YP.rotationDegrees(player.tickCount*80));
						}
					}
					
					if(playerData.getActiveDriveForm().equals(Strings.Form_Anti)) {
						player.level().addParticle(ParticleTypes.SMOKE, player.getX()+player.level().random.nextDouble() - 0.5D, player.getY()+player.level().random.nextDouble() *2D, player.getZ()+player.level().random.nextDouble() - 0.5D, (player.level().random.nextDouble() - 0.5D)*0.2, 0.1, (player.level().random.nextDouble() - 0.5D)*0.2);
					} else if(playerData.getActiveDriveForm().equals(Strings.Form_Wisdom)) {
						player.level().addParticle(new DustParticleOptions(new Vector3f(0F,1F,1F),1F), player.getX(), player.getY(), player.getZ(), 0, 0.3, 0);
						//player.level().addParticle(ParticleTypes.ENCHANTED_HIT, player.getX(), player.getY(), player.getZ(), 0, 0.3, 0);
					}

				}
			}
		}
	}

	@SubscribeEvent
	public void clientTick(TickEvent.ClientTickEvent event) {
		if (Minecraft.getInstance().level != null) {
			if (event.phase == Phase.START) {
				for (KeyMapping key : Minecraft.getInstance().options.keyHotbarSlots) {
					if (KeyboardHelper.isScrollActivatorDown()) {
						key.setKey(InputConstants.getKey(InputConstants.KEY_F25,InputConstants.KEY_F25));
					} else {
						if (!key.matches(key.getDefaultKey().getValue(), key.getKey().getValue())) {
							key.setToDefault();
						}
					}
				}
			}
		}
	}

	public static boolean focusing = false;
	int focusingTicks = 0;
	public static int focusingAnEntityTicks = 0;
	LivingEntity tempShotlockEntity = null;
	public static double focusGaugeTemp = 100;
	double cost = 0;

	int cooldownTicks = 0;
	public static BlockPos lockedAirStep = new BlockPos(0,0,0);

	@SubscribeEvent
	public void PlayerTick(PlayerTickEvent event) {
		if (event.phase == Phase.END) {
			Minecraft mc = Minecraft.getInstance();
			if (event.player == mc.player && cooldownTicks <= 0) { // Only run this for the local client player
				focusing = mc.options.keyPickItem.isDown() && event.player.getMainHandItem() != null && Utils.getPlayerShotlock(mc.player) != null && (event.player.getMainHandItem().getItem() instanceof KeybladeItem || event.player.getMainHandItem().getItem() instanceof IOrgWeapon);
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(event.player);
				if(playerData == null)
					return;
				Shotlock shotlock = Utils.getPlayerShotlock(mc.player);
				if (focusing) {
					if (focusingTicks == 0) {
						// Has started focusing
						focusGaugeTemp = playerData.getFocus();
						playerData.setShotlockEnemies(new ArrayList<>());
						event.player.level().playSound(event.player, event.player.position().x(),event.player.position().y(),event.player.position().z(), ModSounds.shotlock_lockon_start.get(), SoundSource.PLAYERS, 1F, 1F);
					}
					
					if(focusingTicks == 5) {
						event.player.level().playSound(event.player, event.player.position().x(),event.player.position().y(),event.player.position().z(), ModSounds.shotlock_lockon_idle.get(), SoundSource.PLAYERS, 1F, 1F);
					}
					focusingTicks++;
					
					if(focusGaugeTemp > 0)
						focusGaugeTemp-=0.8;

					if(shotlock.getMaxLocks() == 1 && focusGaugeTemp > 0 && playerData.getShotlockEnemies().size() < shotlock.getMaxLocks()){
						HitResult rt = InputHandler.getMouseOverExtended(ModConfigs.shotlockMaxDist);

						if (rt == null)
							return;

						if (rt instanceof EntityHitResult ertr) {
							if (ertr.getEntity() instanceof LivingEntity target) {
								if(target != tempShotlockEntity){
									focusingAnEntityTicks = 0;
									event.player.level().playSound(event.player, event.player.position().x(), event.player.position().y(), event.player.position().z(), ModSounds.shotlock_lockon_idle.get(), SoundSource.PLAYERS, 1F, 1F);
									event.player.level().playSound(event.player, event.player.position().x(), event.player.position().y(), event.player.position().z(), ModSounds.shotlock_lockon_start.get(), SoundSource.PLAYERS, 1F, 1F);
								}
								tempShotlockEntity = target;
								Party p = ModCapabilities.getWorld(mc.level).getPartyFromMember(event.player.getUUID());
								if (p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) { // If caster is not in a party || the party doesn't have the target in it || the party has FF on
									if(focusingAnEntityTicks >= shotlock.getCooldown()) {
										playerData.addShotlockEnemy(new Utils.ShotlockPosition(target.getId(), Utils.randomWithRange(0, target.getBbWidth() * 2) - target.getBbWidth(), Utils.randomWithRange(0, target.getBbHeight() * 2) - target.getBbHeight(), Utils.randomWithRange(0, target.getBbWidth() * 2) - target.getBbWidth()));
										event.player.level().playSound(event.player, event.player.position().x(), event.player.position().y(), event.player.position().z(), ModSounds.shotlock_lockon_all.get(), SoundSource.PLAYERS, 1F, 1F);
										cost = playerData.getFocus() - focusGaugeTemp;
										tempShotlockEntity = null;
									}
									focusingAnEntityTicks++;
								}
							}
						}
						if (rt instanceof BlockHitResult brtr) {
							tempShotlockEntity = null;

						}

						} else {
						if (focusingTicks % shotlock.getCooldown() == 1 && focusGaugeTemp > 0 && playerData.getShotlockEnemies().size() < shotlock.getMaxLocks()) {
							HitResult rt = InputHandler.getMouseOverExtended(ModConfigs.shotlockMaxDist);
							if (rt == null)
								return;

							if (rt instanceof EntityHitResult ertr) {
								Party p = ModCapabilities.getWorld(mc.level).getPartyFromMember(event.player.getUUID());
								if (ertr.getEntity() instanceof LivingEntity target) {
									if (p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) { // If caster is not in a party || the party doesn't have the target in it || the party has FF on
										playerData.addShotlockEnemy(new Utils.ShotlockPosition(target.getId(), Utils.randomWithRange(0, target.getBbWidth() * 2) - target.getBbWidth(), Utils.randomWithRange(0, target.getBbHeight() * 2) - target.getBbHeight(), Utils.randomWithRange(0, target.getBbWidth() * 2) - target.getBbWidth()));

										event.player.level().playSound(event.player, event.player.position().x(), event.player.position().y(), event.player.position().z(), ModSounds.shotlock_lockon.get(), SoundSource.PLAYERS, 1F, 1F);
										cost = playerData.getFocus() - focusGaugeTemp;
										tempShotlockEntity = null;

										if (playerData.getShotlockEnemies().size() >= shotlock.getMaxLocks()) {
											event.player.level().playSound(event.player, event.player.position().x(), event.player.position().y(), event.player.position().z(), ModSounds.shotlock_lockon_all.get(), SoundSource.PLAYERS, 1F, 1F);
										}
									}
								}
							}

							if (rt instanceof BlockHitResult blockResult) {
								if (event.player.level().getBlockState(blockResult.getBlockPos()) == ModBlocks.airstepTarget.get().defaultBlockState()) {
									if (!lockedAirStep.equals(blockResult.getBlockPos())) {
										event.player.level().playSound(event.player, event.player.position().x(), event.player.position().y(), event.player.position().z(), ModSounds.shotlock_lockon.get(), SoundSource.PLAYERS, 1F, 0.5F);
									}
									if (mc.options.keyUse.isDown()) {
										PacketHandler.sendToServer(new CSSetAirStepPacket(blockResult.getBlockPos()));
										lockedAirStep = new BlockPos(0, 0, 0);
										cooldownTicks = 20;
										focusingAnEntityTicks = 0;
										focusingTicks = 0;
										focusing = false;
										tempShotlockEntity = null;
										focusGaugeTemp = playerData.getFocus();
										return;
									}
								}
								lockedAirStep = blockResult.getBlockPos();

							}
						}
					}
					
					if(mc.options.keyAttack.isDown()) {
						if (focusingTicks > 0) {
							// Has finished shotlocking, send packet to spawn entities and track enemies
							if(!playerData.getShotlockEnemies().isEmpty()) {
								playerData.remFocus(cost);
								event.player.level().playSound(event.player, event.player.position().x(),event.player.position().y(),event.player.position().z(), ModSounds.shotlock_shot.get(), SoundSource.PLAYERS, 1F, 1F);
								PacketHandler.sendToServer(new CSShotlockShot(cost, playerData.getShotlockEnemies()));
								cooldownTicks = 100;
								focusing = false;
							}
						}
						focusingTicks = 0;
						focusingAnEntityTicks = 0;
						tempShotlockEntity = null;
						focusGaugeTemp = playerData.getFocus();
						playerData.setShotlockEnemies(new ArrayList<>());
					}
				} else {
					lockedAirStep = new BlockPos(0,0,0);
					focusingTicks = 0;
					focusingAnEntityTicks = 0;
					tempShotlockEntity = null;
					focusGaugeTemp = playerData.getFocus();
					playerData.setShotlockEnemies(new ArrayList<>());
				}
			} else {
				if(cooldownTicks > 0) {
					cooldownTicks--;
				}
			}
		}
	}
	
	public void blit(PoseStack matrixStack, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight) {
		blit(matrixStack, x, y, 0, (float) uOffset, (float) vOffset, uWidth, vHeight, 256, 256);
	}

	public static void blit(PoseStack matrixStack, int x, int y, int blitOffset, float uOffset, float vOffset, int uWidth, int vHeight, int textureHeight, int textureWidth) {
		innerBlit(matrixStack, x, x + uWidth, y, y + vHeight, blitOffset, uWidth, vHeight, uOffset, vOffset, textureWidth, textureHeight);
	}

	private static void innerBlit(PoseStack matrixStack, int x1, int x2, int y1, int y2, int blitOffset, int uWidth, int vHeight, float uOffset, float vOffset, int textureWidth, int textureHeight) {
		innerBlit(matrixStack.last().pose(), x1, x2, y1, y2, blitOffset, (uOffset + 0.0F) / (float) textureWidth, (uOffset + (float) uWidth) / (float) textureWidth, (vOffset + 0.0F) / (float) textureHeight, (vOffset + (float) vHeight) / (float) textureHeight);
	}

	private static void innerBlit(Matrix4f matrix, int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferbuilder.vertex(matrix, (float) x1, (float) y2, (float) blitOffset).uv(minU, maxV).endVertex();
		bufferbuilder.vertex(matrix, (float) x2, (float) y2, (float) blitOffset).uv(maxU, maxV).endVertex();
		bufferbuilder.vertex(matrix, (float) x2, (float) y1, (float) blitOffset).uv(maxU, minV).endVertex();
		bufferbuilder.vertex(matrix, (float) x1, (float) y1, (float) blitOffset).uv(minU, minV).endVertex();
		bufferbuilder.end();
		RenderSystem.enableBlend();
		//BufferUploader.end(bufferbuilder);
	}
	
	@SubscribeEvent
	public void PlayerClick(InteractionKeyMappingTriggered event) {
		if(event.isPickBlock()) {
			Minecraft mc = Minecraft.getInstance();
			if(mc.player.getMainHandItem() != null && Utils.getPlayerShotlock(mc.player) != null && (mc.player.getMainHandItem().getItem() instanceof KeybladeItem || mc.player.getMainHandItem().getItem() instanceof IOrgWeapon)){
				event.setCanceled(true);
			}
		}	
	}

	public static void colourTint(RegisterColorHandlersEvent.Block event) {
		event.register(ClientEvents::getStructureWallColour, ModBlocks.structureWall.get());
	}

	public static int getStructureWallColour(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex) {
		Color colour = Color.BLACK;
		if (Minecraft.getInstance().level.dimension().location().getPath().contains("castle_oblivion_")) {
			CastleOblivionCapabilities.ICastleOblivionInteriorCapability cap = ModCapabilities.getCastleOblivionInterior(Minecraft.getInstance().level);
			if (cap != null) {
				if (!cap.getFloors().isEmpty()) {
					Room room = cap.getRoomAtPos(Minecraft.getInstance().level, pos);
					if (room != null) {
						if (room.getType().getProperties().getColour() != null) {
							colour = room.getType().getProperties().getColour();
						} else {
							Floor floor = room.getParent(Minecraft.getInstance().level);
							if (floor != null) {
								colour = floor.getType().floorColour;
							}
						}
					}
				}
			}
		}
		return colour.getRGB();
	}

	public static void itemColour(RegisterColorHandlersEvent.Item event) {
		event.register((pStack, pTintIndex) -> {
			Color colour = Color.WHITE;
			int itemColor = ((WayfinderItem)pStack.getItem()).getColor(pStack);
			colour = new Color(itemColor);
			return colour.getRGB();
		}, ModItems.wayfinder.get());
	}

}