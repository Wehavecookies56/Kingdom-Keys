package online.kingdomkeys.kingdomkeys.client.gui.organization;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.Lists;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSummonKeyblade;
import online.kingdomkeys.kingdomkeys.network.cts.CSUnlockEquipOrgWeapon;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class WeaponUnlockScreen extends Screen {

    IPlayerCapabilities playerData;
    Utils.OrgMember member;

    public WeaponUnlockScreen(Utils.OrgMember member) {
        super(Component.translatable(""));
        this.member = member;
        this.weapons = Lists.getListForMember(member);
        playerData = ModCapabilities.getPlayer(Minecraft.getInstance().player);
    }

    Button cancel, next, prev, select;
    final int CANCEL = 2, NEXT = 3, PREV = 4, SELECT = 5;

    List<Item> weapons;
    int current = 0;

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
        int cost = (int) (startCost + ((0.1 * startCost) * current));
        renderBackground(gui);
        String name = "";
        String weapon = "";
        int weapon_w = 128;
        int weapon_h = 128;
        renderBackground(gui);
        matrixStack.pushPose();
        RenderSystem.enableBlend();
        gui.blit(GLOW, (width / 2) - (256 / 2) - 5, (height / 2) - (256 / 2), 0, 0, 256, 256);
        gui.drawString(font, new ItemStack(weapons.get(current)).getHoverName().getString(), (width / 2) - (256 / 2) - 5, (height / 2) - 120, 0xFFFFFF);
        gui.drawString(font, "Hearts Cost: " + cost, (width / 2) - (256 / 2) - 5, (height / 2) - 110, 0xFF0000);
        gui.drawString(font, "Current Hearts: " + playerData.getHearts(), (width / 2) - (256 / 2) - 5, (height / 2) - 100, 0xFF0000);
        matrixStack.popPose();
        matrixStack.pushPose();
        matrixStack.translate((width / 2) - (256 / 2) - 5 + 94, (height / 2) - (256 / 2) + 88, 0);
        matrixStack.scale(5, 5, 5);
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(0, 0, 0, 1);
        ClientUtils.drawItemAsIcon(new ItemStack(weapons.get(current)), matrixStack, 0,0,16);

        matrixStack.popPose();
        super.render(gui, p_render_1_, p_render_2_, p_render_3_);
    }

    @Override
    protected void init() {
        super.init();
        
        addRenderableWidget(cancel = Button.builder(Component.translatable("Back"), (e) -> {
    		actionPerformed(CANCEL);
		}).bounds(0, 0, 50, 20).build());
        addRenderableWidget(next = Button.builder(Component.translatable(">"), (e) -> {
    		actionPerformed(NEXT);
		}).bounds(0, 0, 20, 20).build());
        
        addRenderableWidget(prev = Button.builder(Component.translatable("<"), (e) -> {
    		actionPerformed(PREV);
		}).bounds(0, 0, 20, 20).build());

        addRenderableWidget(select = Button.builder(Component.translatable("Unlock"), (e) -> {
    		actionPerformed(SELECT);
		}).bounds(0, 0, 50, 20).build());
        updateButtons();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    void actionPerformed(int ID) {
        switch (ID) {
            case CANCEL:
                Minecraft.getInstance().setScreen(new WeaponTreeSelectionScreen(member));
                //Go back
                break;
            case NEXT:
                if (current == weapons.size()-1) {
                    current = 0;
                } else {
                    current++;
                }
                //Go to the right
                break;
            case PREV:
                //Go to the left
                if (current == 0) {
                    current = weapons.size()-1;
                } else {
                    current--;
                }
                break;
            case SELECT:
                //Select the current member
                ItemStack weapon = new ItemStack(weapons.get(current));
                playerData.getWeaponsUnlocked().forEach(itemStack -> {
                    if (itemStack.is(weapon.getItem())) {
                        weapon.setTag(itemStack.getTag());
                    }
                });
                if (unlock) {
                    playerData.unlockWeapon(weapon);
                    int cost = (int) (startCost + ((0.1 * startCost) * current));
                    playerData.removeHearts(cost);
                    PacketHandler.sendToServer(new CSUnlockEquipOrgWeapon(weapon, cost));
                } else {
                    playerData.equipWeapon(weapon);
					if(Utils.findSummoned(minecraft.player.getInventory(), playerData.getEquippedWeapon()) > -1)
						PacketHandler.sendToServer(new CSSummonKeyblade(true));
                    
					PacketHandler.sendToServer(new CSUnlockEquipOrgWeapon(weapon));
                }
                break;
        }
        updateButtons();
    }

    //TODO make this configurable
    int startCost = 1000;

    public Item getStarterWeapon(Utils.OrgMember member) {
        switch(member) {
            case XEMNAS:
                return ModItems.malice.get();
            case XIGBAR:
                return ModItems.standalone.get();
            case XALDIN:
                return ModItems.zephyr.get();
            case VEXEN:
                return ModItems.testerZero.get();
            case LEXAEUS:
                return ModItems.reticence.get();
            case ZEXION:
                return ModItems.blackPrimer.get();
            case SAIX:
                return ModItems.newMoon.get();
            case AXEL:
                return ModItems.ashes.get();
            case DEMYX:
                return ModItems.basicModel.get();
            case LUXORD:
                return ModItems.theFool.get();
            case MARLUXIA:
                return ModItems.fickleErica.get();
            case LARXENE:
                return ModItems.trancheuse.get();
            case ROXAS:
                return ModItems.kingdomKey.get();
        }
        return null;
    }

    public boolean canUnlock() {
        int cost = (int) (startCost + ((0.1 * startCost) * current));
        if (playerData.getHearts() >= cost) {
            if (current == 0) {
                Utils.OrgMember leftMember;
                Utils.OrgMember rightMember;
                if (member == Utils.OrgMember.ROXAS) {
                    rightMember = Utils.OrgMember.XEMNAS;
                } else {
                    rightMember = Utils.OrgMember.values()[member.ordinal() + 1];
                }
                if (member == Utils.OrgMember.XEMNAS) {
                    leftMember = Utils.OrgMember.ROXAS;
                } else {
                    leftMember = Utils.OrgMember.values()[member.ordinal() - 1];
                }
                if (playerData.isWeaponUnlocked(getStarterWeapon(leftMember))) {
                    return true;
                }
                if (playerData.isWeaponUnlocked(getStarterWeapon(rightMember))) {
                    return true;
                }
                return false;
            } else {
                if (playerData.isWeaponUnlocked(weapons.get(current - 1))) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    boolean unlock = true;

    public void updateButtons() {
        if (playerData.isWeaponUnlocked(weapons.get(current))) {
            unlock = false;
            select.setMessage(Component.translatable("Equip"));
            if (playerData.getEquippedWeapon().getItem() == weapons.get(current)) {
                select.active = false;
                select.setMessage(Component.translatable("Equipped"));
            } else {
                select.active = true;
                select.setMessage(Component.translatable("Equip"));
            }
        } else {
            unlock = true;
            select.setMessage(Component.translatable("Unlock"));
            if (canUnlock()) {
                select.active = true;
            } else {
                select.active = false;
            }
        }
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
