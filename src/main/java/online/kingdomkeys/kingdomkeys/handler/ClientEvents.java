package online.kingdomkeys.kingdomkeys.handler;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.event.EquipmentEvent;
import online.kingdomkeys.kingdomkeys.block.FlowmotionRailBlock;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiBlockBase;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiPlacementType;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.KOGui;
import online.kingdomkeys.kingdomkeys.client.gui.StopGui;
import online.kingdomkeys.kingdomkeys.client.gui.elements.CommandMenuSubMenu;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.CommandMenuGui;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.ItemGetGui;
import online.kingdomkeys.kingdomkeys.client.shotlock.ShotlockMinigameClient;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.entity.KKVehicleEntity;
import online.kingdomkeys.kingdomkeys.integration.epicfight.EpicFightUtils;
import online.kingdomkeys.kingdomkeys.integration.shouldersurfing.KKShoulderSurfing;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.item.WayfinderItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.lib.Struggle;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.*;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.kingdomkeys.kingdomkeys.sound.AlarmSoundInstance;
import online.kingdomkeys.kingdomkeys.util.IDisabledAnimations;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.StruggleHandler;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionHandler;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.Floor;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomModifiers;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.RoomModifier;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.Supplier;

public class ClientEvents {

	@SubscribeEvent
	public void onEquipmentChange(EquipmentEvent.Magic e) {
		CommandMenuSubMenu submenu = CommandMenuGui.commandMenuElements.get(CommandMenuGui.INSTANCE.currentSubmenu);
		if(submenu.getId().equals(CommandMenuGui.INSTANCE.magic)) {
			CommandMenuGui.INSTANCE.createMagicSpells(submenu);
		}
		if(submenu.getId().equals(CommandMenuGui.INSTANCE.attack)) {
			CommandMenuGui.INSTANCE.createPhysicalSpells(submenu);
		}
	}

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
	public void onRenderTick(RenderFrameEvent.Pre event) {
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;

		if (mc.level == null || player == null || InputHandler.lockOn == null) {
			if (KingdomKeys.shoulderSurfingLoaded) {
				KKShoulderSurfing.enableDecoupling();
			}
			return;
		}

		LivingEntity target = InputHandler.lockOn;
		if (target.getHealth() <= 0 || target.isRemoved()) {
			InputHandler.lockOn = null;
			player.playSound(ModSounds.lockoff.get(), 1.0f, 1.0f);

			if (KingdomKeys.shoulderSurfingLoaded) {
				KKShoulderSurfing.enableDecoupling();
			}
			return;
		}

		if (KingdomKeys.shoulderSurfingLoaded) {
			KKShoulderSurfing.disableDecoupling();
		}

		if (ModConfigs.SERVER_SPEC.isLoaded()) {
			if (ModConfigs.SERVER.softLockOnMode.get())
				softLockOn(player, target);
			else
				hardLockOn(player, target);
		}

	}

	/**
	 * New method to allow moving the camera slightly
	 * @param player
	 * @param target
	 */
	private void softLockOn(Player player, LivingEntity target) {
		Minecraft mc = Minecraft.getInstance();

		double verticalFovDeg = mc.options.fov().get();
		double verticalFovRad = Math.toRadians(verticalFovDeg);

		Window window = mc.getWindow();
		double aspect = (double) window.getGuiScaledWidth() / window.getGuiScaledHeight();

		double horizontalFovRad = 2.0 * Math.atan(Math.tan(verticalFovRad / 2.0) * aspect);
		double horizontalFovDeg = Math.toDegrees(horizontalFovRad);

		float maxYawOffset = (float) horizontalFovDeg * 0.4f;
		float maxPitchOffset = (float) verticalFovDeg * 0.4f;

		final float CORRECTION_SMOOTH = 0.15f;

		double dx = target.getX() - player.getX();
		double dz = target.getZ() - player.getZ();
		double dy = (target.getY() + target.getBbHeight() * 0.5) - (player.getY() + player.getEyeHeight());

		float targetYaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
		float targetPitch = (float) (-Math.toDegrees(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz))));

		float currentYaw = player.getYRot();
		float currentPitch = player.getXRot();

		float yawDiff = Mth.wrapDegrees(targetYaw - currentYaw);
		float pitchDiff = targetPitch - currentPitch;

		float yawCorrection = 0f;
		float pitchCorrection = 0f;

		if (yawDiff > maxYawOffset) {
			yawCorrection = yawDiff - maxYawOffset;
		} else if (yawDiff < -maxYawOffset) {
			yawCorrection = yawDiff + maxYawOffset;
		}

		if (pitchDiff > maxPitchOffset) {
			pitchCorrection = pitchDiff - maxPitchOffset;
		} else if (pitchDiff < -maxPitchOffset) {
			pitchCorrection = pitchDiff + maxPitchOffset;
		}

		if (yawCorrection != 0 || pitchCorrection != 0) {
			player.setYRot(currentYaw + yawCorrection * CORRECTION_SMOOTH);
			player.setXRot(currentPitch + pitchCorrection * CORRECTION_SMOOTH);
			if (KingdomKeys.shoulderSurfingLoaded) {
				KKShoulderSurfing.setCameraPos(currentYaw, currentPitch, yawCorrection, pitchCorrection, CORRECTION_SMOOTH);
			}

			player.yRotO = currentYaw;
			player.xRotO = currentPitch;

			if (player.getVehicle() != null) {
				player.getVehicle().onPassengerTurned(player);
			}
		}
	}

	/**
	 * Old method to lock the camera strictly on the entity
	 * @param player
	 * @param target
	 */
	private void hardLockOn(Player player, LivingEntity target) {
		double dx = target.getX() - player.getX();
		double dz = target.getZ() - player.getZ();
		double dy = (target.getY() + target.getBbHeight() * 0.5) - (player.getY() + player.getEyeHeight());

		double angleYaw = Math.toDegrees(Math.atan2(dz, dx)) - 90.0;
		double anglePitch = -Math.toDegrees(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)));

		float currentYaw = player.getYRot();
		float currentPitch = player.getXRot();

		float yawDifference = Mth.wrapDegrees((float) angleYaw - currentYaw);
		float pitchDifference = (float) anglePitch - currentPitch;

		float smoothFactor = 0.2F;

		float newYaw = currentYaw + yawDifference * smoothFactor;
		float newPitch = currentPitch + pitchDifference * smoothFactor;

		player.setYRot(newYaw);
		player.setXRot(newPitch);

		if (KingdomKeys.shoulderSurfingLoaded) {
			KKShoulderSurfing.setCameraPos(currentYaw, currentPitch, 0, 0, 0);
		}

		player.yRotO = currentYaw;
		player.xRotO = currentPitch;

		if (player.getVehicle() != null) {
			player.getVehicle().onPassengerTurned(player);
		}
	}

	@SubscribeEvent
	public void onCameraSetup(CalculateDetachedCameraDistanceEvent event) {
		Camera camera = event.getCamera();
		Entity viewEntity = camera.getEntity();
		if (viewEntity instanceof Player player && player.getVehicle() instanceof GummiShipEntity ship) {
			if(ship.structure != null){
				Vec3i realSize = Utils.getRealGummiStructureSize(ship.structure);
				int maxSize = Math.max(Math.max(realSize.getX(), realSize.getY()), realSize.getZ());

				if (Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_BACK) {
					event.setDistance(maxSize * 1.2F);
				} else if(Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_FRONT) {
					event.setDistance(maxSize * 0.8F);
				}

			}
		}
	}

	boolean handledCamera = false;
	boolean prevPickItemDown = false;
	public static CameraType prevCamera = CameraType.FIRST_PERSON;


	@SubscribeEvent
	public void onLivingUpdate(EntityTickEvent.Pre event) {
		if(event.getEntity() instanceof LocalPlayer player){
			if(player.getControlledVehicle() instanceof KKVehicleEntity vehicle) {
				vehicle.setInput(player.input.left, player.input.right, player.input.up, player.input.down, Minecraft.getInstance().options.keyJump.isDown(), Minecraft.getInstance().options.keySprint.isDown(), player.getXRot(), player.getYRot());

				if (vehicle instanceof GummiShipEntity) {
					boolean pickItemDown = Minecraft.getInstance().options.keyPickItem.isDown();
					if (pickItemDown && !prevPickItemDown) {
						PacketHandler.sendToServer(new CSToggleFlightModePacket());
					}
					prevPickItemDown = pickItemDown;
				}
			}

			//From wall hang to bounce up with jump (SPACE)
			PlayerData playerData = PlayerData.get(player);
			if(playerData != null) {
				if (playerData.getHangingInWallTicks() > 0) {
					if(Minecraft.getInstance().options.keyJump.isDown()) {
						if(!playerData.hasBounced()) { //If has not bounced before bounce
							Vec3 look = player.getLookAngle();

							Vec3 horizontalDir = new Vec3(-look.x, 0, -look.z).normalize();
							double baseY = 1.5;

							if (InputHandler.jumpRayTrace instanceof BlockHitResult blockHitResult) {
								switch (blockHitResult.getDirection()) {
									case NORTH -> horizontalDir = new Vec3(0, 0, -1);
									case SOUTH -> horizontalDir = new Vec3(0, 0, 1);
									case WEST  -> horizontalDir = new Vec3(-1, 0, 0);
									case EAST  -> horizontalDir = new Vec3(1, 0, 0);
								}
							}

							float pow = 0.35F + playerData.getNumberOfAbilitiesEquipped(ModAbilities.SUPERJUMP) * 0.15F;
							double horizontalStrength = 0.25;
							double verticalStrength = baseY * pow;

							Vec3 push = new Vec3(horizontalDir.x * horizontalStrength, verticalStrength, horizontalDir.z * horizontalStrength);

							player.setDeltaMovement(push);
							player.hasImpulse = true;
							PacketHandler.sendToServer(new CSPlaySoundPacket(player.getX(), player.getY(), player.getZ(), ModSounds.wall_jump.get().getLocation(), SoundSource.PLAYERS));

							PacketHandler.sendToServer(new CSSetBouncedPacket(true));
							playerData.setBounced(true);
							playerData.setAirDashed(false);
							PacketHandler.sendToServer(new CSSetAirDashedPacket(false));
							InputHandler.qrCooldown = 5;
						}
					}
				}
			}

		}

		if (event.getEntity() instanceof Player player) {
			// Everything below only ever applies to players (KO/STOP effects, magic cast-time lock),
			// so we check for Player first instead of fetching GlobalData for every single LivingEntity
			// (every mob, animal, etc.) ticking client-side each tick - GlobalData.get() also lazily
			// attaches a GlobalData instance to whatever entity it's called on, which we don't want to
			// do to every nearby mob just to immediately discard the result.
			if (player == Minecraft.getInstance().player) {
				if (player.hasEffect(ModMobEffects.KO)) {
					if (player.level().isClientSide) {
						if (player.isDeadOrDying())
							return;

						//Force the 3rd person view when KO
						if (!handledCamera) {
							// Store and swap camera if needed
							prevCamera = Minecraft.getInstance().options.getCameraType();
							Minecraft.getInstance().options.setCameraType(CameraType.THIRD_PERSON_BACK);
							handledCamera = true;
						}

						if (!(Minecraft.getInstance().screen instanceof KOGui))
							Minecraft.getInstance().setScreen(new KOGui());
					}
				} else { //If doesn't have KO
					if (handledCamera) {
						Minecraft.getInstance().options.setCameraType(prevCamera);
						handledCamera = false;
					}
				}
			}
			if (player.hasEffect(ModMobEffects.STOP)) {
				if (player.level().isClientSide && player == Minecraft.getInstance().player) {
					if (Minecraft.getInstance().screen == null)
						Minecraft.getInstance().setScreen(new StopGui());
				}
				event.setCanceled(true);
			}
			PlayerData playerData = PlayerData.get(player);
			if (playerData != null) {
				if (playerData.getMagicCasttimeTicks() > 0) {
					player.setDeltaMovement(0, 0, 0);
				}
			}

			if (player == Minecraft.getInstance().player) { //Local player
				if (InputHandler.qrCooldown > 0) {
					InputHandler.qrCooldown -= 1;
				}
			}
		}
	}


	@SubscribeEvent
	public void onRenderWorld(RenderHighlightEvent.Block event) {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (player == null || mc.level == null || mc.options.hideGui)
			return;

		if (!(player.getMainHandItem().getItem() instanceof BlockItem blockItem) || event.getTarget().getDirection() == Direction.DOWN || event.getTarget().getDirection() == Direction.UP)
			return;

		if(blockItem.getBlock() instanceof GummiBlockBase blockBase && (blockBase.getPlacementType() == GummiPlacementType.EDGE || blockBase.getPlacementType() == GummiPlacementType.CORNER)) {
			BlockPos pos = event.getTarget().getBlockPos();
			Direction face = event.getTarget().getDirection();

			PoseStack poseStack = event.getPoseStack();
			MultiBufferSource buffer = event.getMultiBufferSource();

			Vec3 cameraPos = event.getCamera().getPosition();

			BlockState state = mc.level.getBlockState(pos);
			VoxelShape shape = state.getShape(mc.level, pos);
			if (shape.isEmpty())
				shape = Shapes.block(); // fallback
			double x = (pos.getX()) - cameraPos.x;
			double y = pos.getY() - cameraPos.y;
			double z = pos.getZ() - cameraPos.z;

			double offset = 0.001;

			poseStack.pushPose();
			{
				VertexConsumer builder = buffer.getBuffer(RenderType.lines());
				double dx = 0, dy = 0, dz = 0;
				switch (face) {
					case UP -> dy = offset + shape.bounds().maxY;
					case DOWN -> dy = -offset + shape.bounds().minY;
					case NORTH -> dz = -offset;
					case SOUTH -> dz = 1 + offset;
					case EAST -> dx = 1 + offset;
					case WEST -> dx = -offset;
				}

				if (blockItem.getBlock() instanceof GummiBlockBase gummiBlockBase && gummiBlockBase.getPlacementType() == GummiPlacementType.CORNER)
					ClientUtils.drawPlusOnFace(poseStack, builder, x + dx, y + dy, z + dz, face);
				else if (blockItem.getBlock() instanceof GummiBlockBase gummiBlockBase && gummiBlockBase.getPlacementType() == GummiPlacementType.EDGE)
					ClientUtils.drawXOnFace(poseStack, builder, x + dx, y + dy, z + dz, face);

			}
			poseStack.popPose();
		}
	}

	@SubscribeEvent
	public void RenderEntity(RenderLivingEvent.Post<Player, ? extends PlayerModel<Player>> event) { //Hide the player shadow when KO'd
		if(event.getEntity() != null) {
			if(event.getEntity().hasEffect(ModMobEffects.KO)) {
				event.getPoseStack().mulPose(Axis.XP.rotationDegrees(90));
				event.getPoseStack().scale(0.01F, 0.01F, 0.01F);
			}
		}
	}

	@SubscribeEvent
	public void onRenderLevel(RenderLevelStageEvent event) {
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES)
			return;

		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		if (player == null)
			return;

		PoseStack poseStack = event.getPoseStack();
		MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
		PlayerData localPlayerData = PlayerData.get(player);

		float partialTicks = mc.getTimer().getGameTimeDeltaPartialTick(false);

		/*if (DEBUG_GUMMI_COLLISION) {
			drawGummiCollision(mc, poseStack, buffer);
		}*/

		// Lock on
		if (InputHandler.lockOn != null && ModConfigs.SERVER.softLockOnMode.get()) {
			ClientUtils.drawLockOnIndicator(InputHandler.lockOn.getId(), poseStack, buffer, partialTicks);
		}

		// Single shotlock indicator (Ultima cannon)
		Shotlock shotlock = Utils.getPlayerShotlock(mc.player);
		if (shotlock != null) {
			boolean singleLock = shotlock.getMaxLocks() == 1;

			if (tempShotlockEntity != null || (singleLock && !localPlayerData.getShotlockEnemies().isEmpty())) {
				int entityID = tempShotlockEntity == null ? localPlayerData.getShotlockEnemies().getFirst().id() : tempShotlockEntity.getId();
				ClientUtils.drawSingleShotlockIndicator(entityID, poseStack, buffer, partialTicks);
			}

			//Normal shotlocks
			if (focusing && !singleLock && localPlayerData.getShotlockEnemies() != null && !localPlayerData.getShotlockEnemies().isEmpty()) {
				for (Utils.ShotlockPosition sh : localPlayerData.getShotlockEnemies()) {
					ClientUtils.drawShotlockIndicator(sh, poseStack, buffer, partialTicks);
				}
			}

			if (lockedAirStepEntity != null) {
				ClientUtils.drawAirstepIndicator(lockedAirStepEntity.getId(), poseStack, buffer, partialTicks);
			}
		}

		Camera camera = mc.gameRenderer.getMainCamera();
		Vec3 camPos = camera.getPosition();

		//Flowmotion trails
		for (Player p : mc.level.players()) {
			if (p.distanceToSqr(mc.player) > 100 * 100) //Only update and render trails if the player currently iterating is closer than 100 blocks
				continue;
			PlayerData playerData = PlayerData.get(p);
			if(playerData == null)
				continue;

			float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);
			if (playerData.inFlowmotion()) {
				ClientUtils.updateTrail(ClientUtils.TrailType.FLOWMOTION, p, partialTick, 200);
			} else {
				ClientUtils.fadeTrail(ClientUtils.TrailType.FLOWMOTION, p);
			}

			if (playerData.hasAirDashed()) {
				ClientUtils.updateTrail(ClientUtils.TrailType.DASH, p, partialTick, 50);
			} else {
				ClientUtils.fadeTrail(ClientUtils.TrailType.DASH, p);
			}

			if (!playerData.getAirStep().equals(BlockPos.ZERO)) {
				ClientUtils.updateTrail(ClientUtils.TrailType.AIRSTEP, p, partialTick, 300);
			} else {
				ClientUtils.fadeTrail(ClientUtils.TrailType.AIRSTEP, p);
			}


			poseStack.pushPose();
			{
				poseStack.translate(-camPos.x, -camPos.y, -camPos.z);
				ClientUtils.renderTrail(ClientUtils.TrailType.FLOWMOTION, p, poseStack, buffer, -5F,0,1F,0.2F,1F, false);
				ClientUtils.renderTrail(ClientUtils.TrailType.FLOWMOTION, p, poseStack, buffer, 0F,0,0.2F,0.6F,1F, false);
				ClientUtils.renderTrail(ClientUtils.TrailType.FLOWMOTION, p, poseStack, buffer, 5F,0,1F,0.2F,1F, false);

				ClientUtils.renderTrail(ClientUtils.TrailType.FLOWMOTION, p, poseStack, buffer, -5F,0,1F,0.2F,1F, true);
				ClientUtils.renderTrail(ClientUtils.TrailType.FLOWMOTION, p, poseStack, buffer, 5F,0,1F,0.2F,1F, true);

				//DASH
				//Legs
				ClientUtils.renderTrail(ClientUtils.TrailType.DASH, p, poseStack, buffer, -4F,0.2F,1F,1F,1F, false);
				ClientUtils.renderTrail(ClientUtils.TrailType.DASH, p, poseStack, buffer, 4F,0.2F,1F,1F,1F, false);

				//Shoulders
				ClientUtils.renderTrail(ClientUtils.TrailType.DASH, p, poseStack, buffer, -7F,1.2F,1F,1F,1F, false);
				ClientUtils.renderTrail(ClientUtils.TrailType.DASH, p, poseStack, buffer, 7F,1.2F,1F,1F,1F, false);

				//Body
				ClientUtils.renderTrail(ClientUtils.TrailType.DASH, p, poseStack, buffer, 0,1.8F,1F,1F,1F, false);

				//AIRSTEP - tinted with the player's own notification colour, same as the old particle was
				Color airStepColor = new Color(playerData.getNotifColor());
				float airR = airStepColor.getRed() / 255F;
				float airG = airStepColor.getGreen() / 255F;
				float airB = airStepColor.getBlue() / 255F;

				ClientUtils.renderTrail(ClientUtils.TrailType.AIRSTEP, p, poseStack, buffer, 0, 1F, airR, airG, airB, false);
				ClientUtils.renderTrail(ClientUtils.TrailType.AIRSTEP, p, poseStack, buffer, -3F, 1F, airR, airG, airB, true);
				ClientUtils.renderTrail(ClientUtils.TrailType.AIRSTEP, p, poseStack, buffer, 3F, 1F, airR, airG, airB, true);
			}
			poseStack.popPose();
		}



		poseStack.pushPose();
		{
			poseStack.translate(-camPos.x, -camPos.y, -camPos.z);
			//Magnet blox trails
			ClientUtils.updateMiniTrails();
			ClientUtils.renderMiniTrails(poseStack, buffer, partialTicks);
		}
		poseStack.popPose();
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void RenderEntity(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event) {
		if(event.getEntity() != null) {
			if(event.getEntity() instanceof Player player) {
				PlayerData playerData = PlayerData.get(player);
				if(player.getVehicle() != null && player.getVehicle() instanceof GummiShipEntity ship){
					LocalPlayer localPlayer = Minecraft.getInstance().player;
					//Stop rendering players if they are in different ships than the local player
					if(localPlayer.getVehicle() != ship) { //make other players invis
						event.setCanceled(true);
					} else { //For same ship only make em invis in 3rd person
						if (Minecraft.getInstance().options.getCameraType() != CameraType.FIRST_PERSON) { //Make it so
							event.setCanceled(true);
						}
					}
				}

				if(player.hasEffect(ModMobEffects.KO)) {
					LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer = (LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer((AbstractClientPlayer) player);
					if (!((IDisabledAnimations) renderer).kingdom_Keys$isDisabled()) {
						//Cancel the vanilla animation
						event.setCanceled(true);

						PoseStack pose = event.getPoseStack();
						MultiBufferSource buffer = event.getMultiBufferSource();
						int light = event.getPackedLight();

						pose.pushPose();
						{
							float MAX = 100;
							float MAX2 = 35;

							double t = player.tickCount % MAX;
							double t2 = player.tickCount % MAX2;

							double bob = (t < MAX / 2) ? (t / (MAX / 2D)) : ((MAX - t) / (MAX / 2D));
							double bob2 = (t2 < MAX2 / 2) ? (t2 / (MAX2 / 2D)) : ((MAX2 - t2) / (MAX2 / 2D));

							//Render body
							pose.pushPose();
							{
								pose.mulPose(Axis.XP.rotationDegrees(90));
								pose.mulPose(Axis.ZP.rotationDegrees(90));

								pose.translate(0, -0.5, bob * 0.3 - 0.8F);

								ResourceLocation tex = ((AbstractClientPlayer) player).getSkin().texture();
								renderer.getModel().renderToBuffer(pose, buffer.getBuffer(RenderType.entityCutout(tex)), light, LivingEntityRenderer.getOverlayCoords(player, 0), 0xffffff);
							}
							pose.popPose();

							String name = player.getDisplayName().getString();

							ClientUtils.renderNameTag(renderer, player, name, pose, buffer, light, event.getPartialTick());

							pose.translate(0, -bob2 * 0.15 - 0.8F + 0.9F, 0);
							ClientUtils.renderHeart(pose,buffer,player);

						}
						pose.popPose();
					}
				}

				if(playerData != null) {
					if(!playerData.getAirStep().equals(BlockPos.ZERO)){
						// Still hidden while stepping; the streak that replaces the old dust trail is drawn
						// from the level render, so cancelling the player here does not take it with it.
						event.setCanceled(true);
					}

					// Aerial Dodge rotation
					if(playerData.getAerialDodgeTicks() > 0) {
						LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer = (LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer((AbstractClientPlayer) player);
						if (!((IDisabledAnimations) renderer).kingdom_Keys$isDisabled()) {
							float partialTicks = event.getPartialTick();
							float time = player.tickCount + partialTicks;

							event.getPoseStack().mulPose(Axis.YP.rotationDegrees(-time * 100));
						}
					}

					if(playerData.isFormActive(ModDriveForms.ANTI)) {
						player.level().addParticle(ParticleTypes.SMOKE, player.getX()+player.level().random.nextDouble() - 0.5D, player.getY()+player.level().random.nextDouble() *2D, player.getZ()+player.level().random.nextDouble() - 0.5D, (player.level().random.nextDouble() - 0.5D)*0.2, 0.1, (player.level().random.nextDouble() - 0.5D)*0.2);
					} else if(playerData.isFormActive(ModDriveForms.WISDOM)) {
						player.level().addParticle(new DustParticleOptions(new Vector3f(0F,1F,1F),1F), player.getX(), player.getY(), player.getZ(), 0, 0.3, 0);
					}
				}
			}
		}
	}

    /* @SubscribeEvent
    public void onRenderLivingPost(RenderLivingEvent.Post<?, ?> event) {
        if (!(event.getEntity() instanceof Player player)) return;

        PlayerData playerData = PlayerData.get(player); // tu sistema

        if (playerData.inFlowmotion()) {
            ClientUtils.updateTrail(player, event.getPartialTick()); // solo añadir puntos
        } else {
            ClientUtils.fadeTrail(player); // eliminar poco a poco
        }


        ClientUtils.renderTrail(player, event.getPoseStack(), event.getMultiBufferSource(), event.getPartialTick());
    }*/

	private static int selectedSlot = 0;

	private static long timeSinceLastshot = 0;

	public static int gummiBoostCD;

	@SubscribeEvent
	public void clientTickPre(ClientTickEvent.Pre event) {
		Minecraft mc = Minecraft.getInstance();

		if (mc.level == null) {
			ItemGetGui.INSTANCE.clearItems();
			// Back at the main menu: forget the nudges so the next world gets its own
			pressMToast = null;
			pressMDismissed = false;
			moogleToast = null;
		} else {
			ItemGetGui.INSTANCE.tick();
			tickPressMHint(mc);
			tickMoogleHint(mc);
		}

		if (mc.level != null)
			selectedSlot = Minecraft.getInstance().player.getInventory().selected;

		if (mc.player == null || !(mc.player.getVehicle() instanceof GummiShipEntity ship))
			return;

		if (ship.shipStats == null)
			return;

		if (timeSinceLastshot > 0) {
			timeSinceLastshot--;
		}

		if(ship.getFuel() > 0) {
			if(!ship.shipStats.firepower().isEmpty()){
				int baseTicks = 14;
				int weaponCount = Math.max(1, ship.shipStats.firepower().size());
				int delayTicks = Math.max(1, Math.round(baseTicks / (float)weaponCount));

				if (EpicFightUtils.isAttacking() && timeSinceLastshot <= 0) {
					timeSinceLastshot = delayTicks;
					PacketHandler.sendToServer(new CSGummiFirePacket(ship.getId(), false));
				}
			}

			if(gummiBoostCD <= 0) {
				if (InputHandler.Keybinds.ACTION.getKeybind().isDown()) { //TODO check for booster block
					float yaw = ship.getYRot();
					float motionX = -Mth.sin(yaw / 180.0f * (float) Math.PI);
					float motionZ = Mth.cos(yaw / 180.0f * (float) Math.PI);
					double power = ship.shipStats.getEffectiveSpeed()*10;
					ship.push(motionX * power, 0, motionZ * power);
					PacketHandler.sendToServer(new CSGummiBoostPacket(ship.getId()));
					gummiBoostCD = 5 * 20;
				}
			} else {
				gummiBoostCD--;
			}
		}
	}

	private static TutorialToast pressMToast;
	private static boolean pressMDismissed;

	private void tickPressMHint(Minecraft mc) {
		if (mc.player == null || pressMDismissed) {
			return;
		}

		PlayerData playerData = PlayerData.get(mc.player);

		// Nothing to hint at once the heart has been dived into
		if (playerData == null || playerData.getChosen() != SoAState.NONE) {
			hidePressMHint();
			return;
		}

		if (pressMToast == null) {
			pressMToast = new TutorialToast(TutorialToast.Icons.RECIPE_BOOK, Component.translatable("advancements.kingdomkeys.press_m_hint"), Component.translatable("advancements.kingdomkeys.press_m_hint.desc"), false);
			mc.getToasts().addToast(pressMToast);
		}
	}

	// Hitbox render for gummi blocks
	/*public static final boolean DEBUG_GUMMI_COLLISION = true;
	private static final int DEBUG_RANGE = 6;

	private void drawGummiCollision(Minecraft mc, PoseStack poseStack, MultiBufferSource.BufferSource buffer) {
		Vec3 camera = mc.gameRenderer.getMainCamera().getPosition();
		BlockPos around = mc.player.blockPosition();
		VertexConsumer lines = buffer.getBuffer(RenderType.lines());

		poseStack.pushPose();
		poseStack.translate(-camera.x, -camera.y, -camera.z);

		for (BlockPos pos : BlockPos.betweenClosed(around.offset(-DEBUG_RANGE, -DEBUG_RANGE, -DEBUG_RANGE), around.offset(DEBUG_RANGE, DEBUG_RANGE, DEBUG_RANGE))) {
			BlockState state = mc.level.getBlockState(pos);
			if (!(state.getBlock() instanceof GummiBlockBase gummi)) {
				continue;
			}

			VoxelShape shape = gummi.debugCollisionShape(state);
			if (shape == null) {
				continue;
			}

			for (AABB box : shape.toAabbs()) {
				LevelRenderer.renderLineBox(poseStack, lines, box.move(pos), 1F, 0.2F, 0.2F, 1F);
			}
		}

		poseStack.popPose();
		buffer.endBatch(RenderType.lines());
	}*/

	private static final double GRIND_SPEED = 0.8D;
	private static final double HOP_OFF = 0.55D;
	private static final int RELATCH_DELAY = 10;
	private static final int REVERSE_DELAY = 8;

	private static final int MAX_STEPS_PER_TICK = 24;

	private static BlockPos grindRail;
	private static Direction grindDir;
	private static Vec3[] grindPath;
	private static int grindStep;
	private static int grindCooldown;
	private static int reverseCooldown;

	@SubscribeEvent
	public void grindTick(PlayerTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();

		if (event.getEntity() == mc.player) {
			tickGrind(mc);
		}
	}

	private void tickGrind(Minecraft mc) {
		LocalPlayer player = mc.player;
		if (player == null) {
			return;
		}

		if (grindCooldown > 0) {
			grindCooldown--;
		}
		if (reverseCooldown > 0) {
			reverseCooldown--;
		}

		if (grindRail == null) {
			tryStartGrind(player);
			return;
		}

		RailShape shape = FlowmotionRailBlock.shapeAt(player.level(), grindRail);
		if (shape == null) {
			stopGrind(player, false);
			return;
		}

		if (mc.options.keyJump.isDown()) {
			stopGrind(player, true);
			return;
		}

		// Reversing direction
		if (reverseCooldown == 0 && pushingBackwards(player, grindDir)) {
			grindDir = FlowmotionRailBlock.other(shape, grindDir);
			takePath(shape);
			reverseCooldown = REVERSE_DELAY;
		}

		advanceGrind(player);
	}

	private void tryStartGrind(LocalPlayer player) {
		if (grindCooldown > 0) {
			return;
		}

		PlayerData playerData = PlayerData.get(player);
		if (playerData == null || !playerData.isAbilityEquipped(ModAbilities.WALL_KICK)) {
			return;
		}

		// Either passing through a floating rail or standing on one lying on the floor
		BlockPos rail = player.blockPosition();
		RailShape shape = FlowmotionRailBlock.shapeAt(player.level(), rail);

		if (shape == null) {
			rail = rail.below();
			shape = FlowmotionRailBlock.shapeAt(player.level(), rail);
		}

		if (shape == null) {
			return;
		}

		// Set off towards whichever end of the piece the player was already heading for
		Vec3 heading = player.getDeltaMovement().horizontalDistanceSqr() > 0.01D ? player.getDeltaMovement() : player.getLookAngle();
		Direction[] ends = FlowmotionRailBlock.connections(shape);

		grindRail = rail;
		grindDir = along(heading, ends[0]) >= along(heading, ends[1]) ? ends[0] : ends[1];
		takePath(shape);

		player.level().playSound(player, player.blockPosition(), ModSounds.wall_grab.get(), SoundSource.PLAYERS, 1F, 1.4F);
		PacketHandler.sendToServer(new CSSetFlowmotionPacket(true));
	}

	private static double along(Vec3 heading, Direction direction) {
		return heading.x * direction.getStepX() + heading.z * direction.getStepZ();
	}

	private static boolean pushingBackwards(LocalPlayer player, Direction travelling) {
		Input input = player.input;
		Vec3 newDirection = Vec3.directionFromRotation(0, player.getYRot()).scale(input.forwardImpulse).add(Vec3.directionFromRotation(0, player.getYRot() - 90).scale(input.leftImpulse));

		if (newDirection.lengthSqr() < 1.0E-4D) {
			return false;
		}

		return newDirection.normalize().dot(Vec3.atLowerCornerOf(travelling.getNormal())) < -0.5D;
	}

	private void advanceGrind(LocalPlayer player) {
		double remaining = GRIND_SPEED;
		Vec3 at = player.position();

		for (int step = 0; step < MAX_STEPS_PER_TICK && remaining > 0; step++) {
			// Reached the end of this piece's line, so hand over to the next one
			if (grindPath == null || grindStep >= grindPath.length) {
				BlockPos next = FlowmotionRailBlock.next(player.level(), grindRail, grindDir);
				RailShape shape = next == null ? null : FlowmotionRailBlock.shapeAt(player.level(), next);
				Direction heading = shape == null ? null : FlowmotionRailBlock.travel(shape, grindDir);

				// Ran out of track, thrown off the end
				if (heading == null) {
					player.setPos(at.x, at.y, at.z);
					stopGrind(player, true);
					return;
				}

				grindRail = next;
				grindDir = heading;
				takePath(shape);
				continue;
			}

			Vec3 target = grindPath[grindStep];
			double distance = at.distanceTo(target);

			// Still short of the next point: slide towards it and stop for this tick
			if (distance > remaining) {
				at = at.add(target.subtract(at).normalize().scale(remaining));
				break;
			}

			at = target;
			remaining -= distance;
			grindStep++;
		}

		player.setPos(at.x, at.y, at.z);
		player.setDeltaMovement(Vec3.ZERO);
		player.fallDistance = 0;
	}

	private void takePath(RailShape shape) {
		grindPath = FlowmotionRailBlock.path(grindRail, shape, grindDir);
		grindStep = 0;
	}

	private void stopGrind(LocalPlayer player, boolean launch) {
		if (launch && grindDir != null) {
			player.setDeltaMovement(grindDir.getStepX() * GRIND_SPEED, HOP_OFF, grindDir.getStepZ() * GRIND_SPEED);
			player.fallDistance = 0;
		}

		grindRail = null;
		grindDir = null;
		grindPath = null;
		grindStep = 0;
		grindCooldown = RELATCH_DELAY;
	}

	private static TutorialToast moogleToast;

	private void tickMoogleHint(Minecraft mc) {
		if (mc.player == null) {
			return;
		}

		PlayerData playerData = PlayerData.get(mc.player);

		// Only between making the choice and meeting a Moogle
		boolean wanted = playerData != null && playerData.getChosen() != SoAState.NONE && !playerData.hasMetMoogle();

		if (!wanted) {
			if (moogleToast != null) {
				moogleToast.hide();
				moogleToast = null;
			}
			return;
		}

		if (moogleToast == null) {
			moogleToast = new TutorialToast(TutorialToast.Icons.SOCIAL_INTERACTIONS, Component.translatable("advancements.kingdomkeys.visit_moogle"), Component.translatable("advancements.kingdomkeys.visit_moogle.desc"), false);
			mc.getToasts().addToast(moogleToast);
		}
	}

	// Called the moment the menu key does its job, so the hint leaves as soon as it has been obeyed
	public static void hidePressMHint() {
		if (pressMToast != null) {
			pressMToast.hide();
			pressMToast = null;
		}

		pressMDismissed = true;
	}

	public static float ballRot = 0;
	public static float prevBallRot = 0;

	public static float visualMP = 0;
	public static float prevVisualMP = 0;

	private static boolean isLockedInStruggle(Player player) {
		WorldData clientData = WorldData.getClient();
		if (clientData == null)
			return false;
		for (Struggle struggle : clientData.getStruggles()) {
			if (struggle.isInProgress() && struggle.getActiveCombatantIds().contains(player.getUUID())) {
				return true;
			}
		}
		return false;
	}

	@SubscribeEvent
	public void onMouseScroll(InputEvent.MouseScrollingEvent event) {
		Player player = Minecraft.getInstance().player;
		if (player != null && isLockedInStruggle(player)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void clientTickPost(ClientTickEvent.Post event) {
		if (Minecraft.getInstance().level != null) {
			if (KeyboardHelper.isScrollActivatorDown()) {
				Minecraft.getInstance().player.getInventory().selected = selectedSlot;
			}

			Player localPlayer = Minecraft.getInstance().player;
			if (localPlayer != null && isLockedInStruggle(localPlayer)) {
				StruggleHandler.WeaponSlot weaponSlot = StruggleHandler.findAnyWeaponSlot(localPlayer.getInventory());
				if (weaponSlot != null) {
					localPlayer.getInventory().selected = weaponSlot.slot();
				}
			}
		}

		prevBallRot = ballRot;
		ballRot = (ballRot + 5F) % 360f;

		if (ballRot >= 360F)
			ballRot -= 360F;

		if(Minecraft.getInstance().player == null)
			return;
		PlayerData playerData = PlayerData.get(Minecraft.getInstance().player);
		if(playerData == null)
			return;

		float targetMP = (float) playerData.getMP();

		prevVisualMP = visualMP;

		if (targetMP < 1) {
			visualMP = targetMP;
			prevVisualMP = visualMP;
		} else {
			visualMP += (targetMP - visualMP) * 0.2F;
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
	private LivingEntity lockedAirStepEntity = null;

	@SubscribeEvent
	public void PlayerTick(PlayerTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		Player player = event.getEntity();
		if (player == mc.player && player.getControlledVehicle() == null && cooldownTicks <= 0) { // Only run this for the local client player
			focusing = mc.options.keyPickItem.isDown() && player.getMainHandItem() != null && Utils.getPlayerShotlock(mc.player) != null && (player.getMainHandItem().getItem() instanceof KeybladeItem || player.getMainHandItem().getItem() instanceof IOrgWeapon);
			PlayerData playerData = PlayerData.get(player);
			if(playerData == null)
				return;

			Shotlock shotlock = Utils.getPlayerShotlock(mc.player);
			if (focusing) {
				if(focusGaugeTemp <= 0){ //Clear temp shotlock icon if time has run out
					tempShotlockEntity = null;
				}
				if (focusingTicks == 0) {
					// Has started focusing
					focusGaugeTemp = playerData.getFocus();
					//playerData.setShotlockEnemies();
					PacketHandler.sendToServer(new CSSetShotlockEnemyListPacket(new ArrayList<>()));
					player.level().playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.shotlock_lockon_start.get(), SoundSource.PLAYERS, 1F, 1F);
				}

				if(focusingTicks == 5) {
					player.level().playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.shotlock_lockon_idle.get(), SoundSource.PLAYERS, 1F, 1F);
				}
				focusingTicks++;

				if(focusGaugeTemp > 0)
					focusGaugeTemp-=0.8;

				HitResult rt = InputHandler.getMouseOverExtended(ModConfigs.SERVER.shotlockMaxDist.get());
				if (rt == null) {
					lockedAirStep = BlockPos.ZERO;
					lockedAirStepEntity = null;
					return;
				}

				if (rt instanceof BlockHitResult blockResult) { //Airstep
					tempShotlockEntity = null;
					if (player.level().getBlockState(blockResult.getBlockPos()) == ModBlocks.airstepTarget.get().defaultBlockState()) {
						if (!lockedAirStep.equals(blockResult.getBlockPos())) {
							player.level().playSound(player, player.position().x(), player.position().y(), player.position().z(), ModSounds.shotlock_lockon.get(), SoundSource.PLAYERS, 1F, 0.5F);
						}
						lockedAirStep = blockResult.getBlockPos();

						// On right click
						if (mc.options.keyUse.isDown()) {
							PacketHandler.sendToServer(new CSSetAirStepPacket(blockResult.getBlockPos(), 0));
							lockedAirStep = new BlockPos(0, 0, 0);
							lockedAirStepEntity = null;
							cooldownTicks = 20;
							focusingAnEntityTicks = 0;
							focusingTicks = 0;
							focusing = false;
							tempShotlockEntity = null;
							focusGaugeTemp = playerData.getFocus();
							return;
						}
					}
				}

				float costDivider = 3;
				//If looking at an entity
				if (rt instanceof EntityHitResult ertr && focusGaugeTemp > 0 && cooldownTicks <= 0) {
					//Airstep to entity
					if (ertr.getEntity() instanceof LivingEntity target) {
						float distance = mc.player.distanceTo(target);
						if(playerData.isAbilityEquipped(ModAbilities.FLOWSTEP) && distance / costDivider <= playerData.getFocus()) { //Only able to target enemies that are as far as focus can take you to
							if (lockedAirStepEntity != target) {
								player.level().playSound(player, player.position().x(), player.position().y(), player.position().z(), ModSounds.shotlock_lockon.get(), SoundSource.PLAYERS, 1F, 0.6F);
							}
							lockedAirStepEntity = target;
							lockedAirStep = BlockPos.ZERO;

							if (mc.options.keyUse.isDown()) {
								float focusCost = mc.player.distanceTo(target) / costDivider;
								PacketHandler.sendToServer(new CSSetAirStepPacket(target.blockPosition(), focusCost));

								lockedAirStepEntity = null;
								cooldownTicks = 40;
								focusingAnEntityTicks = 0;
								focusingTicks = 0;
								focusing = false;
								tempShotlockEntity = null;
								focusGaugeTemp = playerData.getFocus();
								return;
							}
						}
					}

					//Ultimate shotlock
					if(shotlock.getMaxLocks() == 1 && playerData.getShotlockEnemies().size() < shotlock.getMaxLocks()){
						if (ertr.getEntity() instanceof LivingEntity target) {
							if(target != tempShotlockEntity){
								focusingAnEntityTicks = 0;
								player.level().playSound(player, player.position().x(), player.position().y(), player.position().z(), ModSounds.shotlock_lockon_idle.get(), SoundSource.PLAYERS, 1F, 1F);
								player.level().playSound(player, player.position().x(), player.position().y(), player.position().z(), ModSounds.shotlock_lockon_start.get(), SoundSource.PLAYERS, 1F, 1F);
							}
							tempShotlockEntity = target;
							Party p = WorldData.getClient().getPartyFromMember(player.getUUID());
							if (p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) { // If caster is not in a party || the party doesn't have the target in it || the party has FF on
								if(focusingAnEntityTicks >= shotlock.getRealCooldown(player)) {
									float halfWidth = target.getBbWidth() * 0.5F;
									float height = target.getBbHeight();
									float ox = Mth.nextFloat(player.getRandom(), -halfWidth, halfWidth);
									float oy = Mth.nextFloat(player.getRandom(), 0.0F, height);
									float oz = Mth.nextFloat(player.getRandom(), -halfWidth, halfWidth);

									playerData.addShotlockEnemy(new Utils.ShotlockPosition(target.getId(), ox, oy, oz));
									player.level().playSound(player, player.position().x(), player.position().y(), player.position().z(), ModSounds.shotlock_lockon_all.get(), SoundSource.PLAYERS, 1F, 1F);
									cost = playerData.getFocus() - focusGaugeTemp;
									tempShotlockEntity = null;
								}
								focusingAnEntityTicks++;
							}
						}
						// Locking on
					} else if (focusingTicks % shotlock.getRealCooldown(player) == 0 && playerData.getShotlockEnemies().size() < shotlock.getMaxLocks()) {
						Party p = WorldData.getClient().getPartyFromMember(player.getUUID());
						if (ertr.getEntity() instanceof LivingEntity target) {
							if (p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) { // If caster is not in a party || the party doesn't have the target in it || the party has FF on
								float halfWidth = target.getBbWidth() * 0.5F;
								float height = target.getBbHeight();
								float ox = Mth.nextFloat(player.getRandom(), -halfWidth, halfWidth);
								float oy = Mth.nextFloat(player.getRandom(), 0.0F, height);
								float oz = Mth.nextFloat(player.getRandom(), -halfWidth, halfWidth);

								playerData.addShotlockEnemy(new Utils.ShotlockPosition(target.getId(), ox, oy, oz));
								PacketHandler.sendToServer(new CSSetShotlockEnemyListPacket(playerData.getShotlockEnemies()));
								player.level().playSound(player, player.position().x(), player.position().y(), player.position().z(), ModSounds.shotlock_lockon.get(), SoundSource.PLAYERS, 1F, 1F);
								cost = playerData.getFocus() - focusGaugeTemp;
								tempShotlockEntity = null;

								if (playerData.getShotlockEnemies().size() >= shotlock.getMaxLocks()) {
									player.level().playSound(player, player.position().x(), player.position().y(), player.position().z(), ModSounds.shotlock_lockon_all.get(), SoundSource.PLAYERS, 1F, 1F);
								}
							}
						}
					}
				}

				if(EpicFightUtils.isAttacking()) {
					if (focusingTicks > 0) {
						// Has finished shotlocking, send packet to spawn entities and track enemies
						if(!playerData.getShotlockEnemies().isEmpty()) {
							playerData.remFocus(cost);
							player.level().playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.shotlock_shot.get(), SoundSource.PLAYERS, 1F, 1F);
							PacketHandler.sendToServer(new CSShotlockShot(playerData.getShotlockEnemies(), cost));
							cooldownTicks = 100;
							focusing = false;
						}
					}
					focusingTicks = 0;
					focusingAnEntityTicks = 0;
					tempShotlockEntity = null;
					lockedAirStepEntity = null;
					focusGaugeTemp = playerData.getFocus();
					playerData.setShotlockEnemies(new ArrayList<>());
				}
			} else { //No longer focusing (released wheel button)
				lockedAirStep = new BlockPos(0,0,0);
				lockedAirStepEntity = null;
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
		BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferbuilder.addVertex(matrix, (float) x1, (float) y2, (float) blitOffset).setUv(minU, maxV);
		bufferbuilder.addVertex(matrix, (float) x2, (float) y2, (float) blitOffset).setUv(maxU, maxV);
		bufferbuilder.addVertex(matrix, (float) x2, (float) y1, (float) blitOffset).setUv(maxU, minV);
		bufferbuilder.addVertex(matrix, (float) x1, (float) y1, (float) blitOffset).setUv(minU, minV);
		RenderSystem.enableBlend();
		BufferUploader.drawWithShader(bufferbuilder.build());
	}

	/**
	 * Stops the player from moving except if it's a sonic blade-like attack.
	 */
	@SubscribeEvent
	public void onMovementInput(MovementInputUpdateEvent event) {
		if (!ShotlockMinigameClient.movementLocked) {
			return;
		}

		Input input = event.getInput();
		input.forwardImpulse = 0F;
		input.leftImpulse = 0F;
		input.up = false;
		input.down = false;
		input.left = false;
		input.right = false;
		input.jumping = false;
		input.shiftKeyDown = false;
	}

	// Stop the clicking from going further if it's a minigame.
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void PlayerClick(InputEvent.InteractionKeyMappingTriggered event) {
		if (event.isAttack() && ShotlockMinigameClient.active) {
			event.setSwingHand(false);
			event.setCanceled(true);
			return;
		}

		if(event.isPickBlock()) {
			Minecraft mc = Minecraft.getInstance();
			if(mc.player.getMainHandItem() != null && Utils.getPlayerShotlock(mc.player) != null && (mc.player.getMainHandItem().getItem() instanceof KeybladeItem || mc.player.getMainHandItem().getItem() instanceof IOrgWeapon)){
				event.setCanceled(true);
			}
			if(mc.player.getControlledVehicle() != null && mc.player.getControlledVehicle() instanceof GummiShipEntity){
				event.setCanceled(true);
			}
		}
	}

	@EventBusSubscriber(value = Dist.CLIENT)
	public static class ModBusEvents {
		@SubscribeEvent
		public static void colourTint(RegisterColorHandlersEvent.Block event) {
			event.register(ModBusEvents::getStructureWallColour, ModBlocks.structureWall.get());
			event.register(ModBusEvents::getGummiBlockColour, ModBlocks.gummiBlocks.get().stream().map(Supplier::get).toList().toArray(new Block[0]));
			event.register(ModBusEvents::getGummiBlockColour, ModBlocks.gummiBubbleHelms.stream().map(Supplier::get).toList().toArray(new Block[0]));
			event.register(ModBusEvents::getGummiBlockColour, ModBlocks.gummiMiniHelms.stream().map(Supplier::get).toList().toArray(new Block[0]));
			event.register(ModBusEvents::getGummiBlockColour, ModBlocks.gummiAeroTriangles.stream().map(Supplier::get).toList().toArray(new Block[0]));
			event.register(ModBusEvents::getGummiBlockColour, ModBlocks.gummiAeroSquares.stream().map(Supplier::get).toList().toArray(new Block[0]));
		}

		public static int getStructureWallColour(BlockState state, BlockAndTintGetter blockAndTintGetter, BlockPos pos, int tintIndex) {
			Color colour = Color.BLACK;
			ClientLevel level = Minecraft.getInstance().level;
			Player player = Minecraft.getInstance().player;
			if (level != null && player != null) {
				if (CastleOblivionHandler.inInterior(player)) {
					colour = CastleOblivionData.InteriorData.getClient(level).map(interiorData -> {
						if (!interiorData.getFloors().isEmpty()) {
							Room room = interiorData.getRoomAtPos(pos);
							if (room != null) {
								if (room.getType().getColour() != null) {
									return room.getType().getColour();
								} else {
									Floor floor = room.getParent(interiorData);
									if (floor != null) {
										int biomeColour = floor.getType().useFogColour() ? floor.getType().getFloorColour().value().getFogColor() : floor.getType().getFloorColour().value().getSkyColor();
										return new Color(biomeColour);
									}
								}
							}
						}
						return Color.BLACK;
					}).orElse(Color.BLACK);
				}
			}
			return colour.getRGB();
		}

		public static int getGummiBlockColour(ItemStack stack, int tintIndex) {
			if (stack.getItem() instanceof BlockItem blockItem) {
				return getGummiBlockColour(blockItem.getBlock().defaultBlockState(), null, null, tintIndex);
			} else {
				return Color.BLACK.getRGB();
			}
		}

		public static int getGummiBlockColour(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex) {
			if (tintIndex == 0) {
				int colour = Color.RED.getRGB();
				if (state.getBlock() instanceof GummiBlockBase gummi) {
					colour = gummi.getColor().getTextureDiffuseColor();
				}
				return colour;
			} else {
				return Color.white.getRGB();
			}
		}

		@SubscribeEvent
		public static void itemColour(RegisterColorHandlersEvent.Item event) {
			event.register((pStack, pTintIndex) -> {
				int itemColor = ((WayfinderItem)pStack.getItem()).getColor(pStack);
				Color colour = new Color(itemColor);
				return colour.getRGB();
			}, ModItems.wayfinder.get());
			event.register(ModBusEvents::getGummiBlockColour, ModBlocks.gummiBlocks.get().stream().map(Supplier::get).toList().toArray(new Block[0]));
			event.register(ModBusEvents::getGummiBlockColour, ModBlocks.gummiBubbleHelms.stream().map(Supplier::get).toList().toArray(new Block[0]));
			event.register(ModBusEvents::getGummiBlockColour, ModBlocks.gummiMiniHelms.stream().map(Supplier::get).toList().toArray(new Block[0]));
			event.register(ModBusEvents::getGummiBlockColour, ModBlocks.gummiAeroTriangles.stream().map(Supplier::get).toList().toArray(new Block[0]));
			event.register(ModBusEvents::getGummiBlockColour, ModBlocks.gummiAeroSquares.stream().map(Supplier::get).toList().toArray(new Block[0]));
		}
	}

	@SubscribeEvent
	public void closeScreen(ScreenEvent.Closing event) {
		if (event.getScreen() instanceof StopGui) {
			GLFW.glfwSetInputMode(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
		}
	}


	@SubscribeEvent
	public void debugInfo(CustomizeGuiOverlayEvent.DebugText event) {
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		ClientLevel level = mc.level;

		if (CastleOblivionHandler.isInterior(level.dimension())) {
			CastleOblivionData.InteriorData.getClient(level).ifPresent(interiorData -> {
				event.getLeft().add("");
				event.getLeft().add(ChatFormatting.UNDERLINE + "Castle Oblivion Info");
				Room room = interiorData.getRoomAtPos(player.blockPosition());
				if (room == null) {
					event.getLeft().add("Floor: N/A, Room: N/A");
				} else {
					Floor floor = interiorData.getFloorByID(room.parentFloor);
					RoomData data = room.getRoomData(interiorData);
					event.getLeft().add("Floor: " + room.parentFloor + ", Room: " + room + " " + data.pos);
					if (!room.getType().getModifiers().isEmpty()) {
						StringBuilder modifiers = new StringBuilder("Modifiers [");
						for (RoomModifier modifier : room.getType().getModifiers()) {
							modifiers.append(ModRoomModifiers.registry.getKey(modifier.type()));
							modifiers.append(", ");
						}
						event.getLeft().add(modifiers.substring(0, modifiers.length()-2) + "]");
					}
					if (room.getEncounter().isPresent()) {
						event.getLeft().add("Encounter: " + room.getEncounter().get().getEncounter().getRegistryName() + " Complete: " + room.getEncounter().get().isComplete());
					}
				}
			});
		}
	}

}