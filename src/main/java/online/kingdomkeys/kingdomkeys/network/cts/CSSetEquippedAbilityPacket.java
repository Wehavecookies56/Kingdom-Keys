package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.ability.AbilityEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSSetEquippedAbilityPacket {

	String ability;
	int level;

	public CSSetEquippedAbilityPacket() {
	}

	public CSSetEquippedAbilityPacket(String ability, int level) {
		this.ability = ability;
		this.level = level;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.ability.length());
		buffer.writeUtf(this.ability);
		buffer.writeInt(this.level);
	}

	public static CSSetEquippedAbilityPacket decode(FriendlyByteBuf buffer) {
		CSSetEquippedAbilityPacket msg = new CSSetEquippedAbilityPacket();
		int length = buffer.readInt();
		msg.ability = buffer.readUtf(length);
		msg.level = buffer.readInt();
		return msg;
	}

	public static void handle(CSSetEquippedAbilityPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			boolean cancelled;
			if (playerData.isAbilityEquipped(message.ability, message.level)) {
				cancelled = MinecraftForge.EVENT_BUS.post(new AbilityEvent.Unequip(ModAbilities.registry.get().getValue(new ResourceLocation(message.ability)), message.level, player, false));
			} else {
				cancelled = MinecraftForge.EVENT_BUS.post(new AbilityEvent.Equip(ModAbilities.registry.get().getValue(new ResourceLocation(message.ability)), message.level, player, false));
			}
			if (!cancelled) {
				playerData.equipAbilityToggle(message.ability, message.level);
				Utils.RefreshAbilityAttributes(player, playerData);
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
