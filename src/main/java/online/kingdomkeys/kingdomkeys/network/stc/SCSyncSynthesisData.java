package online.kingdomkeys.kingdomkeys.network.stc;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.nbt.CompoundNBT;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.Recipe;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;
import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeDataDeserializer;

public class SCSyncSynthesisData {

	public SCSyncSynthesisData() {
	}

	List<Recipe> recipes = new LinkedList<>();
	
	public SCSyncSynthesisData(List<Recipe> recipes) {
		this.recipes = recipes;
	}
	
	public void encode(PacketBuffer buffer) {
		buffer.writeInt(recipes.size());
		CompoundNBT compoundNBT = new CompoundNBT();
		for(int i = 0; i < recipes.size(); i++) {
			compoundNBT.put("recipe"+i, recipes.get(i).serializeNBT());
		}
		buffer.writeCompoundTag(compoundNBT);
	}

	public static SCSyncSynthesisData decode(PacketBuffer buffer) {
		SCSyncSynthesisData msg = new SCSyncSynthesisData();
		int size = buffer.readInt();
		CompoundNBT compoundNBT = buffer.readCompoundTag();
		for (int i = 0; i < size; i++) {
			Recipe r = new Recipe();
			r.deserializeNBT((CompoundNBT) compoundNBT.get("recipe"+i));
			msg.recipes.add(r);
		}
		return msg;	
	}

	public static void handle(final SCSyncSynthesisData message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = KingdomKeys.proxy.getClientPlayer();

			RecipeRegistry.getInstance().clearRegistry();

			message.recipes.forEach(recipe -> {
				RecipeRegistry.getInstance().register(recipe);
			});
		});
		ctx.get().setPacketHandled(true);
	}

}
