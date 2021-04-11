package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.magic.FireEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.ShotlockCoreEntity;
import online.kingdomkeys.kingdomkeys.limit.Limit;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSShotlockShot {
	
	List<Integer> shotlockEnemies;
	
	public CSShotlockShot() {}

	public CSShotlockShot(List<Integer> shotlockEnemies) {
		this.shotlockEnemies = shotlockEnemies;
	}


	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.shotlockEnemies.size());
		for(int i= 0; i< this.shotlockEnemies.size(); i++) {
			buffer.writeInt(this.shotlockEnemies.get(i));
		}
	}

	public static CSShotlockShot decode(PacketBuffer buffer) {
		CSShotlockShot msg = new CSShotlockShot();
		int size = buffer.readInt();
		msg.shotlockEnemies = new ArrayList<Integer>();

		for(int i= 0; i< size; i++) {
			msg.shotlockEnemies.add(buffer.readInt());
		}

		return msg;
	}

	public static void handle(CSShotlockShot message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			List<Entity> targets = new ArrayList<Entity>();
			for(int enemyID : message.shotlockEnemies) {
				Entity target = player.world.getEntityByID(enemyID);
				targets.add(target);
			}

			ShotlockCoreEntity core = new ShotlockCoreEntity(player.world, player, targets, 10);
			core.setPosition(player.getPosX(), player.getPosY(), player.getPosZ());
			player.world.addEntity(core);
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			
			PacketHandler.syncToAllAround(player, playerData);
			
		});
		ctx.get().setPacketHandled(true);
	}

}