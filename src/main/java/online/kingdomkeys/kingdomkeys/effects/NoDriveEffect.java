package online.kingdomkeys.kingdomkeys.effects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;

public class NoDriveEffect extends MobEffect {
    public NoDriveEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if(pLivingEntity instanceof Player player){
            PlayerData playerData = PlayerData.get(player);
            if(playerData != null){
                DriveForm form = ModDriveForms.registry.get(ResourceLocation.parse(playerData.getActiveDriveForm()));
                if(form != null && !playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())){
                    form.endDrive(player);
                }
            }
        }
        return super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
