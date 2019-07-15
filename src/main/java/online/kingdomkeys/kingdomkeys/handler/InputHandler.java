package online.kingdomkeys.kingdomkeys.handler;


import java.util.List;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import online.kingdomkeys.kingdomkeys.client.gui.CommandMenuGui;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Constants;
import online.kingdomkeys.kingdomkeys.lib.PortalCoords;

//TODO cleanup
public class InputHandler {

    List<String> magicCommands, itemsCommands, driveCommands;
    List<PortalCoords> portalCommands;
    //List<Ability> attackCommands;

    public static LivingEntity lockOn = null;

    public boolean antiFormCheck() {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        World world = mc.world;
        double random = Math.random();
        int ap = 0;//TODO player.getCapability(ModCapabilities.DRIVE_STATE, null).getAntiPoints();
        int prob = 0;
        if (ap > 0 && ap <= 4)
            prob = 0;
        else if (ap > 4 && ap <= 9)
            prob = 10;
        else if (ap >= 10)
            prob = 25;

        if (random * 100 < prob) {
            //PacketDispatcher.sendToServer(new DriveFormPacket(Strings.Form_Anti));
            CommandMenuGui.selected = CommandMenuGui.ATTACK;
            CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
            //PacketDispatcher.sendToServer(new AntiPoints(-4));
            world.playSound(player, player.getPosition(), ModSounds.select, SoundCategory.MASTER, 1.0f, 1.0f);
            return true;
        } else
            return false;
    }

    public void commandUp() {
        Minecraft mc = Minecraft.getInstance();
        mc.world.playSound(mc.player, mc.player.getPosition(), ModSounds.move, SoundCategory.MASTER, 1.0f, 1.0f);

        loadLists();

        // Mainmenu
        if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAIN) {
            if (CommandMenuGui.selected == CommandMenuGui.ATTACK)
                CommandMenuGui.selected = CommandMenuGui.DRIVE;
            else
                CommandMenuGui.selected++;
        }
        // InsideMagic
       /* else if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAGIC) {
            if (CommandMenuGui.magicselected > 0) {
                CommandMenuGui.magicselected--;
                CommandMenuGui.submenu = CommandMenuGui.SUB_MAGIC;
            } else if (CommandMenuGui.magicselected <= 1)
                CommandMenuGui.magicselected = this.magicCommands.size() - 1;
        }
        // InsideItems
        else if (CommandMenuGui.submenu == CommandMenuGui.SUB_ITEMS) {
            if (CommandMenuGui.potionselected > 0) {
                CommandMenuGui.potionselected--;
                CommandMenuGui.submenu = CommandMenuGui.SUB_ITEMS;
            } else if (CommandMenuGui.potionselected <= 1) {
                CommandMenuGui.potionselected = this.itemsCommands.size() - 1;
            }
        }
        // InsideDrive
        else if (CommandMenuGui.submenu == CommandMenuGui.SUB_DRIVE) {
            if (CommandMenuGui.driveselected > 0) {
                CommandMenuGui.driveselected--;
                CommandMenuGui.submenu = CommandMenuGui.SUB_DRIVE;
            } else if (CommandMenuGui.driveselected <= 1) {
                CommandMenuGui.driveselected = this.driveCommands.size() - 1;
            }
        }
        // InsidePortal
        else if (CommandMenuGui.submenu == CommandMenuGui.SUB_PORTALS) {
            if (CommandMenuGui.portalSelected > 0) {
                CommandMenuGui.portalSelected--;
                CommandMenuGui.submenu = CommandMenuGui.SUB_PORTALS;
            } else if (CommandMenuGui.portalSelected <= 1) {
                CommandMenuGui.portalSelected = this.portalCommands.size() - 1;
            }
        }
        // InsideAttacks
        else if (CommandMenuGui.submenu == CommandMenuGui.SUB_ATTACKS) {
            if (CommandMenuGui.attackSelected > 0) {
                CommandMenuGui.attackSelected--;
                CommandMenuGui.submenu = CommandMenuGui.SUB_ATTACKS;
            } else if (CommandMenuGui.attackSelected <= 1) {
                CommandMenuGui.attackSelected = this.attackCommands.size() - 1;
            }
        }*/
    }

    public void commandDown() {
        Minecraft mc = Minecraft.getInstance();
        mc.world.playSound(mc.player, mc.player.getPosition(), ModSounds.move, SoundCategory.MASTER, 1.0f, 1.0f);
        loadLists();

        // Mainmenu
        if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAIN) {
            if (CommandMenuGui.selected == CommandMenuGui.DRIVE)
                CommandMenuGui.selected = CommandMenuGui.ATTACK;
            else
                CommandMenuGui.selected--;
        }
        // InsideMagic
        /*else if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAGIC) {
            if (CommandMenuGui.magicselected < this.magicCommands.size() - 1) {
                CommandMenuGui.magicselected++;
                CommandMenuGui.submenu = CommandMenuGui.SUB_MAGIC;
            } else if (CommandMenuGui.magicselected >= this.magicCommands.size() - 1)
                CommandMenuGui.magicselected = 0;
        }
        // InsideItems
        else if (CommandMenuGui.submenu == CommandMenuGui.SUB_ITEMS) {
            if (CommandMenuGui.potionselected < this.itemsCommands.size() - 1) {
                CommandMenuGui.potionselected++;
                CommandMenuGui.submenu = CommandMenuGui.SUB_ITEMS;
            } else {
                if (CommandMenuGui.potionselected >= this.itemsCommands.size() - 1)
                    CommandMenuGui.potionselected = 0;
            }
        }
        // InsideDrive
        else if (CommandMenuGui.submenu == CommandMenuGui.SUB_DRIVE) {
            if (CommandMenuGui.driveselected < this.driveCommands.size() - 1) {
                CommandMenuGui.driveselected++;
                CommandMenuGui.submenu = CommandMenuGui.SUB_DRIVE;
            } else {
                if (CommandMenuGui.driveselected >= this.driveCommands.size() - 1)
                    CommandMenuGui.driveselected = 0;
            }
        }
        // InsidePortal
        else if (CommandMenuGui.submenu == CommandMenuGui.SUB_PORTALS) {
            if (CommandMenuGui.portalSelected < this.portalCommands.size() - 1) {
                CommandMenuGui.portalSelected++;
                CommandMenuGui.submenu = CommandMenuGui.SUB_PORTALS;
            } else {
                if (CommandMenuGui.portalSelected >= this.portalCommands.size() - 1)
                    CommandMenuGui.portalSelected = 0;
            }
        }
        // InsideAttack
        else if (CommandMenuGui.submenu == CommandMenuGui.SUB_ATTACKS) {
            if (CommandMenuGui.attackSelected < this.attackCommands.size() - 1) {
                CommandMenuGui.attackSelected++;
                CommandMenuGui.submenu = CommandMenuGui.SUB_ATTACKS;
            } else {
                if (CommandMenuGui.attackSelected >= this.attackCommands.size() - 1)
                    CommandMenuGui.attackSelected = 0;
            }
        }*/
    }

    public void commandEnter() {
    	Minecraft mc = Minecraft.getInstance();
        mc.world.playSound(mc.player, mc.player.getPosition(), ModSounds.menuin, SoundCategory.MASTER, 1.0f, 1.0f);
        PlayerEntity player = mc.player;
        World world = mc.world;
        //PlayerStatsCapability.IPlayerStats STATS = (IPlayerStats) player.getCapability(ModCapabilities.PLAYER_STATS, null);
        //IDriveState DRIVE = (IDriveState) player.getCapability(ModCapabilities.DRIVE_STATE, null);

        loadLists();

        switch (CommandMenuGui.selected) {
            case CommandMenuGui.ATTACK: //Accessing ATTACK / PORTAL submenu
                System.out.println("attack");
                /*if (player.getCapability(ModCapabilities.ORGANIZATION_XIII, null).getMember() != Utils.OrgMember.NONE) {
                    // Submenu of the portals
                    if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAIN) {
                        if (!this.portalCommands.isEmpty() && !STATS.getRecharge()) {
                            CommandMenuGui.submenu = CommandMenuGui.SUB_PORTALS;
                            CommandMenuGui.portalSelected = 0;
                            world.playSound(player, player.getPosition(), ModSounds.select, SoundCategory.MASTER, 1.0f, 1.0f);
                        } else {
                            CommandMenuGui.selected = CommandMenuGui.ATTACK;
                            world.playSound(player, player.getPosition(), ModSounds.error, SoundCategory.MASTER, 1.0f, 1.0f);
                        }
                        return;
                    }
                } else {
                    // Attacks Submenu
                    if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAIN) {
                        if (!this.attackCommands.isEmpty() && !STATS.getRecharge()) {
                            CommandMenuGui.submenu = CommandMenuGui.SUB_ATTACKS;
                            CommandMenuGui.attackSelected = 0;
                            world.playSound(player, player.getPosition(), ModSounds.select, SoundCategory.MASTER, 1.0f, 1.0f);
                        } else {
                            CommandMenuGui.selected = CommandMenuGui.ATTACK;
                            world.playSound(player, player.getPosition(), ModSounds.error, SoundCategory.MASTER, 1.0f, 1.0f);
                        }
                        return;
                    }
                }

                if (player.getCapability(ModCapabilities.DRIVE_STATE, null).getActiveDriveName().equals(Strings.Form_Wisdom)) {
                    PacketDispatcher.sendToServer(new MagicWisdomShot());
                }*/
                break;
            case CommandMenuGui.MAGIC: //Accessing MAGIC submenu
                if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAIN) {
                    System.out.println("magic");
                    /*if (!STATS.getRecharge() && (!this.magicCommands.isEmpty() && (!DRIVE.getActiveDriveName().equals(Strings.Form_Valor) && !DRIVE.getActiveDriveName().equals(Strings.Form_Anti)))) {
                        CommandMenuGui.magicselected = 0;
                        CommandMenuGui.submenu = CommandMenuGui.SUB_MAGIC;
                        world.playSound(player, player.getPosition(), ModSounds.select, SoundCategory.MASTER, 1.0f, 1.0f);
                        return;
                    } else {
                        CommandMenuGui.selected = CommandMenuGui.ATTACK;
                        world.playSound(player, player.getPosition(), ModSounds.error, SoundCategory.MASTER, 1.0f, 1.0f);
                    }*/
                }
                break;

            case CommandMenuGui.ITEMS: //Accessing ITEMS submenu
                if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAIN) {
                    System.out.println("items");
                    /*if (!this.itemsCommands.isEmpty()) {
                        CommandMenuGui.submenu = CommandMenuGui.SUB_ITEMS;
                        CommandMenuGui.potionselected = 0;
                        world.playSound(player, player.getPosition(), ModSounds.select, SoundCategory.MASTER, 1.0f, 1.0f);
                    } else {
                        CommandMenuGui.selected = CommandMenuGui.ATTACK;
                        world.playSound(player, player.getPosition(), ModSounds.error, SoundCategory.MASTER, 1.0f, 1.0f);
                    }
                    return;*/
                }
                break;

            case CommandMenuGui.DRIVE: //Accessing DRIVE submenu
                if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAIN) {
                    System.out.println("drive");
                   /* if (player.getCapability(ModCapabilities.ORGANIZATION_XIII, null).getMember() != Utils.OrgMember.NONE) {
                        // TODO Use Limit
                        player.sendMessage(new TextComponentString("Limits are not available yet"));
                        CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
                        CommandMenuGui.selected = CommandMenuGui.ATTACK;
                        world.playSound(player, player.getPosition(), ModSounds.error, SoundCategory.MASTER, 1.0f, 1.0f);
                    } else {
                        if (DRIVE.getInDrive()) {// Revert
                            if (DRIVE.getActiveDriveName().equals(Strings.Form_Anti) && !player.getCapability(ModCapabilities.CHEAT_MODE, null).getCheatMode()) {
                                CommandMenuGui.selected = CommandMenuGui.ATTACK;
                                world.playSound(player, player.getPosition(), ModSounds.error, SoundCategory.MASTER, 1.0f, 1.0f);
                                player.sendMessage(new TextComponentTranslation("Cannot revert while in Anti form"));
                            } else {
                                PacketDispatcher.sendToServer(new DriveFormPacket(DRIVE.getActiveDriveName(), true));
                                if (DriveFormRegistry.isDriveFormRegistered(DRIVE.getActiveDriveName()))
                                    DriveFormRegistry.get(DRIVE.getActiveDriveName()).endDrive(player);
                                CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
                                CommandMenuGui.selected = CommandMenuGui.ATTACK;
                                world.playSound(player, player.getPosition(), ModSounds.select, SoundCategory.MASTER, 1.0f, 1.0f);
                            }
                        } else if (this.driveCommands.isEmpty() || DRIVE.getDP() <= 0) {
                            world.playSound(player, player.getPosition(), ModSounds.error, SoundCategory.MASTER, 1.0f, 1.0f);
                            CommandMenuGui.selected = CommandMenuGui.ATTACK;
                        } else {
                            CommandMenuGui.driveselected = 0;
                            CommandMenuGui.submenu = CommandMenuGui.SUB_DRIVE;
                            world.playSound(player, player.getPosition(), ModSounds.select, SoundCategory.MASTER, 1.0f, 1.0f);
                            return;
                        }
                    }*/
                }
                break;
        }
        // Attacks Submenu
        if (CommandMenuGui.selected == CommandMenuGui.ATTACK && CommandMenuGui.submenu == CommandMenuGui.SUB_ATTACKS) {
            /*if (this.attackCommands.isEmpty()) {
            } else {
                // ModDriveForms.getDriveForm(player, world, (String)
                // this.driveCommands.get(CommandMenuGui.driveselected));
                if (!player.getCapability(ModCapabilities.PLAYER_STATS, null).getRecharge()) {
                    Ability ability = this.attackCommands.get((byte) CommandMenuGui.attackSelected);
                    // UseAbility
                    useAttack(player, ability);
                    CommandMenuGui.selected = CommandMenuGui.ATTACK;
                    CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
                    world.playSound(player, player.getPosition(), ModSounds.select, SoundCategory.MASTER, 1.0f, 1.0f);
                }
            }*/
        }

        // Portal Submenu
        if (CommandMenuGui.selected == CommandMenuGui.ATTACK && CommandMenuGui.submenu == CommandMenuGui.SUB_PORTALS) {
            /*if (this.portalCommands.isEmpty()) {
            } else {
                // ModDriveForms.getDriveForm(player, world, (String)
                // this.driveCommands.get(CommandMenuGui.driveselected));
                if (!player.getCapability(ModCapabilities.PLAYER_STATS, null).getRecharge()) {
                    PortalCoords coords = this.portalCommands.get((byte) CommandMenuGui.portalSelected);
                    if (coords.getX() != 0 && coords.getY() != 0 && coords.getZ() != 0) {
                        // ValidPortal
                        summonPortal(player, coords);
                    } else {
                        player.sendMessage(new TextComponentString(TextFormatting.RED + "You don't have any portal destination"));
                    }

                    CommandMenuGui.selected = CommandMenuGui.ATTACK;
                    CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
                    world.playSound(player, player.getPosition(), ModSounds.select, SoundCategory.MASTER, 1.0f, 1.0f);
                }
            }*/
        }

        // Magic Submenu
        if (CommandMenuGui.selected == CommandMenuGui.MAGIC && CommandMenuGui.submenu == CommandMenuGui.SUB_MAGIC) {
            /*if (this.magicCommands.isEmpty()) {
            } else if (!STATS.getRecharge() || Constants.getCost((String) this.magicCommands.get(CommandMenuGui.magicselected)) == -1 && STATS.getMP() > 0) {
                Magic.getMagic(player, world, (String) this.magicCommands.get(CommandMenuGui.magicselected));
                CommandMenuGui.selected = CommandMenuGui.ATTACK;
                CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
                world.playSound(player, player.getPosition(), ModSounds.select, SoundCategory.MASTER, 1.0f, 1.0f);
            }*/
        }

        if (CommandMenuGui.selected == CommandMenuGui.ITEMS && CommandMenuGui.submenu == CommandMenuGui.SUB_ITEMS) {
            /*if (this.itemsCommands.isEmpty()) {
            } else if (!this.itemsCommands.isEmpty()) {
                ItemKKPotion.getItem(player, world, (String) this.itemsCommands.get(CommandMenuGui.potionselected), CommandMenuGui.potionselected);

                CommandMenuGui.selected = CommandMenuGui.ATTACK;
                CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
                world.playSound(player, player.getPosition(), ModSounds.select, SoundCategory.MASTER, 1.0f, 1.0f);
            }*/
        }

        if (CommandMenuGui.selected == CommandMenuGui.DRIVE && CommandMenuGui.submenu == CommandMenuGui.SUB_DRIVE) {
            /*if (this.driveCommands.isEmpty()) {
            } else if ((DRIVE.getDP() >= Constants.getCost((String) this.driveCommands.get(CommandMenuGui.driveselected)))) {
                if (this.driveCommands.get(CommandMenuGui.driveselected).equals(Strings.Form_Final)) {
                    ModDriveForms.driveIntoForm(player, world, (String) this.driveCommands.get(CommandMenuGui.driveselected));
                } else {
                    if (!antiFormCheck()) {
                        ModDriveForms.driveIntoForm(player, world, (String) this.driveCommands.get(CommandMenuGui.driveselected));
                    }
                }
                CommandMenuGui.selected = CommandMenuGui.ATTACK;
                CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
                world.playSound(player, player.getPosition(), ModSounds.select, SoundCategory.MASTER, 1.0f, 1.0f);
            }*/
        }

    }

    public void commandBack() {
    	Minecraft mc = Minecraft.getInstance();
        mc.world.playSound(mc.player, mc.player.getPosition(), ModSounds.menuout, SoundCategory.MASTER, 1.0f, 1.0f);
        PlayerEntity player = mc.player;
        World world = mc.world;

        if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAIN)
            CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
        else if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAGIC) {
            CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
            world.playSound(player, player.getPosition(), ModSounds.cancel, SoundCategory.MASTER, 1.0f, 1.0f);
        } else if (CommandMenuGui.submenu == CommandMenuGui.SUB_ITEMS) {
            CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
            world.playSound(player, player.getPosition(), ModSounds.cancel, SoundCategory.MASTER, 1.0f, 1.0f);
        } else if (CommandMenuGui.submenu == CommandMenuGui.SUB_DRIVE) {
            CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
            world.playSound(player, player.getPosition(), ModSounds.cancel, SoundCategory.MASTER, 1.0f, 1.0f);
        } else if (CommandMenuGui.submenu == CommandMenuGui.SUB_PORTALS) {
            CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
            world.playSound(player, player.getPosition(), ModSounds.cancel, SoundCategory.MASTER, 1.0f, 1.0f);
        } else if (CommandMenuGui.submenu == CommandMenuGui.SUB_ATTACKS) {
            CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
            world.playSound(player, player.getPosition(), ModSounds.cancel, SoundCategory.MASTER, 1.0f, 1.0f);
        }
        CommandMenuGui.magicselected = 0;
        CommandMenuGui.driveselected = 0;

        // GuiHelper.openTutorial(Tutorials.TUTORIAL_SOA_1);

    }

    @SubscribeEvent
    public void handleKeyInputEvent(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        World world = mc.world;
      //  SummonKeybladeCapability.ISummonKeyblade SUMMON = player.getCapability(ModCapabilities.SUMMON_KEYBLADE, null);

       /* if (player.getCapability(ModCapabilities.DRIVE_STATE, null).getInDrive()) {
            Minecraft.getMinecraft().gameSettings.keyBindSwapHands.isPressed();
        }*/

        Keybinds key = getPressedKey();
        if (key != null)
            switch (key) {
               /* case OPENMENU:
                    GuiHelper.openMenu();
                    PacketDispatcher.sendToServer(new OpenMenu());
                    break;

                case SHOW_GUI:
                    MainConfig.toggleShowGUI();
                    break;*/

                case SCROLL_UP:
                   // if (!MainConfig.displayGUI())
                     //   break;
                    commandUp();
                    break;

                case SCROLL_DOWN:
                  //  if (!MainConfig.displayGUI())
                   //     break;
                    commandDown();
                    break;

                case ENTER:
                   /* if (!MainConfig.displayGUI())
                        break;*/
                    commandEnter();

                    break;

                case BACK:
                  //  if (!MainConfig.displayGUI())
                  //      break;
                    commandBack();
                    

                    break;
               /* case SUMMON_KEYBLADE:
                    if (!player.getCapability(ModCapabilities.DRIVE_STATE, null).getInDrive())
                        Utils.summonWeapon(player, EnumHand.MAIN_HAND, 0);
                    break;
                case SCROLL_ACTIVATOR:
                    break;
                case ACTION:
                    commandAction();
                    break;

                case LOCK_ON:
                    if (lockOn == null) {
                        RayTraceResult rtr = getMouseOverExtended(100);
                        if (rtr != null) {
                            if (rtr.entityHit != null) {
                                double distanceSq = player.getDistanceSqToEntity(rtr.entityHit);
                                double reachSq = 100 * 100;
                                if (reachSq >= distanceSq) {
                                    if (rtr.entityHit instanceof EntityLivingBase) {
                                        lockOn = (EntityLivingBase) rtr.entityHit;
                                        LockOn.target = (EntityLivingBase) rtr.entityHit;
                                        player.world.playSound((EntityPlayer) player, player.getPosition(), ModSounds.lockon, SoundCategory.MASTER, 1.0f, 1.0f);
                                    } else if (rtr.entityHit instanceof EntityPart) {
                                        EntityPart part = (EntityPart) rtr.entityHit;
                                        if (part.getParent() != null && part.getParent() instanceof EntityLivingBase) {
                                            lockOn = (EntityLivingBase) part.getParent();
                                            LockOn.target = (EntityLivingBase) part.getParent();
                                            player.world.playSound((EntityPlayer) player, player.getPosition(), ModSounds.lockon, SoundCategory.MASTER, 1.0f, 1.0f);

                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        lockOn = null;
                    }
                    break;*/

            }
    }

    private Keybinds getPressedKey() {
        for (Keybinds key : Keybinds.values())
            if (key.isPressed())
                return key;
        return null;
    }

    @SubscribeEvent
    public void handleKeyInputEvent(InputEvent.MouseInputEvent event) {
        /*
         * if (player.getCapability(ModCapabilities.DRIVE_STATE, null).getInDrive()) {
         * if (player.getCapability(ModCapabilities.DRIVE_STATE,
         * null).getActiveDriveName().equals(Strings.Form_Wisdom)) {
         * event.setCanceled(true); } else { event.setCanceled(false); } }
         */

        if (event.getButton() == Constants.LEFT_MOUSE && KeyboardHelper.isScrollActivatorDown()) {
            commandEnter();
            //event.setCanceled(true);
        }

        if (event.getButton() == Constants.RIGHT_MOUSE && KeyboardHelper.isScrollActivatorDown()) {
            commandBack();
           // event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void OnMouseWheelScroll(online.kingdomkeys.kingdomkeys.handler.ScrollCallbackWrapper.MouseScrolledEvent event) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        World world = mc.world;

        if (!mc.isGameFocused() && !KeyboardHelper.isScrollActivatorDown()) {
            event.setCanceled(false);
            return;
        }

        if (event.getYOffset() == Constants.WHEEL_DOWN && KeyboardHelper.isScrollActivatorDown()) {
            commandDown();
            event.setCanceled(true);
        }
        if (event.getYOffset() == Constants.WHEEL_UP && KeyboardHelper.isScrollActivatorDown()) {
            commandUp();
            event.setCanceled(true);
        }

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
        ACTION("key.kingdomkeys.action",GLFW.GLFW_KEY_X);

        private final KeyBinding keybinding;
        Keybinds(String name, int defaultKey) {
            keybinding = new KeyBinding(name, defaultKey, "key.categories.kingdomkeys");
        }

        public KeyBinding getKeybind() {
            return keybinding;
        }

        public boolean isPressed() {
            return keybinding.isPressed();
        }
    }

    public void loadLists() {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        /*PlayerStatsCapability.IPlayerStats STATS = player.getCapability(ModCapabilities.PLAYER_STATS, null);
        IDriveState DS = player.getCapability(ModCapabilities.DRIVE_STATE, null);
        this.magicCommands = new ArrayList<String>();
        this.itemsCommands = new ArrayList<String>();
        this.driveCommands = new ArrayList<String>();
        this.portalCommands = new ArrayList<PortalCoords>();
        this.attackCommands = new ArrayList<Ability>();

        this.magicCommands.clear();
        for (int i = 0; i < player.getCapability(ModCapabilities.MAGIC_STATE, null).getInventorySpells().getSlots(); i++)
            if (!ItemStack.areItemStacksEqual(player.getCapability(ModCapabilities.MAGIC_STATE, null).getInventorySpells().getStackInSlot(i), ItemStack.EMPTY))
                this.magicCommands.add(((ItemSpellOrb) player.getCapability(ModCapabilities.MAGIC_STATE, null).getInventorySpells().getStackInSlot(i).getItem()).getMagicName());

        this.itemsCommands.clear();
        for (int i = 0; i < player.getCapability(ModCapabilities.PLAYER_STATS, null).getInventoryPotionsMenu().getSlots(); i++)
            if (!ItemStack.areItemStacksEqual(player.getCapability(ModCapabilities.PLAYER_STATS, null).getInventoryPotionsMenu().getStackInSlot(i), ItemStack.EMPTY))
                this.itemsCommands.add(((ItemKKPotion) player.getCapability(ModCapabilities.PLAYER_STATS, null).getInventoryPotionsMenu().getStackInSlot(i).getItem()).getUnlocalizedName().substring(5));

        this.driveCommands.clear();
        for (int i = 0; i < player.getCapability(ModCapabilities.DRIVE_STATE, null).getInventoryDriveForms().getSlots(); i++)
            if (!ItemStack.areItemStacksEqual(player.getCapability(ModCapabilities.DRIVE_STATE, null).getInventoryDriveForms().getStackInSlot(i), ItemStack.EMPTY))
                this.driveCommands.add(((ItemDriveForm) player.getCapability(ModCapabilities.DRIVE_STATE, null).getInventoryDriveForms().getStackInSlot(i).getItem()).getDriveFormName());

        this.portalCommands.clear();
        for (byte i = 0; i < 3; i++) {
            PortalCoords coords = player.getCapability(ModCapabilities.ORGANIZATION_XIII, null).getPortalCoords(i);
            if (!(coords.getX() == 0 && coords.getY() == 0 && coords.getZ() == 0)) {
                this.portalCommands.add(coords);
                // System.out.println(i+" Added portal: "+coords.getPID());
            }
        }

        this.attackCommands.clear();
        IAbilities ABILITIES = player.getCapability(ModCapabilities.ABILITIES, null);
        // for (int i = 0; i < ABILITIES.getEquippedAbilities().size(); i++) {
        for (Ability ability : ABILITIES.getEquippedAbilities()) {
            if (ability == ModAbilities.sonicBlade || ability == ModAbilities.strikeRaid) {
                this.attackCommands.add(ability);
            }
        }*/
    }
}