package online.kingdomkeys.kingdomkeys.network.magic;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.magic.EntityFire;
import online.kingdomkeys.kingdomkeys.entity.magic.Magics;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.PacketSyncCapability;

public class PacketUseMagic {

	private static final int FIRE_MAGIC_COST = 10;

	public PacketUseMagic() {

	}

	public void encode(PacketBuffer buffer) {

	}

	public static PacketUseMagic decode(PacketBuffer buffer) {
		PacketUseMagic msg = new PacketUseMagic();
		return msg;
	}

	public static void handle(PacketUseMagic message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			 IPlayerCapabilities props = ModCapabilities.get(player);
			// props.setMaxMP(120);
			// props.setMP(props.getMaxMP());
			if (props.getMP() >= 0 && !props.getRecharge()) {
				props.remMP(FIRE_MAGIC_COST);
				PacketHandler.sendTo(new PacketSyncCapability(props), (ServerPlayerEntity)player);
				Magics.gravity(player);
			}
			 System.out.println(props.getMP());

		});
		ctx.get().setPacketHandled(true);
	}

}
