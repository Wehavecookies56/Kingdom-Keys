package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.magic.SparkEntity;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class MagicSpark extends Magic {

    public MagicSpark(ResourceLocation registryName, boolean hasToSelect, int maxLevel, String gmAbility) {
        super(registryName, hasToSelect, maxLevel, gmAbility);
    }

    @Override
    public void magicUse(LivingEntity player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnTarget) {
        float dmgMult = getDamageMult(level + 1) + PlayerData.get(caster).getNumberOfAbilitiesEquipped(Strings.thunderBoost) * 0.2F;
        dmgMult *= fullMPBlastMult;

        double baseRadius = 1.0;
        double outerRadius = 1.6;
        double baseHeight = -1;
        double heightStep = 0.5;
        double speed = 0.5;

        switch (level) {
            case 0:
                // === 2 orbs: one pair (north/south) ===
                for (int i = 0; i < 2; i++) {
                    SparkEntity spark = new SparkEntity(player.level(), player, i, dmgMult);
                    spark.setCaster(player.getDisplayName().getString());

                    // 0 and PI → opposite
                    spark.setAngleOffset(i * Math.PI);
                    spark.setDirection(1); // both rotate the same way
                    spark.setOrbitRadius(baseRadius);
                    spark.setOrbitSpeed(speed);
                    spark.setVerticalOffset(baseHeight + 0.5);
                    player.level().addFreshEntity(spark);

                }
                player.level().playSound(null, player.blockPosition(), ModSounds.spark.get(), SoundSource.PLAYERS, 1F, 1F);
                break;

            case 1:
                // === 4 orbs: 2 pairs (north/south + east/west) ===
                for (int i = 0; i < 2; i++) {
                    // pair 1: N/S
                    SparkEntity spark = new SparkEntity(player.level(), player, i, dmgMult);
                    spark.setCaster(player.getDisplayName().getString());
                    spark.setAngleOffset(i * Math.PI);
                    spark.setDirection(1);
                    spark.setOrbitRadius(baseRadius);
                    spark.setOrbitSpeed(speed);
                    spark.setVerticalOffset(baseHeight + 0.5);
                    player.level().addFreshEntity(spark);


                    // pair 2: E/W → offset by 90°
                    SparkEntity spark2 = new SparkEntity(player.level(), player, i + 2, dmgMult);
                    spark2.setCaster(player.getDisplayName().getString());
                    spark2.setAngleOffset(i * Math.PI + Math.PI / 2);
                    spark2.setDirection(1);
                    spark2.setOrbitRadius(baseRadius + 0.5);
                    spark2.setOrbitSpeed(speed + 0.5);
                    spark2.setVerticalOffset(baseHeight);
                    player.level().addFreshEntity(spark2);

                }
                player.level().playSound(null, player.blockPosition(), ModSounds.sparkra.get(), SoundSource.PLAYERS, 1F, 1F);
                break;

            case 2:
                // === 6 orbs: 3 pairs (120° apart) ===
                for (int pair = 0; pair < 3; pair++) {
                    for (int j = 0; j < 2; j++) {
                        SparkEntity spark = new SparkEntity(player.level(), player, pair * 2 + j, dmgMult);
                        spark.setCaster(player.getDisplayName().getString());

                        // each pair is 180° apart; shift each pair by 120°
                        spark.setAngleOffset(j * Math.PI + (2 * Math.PI / 3) * pair);
                        spark.setDirection(1);
                        spark.setOrbitRadius(outerRadius + j);
                        spark.setOrbitSpeed(speed);
                        spark.setVerticalOffset(baseHeight + pair * heightStep);
                        player.level().addFreshEntity(spark);
                    }
                }
                player.level().playSound(null, player.blockPosition(), ModSounds.sparkga.get(), SoundSource.PLAYERS, 1F, 1F);
                break;
        }
    }


            @Override
    protected void playMagicCastSound(LivingEntity player, Player player1, int i) {
        // sound handled elsewhere if desired

    }
}
