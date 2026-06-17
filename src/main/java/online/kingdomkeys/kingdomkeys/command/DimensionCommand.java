package online.kingdomkeys.kingdomkeys.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;

import java.util.Collection;
import java.util.List;

public class DimensionCommand extends BaseCommand {

	public static SuggestionProvider<CommandSourceStack> SUGGEST_DIMENSIONS = (p_198296_0_, p_198296_1_) -> {
		List<String> list = ServerLifecycleHooks.getCurrentServer().levelKeys().stream().map(rk -> rk.location().toString()).toList();
		return SharedSuggestionProvider.suggest(list.stream().map(StringArgumentType::escapeIfRequired), p_198296_1_);
	};

	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("dimension").requires(source -> source.hasPermission(2));
		builder.then(Commands.argument("dim", StringArgumentType.string()).suggests(SUGGEST_DIMENSIONS).then(Commands.argument("targets", EntityArgument.players()).executes(DimensionCommand::changeDim)).executes(DimensionCommand::changeDim));

		KingdomKeys.LOGGER.warn("Registered command " + builder.getLiteral());
		return builder;
	}

	private static int changeDim(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 3);
		String dim = StringArgumentType.getString(context, "dim");
		ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(dim));
		if (!ServerLifecycleHooks.getCurrentServer().levelKeys().stream().toList().contains(dimension)) {
			context.getSource().sendFailure(Component.literal("Dimension '"+dim+ "' does not exist"));
			return 0;
		}
		for (ServerPlayer player : players) {
			BlockPos coords = getWorldCoords(player, dimension);
			player.changeDimension(new DimensionTransition(player.getServer().getLevel(dimension), new Vec3(coords.getX(), coords.getY(), coords.getZ()), Vec3.ZERO, player.getYRot(), player.getXRot(), entity -> {}));
			context.getSource().sendSuccess(() -> Component.translatable("Teleported " + player.getDisplayName().getString() + " to dimension " + dimension.location()), true);
			player.sendSystemMessage(Component.translatable("You have been teleported to " + dimension.location()));
		}
		return 1;
	}

	public static BlockPos getWorldCoords(Player player, ResourceKey<Level> dimension) {
		if (dimension.location().toString().contains("castle_oblivion_interior_")) {
			return new BlockPos(8, 62, 8);
		}
		if (dimension == ModDimensions.DIVE_TO_THE_HEART) {
			return new BlockPos(0, 26, 0);
		}
		if (dimension == ModDimensions.STATION_OF_SORROW) {
			return new BlockPos(0, 26, 0);
		}
		if (dimension == ModDimensions.CASTLE_OBLIVION) {
			if (!FMLEnvironment.production) {
				player.getInventory().add(new ItemStack(ModItems.plainsCard.get()));
				player.getInventory().add(new ItemStack(ModItems.theNetherCard.get()));
				ItemStack nineCard = new ItemStack(ModItems.tranquilDarkness.get());
				nineCard.set(ModComponents.CARD_VALUE, 9);
				nineCard.setCount(64);
				player.getInventory().add(nineCard);
				return new BlockPos(-6, 90, 8);
			} else {
				return new BlockPos(-2, 90, -167);
			}
		}
		if(dimension.location().toString().contains("realm_of_darkness")) {
			return player.getServer().getLevel(dimension).getSharedSpawnPos();
		}

		PlayerData playerData = PlayerData.get(player);
		if (dimension == playerData.getReturnDimension()) {
			Vec3 vec3 = playerData.getReturnLocation();
			//TODO fix cast
			return new BlockPos((int)vec3.x, (int)vec3.y, (int)vec3.z);
		}
		return new BlockPos(0, 64, 0);
	}
}
