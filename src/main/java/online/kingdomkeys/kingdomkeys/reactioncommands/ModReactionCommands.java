package online.kingdomkeys.kingdomkeys.reactioncommands;

import com.google.common.base.Suppliers;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.KKSupplier;
import online.kingdomkeys.kingdomkeys.lib.Strings;

import java.util.List;
import java.util.function.Supplier;

public class ModReactionCommands {

	public static DeferredRegister<ReactionCommand> REACTION_COMMANDS = DeferredRegister.create(KingdomKeys.rl("reactioncommands"), KingdomKeys.MODID);
	public static Registry<ReactionCommand> registry = REACTION_COMMANDS.makeRegistry(builder -> builder.sync(true));

	public static final Supplier<List<ReactionCommand>> CONSTANT_CHECK_COMMANDS = Suppliers.memoize(() -> registry.stream().filter(ReactionCommand::needsConstantCheck).toList());

	public static final KKSupplier<ReactionCommand>
		AUTO_VALOR = register(Strings.autoValorRC, () -> new ReactionAutoForm(KingdomKeys.rl(Strings.autoValorRC), KingdomKeys.rl(Strings.autoValor), KingdomKeys.rl(Strings.Form_Valor))),
		AUTO_WISDOM = register(Strings.autoWisdomRC, () -> new ReactionAutoForm(KingdomKeys.rl(Strings.autoWisdomRC), KingdomKeys.rl(Strings.autoWisdom), KingdomKeys.rl(Strings.Form_Wisdom))),
		AUTO_LIMIT = register(Strings.autoLimitRC, () -> new ReactionAutoForm(KingdomKeys.rl(Strings.autoLimitRC), KingdomKeys.rl(Strings.autoLimit), KingdomKeys.rl(Strings.Form_Limit))),
		AUTO_MASTER = register(Strings.autoMasterRC, () -> new ReactionAutoForm(KingdomKeys.rl(Strings.autoMasterRC), KingdomKeys.rl(Strings.autoMaster), KingdomKeys.rl(Strings.Form_Master))),
		AUTO_FINAL = register(Strings.autoFinalRC, () -> new ReactionAutoForm(KingdomKeys.rl(Strings.autoFinalRC), KingdomKeys.rl(Strings.autoFinal), KingdomKeys.rl(Strings.Form_Final))),
		FIRE = register(KingdomKeys.rl(Strings.Magic_Fire).getPath(), () -> new ReactionMagic(KingdomKeys.rl(Strings.Magic_Fire))),
		BLIZZARD = register(KingdomKeys.rl(Strings.Magic_Blizzard).getPath(), () -> new ReactionMagic(KingdomKeys.rl(Strings.Magic_Blizzard))),
		WATER = register(KingdomKeys.rl(Strings.Magic_Water).getPath(), () -> new ReactionMagic(KingdomKeys.rl(Strings.Magic_Water))),
		THUNDER = register(KingdomKeys.rl(Strings.Magic_Thunder).getPath(), () -> new ReactionMagic(KingdomKeys.rl(Strings.Magic_Thunder))),
		CURE = register(KingdomKeys.rl(Strings.Magic_Cure).getPath(), () -> new ReactionMagic(KingdomKeys.rl(Strings.Magic_Cure))),
		AERO =  register(KingdomKeys.rl(Strings.Magic_Aero).getPath(), () -> new ReactionMagic(KingdomKeys.rl(Strings.Magic_Aero))),
		MAGNET = register(KingdomKeys.rl(Strings.Magic_Magnet).getPath(), () -> new ReactionMagic(KingdomKeys.rl(Strings.Magic_Magnet))),
		REFLECT = register(KingdomKeys.rl(Strings.Magic_Reflect).getPath(), () -> new ReactionMagic(KingdomKeys.rl(Strings.Magic_Reflect))),
		GRAVITY = register(KingdomKeys.rl(Strings.Magic_Gravity).getPath(), () -> new ReactionMagic(KingdomKeys.rl(Strings.Magic_Gravity))),
		STOP = register(KingdomKeys.rl(Strings.Magic_Stop).getPath(), () -> new ReactionMagic(KingdomKeys.rl(Strings.Magic_Stop))),

		SAVE = register("save_rc", () -> new ReactionSave(KingdomKeys.rl("save_rc")));

	private static KKSupplier<ReactionCommand> register(String name, Supplier<ReactionCommand> reactionCommandSupplier) {
		return new KKSupplier<>(KingdomKeys.rl(REACTION_COMMANDS.getNamespace(), name), REACTION_COMMANDS.register(name, reactionCommandSupplier));
	}
}