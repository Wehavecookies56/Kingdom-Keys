package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSUseMagicPacket {
	
	String name, allyTarget;
	int level, lockedTarget;
	
	public CSUseMagicPacket() {}

	public CSUseMagicPacket(String name, int level, LivingEntity lockedTarget) {
		this.name = name;
		this.allyTarget = "";
		this.level = level;

		this.lockedTarget = lockedTarget == null ? -1 : lockedTarget.getId();
	}
	
	public CSUseMagicPacket(String name, String target, int level) {
		this.name = name;
		this.allyTarget = target;
		this.level = level;
		this.lockedTarget = -1;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.name.length());
		buffer.writeUtf(this.name);
		buffer.writeInt(this.allyTarget.length());
		buffer.writeUtf(this.allyTarget);
		buffer.writeInt(this.level);
		buffer.writeInt(this.lockedTarget);
	}

	public static CSUseMagicPacket decode(FriendlyByteBuf buffer) {
		CSUseMagicPacket msg = new CSUseMagicPacket();
		int length = buffer.readInt();
		msg.name = buffer.readUtf(length);
		length = buffer.readInt();
		msg.allyTarget = buffer.readUtf(length);
		msg.level = buffer.readInt();
		msg.lockedTarget = buffer.readInt();
		return msg;
	}

	public static void handle(CSUseMagicPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
				if (playerData.getMP() >= 0 && !playerData.getRecharge()) {
					PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer)player);
					
					if(message.allyTarget.equals("")) { // Direct magic
						if(message.lockedTarget > -1) {
							ModMagic.registry.get().getValue(new ResourceLocation(message.name)).onUse(player, player, message.level, (LivingEntity) player.level.getEntity(message.lockedTarget));
						} else {
							ModMagic.registry.get().getValue(new ResourceLocation(message.name)).onUse(player, player, message.level, null);

						}
					} else { // On party member
						Player allyTargetEntity = Utils.getPlayerByName(player.level, message.allyTarget);
		            	ModMagic.registry.get().getValue(new ResourceLocation(message.name)).onUse(allyTargetEntity, player, message.level, null);
					}
				}
				
				PacketHandler.syncToAllAround(player, playerData);
			
		});
		ctx.get().setPacketHandled(true);
	}

}
