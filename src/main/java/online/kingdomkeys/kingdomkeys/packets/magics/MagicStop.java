package uk.co.wehavecookies56.kk.common.network.packet.server.magics;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import uk.co.wehavecookies56.kk.common.capability.ModCapabilities;
import uk.co.wehavecookies56.kk.common.entity.magic.EntityStop;
import uk.co.wehavecookies56.kk.common.entity.magic.EntityStopga;
import uk.co.wehavecookies56.kk.common.entity.magic.EntityStopra;
import uk.co.wehavecookies56.kk.common.lib.Constants;
import uk.co.wehavecookies56.kk.common.lib.Strings;
import uk.co.wehavecookies56.kk.common.network.packet.AbstractMessage;
import uk.co.wehavecookies56.kk.common.network.packet.PacketDispatcher;
import uk.co.wehavecookies56.kk.common.network.packet.client.SpawnStopParticles;
import uk.co.wehavecookies56.kk.common.network.packet.client.SyncMagicData;

public class MagicStop extends AbstractMessage.AbstractServerMessage<MagicStop> {

    public MagicStop () {}

    @Override
    protected void read (PacketBuffer buffer) throws IOException {

    }

    @Override
    protected void write (PacketBuffer buffer) throws IOException {

    }

    @Override
    public void process (EntityPlayer player, Side side) {
        if (!player.getCapability(ModCapabilities.CHEAT_MODE, null).getCheatMode()) player.getCapability(ModCapabilities.PLAYER_STATS, null).remMP(Constants.getCost(Strings.Spell_Aero));
        World world = player.world;
        if (!world.isRemote) switch (player.getCapability(ModCapabilities.MAGIC_STATE, null).getMagicLevel(Strings.Spell_Stop)) {
            case 1:
                world.spawnEntity(new EntityStop(world, player, player.posX, player.posY, player.posZ));
                PacketDispatcher.sendToAllAround(new SpawnStopParticles(player,1), player, 64.0D);
                break;
            case 2:
                world.spawnEntity(new EntityStopra(world, player, player.posX, player.posY, player.posZ));
                PacketDispatcher.sendToAllAround(new SpawnStopParticles(player,2), player, 64.0D);
                break;
            case 3:
                world.spawnEntity(new EntityStopga(world, player, player.posX, player.posY, player.posZ));
                PacketDispatcher.sendToAllAround(new SpawnStopParticles(player,3), player, 64.0D);
                break;
        }
        PacketDispatcher.sendTo(new SyncMagicData(player.getCapability(ModCapabilities.MAGIC_STATE, null), player.getCapability(ModCapabilities.PLAYER_STATS, null)), (EntityPlayerMP) player);
    }

}
