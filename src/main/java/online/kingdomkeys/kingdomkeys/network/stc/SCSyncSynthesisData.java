package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.Recipe;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;

public class SCSyncSynthesisData {

	public SCSyncSynthesisData() {
	}

	List<Recipe> recipes = new LinkedList<>();
	
	public SCSyncSynthesisData(List<Recipe> recipes) {
		this.recipes = recipes;
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(recipes.size());
		CompoundTag compoundNBT = new CompoundTag();
		for(int i = 0; i < recipes.size(); i++) {
			compoundNBT.put("recipe"+i, recipes.get(i).serializeNBT());
		}
		buffer.writeNbt(compoundNBT);
	}

	public static SCSyncSynthesisData decode(FriendlyByteBuf buffer) {
		SCSyncSynthesisData msg = new SCSyncSynthesisData();
		int size = buffer.readInt();
		CompoundTag compoundNBT = buffer.readNbt();
		for (int i = 0; i < size; i++) {
			Recipe r = new Recipe();
			r.deserializeNBT((CompoundTag) compoundNBT.get("recipe"+i));
			msg.recipes.add(r);
		}
		return msg;	
	}

	public static void handle(final SCSyncSynthesisData message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = KingdomKeys.proxy.getClientPlayer();

			RecipeRegistry.getInstance().clearRegistry();

			message.recipes.forEach(recipe -> {
				RecipeRegistry.getInstance().register(recipe);
			});
		});
		ctx.get().setPacketHandled(true);
	}

}
