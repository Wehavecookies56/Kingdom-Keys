package online.kingdomkeys.kingdomkeys.network.packet;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class CSUpgradeSynthesisBagPacket {

	public CSUpgradeSynthesisBagPacket() {

	}

	public void encode(PacketBuffer buffer) {
	}

	public static CSUpgradeSynthesisBagPacket decode(PacketBuffer buffer) {
		CSUpgradeSynthesisBagPacket msg = new CSUpgradeSynthesisBagPacket();
		return msg;
	}

	public static void handle(CSUpgradeSynthesisBagPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			ItemStack stack = player.getHeldItemMainhand();
			CompoundNBT nbt = stack.getOrCreateTag();
			nbt.putInt("level", nbt.getInt("level")+1);
		});
		ctx.get().setPacketHandled(true);
	}

}
