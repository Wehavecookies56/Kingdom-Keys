package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.DoorData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class COMinimap extends OverlayBase {
    public static final IGuiOverlay INSTANCE = new COMinimap();

    public static List<RoomData> rooms = new ArrayList<>();

    private COMinimap() {
        super();
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int width, int height) {
        super.render(gui, guiGraphics, partialTick, width, height);
        if (!rooms.isEmpty()) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(200, 200, 0);
            guiGraphics.pose().scale(4, 4, 4);
            for (int i = 0; i < rooms.size(); i++) {
                RoomData roomData = rooms.get(i);
                guiGraphics.fill(-roomData.pos.getX()*2, -roomData.pos.getY()*2, (-roomData.pos.getX()*2) + 1, (-roomData.pos.getY()*2) + 1, Color.RED.getRGB());
                roomData.getDoors().forEach((direction, doorData) -> {
                    int offsetY = 0;
                    int offsetX = 0;
                    switch (direction) {
                        case NORTH -> offsetY = 1;
                        case SOUTH -> offsetY = -1;
                        case EAST -> offsetX = -1;
                        case WEST -> offsetX = 1;
                    }
                    int colour = doorData.isOpen() ? Color.GREEN.getRGB() : Color.YELLOW.getRGB();
                    guiGraphics.fill(-roomData.pos.getX()*2 - offsetX, -roomData.pos.getY()*2 - offsetY, (-roomData.pos.getX()*2) + 1 - offsetX, (-roomData.pos.getY()*2) + 1 - offsetY, colour);
                });
            }
            guiGraphics.pose().popPose();
        }
    }

}
