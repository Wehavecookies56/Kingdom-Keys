package online.kingdomkeys.kingdomkeys.network.magic.old;
/*
package online.kingdomkeys.kingdomkeys.packets.magic;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Constants;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.packets.PacketHandler;
import uk.co.wehavecookies56.kk.common.network.packet.server.magic.EntityFira;
import uk.co.wehavecookies56.kk.common.network.packet.server.magic.EntityFiraga;
import uk.co.wehavecookies56.kk.common.network.packet.server.magic.FireEntity;
import uk.co.wehavecookies56.kk.common.network.packet.server.magic.EntityPlayerMP;
import uk.co.wehavecookies56.kk.common.network.packet.server.magic.SyncMagicData;

public class MagicFire {

	public MagicFire() {
	}

	public void encode(PacketBuffer buffer) {
		
	}

	public static MagicFire decode(PacketBuffer buffer) {
		MagicFire msg = new MagicFire();
		
		return msg;
	}

	public static void handle(final MagicFire message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			ModCapabilities.get(player).remMP(Constants.getCost(Strings.Spell_Fire));
			
			
	        World world = player.world;
	        switch (player.getCapability(ModCapabilities.MAGIC_STATE, null).getMagicLevel(Strings.Spell_Fire)) {
	            case 1:
	                world.spawnEntity(new FireEntity(world, player, player.posX, player.posY, player.posZ));
	                break;
	            case 2:
	                world.spawnEntity(new EntityFira(world, player, player.posX, player.posY, player.posZ));
	                break;
	            case 3:
	                world.spawnEntity(new EntityFiraga(world, player, player.posX, player.posY, player.posZ));
	                break;
	        }
	        PacketHandler.sendTo(new SyncMagicData(player.getCapability(ModCapabilities.MAGIC_STATE, null), player.getCapability(ModCapabilities.PLAYER_STATS, null)), (ServerPlayerEntity) player);
		});
		ctx.get().setPacketHandled(true);
	}

}
*/