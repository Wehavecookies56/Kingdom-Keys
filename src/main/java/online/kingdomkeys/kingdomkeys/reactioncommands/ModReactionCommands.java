package online.kingdomkeys.kingdomkeys.reactioncommands;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class ModReactionCommands {

	public static DeferredRegister<ReactionCommand> REACTION_COMMANDS = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "reactioncommands"), KingdomKeys.MODID);

	public static final ResourceKey<Registry<ReactionCommand>> REACTION_COMMANDS_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "roomtypes"));
	public static Registry<ReactionCommand> registry = new RegistryBuilder<>(REACTION_COMMANDS_KEY).sync(true).defaultKey(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "empty")).create();

	public static final Supplier<ReactionCommand>
		AUTO_VALOR = REACTION_COMMANDS.register(Strings.autoValorRC, () -> new ReactionAutoForm(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, Strings.autoValorRC), Strings.autoValor, Strings.Form_Valor)),
		AUTO_WISDOM = REACTION_COMMANDS.register(Strings.autoWisdomRC, () -> new ReactionAutoForm(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, Strings.autoWisdomRC), Strings.autoWisdom, Strings.Form_Wisdom)),
		AUTO_LIMIT = REACTION_COMMANDS.register(Strings.autoLimitRC, () -> new ReactionAutoForm(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, Strings.autoLimitRC), Strings.autoLimit, Strings.Form_Limit)),
		AUTO_MASTER = REACTION_COMMANDS.register(Strings.autoMasterRC, () -> new ReactionAutoForm(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, Strings.autoMasterRC), Strings.autoMaster, Strings.Form_Master)),
		AUTO_FINAL = REACTION_COMMANDS.register(Strings.autoFinalRC, () -> new ReactionAutoForm(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, Strings.autoFinalRC), Strings.autoFinal, Strings.Form_Final)),
		FIRE = REACTION_COMMANDS.register(ResourceLocation.parse(Strings.Magic_Fire).getPath(), () -> new ReactionMagic(ResourceLocation.parse(Strings.Magic_Fire))),
		BLIZZARD = REACTION_COMMANDS.register(ResourceLocation.parse(Strings.Magic_Blizzard).getPath(), () -> new ReactionMagic(ResourceLocation.parse(Strings.Magic_Blizzard))),
		WATER = REACTION_COMMANDS.register(ResourceLocation.parse(Strings.Magic_Water).getPath(), () -> new ReactionMagic(ResourceLocation.parse(Strings.Magic_Water))),
		THUNDER = REACTION_COMMANDS.register(ResourceLocation.parse(Strings.Magic_Thunder).getPath(), () -> new ReactionMagic(ResourceLocation.parse(Strings.Magic_Thunder))),
		CURE = REACTION_COMMANDS.register(ResourceLocation.parse(Strings.Magic_Cure).getPath(), () -> new ReactionMagic(ResourceLocation.parse(Strings.Magic_Cure))),
		AERO =  REACTION_COMMANDS.register(ResourceLocation.parse(Strings.Magic_Aero).getPath(), () -> new ReactionMagic(ResourceLocation.parse(Strings.Magic_Aero))),
		MAGNET = REACTION_COMMANDS.register(ResourceLocation.parse(Strings.Magic_Magnet).getPath(), () -> new ReactionMagic(ResourceLocation.parse(Strings.Magic_Magnet))),
		REFLECT = REACTION_COMMANDS.register(ResourceLocation.parse(Strings.Magic_Reflect).getPath(), () -> new ReactionMagic(ResourceLocation.parse(Strings.Magic_Reflect))),
		GRAVITY = REACTION_COMMANDS.register(ResourceLocation.parse(Strings.Magic_Gravity).getPath(), () -> new ReactionMagic(ResourceLocation.parse(Strings.Magic_Gravity))),
		STOP = REACTION_COMMANDS.register(ResourceLocation.parse(Strings.Magic_Stop).getPath(), () -> new ReactionMagic(ResourceLocation.parse(Strings.Magic_Stop))),

		SAVE = REACTION_COMMANDS.register("save_rc", () -> new ReactionSave(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "save_rc")));
	;
}