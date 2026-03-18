package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.math.Axis;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.DoorData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomData;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class COMinimap extends OverlayBase {
    public static final LayeredDraw.Layer INSTANCE = new COMinimap();

    public static List<RoomData> rooms = new ArrayList<>();

    private COMinimap() {
        super();
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        super.render(guiGraphics, deltaTracker);

        if (rooms.isEmpty()) return;

        int tileSize = 10;
        int thickness = 2;

        int originX = 200;
        int originY = 200;

        RoomData currentRoom = null;

        guiGraphics.pose().pushPose();
        {
            guiGraphics.pose().translate(originX, originY, 0);

            for (RoomData roomData : rooms) {
                int x = -roomData.pos.x() * 2;
                int y = -roomData.pos.y() * 2;

                int px = x * tileSize;
                int py = y * tileSize;

                // Room
                guiGraphics.fill(px, py, px + tileSize, py + tileSize, Color.RED.getRGB());

                // Detect current room
                if (roomData.getGenerated() != null) {
                    if (minecraft.player.getX() >= roomData.getGenerated().getPosition().getX() && minecraft.player.getX() < roomData.getGenerated().getPosition().getX() + 64 && minecraft.player.getZ() >= roomData.getGenerated().getPosition().getZ() && minecraft.player.getZ() < roomData.getGenerated().getPosition().getZ() + 64) {
                        currentRoom = roomData;
                    }
                }

                // Doors
                roomData.getDoors().forEach((direction, doorData) -> {
                    if (doorData.getType() != DoorData.Type.NONE) {
                        boolean open = false;

                        if (roomData.getGenerated() != null) {
                            CardDoorTileEntity te = roomData.getGenerated().getDoorTE(minecraft.level, direction);
                            if (te != null)
                                open = te.isOpen();
                        }

                        int color = open ? Color.GREEN.getRGB() : Color.YELLOW.getRGB();

                        switch (direction) {
                            case SOUTH ->
                                    guiGraphics.fill(px + 2, py + tileSize, px + tileSize - 2, py + tileSize + thickness, color);
                            case NORTH ->
                                    guiGraphics.fill(px + 2, py - thickness, px + tileSize - 2, py, color);
                            case EAST ->
                                    guiGraphics.fill(px + tileSize, py + 2, px + tileSize + thickness, py + tileSize - 2, color);
                            case WEST ->
                                    guiGraphics.fill(px - thickness, py + 2, px, py + tileSize - 2, color);
                        }
                    }
                });
            }

            //Keyblade icon
            if (currentRoom != null) {
                guiGraphics.pose().pushPose();

                int x = currentRoom.pos.x() * 2;
                int y = currentRoom.pos.y() * 2;

                int px = -x * tileSize;
                int py = -y * tileSize;

                guiGraphics.pose().translate(px + tileSize / 2f, py + tileSize / 2f, 0);


                float rotation = Mth.wrapDegrees(minecraft.player.getYRot());
                guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(rotation));
                guiGraphics.pose().scale(6f, 6f, 1f);
                ClientUtils.drawItemAsIcon(new ItemStack(ModItems.k111.get()), guiGraphics.pose(), -8, -8, 1);
                guiGraphics.pose().popPose();
            }
        }
        guiGraphics.pose().popPose();
    }
}