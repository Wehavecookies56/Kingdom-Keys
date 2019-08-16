package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.packets.PacketHandler;

public class Magic {
	public static void getMagic (PlayerEntity player, World world, String magic) {
        switch (magic) {
            case Strings.Spell_Fire:
                Fire(player, world);
                break;
            case Strings.Spell_Blizzard:
                Blizzard(player, world);
                break;
            case Strings.Spell_Cure:
                Cure(player, world);
                break;
            case Strings.Spell_Thunder:
                Thunder(player, world);
                break;
            case Strings.Spell_Aero:
                Aero(player, world);
                break;
            case Strings.Spell_Stop:
                Stop(player, world);
                break;
            default:
                break;
        }
    }

    public static void Fire (PlayerEntity player, World world) {
        if(!player.getCapability(ModCapabilities.MAGIC_STATE, null).getKH1Fire()) {
        	PacketHandler.sendToServer(new MagicFire());
        } else{
        	PacketHandler.sendToServer(new MagicKH1Fire());
        }
        player.swingArm(Hand.MAIN_HAND);
        world.playSound(player.posX, player.posY, player.posZ, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 1, 1, false);
    }

    public static void Blizzard (PlayerEntity player, World world) {
        PacketHandler.sendToServer(new MagicBlizzard());
        player.swingArm(Hand.MAIN_HAND);
        world.playSound(player.posX, player.posY, player.posZ, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1, 0, false);
    }

    public static void Thunder (PlayerEntity player, World world) {
    	PacketHandler.sendToServer(new MagicThunder());
        player.swingArm(Hand.MAIN_HAND);
    }

    public static void Cure (PlayerEntity player, World world) {
    	PacketHandler.sendToServer(new MagicCure());
        player.swingArm(Hand.MAIN_HAND);
        world.playSound(player.posX, player.posY, player.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1, 0, false);
    }

    public static void Aero (PlayerEntity player, World world) {
    	PacketHandler.sendToServer(new MagicAero());
        player.swingArm(Hand.MAIN_HAND);
        world.playSound(player.posX, player.posY, player.posZ, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 1, 1, false);
    }

    public static void Stop (PlayerEntity player, World world) {
    	PacketHandler.sendToServer(new MagicStop());
        player.swingArm(Hand.MAIN_HAND);
        world.playSound(player.posX, player.posY, player.posZ, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 1, 1, false);
    }
}
