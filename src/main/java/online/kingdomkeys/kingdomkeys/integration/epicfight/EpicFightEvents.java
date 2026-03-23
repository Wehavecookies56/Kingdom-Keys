package online.kingdomkeys.kingdomkeys.integration.epicfight;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.damagesource.StopDamageSource;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.Random;

public class EpicFightEvents {

    private LivingEntity target;
    ResourceLocation name;
    double speed;
    public int ticks;
    int maxTicks;

    float critChance = 0f;


    @SubscribeEvent
    public void hurtEvent(LivingDamageEvent.Pre event){
        if (event.getSource().getEntity() instanceof Player player){
            PlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
            if (playerpatch.isEpicFightMode()) {
                PlayerData playerData = PlayerData.get(player);
                ItemStack weapon = Utils.getWeaponDamageStack(event.getSource(), player);
                if (weapon != null && !(event.getSource() instanceof StopDamageSource)) {
                    float dmg = 0;
                    if (weapon.getItem() instanceof KeybladeItem) {
                        dmg = DamageCalculation.getKBStrengthDamage(player, weapon);
                    } else if (weapon.getItem() instanceof IOrgWeapon) {
                        dmg = DamageCalculation.getOrgStrengthDamage(player, weapon);
                    }
                    if (playerData != null) {
                        if (playerData.isAbilityEquipped(Strings.criticalBoost)) {
                            float critBoost = playerData.getNumberOfAbilitiesEquipped(Strings.criticalBoost) * 0.1f;
                            System.out.println("Crit Chance: " + critBoost);
                            critChance += critBoost;
                            RandomSource rand = player.getRandom();
                            if (rand.nextFloat() < critBoost) {
                                dmg *= ModConfigs.critMult;
                                dmg += dmg * PlayerData.get(player).getNumberOfAbilitiesEquipped(Strings.criticalBoost) * 0.1F;

                                float newDMG = (event.getNewDamage() - 1) + dmg * player.getAttackStrengthScale(0);
                                event.setNewDamage(newDMG);

                            }
                        }
                    }
                }
            }
            }
        }
    }

