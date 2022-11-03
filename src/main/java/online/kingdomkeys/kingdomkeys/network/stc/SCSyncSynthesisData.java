package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.Recipe;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class SCSyncSynthesisData {

	public SCSyncSynthesisData() {
	}

	public List<Recipe> recipes = new LinkedList<>();
	
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
		ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientUtils.syncSynthesisData(message)));
		ctx.get().setPacketHandled(true);
	}

}
