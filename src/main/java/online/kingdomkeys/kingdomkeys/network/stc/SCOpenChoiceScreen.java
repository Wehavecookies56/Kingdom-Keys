package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record SCOpenChoiceScreen(SoAState choice, SoAState state, BlockPos pos) implements Packet {

	public static final Type<SCOpenChoiceScreen> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_open_choice_screen"));

	public static final StreamCodec<FriendlyByteBuf, SCOpenChoiceScreen> STREAM_CODEC = StreamCodec.composite(
		SoAState.STREAM_CODEC,
		SCOpenChoiceScreen::choice,
		SoAState.STREAM_CODEC,
		SCOpenChoiceScreen::state,
		BlockPos.STREAM_CODEC,
		SCOpenChoiceScreen::pos,
		SCOpenChoiceScreen::new
	);

	public SCOpenChoiceScreen(ItemStack choiceItem, SoAState state, BlockPos pos) {
		this(getChoiceFromItem(choiceItem), state, pos);
	}

	public static SoAState getChoiceFromItem(ItemStack choiceItem) {
		if (choiceItem.getItem() == ModItems.dreamSword.get()) {
			return SoAState.WARRIOR;
		} else if (choiceItem.getItem() == ModItems.dreamShield.get()) {
			return SoAState.GUARDIAN;
		} else if (choiceItem.getItem() == ModItems.dreamStaff.get()) {
			return SoAState.MYSTIC;
		} else {
			return SoAState.NONE;
		}
	}


	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			ClientPacketHandler.openChoice(this);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
