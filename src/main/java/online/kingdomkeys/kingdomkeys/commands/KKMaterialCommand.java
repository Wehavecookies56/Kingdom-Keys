package online.kingdomkeys.kingdomkeys.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.synthesis.material.ModMaterials;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class KKMaterialCommand extends BaseCommand{ //kkmaterial <give/take> <material/all> <amount> [player]
													//kkmaterial <take> 	 <all> 					 [player]
	
	private static final SuggestionProvider<CommandSource> SUGGEST_MATERIALS = (p_198296_0_, p_198296_1_) -> {
	   List<String> list = new ArrayList<>();
	   for (ResourceLocation actual : ModMaterials.registry.getKeys()) {
		   list.add(actual.getNamespace() + ":" + actual.getPath());
	   }
	   return ISuggestionProvider.suggest(list.stream().map(StringArgumentType::escapeIfRequired), p_198296_1_);
	};

	public static void register(CommandDispatcher<CommandSource> dispatcher) {		
		LiteralArgumentBuilder<CommandSource> builder = Commands.literal("kkmaterial").requires(source -> source.hasPermissionLevel(3));
				
		builder.then(Commands.literal("give")
				
			.then(Commands.argument("material", StringArgumentType.string()).suggests(SUGGEST_MATERIALS)
				.then(Commands.argument("amount", IntegerArgumentType.integer(1))		
					.then(Commands.argument("targets", EntityArgument.players())
						.executes(KKMaterialCommand::addMaterial)
					)
					.executes(KKMaterialCommand::addMaterial)
				)
			)
			.then(Commands.literal("all")
				.then(Commands.argument("amount", IntegerArgumentType.integer(1))		
					.then(Commands.argument("targets", EntityArgument.players())
						.executes(KKMaterialCommand::addAllMaterials)
					)
				.executes(KKMaterialCommand::addAllMaterials)
				)
			)
		);
		
		builder.then(Commands.literal("take")
				
			.then(Commands.argument("material", StringArgumentType.string()).suggests(SUGGEST_MATERIALS)
				.then(Commands.argument("amount", IntegerArgumentType.integer(1))		
					.then(Commands.argument("targets", EntityArgument.players())
						.executes(KKMaterialCommand::takeMaterial)
					)
					.executes(KKMaterialCommand::takeMaterial)
				)
			)
			.then(Commands.literal("all")
				.then(Commands.argument("targets", EntityArgument.players())
					.executes(KKMaterialCommand::takeAllMaterials)
				)
				.executes(KKMaterialCommand::takeAllMaterials)
				)
			
		);
		
		builder.then(Commands.literal("set")
				
				.then(Commands.argument("material", StringArgumentType.string()).suggests(SUGGEST_MATERIALS)
					.then(Commands.argument("amount", IntegerArgumentType.integer(1))		
						.then(Commands.argument("targets", EntityArgument.players())
							.executes(KKMaterialCommand::setMaterial)
						)
						.executes(KKMaterialCommand::setMaterial)
					)
				)
				.then(Commands.literal("all")
					.then(Commands.argument("amount", IntegerArgumentType.integer(1))		
						.then(Commands.argument("targets", EntityArgument.players())
							.executes(KKMaterialCommand::setAllMaterials)
						)
					.executes(KKMaterialCommand::setAllMaterials)
					)
				)
			);
		
		dispatcher.register(builder);
		KingdomKeys.LOGGER.warn("Registered command "+builder.getLiteral());
	}
	
	private static int addMaterial(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = getPlayers(context, 4);
		String materialName = StringArgumentType.getString(context, "material");
		int amount = IntegerArgumentType.getInteger(context, "amount");
		Material material = ModMaterials.registry.getValue(new ResourceLocation(materialName));
		
		for (ServerPlayerEntity player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.addMaterial(material, amount);
			
			if(player != context.getSource().asPlayer()) {
				context.getSource().sendFeedback(new TranslationTextComponent("Given x"+amount+" '"+ Utils.translateToLocal(material.getMaterialName())+"' to "+player.getDisplayName().getFormattedText()), true);
			}
			player.sendMessage(new TranslationTextComponent("You have been given x"+amount+" '"+Utils.translateToLocal(material.getMaterialName())+"'"));
		}
		return 1;
	}
	
	private static int takeMaterial(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = getPlayers(context, 4);
		String materialName = StringArgumentType.getString(context, "material");
		int amount = IntegerArgumentType.getInteger(context, "amount");
		Material material = ModMaterials.registry.getValue(new ResourceLocation(materialName));

		for (ServerPlayerEntity player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.removeMaterial(material, amount);
			
			if(player != context.getSource().asPlayer()) {
				context.getSource().sendFeedback(new TranslationTextComponent("Removed material '"+Utils.translateToLocal(material.getMaterialName())+"' from "+player.getDisplayName().getFormattedText()), true);
			}
			player.sendMessage(new TranslationTextComponent("x"+amount+" '"+Utils.translateToLocal(material.getMaterialName())+"' have been taken away from you"));
		}
		return 1;
	}
	
	private static int addAllMaterials(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = getPlayers(context, 4);
		int amount = IntegerArgumentType.getInteger(context, "amount");

		for (ServerPlayerEntity player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			for(Material material : ModMaterials.registry.getValues()) {
				playerData.addMaterial(material, amount);
			}
			if(player != context.getSource().asPlayer()) {
				context.getSource().sendFeedback(new TranslationTextComponent("Given all materials to "+player.getDisplayName().getFormattedText()), true);
			}
			player.sendMessage(new TranslationTextComponent("You have been given all the materials"));
		}
		return 1;
	}
	
	private static int takeAllMaterials(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = getPlayers(context, 3);
		
		for (ServerPlayerEntity player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.clearMaterials();
			
			if(player != context.getSource().asPlayer()) {
				context.getSource().sendFeedback(new TranslationTextComponent("Taken all materials from "+player.getDisplayName().getFormattedText()), true);
			}
			player.sendMessage(new TranslationTextComponent("Your materials have been taken away"));
		}
		return 1;
	}
	
	private static int setMaterial(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = getPlayers(context, 4);
		String materialName = StringArgumentType.getString(context, "material");
		int amount = IntegerArgumentType.getInteger(context, "amount");
		Material material = ModMaterials.registry.getValue(new ResourceLocation(materialName));
		
		for (ServerPlayerEntity player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.setMaterial(material, amount);
			
			if(player != context.getSource().asPlayer()) {
				context.getSource().sendFeedback(new TranslationTextComponent("Set x"+amount+" '"+Utils.translateToLocal(material.getMaterialName())+"' to "+player.getDisplayName().getFormattedText()), true);
			}
			player.sendMessage(new TranslationTextComponent("Your '"+Utils.translateToLocal(material.getMaterialName())+"' have been set to x"+amount));
		}
		return 1;
	}
	
	private static int setAllMaterials(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = getPlayers(context, 4);
		int amount = IntegerArgumentType.getInteger(context, "amount");

		for (ServerPlayerEntity player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			for(Material material : ModMaterials.registry.getValues()) {
				playerData.setMaterial(material, amount);
			}
			if(player != context.getSource().asPlayer()) {
				context.getSource().sendFeedback(new TranslationTextComponent("Set all materials for "+player.getDisplayName().getFormattedText()+" to "+amount), true);
			}
			player.sendMessage(new TranslationTextComponent("You have been set all the materials to "+amount));
		}
		return 1;
	}

}
