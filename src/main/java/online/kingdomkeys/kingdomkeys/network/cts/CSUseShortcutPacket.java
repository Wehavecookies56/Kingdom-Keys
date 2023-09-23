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
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public class CSUseShortcutPacket {

	int index;
	int lockOnTarget;

	public CSUseShortcutPacket() {
	}

	public CSUseShortcutPacket(int index, LivingEntity lockOnTarget) {
		this.index = index;
		System.out.println("Shortcut P: "+lockOnTarget);
		this.lockOnTarget = lockOnTarget == null ? -1 : lockOnTarget.getId();
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.index);
		buffer.writeInt(this.lockOnTarget);
	}

	public static CSUseShortcutPacket decode(FriendlyByteBuf buffer) {
		CSUseShortcutPacket msg = new CSUseShortcutPacket();
		msg.index = buffer.readInt();
		msg.lockOnTarget = buffer.readInt();
		return msg;
	}

	public static void handle(CSUseShortcutPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			if(playerData.getMagicCooldownTicks() <= 0 && !playerData.getRecharge() && !playerData.getActiveDriveForm().equals(Strings.Form_Valor)) {
				if (playerData.getShortcutsMap().containsKey(message.index)) {
					String[] data = playerData.getShortcutsMap().get(message.index).split(",");
					String magicName = data[0];
					int level = Integer.parseInt(data[1]);
					Magic magic = ModMagic.registry.get().getValue(new ResourceLocation(magicName));
					double cost = magic.getCost(level, player);
					
					if(playerData.getMaxMP() == 0 || playerData.getRecharge() || cost > playerData.getMaxMP() && cost < 300 || cost < 300 && cost >= playerData.getMP() && playerData.isAbilityEquipped(Strings.mpSafety) || playerData.getMagicCooldownTicks() > 0) {
					
					} else {
						magic.onUse(player, player, level, (LivingEntity) player.level.getEntity(message.lockOnTarget));
					}
					
					PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
				}
			}

		});
		ctx.get().setPacketHandled(true);
	}

}