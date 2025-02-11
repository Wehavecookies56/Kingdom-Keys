package online.kingdomkeys.kingdomkeys.handler;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import org.lwjgl.glfw.GLFW;

import net.minecraft.ChatFormatting;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.*;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputEvent.MouseScrollingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.api.event.client.KKInputEvent;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.menu.NoChoiceMenuPopup;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.CommandMenuGui;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.entity.mob.SpawningOrbEntity;
import online.kingdomkeys.kingdomkeys.integration.epicfight.SeparateClassToAvoidLoadingIssuesExtendedReach;
import online.kingdomkeys.kingdomkeys.integration.epicfight.init.KKAnimations;
import online.kingdomkeys.kingdomkeys.item.KKPotionItem;
import online.kingdomkeys.kingdomkeys.lib.*;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.limit.Limit;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.*;
import online.kingdomkeys.kingdomkeys.util.IExtendedReach;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;

public class InputHandler {

    @Nullable public List<UUID> portalCommands;
    @Nullable public Map<String, int[]> driveFormsMap;
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
    public IPlayerCapabilities playerData;
    public IGlobalCapabilities globalData;
    @Nullable public IWorldCapabilities worldData;
    public InputHandler() {
        mc = Minecraft.getInstance();
    }

    private void init() {
        player = mc.player;
        level = mc.level;
        if (level != null) {
            worldData = ModCapabilities.getWorld(level);
        }
        if (player != null) {
            playerData = ModCapabilities.getPlayer(player);
            globalData = ModCapabilities.getGlobal(player);
        }
    }


    @SubscribeEvent
    public void handleKeyInputEvent(InputEvent.Key event) {
        init();
        Keybinds key = getPressedKey();
        if (player != null) {
            if(playerData == null)
                return;

            if (key != null) {
                if (!MinecraftForge.EVENT_BUS.post(new KKInputEvent.Pre(key, this))) {
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
                        case REACTION_COMMAND -> reactionCommand();
                    }
                    MinecraftForge.EVENT_BUS.post(new KKInputEvent.Post(key, this));
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
                                    if (rtr instanceof EntityHitResult) {
                                        EntityHitResult ertr = (EntityHitResult) rtr;
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
    public void OnMouseWheelScroll(MouseScrollingEvent event) {
        init();
        if (mc.isWindowActive() && KeyboardHelper.isScrollActivatorDown()) {
            event.setCanceled(true);
            if(!Utils.shouldRenderOverlay(player))
                return;
            if(event.getScrollDelta() == Constants.WHEEL_DOWN) {
                commandDown();
            }else if(event.getScrollDelta() == Constants.WHEEL_UP) {
                commandUp();
            }
        }
    }

    public void showGui() {
        ModConfigs.toggleGui();
        player.displayClientMessage(Component.translatable("message.kingdomkeys.gui_toggle", ModConfigs.showGuiToggle.toString()), true);
    }

    public void openMenu() {
        PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
        if (playerData.getSoAState() != SoAState.COMPLETE) {
            if (level.dimension() != ModDimensions.DIVE_TO_THE_HEART) {
                mc.setScreen(new NoChoiceMenuPopup());
            }
        } else {
            GuiHelper.openMenu();
        }
    }

    public void summonKeyblade() {
        if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
            if(SeparateClassToAvoidLoadingIssuesExtendedReach.isBattleMode(player)) {
                if(Utils.findSummoned(player.getInventory(), playerData.getEquippedKeychain(DriveForm.NONE)) == -1 && playerData.getAlignment() == OrgMember.NONE)
                    if(!playerData.isAbilityEquipped(Strings.synchBlade))
                        PacketHandler.sendToServer(new CSPlayAnimation(KKAnimations.singleKeybladeMap.get(playerData.getSingleStyle())));
                    else
                        PacketHandler.sendToServer(new CSPlayAnimation(KKAnimations.dualKeybladeMap.get(playerData.getDualStyle())));
                else if(Utils.findSummoned(player.getInventory(), playerData.getEquippedWeapon()) == -1 && playerData.getAlignment() != OrgMember.NONE)

                    PacketHandler.sendToServer(new CSPlayAnimation(KKAnimations.orgMap.get(playerData.getAlignment())));
                else
                    PacketHandler.sendToServer(new CSSummonKeyblade()); // desummon
            }
            else
                PacketHandler.sendToServer(new CSSummonKeyblade());
        } else {
            if(SeparateClassToAvoidLoadingIssuesExtendedReach.isBattleMode(player) && Utils.findSummoned(player.getInventory(), playerData.getEquippedKeychain(DriveForm.NONE)) == -1)
                PacketHandler.sendToServer(new CSPlayAnimation(KKAnimations.DRIVE_SUMMON));
            else
                PacketHandler.sendToServer(new CSSummonKeyblade(new ResourceLocation(playerData.getActiveDriveForm())));
        }

        if(ModConfigs.summonTogether)
            PacketHandler.sendToServer(new CSSummonArmor());
    }

    public void summonArmor() {
        PacketHandler.sendToServer(new CSSummonArmor());
    }

    public void lockOn() {
        if (lockOn == null) {
            int reach = 35;
            HitResult rtr = getMouseOverExtended(reach);
            if (rtr != null && rtr instanceof EntityHitResult ertr) {
                double distance = player.distanceTo(ertr.getEntity());

                if (reach >= distance) {
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
        DriveForm form = ModDriveForms.registry.get().getValue(new ResourceLocation(playerData.getActiveDriveForm()));

        // Wisdom Form
        if (playerData.getActiveDriveForm().equals(Strings.Form_Wisdom)) {
            power = Constants.WISDOM_QR[wisdomLevel];
            if (!player.onGround()) {
                player.push(motionX * power / 2, 0, motionZ * power / 2);
                qrCooldown = 20;
            }
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
        DriveForm form = ModDriveForms.registry.get().getValue(new ResourceLocation(playerData.getActiveDriveForm()));

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
            ReactionCommand reaction = ModReactionCommands.registry.get().getValue(new ResourceLocation(reactionName));
            CommandMenuGui.reactionSelected = 0;
            if (reaction != null) {
                playSound(reaction.getUseSound(player, InputHandler.lockOn));
            } else {
                playInSound();
            }
		}
	}

    public void otherKeyPressed(InputEvent.Key event) {
        DriveForm form = ModDriveForms.registry.get().getValue(new ResourceLocation(playerData.getActiveDriveForm()));

        if (KeyboardHelper.isScrollActivatorDown() && event.getKey() > 320 && event.getKey() < 330) {
            if (globalData != null && globalData.getStoppedTicks() <= 0) {
                if (playerData.getMagicCasttimeTicks() <= 0 && playerData.getMagicCooldownTicks() <= 0 && !playerData.getRecharge() && form.canUseMagic() && !globalData.isKO()) {
                    PacketHandler.sendToServer(new CSUseShortcutPacket(event.getKey() - 321, InputHandler.lockOn));
                }
            }
        }

        if (KeyboardHelper.isScrollActivatorDown() && event.getKey() > 48 && event.getKey() < 58) {
            if (globalData != null && globalData.getStoppedTicks() <= 0) {
                if (playerData.getMagicCasttimeTicks() <= 0 && playerData.getMagicCooldownTicks() <= 0 && !playerData.getRecharge() && form.canUseMagic() && !globalData.isKO()) {
                    PacketHandler.sendToServer(new CSUseShortcutPacket(event.getKey() - 49, InputHandler.lockOn));
                }
            }
        }
    }

    public void loadLists() {
        if(playerData != null && worldData != null) {
            this.driveFormsMap = Utils.getSortedDriveForms(playerData.getDriveFormMap(), playerData.getVisibleDriveForms());
            if(!playerData.isAbilityEquipped(Strings.darkDomination)) {
            	this.driveFormsMap.remove(Strings.Form_Anti);
            }
            this.driveFormsMap.remove(DriveForm.NONE.toString());
            this.driveFormsMap.remove(DriveForm.SYNCH_BLADE.toString());
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

    public static HitResult getMouseOverExtended(float dist) {
        Minecraft mc = Minecraft.getInstance();
        Entity theRenderViewEntity = mc.getCameraEntity();
        if(theRenderViewEntity == null){
            return null;
        }
        AABB theViewBoundingBox = new AABB(theRenderViewEntity.getX() - 0.5D, theRenderViewEntity.getY() - 0.0D, theRenderViewEntity.getZ() - 0.5D, theRenderViewEntity.getX() + 0.5D, theRenderViewEntity.getY() + 1.5D, theRenderViewEntity.getZ() + 0.5D);
        HitResult returnMOP = null;
        if (mc.level != null) {
            double var2 = dist;
            returnMOP = theRenderViewEntity.pick(var2, 0, false);
            double calcdist = var2;
            Vec3 pos = theRenderViewEntity.getEyePosition(0);
            var2 = calcdist;
            if (returnMOP != null) {
                calcdist = returnMOP.getLocation().distanceTo(pos);
            }

            Vec3 lookvec = theRenderViewEntity.getViewVector(0);
            Vec3 var8 = pos.add(lookvec.x * var2, lookvec.y * var2, lookvec.z * var2);
            Entity pointedEntity = null;
            float var9 = 1.0F;

            List<Entity> list = mc.level.getEntities(theRenderViewEntity, theViewBoundingBox.inflate(lookvec.x * var2, lookvec.y * var2, lookvec.z * var2).inflate(var9, var9, var9));
            double d = calcdist;

            for (Entity entity : list) {
                if (entity.isPickable()) {
                    float bordersize = entity.getPickRadius();
                    AABB aabb = new AABB(entity.getX() - entity.getBbWidth() / 2, entity.getY(), entity.getZ() - entity.getBbWidth() / 2, entity.getX() + entity.getBbWidth() / 2, entity.getY() + entity.getBbHeight(), entity.getZ() + entity.getBbWidth() / 2);
                    aabb.inflate(bordersize, bordersize, bordersize);
                    Optional<Vec3> mop0 = aabb.clip(pos, var8);

                    if (aabb.contains(pos)) {
                        if (0.0D < d || d == 0.0D) {
                            pointedEntity = entity;
                            d = 0.0D;
                        }
                    } else if (mop0 != null && mop0.isPresent()) {
                        double d1 = pos.distanceTo(mop0.get());

                        if (d1 < d || d == 0.0D) {
                            pointedEntity = entity;
                            d = d1;
                        }
                    }
                }
            }

            if (pointedEntity != null && (d < calcdist || returnMOP == null)) {
                returnMOP = new EntityHitResult(pointedEntity);
            }
        }
        return returnMOP;
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
