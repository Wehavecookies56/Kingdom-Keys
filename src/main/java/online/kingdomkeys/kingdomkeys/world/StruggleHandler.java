package online.kingdomkeys.kingdomkeys.world;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Struggle;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCCloseScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowMessagesPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncWorldData;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Drives every Struggle match state machine (DUEL / TOURNAMENT / FFA): waits for the right people to
 * be ready, runs a short countdown, teleports them to their spot(s), hands out the weapon matching
 * their Station of Awakening choice while locking away the rest of their inventory, ticks the round
 * timer/score, and restores everything once someone wins or time runs out.
 *
 * All ephemeral state here (countdowns, round timers, saved inventories, who's actively fighting right
 * now) is server-side only and NOT persisted or synced - if the server restarts mid-match, the match is
 * simply lost. This is an acceptable limitation for now.
 *
 * All player-facing messages use the same big centered Title system as the rest of the mod (not the
 * hotbar action bar), via {@link SCShowMessagesPacket}. Title/subtitle strings are translation keys
 * (see LanguageENUS/LanguageESES "kingdomkeys.struggle.*"), except the tournament champion's subtitle,
 * which is their raw username (not a translation key - it's just displayed as-is since Minecraft shows
 * an unmatched key literally when there's no such translation).
 */
public class StruggleHandler {

	private static final int COUNTDOWN_TICKS = 60; // 3 seconds
	private static final int ROUND_TICKS = 1200; // 60 seconds
	private static final int WIN_SCORE = 200;
	private static final Random RANDOM = new Random();

	private static final Map<String, Integer> countdowns = new HashMap<>();
	private static final Map<String, Integer> roundTicksLeft = new HashMap<>();
	private static final Map<UUID, InventorySnapshot> savedInventories = new HashMap<>();
	/** Who is (about to be) actually fighting right now, keyed by struggle name. */
	private static final Map<String, List<UUID>> activeCombatants = new HashMap<>();

	@SubscribeEvent
	public void onServerTick(ServerTickEvent.Post event) {
		MinecraftServer server = event.getServer();
		WorldData worldData = WorldData.get(server);
		ServerLevel level = server.overworld();

		for (Struggle struggle : new ArrayList<>(worldData.getStruggles())) {
			tick(server, level, worldData, struggle);
		}
	}

	private void tick(MinecraftServer server, ServerLevel level, WorldData worldData, Struggle struggle) {
		String name = struggle.getName();

		if (struggle.isInProgress()) {
			tickRound(server, worldData, struggle, name);
			return;
		}

		if (countdowns.containsKey(name)) {
			tickCountdown(server, level, worldData, struggle, name);
			return;
		}

		switch (struggle.getMode()) {
			case DUEL -> tryStartDuel(server, worldData, struggle, name);
			case TOURNAMENT -> tryStartTournament(server, worldData, struggle, name);
			case FFA -> tryStartFfa(server, worldData, struggle, name);
		}
	}

	private void tryStartDuel(MinecraftServer server, WorldData worldData, Struggle struggle, String name) {
		if (!struggle.isConfigured() || struggle.getParticipants().size() < 2) return;

		Struggle.Participant p1 = struggle.getParticipants().get(0);
		Struggle.Participant p2 = struggle.getParticipants().get(1);
		if (p1.isReady() && p2.isReady()) {
			activeCombatants.put(name, List.of(p1.getUUID(), p2.getUUID()));
			countdowns.put(name, COUNTDOWN_TICKS);
			sendTitle(server, activeCombatants.get(name), "kingdomkeys.struggle.starting", "");
		}
	}

	private void tryStartTournament(MinecraftServer server, WorldData worldData, Struggle struggle, String name) {
		if (!struggle.isConfigured()) return;
		List<UUID> queue = struggle.getTournamentQueue();

		if (queue.isEmpty()) {
			// Tournament hasn't started yet - kick it off once everyone currently registered is ready.
			if (struggle.getParticipants().size() < 2) return;
			if (struggle.getParticipants().stream().noneMatch(p -> !p.isReady())) {
				List<UUID> order = struggle.getParticipants().stream().map(Struggle.Participant::getUUID).collect(Collectors.toList());
				Collections.shuffle(order, RANDOM);
				queue.addAll(order);
				worldData.setDirty();
			} else {
				return;
			}
		}

		// Odd number of fighters left this round? One random one gets a bye (skips straight to next round).
		if (queue.size() % 2 != 0 && queue.size() > 1) {
			UUID byePlayer = queue.remove(0);
			queue.add(byePlayer);
			sendTitle(server, List.of(byePlayer), "kingdomkeys.struggle.tournament.bye", "");
			worldData.setDirty();
		}

		if (queue.size() < 2) return; // shouldn't normally happen (tournament ends before this), but just in case

		List<UUID> pair = List.of(queue.get(0), queue.get(1));
		activeCombatants.put(name, pair);
		countdowns.put(name, COUNTDOWN_TICKS);
		sendTitle(server, pair, "kingdomkeys.struggle.tournament.next_match", "");
	}

	private void tryStartFfa(MinecraftServer server, WorldData worldData, Struggle struggle, String name) {
		if (!struggle.isConfigured()) return;

		List<UUID> ready = struggle.getParticipants().stream()
				.filter(Struggle.Participant::isReady)
				.map(Struggle.Participant::getUUID)
				.collect(Collectors.toList());

		if (ready.size() >= 2) {
			activeCombatants.put(name, ready);
			countdowns.put(name, COUNTDOWN_TICKS);
			sendTitle(server, ready, "kingdomkeys.struggle.ffa.starting", "");
		}
	}

	private void tickCountdown(MinecraftServer server, ServerLevel level, WorldData worldData, Struggle struggle, String name) {
		int ticks = countdowns.get(name) - 1;

		if (ticks <= 0) {
			countdowns.remove(name);
			startMatch(server, level, worldData, struggle);
			return;
		}

		countdowns.put(name, ticks);
		if (ticks % 20 == 0) {
			// Countdown numbers are shown as-is (no "kingdomkeys.struggle...." key/translation needed -
			// a number looks the same in every language, and an unmatched key just renders as itself).
			sendTitle(server, activeCombatants.getOrDefault(name, List.of()), String.valueOf(ticks / 20), "");
		}
	}

	private void startMatch(MinecraftServer server, ServerLevel level, WorldData worldData, Struggle struggle) {
		List<UUID> combatantIds = activeCombatants.getOrDefault(struggle.getName(), List.of());
		List<BlockPos> spawnPositions = computeSpawnPositions(struggle, combatantIds.size());

		for (int i = 0; i < combatantIds.size(); i++) {
			Struggle.Participant participant = struggle.getParticipant(combatantIds.get(i));
			if (participant == null) continue;
			ServerPlayer player = server.getPlayerList().getPlayer(participant.getUUID());
			if (player == null) continue;

			participant.setScore(100);
			participant.setReady(false);

			savedInventories.put(player.getUUID(), InventorySnapshot.capture(player));
			Inventory inventory = player.getInventory();
			inventory.clearContent();

			Item weapon = Struggle.weaponFor(PlayerData.get(player).getChosen());
			inventory.items.set(0, new ItemStack(weapon));
			inventory.selected = 0;
			player.inventoryMenu.broadcastChanges();

			BlockPos spawn = spawnPositions.get(i);
			player.teleportTo(spawn.getX() + 0.5, spawn.getY(), spawn.getZ() + 0.5);
			PacketHandler.sendTo(new SCCloseScreen(), player); // close any open UI (menus, inventory, etc)
		}

		sendTitle(server, combatantIds, "kingdomkeys.struggle.go", "");

		struggle.setInProgress(true);
		struggle.setRoundSecondsLeft(ROUND_TICKS / 20);
		struggle.getActiveCombatantIds().clear();
		struggle.getActiveCombatantIds().addAll(combatantIds);
		roundTicksLeft.put(struggle.getName(), ROUND_TICKS);
		worldData.setDirty();
		PacketHandler.sendToAll(new SCSyncWorldData(server));
	}

	/**
	 * 2 combatants (DUEL/TOURNAMENT) always spawn exactly at c1/c2. More than that (FFA) get scattered
	 * at random points across the c1/c2 bounding box instead, since the arena only has 2 defined corners.
	 */
	private List<BlockPos> computeSpawnPositions(Struggle struggle, int count) {
		if (count == 2) {
			return List.of(struggle.getC1(), struggle.getC2());
		}

		List<BlockPos> positions = new ArrayList<>();
		BlockPos c1 = struggle.getC1();
		BlockPos c2 = struggle.getC2();
		int minX = Math.min(c1.getX(), c2.getX());
		int maxX = Math.max(c1.getX(), c2.getX());
		int minZ = Math.min(c1.getZ(), c2.getZ());
		int maxZ = Math.max(c1.getZ(), c2.getZ());
		int y = Math.min(c1.getY(), c2.getY());

		for (int i = 0; i < count; i++) {
			int x = minX + RANDOM.nextInt(Math.max(1, maxX - minX + 1));
			int z = minZ + RANDOM.nextInt(Math.max(1, maxZ - minZ + 1));
			positions.add(new BlockPos(x, y, z));
		}
		return positions;
	}

	private void tickRound(MinecraftServer server, WorldData worldData, Struggle struggle, String name) {
		List<UUID> combatantIds = activeCombatants.getOrDefault(name, List.of());
		List<Struggle.Participant> combatants = combatantIds.stream()
				.map(struggle::getParticipant)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());

		Struggle.Participant winner = combatants.stream().filter(p -> p.getScore() >= WIN_SCORE).findFirst().orElse(null);

		int ticksLeft = roundTicksLeft.getOrDefault(name, 0) - 1;
		if (winner == null && ticksLeft <= 0) {
			winner = combatants.stream().max(Comparator.comparingInt(Struggle.Participant::getScore)).orElse(null);
		}

		if (winner != null) {
			endMatch(server, worldData, struggle, winner, combatants);
		} else {
			roundTicksLeft.put(name, ticksLeft);
			if (ticksLeft % 20 == 0) {
				struggle.setRoundSecondsLeft(ticksLeft / 20);
				worldData.setDirty();
				PacketHandler.sendToAll(new SCSyncWorldData(server));
			}
		}
	}

	private void endMatch(MinecraftServer server, WorldData worldData, Struggle struggle, Struggle.Participant winner, List<Struggle.Participant> combatants) {
		for (Struggle.Participant participant : combatants) {
			ServerPlayer player = server.getPlayerList().getPlayer(participant.getUUID());
			if (player != null) {
				InventorySnapshot snapshot = savedInventories.remove(player.getUUID());
				player.getInventory().clearContent();
				if (snapshot != null) {
					snapshot.restore(player);
				}
				boolean won = participant.getUUID().equals(winner.getUUID());
				sendTitle(server, List.of(participant.getUUID()), won ? "kingdomkeys.struggle.win" : "kingdomkeys.struggle.lose", "");
			}
			participant.setReady(false);
			participant.setScore(100);
		}

		activeCombatants.remove(struggle.getName());
		struggle.getActiveCombatantIds().clear();
		roundTicksLeft.remove(struggle.getName());
		struggle.setRoundSecondsLeft(-1);
		struggle.setInProgress(false);

		if (struggle.getMode() == Struggle.Mode.TOURNAMENT) {
			handleTournamentAdvance(server, worldData, struggle, winner, combatants);
		}

		worldData.setDirty();
		PacketHandler.sendToAll(new SCSyncWorldData(server));
	}

	private void handleTournamentAdvance(MinecraftServer server, WorldData worldData, Struggle struggle, Struggle.Participant winner, List<Struggle.Participant> combatants) {
		List<UUID> queue = struggle.getTournamentQueue();
		for (Struggle.Participant participant : combatants) {
			queue.remove(participant.getUUID()); // losers drop out entirely, winner re-added below
		}
		queue.add(winner.getUUID());

		if (queue.size() <= 1) {
			Struggle.Participant champion = struggle.getParticipant(winner.getUUID());
			String championName = champion != null ? champion.getUsername() : "?";
			List<UUID> everyone = struggle.getParticipants().stream().map(Struggle.Participant::getUUID).collect(Collectors.toList());
			sendTitle(server, everyone, "kingdomkeys.struggle.tournament.champion", championName);

			// Free up the board for a new match, but remember the arena so it doesn't need reconfiguring.
			worldData.saveStruggleCorners(struggle.getPos(), struggle.getC1(), struggle.getC2());
			worldData.removeStruggle(struggle);
		}
	}

	/**
	 * Shows a big centered title (same system as the rest of the mod, e.g. Castle Oblivion encounter
	 * messages) instead of a hotbar/action bar message. titleKey/subtitleKey are translation keys - pass
	 * "" for no subtitle. The tournament champion announcement is the one exception where the "subtitle"
	 * is a raw player name rather than a translation key.
	 */
	private static void sendTitle(MinecraftServer server, List<UUID> targets, String titleKey, String subtitleKey) {
		List<Utils.Title> titles = List.of(new Utils.Title(titleKey, subtitleKey));
		for (UUID id : targets) {
			ServerPlayer player = server.getPlayerList().getPlayer(id);
			if (player != null) {
				PacketHandler.sendTo(new SCShowMessagesPacket(titles), player);
			}
		}
	}

	/** Snapshot of a player's inventory contents, kept in memory only while they're locked into a match. */
	private static class InventorySnapshot {
		final List<ItemStack> items = new ArrayList<>();
		final List<ItemStack> armor = new ArrayList<>();
		final List<ItemStack> offhand = new ArrayList<>();

		static InventorySnapshot capture(ServerPlayer player) {
			InventorySnapshot snapshot = new InventorySnapshot();
			Inventory inventory = player.getInventory();
			for (ItemStack stack : inventory.items) snapshot.items.add(stack.copy());
			for (ItemStack stack : inventory.armor) snapshot.armor.add(stack.copy());
			for (ItemStack stack : inventory.offhand) snapshot.offhand.add(stack.copy());
			return snapshot;
		}

		void restore(ServerPlayer player) {
			Inventory inventory = player.getInventory();
			for (int i = 0; i < items.size() && i < inventory.items.size(); i++) inventory.items.set(i, items.get(i));
			for (int i = 0; i < armor.size() && i < inventory.armor.size(); i++) inventory.armor.set(i, armor.get(i));
			for (int i = 0; i < offhand.size() && i < inventory.offhand.size(); i++) inventory.offhand.set(i, offhand.get(i));
			player.inventoryMenu.broadcastChanges();
		}
	}
}
