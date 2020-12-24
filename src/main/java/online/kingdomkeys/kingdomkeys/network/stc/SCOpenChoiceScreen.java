package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.ConfirmChoiceMenuPopup;
import online.kingdomkeys.kingdomkeys.client.gui.SoAMessages;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.SoAState;

import java.util.function.Supplier;

public class SCOpenChoiceScreen {

	SoAState choice, state;
	BlockPos pos;

	public SCOpenChoiceScreen() { }

	public SCOpenChoiceScreen(ItemStack choiceItem, SoAState state, BlockPos pos) {
		if (choiceItem.getItem() == ModItems.dreamSword.get()) {
			choice = SoAState.WARRIOR;
		} else if (choiceItem.getItem() == ModItems.dreamShield.get()) {
			choice = SoAState.GUARDIAN;
		} else if (choiceItem.getItem() == ModItems.dreamStaff.get()) {
			choice = SoAState.MYSTIC;
		} else {
			this.choice = SoAState.NONE;
		}
		this.state = state;
		this.pos = pos;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeByte(choice.get());
		buffer.writeByte(state.get());
		buffer.writeBlockPos(pos);
	}

	public static SCOpenChoiceScreen decode(PacketBuffer buffer) {
		SCOpenChoiceScreen msg = new SCOpenChoiceScreen();
		msg.choice = SoAState.fromByte(buffer.readByte());
		msg.state = SoAState.fromByte(buffer.readByte());
		msg.pos = buffer.readBlockPos();
		return msg;
	}

	public static void handle(final SCOpenChoiceScreen message, Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
			ctx.get().enqueueWork(() -> ClientHandler.handle(message));
		ctx.get().setPacketHandled(true);
	}

	public static class ClientHandler {
		@OnlyIn(Dist.CLIENT)
		public static void handle(SCOpenChoiceScreen message) {
			KingdomKeys.proxy.getClientMCInstance().displayGuiScreen(new ConfirmChoiceMenuPopup(message.state, message.choice, message.pos));
			SoAMessages.INSTANCE.clearMessage();
		}
	}

}
