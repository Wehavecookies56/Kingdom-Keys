package online.kingdomkeys.kingdomkeys.mixin.wildfire_gender;

import com.wildfire.render.GenderLayer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GenderLayer.class)
public class GenderLayerMixin<ENTITY extends LivingEntity> {

    @Redirect(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lcom/wildfire/render/GenderLayer;getBreastTexture(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/resources/ResourceLocation;"))
    public @Nullable ResourceLocation renderDriveForm(GenderLayer instance, ENTITY entity) {
        ResourceLocation rl;
        if (entity instanceof AbstractClientPlayer player) {
            rl = player.getSkin().texture();
            if (PlayerData.get(player) != null) {
                ResourceLocation drive = PlayerData.get(player).getActiveDriveForm();
                if (!drive.equals(DriveForm.NONE)) {
                    DriveForm form = ModDriveForms.registry.get(drive);
                    if (form.getTextureLocation(player) != null) {
                        rl = form.getTextureLocation(player);
                    }
                }
            }
        } else {
            rl = null;
        }

        return rl;
    }

}
