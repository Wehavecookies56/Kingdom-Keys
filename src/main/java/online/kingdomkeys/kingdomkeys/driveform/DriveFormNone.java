package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class DriveFormNone extends DriveForm {

    public DriveFormNone(ResourceLocation registryName, int order, boolean hasKeychain) {
        super(registryName, order, hasKeychain, false);
        this.color = new float[] { 0F, 0F, 0F };
        this.isFakeForm = true;
    }

    @Override
    public Optional<ResourceLocation> getBaseAbilityForLevel(int driveFormLevel) {
        return Optional.empty();
    }

    @Override
    public Optional<ResourceLocation> getDFAbilityForLevel(int driveFormLevel) {
        return Optional.empty();
    }
    
    @Override
    public boolean canUseMagic() {
    	return true;
    }

    @Override
    public boolean displayInCommandMenu(Player player) {
        return false;
    }
}
