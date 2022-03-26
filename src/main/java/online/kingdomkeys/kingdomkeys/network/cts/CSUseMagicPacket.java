package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSUseMagicPacket {
	
	String name, target;
	int level;
	
	public CSUseMagicPacket() {}

	public CSUseMagicPacket(String name, int level) {
		this.name = name;
		this.target = "";
		this.level = level;
	}
	
	public CSUseMagicPacket(String name, String target, int level) {
		this.name = name;
		this.target = target;
		this.level = level;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.name.length());
		buffer.writeUtf(this.name);
		buffer.writeInt(this.target.length());
		buffer.writeUtf(this.target);
		buffer.writeInt(this.level);
	}

	public static CSUseMagicPacket decode(FriendlyByteBuf buffer) {
		CSUseMagicPacket msg = new CSUseMagicPacket();
		int length = buffer.readInt();
		msg.name = buffer.readUtf(length);
		length = buffer.readInt();
		msg.target = buffer.readUtf(length);
		msg.level = buffer.readInt();
		return msg;
	}

	public static void handle(CSUseMagicPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
				if (playerData.getMP() >= 0 && !playerData.getRecharge()) {
					PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer)player);
					
					if(message.target.equals("")) {
		            	ModMagic.registry.get().getValue(new ResourceLocation(message.name)).onUse(player, player, message.level);
					} else {
						Player targetEntity = Utils.getPlayerByName(player.level, message.target);
		            	ModMagic.registry.get().getValue(new ResourceLocation(message.name)).onUse(targetEntity, player, message.level);
					}
				}
				
				PacketHandler.syncToAllAround(player, playerData);
			
		});
		ctx.get().setPacketHandled(true);
	}

}
