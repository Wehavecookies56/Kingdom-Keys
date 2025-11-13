package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.entity.GummiShotEntity;

public class GummiWeaponBlock extends GummiBlockEdge {

    int firepower, fuelPerShot;
    SHOT_TYPE shotType;
    public enum SHOT_TYPE {
        FIRE, FIRA, FIRAGA, BLIZZARD, BLIZZARA, BLIZZAGA, GRAVITY, GRAVIRA, GRAVIGA;

        SHOT_TYPE getTextureName(){
            return switch (this) {
                case FIRE, FIRA, FIRAGA -> FIRE;
                case BLIZZARD, BLIZZARA, BLIZZAGA -> BLIZZARD;
                case GRAVITY, GRAVIRA, GRAVIGA -> GRAVITY;
            };
        }
    }

    public GummiWeaponBlock(Properties properties, SHOT_TYPE shotType, int weight, int armour, int firepower, int fuelPerShot) {
        super(properties, weight, armour, null, null);
        this.firepower = firepower;
        this.shotType = shotType;
        this.fuelPerShot = fuelPerShot;
    }

    public int getFirepower() {
        return firepower;
    }

    public int getFuelPerShot(){
        return fuelPerShot;
    }

    public void castShot(Player player, GummiShipEntity ship, float dmg, Vec3 pos, float xOff, float yOff, float speed){
        Level level = player.level();
        GummiShotEntity shot = new GummiShotEntity(level, player, shotType.getTextureName().name().toLowerCase(), dmg);
        level.addFreshEntity(shot);
        shot.setPos(pos);
        shot.shootFromRotation(ship, player.getXRot()+xOff, player.getYRot()+yOff, 0, speed, 0);
    }

    public void shoot(Player player, GummiShipEntity ship, Vec3 finalPos){
        float speed = 4F;

        switch(shotType){
            case FIRE, FIRA, FIRAGA-> {
                castShot(player, ship, getFirepower(), finalPos,0,0, speed);
                player.level().playSound(null, player.blockPosition(), ModSounds.laser.get(), SoundSource.PLAYERS, 4F, 1F);
            }
            case BLIZZARD -> {
                castShot(player,ship, getFirepower(), finalPos,0,-3, speed);
                castShot(player,ship, getFirepower(), finalPos,0,+3, speed);
                player.level().playSound(null, player.blockPosition(), ModSounds.laser.get(), SoundSource.PLAYERS, 4F, 1F);
            }
            case BLIZZARA -> {
                castShot(player,ship, getFirepower(), finalPos,-3,0, speed);
                castShot(player,ship, getFirepower(), finalPos,3,3, speed);
                castShot(player,ship, getFirepower(), finalPos,3,-3, speed);
                player.level().playSound(null, player.blockPosition(), ModSounds.laser.get(), SoundSource.PLAYERS, 4F, 1F);
            }
            case BLIZZAGA -> {
                castShot(player,ship, getFirepower(), finalPos,-6,0, speed);
                castShot(player,ship, getFirepower(), finalPos,6,0, speed);
                castShot(player,ship, getFirepower(), finalPos,0,-6, speed);
                castShot(player,ship, getFirepower(), finalPos,0,6, speed);
                player.level().playSound(null, player.blockPosition(), ModSounds.laser.get(), SoundSource.PLAYERS, 4F, 1F);
            }
            case GRAVITY, GRAVIRA, GRAVIGA-> {
                castShot(player, ship, getFirepower(), finalPos,0,0, 0.75F);
                player.level().playSound(null, player.blockPosition(), ModSounds.laser.get(), SoundSource.PLAYERS, 4F, 1F);
            }

        }
        ship.remFuel(getFuelPerShot());
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
