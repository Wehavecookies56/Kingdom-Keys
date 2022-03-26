package online.kingdomkeys.kingdomkeys.client.gui.hud;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

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
        if (currentItem - 1 == 0) {
            currentItem = menuItems.get(currentPage).menuItems.size()-1;
        } else currentItem--;
    }

    public void moveDown() {
        if (currentItem + 1 == menuItems.get(currentPage).menuItems.size()) {
            currentItem = 0;
        } else currentItem++;
    }

    public void enter() {
        menuItems.get(currentPage).menuItems.get(currentItem).onUse();
    }

    public void cancel() {
        currentPage = 0;
    }

    public void changePage(int subMenu) {
        currentPage = subMenu;
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
                        changePage(1);
                    }
                },
                new MenuItem("Items", menuColour) {
                    @Override
                    public void onUse() {
                        changePage(2);
                    }
                },
                new MenuItem("Drive", menuColour) {
                    @Override
                    public void onUse() {
                        changePage(3);
                    }
                }
        );
        //Gonna need to populate these dynamically
        Menu magic = new Menu(1, new MenuItem("Fire", magicColour) {
            @Override
            public void onUse() {
                //FIRE!
                changePage(0);
            }
        });

        Menu items = new Menu(2, (MenuItem) null);
        Menu drive = new Menu(3, (MenuItem) null);

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
                }
        );

        menuItems.put(0, main);
        menuItems.put(1, main2);

        setHeight(MENU_HEIGHT * menuItems.get(currentPage).menuItems.size());
        setWidth(MENU_WIDTH);

    }

    @Override
    public void tick() {
        if (currentTickPos > 200) currentTickPos = 0;
        previousTickPos = currentTickPos;
        currentTickPos += 2;
    }

    @Override
    public void drawElement(PoseStack matrixStack, float partialTicks) {
        mcInstance.getTextureManager().bindForSetup(TEXTURE);
        blit(matrixStack, 0, 0, 0, 0, MENU_WIDTH, MENU_HEIGHT);
        drawString(matrixStack, "COMMANDS", 0, 0, Color.WHITE);
        float lerpedPos = previousTickPos + (currentTickPos - previousTickPos) * partialTicks;
        drawString(matrixStack, "TEST", Math.round(lerpedPos), 0, Color.RED);
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    //Class for the menus e.g. the main one, submenus, 2nd page, etc.
    public static class Menu {

        //Which submenu it is in, 0 = no submenu
        int subMenuIndex;

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
            super(new TranslatableComponent(""));
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

        public void draw(float partialTicks) {

        }

    }
}
