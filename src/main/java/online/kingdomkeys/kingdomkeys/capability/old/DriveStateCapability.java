package online.kingdomkeys.kingdomkeys.capability.old;
/*
package online.kingdomkeys.kingdomkeys.capability;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.fml.network.PacketDispatcher;
import net.minecraftforge.items.ItemStackHandler;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import uk.co.wehavecookies56.kk.api.driveforms.DriveForm;
import uk.co.wehavecookies56.kk.common.network.packet.client.ShowOverlayPacket;
import uk.co.wehavecookies56.kk.common.network.packet.client.SyncDriveData;

public class DriveStateCapability {

    public interface IDriveState {
        List<String> getMessages();

        boolean getInDrive();

        String getActiveDriveName();

        int getAntiPoints();
        int getDriveLevel(String drive);
        int getDriveExp(String drive);
        int getDriveGaugeLevel();
        int getFormGaugeLevel(String drive);
        double getDP();
        double getMaxDP();
        double getFP();
        double getMaxFP();
        
        void setDriveGaugeLevel(int level);
        void setInDrive(boolean drive);
        void setActiveDriveName(String drive);
        void setAntiPoints(int points);
        void setDriveLevel(String drive, int level);
        void setDriveExp(String drive, int exp);
        
        boolean setDP(double dp);
        void addDP(double dp);
        void remDP(double dp);
        void setFP(double fp);
        void addFP(double fp);
        void remFP(double fp);
        
        void learnDriveForm(DriveForm form);

        ItemStackHandler getInventoryDriveForms();
        
        void displayLevelUpMessage(EntityPlayer player, String driveForm);
        void clearMessages();
    }

    public static class Storage implements IStorage<IDriveState> {

        @Override
        public NBTBase writeNBT(Capability<IDriveState> capability, IDriveState instance, EnumFacing side) {
            NBTTagCompound properties = new NBTTagCompound();
            properties.setBoolean("InDrive", instance.getInDrive());
            properties.setString("ActiveDriveName", instance.getActiveDriveName());
            properties.setInteger("AntiPoints", instance.getAntiPoints());
            properties.setInteger("DriveLevelValor", instance.getDriveLevel(Strings.Form_Valor));
            properties.setInteger("DriveLevelWisdom", instance.getDriveLevel(Strings.Form_Wisdom));
            properties.setInteger("DriveLevelLimit", instance.getDriveLevel(Strings.Form_Limit));
            properties.setInteger("DriveLevelMaster", instance.getDriveLevel(Strings.Form_Master));
            properties.setInteger("DriveLevelFinal", instance.getDriveLevel(Strings.Form_Final));

            properties.setInteger("DriveExpValor", instance.getDriveExp(Strings.Form_Valor));
            properties.setInteger("DriveExpWisdom", instance.getDriveExp(Strings.Form_Wisdom));
            properties.setInteger("DriveExpLimit", instance.getDriveExp(Strings.Form_Limit));
            properties.setInteger("DriveExpMaster", instance.getDriveExp(Strings.Form_Master));
            properties.setInteger("DriveExpFinal", instance.getDriveExp(Strings.Form_Final));
            properties.setInteger("DriveGaugeLevel", instance.getDriveGaugeLevel());
            
            properties.setDouble("DP", instance.getDP());
            properties.setDouble("FP", instance.getFP());


            properties.setTag("DriveInvKey", instance.getInventoryDriveForms().serializeNBT());

            return properties;
        }

        @Override
        public void readNBT(Capability<IDriveState> capability, IDriveState instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound properties = (NBTTagCompound) nbt;
            instance.setInDrive(properties.getBoolean("InDrive"));
            instance.setActiveDriveName(properties.getString("ActiveDriveName"));
            instance.setAntiPoints(properties.getInteger("AntiPoints"));
            instance.setDriveLevel(Strings.Form_Valor, properties.getInteger("DriveLevelValor"));
            instance.setDriveLevel(Strings.Form_Wisdom, properties.getInteger("DriveLevelWisdom"));
            instance.setDriveLevel(Strings.Form_Limit, properties.getInteger("DriveLevelLimit"));
            instance.setDriveLevel(Strings.Form_Master, properties.getInteger("DriveLevelMaster"));
            instance.setDriveLevel(Strings.Form_Final, properties.getInteger("DriveLevelFinal"));

            instance.setDriveExp(Strings.Form_Valor, properties.getInteger("DriveExpValor"));
            instance.setDriveExp(Strings.Form_Wisdom, properties.getInteger("DriveExpWisdom"));
            instance.setDriveExp(Strings.Form_Limit, properties.getInteger("DriveExpLimit"));
            instance.setDriveExp(Strings.Form_Master, properties.getInteger("DriveExpMaster"));
            instance.setDriveExp(Strings.Form_Final, properties.getInteger("DriveExpFinal"));
            instance.setDriveGaugeLevel(properties.getInteger("DriveGaugeLevel"));
            
            instance.setDP(properties.getDouble("DP"));
            instance.setFP(properties.getDouble("FP"));


            instance.getInventoryDriveForms().deserializeNBT(properties.getCompoundTag("DriveInvKey"));
        }
    }

    public static class Default implements IDriveState {
    	
        private List<String> messages = new ArrayList<String>();

        private boolean inDrive = false;
        private String activeDrive = "none";
        int antiPoints = 0;
        private final ItemStackHandler inventoryDrive = new ItemStackHandler(5);
        private static List<String> driveForms = new ArrayList<String>();
        private int driveGaugeLevel = 3;
        int valor, wisdom, limit, master, Final;
        int valorExp, wisdomExp, limitExp, masterExp, FinalExp;
        
        private double dp = 0;
        private double fp = 0;
        private double maxFP = 300;
        
        @Override public boolean getInDrive() { return inDrive; }
        @Override public String getActiveDriveName() { return activeDrive; }
        @Override public int getAntiPoints() { return antiPoints; }
        @Override public int getDriveLevel(String drive) {
            switch(drive) {
                case Strings.Form_Valor:
                    return valor;
                case Strings.Form_Wisdom:
                    return wisdom;
                case Strings.Form_Limit:
                    return limit;
                case Strings.Form_Master:
                    return master;
                case Strings.Form_Final:
                    return Final;
            }
            return 0;
        }
        
        @Override
        public List<String> getMessages() {
            return this.messages;
        }

        @Override
        public void clearMessages() {
            this.getMessages().clear();
        }

        @Override
        public int getFormGaugeLevel(String drive) {
            return (3 + getDriveLevel(drive)) - 1;
        }

        @Override public void setInDrive(boolean drive) { this.inDrive = drive; }
        @Override public void setActiveDriveName(String drive) { this.activeDrive = drive; }
        @Override public void setAntiPoints(int points) { this.antiPoints = points; }
        @Override public void setDriveLevel(String drive, int level) {
            switch (drive) {
                case Strings.Form_Valor:
                    valor = level;
                    break;
                case Strings.Form_Wisdom:
                    wisdom = level;
                    break;
                case Strings.Form_Limit:
                    limit = level;
                    break;
                case Strings.Form_Master:
                    master = level;
                    break;
                case Strings.Form_Final:
                    Final = level;
                    break;
            }
        }
        @Override
        public void learnDriveForm(DriveForm form) {
            driveForms.add(form.getName());
        }
        @Override public ItemStackHandler getInventoryDriveForms(){return this.inventoryDrive;}
        @Override public int getDriveExp(String drive) {
            switch(drive) {
            case Strings.Form_Valor:
                return valorExp;
            case Strings.Form_Wisdom:
                return wisdomExp;
            case Strings.Form_Limit:
                return limitExp;
            case Strings.Form_Master:
                return masterExp;
            case Strings.Form_Final:
                return FinalExp;
            }
            return 0;
        }
        @Override public void setDriveExp(String drive, int exp) {
            switch (drive) {
                case Strings.Form_Valor:
                    valorExp = exp;
                    break;
                case Strings.Form_Wisdom:
                    wisdomExp = exp;
                    break;
                case Strings.Form_Limit:
                    limitExp = exp;
                    break;
                case Strings.Form_Master:
                    masterExp = exp;
                    break;
                case Strings.Form_Final:
                    FinalExp = exp;
                    break;
            }
        }

        @Override
        public int getDriveGaugeLevel() {
            return driveGaugeLevel;
        }

        @Override
        public void setDriveGaugeLevel(int level) {
        	if(level > 9) {
        		this.driveGaugeLevel = 9;
        	}else{
        		this.driveGaugeLevel = level;
        	}
        }
        @Override
        public void displayLevelUpMessage (EntityPlayer player, String driveForm) {
        	this.getMessages().clear();
            switch (driveForm) {
            
            case Strings.Form_Valor:
                messages.add(CorsairUtils.translateToLocal(Strings.Stats_LevelUp_FormGauge));
                System.out.println(this.getDriveLevel(driveForm));
            	switch (this.getDriveLevel(driveForm)) {
            	case 2:
                    break;
                case 3:
                    messages.add(CorsairUtils.translateToLocalFormatted(Strings.Stats_LevelUp_HighJump, "2"));
                    break;
                case 4:
                    break;
                case 5:
                    messages.add(CorsairUtils.translateToLocalFormatted(Strings.Stats_LevelUp_HighJump, "3"));
                    break;
                case 6:
                    break;
                case 7:
                    messages.add(CorsairUtils.translateToLocalFormatted(Strings.Stats_LevelUp_HighJump, "MAX"));
                    break;
            	}
            	break;
            	
            case Strings.Form_Wisdom:
            	messages.add(Strings.Stats_LevelUp_FormGauge);
            	switch (this.getDriveLevel(driveForm)) {
            	case 2:
                    break;
                case 3:
                    messages.add(CorsairUtils.translateToLocalFormatted(Strings.Stats_LevelUp_QuickRun, "2"));
                    break;
                case 4:
                    break;
                case 5:
                    messages.add(CorsairUtils.translateToLocalFormatted(Strings.Stats_LevelUp_QuickRun, "3"));
                    break;
                case 6:
                    break;
                case 7:
                    messages.add(CorsairUtils.translateToLocalFormatted(Strings.Stats_LevelUp_QuickRun, "MAX"));
                    break;
            	}
            	break;
            	
            case Strings.Form_Limit:
            	messages.add(Strings.Stats_LevelUp_FormGauge);
            	switch (this.getDriveLevel(driveForm)) {
            	case 2:
                    break;
                case 3:
                    messages.add(CorsairUtils.translateToLocalFormatted(Strings.Stats_LevelUp_DodgeRoll, "2"));
                    break;
                case 4:
                    break;
                case 5:
                    messages.add(CorsairUtils.translateToLocalFormatted(Strings.Stats_LevelUp_DodgeRoll, "3"));
                    break;
                case 6:
                    break;
                case 7:
                    messages.add(CorsairUtils.translateToLocalFormatted(Strings.Stats_LevelUp_DodgeRoll, "MAX"));
                    break;
            	}
            	break;
            	
            case Strings.Form_Master:
            	messages.add(Strings.Stats_LevelUp_FormGauge);
            	switch (this.getDriveLevel(driveForm)) {
            	case 2:
                    break;
                case 3:
                    messages.add(CorsairUtils.translateToLocalFormatted(Strings.Stats_LevelUp_AerialDodge, "2"));
                    break;
                case 4:
                    break;
                case 5:
                    messages.add(CorsairUtils.translateToLocalFormatted(Strings.Stats_LevelUp_AerialDodge, "3"));
                    break;
                case 6:
                    break;
                case 7:
                    messages.add(CorsairUtils.translateToLocalFormatted(Strings.Stats_LevelUp_AerialDodge, "MAX"));
                    break;
            	}
            	break;
            	
            case Strings.Form_Final:
            	messages.add(Strings.Stats_LevelUp_FormGauge);
            	switch (this.getDriveLevel(driveForm)) {
            	case 2:
                    break;
                case 3:
                    messages.add(CorsairUtils.translateToLocalFormatted(Strings.Stats_LevelUp_Glide, "2"));
                    break;
                case 4:
                    break;
                case 5:
                    messages.add(CorsairUtils.translateToLocalFormatted(Strings.Stats_LevelUp_Glide, "3"));
                    break;
                case 6:
                    break;
                case 7:
                    messages.add(CorsairUtils.translateToLocalFormatted(Strings.Stats_LevelUp_Glide, "MAX"));
                    break;
            	}
            	break;
            	
            }
            
            player.world.playSound((EntityPlayer)null, player.getPosition(), ModSounds.levelup, SoundCategory.MASTER, 0.5f, 1.0f);
            PacketDispatcher.sendTo(new SyncDriveData(player.getCapability(ModCapabilities.DRIVE_STATE, null)), (EntityPlayerMP) player);
            
            PacketDispatcher.sendTo(new ShowOverlayPacket("drivelevelup", driveForm),(EntityPlayerMP)player);
        }
		@Override
		public double getMaxDP() {
			return getDriveGaugeLevel()*100;
		}
		@Override
		public double getDP() {
			return this.dp;
		}
		
		@Override
        public double getFP() {
            return fp;
        }

        @Override
        public double getMaxFP() {
            return maxFP;
        }

        @Override
        public void setFP(double fp) {
            this.fp = fp;
        }

        @Override
        public void addFP(double fp) {
            this.fp += fp;
        }

        @Override
        public void remFP(double fp) {
            this.fp -= fp;
        }
        
        @Override
        public boolean setDP(double dp) {
            if (dp <= getMaxDP()) {
                this.dp = dp;
                return true;
            }
            return false;
        }
        @Override
        public void addDP(double dp) {
            if (dp + this.dp > getMaxDP())
                this.dp = getMaxDP();
            else
                this.dp += dp;
        }
        @Override
        public void remDP(double dp) {
            if (this.dp - dp < 0)
                this.dp = 0;
            else
                this.dp -= dp;
        }
    }
}
*/

