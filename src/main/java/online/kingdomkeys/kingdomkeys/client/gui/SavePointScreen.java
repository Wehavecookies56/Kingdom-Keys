package online.kingdomkeys.kingdomkeys.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.SavePointBlock;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.EditBoxLength;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.entity.block.SavepointTileEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSCreateSavePoint;
import online.kingdomkeys.kingdomkeys.network.cts.CSSavePointTP;
import online.kingdomkeys.kingdomkeys.world.SavePointStorage;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Map;
import java.util.UUID;

public class SavePointScreen extends MenuBackground {

    SavepointTileEntity tileEntity;
    Map<UUID, SavePointStorage.SavePoint> savePoints;
    boolean create;

    private final int SAVE = 0;

    EditBoxLength nameField;
    MenuButton save;

    SavePointStorage.SavePointType type;

    public SavePointScreen(SavepointTileEntity tileEntity, Map<UUID, SavePointStorage.SavePoint> savePoints, boolean create) {
        super("", Color.green);
        this.tileEntity = tileEntity;
        type = ((SavePointBlock)tileEntity.getBlockState().getBlock()).getType();
        this.savePoints = savePoints;
        this.create = create;
    }

    public void updateSavePointsFromServer(boolean tileEntityExists, Map<UUID, SavePointStorage.SavePoint> savePoints) {
        if (!tileEntityExists) {
            onClose();
        }
        this.savePoints = savePoints;
        init();
        updateButtons();
    }

    @Override
    public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        super.render(gui, mouseX, mouseY, partialTicks);
    }

    private void action(int id) {
        switch (id) {
            case SAVE -> {
                if (!nameField.getValue().isEmpty()) {
                    Player player = Minecraft.getInstance().player;
                    PacketHandler.sendToServer(new CSCreateSavePoint(tileEntity, nameField.getValue(), player));
                    create = false;
                    nameField.visible = false;
                    save.visible = false;
                }
            }
        }
        updateButtons();
    }

    private void updateButtons() {
        if (create) {
            if (nameField.getValue().isEmpty()) {
                save.active = false;
            } else {
                save.active = true;
            }
        } else {

        }
    }

    @Override
    public void init() {
        super.init();
        if (create) {
            addRenderableWidget(nameField = new EditBoxLength(Minecraft.getInstance().font, 0, 0, 100, 20, 32, Component.empty()){
                @Override
                public boolean charTyped(char pCodePoint, int pModifiers) {
                    boolean b = super.charTyped(pCodePoint, pModifiers);
                    updateButtons();
                    return b;
                }

                @Override
                public void deleteChars(int pNum) {
                    super.deleteChars(pNum);
                    updateButtons();
                }
            });
            addRenderableWidget(save = new MenuButton(0, 40, 100, "Save", MenuButton.ButtonType.BUTTON, press -> action(SAVE)));
        } else {
            int index = 0;
            for (Map.Entry<UUID, SavePointStorage.SavePoint> entry : savePoints.entrySet()) {
                if (type == SavePointStorage.SavePointType.WARP || entry.getValue().dimension() == tileEntity.getLevel().dimension()) {
                    addRenderableWidget(new MenuButton(0, index + (24 * index), 100, entry.getValue().name(), MenuButton.ButtonType.BUTTON, press -> {
                        KingdomKeys.LOGGER.info("Teleport to save point at {}", entry.getValue().pos().toShortString());
                        PacketHandler.sendToServer(new CSSavePointTP(tileEntity.getID(), entry.getKey()));
                        onClose();
                    }));
                    index++;
                }
            }
        }
        updateButtons();
    }
}
