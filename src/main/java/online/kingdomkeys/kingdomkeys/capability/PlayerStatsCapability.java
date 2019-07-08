/*
package online.kingdomkeys.kingdomkeys.capability;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.fml.network.PacketDispatcher;
import net.minecraftforge.items.ItemStackHandler;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import uk.co.wehavecookies56.kk.common.ability.ModAbilities;
import uk.co.wehavecookies56.kk.common.container.inventory.InventoryEquipmentMenu;
import uk.co.wehavecookies56.kk.common.container.inventory.InventoryPotionsMenu;
import uk.co.wehavecookies56.kk.common.network.packet.client.ShowOverlayPacket;
import uk.co.wehavecookies56.kk.common.network.packet.client.SyncLevelData;
import uk.co.wehavecookies56.kk.common.network.packet.client.SyncOrgXIIIData;
import uk.co.wehavecookies56.kk.common.network.packet.client.SyncUnlockedAbilities;

public class PlayerStatsCapability {

    List<String> messages = new ArrayList<String>();

    public interface IPlayerStats {
        List<String> getMessages();
        int getExperience();

        boolean enderDragonDefeated();

        int getLevel();
        int getMaxLevel();
        int getMaxExperience();
        int getStrength();
        int getDefense();
        int getMagic();
        int getHP();
        int getConsumedAP();
        int getMaxAP();

        double getMP();
        double getMaxMP();
        boolean getRecharge();

        String getChoice1();
        String getChoice2();

        boolean setLevel(int level);
        boolean setExperience(int experience);
        void addExperience(EntityPlayer player, int amount);
        void setStrength(int strength);
        void addStrength(int strength);
        void setDefense(int defense);
        void addDefense(int defense);
        void setMagic(int magic);
        void addMagic(int magic);
        int setHP(int hp);
        int addHP(int hp);
        int setConsumedAP(int ap);
        int addConsumedAP(int ap);
        int setMaxAP(int ap);
        int addMaxAP(int ap);

        boolean setMP(double mp);
        void addMP(double mp);
        void remMP(double mp);
        void setMaxMP(double mp);
        double addMaxMP(double mp);

        void setRecharge(boolean recharge);

        void setEnderDragonDefeated(boolean defeated);

        void setChoice1(String choice);
        void setChoice2(String choice);

        ItemStackHandler getInventoryPotionsMenu();
        ItemStackHandler getInventoryEquipmentMenu();

        boolean getHudMode();
        void setHudMode(boolean mode);
        int getExpNeeded(int level, int experience);
        void levelUpStatsAndDisplayMessage(EntityPlayer player);
        void clearMessages();

        void setRechargeSpeed(double amount);
        double getRechargeSpeed();
        void setPotionsInventory(ItemStackHandler handler);
        void setEquipmentInventory(ItemStackHandler handler);
    }

    public static class Storage implements IStorage<IPlayerStats> {

        @Override
        public INBTBase writeNBT(Capability<IPlayerStats> capability, IPlayerStats instance, EnumFacing side) {
            NBTTagCompound properties = new NBTTagCompound();
            properties.setInt("Level", instance.getLevel());
            properties.setInt("Experience", instance.getExperience());
            properties.setInt("Strength", instance.getStrength());
            properties.setInt("Defense", instance.getDefense());
            properties.setInt("Magic", instance.getMagic());
            properties.setInt("HP", instance.getHP());
            properties.setDouble("MP", instance.getMP());
            properties.setDouble("Max MP", instance.getMaxMP());
            properties.setBoolean("Recharge", instance.getRecharge());
            properties.setBoolean("Ender Dragon Defeated", instance.enderDragonDefeated());

            properties.setBoolean("HUD", instance.getHudMode());

            properties.setString("Choice1", instance.getChoice1());
            properties.setString("Choice2", instance.getChoice2());
            properties.setTag("PotionsInvKey", instance.getInventoryPotionsMenu().serializeNBT());
            properties.setDouble("RechargeSpeed", instance.getRechargeSpeed());
            properties.setInt("AP", instance.getConsumedAP());
            properties.setInt("MaxAP", instance.getMaxAP());

            return properties;
        }

        @Override
        public void readNBT(Capability<IPlayerStats> capability, IPlayerStats instance, EnumFacing side, INBTBase nbt) {
            NBTTagCompound properties = (NBTTagCompound) nbt;
            instance.setLevel(properties.getInt("Level"));
            instance.setExperience(properties.getInt("Experience"));
            instance.setStrength(properties.getInt("Strength"));
            instance.setDefense(properties.getInt("Defense"));
            instance.setMagic(properties.getInt("Magic"));
            instance.setHP(properties.getInt("HP"));
            instance.setMP(properties.getDouble("MP"));
            instance.setMaxMP(properties.getDouble("Max MP"));
            instance.setRecharge(properties.getBoolean("Recharge"));
            instance.setEnderDragonDefeated(properties.getBoolean("Ender Dragon Defeated"));

            instance.setHudMode(properties.getBoolean("HUD"));

            instance.setChoice1(properties.getString("Choice1"));
            instance.setChoice2(properties.getString("Choice2"));
            instance.getInventoryPotionsMenu().deserializeNBT(properties.getCompound("PotionsInvKey"));

            instance.setRechargeSpeed(properties.getDouble("RechargeSpeed"));
            instance.setConsumedAP(properties.getInt("AP"));
            instance.setMaxAP(properties.getInt("MaxAP"));
        }
    }

    public static class Default implements IPlayerStats {
        private int level = 1;
        private int maxLevel = 100;
        private int experience = 0;
        private int maxExperience = Integer.MAX_VALUE;
        private int valorMaxExperience = Integer.MAX_VALUE;
        private int wisdomMaxExperience = Integer.MAX_VALUE;
        private int limitMaxExperience = Integer.MAX_VALUE;
        private int masterMaxExperience = Integer.MAX_VALUE;
        private int finalMaxExperience = Integer.MAX_VALUE;
        private int strength = 1;
        private int defense = 1;
        private int magic = 1;
        private int maxHP = 20;
        private double mp = 20;
        private double maxMP = 40;
        private int consumedAP=0;
        private int maxAP = 10;

        private boolean recharge = false;
        private double rechargeSpeed = 1;
        private boolean cheatMode = false;
        private boolean enderDragonDefeated = false;
        private boolean hudmode = true;
        private int remainingExp = 0;
        private List<String> messages = new ArrayList<String>();

        private String choice1="", choice2="";

        private ItemStackHandler inventoryPotions = new ItemStackHandler(InventoryPotionsMenu.INV_SIZE);
        private ItemStackHandler inventoryEquipment = new ItemStackHandler(InventoryEquipmentMenu.INV_SIZE);

        @Override
        public boolean enderDragonDefeated() {
            return this.enderDragonDefeated;
        }

        @Override
        public void setEnderDragonDefeated(boolean defeated) {
            this.enderDragonDefeated = defeated;
        }

        @Override
        public List<String> getMessages() {
            return this.messages;
        }

        @Override
        public ItemStackHandler getInventoryPotionsMenu(){
            return this.inventoryPotions;
        }

        @Override
        public double getMP() {
            return this.mp;
        }
        @Override
        public double getMaxMP() {
            return this.maxMP;
        }
        @Override
        public int getLevel() {
            return this.level;
        }
        @Override
        public int getMaxLevel() {
            return this.maxLevel;
        }
        @Override
        public int getExperience() {
            return this.experience;
        }
        @Override
        public int getMaxExperience() {
            return this.maxExperience;
        }
        @Override
        public int getStrength() {
            return this.strength;
        }
        @Override
        public int getDefense() {
            return this.defense;
        }
        @Override
        public int getMagic() {
            return this.magic;
        }
        @Override
        public int getHP() {
            return this.maxHP;
        }
        @Override
        public int getConsumedAP() {
            return this.consumedAP;
        }
        @Override
        public int getMaxAP() {
            return this.maxAP;
        }
        @Override
        public boolean getRecharge() {
            return this.recharge;
        }
        @Override
        public boolean getHudMode() {
            return this.hudmode;
        }

        @Override
        public String getChoice1() {
            return this.choice1;
        }
        @Override
        public String getChoice2() {
            return this.choice2;
        }

        @Override
        public boolean setLevel(int level) {
            if (level <= this.maxLevel) {
                this.level = level;
                return true;
            }
            return false;
        }

        @Override
        public void clearMessages() {
            this.getMessages().clear();
        }
        @Override
        public boolean setExperience(int experience) {
            if (experience <= this.maxExperience) {
                this.experience = experience;
                return true;
            } return false;
        }

        @Override
        public void addExperience(EntityPlayer player, int amount) {
            if(player != null) {
                IPlayerStats stats = player.getCapability(ModCapabilities.PLAYER_STATS, null);
                if (this.experience + amount <= this.maxExperience){
                    this.experience += amount;
                    while (this.getExpNeeded(this.getLevel(), this.experience) <= 0 && this.getLevel() != 100) {
                        this.setLevel(this.getLevel() + 1);
                        this.levelUpStatsAndDisplayMessage(player);
                        PacketDispatcher.sendTo(new ShowOverlayPacket("levelup"),(EntityPlayerMP)player);
                    }
                }else {
                    this.experience = this.maxExperience;
                }
                PacketDispatcher.sendTo(new ShowOverlayPacket("exp"),(EntityPlayerMP)player);
            }
        }
        @Override
        public void setStrength(int strength) {
            this.strength = strength;
        }
        @Override
        public void addStrength(int strength) {
            this.strength += strength;
            messages.add(Strings.Stats_LevelUp_Str);
        }
        @Override
        public void setDefense(int defense) {
            this.defense = defense;
        }
        @Override
        public void addDefense(int defense) {
            this.defense += defense;
            messages.add(Strings.Stats_LevelUp_Def);
        }
        @Override
        public void setMagic(int magic) {
            this.magic = magic;
        }
        @Override
        public void addMagic(int magic) {
            this.magic += magic;
            messages.add(Strings.Stats_LevelUp_Magic);
        }
        @Override
        public int setHP(int hp) {
            this.maxHP = hp;
            return this.maxHP;
        }
        @Override
        public int addHP(int hp) {
            this.maxHP += hp;
            messages.add(Strings.Stats_LevelUp_HP);
            return this.maxHP;
        }
        @Override
        public int setConsumedAP(int ap) {
            this.consumedAP = ap;
            return this.consumedAP;
        }
        @Override
        public int addConsumedAP(int ap) {
            this.consumedAP += ap;
            // messages.add(Strings.Stats_LevelUp_AP);
            return this.consumedAP;
        }
        @Override
        public int setMaxAP(int ap) {
            this.maxAP = ap;
            return this.maxAP;
        }
        @Override
        public int addMaxAP(int ap) {
            this.maxAP += ap;
            messages.add(Strings.Stats_LevelUp_AP);
            return this.maxAP;
        }

        @Override
        public boolean setMP(double mp) {
            if (mp <= this.maxMP) {
                this.mp = mp;
                return true;
            }
            return false;
        }
        @Override
        public void addMP(double mp) {
            if (mp + this.mp > this.maxMP)
                this.mp = this.maxMP;
            else
                this.mp += mp;
        }
        @Override
        public void remMP(double mp) {
            if(cheatMode)
                return;
            if (this.mp - mp < 0)
                this.mp = 0;
            else
                this.mp -= mp;
        }
        @Override
        public void setMaxMP(double maxMP) {
            this.maxMP = maxMP;
        }

        public double addMaxMP(double mp) {
            this.maxMP += mp;
            messages.add(Strings.Stats_LevelUp_MP);
            System.out.println(maxMP);
            return this.maxMP;
        }

        @Override
        public void setRecharge(boolean recharge) {
            this.recharge = recharge;
        }
        @Override
        public void setHudMode(boolean hud) {
            this.hudmode = hud;
        }

        @Override
        public void setChoice1(String choice) {
            this.choice1 = choice;
        }
        @Override
        public void setChoice2(String choice) {
            this.choice2 = choice;
        }

        @Override
        public int getExpNeeded(int level, int currentExp) {
            if (level == 100) return 0;
            double nextLevel = (double) (((level+1.0)+300.0*(Math.pow(2.0,((level+1.0)/7.0))))*((level+1.0)*0.25));
            int needed = ((int)nextLevel - currentExp);
            this.remainingExp = needed;
            return remainingExp;
        }

        @Override
        public void levelUpStatsAndDisplayMessage (EntityPlayer player) {
            AbilitiesCapability.IAbilities ABILITIES = player.getCapability(ModCapabilities.ABILITIES, null);
            this.getMessages().clear();
            switch (this.level) {
                case 2:
                    this.addDefense(1);
                    ABILITIES.unlockAbility(ModAbilities.scan);
                    break;
                case 3:
                    this.addStrength(1);
                    break;
                case 4:
                    this.addDefense(1);
                    break;
                case 5:
                    this.addStrength(1);
                    this.addHP(5);
                    ABILITIES.unlockAbility(ModAbilities.guard);
                    break;
                case 6:
                    this.addMagic(1);
                    this.addDefense(1);
                    break;
                case 7:
                    this.addStrength(1);
                    break;
                case 8:
                    this.addMagic(1);
                    break;
                case 9:
                    this.addStrength(1);
                    break;
                case 10:
                    this.addMagic(1);
                    this.addDefense(1);
                    this.addHP(5);
                    ABILITIES.unlockAbility(ModAbilities.mpHaste);
                    break;
                case 11:
                    this.addStrength(1);
                    break;
                case 12:
                    this.addMagic(1);
                    break;
                case 13:
                    this.addStrength(1);
                    break;
                case 14:
                    this.addMagic(1);
                    this.addDefense(1);
                    break;
                case 15:
                    this.addStrength(1);
                    this.addHP(5);
                    ABILITIES.unlockAbility(ModAbilities.formBoost);
                    break;
                case 16:
                    this.addMagic(1);
                    break;
                case 17:
                    this.addStrength(1);
                    break;
                case 18:
                    this.addMagic(1);
                    this.addDefense(1);
                    break;
                case 19:
                    this.addStrength(1);
                    break;
                case 20:
                    this.addMagic(1);
                    this.addHP(5);
                    ABILITIES.unlockAbility(ModAbilities.mpHastera);
                    break;
                case 21:
                    this.addStrength(1);
                    break;
                case 22:
                    this.addMagic(1);
                    this.addDefense(1);
                    break;
                case 23:
                    this.addStrength(1);
                    break;
                case 24:
                    this.addMagic(1);
                    break;
                case 25:
                    this.addStrength(1);
                    this.addHP(5);
                    ABILITIES.unlockAbility(ModAbilities.damageDrive);
                    break;
                case 26:
                    this.addMagic(1);
                    this.addDefense(1);
                    break;
                case 27:
                    this.addStrength(1);
                    this.addMagic(1);
                    break;
                case 28:
                    this.addMagic(1);
                    break;
                case 29:
                    this.addStrength(1);
                    break;
                case 30:
                    this.addMagic(1);
                    this.addDefense(1);
                    this.addHP(5);
                    break;
                case 31:
                    this.addStrength(1);
                    break;
                case 32:
                    this.addStrength(1);
                    this.addMagic(1);
                    break;
                case 33:
                    this.addStrength(1);
                    ABILITIES.unlockAbility(ModAbilities.driveConverter);
                    break;
                case 34:
                    this.addMagic(1);
                    this.addDefense(1);
                    break;
                case 35:
                    this.addStrength(1);
                    this.addHP(5);
                    break;
                case 36:
                    this.addMagic(1);
                    break;
                case 37:
                    this.addStrength(1);
                    break;
                case 38:
                    this.addMagic(1);
                    this.addDefense(1);
                    break;
                case 39:
                    this.addStrength(1);
                    break;
                case 40:
                    this.addMagic(1);
                    this.addHP(5);
                    break;
                case 41:
                    this.addStrength(1);
                    break;
                case 42:
                    this.addMagic(1);
                    this.addDefense(1);
                    break;
                case 43:
                    this.addStrength(1);
                    this.addMagic(1);
                    break;
                case 44:
                    this.addMagic(1);
                    break;
                case 45:
                    this.addStrength(1);
                    this.addHP(5);
                    break;
                case 46:
                    this.addMagic(1);
                    this.addDefense(1);
                    break;
                case 47:
                    this.addStrength(1);
                    break;
                case 48:
                    this.addStrength(1);
                    this.addMagic(1);
                    ABILITIES.unlockAbility(ModAbilities.sonicBlade);
                    break;
                case 49:
                    this.addStrength(1);
                    break;
                case 50:
                    this.addMagic(1);
                    this.addDefense(1);
                    this.addHP(5);
                    ABILITIES.unlockAbility(ModAbilities.mpHastega);
                    break;
                case 51:
                    this.addStrength(1);
                    break;
                case 52:
                    this.addMagic(1);
                    break;
                case 53:
                    this.addStrength(1);
                    break;
                case 54:
                    this.addMagic(1);
                    this.addDefense(1);
                    break;
                case 55:
                    this.addStrength(1);
                    this.addHP(5);
                    ABILITIES.unlockAbility(ModAbilities.strikeRaid);
                    break;
                case 56:
                    this.addMagic(1);
                    break;
                case 57:
                    this.addStrength(1);
                    break;
                case 58:
                    this.addMagic(1);
                    this.addDefense(1);
                    break;
                case 59:
                    this.addStrength(1);
                    break;
                case 60:
                    this.addMagic(1);
                    this.addHP(5);
                    break;
                case 61:
                    this.addStrength(1);
                    break;
                case 62:
                    this.addMagic(1);
                    this.addDefense(1);
                    break;
                case 63:
                    this.addStrength(1);
                    break;
                case 64:
                    this.addMagic(1);
                    break;
                case 65:
                    this.addStrength(1);
                    this.addHP(5);
                    break;
                case 66:
                    this.addMagic(1);
                    this.addDefense(1);
                    break;
                case 67:
                    this.addStrength(1);
                    break;
                case 68:
                    this.addMagic(1);
                    break;
                case 69:
                    this.addStrength(1);
                    break;
                case 70:
                    this.addMagic(1);
                    this.addDefense(1);
                    this.addHP(5);
                    break;
                case 71:
                    this.addStrength(1);
                    break;
                case 72:
                    this.addMagic(1);
                    break;
                case 73:
                    this.addStrength(1);
                    break;
                case 74:
                    this.addMagic(1);
                    this.addDefense(1);
                    break;
                case 75:
                    this.addStrength(1);
                    this.addHP(5);
                    break;
                case 76:
                    this.addMagic(1);
                    break;
                case 77:
                    this.addStrength(1);
                    break;
                case 78:
                    this.addMagic(1);
                    this.addDefense(1);
                    break;
                case 79:
                    this.addStrength(1);
                    break;
                case 80:
                    this.addMagic(1);
                    this.addHP(5);
                    break;
                case 81:
                    this.addStrength(1);
                    break;
                case 82:
                    this.addMagic(1);
                    this.addDefense(1);
                    break;
                case 83:
                    this.addStrength(1);
                    break;
                case 84:
                    this.addMagic(1);
                    break;
                case 85:
                    this.addStrength(1);
                    this.addHP(5);
                    break;
                case 86:
                    this.addMagic(1);
                    this.addDefense(1);
                    break;
                case 87:
                    this.addStrength(1);
                    break;
                case 88:
                    this.addMagic(1);
                    break;
                case 89:
                    this.addStrength(1);
                    break;
                case 90:
                    this.addMagic(1);
                    this.addDefense(1);
                    this.addHP(5);
                    break;
                case 91:
                    this.addStrength(1);
                    break;
                case 92:
                    this.addMagic(1);
                    break;
                case 93:
                    this.addStrength(1);
                    break;
                case 94:
                    this.addMagic(1);
                    this.addDefense(1);
                    break;
                case 95:
                    this.addStrength(1);
                    this.addHP(5);
                    break;
                case 96:
                    this.addMagic(1);
                    break;
                case 97:
                    this.addStrength(1);
                    break;
                case 98:
                    this.addMagic(1);
                    this.addDefense(1);
                    break;
                case 99:
                    this.addStrength(1);
                    break;
                case 100:
                    this.addStrength(10);
                    this.addDefense(10);
                    this.addMagic(10);
                    this.addHP(5);
                    break;
            }
            if(this.level%5 == 0) {
                player.setHealth(getHP());
                player.getFoodStats().addStats(20,0);
                player.getCapability(ModCapabilities.ORGANIZATION_XIII, null).addPoints(1);
                this.addMaxMP(5);
                this.setMP(this.getMaxMP());
                PacketDispatcher.sendTo(new SyncOrgXIIIData(player.getCapability(ModCapabilities.ORGANIZATION_XIII, null)), (EntityPlayerMP) player);
            }

            if(this.level%2 == 0) {
                this.addMaxAP(1);
            }

            PacketDispatcher.sendTo(new SyncUnlockedAbilities(ABILITIES), (EntityPlayerMP) player);

            player.world.playSound((EntityPlayer)null, player.getPosition(), ModSounds.levelup, SoundCategory.MASTER, 0.5f, 1.0f);
            player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getHP());
            PacketDispatcher.sendTo(new SyncLevelData(player.getCapability(ModCapabilities.PLAYER_STATS, null)), (EntityPlayerMP) player);
        }

        @Override
        public void setRechargeSpeed(double amount) {
            this.rechargeSpeed = amount;
        }

        @Override
        public double getRechargeSpeed() {
            return rechargeSpeed;
        }

        @Override
        public void setPotionsInventory(ItemStackHandler handler) {
            inventoryPotions = handler;
        }

        @Override
        public ItemStackHandler getInventoryEquipmentMenu() {
            return inventoryEquipment;
        }

        @Override
        public void setEquipmentInventory(ItemStackHandler handler) {
            inventoryEquipment = handler;
        }
    }
}
*/