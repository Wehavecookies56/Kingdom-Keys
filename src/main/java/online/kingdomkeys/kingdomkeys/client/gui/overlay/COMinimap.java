package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.menu.MenuScreen;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomTypes;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.DoorData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomDirection;

import java.util.Map;

public class COMinimap extends OverlayBase {
    public static final LayeredDraw.Layer INSTANCE = new COMinimap();

    private static final ResourceLocation ROOM_TEX = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/co/room.png");

    public RoomData currentRoom = null;

    private COMinimap() {
        super();
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        super.render(guiGraphics, deltaTracker);

        if (MenuScreen.rooms.isEmpty())
            return;

        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        for (RoomData roomData : MenuScreen.rooms) {
            if (roomData.getGenerated().isPresent()) {
                if (roomData.getGenerated().get().inRoom(minecraft.player.blockPosition())) {
                    currentRoom = roomData;
                }
            }
        }

        ClientUtils.MINIMAP_ELEMENT.applyTransform(guiGraphics,screenWidth,screenHeight);
        guiGraphics.pose().translate(50.1,20,0);
        guiGraphics.pose().scale(0.7F,0.7F,1);
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1,1,1,1);
        renderMinimap(guiGraphics, deltaTracker);

        RenderSystem.disableBlend();

        ClientUtils.MINIMAP_ELEMENT.endTransform(guiGraphics);

        if (currentRoom != null) {
            MutableComponent roomName = currentRoom.getGenerated().map(room -> room.getType().getName(currentRoom).withStyle(ClientUtils.KK_Font_MENU)).orElse(Component.literal("???").withStyle(ClientUtils.KK_Font_MENU));
            ClientUtils.ROOMNAME_ELEMENT.applyTransform(guiGraphics, screenWidth, screenHeight);
            guiGraphics.drawString(Minecraft.getInstance().font, roomName, 0, 0, 0xFFFFFF);
            ClientUtils.ROOMNAME_ELEMENT.endTransform(guiGraphics);
        }
    }

    private void renderMinimap(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        int tileSize = 60;

        guiGraphics.pose().pushPose();
        {
            guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(45));

            if(currentRoom == null || currentRoom.getGenerated().get().getType().isEntranceHall()){
                guiGraphics.pose().popPose();
                return;
            }


            guiGraphics.blit(ROOM_TEX, 0,0, tileSize, tileSize, 0, 0, 16, 16, 16, 16);
            guiGraphics.setColor(1, 1, 1, 1);


            for (Map.Entry<RoomDirection, DoorData> entry : currentRoom.getDoors().entrySet()) {
                RoomDirection dir = entry.getKey();
                DoorData data = entry.getValue();

                if (data.getType() == DoorData.Type.NONE)
                    continue;

                int dx = 0;
                int dy = 0;

                switch (dir) {
                    case EAST -> dx = -1;
                    case WEST -> dx = 1;
                    case SOUTH -> dy = -1;
                    case NORTH -> dy = 1;
                }

                RoomData neighbor = getRoomAt(
                        currentRoom.pos.x() + dx,
                        currentRoom.pos.y() + dy
                );

                if (neighbor == null)
                    continue;

                boolean open = false;

                if (currentRoom.getGenerated().isPresent() && neighbor.getGenerated().isPresent()) {

                    CardDoorTileEntity te1 = currentRoom.getGenerated().get().getDoorTE(minecraft.level, dir);

                    CardDoorTileEntity te2 = neighbor.getGenerated().get().getDoorTE(minecraft.level, dir.opposite());

                    if (te1 != null && te1.isOpen())
                        open = true;
                    if (te2 != null && te2.isOpen())
                        open = true;
                }

                int color = open ? 0xFF00FF00 : 0xFFFFFF00;
                int thickness = Math.max(2, tileSize / 5);

                switch (dir) {
                    case EAST -> fillSafe(guiGraphics, tileSize, -thickness / 2 + tileSize / 2, tileSize * 2, thickness / 2 + tileSize / 2, color);
                    case WEST -> fillSafe(guiGraphics, -tileSize, -thickness / 2 + tileSize / 2, 0, thickness / 2 + tileSize / 2, color);
                    case SOUTH -> fillSafe(guiGraphics, -thickness / 2 + tileSize / 2, tileSize, thickness / 2 + tileSize / 2, tileSize*2, color);
                    case NORTH -> fillSafe(guiGraphics, -thickness / 2 + tileSize / 2, -tileSize, thickness / 2 + tileSize / 2, 0, color);
                }
            }

            // Keyblade icon
            drawKeybladeIcon(tileSize);

        }
        guiGraphics.pose().popPose();
    }

    public void drawKeybladeIcon(float tileSize) {
        guiGraphics.pose().pushPose();
        {
            guiGraphics.pose().translate(tileSize / 2f, tileSize / 2f, 0);

            float rotation = Mth.wrapDegrees(Minecraft.getInstance().player.getYRot() - 45);
            guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(rotation));

            float iconScale = tileSize * 0.8F;
            guiGraphics.pose().scale(iconScale, iconScale, 1f);

            PlayerData playerData = PlayerData.get(Minecraft.getInstance().player);
            ItemStack stack = playerData.getEquippedKeychain(DriveForm.NONE);
            ItemStack item = stack;
            if(stack.isEmpty()) {
                item = new ItemStack(ModItems.k111.get());
            } else if (stack.getItem() instanceof IKeychain kc) {
                item = new ItemStack(kc.toSummon());
            }
            ClientUtils.drawItemAsIcon(item, guiGraphics.pose(), -8, -8, 1);
        }
        guiGraphics.pose().popPose();

    }

    private void fillSafe(GuiGraphics g, int x1, int y1, int x2, int y2, int color) {
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);

        g.fill(minX, minY, maxX, maxY, color);
    }

    private RoomData getRoomAt(int x, int y) {
        for (RoomData r : MenuScreen.rooms) {
            if (r.pos.x() == x && r.pos.y() == y) {
                return r;
            }
        }
        return null;
    }
}