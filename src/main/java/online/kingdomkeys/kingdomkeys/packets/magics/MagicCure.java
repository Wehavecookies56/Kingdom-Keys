package uk.co.wehavecookies56.kk.common.network.packet.server.magics;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import uk.co.wehavecookies56.kk.common.capability.ModCapabilities;
import uk.co.wehavecookies56.kk.common.entity.magic.EntityCura;
import uk.co.wehavecookies56.kk.common.entity.magic.EntityCuraga;
import uk.co.wehavecookies56.kk.common.entity.magic.EntityCure;
import uk.co.wehavecookies56.kk.common.lib.Strings;
import uk.co.wehavecookies56.kk.common.network.packet.AbstractMessage;
import uk.co.wehavecookies56.kk.common.network.packet.PacketDispatcher;
import uk.co.wehavecookies56.kk.common.network.packet.client.SyncMagicData;

public class MagicCure extends AbstractMessage.AbstractServerMessage<MagicCure> {

    public MagicCure () {}

    @Override
    protected void read (PacketBuffer buffer) throws IOException {

    }

    @Override
    protected void write (PacketBuffer buffer) throws IOException {

    }

    @Override
    public void process (EntityPlayer player, Side side) {
        if (!player.getCapability(ModCapabilities.CHEAT_MODE, null).getCheatMode()) player.getCapability(ModCapabilities.PLAYER_STATS, null).setMP(0);
        World world = player.world;
        player.extinguish();
        switch (player.getCapability(ModCapabilities.MAGIC_STATE, null).getMagicLevel(Strings.Spell_Cure)) {
            case 1:
                if (player.getCapability(ModCapabilities.PLAYER_STATS, null).getHP() / 3 + player.getHealth() > player.getCapability(ModCapabilities.PLAYER_STATS, null).getHP())
                    player.heal(player.getCapability(ModCapabilities.PLAYER_STATS, null).getHP() - player.getHealth());
                else
                    player.heal(player.getCapability(ModCapabilities.PLAYER_STATS, null).getHP() / 3);
                world.spawnEntity(new EntityCure(world, player, player.posX, player.posY, player.posZ));
                break;
            case 2:
                if (player.getCapability(ModCapabilities.PLAYER_STATS, null).getHP() / 3 * 2 + player.getHealth() > player.getCapability(ModCapabilities.PLAYER_STATS, null).getHP())
                    player.heal(player.getCapability(ModCapabilities.PLAYER_STATS, null).getHP() - player.getHealth());
                else
                    player.heal(player.getCapability(ModCapabilities.PLAYER_STATS, null).getHP() / 3 * 2);
                world.spawnEntity(new EntityCura(world, player, player.posX, player.posY, player.posZ));
                break;
            case 3:
                player.heal(player.getCapability(ModCapabilities.PLAYER_STATS, null).getHP() - player.getHealth());
                world.spawnEntity(new EntityCuraga(world, player, player.posX, player.posY, player.posZ));
                break;
        }
        PacketDispatcher.sendTo(new SyncMagicData(player.getCapability(ModCapabilities.MAGIC_STATE, null), player.getCapability(ModCapabilities.PLAYER_STATS, null)), (EntityPlayerMP) player);

    }
}
