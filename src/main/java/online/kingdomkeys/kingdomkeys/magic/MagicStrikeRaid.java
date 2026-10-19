package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.organization.KKThrowableEntity;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.KKSupplier;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber
public class MagicStrikeRaid extends Magic {

	public static final int RAIDS = 3;
	public static final float JUDGMENT_MULT = 1.5F;

	public static final int WINDOW = 20, COOLDOWN = 15;

	// Throws done so far in each player's chain, and the game time the next follow up is due (-1 once offered).
	// Only the server needs it, the commands on offer say the rest
	private record Chain(int done, long offerAt) {}
	private static final Map<UUID, Chain> CHAIN = new HashMap<>();

	public MagicStrikeRaid(ResourceLocation registryName, int tier, ResourceLocation gmAbility) {
		super(registryName, false, gmAbility);
		setTier(tier);
	}

	@Override
	public boolean canCast(Player player) {
		return player.getMainHandItem().getItem() instanceof KeybladeItem;
	}

	@Override
	public boolean isProjectile() {
		return true;
	}

	@Override
	public void magicUse(LivingEntity player, LivingEntity caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
		if (caster instanceof Player thrower && throwKeyblade(thrower, getRealDamageMult(caster) * fullMPBlastMult)) {
			CHAIN.put(thrower.getUUID(), new Chain(1, thrower.level().getGameTime() + COOLDOWN));
		}
	}

	/** Raid and Judgment: throws again and offers the next one, Judgment ending the chain */
	public static void followUp(Player player, boolean judgment) {
		Chain chain = CHAIN.get(player.getUUID());
		// Judgment only once the three Raids are done, and nothing at all outside a chain
		if (chain == null || judgment != (chain.done() > RAIDS)) {
			return;
		}
		int done = chain.done();

		float dmgMult = ModMagic.STRIKE_RAID.get().getRealDamageMult(player) * (judgment ? JUDGMENT_MULT : 1F);
		if (!throwKeyblade(player, dmgMult) || judgment) {
			CHAIN.remove(player.getUUID());
			return;
		}

		CHAIN.put(player.getUUID(), new Chain(done + 1, player.level().getGameTime() + COOLDOWN));
	}

	// The next follow up shows up once the cooldown since the last throw is over, so they cannot be chained at once
	@SubscribeEvent
	public static void tick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		if (player.level().isClientSide) {
			return;
		}
		Chain chain = CHAIN.get(player.getUUID());
		if (chain == null || chain.offerAt() < 0 || player.level().getGameTime() < chain.offerAt()) {
			return;
		}
		CHAIN.put(player.getUUID(), new Chain(chain.done(), -1));
		offer(player, chain.done() > RAIDS ? ModReactionCommands.JUDGMENT : ModReactionCommands.RAID);
	}

	private static void offer(Player player, KKSupplier<ReactionCommand> command) {
		PlayerData playerData = PlayerData.get(player);
		playerData.addReactionCommand(command.get().getRegistryName(), player, WINDOW);
		if (player instanceof ServerPlayer serverPlayer) {
			PacketHandler.sendTo(new SCSyncPlayerData(serverPlayer), serverPlayer);
		}
	}

	/** Throws the keyblade in their main hand, which stays there. Returns false if they hold none */
	public static boolean throwKeyblade(Player player, float dmgMult) {
		ItemStack stack = player.getMainHandItem();
		if (!(stack.getItem() instanceof KeybladeItem)) {
			return false;
		}

		Level level = player.level();
		level.playSound(null, player.blockPosition(), ModSounds.strike_raid.get(), SoundSource.PLAYERS, 1, 1);

		KKThrowableEntity entity = new KKThrowableEntity(level);
		entity.setRotationPoint(BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath().equals(Strings.retribution) ? 0 : 1);
		entity.setData(DamageCalculation.getKBStrengthDamage(player, stack) * 0.7F * dmgMult, player.getUUID(), player.getInventory().selected, stack);
		entity.setPos(player.position().x, player.getEyePosition().y, player.position().z);
		entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3F, 0F);
		level.addFreshEntity(entity);
		player.swing(InteractionHand.MAIN_HAND, true);
		return true;
	}

	@Override
	public void playMagicCastSound(LivingEntity player, LivingEntity caster) {
		// The throw itself makes the sound
	}
}
