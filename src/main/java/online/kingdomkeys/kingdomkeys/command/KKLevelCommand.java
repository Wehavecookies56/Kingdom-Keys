package online.kingdomkeys.kingdomkeys.command;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public class KKLevelCommand extends BaseCommand{ //kk_level <give/take/set> <amount> [player]
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> builder = Commands.literal("kk_level").requires(source -> source.hasPermissionLevel(2));
		
		builder.then(Commands.literal("set")
			.then(Commands.argument("level", IntegerArgumentType.integer(1,100))
				.then(Commands.argument("targets", EntityArgument.players())
					.executes(KKLevelCommand::setValue)
				)
				.executes(KKLevelCommand::setValue)
			)
		);
		
		/*builder.then(Commands.literal("give")
			.then(Commands.argument("value", IntegerArgumentType.integer(1,Integer.MAX_VALUE))
				.then(Commands.argument("targets", EntityArgument.players())
					.executes(KKLevelCommand::addValue)
				)
				.executes(KKLevelCommand::addValue)
			)
		);
		
		builder.then(Commands.literal("take")
			.then(Commands.argument("value", IntegerArgumentType.integer(1,Integer.MAX_VALUE))
				.then(Commands.argument("targets", EntityArgument.players())
					.executes(KKLevelCommand::removeValue)
				)
				.executes(KKLevelCommand::removeValue)
			)
		);*/
		
		dispatcher.register(builder);
		KingdomKeys.LOGGER.warn("Registered command "+builder.getLiteral());
	}

	private static int setValue(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = getPlayers(context, 3);
		int level = IntegerArgumentType.getInteger(context, "level");
		
		for (ServerPlayerEntity player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            
			playerData.setLevel(1);
			playerData.setExperience(0);
			playerData.setMaxHP(20);
            player.setHealth(playerData.getMaxHP());
    		player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(playerData.getMaxHP());
            playerData.setMaxMP(0);
            playerData.setMP(playerData.getMaxMP());
            playerData.setMaxAP(10);
            
            playerData.setStrength(1);
            playerData.setMagic(1);
            playerData.setDefense(1);
			SoAState.applyStatsForChoices(playerData);

			playerData.setEquippedShotlock("");
			playerData.getShotlockList().clear();
			
			playerData.clearAbilities();
            playerData.addAbility(Strings.zeroExp, false);
            
			while (playerData.getLevel() < level) {
				playerData.addExperience(player, playerData.getExpNeeded(level - 1, playerData.getExperience()), false, false);
			}
			player.world.playSound((PlayerEntity) null, player.getPosition(), ModSounds.levelup.get(), SoundCategory.MASTER, 1f, 1.0f);

			
			LinkedHashMap<String, int[]> driveForms = playerData.getDriveFormMap();
			Iterator<Entry<String, int[]>> it = driveForms.entrySet().iterator();
			while(it.hasNext()) {
				Entry<String, int[]> entry = it.next();
				int dfLevel = entry.getValue()[0];
				DriveForm form = ModDriveForms.registry.getValue(new ResourceLocation(entry.getKey()));
				if(!form.getRegistryName().equals(DriveForm.NONE) && !form.getRegistryName().equals(DriveForm.SYNCH_BLADE)) {
					for(int i=1;i<=dfLevel;i++) {
						String baseAbility = form.getBaseAbilityForLevel(i);
				     	if(baseAbility != null && !baseAbility.equals("")) {
				     		playerData.addAbility(baseAbility, false);
				     	}
					}
				}
			}
			
			player.heal(playerData.getMaxHP());
			playerData.setMP(playerData.getMaxMP());
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
			context.getSource().sendFeedback(new TranslationTextComponent("Set "+player.getDisplayName().getString()+" level to "+level), true);
			
			player.sendMessage(new TranslationTextComponent("Your level is now "+level), Util.DUMMY_UUID);
		}
		return 1;
	}
	

	private static int addValue(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = getPlayers(context, 3);
		int value = IntegerArgumentType.getInteger(context, "value");
		
		for (ServerPlayerEntity player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.setMunny(playerData.getMunny() + value);
			
				context.getSource().sendFeedback(new TranslationTextComponent("Added "+value+" munny to "+player.getDisplayName().getString()), true);
			
			player.sendMessage(new TranslationTextComponent("Your munny has been increased by "+value),Util.DUMMY_UUID);	
		}
		return 1;
	}
	
	
	private static int removeValue(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = getPlayers(context, 3);
		int value = IntegerArgumentType.getInteger(context, "value");
		
		for (ServerPlayerEntity player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.setMunny(playerData.getMunny() - value);
			
				context.getSource().sendFeedback(new TranslationTextComponent("Taken "+value+" munny from "+player.getDisplayName().getString()), true);
			
			player.sendMessage(new TranslationTextComponent("Your munny has been decreased by "+value),Util.DUMMY_UUID);
		}
		return 1;
	}
	
}
