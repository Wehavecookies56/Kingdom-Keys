package online.kingdomkeys.kingdomkeys.client.gui.organization;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.Lists;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSUnlockEquipOrgWeapon;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class WeaponUnlockScreen extends Screen {

    IPlayerCapabilities playerData;
    Utils.OrgMember member;

    public WeaponUnlockScreen(Utils.OrgMember member) {
        super(new TranslationTextComponent(""));
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
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        int cost = (int) (startCost + ((0.1 * startCost) * current));
        renderBackground();
        String name = "";
        String weapon = "";
        int weapon_w = 128;
        int weapon_h = 128;
        renderBackground();
        RenderSystem.pushMatrix();
        Minecraft.getInstance().getTextureManager().bindTexture(GLOW);
        RenderSystem.enableBlend();
        blit((width / 2) - (256 / 2) - 5, (height / 2) - (256 / 2), 0, 0, 256, 256);
        drawString(font, new ItemStack(weapons.get(current)).getDisplayName().getString(), (width / 2) - (256 / 2) - 5, (height / 2) - 120, 0xFFFFFF);
        drawString(font, "Hearts Cost: " + cost, (width / 2) - (256 / 2) - 5, (height / 2) - 110, 0xFF0000);
        drawString(font, "Current Hearts: " + playerData.getHearts(), (width / 2) - (256 / 2) - 5, (height / 2) - 100, 0xFF0000);
        RenderSystem.popMatrix();
        RenderSystem.pushMatrix();
        RenderSystem.translatef((width / 2) - (256 / 2) - 5 + 94, (height / 2) - (256 / 2) + 88, 0);
        RenderSystem.scalef(5, 5, 5);
        RenderSystem.enableBlend();
        RenderSystem.color3f(0, 0, 0);
        Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(new ItemStack(weapons.get(current)), 0, 0);
        RenderSystem.popMatrix();
        super.render(p_render_1_, p_render_2_, p_render_3_);
    }

    @Override
    protected void init() {
        super.init();
        addButton(cancel = new Button(0, 0, 50, 20, "Back", p -> actionPerformed(CANCEL)));
        addButton(next = new Button(0, 0, 20, 20, ">", p -> actionPerformed(NEXT)));
        addButton(prev = new Button(0, 0, 20, 20, "<", p -> actionPerformed(PREV)));
        addButton(select = new Button(0, 0, 50, 20, "Unlock", p -> actionPerformed(SELECT)));
        updateButtons();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    void actionPerformed(int ID) {
        switch (ID) {
            case CANCEL:
                Minecraft.getInstance().displayGuiScreen(new WeaponTreeSelectionScreen(member));
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
                if (unlock) {
                    playerData.unlockWeapon(weapon);
                    int cost = (int) (startCost + ((0.1 * startCost) * current));
                    playerData.removeHearts(cost);
                    PacketHandler.sendToServer(new CSUnlockEquipOrgWeapon(weapon, cost));
                    //Take hearts
                    //Send packet
                } else {
                    playerData.equipWeapon(weapon);
                    PacketHandler.sendToServer(new CSUnlockEquipOrgWeapon(weapon));
                    //Send packet
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
            select.setMessage("Equip");
            if (playerData.getEquippedWeapon().getItem() == weapons.get(current)) {
                select.active = false;
                select.setMessage("Equipped");
            } else {
                select.active = true;
                select.setMessage("Equip");
            }
        } else {
            unlock = true;
            select.setMessage("Unlock");
            if (canUnlock()) {
                select.active = true;
            } else {
                select.active = false;
            }
        }
        next.visible = true;
        next.x = (width / 2) - (next.getWidth() / 2) + 128;
        next.y = (height / 2) - (next.getHeight() / 2);
        prev.visible = true;
        prev.x = (width / 2) - (prev.getWidth() / 2) - 128;
        prev.y = (height / 2) - (prev.getHeight() / 2);
        select.visible = true;
        select.x = (width / 2) - (select.getWidth() / 2);
        select.y = (height / 2) - (select.getHeight() / 2) + 90;
        cancel.visible = true;
        cancel.x = (width / 2) - (select.getWidth() / 2);
        cancel.y = (height / 2) - (select.getHeight() / 2) + 115;
    }
}
