package online.kingdomkeys.kingdomkeys.handler;


import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import online.kingdomkeys.kingdomkeys.client.gui.GuiCommandMenu;
import org.lwjgl.glfw.GLFW;

public class InputHandler {

    public void commandUp() {
       // loadLists();

        // Mainmenu
        if (GuiCommandMenu.submenu == GuiCommandMenu.SUB_MAIN) {
            if (GuiCommandMenu.selected == GuiCommandMenu.ATTACK)
                GuiCommandMenu.selected = GuiCommandMenu.DRIVE;
            else
                GuiCommandMenu.selected++;
        }
        // InsideMagic
       /* else if (GuiCommandMenu.submenu == GuiCommandMenu.SUB_MAGIC) {
            if (GuiCommandMenu.magicselected > 0) {
                GuiCommandMenu.magicselected--;
                GuiCommandMenu.submenu = GuiCommandMenu.SUB_MAGIC;
            } else if (GuiCommandMenu.magicselected <= 1)
                GuiCommandMenu.magicselected = this.magicCommands.size() - 1;
        }
        // InsideItems
        else if (GuiCommandMenu.submenu == GuiCommandMenu.SUB_ITEMS) {
            if (GuiCommandMenu.potionselected > 0) {
                GuiCommandMenu.potionselected--;
                GuiCommandMenu.submenu = GuiCommandMenu.SUB_ITEMS;
            } else if (GuiCommandMenu.potionselected <= 1) {
                GuiCommandMenu.potionselected = this.itemsCommands.size() - 1;
            }
        }
        // InsideDrive
        else if (GuiCommandMenu.submenu == GuiCommandMenu.SUB_DRIVE) {
            if (GuiCommandMenu.driveselected > 0) {
                GuiCommandMenu.driveselected--;
                GuiCommandMenu.submenu = GuiCommandMenu.SUB_DRIVE;
            } else if (GuiCommandMenu.driveselected <= 1) {
                GuiCommandMenu.driveselected = this.driveCommands.size() - 1;
            }
        }
        // InsidePortal
        else if (GuiCommandMenu.submenu == GuiCommandMenu.SUB_PORTALS) {
            if (GuiCommandMenu.portalSelected > 0) {
                GuiCommandMenu.portalSelected--;
                GuiCommandMenu.submenu = GuiCommandMenu.SUB_PORTALS;
            } else if (GuiCommandMenu.portalSelected <= 1) {
                GuiCommandMenu.portalSelected = this.portalCommands.size() - 1;
            }
        }
        // InsideAttacks
        else if (GuiCommandMenu.submenu == GuiCommandMenu.SUB_ATTACKS) {
            if (GuiCommandMenu.attackSelected > 0) {
                GuiCommandMenu.attackSelected--;
                GuiCommandMenu.submenu = GuiCommandMenu.SUB_ATTACKS;
            } else if (GuiCommandMenu.attackSelected <= 1) {
                GuiCommandMenu.attackSelected = this.attackCommands.size() - 1;
            }
        }*/
    }

    public void commandDown() {
       // loadLists();

        // Mainmenu
        if (GuiCommandMenu.submenu == GuiCommandMenu.SUB_MAIN) {
            if (GuiCommandMenu.selected == GuiCommandMenu.DRIVE)
                GuiCommandMenu.selected = GuiCommandMenu.ATTACK;
            else
                GuiCommandMenu.selected--;
        }
        // InsideMagic
        /*else if (GuiCommandMenu.submenu == GuiCommandMenu.SUB_MAGIC) {
            if (GuiCommandMenu.magicselected < this.magicCommands.size() - 1) {
                GuiCommandMenu.magicselected++;
                GuiCommandMenu.submenu = GuiCommandMenu.SUB_MAGIC;
            } else if (GuiCommandMenu.magicselected >= this.magicCommands.size() - 1)
                GuiCommandMenu.magicselected = 0;
        }
        // InsideItems
        else if (GuiCommandMenu.submenu == GuiCommandMenu.SUB_ITEMS) {
            if (GuiCommandMenu.potionselected < this.itemsCommands.size() - 1) {
                GuiCommandMenu.potionselected++;
                GuiCommandMenu.submenu = GuiCommandMenu.SUB_ITEMS;
            } else {
                if (GuiCommandMenu.potionselected >= this.itemsCommands.size() - 1)
                    GuiCommandMenu.potionselected = 0;
            }
        }
        // InsideDrive
        else if (GuiCommandMenu.submenu == GuiCommandMenu.SUB_DRIVE) {
            if (GuiCommandMenu.driveselected < this.driveCommands.size() - 1) {
                GuiCommandMenu.driveselected++;
                GuiCommandMenu.submenu = GuiCommandMenu.SUB_DRIVE;
            } else {
                if (GuiCommandMenu.driveselected >= this.driveCommands.size() - 1)
                    GuiCommandMenu.driveselected = 0;
            }
        }
        // InsidePortal
        else if (GuiCommandMenu.submenu == GuiCommandMenu.SUB_PORTALS) {
            if (GuiCommandMenu.portalSelected < this.portalCommands.size() - 1) {
                GuiCommandMenu.portalSelected++;
                GuiCommandMenu.submenu = GuiCommandMenu.SUB_PORTALS;
            } else {
                if (GuiCommandMenu.portalSelected >= this.portalCommands.size() - 1)
                    GuiCommandMenu.portalSelected = 0;
            }
        }
        // InsideAttack
        else if (GuiCommandMenu.submenu == GuiCommandMenu.SUB_ATTACKS) {
            if (GuiCommandMenu.attackSelected < this.attackCommands.size() - 1) {
                GuiCommandMenu.attackSelected++;
                GuiCommandMenu.submenu = GuiCommandMenu.SUB_ATTACKS;
            } else {
                if (GuiCommandMenu.attackSelected >= this.attackCommands.size() - 1)
                    GuiCommandMenu.attackSelected = 0;
            }
        }*/
    }

    @SubscribeEvent
    public void handleKeyInputEvent(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        EntityPlayer player = mc.player;
        World world = mc.world;
      //  SummonKeybladeCapability.ISummonKeyblade SUMMON = player.getCapability(ModCapabilities.SUMMON_KEYBLADE, null);

       /* if (player.getCapability(ModCapabilities.DRIVE_STATE, null).getInDrive()) {
            Minecraft.getMinecraft().gameSettings.keyBindSwapHands.isPressed();
        }*/
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

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
                  //  world.playSound(player, player.getPosition(), ModSounds.move, SoundCategory.MASTER, 1.0f, 1.0f);
                    break;

                case SCROLL_DOWN:
                  //  if (!MainConfig.displayGUI())
                   //     break;
                    commandDown();
                  //  world.playSound(player, player.getPosition(), ModSounds.move, SoundCategory.MASTER, 1.0f, 1.0f);
                    break;

               /* case ENTER:
                    if (!MainConfig.displayGUI())
                        break;
                    commandEnter();
                    break;

                case BACK:
                    if (!MainConfig.displayGUI())
                        break;
                    commandBack();
                    break;
                case SUMMON_KEYBLADE:
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
   /* @SubscribeEvent
    public void OnMouseWheelScroll(MouseEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        World world = mc.world;
        if (!mc.inGameHasFocus && !KeyboardHelper.isScrollActivatorDown()) {
            event.setCanceled(false);
            return;
        }

        /*
         * if (player.getCapability(ModCapabilities.DRIVE_STATE, null).getInDrive()) {
         * if (player.getCapability(ModCapabilities.DRIVE_STATE,
         * null).getActiveDriveName().equals(Strings.Form_Wisdom)) {
         * event.setCanceled(true); } else { event.setCanceled(false); } }


        if (event.getButton() == Constants.LEFT_MOUSE && KeyboardHelper.isScrollActivatorDown() && event.isButtonstate()) {
            commandEnter();
            event.setCanceled(true);
        }

        if (event.getButton() == Constants.RIGHT_MOUSE && KeyboardHelper.isScrollActivatorDown() && event.isButtonstate()) {
            commandBack();
            event.setCanceled(true);
        }

        if (event.getDwheel() <= Constants.WHEEL_DOWN && KeyboardHelper.isScrollActivatorDown() && event.getDwheel() != 0) {
            commandDown();
            event.setCanceled(true);
            world.playSound(player, player.getPosition(), ModSounds.move, SoundCategory.MASTER, 1.0f, 1.0f);
        }
        if (event.getDwheel() >= Constants.WHEEL_UP && KeyboardHelper.isScrollActivatorDown() && event.getDwheel() != 0) {
            commandUp();
            event.setCanceled(true);
            world.playSound(player, player.getPosition(), ModSounds.move, SoundCategory.MASTER, 1.0f, 1.0f);
        }

    }*/

    public static enum Keybinds {
        OPENMENU("key.kingdomkeys.openmenu", GLFW.GLFW_KEY_M), SCROLL_UP("key.kingdomkeys.scrollup",GLFW.GLFW_KEY_UP), SCROLL_DOWN("key.kingdomkeys.scrolldown", GLFW.GLFW_KEY_DOWN), ENTER("key.kingdomkeys.enter",GLFW.GLFW_KEY_RIGHT), BACK("key.kingdomkeys.back", GLFW.GLFW_KEY_LEFT), SCROLL_ACTIVATOR("key.kingdomkeys.scrollactivator",GLFW.GLFW_KEY_LEFT_ALT), SUMMON_KEYBLADE("key.kingdomkeys.summonkeyblade", GLFW.GLFW_KEY_G), LOCK_ON("key.kingdomkeys.lockon",GLFW.GLFW_KEY_Z), SHOW_GUI("key.kingdomkeys.showgui", GLFW.GLFW_KEY_O), ACTION("key.kingdomkeys.action",GLFW.GLFW_KEY_X);
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
}
