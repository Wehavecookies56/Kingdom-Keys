package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.math.Axis;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.DoorData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class COMinimap extends OverlayBase {
    public static final LayeredDraw.Layer INSTANCE = new COMinimap();

    public static List<RoomData> rooms = new ArrayList<>();

    private static final ResourceLocation ROOM_TEX = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/co/room.png");

    private COMinimap() {
        super();
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        super.render(guiGraphics, deltaTracker);

        if (rooms.isEmpty())
            return;

        int tileSize = 20;
        int originX = 200;
        int originY = 200;

        RoomData currentRoom = null;

        guiGraphics.pose().pushPose();
        {
            guiGraphics.pose().translate(originX, originY, 0);
            guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(45));

            for (RoomData roomData : rooms) {
                int x = -roomData.pos.x() * 2;
                int y = -roomData.pos.y() * 2;

                int px = x * tileSize;
                int py = y * tileSize;

                if (roomData.getGenerated() != null) {
                    if (minecraft.player.getX() >= roomData.getGenerated().getPosition().getX()
                            && minecraft.player.getX() < roomData.getGenerated().getPosition().getX() + 64
                            && minecraft.player.getZ() >= roomData.getGenerated().getPosition().getZ()
                            && minecraft.player.getZ() < roomData.getGenerated().getPosition().getZ() + 64) {

                        currentRoom = roomData;
                    }
                }

                //Room
                guiGraphics.blit(ROOM_TEX, px, py, tileSize, tileSize, 0, 0, 16, 16, 16, 16);

                //Connections
                for (Map.Entry<RoomDirection, DoorData> entry : roomData.getDoors().entrySet()) {
                    RoomDirection dir = entry.getKey();
                    DoorData data = entry.getValue();

                    if (data.getType() == DoorData.Type.NONE)
                        continue;

                    if (dir != RoomDirection.EAST && dir != RoomDirection.SOUTH)
                        continue;

                    int dx = 0;
                    int dy = 0;

                    switch (dir) {
                        case EAST -> dx = -1;
                        case SOUTH -> dy = -1;
                    }

                    RoomData neighbor = getRoomAt(roomData.pos.x() + dx, roomData.pos.y() + dy);
                    if (neighbor == null) continue;

                    //Door state
                    boolean open = false;

                    if (roomData.getGenerated() != null && neighbor.getGenerated() != null) {
                        CardDoorTileEntity te1 = roomData.getGenerated().getDoorTE(minecraft.level, dir);
                        CardDoorTileEntity te2 = neighbor.getGenerated().getDoorTE(minecraft.level, dir.opposite());

                        if (te1 != null && te1.isOpen())
                            open = true;
                        if (te2 != null && te2.isOpen())
                            open = true;
                    }

                    int color = open ? 0xFF00FF00 : 0xFFFFFF00;

                    int thickness = Math.max(2, tileSize / 5);

                    switch (dir) {
                        case EAST -> {
                            guiGraphics.fill(px + tileSize, py + tileSize / 2 - thickness / 2, px + tileSize * 2, py + tileSize / 2 + thickness / 2, color);
                        }

                        case SOUTH -> {
                            guiGraphics.fill(px + tileSize / 2 - thickness / 2, py + tileSize, px + tileSize / 2 + thickness / 2, py + tileSize * 2, color);
                        }
                    }
                }
            }

            // Keyblade icon
            if (currentRoom != null) {
                guiGraphics.pose().pushPose();

                int x = currentRoom.pos.x() * 2;
                int y = currentRoom.pos.y() * 2;

                int px = -x * tileSize;
                int py = -y * tileSize;

                guiGraphics.pose().translate(px + tileSize / 2f, py + tileSize / 2f, 0);

                float rotation = Mth.wrapDegrees(minecraft.player.getYRot() - 45);
                guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(rotation));

                float iconScale = 16F;
                guiGraphics.pose().scale(iconScale, iconScale, 1f);

                ClientUtils.drawItemAsIcon(new ItemStack(ModItems.kingdomKey.get()), guiGraphics.pose(), -8, -8, 1);

                guiGraphics.pose().popPose();
            }
        }
        guiGraphics.pose().popPose();
    }

    private RoomData getRoomAt(int x, int y) {
        for (RoomData r : rooms) {
            if (r.pos.x() == x && r.pos.y() == y) {
                return r;
            }
        }
        return null;
    }
}