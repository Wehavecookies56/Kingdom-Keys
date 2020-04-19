package online.kingdomkeys.kingdomkeys.network.magic;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.ILevelCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.magic.EntityFire;

public class PacketMagicFire {


	private static final int FIRE_MAGIC_COST = 10;

	public PacketMagicFire() {

	}

	public void encode(PacketBuffer buffer) {

	}

	public static PacketMagicFire decode(PacketBuffer buffer) {
		PacketMagicFire msg = new PacketMagicFire();
		return msg;
	}

	public static void handle(PacketMagicFire message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			 ILevelCapabilities props = ModCapabilities.get(player);
			//if (props.getMagic() > FIRE_MAGIC_COST) {
				ThrowableEntity shot = new EntityFire(player.world, player);
				if (shot != null) {
					player.world.addEntity(shot);
					shot.shoot(player, player.rotationPitch, player.rotationYaw, 0, 1F, 0);
					//player.world.playSound(null, player.getPosition(), ModSounds.fistShot0, SoundCategory.MASTER, 1F, 1F);
					//player.world.playSound(null, player.getPosition(), Utils.getShootSound(player, message.charged), SoundCategory.MASTER, 1F, 1F);
					player.swingArm(Hand.MAIN_HAND);
				}
			//}
		});
		ctx.get().setPacketHandled(true);
	}

}
