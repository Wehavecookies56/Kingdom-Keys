package online.kingdomkeys.kingdomkeys.world;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.StruggleBoardBlock;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.drops.StruggleOrbEntity;
import online.kingdomkeys.kingdomkeys.lib.Struggle;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCCloseScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowMessagesPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncWorldData;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class StruggleHandler {

	private static final int COUNTDOWN_TICKS = 60; // 3 seconds
	private static final int OVERTIME_TICKS = 200; // 10 seconds, tournament sudden-death on a tie
	private static final int ROUND_WINNER_ANNOUNCE_TICKS = 70; // ~3.5s, matches the Title's own display time
	private static final Random RANDOM = new Random();

	private static final Map<String, Integer> countdowns = new HashMap<>();
	private static final Map<String, Integer> roundTicksLeft = new HashMap<>();
	private static final Map<String, Integer> announceDelay = new HashMap<>();
	/** Who is (about to be) actually fighting right now, keyed by struggle name. */
	private static final Map<String, List<UUID>> activeCombatants = new HashMap<>();
	public record WeaponSlot(int slot, Item weapon) {}

	private static final Map<UUID, WeaponSlot> weaponSlots = new HashMap<>();

	public static boolean isWeaponLocked(UUID id) {
		return weaponSlots.containsKey(id);
	}

	@SubscribeEvent
	public void onServerTick(ServerTickEvent.Post event) {
		MinecraftServer server = event.getServer();
		WorldData worldData = WorldData.get(server);
		ServerLevel overworld = server.overworld();

		for (Struggle struggle : new ArrayList<>(worldData.getStruggles())) {
			if (cleanupIfOrphaned(server, overworld, worldData, struggle)) {
				continue; // struggle is gone now, nothing left to tick for it
			}

			tick(server, overworld, worldData, struggle);

			// While a match is running, keep every combatant's weapon slot selected no matter what they try to switch to - this is what makes them "unable to change" out of the Struggle weapon.
			if (struggle.isInProgress()) {
				for (UUID id : struggle.getActiveCombatantIds()) {
					WeaponSlot weaponSlot = weaponSlots.get(id);
					if (weaponSlot == null) continue;
					ServerPlayer player = server.getPlayerList().getPlayer(id);
					if (player == null) continue;

					player.getInventory().selected = weaponSlot.slot();

					ItemStack current = player.getInventory().items.get(weaponSlot.slot());
					if (current.isEmpty() || current.getItem() != weaponSlot.weapon()) {
						player.getInventory().items.set(weaponSlot.slot(), new ItemStack(weaponSlot.weapon()));
						player.inventoryMenu.broadcastChanges();
					}
				}
			}
		}
	}

	private boolean cleanupIfOrphaned(MinecraftServer server, ServerLevel level, WorldData worldData, Struggle struggle) {
		BlockPos pos = struggle.getPos();
		if (pos == null || !level.isLoaded(pos))
			return false;
		if (level.getBlockState(pos).getBlock() instanceof StruggleBoardBlock)
			return false;

		if (struggle.isInProgress()) {
			for (UUID id : new ArrayList<>(struggle.getActiveCombatantIds())) {
				ServerPlayer player = server.getPlayerList().getPlayer(id);
				if (player != null)
					removeWeapon(player);
			}
		}

		KingdomKeys.LOGGER.info("Removed orphaned Struggle '{}' - its board block at {} is gone", struggle.getName(), pos);
		if (struggle.getC1() != null && struggle.getC2() != null) {
			worldData.saveStruggleCorners(pos, struggle.getC1(), struggle.getC2());
		}
		worldData.removeStruggle(struggle);
		worldData.setDirty();
		PacketHandler.sendToAll(new SCSyncWorldData(server));
		return true;
	}

	/**
	 * If someone disconnects while actively fighting, they lose immediately (in a 1v1 - Duel or a
	 * Tournament match - the other combatant is declared the winner right away; in FFA with more than 2
	 * still fighting, they're just removed and everyone else keeps going). Either way they're also
	 * removed from the match's roster entirely, win or not.
	 */
	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer player)) return;
		MinecraftServer server = player.getServer();
		if (server == null) return;
		WorldData worldData = WorldData.get(server);
		UUID id = player.getUUID();

		for (Struggle struggle : new ArrayList<>(worldData.getStruggles())) {
			if (struggle.isInProgress() && struggle.getActiveCombatantIds().contains(id)) {
				List<UUID> activeIds = struggle.getActiveCombatantIds();
				if (activeIds.size() <= 2) {
					List<Struggle.Participant> combatants = activeIds.stream().map(struggle::getParticipant).filter(Objects::nonNull).collect(Collectors.toList());
					Struggle.Participant winner = combatants.stream().filter(p -> !p.getUUID().equals(id)).findFirst().orElse(null);
					if (winner != null) {
						endMatch(server, worldData, struggle, winner, combatants);
					}
				} else {
					// FFA with others still fighting - just drop them, everyone else keeps going.
					activeIds.remove(id);
				}
			}

			if (struggle.hasParticipant(id)) {
				worldData.removeStruggleParticipant(struggle, id);
			}
		}

		worldData.setDirty();
		PacketHandler.sendToAll(new SCSyncWorldData(server));
	}

	public static WeaponSlot findAnyWeaponSlot(Inventory inventory) {
		for (int i = 0; i < 9; i++) {
			Item item = inventory.items.get(i).getItem();
			if (Struggle.weapons().contains(item))
				return new WeaponSlot(i, item);
		}
		return null;
	}

	private void tick(MinecraftServer server, ServerLevel level, WorldData worldData, Struggle struggle) {
		String name = struggle.getName();

		if (struggle.isInProgress()) {
			tickRound(server, worldData, struggle, name);
			return;
		}

		if (announceDelay.containsKey(name)) {
			int t = announceDelay.get(name) - 1;
			if (t <= 0) announceDelay.remove(name);
			else announceDelay.put(name, t);
			return; // hold off starting the next match while the round-winner announcement is showing
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
			KingdomKeys.LOGGER.debug("Struggle '{}' countdown started", name);
			sendTitle(server, activeCombatants.get(name), "kingdomkeys.struggle.starting", "");
		}
	}

	// TOURNAMENT
	/**
	 * Builds a fresh single-elimination bracket from the current roster (shuffled), sized to the next
	 * power of 2 - extra slots are byes. Immediately resolves any bye matches (a real player against an
	 * empty slot advances automatically, cascading through multiple rounds if needed).
	 */
	private void buildBracket(Struggle struggle) {
		List<UUID> players = struggle.getParticipants().stream().map(Struggle.Participant::getUUID).collect(Collectors.toList());
		Collections.shuffle(players, RANDOM);

		int size = 1;
		while (size < players.size()) size *= 2;

		List<List<UUID>> bracket = struggle.getBracket();
		bracket.clear();

		List<UUID> round1 = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			round1.add(i < players.size() ? players.get(i) : null);
		}
		bracket.add(round1);

		int roundSize = size / 2;
		while (roundSize >= 1) {
			List<UUID> round = new ArrayList<>();
			for (int i = 0; i < roundSize; i++) round.add(null);
			bracket.add(round);
			roundSize /= 2;
		}

		resolveByes(struggle);
	}

	private void resolveByes(Struggle struggle) {
		List<List<UUID>> bracket = struggle.getBracket();
		if (bracket.isEmpty()) return;

		boolean[] permanentlyEmpty = new boolean[bracket.get(0).size()];
		for (int i = 0; i < permanentlyEmpty.length; i++) {
			permanentlyEmpty[i] = bracket.get(0).get(i) == null;
		}

		for (int r = 0; r < bracket.size() - 1; r++) {
			List<UUID> round = bracket.get(r);
			List<UUID> nextRound = bracket.get(r + 1);
			boolean[] nextPermanentlyEmpty = new boolean[nextRound.size()];

			for (int i = 0; i < round.size(); i += 2) {
				int parentIndex = i / 2;
				boolean aEmpty = permanentlyEmpty[i];
				boolean bEmpty = permanentlyEmpty[i + 1];

				if (aEmpty && bEmpty) {
					// No one ever comes from this pair - the parent slot stays permanently empty too.
					nextPermanentlyEmpty[parentIndex] = true;
				} else if (aEmpty != bEmpty) {
					// Exactly one side is empty: the other side advances as a bye (only if not already
					// filled - don't clobber a real match result that happened to run before this call).
					if (nextRound.get(parentIndex) == null) {
						nextRound.set(parentIndex, aEmpty ? round.get(i + 1) : round.get(i));
					}
				}
				// Both sides real: a genuine match that has to actually be played - leave nextRound
				// alone (null = still pending), regardless of what its current value happens to be.
			}

			permanentlyEmpty = nextPermanentlyEmpty;
		}
	}

	/** The first pair (round index, slot index of the first of the pair) with two real players whose
	 * winner hasn't been decided/placed yet - i.e. the next match that needs to be fought. */
	private int[] findNextMatch(Struggle struggle) {
		List<List<UUID>> bracket = struggle.getBracket();
		for (int r = 0; r < bracket.size() - 1; r++) {
			List<UUID> round = bracket.get(r);
			List<UUID> nextRound = bracket.get(r + 1);
			for (int i = 0; i < round.size(); i += 2) {
				UUID a = round.get(i);
				UUID b = round.get(i + 1);
				int parentIndex = i / 2;
				if (a != null && b != null && nextRound.get(parentIndex) == null) {
					return new int[]{r, i};
				}
			}
		}
		return null;
	}

	private void tryStartTournament(MinecraftServer server, WorldData worldData, Struggle struggle, String name) {
		if (!struggle.isConfigured()) return;

		if (struggle.getBracket().isEmpty()) {
			if (struggle.getParticipants().size() < 2) return;
			if (struggle.getParticipants().stream().anyMatch(p -> !p.isReady())) return;

			buildBracket(struggle);
			worldData.setDirty();
			PacketHandler.sendToAll(new SCSyncWorldData(server));
		}

		int[] next = findNextMatch(struggle);
		if (next == null)
			return; // nothing left to fight (tournament should already have ended)

		List<UUID> round = struggle.getBracket().get(next[0]);
		List<UUID> pair = List.of(round.get(next[1]), round.get(next[1] + 1));
		activeCombatants.put(name, pair);
		countdowns.put(name, COUNTDOWN_TICKS);
		KingdomKeys.LOGGER.debug("Struggle '{}' countdown started", name);
		sendTitle(server, pair, "kingdomkeys.struggle.tournament.next_match", "");
	}

	private void tryStartFfa(MinecraftServer server, WorldData worldData, Struggle struggle, String name) {
		if (!struggle.isConfigured())
			return;
		if (struggle.getParticipants().size() < 2)
			return;
		// Wait for EVERYONE currently registered to be ready, same gate as Tournament - otherwise late arrivals who haven't hit Ready yet would get left out of the fight entirely.
		if (struggle.getParticipants().stream().anyMatch(p -> !p.isReady()))
			return;

		List<UUID> everyone = struggle.getParticipants().stream().map(Struggle.Participant::getUUID).collect(Collectors.toList());
		activeCombatants.put(name, everyone);
		countdowns.put(name, COUNTDOWN_TICKS);
		KingdomKeys.LOGGER.debug("Struggle '{}' countdown started", name);
		sendTitle(server, everyone, "kingdomkeys.struggle.ffa.starting", "");
	}

	private void tickCountdown(MinecraftServer server, ServerLevel level, WorldData worldData, Struggle struggle, String name) {
		int ticks = countdowns.get(name);

		if (ticks <= 0) {
			countdowns.remove(name);
			startMatch(server, level, worldData, struggle);
			return;
		}

		if (ticks % 20 == 0) {
			sendTitle(server, activeCombatants.getOrDefault(name, List.of()), String.valueOf(ticks / 20), "");
		}

		countdowns.put(name, ticks - 1);
	}

	private void startMatch(MinecraftServer server, ServerLevel level, WorldData worldData, Struggle struggle) {
		List<UUID> combatantIds = activeCombatants.getOrDefault(struggle.getName(), List.of());
		List<BlockPos> spawnPositions = computeSpawnPositions(struggle, combatantIds.size());
		boolean facingPairs = combatantIds.size() == 2;

		for (int i = 0; i < combatantIds.size(); i++) {
			Struggle.Participant participant = struggle.getParticipant(combatantIds.get(i));
			if (participant == null) continue;
			ServerPlayer player = server.getPlayerList().getPlayer(participant.getUUID());
			if (player == null) continue;

			participant.setScore(struggle.getStartingScore());
			participant.setReady(false);

			Inventory inventory = player.getInventory();
			WeaponSlot weaponSlot = findAnyWeaponSlot(inventory);
			if (weaponSlot != null) {
				inventory.selected = weaponSlot.slot();
				weaponSlots.put(player.getUUID(), weaponSlot);
			} else {
				KingdomKeys.LOGGER.warn("Struggle combatant {} lost their weapon between readying up and match start - fighting bare-handed", player.getName().getString());
			}

			BlockPos spawn = spawnPositions.get(i);
			float yaw = facingPairs ? yawTowards(spawn, spawnPositions.get(i == 0 ? 1 : 0)) : player.getYRot();
			float pitch = facingPairs ? 0F : player.getXRot();
			player.teleportTo(level, spawn.getX() + 0.5, spawn.getY(), spawn.getZ() + 0.5, java.util.Set.of(), yaw, pitch);
			player.setYHeadRot(yaw);

			PacketHandler.sendTo(new SCCloseScreen(), player); // close any open UI (menus, inventory, etc)
		}

		sendTitle(server, combatantIds, "kingdomkeys.struggle.go", "");

		struggle.setInProgress(true);
		int roundTicks = struggle.getRoundTimeSeconds() * 20;
		struggle.setRoundSecondsLeft(struggle.getRoundTimeSeconds());
		struggle.getActiveCombatantIds().clear();
		struggle.getActiveCombatantIds().addAll(combatantIds);
		roundTicksLeft.put(struggle.getName(), roundTicks);
		worldData.setDirty();
		KingdomKeys.LOGGER.debug("Struggle '{}' match STARTED, combatants={}", struggle.getName(), combatantIds);
		PacketHandler.sendToAll(new SCSyncWorldData(server));
	}

	/** The yaw (in degrees) to look from `from` towards `to`, so combatants can be turned to face each
	 * other instead of just facing wherever the board happened to be facing. */
	private float yawTowards(BlockPos from, BlockPos to) {
		double dx = to.getX() - from.getX();
		double dz = to.getZ() - from.getZ();
		return (float) (Mth.atan2(dz, dx) * (180D / Math.PI)) - 90F;
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
		List<Struggle.Participant> combatants = combatantIds.stream().map(struggle::getParticipant).filter(Objects::nonNull).collect(Collectors.toList());

		int winScore = struggle.getStartingScore() * Math.max(1, combatants.size());
		Struggle.Participant winner = combatants.stream().filter(p -> p.getScore() >= winScore).findFirst().orElse(null);

		int ticksLeft = roundTicksLeft.getOrDefault(name, 0) - 1;
		if (winner == null && ticksLeft <= 0) {
			int maxScore = combatants.stream().mapToInt(Struggle.Participant::getScore).max().orElse(0);
			List<Struggle.Participant> topScorers = combatants.stream().filter(p -> p.getScore() == maxScore).collect(Collectors.toList());

			if (topScorers.size() == 1) {
				winner = topScorers.get(0);
			} else {
				// Tied at time-up. Duel/FFA just end as a draw; a Tournament match can't have a draw
				// (someone has to advance), so it goes into a short sudden-death overtime instead.
				if (struggle.getMode() == Struggle.Mode.TOURNAMENT) {
					roundTicksLeft.put(name, OVERTIME_TICKS);
					struggle.setRoundSecondsLeft(OVERTIME_TICKS / 20);
					worldData.setDirty();
					sendTitle(server, combatantIds, "kingdomkeys.struggle.tie.overtime", "");
					PacketHandler.sendToAll(new SCSyncWorldData(server));
				} else {
					endMatchDraw(server, worldData, struggle, combatants);
				}
				return;
			}
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

	/** Removes the weapon from a combatant's hotbar (if they still have it there) and frees their slot,
	 * WITHOUT touching anything else in their inventory - there's nothing else to restore. */
	/** Stops tracking/locking a combatant's weapon slot once their match ends - the weapon is their own
	 * item now (not a temporary gift), so it stays in their inventory; this just lifts the "selection
	 * locked to this slot" restriction from tick() above. */
	private void removeWeapon(ServerPlayer player) {
		weaponSlots.remove(player.getUUID());
	}

	/** Sends a combatant out of the arena once their match is over, if the owner set a spectator spot
	 * for this board - otherwise they're just left wherever they ended up fighting. */
	private void teleportToSpectatorArea(Struggle struggle, ServerPlayer player) {
		BlockPos pos = struggle.getSpectatorPos();
		if (pos != null) {
			player.teleportTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
		}
	}

	/** Duel/FFA tie at time-up: everyone just goes back to normal, no winner/loser declared. */
	private void endMatchDraw(MinecraftServer server, WorldData worldData, Struggle struggle, List<Struggle.Participant> combatants) {
		for (Struggle.Participant participant : combatants) {
			ServerPlayer player = server.getPlayerList().getPlayer(participant.getUUID());
			if (player != null) {
				removeWeapon(player);
				sendTitle(server, List.of(participant.getUUID()), "kingdomkeys.struggle.draw", "");
				teleportToSpectatorArea(struggle, player);
			}
			participant.setReady(false);
			participant.setScore(struggle.getStartingScore());
		}

		activeCombatants.remove(struggle.getName());
		struggle.getActiveCombatantIds().clear();
		roundTicksLeft.remove(struggle.getName());
		struggle.setRoundSecondsLeft(-1);
		struggle.setInProgress(false);
		KingdomKeys.LOGGER.debug("Struggle '{}' match ENDED (draw)", struggle.getName());

		despawnOrbs(server.overworld(), struggle);

		worldData.setDirty();
		PacketHandler.sendToAll(new SCSyncWorldData(server));
	}

	private void endMatch(MinecraftServer server, WorldData worldData, Struggle struggle, Struggle.Participant winner, List<Struggle.Participant> combatants) {
		for (Struggle.Participant participant : combatants) {
			ServerPlayer player = server.getPlayerList().getPlayer(participant.getUUID());
			if (player != null) {
				removeWeapon(player);
				boolean won = participant.getUUID().equals(winner.getUUID());
				sendTitle(server, List.of(participant.getUUID()), won ? "kingdomkeys.struggle.win" : "kingdomkeys.struggle.lose", "");
				teleportToSpectatorArea(struggle, player);
			}
			participant.setReady(false);
			participant.setScore(struggle.getStartingScore());
		}

		activeCombatants.remove(struggle.getName());
		struggle.getActiveCombatantIds().clear();
		roundTicksLeft.remove(struggle.getName());
		struggle.setRoundSecondsLeft(-1);
		struggle.setInProgress(false);
		KingdomKeys.LOGGER.debug("Struggle '{}' match ENDED, winner={}", struggle.getName(), winner.getUsername());

		despawnOrbs(server.overworld(), struggle);

		if (struggle.getMode() == Struggle.Mode.TOURNAMENT) {
			handleTournamentAdvance(server, worldData, struggle, winner, combatants);
		}

		worldData.setDirty();
		PacketHandler.sendToAll(new SCSyncWorldData(server));
	}

	/**
	 * Removes any leftover orbs from this match so they don't linger around after the fight (or get
	 * picked up outside of combat). Scoped to a generous area around the arena rather than the whole
	 * level, both for performance and because orbs only ever spawn there in the first place.
	 */
	private void despawnOrbs(ServerLevel level, Struggle struggle) {
		BlockPos c1 = struggle.getC1();
		BlockPos c2 = struggle.getC2();
		if (c1 == null || c2 == null) return;

		AABB searchArea = new AABB(c1.getCenter(), c2.getCenter()).inflate(32);
		for (StruggleOrbEntity orb : level.getEntitiesOfClass(StruggleOrbEntity.class, searchArea)) {
			if (orb.getStruggleName().equals(struggle.getName())) {
				orb.discard();
			}
		}
	}

	private void handleTournamentAdvance(MinecraftServer server, WorldData worldData, Struggle struggle, Struggle.Participant winner, List<Struggle.Participant> combatants) {
		List<List<UUID>> bracket = struggle.getBracket();

		// Find which pair this match was and place the winner in the parent (next round) slot.
		outer:
		for (int r = 0; r < bracket.size() - 1; r++) {
			List<UUID> round = bracket.get(r);
			for (int i = 0; i < round.size(); i += 2) {
				UUID a = round.get(i);
				UUID b = round.get(i + 1);
				if (a == null || b == null || combatants.size() != 2) continue;
				boolean matches = (a.equals(combatants.get(0).getUUID()) && b.equals(combatants.get(1).getUUID()))
						|| (a.equals(combatants.get(1).getUUID()) && b.equals(combatants.get(0).getUUID()));
				if (matches) {
					bracket.get(r + 1).set(i / 2, winner.getUUID());
					resolveByes(struggle); // in case this newly-placed winner unblocks a further bye
					break outer;
				}
			}
		}

		List<UUID> finalRound = bracket.get(bracket.size() - 1);
		if (finalRound.get(0) != null) {
			// Champion decided!
			Struggle.Participant champion = struggle.getParticipant(finalRound.get(0));
			String championName = champion != null ? champion.getUsername() : "?";
			List<UUID> everyone = struggle.getParticipants().stream().map(Struggle.Participant::getUUID).collect(Collectors.toList());
			sendTitle(server, everyone, "kingdomkeys.struggle.tournament.champion", championName);

			struggle.getBracket().clear();
		} else {
			// Announce who won this round and hold off starting the next match's countdown for a bit,
			// so the announcement is actually visible instead of being instantly replaced.
			announceDelay.put(struggle.getName(), ROUND_WINNER_ANNOUNCE_TICKS);
			List<UUID> everyone = struggle.getParticipants().stream().map(Struggle.Participant::getUUID).collect(Collectors.toList());
			sendTitle(server, everyone, "kingdomkeys.struggle.tournament.round_winner", winner.getUsername());
		}
	}

	/**
	 * Shows a big centered title (same system as the rest of the mod, e.g. Castle Oblivion encounter
	 * messages) instead of a hotbar/action bar message. titleKey/subtitleKey are translation keys - pass
	 * "" for no subtitle. Some announcements (tournament champion/round winner) use a raw player name as
	 * the "subtitle" instead of a translation key - see the class javadoc for why that's fine.
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
}