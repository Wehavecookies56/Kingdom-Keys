package online.kingdomkeys.kingdomkeys.handler;


import com.mojang.datafixers.util.Pair;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.common.NeoForge;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.event.client.KKInputEvent;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.CommandMenuGui;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.entity.mob.SpawningOrbEntity;
import online.kingdomkeys.kingdomkeys.integration.epicfight.EpicFightUtils;
import online.kingdomkeys.kingdomkeys.integration.epicfight.init.KKAnimations;
import online.kingdomkeys.kingdomkeys.lib.Constants;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.limit.Limit;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.*;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.util.IExtendedReach;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import javax.crypto.spec.PSource;
import java.util.*;

public class InputHandler {

    @Nullable public List<UUID> portalCommands;
    @Nullable public List<Member> targetsList;
    @Nullable public List<Limit> limitsList;
    @Nullable public List<String> magicList;
    @Nullable public Map<Integer, ItemStack> itemsList;
    @Nullable public List<String> reactionList;
    
    @Nullable public static LivingEntity lockOn = null;
    public static int qrCooldown = 40;

    public Minecraft mc;
    public LocalPlayer player;
    @Nullable public ClientLevel level;
    public PlayerData playerData;
    public GlobalData globalData;
    @Nullable public WorldData worldData;
    public InputHandler() {
        mc = Minecraft.getInstance();
    }

    private void init() {
        player = mc.player;
        level = mc.level;
        if (level != null) {
            worldData = WorldData.getClient();
        }
        if (player != null) {
            playerData = PlayerData.get(player);
            globalData = GlobalData.get(player);
        }
    }

    boolean canSwitchTarget = true;

    @SubscribeEvent
    public void handleKeyInputEvent(InputEvent.Key event) {
        init();

        Keybinds key = getPressedKey();
        if (player != null) {
            if(playerData == null)
                return;

            if (key != null) {
                if (!NeoForge.EVENT_BUS.post(new KKInputEvent.Pre(key, this)).isCanceled()) {
                    switch (key) {
                        case OPENMENU -> openMenu();
                        case SHOW_GUI -> showGui();
                        case SCROLL_UP -> {
                            if (mc.screen == null && Utils.shouldRenderOverlay(player))
                                commandUp();
                        }
                        case SCROLL_DOWN -> {
                            if (mc.screen == null && Utils.shouldRenderOverlay(player))
                                commandDown();
                        }
                        case ENTER -> {
                            if (mc.screen == null && Utils.shouldRenderOverlay(player))
                                commandEnter();
                        }
                        case BACK -> {
                            if (mc.screen == null && Utils.shouldRenderOverlay(player))
                                commandBack();
                        }
                        case SUMMON_KEYBLADE -> summonKeyblade();
                        case SUMMON_ARMOR -> summonArmor();
                        case ACTION -> commandAction();
                        case LOCK_ON -> lockOn();
                        case LOCK_ON_SWAP -> lockOnSwap();
                        case REACTION_COMMAND -> reactionCommand();
                    }
                    NeoForge.EVENT_BUS.post(new KKInputEvent.Post(key, this));
                }
            } else {
                otherKeyPressed(event);
            }
        }
    }

    @SubscribeEvent
    public void handleMouseInputEvent(InputEvent.MouseButton.Pre event) {
        init();
        if(level != null){
            if (event.getButton() == Constants.LEFT_MOUSE && event.getAction() == 1) {
                if(KeyboardHelper.isScrollActivatorDown() && Utils.shouldRenderOverlay(player)) {
                    commandEnter();
                    event.setCanceled(true);
                } else if(mc.screen == null){
                    if (player != null) {
                        ItemStack itemstack = player.getMainHandItem();
                        if (!ItemStack.matches(itemstack, ItemStack.EMPTY)) {
                            IExtendedReach ieri = itemstack.getItem() instanceof IExtendedReach ? (IExtendedReach) itemstack.getItem() : null;
                            if (ieri != null) {
                                float reach = ieri.getReach();
                                HitResult rtr = getMouseOverExtended(reach);
                                if (rtr != null) {
                                    if (rtr instanceof EntityHitResult ertr) {
                                        if (ertr.getEntity() != null && ertr.getEntity().invulnerableTime == 0) {
                                            if (ertr.getEntity() != player) {
                                                if(!ertr.getEntity().getPassengers().contains(player)) {
                                                    PacketHandler.sendToServer(new CSExtendedReach(ertr.getEntity().getId()));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (event.getButton() == Constants.MIDDLE_MOUSE && KeyboardHelper.isScrollActivatorDown() && event.getAction() == 1 && Utils.shouldRenderOverlay(player)) {
                commandSwapReaction();
                event.setCanceled(true);
            }

            if (event.getButton() == Constants.RIGHT_MOUSE && KeyboardHelper.isScrollActivatorDown() && event.getAction() == 1&& Utils.shouldRenderOverlay(player)) {
                commandBack();
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void OnMouseWheelScroll(InputEvent.MouseScrollingEvent event) {
        init();
        if (mc.isWindowActive() && KeyboardHelper.isScrollActivatorDown()) {
            event.setCanceled(true);
            if(!Utils.shouldRenderOverlay(player))
                return;
            if(event.getScrollDeltaY() == Constants.WHEEL_DOWN) {
                commandDown();
            }else if(event.getScrollDeltaY() == Constants.WHEEL_UP) {
                commandUp();
            }
        }
    }

    public void showGui() {
        ModConfigs.toggleGui();
        player.displayClientMessage(Component.translatable("message.kingdomkeys.gui_toggle", ModConfigs.showGuiToggle.toString()), true);
    }

    public void openMenu() {
        PacketHandler.sendToServer(new CSOpenMenu());
    }

    public void summonKeyblade() {
        if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
            if(KingdomKeys.efmLoaded) {
                if(Utils.findSummoned(player.getInventory(), playerData.getEquippedKeychain(DriveForm.NONE)) == -1 && playerData.getAlignment() == OrgMember.NONE) {
                    if (!playerData.isAbilityEquipped(Strings.synchBlade)) {
                        PacketHandler.sendToServer(new CSPlayAnimation(KKAnimations.singleKeybladeMap.get(playerData.getSingleStyle())));
                    } else {
                        PacketHandler.sendToServer(new CSPlayAnimation(KKAnimations.dualKeybladeMap.get(playerData.getDualStyle())));
                    }
                } else if(Utils.findSummoned(player.getInventory(), playerData.getEquippedWeapon()) == -1 && playerData.getAlignment() != OrgMember.NONE) {
                    PacketHandler.sendToServer(new CSPlayAnimation(KKAnimations.orgMap.get(playerData.getAlignment())));
                } else {
                    PacketHandler.sendToServer(new CSSummonKeyblade()); // desummon
                }
            }
            else {
                PacketHandler.sendToServer(new CSSummonKeyblade());
            }
        } else {

            if(KingdomKeys.efmLoaded && Utils.findSummoned(player.getInventory(), playerData.getEquippedKeychain(DriveForm.NONE)) == -1) {
                PacketHandler.sendToServer(new CSPlayAnimation(KKAnimations.DRIVE_SUMMON));
            } else {
                PacketHandler.sendToServer(new CSSummonKeyblade(ResourceLocation.parse(playerData.getActiveDriveForm())));
            }
        }

        if(ModConfigs.summonTogether)
            PacketHandler.sendToServer(new CSSummonArmor());
    }

    public void summonArmor() {
        PacketHandler.sendToServer(new CSSummonArmor());
    }

    public static int LOCK_ON_REACH = 35;

    public void lockOn() {
        if (lockOn == null) {
            HitResult rtr = getMouseOverExtended(LOCK_ON_REACH);
            if (rtr instanceof EntityHitResult ertr) {
                double distance = player.distanceTo(ertr.getEntity());

                if (LOCK_ON_REACH >= distance) {
                    if (ertr.getEntity() instanceof LivingEntity && !(ertr.getEntity() instanceof SpawningOrbEntity)) {
                        lockOn = (LivingEntity) ertr.getEntity();
                        playSound(ModSounds.lockon.get());
                    }
                }
            }
        } else {
            lockOn = null;
        }
    }

    public void lockOnSwap() {
        if(canSwitchTarget){
            switchTarget(player.isCrouching());
        }
        canSwitchTarget = !canSwitchTarget;

        switchTarget(player.isCrouching());
    }

    private void switchTarget(boolean toRight) {
        Player player = Minecraft.getInstance().player;
        if (player == null || InputHandler.lockOn == null) return;

        LivingEntity currentTarget = InputHandler.lockOn;
        //Get all entities in a radius (25% of the lock on reach)
        List<LivingEntity> candidates = player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(LOCK_ON_REACH / 4F), e -> e != player && !e.isDeadOrDying() && e.isAlive());

        if (candidates.size() <= 1)
            return;

        Vec3 playerPos = player.position();
        Vec3 lookVec = player.getLookAngle();

        // Sort enemies by angle from the player POV
        List<Pair<LivingEntity, Double>> sorted = candidates.stream()
                .map(entity -> {
                    Vec3 dirToEntity = entity.position().subtract(playerPos).normalize();
                    double angle = Math.toDegrees(Math.atan2(
                            lookVec.z * dirToEntity.x - lookVec.x * dirToEntity.z,
                            lookVec.x * dirToEntity.x + lookVec.z * dirToEntity.z
                    ));
                    return Pair.of(entity, angle);
                })
                .sorted(Comparator.comparingDouble(Pair::getSecond))
                .toList();

        int index = -1;
        for (int i = 0; i < sorted.size(); i++) {
            if (sorted.get(i).getFirst().equals(currentTarget)) {
                index = i;
                break;
            }
        }

        // Fallback to the first enemy
        if (index == -1) {
            index = 0;
        }

        int nextIndex = (index + (toRight ? 1 : -1) + sorted.size()) % sorted.size();
        LivingEntity nextTarget = sorted.get(nextIndex).getFirst();

        // Prevent switching if entity is the same
        if (nextTarget.equals(currentTarget))
            return;

        InputHandler.lockOn = nextTarget;
        playSound(ModSounds.lockon.get());
    }

    public void commandUp() {
        CommandMenuGui.up();
    }

    public void commandDown() {
        CommandMenuGui.down();
    }

    public void commandEnter() {
        CommandMenuGui.enter();
    }

	public void commandBack() {
        CommandMenuGui.cancel();
    }

	public void commandAction() {
    	if (qrCooldown <= 0 && (player.getDeltaMovement().x != 0 && player.getDeltaMovement().z != 0)) { // If player is moving do dodge roll / quick run
			if (player.isSprinting()) { //If player is sprinting do quick run
				if (playerData.isAbilityEquipped(Strings.quickRun) || playerData.getActiveDriveForm().equals(Strings.Form_Wisdom)) {
					quickRun();
				}
			} else { //If player is moving without sprinting do dodge roll
				if (playerData.isAbilityEquipped(Strings.dodgeRoll) || playerData.getActiveDriveForm().equals(Strings.Form_Limit)) {
					dodgeRoll();
				}
			}
		} else { // If player is not moving do guard
			/*if (ABILITIES.getEquippedAbility(ModAbilities.guard)) {
				if (player.getHeldItemMainhand() != null) {
					// If the player holds a weapon
					if (player.getHeldItemMainhand().getItem() instanceof ItemKeyblade || player.getHeldItemMainhand().getItem() instanceof IOrgWeapon) {
						PacketDispatcher.sendToServer(new InvinciblePacket(20));
					}
				}
			}*/
		}
    }

    public void quickRun() {
        float yaw = player.getYRot();
        float motionX = -Mth.sin(yaw / 180.0f * (float) Math.PI);
        float motionZ = Mth.cos(yaw / 180.0f * (float) Math.PI);

        int wisdomLevel = playerData.getDriveFormLevel(Strings.Form_Wisdom);

        double power = 0;
        DriveForm form = ModDriveForms.registry.get(ResourceLocation.parse(playerData.getActiveDriveForm()));

        // Wisdom Form
        if (playerData.getActiveDriveForm().equals(Strings.Form_Wisdom)) {
            power = Constants.WISDOM_QR[wisdomLevel];
        } else if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) || form.getBaseGrowthAbilities()) { //Base
            if (wisdomLevel > 2) {
                power = Constants.WISDOM_QR[wisdomLevel - 2];
            }
        }

        if (player.onGround()) {
            player.push(motionX * power, 0, motionZ * power);
            qrCooldown = 20;
        }
    }

    public void dodgeRoll() {
        int limitLevel = playerData.getDriveFormLevel(Strings.Form_Limit);
        double power = 0;
        DriveForm form = ModDriveForms.registry.get(ResourceLocation.parse(playerData.getActiveDriveForm()));

        if (playerData.getActiveDriveForm().equals(Strings.Form_Limit)) {
            power = Constants.LIMIT_DR[limitLevel];
        } else if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) || form.getBaseGrowthAbilities()) { //Base
            if (limitLevel > 2) {
                power = Constants.LIMIT_DR[limitLevel - 2];
            }
        }

        if (player.onGround()) {
            player.push(player.getDeltaMovement().x * power, 0, player.getDeltaMovement().z * power);
            qrCooldown = 20;
            //PacketDispatcher.sendToServer(new InvinciblePacket(20));
        }
    }

	public void commandSwapReaction() {
		loadLists();
		if (this.reactionList != null && !this.reactionList.isEmpty()) {
			if (CommandMenuGui.reactionSelected < this.reactionList.size() - 1) {
				CommandMenuGui.reactionSelected++;
			} else {
				if (CommandMenuGui.reactionSelected >= this.reactionList.size() - 1)
					CommandMenuGui.reactionSelected = 0;
			}
		}
	}
    
    public void reactionCommand() {
    	loadLists();
    	if(!reactionList.isEmpty()) {
			PacketHandler.sendToServer(new CSUseReactionCommandPacket(CommandMenuGui.reactionSelected, InputHandler.lockOn));
            String reactionName = playerData.getReactionCommands().get(CommandMenuGui.reactionSelected);
            ReactionCommand reaction = ModReactionCommands.registry.get(ResourceLocation.parse(reactionName));
            CommandMenuGui.reactionSelected = 0;
            if (reaction != null) {
                playSound(reaction.getUseSound(player, InputHandler.lockOn));
            } else {
                playInSound();
            }
		}
	}

    public void otherKeyPressed(InputEvent.Key event) {
        DriveForm form = ModDriveForms.registry.get(ResourceLocation.parse(playerData.getActiveDriveForm()));

        if (KeyboardHelper.isScrollActivatorDown() && event.getKey() > 320 && event.getKey() < 330) {
            if (globalData != null && !player.hasEffect(ModMobEffects.STOP)) {
                if (playerData.getMagicCasttimeTicks() <= 0 && playerData.getMagicCooldownTicks() <= 0 && !playerData.getRecharge() && form.canUseMagic() && !player.hasEffect(ModMobEffects.KO)) {
                    PacketHandler.sendToServer(new CSUseShortcutPacket(event.getKey() - 321, InputHandler.lockOn));
                }
            }
        }

        if (KeyboardHelper.isScrollActivatorDown() && event.getKey() > 48 && event.getKey() < 58) {
            if (globalData != null && !player.hasEffect(ModMobEffects.STOP)) {
                if (playerData.getMagicCasttimeTicks() <= 0 && playerData.getMagicCooldownTicks() <= 0 && !playerData.getRecharge() && form.canUseMagic() && !player.hasEffect(ModMobEffects.KO)) {
                    PacketHandler.sendToServer(new CSUseShortcutPacket(event.getKey() - 49, InputHandler.lockOn));
                }
            }
        }
    }

    public void loadLists() {
        if(playerData != null && worldData != null) {
            //this.magicsMap = Utils.getSortedMagics(playerData.getMagicsMap());
            this.portalCommands = worldData.getAllPortalsFromOwnerID(player.getUUID());
            this.magicList = ModConfigs.magicDisplayedInCommandMenu.stream().filter(magic -> playerData.getMagicsMap().containsKey(magic)).toList();
            this.limitsList = Utils.getSortedLimits(Utils.getPlayerLimitAttacks(player));

            if(worldData.getPartyFromMember(player.getUUID()) != null) {
                this.targetsList = worldData.getPartyFromMember(player.getUUID()).getMembers();
            }
            this.itemsList = Utils.getEquippedItems(playerData.getEquippedItems());

            this.reactionList = playerData.getReactionCommands();
        }
    }

    public void playSelectSound() {
        playSound(ModSounds.menu_select.get());
    }

    public void playMoveSound() {
        playSound(ModSounds.menu_move.get());
    }

    public void playBackSound() {
        playSound(ModSounds.menu_back.get());
    }

    public void playInSound() {
        playSound(ModSounds.menu_in.get());
    }

    public void playErrorSound() {
        playSound(ModSounds.error.get());
    }

    public void playSound(SoundEvent sound) {
        level.playSound(player, player.position().x(),player.position().y(),player.position().z(), sound, SoundSource.MASTER, 1.0f, 1.0f);
    }

    public static HitResult getMouseOverExtended(double dist) {
        Minecraft mc = Minecraft.getInstance();
        Entity camera = mc.getCameraEntity();
        if (mc.level == null || camera == null) return null;

        final float coneAngleDeg = 12f;
        final int raySteps = 5; // número de divisiones en el cono
        final boolean checkVisibility = true;

        Vec3 eyePos = camera.getEyePosition(0);
        Vec3 lookVec = camera.getViewVector(0).normalize();
        Level level = mc.level;

        HitResult bestBlockHit = null;
        double bestBlockAngle = coneAngleDeg;

        for (int phiStep = -raySteps; phiStep <= raySteps; phiStep++) {
            for (int thetaStep = -raySteps; thetaStep <= raySteps; thetaStep++) {
                double phi = Math.toRadians(coneAngleDeg) * phiStep / raySteps;
                double theta = Math.toRadians(coneAngleDeg) * thetaStep / raySteps;

                Vec3 dir = rotateVector(lookVec, phi, theta).normalize();
                Vec3 end = eyePos.add(dir.scale(dist));

                HitResult hit = level.clip(new ClipContext(eyePos, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, camera));
                if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
                    BlockPos pos = ((BlockHitResult) hit).getBlockPos();
                    BlockState state = level.getBlockState(pos);

                    if (state.is(ModBlocks.airstepTarget.get())) {
                        double angle = Math.toDegrees(Math.acos(lookVec.dot(dir)));
                        if (angle < bestBlockAngle) {
                            bestBlockHit = hit;
                            bestBlockAngle = angle;
                        }
                    }
                }
            }
        }

        if (bestBlockHit != null) return bestBlockHit;

        HitResult bestEntityHit = null;
        double bestEntityDist = dist;
        double bestEntityAngle = coneAngleDeg;

        for (Entity e : level.getEntities(camera, camera.getBoundingBox().inflate(dist), Entity::isPickable)) {
            Vec3 targetPos = e.position().add(0, e.getBbHeight() * 0.5, 0);
            Vec3 dir = targetPos.subtract(eyePos);
            double d = dir.length();
            if (d > dist) continue;

            Vec3 dirNorm = dir.normalize();
            double angle = Math.toDegrees(Math.acos(lookVec.dot(dirNorm)));
            if (angle > coneAngleDeg) continue;

            if (checkVisibility) {
                HitResult hit = level.clip(new ClipContext(eyePos, targetPos, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, camera));
                if (hit.getType() == HitResult.Type.BLOCK && hit.getLocation().distanceTo(eyePos) + 0.5 < d)
                    continue;
            }

            if (angle < bestEntityAngle || (angle == bestEntityAngle && d < bestEntityDist)) {
                bestEntityHit = new EntityHitResult(e);
                bestEntityAngle = angle;
                bestEntityDist = d;
            }
        }

        return bestEntityHit;
    }

    private static Vec3 rotateVector(Vec3 lookVec, double phi, double theta) {
        Vec3 z = lookVec.normalize();

        Vec3 x = z.cross(new Vec3(0, 1, 0));
        if (x.lengthSqr() < 1e-6) x = z.cross(new Vec3(1, 0, 0));
        x = x.normalize();
        Vec3 y = z.cross(x).normalize();

        Vec3 dir = x.scale(Math.sin(phi) * Math.cos(theta))
                .add(y.scale(Math.sin(phi) * Math.sin(theta)))
                .add(z.scale(Math.cos(phi)));

        return dir.normalize();
    }



    public enum Keybinds {
        OPENMENU("key.kingdomkeys.openmenu", GLFW.GLFW_KEY_M),
        SCROLL_UP("key.kingdomkeys.scrollup",GLFW.GLFW_KEY_UP),
        SCROLL_DOWN("key.kingdomkeys.scrolldown", GLFW.GLFW_KEY_DOWN),
        ENTER("key.kingdomkeys.enter",GLFW.GLFW_KEY_RIGHT),
        BACK("key.kingdomkeys.back", GLFW.GLFW_KEY_LEFT),
        SCROLL_ACTIVATOR("key.kingdomkeys.scrollactivator",GLFW.GLFW_KEY_LEFT_ALT),
        SUMMON_KEYBLADE("key.kingdomkeys.summonkeyblade", GLFW.GLFW_KEY_G),
        LOCK_ON("key.kingdomkeys.lockon",GLFW.GLFW_KEY_Z),
        LOCK_ON_SWAP("key.kingdomkeys.lockonswap",GLFW.GLFW_KEY_C),
        SHOW_GUI("key.kingdomkeys.showgui", GLFW.GLFW_KEY_O),
        ACTION("key.kingdomkeys.action",GLFW.GLFW_KEY_X),
        SUMMON_ARMOR("key.kingdomkeys.summonarmor",GLFW.GLFW_KEY_H),
    	REACTION_COMMAND("key.kingdomkeys.reactioncommand", GLFW.GLFW_KEY_R);

        public final KeyMapping keybinding;
        public final String translationKey;
        Keybinds(String name, int defaultKey) {
            keybinding = new KeyMapping(name, defaultKey, "key.categories.kingdomkeys");
            translationKey = name;
        }

        public KeyMapping getKeybind() {
            return keybinding;
        }

        private boolean isPressed() {
            return keybinding.consumeClick();
        }
    }

    private Keybinds getPressedKey() {
        for (Keybinds key : Keybinds.values())
            if (key.isPressed())
                return key;
        return null;
    }
}
