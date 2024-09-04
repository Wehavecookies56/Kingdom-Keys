package online.kingdomkeys.kingdomkeys.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.Recipe;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class RecipeItem extends Item implements IItemCategory {
	int tier=0;
	
	public RecipeItem(int tier,Properties properties) {
		super(properties);
		this.tier = tier;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		if (hand == InteractionHand.MAIN_HAND) {
			if (!world.isClientSide) {
				ItemStack stack = player.getMainHandItem();

				//Allow recipes to be given with pre-set keyblades
				//If the player already has learnt them, the recipe item will be refreshed to try get new recipes.
				PlayerData playerData = PlayerData.get(player);
				if(tier <= playerData.getSynthLevel()) { //If the player has the right tier
					if (stack.has(ModComponents.RECIPES)) { //If the recipe has been generated learn it
						learnRecipes(player, stack);
					} else { //Otherwise generate it
						List<ResourceLocation> missingKeyblades = getMissingRecipes(playerData, "keyblade", tier);
						List<ResourceLocation> missingItems = getMissingRecipes(playerData, "item", tier);

						List<String> types = new ArrayList<String>();
						types.add("keyblade");
						types.add("item");
						if (missingKeyblades.size() == 0) {
							types.remove("keyblade");
						}
						if (missingItems.size() == 0) {
							types.remove("item");
						}

						String type = "";
						if (types.size() > 1) {
							int num = world.random.nextInt(types.size());
							type = types.get(num);
						} else if (types.size() == 1) {
							type = types.get(0);
						} else {
							player.displayClientMessage(Component.translatable("message.recipe.no_more_to_learn"), true);
							return super.use(world, player, hand);
						}

						//Set up the recipe item with the given type
						//We get here if there are recipes still available to learn.
						shuffleRecipes(stack, player, type);
					}
				} else { //If the player tier is not enough don't even try to generate it
					player.displayClientMessage(Component.translatable("message.recipe.cant_learn_yet"), true);
				}
			}
		}
		return super.use(world, player, hand);
	}

	private void learnRecipes(Player player, ItemStack stack) {
		List<ResourceLocation> recipes = stack.getOrDefault(ModComponents.RECIPES, new Recipes("", new ArrayList<>())).recipes;
		PlayerData playerData = PlayerData.get(player);
		// /give Dev kingdomkeys:recipe{type:"keyblade",recipe1:"kingdomkeys:oathkeeper",recipe2:"kingdomkeys:fenrir"} 16

		if(tier > playerData.getSynthLevel()) {
			player.displayClientMessage(Component.translatable("message.recipe.cant_learn_yet"), true);
			return;
		}
		boolean consume = false;
		for (ResourceLocation recipe : recipes) {
			if (RecipeRegistry.getInstance().containsKey(recipe)) {
				ItemStack outputStack = new ItemStack(RecipeRegistry.getInstance().getValue(recipe).getResult());
				if (!RecipeRegistry.getInstance().containsKey(recipe)) { // If recipe is not valid
					String message = "ERROR: Recipe for " + Utils.translateToLocal(recipe.toString()) + " was not learnt because it is not a valid recipe, Report this to a dev";
					player.sendSystemMessage(Component.translatable(ChatFormatting.RED + message));
				} else if (playerData.hasKnownRecipe(recipe)) { // If recipe already known
					player.sendSystemMessage(Component.translatable(Utils.translateToLocal("message.recipe.already_learnt"),ChatFormatting.YELLOW+Utils.translateToLocal(outputStack.getDescriptionId())));
				} else { // If recipe is not known, learn it
					playerData.addKnownRecipe(recipe);
					consume = true;
					player.sendSystemMessage(Component.translatable(Utils.translateToLocal("message.recipe.learnt"), ChatFormatting.GREEN+Utils.translateToLocal(outputStack.getDescriptionId())));
					PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
				}
			}
		}

		if (consume) {
			//remove all child tags so we don't contaminate the stack, this will set the stack's tag field to null once all are removed.
			stack.remove(ModComponents.RECIPES);
			//reduce stack size by one.
			player.getMainHandItem().shrink(1);
		} else {
			//try for fresh recipes, based on what type this stack was set to. No swapping from keyblade to item recipes etc.
			//will fail successfully if none left.
			shuffleRecipes(stack, player, stack.get(ModComponents.RECIPES).type);
		}
	}

	public void shuffleRecipes(ItemStack stack, Player player, String type) {
		PlayerData playerData = PlayerData.get(player);
		List<ResourceLocation> list;
		List<ResourceLocation> newRecipes = new ArrayList<>();
		switch(type) {
		case "keyblade":
			list = getMissingRecipes(playerData, "keyblade", tier);
			int size = Math.min(list.size(), 3);
			for (int i = 0; i < size; i++) {
				int index = Utils.randomWithRange(0, list.size() - 1);
				newRecipes.add(list.get(index));
				list.remove(index);
			}
			break;
		case "item":
			list = getMissingRecipes(playerData, "item", tier);
			if(!list.isEmpty()) {
				newRecipes.add(list.get(Utils.randomWithRange(0, list.size() - 1)));
			}
			break;
		}

		stack.set(ModComponents.RECIPES, new Recipes(type, newRecipes));

		//Call learn recipes immediately.
		//This will remove all child tags and then reduce stack size by one
		//recipe1 is not null if any recipes exist to learn
		if (!newRecipes.isEmpty()) {
			learnRecipes(player, stack);
		}
	}

	private List<ResourceLocation> getMissingRecipes(PlayerData playerData, String type, int tier) {
		List<ResourceLocation> list = new ArrayList<>();
			for(Recipe r : RecipeRegistry.getInstance().getValues()) {
				if(!playerData.hasKnownRecipe(r.getRegistryName())) {
					if(r.getType().equals(type)) {
						if(tier == 0) {
							list.add(r.getRegistryName());
						} else {
							if(r.getTier() == tier) {
								list.add(r.getRegistryName());	
							}
						}
					}
				}
			}
		return list;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
		if (stack.has(ModComponents.RECIPES)) {
			List<ResourceLocation> recipes = stack.get(ModComponents.RECIPES).recipes;
			recipes.forEach(recipeRL -> {
				if(RecipeRegistry.getInstance().containsKey(recipeRL)) {
					Recipe recipe = RecipeRegistry.getInstance().getValue(recipeRL);
					if (recipe != null) {
						String name;
						if(recipe.getType().equals("keyblade")) {
							KeychainItem item = (KeychainItem) recipe.getResult();
							name = new ItemStack(item.keyblade).getDescriptionId();
						} else {
							name = new ItemStack(recipe.getResult()).getDescriptionId();
						}
						tooltip.add(Component.translatable(Utils.translateToLocal(name)));
					}
				}
			});
		}
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.MISC;
	}

	public record Recipes(String type, List<ResourceLocation> recipes) {
		public static final Codec<Recipes> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					Codec.STRING.fieldOf("type").forGetter(Recipes::type),
					Codec.list(ResourceLocation.CODEC).fieldOf("recipes").forGetter(Recipes::recipes)
				).apply(instance, Recipes::new)
		);
		public static final StreamCodec<FriendlyByteBuf, Recipes> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.STRING_UTF8,
				Recipes::type,
				ByteBufCodecs.collection(ArrayList::new, ResourceLocation.STREAM_CODEC),
				Recipes::recipes,
				Recipes::new
		);
	}
}
