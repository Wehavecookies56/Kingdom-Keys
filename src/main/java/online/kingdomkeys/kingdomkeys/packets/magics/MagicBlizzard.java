package uk.co.wehavecookies56.kk.common.network.packet.server.magics;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import uk.co.wehavecookies56.kk.common.capability.ModCapabilities;
import uk.co.wehavecookies56.kk.common.entity.magic.EntityBlizzaga;
import uk.co.wehavecookies56.kk.common.entity.magic.EntityBlizzara;
import uk.co.wehavecookies56.kk.common.entity.magic.EntityBlizzard;
import uk.co.wehavecookies56.kk.common.lib.Constants;
import uk.co.wehavecookies56.kk.common.lib.Strings;
import uk.co.wehavecookies56.kk.common.network.packet.AbstractMessage;
import uk.co.wehavecookies56.kk.common.network.packet.PacketDispatcher;
import uk.co.wehavecookies56.kk.common.network.packet.client.SpawnBlizzardParticles;
import uk.co.wehavecookies56.kk.common.network.packet.client.SyncMagicData;

public class MagicBlizzard extends AbstractMessage.AbstractServerMessage<MagicBlizzard> {

    public MagicBlizzard () {}

    @Override
    protected void read (PacketBuffer buffer) throws IOException {

    }

    @Override
    protected void write (PacketBuffer buffer) throws IOException {

    }

    @Override
    public void process (EntityPlayer player, Side side) {
        if (!player.getCapability(ModCapabilities.CHEAT_MODE, null).getCheatMode()) player.getCapability(ModCapabilities.PLAYER_STATS, null).remMP(Constants.getCost(Strings.Spell_Blizzard));
        World world = player.world;

        int distance = 5;
        switch (player.getCapability(ModCapabilities.MAGIC_STATE, null).getMagicLevel(Strings.Spell_Blizzard)) {
            case 1:
                EntityBlizzard entityBlizzard = new EntityBlizzard(world, player);
                world.spawnEntity(entityBlizzard);
                entityBlizzard.shoot(player, player.rotationPitch, player.rotationYaw, 0, 1, 0);
                break;
            case 2:
            	for(int i=0;i<3;i++) {
                    EntityBlizzara entityBlizzara = new EntityBlizzara(world, player);
                    world.spawnEntity(entityBlizzara);
                    entityBlizzara.shoot(player, player.rotationPitch, player.rotationYaw-distance+(distance*i), 0, 2, 0);
        		}
                break;
            case 3:
            	for(int i=0;i<3;i++) {
                    EntityBlizzaga entityBlizzaga = new EntityBlizzaga(world, player);
                    world.spawnEntity(entityBlizzaga);
                    entityBlizzaga.shoot(player, player.rotationPitch-distance+(distance*i), player.rotationYaw, 0, 3, 0);
            	}
            	
            	for(int i=0;i<3;i++) {
                    EntityBlizzaga entityBlizzaga = new EntityBlizzaga(world, player);
                    world.spawnEntity(entityBlizzaga);
                    entityBlizzaga.shoot(player, player.rotationPitch, player.rotationYaw-distance+(distance*i), 0, 3, 0);
        		}
            	
                break;
        }
        world.playSound(player.posX, player.posY, player.posZ, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1, 1, false);

        PacketDispatcher.sendTo(new SyncMagicData(player.getCapability(ModCapabilities.MAGIC_STATE, null), player.getCapability(ModCapabilities.PLAYER_STATS, null)), (EntityPlayerMP) player);
    }

}
