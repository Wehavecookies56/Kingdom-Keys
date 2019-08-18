/*
package online.kingdomkeys.kingdomkeys.packets.magics;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import uk.co.wehavecookies56.kk.common.capability.ModCapabilities;
import uk.co.wehavecookies56.kk.common.entity.magic.EntityAero;
import uk.co.wehavecookies56.kk.common.entity.magic.EntityAeroga;
import uk.co.wehavecookies56.kk.common.entity.magic.EntityAerora;
import uk.co.wehavecookies56.kk.common.lib.Constants;
import uk.co.wehavecookies56.kk.common.lib.Strings;
import uk.co.wehavecookies56.kk.common.network.packet.AbstractMessage.AbstractServerMessage;
import uk.co.wehavecookies56.kk.common.network.packet.PacketDispatcher;
import uk.co.wehavecookies56.kk.common.network.packet.client.SpawnAeroParticles;
import uk.co.wehavecookies56.kk.common.network.packet.client.SyncMagicData;

public class MagicAero extends AbstractServerMessage<MagicAero> {

    public MagicAero () {}

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
        if (!world.isRemote) {
            switch (player.getCapability(ModCapabilities.MAGIC_STATE, null).getMagicLevel(Strings.Spell_Aero)) {
            case 1:
                world.spawnEntity(new EntityAero(world, player, player.posX, player.posY, player.posZ, false));
                PacketDispatcher.sendToAllAround(new SpawnAeroParticles(player,1), player, 64.0D);
                break;
            case 2:
                world.spawnEntity(new EntityAerora(world, player, player.posX, player.posY, player.posZ));
                PacketDispatcher.sendToAllAround(new SpawnAeroParticles(player,2), player, 64.0D);
                break;
            case 3:
                world.spawnEntity(new EntityAeroga(world, player, player.posX, player.posY, player.posZ));
                PacketDispatcher.sendToAllAround(new SpawnAeroParticles(player,3), player, 64.0D);
                break;
            }
            PacketDispatcher.sendTo(new SyncMagicData(player.getCapability(ModCapabilities.MAGIC_STATE, null), player.getCapability(ModCapabilities.PLAYER_STATS, null)), (EntityPlayerMP) player);
        }
    }

}
 */
