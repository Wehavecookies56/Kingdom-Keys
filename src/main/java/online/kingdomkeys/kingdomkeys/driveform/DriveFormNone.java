package online.kingdomkeys.kingdomkeys.driveform;

public class DriveFormNone extends DriveForm {

    public DriveFormNone(String registryName, int order, boolean hasKeychain) {
        super(registryName, order, hasKeychain);
    }

    @Override
    public String getBaseAbilityForLevel(int driveFormLevel) {
        return null;
    }

    @Override
    public String getDFAbilityForLevel(int driveFormLevel) {
        return null;
    }
}
