package online.kingdomkeys.kingdomkeys.command;

//Add back when dim works
/*
//TODO make this work for other dims, when we add them
public class DimensionCommand extends BaseCommand {
	
	private static final SuggestionProvider<CommandSource> SUGGEST_DIMENSIONS = (p_198296_0_, p_198296_1_) -> {
		List<String> list = new ArrayList<>();
		list.add("overworld");
		for (RegistryObject<ModDimension> location : ModDimensions.DIMENSIONS.getEntries()) {
			list.add(location.get().getRegistryName().toString());
		}
		return ISuggestionProvider.suggest(list.stream().map(StringArgumentType::escapeIfRequired), p_198296_1_);
	};


    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("kk_dimension").requires(source -> source.hasPermissionLevel(3));

        builder.then(Commands.argument("dim", StringArgumentType.string()).suggests(SUGGEST_DIMENSIONS)
	        .then(Commands.argument("targets", EntityArgument.players())
	        		.executes(DimensionCommand::changeDim)
	        	)
			.executes(DimensionCommand::changeDim)
        );

        dispatcher.register(builder);
        KingdomKeys.LOGGER.warn("Registered command "+builder.getLiteral());
    }

    private static int changeDim(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = getPlayers(context, 2);
		String dim = StringArgumentType.getString(context, "dim");
		DimensionType dimension = DimensionType.byName(new ResourceLocation(dim));
		
		if(dimension == null) {
			context.getSource().sendFeedback(new TranslationTextComponent("Invalid dimension "+dim), true);
			return 1;
		}
		for (ServerPlayerEntity player : players) {
        	BlockPos coords = getWorldCoords(player,dimension);
            player.changeDimension(dimension, new BaseTeleporter(coords.getX(), coords.getY(), coords.getZ()));
			
			if(player != context.getSource().asPlayer()) {
				context.getSource().sendFeedback(new TranslationTextComponent("Teleported" +player.getDisplayName().getFormattedText()+" to dimension "+dimension.getRegistryName().toString()), true);
			}
			player.sendMessage(new TranslationTextComponent("You have been teleported to "+dimension.getRegistryName().toString()));
        }
        return 1;
    }
    
    private static BlockPos getWorldCoords(PlayerEntity player, DimensionType dimension) {
    	if(dimension == ModDimensions.DIVE_TO_THE_HEART_TYPE) {
    		return new BlockPos(0,26,0);
    	}
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
    	return new BlockPos(playerData.getReturnLocation());
    }
}
*/