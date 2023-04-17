package online.kingdomkeys.kingdomkeys.integration.epicfight.init;

import com.mojang.math.Matrix4f;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.organization.ArrowgunShotEntity;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.ServerAnimator;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.BasicAttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.awt.*;

public class KKAnimations {
    public static StaticAnimation TEST, CHAKRAM_AUTO_1, ROXAS_AUTO_1, ROXAS_IDLE, ROXAS_RUN,
            KK_SHIELD_AUTO_1, KK_SHIELD_AUTO_2, KK_SHIELD_AUTO_3, KH1_SORA_AUTO_1, VALOR_IDLE, VALOR_AUTO_1, VALOR_AUTO_2,
            VALOR_AUTO_3, MASTER_IDLE, WISDOM_IDLE, WISDOM_RUN, WISDOM_COMBO1;



    private KKAnimations(){

    }

    public static void register(AnimationRegistryEvent event) {
        event.getRegistryMap().put(KingdomKeys.MODID, KKAnimations::build);

    }
    private static void build() {

        VALOR_IDLE = new StaticAnimation(true, "biped/living/valor_idle", Armatures.BIPED);
        WISDOM_IDLE = new StaticAnimation(true, "biped/living/wisdom_idle", Armatures.BIPED);
        WISDOM_RUN = new StaticAnimation(true, "biped/living/wisdom_run", Armatures.BIPED);
        WISDOM_COMBO1= new BasicAttackAnimation(0.16F, 0.05F, 0.5F, 0.7F, KKColiders.KEYBLADE,Armatures.BIPED.toolR,"biped/combat/kh1_sora_auto_1",  Armatures.BIPED)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED, 1.1F).addEvents(AnimationProperty.StaticAnimationProperty.EVENTS, AnimationEvent.TimeStampedEvent
                        .create(.2f, (ep, arr) -> {

                            Player player = (Player) ep.getOriginal();
                            Level world = player.getLevel();
                            if(!world.isClientSide) {

                                // gets the positions of the bone somehow
                                Vec3 playerPos = ep.getOriginal().position();
                                OpenMatrix4f tempMatrix = OpenMatrix4f.createTranslation((float) playerPos.x, (float) playerPos.y, (float) playerPos.z)
                                        .mulBack(OpenMatrix4f.createRotatorDeg(180, Vec3f.Y_AXIS)).mulBack(ep.getModelMatrix(1));
                                Vec3 test2 = OpenMatrix4f.transform(Armatures.BIPED.getBindedTransformFor(ep.getArmature().getPose(1)
                                        , Armatures.BIPED.toolR).mulFront(tempMatrix) , Vec3.ZERO);


                                ArrowgunShotEntity shot = new ArrowgunShotEntity(player.level, player, DamageCalculation.getMagicDamage(player) * 0.1F,test2.x, test2.y, test2.z );
                                shot.setShotType(1);
                                shot.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 3F, 0);
                                world.addFreshEntity(shot);
                                player.level.playSound(null, player.blockPosition(), ModSounds.wisdom_shot.get(), SoundSource.PLAYERS, 1F, 1F);
                            }
                        }, AnimationEvent.Side.BOTH));


        MASTER_IDLE = new StaticAnimation(true, "biped/living/master_idle", Armatures.BIPED);



        ROXAS_AUTO_1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null,Armatures.BIPED.toolR,"biped/combat/roxas_auto_1",  Armatures.BIPED);
        ROXAS_IDLE = new StaticAnimation(true,"biped/living/roxas_idle",  Armatures.BIPED);
        ROXAS_RUN = new StaticAnimation(true,"biped/living/roxas_run",  Armatures.BIPED);

        KH1_SORA_AUTO_1 = new BasicAttackAnimation(0.16F, 0.05F, 0.5F, 0.7F, KKColiders.KEYBLADE,Armatures.BIPED.toolR,"biped/combat/kh1_sora_auto_1",  Armatures.BIPED);

        KK_SHIELD_AUTO_1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null, Armatures.BIPED.toolR,"biped/combat/kk_shield_auto_1",  Armatures.BIPED);
        KK_SHIELD_AUTO_2 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null,Armatures.BIPED.toolR,"biped/combat/kk_shield_auto_2",  Armatures.BIPED);
        KK_SHIELD_AUTO_3 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null,Armatures.BIPED.toolR,"biped/combat/kk_shield_auto_3",  Armatures.BIPED);

        CHAKRAM_AUTO_1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null,Armatures.BIPED.toolR,"biped/combat/chakram_auto_1",  Armatures.BIPED);

        TEST = new StaticAnimation(true,"biped/living/test",  Armatures.BIPED);
    }
}
