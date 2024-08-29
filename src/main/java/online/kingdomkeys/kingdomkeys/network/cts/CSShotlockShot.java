package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSShotlockShot {
	
	List<Utils.ShotlockPosition> shotlockEnemies;
	List<Integer> shotlockList;
	double cost;
	
	public CSShotlockShot() {}

	public CSShotlockShot(double cost, List<Utils.ShotlockPosition> shotlockEnemies) {
		this.cost = cost;
		this.shotlockEnemies = shotlockEnemies;
	}


	public void encode(FriendlyByteBuf buffer) {
		buffer.writeDouble(this.cost);
		buffer.writeInt(this.shotlockEnemies.size());
		for(int i= 0; i< this.shotlockEnemies.size(); i++) {
			buffer.writeInt(this.shotlockEnemies.get(i).id());
		}
	}

	public static CSShotlockShot decode(FriendlyByteBuf buffer) {
		CSShotlockShot msg = new CSShotlockShot();
		msg.cost = buffer.readDouble();
		int size = buffer.readInt();
		msg.shotlockList = new ArrayList<Integer>();

		for(int i= 0; i< size; i++) {
			msg.shotlockList.add(buffer.readInt());
		}

		return msg;
	}

	public static void handle(CSShotlockShot message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();

			IPlayerData playerData = ModData.getPlayer(player);
			Shotlock shotlock = Utils.getPlayerShotlock(player);
			
			List<Entity> targets = new ArrayList<Entity>();
			
			for(int enemyID : message.shotlockList) {
				Entity target = player.level().getEntity(enemyID);
				targets.add(target);
			}
			
			playerData.setHasShotMaxShotlock(targets.size() == shotlock.getMaxLocks());

			shotlock.onUse(player, targets);
			playerData.remFocus(message.cost);
			PacketHandler.syncToAllAround(player, playerData);
			
		});
		ctx.get().setPacketHandled(true);
	}

}