package online.kingdomkeys.kingdomkeys.util;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.integration.epicfight.EpicFightUtils;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCCombatWindowsPacket;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;

/**
 * Guard, Counterattack, Once More and Aerial Recovery. They all hang off the same handful of countdowns on
 * the player's data, so they live together rather than being scattered through the event handlers.
 */
public class CombatAbilities {
    private CombatAbilities() {}

    /** How long the keyblade stays up, and how long before it can go up again */
    public static final int GUARD_TICKS = 20, GUARD_COOLDOWN = 30;
    /** What a blocked hit leaves on the clock. Longer than vanilla's ten ticks of mercy after being hurt,
     * so the next blow of a combo lands while the keyblade is still up and keeps the guard alive */
    public static final int GUARD_EXTEND = 15;
    /** How long a blocked hit leaves you able to answer it */
    public static final int COUNTER_TICKS = 20;
    /** How long after being knocked about you can still catch yourself. Generous, because what launches
     * you in Minecraft is usually a blast or a fall rather than the hit itself, and that takes a moment */
    public static final int RECOVERY_TICKS = 60;
    /** Hits closer together than this count as one combo */
    public static final int COMBO_TICKS = 30;
    /** How long the counter's turn takes. Short and sharp, like the swing it stands for. */
    public static final int SPIN_TICKS = 4;
    /** The ring outlives the turn, or a fifth of a second would be all you ever saw of it */
    public static final int COUNTER_RING_TICKS = 10;
    /** How long the ring left behind by catching yourself hangs around */
    public static final int FLASH_TICKS = 10;

    /** Half of the ninety degree arc the guard covers, as the cosine of the angle */
    private static final double GUARD_ARC = Math.cos(Math.toRadians(45));
    public static final double COUNTER_RANGE = 3.5;

    /** Ticks everything down. Called once a tick for each player, on both sides. */
    public static void tick(Player player, PlayerData data) {
        data.setGuardTicks(Math.max(0, data.getGuardTicks() - 1));
        data.setGuardCooldown(Math.max(0, data.getGuardCooldown() - 1));
        data.setCounterTicks(Math.max(0, data.getCounterTicks() - 1));
        data.setComboTicks(Math.max(0, data.getComboTicks() - 1));
        data.setCounterSpinTicks(Math.max(0, data.getCounterSpinTicks() - 1));
        data.setCounterRingTicks(Math.max(0, data.getCounterRingTicks() - 1));
        data.setRecoveryFlashTicks(Math.max(0, data.getRecoveryFlashTicks() - 1));

        data.setRecoveryTicks(Math.max(0, data.getRecoveryTicks() - 1));
    }


    public static boolean canGuard(Player player, PlayerData data) {
        return data != null
                && data.isAbilityEquipped(ModAbilities.GUARD)
                && data.getGuardCooldown() == 0
                && player.getMainHandItem().getItem() instanceof KeybladeItem
                && !EpicFightUtils.isInEpicFightMode(player);
    }

    public static void startGuard(Player player, PlayerData data) {
        data.setGuardTicks(GUARD_TICKS);
        data.setGuardCooldown(GUARD_TICKS + GUARD_COOLDOWN);
        playSound(player, SoundEvents.ARMOR_EQUIP_NETHERITE.value(), 0.7F, 1.3F);
        tellEveryone(player, data);
    }

    private static void playSound(Player player, SoundEvent sound, float volume, float pitch) {
        if (!player.level().isClientSide) {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), sound, SoundSource.PLAYERS, volume, pitch);
        }
    }

    public static boolean blocks(Player player, PlayerData data, DamageSource source) {
        if (data == null || data.getGuardTicks() <= 0 || source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        }

        Vec3 from = source.getSourcePosition();

        if (from == null) {
            return false;
        }

        Vec3 towards = new Vec3(from.x - player.getX(), 0, from.z - player.getZ());
        Vec3 look = player.getLookAngle();
        Vec3 facing = new Vec3(look.x, 0, look.z);

        // Something directly overhead or underfoot has no direction to speak of, so it goes through
        if (towards.lengthSqr() < 1E-6 || facing.lengthSqr() < 1E-6) {
            return false;
        }

        double aim = facing.normalize().dot(towards.normalize());
        return aim >= GUARD_ARC;
    }


    public static void onBlocked(Player player, PlayerData data) {
        playSound(player, ModSounds.guard.get(), 1F, 1.4F);
        data.setGuardTicks(Math.max(data.getGuardTicks(), GUARD_EXTEND));
        data.setGuardCooldown(data.getGuardTicks() + GUARD_COOLDOWN);

        if (data.isAbilityEquipped(ModAbilities.COUNTERGUARD))
            data.setCounterTicks(COUNTER_TICKS);

        tellEveryone(player, data);
    }

    /**
     * The windows live on the server. Their owner needs them to know when to watch the keys, and everyone
     * else needs them to draw the raised keyblade and the counter's turn.
     */
    private static void tellEveryone(Player player, PlayerData data) {
        if (!player.level().isClientSide) {
            PacketHandler.sendToAll(new SCCombatWindowsPacket(player.getId(), data.getGuardTicks(), data.getCounterTicks(), data.getRecoveryTicks(), data.getCounterSpinTicks(), data.getCounterRingTicks(), data.getRecoveryFlashTicks()));
        }
    }

    /**
     * The answer to a blocked hit. The player turns on the spot, so unlike the guard it reaches all the way
     * round rather than only what was in front. Returns whether there was a counter to make at all.
     */
    public static boolean counter(Player player, PlayerData data) {
        if (data == null || data.getCounterTicks() <= 0) {
            return false;
        }

        data.setCounterTicks(0);
        data.setGuardTicks(0);
        data.setCounterSpinTicks(SPIN_TICKS);
        data.setCounterRingTicks(COUNTER_RING_TICKS);
        tellEveryone(player, data);
        playSound(player, SoundEvents.PLAYER_ATTACK_SWEEP, 1F, 0.7F);
        playSound(player, SoundEvents.PLAYER_ATTACK_CRIT, 1F, 0.8F);

        float damage = DamageCalculation.getKBStrengthDamage(player, player.getMainHandItem()) * 1.5F;

        for (LivingEntity target : player.level().getEntitiesOfClass(LivingEntity.class, new AABB(player.blockPosition()).inflate(COUNTER_RANGE))) {
            if (target == player || !target.isAlive()) {
                continue;
            }

            target.hurt(player.damageSources().playerAttack(player), damage);
            target.knockback(0.6, player.getX() - target.getX(), player.getZ() - target.getZ());
        }

        return true;
    }

    /**
     * Notes that a hit landed, so the next one can tell whether it is part of the same combo. A run of hits
     * remembers the health it started with, which is what decides whether Once More lets it finish you.
     */
    public static void noteHit(Player player, PlayerData data) {
        if (data.getComboTicks() == 0) {
            data.setComboStartHealth(player.getHealth());
        }

        data.setComboTicks(COMBO_TICKS);
    }

    public static float survive(Player player, PlayerData data, float damage) {
        float health = player.getHealth() + player.getAbsorptionAmount();

        if (data == null || damage < health) {
            return damage;
        }

        boolean secondChance = data.isAbilityEquipped(ModAbilities.SECOND_CHANCE) && health > 1;
        boolean onceMore = data.isAbilityEquipped(ModAbilities.ONCE_MORE) && data.getComboTicks() > 0 && data.getComboStartHealth() > 1;

        if (!secondChance && !onceMore) {
            return damage;
        }

        playSound(player, ModSounds.invincible_hit.get(), 0.7F, 1.6F);
        return Math.max(0, health - 1);
    }


    /** Anything that throws the player about opens a window to land on their feet instead */
    public static void noteKnockback(Player player, PlayerData data) {
        if (data == null) {
            return;
        }

        if (data.isAbilityEquipped(ModAbilities.AERIAL_RECOVERY)) {
            data.setRecoveryTicks(RECOVERY_TICKS);
            tellEveryone(player, data);
        }
    }

    // Aerial recovery
    public static boolean recover(Player player, PlayerData data) {
        if (data == null || data.getRecoveryTicks() <= 0 || player.onGround()) {
            return false;
        }

        data.setRecoveryTicks(0);
        data.setRecoveryFlashTicks(FLASH_TICKS);
        tellEveryone(player, data);
        player.fallDistance = 0;
        player.setDeltaMovement(0, Math.min(0, player.getDeltaMovement().y) * 0.1, 0);
        player.hurtMarked = true;
        playSound(player, ModSounds.aerialRecovery.get(), 0.6F, 1.4F);
        return true;
    }
}
