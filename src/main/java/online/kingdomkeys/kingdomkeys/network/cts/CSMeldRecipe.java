package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.MagicSpellItem;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.synthesis.melding.Melding;
import online.kingdomkeys.kingdomkeys.synthesis.melding.MeldingRegistry;

public record CSMeldRecipe(ResourceLocation recipe) implements Packet {

	public static final Type<CSMeldRecipe> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_meld_recipe"));

	public static final StreamCodec<FriendlyByteBuf, CSMeldRecipe> STREAM_CODEC = StreamCodec.composite(ResourceLocation.STREAM_CODEC, CSMeldRecipe::recipe, CSMeldRecipe::new);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();

		Melding melding = MeldingRegistry.getInstance().getValue(recipe);

		if (melding == null)
			return;

		PlayerData playerData = PlayerData.get(player);

		if (playerData.getMunny() < melding.getCost())
			return;

		int slot1 = -1;
		int slot2 = -1;

		for (int i = 0; i < player.getInventory().items.size(); i++) {
			ItemStack stack = player.getInventory().items.get(i);
			if (stack.isEmpty())
				continue;

			if (!(stack.getItem() instanceof MagicSpellItem spell))
				continue;

			if (spell.getExpPercent(stack) < 1F)
				continue;

			//First ingredient
			if (slot1 == -1 && stack.getItem() == melding.getIngredient1()) {
				slot1 = i;
				continue;
			}

			//Second ingredient
			if (slot2 == -1 && stack.getItem() == melding.getIngredient2()) {
				//Avoid using the same slot twice
				if (i != slot1) {
					slot2 = i;
				}
			}
		}

		//No ingredients
		if (slot1 == -1 || slot2 == -1) return;

		player.getInventory().removeItem(slot1, 1);
		player.getInventory().removeItem(slot2, 1);

		playerData.setMunny(playerData.getMunny() - melding.getCost());

		ItemStack result = new ItemStack(melding.getResult(), melding.getAmount());
		player.getInventory().add(result);

		PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}