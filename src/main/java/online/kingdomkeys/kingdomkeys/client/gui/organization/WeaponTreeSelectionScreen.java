package online.kingdomkeys.kingdomkeys.client.gui.organization;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment.MenuEquipmentScreen;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

public class WeaponTreeSelectionScreen extends Screen {
    public WeaponTreeSelectionScreen() {
        super(Component.translatable(""));
    }

    public WeaponTreeSelectionScreen(Utils.OrgMember current) {
        super(Component.translatable(""));
        this.current = current;
    }
    Button cancel, next, prev, select;
    final int CANCEL = 2, NEXT = 3, PREV = 4, SELECT = 5;

    Utils.OrgMember current = Utils.OrgMember.XEMNAS;

    private final int icon_width = 56;
    private final int icon_height = 56;

    private final ResourceLocation[] icons = new ResourceLocation[] {
            new ResourceLocation(KingdomKeys.MODID, "textures/gui/org/xemnas_icons.png"),
            new ResourceLocation(KingdomKeys.MODID, "textures/gui/org/xigbar_icons.png"),
            new ResourceLocation(KingdomKeys.MODID, "textures/gui/org/xaldin_icons.png"),
            new ResourceLocation(KingdomKeys.MODID, "textures/gui/org/vexen_icons.png"),
            new ResourceLocation(KingdomKeys.MODID, "textures/gui/org/lexaeus_icons.png"),
            new ResourceLocation(KingdomKeys.MODID, "textures/gui/org/zexion_icons.png"),
            new ResourceLocation(KingdomKeys.MODID, "textures/gui/org/saix_icons.png"),
            new ResourceLocation(KingdomKeys.MODID, "textures/gui/org/axel_icons.png"),
            new ResourceLocation(KingdomKeys.MODID, "textures/gui/org/demyx_icons.png"),
            new ResourceLocation(KingdomKeys.MODID, "textures/gui/org/luxord_icons.png"),
            new ResourceLocation(KingdomKeys.MODID, "textures/gui/org/marluxia_icons.png"),
            new ResourceLocation(KingdomKeys.MODID, "textures/gui/org/larxene_icons.png"),
            new ResourceLocation(KingdomKeys.MODID, "textures/gui/org/roxas_icons.png")
    };

    private final int members = icons.length;

    private final ResourceLocation GLOW = new ResourceLocation(KingdomKeys.MODID, "textures/gui/org/glow.png");

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta)
    {
        if (delta > 0 && prev.visible)
        {
            actionPerformed(PREV);
            return true;
        }
        else if  (delta < 0 && next.visible)
        {
            actionPerformed(NEXT);
            return true;
        }

        return false;
    }

    @Override
    public void render(@NotNull GuiGraphics gui, int p_render_1_, int p_render_2_, float p_render_3_) {
        PoseStack matrixStack = gui.pose();
        renderBackground(gui);
        String name = "";
        String weapon = "";
        int weapon_w = 128;
        int weapon_h = 128;
        switch(current) {
            case XEMNAS:
                name = "I: Xemnas";
                weapon = "Ethereal Blades";
                weapon_w = 89;
                weapon_h = 111;
                break;
            case XIGBAR:
                name = "II: Xigbar";
                weapon = "Arrowguns";
                weapon_w = 89;
                weapon_h = 110;
                break;
            case XALDIN:
                name = "III: Xaldin";
                weapon = "Lances";
                weapon_w = 86;
                weapon_h = 109;
                break;
            case VEXEN:
                name = "IV: Vexen";
                weapon = "Shields";
                weapon_w = 90;
                weapon_h = 108;
                break;
            case LEXAEUS:
                name = "V: Lexaeus";
                weapon = "Axe Swords";
                weapon_w = 97;
                weapon_h = 108;
                break;
            case ZEXION:
                name = "VI: Zexion";
                weapon = "Lexicons";
                weapon_w = 89;
                weapon_h = 109;
                break;
            case SAIX:
                name = "VII: Saix";
                weapon = "Claymores";
                weapon_w = 96;
                weapon_h = 101;
                break;
            case AXEL:
                name = "VIII: Axel";
                weapon = "Chakrams";
                weapon_w = 103;
                weapon_h = 101;
                break;
            case DEMYX:
                name = "IX: Demyx";
                weapon = "Sitars";
                weapon_w = 85;
                weapon_h = 104;
                break;
            case LUXORD:
                name = "X: Luxord";
                weapon = "Cards";
                weapon_w = 104;
                weapon_h = 89;
                break;
            case MARLUXIA:
                name = "XI: Marluxia";
                weapon = "Scythes";
                weapon_w = 96;
                weapon_h = 107;
                break;
            case LARXENE:
                name = "XII: Larxene";
                weapon = "Knives";
                weapon_w = 106;
                weapon_h = 68;
                break;
            case ROXAS:
                name = "XIII: Roxas";
                weapon = "Keyblades";
                weapon_w = 103;
                weapon_h = 68;
                break;
        }
        renderBackground(gui);
        matrixStack.pushPose();
        RenderSystem.enableBlend();
        gui.blit(GLOW, (width / 2) - (256 / 2) - 5, (height / 2) - (256 / 2), 0, 0, 256, 256);
        matrixStack.popPose();
        matrixStack.pushPose();
        RenderSystem.enableBlend();
        gui.blit(icons[current.ordinal()-1], (width / 2) - (weapon_w / 2), (height / 2) - (weapon_h / 2), 56, 0, weapon_w, weapon_h);
        matrixStack.translate((width / 2) - (8) - 64, (height / 2) - 110, 0);
        matrixStack.scale(0.5F, 0.5F, 0.5F);
        gui.blit(icons[current.ordinal()-1], 0, 0, 0, 0, icon_width, icon_height);
        matrixStack.popPose();
        gui.drawString(font, name, ((width / 2) - (8) - 64) + 2 + icon_width / 2, (height / 2) - 110, 0xFFFFFF);
        gui.drawString(font, weapon, ((width / 2) - (8) - 64) + 2 + icon_width / 2, (height / 2) - 110 + font.lineHeight * 2, 0xFFFFFF);
        super.render(gui, p_render_1_, p_render_2_, p_render_3_);
    }

    @Override
    public void init() {
    	addRenderableWidget(cancel = Button.builder(Component.translatable("Back"), (e) -> {
    		actionPerformed(CANCEL);
		}).bounds(0, 0, 50, 20).build());
    	
        addRenderableWidget(next = Button.builder(Component.translatable(">"), (e) -> {
    		actionPerformed(NEXT);
		}).bounds(0, 0, 20, 20).build());
        
        addRenderableWidget(prev = Button.builder(Component.translatable("<"), (e) -> {
    		actionPerformed(PREV);
		}).bounds(0, 0, 20, 20).build());

        addRenderableWidget(select = Button.builder(Component.translatable("Select"), (e) -> {
    		actionPerformed(SELECT);
		}).bounds(0, 0, 50, 20).build());
        updateButtons();
        super.init();
    }

    public void actionPerformed(int ID) {
        switch (ID) {
            case NEXT:
                if (current == Utils.OrgMember.ROXAS) {
                    current = Utils.OrgMember.XEMNAS;
                } else {
                    current = Utils.OrgMember.values()[current.ordinal()+1];
                }
                //Go to the right
                break;
            case PREV:
                //Go to the left
                if (current == Utils.OrgMember.XEMNAS) {
                    current = Utils.OrgMember.ROXAS;
                } else {
                    current = Utils.OrgMember.values()[current.ordinal()-1];
                }
                break;
            case SELECT:
                //Select the current member
                Minecraft.getInstance().setScreen(new WeaponUnlockScreen(current));
                break;
            case CANCEL:
                //Go back
                Minecraft.getInstance().setScreen(new MenuEquipmentScreen());
                break;
        }
        updateButtons();
    }

    public void updateButtons() {
    	next.visible = true;
        next.setX((width / 2) - (next.getWidth() / 2) + 128);
        next.setY((height / 2) - (next.getHeight() / 2));
        prev.visible = true;
        prev.setX((width / 2) - (prev.getWidth() / 2) - 128);
        prev.setY((height / 2) - (prev.getHeight() / 2));
        select.visible = true;
        select.setX((width / 2) - (select.getWidth() / 2));
        select.setY((height / 2) - (select.getHeight() / 2) + 90);
        cancel.visible = true;
        cancel.setX((width / 2) - (select.getWidth() / 2));
        cancel.setY((height / 2) - (select.getHeight() / 2) + 115);
    }
}
