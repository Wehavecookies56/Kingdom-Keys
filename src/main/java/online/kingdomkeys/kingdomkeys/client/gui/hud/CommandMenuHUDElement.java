package online.kingdomkeys.kingdomkeys.client.gui.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.awt.Color;
import java.util.*;

public class CommandMenuHUDElement extends HUDElement {

    int currentPage = 0;
    int currentItem = 0;

    Map<Integer, Menu> menuItems = new HashMap<>();

    final ResourceLocation TEXTURE = new ResourceLocation(KingdomKeys.MODID, "textures/gui/commandmenu.png");

    final Color menuColour = new Color(10, 60, 255);
    final Color magicColour = new Color(100, 0, 255);

    final int MENU_HEIGHT = 15;
    final int MENU_WIDTH = 70;
    final int ICON_WIDTH = 10;

    public CommandMenuHUDElement(int anchoredPositionX, int anchoredPositionY, int elementWidth, int elementHeight, HUDAnchorPosition anchor, String name) {
        super(anchoredPositionX, anchoredPositionY, elementWidth, elementHeight, anchor, name);
    }

    public void moveUp() {

    }

    public void moveDown() {

    }

    public void enter() {

    }

    public void cancel() {

    }

    @Override
    public void initElement() {
        Menu main = new Menu(0,
                new MenuItem("Attack", menuColour) {
                    @Override
                    public void onUse() {
                        //NOTHING
                    }
                },
                new MenuItem("Magic", menuColour) {
                    @Override
                    public void onUse() {
                        //OPEN MAGIC SUB MENU
                    }
                },
                new MenuItem("Items", menuColour) {
                    @Override
                    public void onUse() {
                        //OPEN ITEMS SUB MENU
                    }
                },
                new MenuItem("Drive", menuColour) {
                    @Override
                    public void onUse() {
                        //OPEN DRIVE SUB MENU
                    }
                });
        Menu magic = new Menu(1, new MenuItem("Fire", magicColour) {
            @Override
            public void onUse() {
                //FIRE!
            }
        });

        Menu main2 = new Menu(0,
                new MenuItem("Attack", menuColour) {
                    @Override
                    public void onUse() {

                    }
                },
                new MenuItem("Summon", menuColour) {
                    @Override
                    public void onUse() {

                    }
                },
                new MenuItem("Party", menuColour) {
                    @Override
                    public void onUse() {

                    }
                },
                new MenuItem("Limit", menuColour) {
                    @Override
                    public void onUse() {

                    }
                });
        menuItems.put(0, main);
        menuItems.put(1, main2);

        setHeight(MENU_HEIGHT * menuItems.get(currentPage).menuItems.size());
        setWidth(MENU_WIDTH);

    }

    @Override
    public void drawElement() {
        drawString("COMMANDS", 0, 0, Color.WHITE);
        mcInstance.getTextureManager().bindTexture(TEXTURE);
        blit(0, 0, 0, 0, MENU_WIDTH, MENU_HEIGHT);
    }

    //Class for the menus e.g. the main one, submenus, 2nd page, etc.
    public static class Menu {

        //Which submenu it is in, 0 = no submenu
        int subMenuIndex;

        int itemSelected = 0;

        List<MenuItem> menuItems;

        public Menu(int subMenuIndex, MenuItem... items) {
            this.subMenuIndex = subMenuIndex;
            menuItems = Arrays.asList(items);
        }

        public void addItem(MenuItem item) {
            menuItems.add(item);
        }

    }

    public abstract static class MenuItem extends Screen {

        //Display name
        String name;

        Color colour;

        public MenuItem(String name, Color colour) {
            super(new TranslationTextComponent(""));
            this.name = name;
            this.colour = colour;
        }

        public void setColour(Color colour) {
            this.colour = colour;
        }

        /**
         * Do something when entering the menu item
         */
        public abstract void onUse();

    }
}
