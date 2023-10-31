package online.kingdomkeys.kingdomkeys.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mojang.brigadier.arguments.IntegerArgumentType;
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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.synthesis.material.ModMaterials;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MaterialCommand extends BaseCommand { // kk_material <give/take> <material/all> <amount> [player]
														// kk_material <take> <all> [player]

	private static final SuggestionProvider<CommandSourceStack> SUGGEST_MATERIALS = (p_198296_0_, p_198296_1_) -> {
		List<String> list = new ArrayList<>();
		for (ResourceLocation actual : ModMaterials.registry.get().getKeys()) {
			list.add(actual.toString());
		}
		return SharedSuggestionProvider.suggest(list.stream().map(StringArgumentType::escapeIfRequired), p_198296_1_);
	};

	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("material").requires(source -> source.hasPermission(2));

		builder.then(Commands.literal("give")

				.then(Commands.argument("material", StringArgumentType.string()).suggests(SUGGEST_MATERIALS).then(Commands.argument("amount", IntegerArgumentType.integer(1)).then(Commands.argument("targets", EntityArgument.players()).executes(MaterialCommand::addMaterial)).executes(MaterialCommand::addMaterial))).then(Commands.literal("all").then(Commands.argument("amount", IntegerArgumentType.integer(1)).then(Commands.argument("targets", EntityArgument.players()).executes(MaterialCommand::addAllMaterials)).executes(MaterialCommand::addAllMaterials))));

		builder.then(Commands.literal("take")

				.then(Commands.argument("material", StringArgumentType.string()).suggests(SUGGEST_MATERIALS).then(Commands.argument("amount", IntegerArgumentType.integer(1)).then(Commands.argument("targets", EntityArgument.players()).executes(MaterialCommand::takeMaterial)).executes(MaterialCommand::takeMaterial))).then(Commands.literal("all").then(Commands.argument("targets", EntityArgument.players()).executes(MaterialCommand::takeAllMaterials)).executes(MaterialCommand::takeAllMaterials))

		);

		builder.then(Commands.literal("set")

				.then(Commands.argument("material", StringArgumentType.string()).suggests(SUGGEST_MATERIALS).then(Commands.argument("amount", IntegerArgumentType.integer(1)).then(Commands.argument("targets", EntityArgument.players()).executes(MaterialCommand::setMaterial)).executes(MaterialCommand::setMaterial))).then(Commands.literal("all").then(Commands.argument("amount", IntegerArgumentType.integer(1)).then(Commands.argument("targets", EntityArgument.players()).executes(MaterialCommand::setAllMaterials)).executes(MaterialCommand::setAllMaterials))));

		KingdomKeys.LOGGER.warn("Registered command " + builder.getLiteral());
		return builder;
	}

	private static int addMaterial(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 5);
		String materialName = StringArgumentType.getString(context, "material");
		int amount = IntegerArgumentType.getInteger(context, "amount");
		Material material = ModMaterials.registry.get().getValue(new ResourceLocation(materialName));

		for (ServerPlayer player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.addMaterial(material, amount);

			context.getSource().sendSuccess(() -> Component.translatable("Given x" + amount + " '" + Utils.translateToLocal(material.getMaterialName()) + "' to " + player.getDisplayName().getString()), true);

			player.sendSystemMessage(Component.translatable("You have been given x" + amount + " '" + Utils.translateToLocal(material.getMaterialName()) + "'"));
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
		}
		return 1;
	}

	private static int takeMaterial(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 5);
		String materialName = StringArgumentType.getString(context, "material");
		int amount = IntegerArgumentType.getInteger(context, "amount");
		Material material = ModMaterials.registry.get().getValue(new ResourceLocation(materialName));

		for (ServerPlayer player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.removeMaterial(material, amount);

			context.getSource().sendSuccess(() -> Component.translatable("Removed material '" + Utils.translateToLocal(material.getMaterialName()) + "' from " + player.getDisplayName().getString()), true);

			player.sendSystemMessage(Component.translatable("x" + amount + " '" + Utils.translateToLocal(material.getMaterialName()) + "' have been taken away from you"));
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
		}
		return 1;
	}

	private static int addAllMaterials(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 5);
		int amount = IntegerArgumentType.getInteger(context, "amount");

		for (ServerPlayer player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			for (Material material : ModMaterials.registry.get().getValues()) {
				playerData.addMaterial(material, amount);
			}

			context.getSource().sendSuccess(() -> Component.translatable("Given all materials to " + player.getDisplayName().getString()), true);

			player.sendSystemMessage(Component.translatable("You have been given all the materials"));
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
		}
		return 1;
	}

	private static int takeAllMaterials(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 4);

		for (ServerPlayer player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.clearMaterials();

			context.getSource().sendSuccess(() -> Component.translatable("Taken all materials from " + player.getDisplayName().getString()), true);

			player.sendSystemMessage(Component.translatable("Your materials have been taken away"));
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
		}
		return 1;
	}

	private static int setMaterial(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 5);
		String materialName = StringArgumentType.getString(context, "material");
		int amount = IntegerArgumentType.getInteger(context, "amount");
		Material material = ModMaterials.registry.get().getValue(new ResourceLocation(materialName));

		for (ServerPlayer player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.setMaterial(material, amount);

			context.getSource().sendSuccess(() -> Component.translatable("Set x" + amount + " '" + Utils.translateToLocal(material.getMaterialName()) + "' to " + player.getDisplayName().getString()), true);

			player.sendSystemMessage(Component.translatable("Your '" + Utils.translateToLocal(material.getMaterialName()) + "' have been set to x" + amount));
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
		}
		return 1;
	}

	private static int setAllMaterials(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 5);
		int amount = IntegerArgumentType.getInteger(context, "amount");

		for (ServerPlayer player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			for (Material material : ModMaterials.registry.get().getValues()) {
				playerData.setMaterial(material, amount);
			}

			context.getSource().sendSuccess(() -> Component.translatable("Set all materials for " + player.getDisplayName().getString() + " to " + amount), true);

			player.sendSystemMessage(Component.translatable("You have been set all the materials to " + amount));
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
		}
		return 1;
	}

}
