package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.capability.PlayerCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.ConfirmChoiceMenuPopup;
import online.kingdomkeys.kingdomkeys.client.gui.SoAMessages;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.lib.Strings;

import java.util.function.Supplier;

//This is only used when the player joins the world if they have left during the SoA so the pedestals hide the choices and messages show again
public class SCUpdateSoA {

	SoAState state;
	BlockPos choicePos, sacrificePos;

	public SCUpdateSoA() { }

	public SCUpdateSoA(IPlayerCapabilities playerData) {
		this.state = playerData.getSoAState();
		this.choicePos = playerData.getChoicePedestal();
		this.sacrificePos = playerData.getSacrificePedestal();
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeByte(this.state.get());
		buffer.writeBlockPos(this.choicePos);
		buffer.writeBlockPos(this.sacrificePos);
	}

	public static SCUpdateSoA decode(PacketBuffer buffer) {
		SCUpdateSoA msg = new SCUpdateSoA();
		msg.state = SoAState.fromByte(buffer.readByte());
		msg.choicePos = buffer.readBlockPos();
		msg.sacrificePos = buffer.readBlockPos();
		return msg;
	}

	public static void handle(final SCUpdateSoA message, Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
			ctx.get().enqueueWork(() -> ClientHandler.handle(message));
		ctx.get().setPacketHandled(true);
	}

	public static class ClientHandler {
		@OnlyIn(Dist.CLIENT)
		public static void handle(SCUpdateSoA message) {
			World world = Minecraft.getInstance().world;
			//Turns out TEs aren't loaded at this point, so not entirely sure how to do this
			TileEntity teChoice = world.getTileEntity(message.choicePos);
			TileEntity teSacrifice = world.getTileEntity(message.sacrificePos);
			if (teChoice != null) {
				((PedestalTileEntity) teChoice).hide = true;
			}
			if (teSacrifice != null) {
				((PedestalTileEntity) teSacrifice).hide = true;
			}
			if (message.state == SoAState.CHOICE) {
				SoAMessages.INSTANCE.clearMessage();
				SoAMessages.INSTANCE.queueMessages(
						new SoAMessages.Title(Strings.SoA_Title, Strings.SoA_Subtitle),
						new SoAMessages.Title(null, Strings.SoA_ChoiceIntro1, 10, 35, 20),
						new SoAMessages.Title(null, Strings.SoA_ChoiceIntro2, 10, 35, 20),
						new SoAMessages.Title(null, Strings.SoA_ChoiceIntro3, 10, 35, 20),
						new SoAMessages.Title(null, Strings.SoA_ChoiceIntro4, 10, 35, 20)
				);
			}
			if (message.state == SoAState.SACRIFICE) {
				SoAMessages.INSTANCE.clearMessage();
				SoAMessages.INSTANCE.queueMessages(
						new SoAMessages.Title(null, Strings.SoA_SacrificeIntro1, 10, 35, 20),
						new SoAMessages.Title(null, Strings.SoA_SacrificeIntro2, 10, 70, 20)
				);
			}
			if (message.state == SoAState.CONFIRM) {
				Minecraft.getInstance().displayGuiScreen(new ConfirmChoiceMenuPopup(SoAState.CONFIRM, SoAState.NONE, new BlockPos(0, 0, 0)));
			}
		}
	}

}
