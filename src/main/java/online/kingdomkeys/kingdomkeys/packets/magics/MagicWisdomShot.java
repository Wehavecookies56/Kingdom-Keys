package uk.co.wehavecookies56.kk.common.network.packet.server.magics;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import uk.co.wehavecookies56.kk.common.entity.magic.EntityWisdomShot;
import uk.co.wehavecookies56.kk.common.network.packet.AbstractMessage;
import uk.co.wehavecookies56.kk.common.network.packet.PacketDispatcher;
import uk.co.wehavecookies56.kk.common.network.packet.client.SpawnWisdomShotParticles;

public class MagicWisdomShot extends AbstractMessage.AbstractServerMessage<MagicWisdomShot> {

    public MagicWisdomShot () {}

    @Override
    protected void read (PacketBuffer buffer) throws IOException {

    }

    @Override
    protected void write (PacketBuffer buffer) throws IOException {

    }

    @Override
    public void process (EntityPlayer player, Side side) {
        World world = player.world;
        EntityWisdomShot wisdomshot = new EntityWisdomShot(world, player);
        world.spawnEntity(wisdomshot);
        wisdomshot.shoot(player, player.rotationPitch, player.rotationYaw, 0, 3, 0);
        PacketDispatcher.sendToAllAround(new SpawnWisdomShotParticles(new EntityWisdomShot(world)), player, 64.0D);
    }
}