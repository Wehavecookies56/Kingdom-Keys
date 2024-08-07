package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomData;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
           // System.out.println("-----------");

            for (int i = 0; i < rooms.size(); i++) {
                RoomData roomData = rooms.get(i);
                int roomColor = Color.RED.getRGB();


                //Room render as red by default (blue if player is in it)
                guiGraphics.fill(-roomData.pos.getX() * 2, -roomData.pos.getY() * 2, (-roomData.pos.getX() * 2) + 1, (-roomData.pos.getY() * 2) + 1, roomColor);

                //Render player icon
                if (roomData.getGenerated() != null) {
                   // System.out.println(i+": "+roomData.getGenerated().position);
                    if (minecraft.player.getX() >= roomData.getGenerated().position.getX() && minecraft.player.getX() < roomData.getGenerated().position.getX() + 64 && minecraft.player.getZ() >= roomData.getGenerated().position.getZ() && minecraft.player.getZ() < roomData.getGenerated().position.getZ() +64) {
                        guiGraphics.pose().pushPose();
                        float rotationDegrees = Mth.wrapDegrees(minecraft.player.getYRot());
                        System.out.println(rotationDegrees);
                        guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(rotationDegrees));

                        ClientUtils.drawItemAsIcon(new ItemStack(ModItems.k111.get()), guiGraphics.pose(), 0,0,1);
                        guiGraphics.pose().popPose();
                    }
                }
                roomData.getDoors().forEach((direction, doorData) -> {
                    int offsetY = 0;
                    int offsetX = 0;
                    switch (direction) {
                        case NORTH -> offsetY = 1;
                        case SOUTH -> offsetY = -1;
                        case EAST -> offsetX = -1;
                        case WEST -> offsetX = 1;
                    }
                    //Offset color and fill
                    int colour = doorData.isOpen() ? Color.GREEN.getRGB() : Color.YELLOW.getRGB();
                    guiGraphics.fill(-roomData.pos.getX() * 2 - offsetX, -roomData.pos.getY() * 2 - offsetY, (-roomData.pos.getX() * 2) + 1 - offsetX, (-roomData.pos.getY() * 2) + 1 - offsetY, colour);
                });
            }
            guiGraphics.pose().popPose();
        }
    }

}
