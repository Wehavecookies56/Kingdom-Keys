package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowMessagesPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class Duel {
    private static final int FIGHTING = -1;

    private final Mob fighter;
    private final Dueller dueller;

    @Nullable
    private UUID duelist;

    private int leavingTicks;
    private int endingTicks = FIGHTING;

    private int readyIn;

    private boolean down;

    public <T extends Mob & Dueller> Duel(T owner) {
        this.fighter = owner;
        this.dueller = owner;
    }

    public void tick() {
        if (fighter.level().isClientSide) {
            return;
        }

        // Once it is settled they are only seeing to the pupil, not fighting them
        if (endingTicks >= 0) {
            endingTicks++;

            if (endingTicks == cureAt()) {
                tendTo();
            }

            if (endingTicks >= endsAt()) {
                dueller.bowOut();
            }

            return;
        }

        tickPreparation();
        tickWatch();
    }

    public boolean isSettled() {
        return endingTicks >= 0;
    }

    public boolean isPreparing() {
        return readyIn > 0;
    }

    public void end(boolean tellThem) {
        if (endingTicks >= 0) {
            return;
        }

        endingTicks = 0;
        fighter.setTarget(null);
        fighter.setNoAi(true);
        fighter.setDeltaMovement(0.0D, fighter.getDeltaMovement().y, 0.0D);

        if (tellThem) {
            announce(Strings.Duel_Lost, Strings.Duel_Lost_Sub, 4, 50, 10);
            return;
        }

        down = true;
        fighter.setHealth(1.0F);

        if (dueller.fallsWhenBeaten()) {
            lieDown();
        }

        dueller.onBeaten();
    }

    private void lieDown() {
        fighter.setPose(Pose.SLEEPING);
    }

    private int cureAt() {
        return down ? Dueller.DOWN_TICKS : Dueller.CURE_AT;
    }

    private int endsAt() {
        return down ? Dueller.DOWN_TICKS + Dueller.RISE_TICKS : Dueller.ENDING_TICKS;
    }

    private void tendTo() {
        if (down) {
            // Comes round under their own cure, and gets up. Being whole again waits for bowOut
            ModMagic.CURAGA.get().castFromMob(fighter, fighter, null);
            getUp();
            return;
        }

        ServerPlayer pupil = getDuelist();

        if (pupil == null) {
            return;
        }

        fighter.lookAt(EntityAnchorArgument.Anchor.EYES, pupil.getEyePosition());
        fighter.setYBodyRot(fighter.getYRot());
        fighter.setYHeadRot(fighter.getYRot());

        ModMagic.CURAGA.get().castFromMob(pupil, fighter, null);
    }

    private void getUp() {
        fighter.setPose(Pose.STANDING);
    }

    private void tickWatch() {
        if (duelist == null) {
            return;
        }

        ServerPlayer pupil = getDuelist();

        if (pupil == null || !pupil.isAlive() || pupil.isSpectator() || pupil.level() != fighter.level()) {
            dueller.bowOut();
            return;
        }

        if (fighter.distanceToSqr(pupil) > Dueller.LEAVE_RANGE * Dueller.LEAVE_RANGE) {
            fighter.setTarget(null);

            if (++leavingTicks >= Dueller.LEAVE_GRACE) {
                dueller.bowOut();
            }

            return;
        }

        leavingTicks = 0;
    }

    private void tickPreparation() {
        if (readyIn <= 0) {
            return;
        }

        if (readyIn % Dueller.BEAT_TICKS == 0) {
            announce(String.valueOf(readyIn / Dueller.BEAT_TICKS));
        }

        if (--readyIn <= 0) {
            announce(Strings.Duel_Begin);
            fighter.setNoAi(false);
            fighter.setInvulnerable(false);
            return;
        }

        fighter.setNoAi(true);
        fighter.setInvulnerable(true);
        fighter.setDeltaMovement(0.0D, fighter.getDeltaMovement().y, 0.0D);
    }

    private void announce(String key) {
        announce(key, "", 2, 12, 4);
    }

    private void announce(String key, String subtitle, int fadeIn, int stay, int fadeOut) {
        ServerPlayer pupil = getDuelist();

        if (pupil != null) {
            PacketHandler.sendTo(new SCShowMessagesPacket(List.of(new Utils.Title(key, subtitle, fadeIn, stay, fadeOut))), pupil);
        }
    }

    @Nullable
    public ServerPlayer getDuelist() {
        if (duelist != null && fighter.level().getServer() != null) {
            ServerPlayer player = fighter.level().getServer().getPlayerList().getPlayer(duelist);

            if (player != null) {
                return player;
            }
        }

        return fighter.getTarget() instanceof ServerPlayer player ? player : null;
    }

    public void setDuelist(Player pupil) {
        this.duelist = pupil == null ? null : pupil.getUUID();
        this.readyIn = pupil == null ? 0 : Dueller.READY_TICKS;

        if (readyIn <= 0) {
            return;
        }

        fighter.setNoAi(true);
        fighter.setInvulnerable(true);
        dueller.startCallingKeyblade();
    }

    public boolean isDuelist(Player pupil) {
        return duelist != null && pupil != null && duelist.equals(pupil.getUUID());
    }

    public void clear() {
        getUp();

        down = false;
        duelist = null;
        leavingTicks = 0;
        readyIn = 0;
        endingTicks = FIGHTING;
    }
}
