package online.kingdomkeys.kingdomkeys.world;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncWorldData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class StruggleHandler {

	private static final int COUNTDOWN_TICKS = 60; // 3 seconds
	private static final int ROUND_TICKS = 1200; // 60 seconds
	private static final int WIN_SCORE = 200;

	private static final Map<String, Integer> countdowns = new HashMap<>();
	private static final Map<String, Integer> roundTicksLeft = new HashMap<>();
	private static final Map<UUID, InventorySnapshot> savedInventories = new HashMap<>();

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

		if (struggle.isConfigured() && struggle.getParticipants().size() >= 2) {
			Struggle.Participant p1 = struggle.getParticipants().get(0);
			Struggle.Participant p2 = struggle.getParticipants().get(1);
			if (p1.isReady() && p2.isReady()) {
				countdowns.put(name, COUNTDOWN_TICKS);
				broadcast(server, struggle, Component.literal("Struggle starting..."));
			}
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
			broadcast(server, struggle, Component.literal(String.valueOf(ticks / 20)));
		}
	}

	private void startMatch(MinecraftServer server, ServerLevel level, WorldData worldData, Struggle struggle) {
		List<Struggle.Participant> combatants = struggle.getParticipants().subList(0, 2);
		BlockPos[] corners = { struggle.getC1(), struggle.getC2() };

		for (int i = 0; i < 2; i++) {
			Struggle.Participant participant = combatants.get(i);
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

			BlockPos corner = corners[i];
			player.teleportTo(corner.getX() + 0.5, corner.getY(), corner.getZ() + 0.5);
			player.displayClientMessage(Component.literal("GO!"), true);
		}

		struggle.setInProgress(true);
		roundTicksLeft.put(struggle.getName(), ROUND_TICKS);
		worldData.setDirty();
		PacketHandler.sendToAll(new SCSyncWorldData(server));
	}

	private void tickRound(MinecraftServer server, WorldData worldData, Struggle struggle, String name) {
		Struggle.Participant winner = struggle.getParticipants().stream().filter(p -> p.getScore() >= WIN_SCORE).findFirst().orElse(null);

		int ticksLeft = roundTicksLeft.getOrDefault(name, 0) - 1;
		if (winner == null && ticksLeft <= 0) {
			winner = struggle.getParticipants().stream().max(Comparator.comparingInt(Struggle.Participant::getScore)).orElse(null);
		}

		if (winner != null) {
			endMatch(server, worldData, struggle, winner);
		} else {
			roundTicksLeft.put(name, ticksLeft);
		}
	}

	private void endMatch(MinecraftServer server, WorldData worldData, Struggle struggle, Struggle.Participant winner) {
		int combatants = Math.min(2, struggle.getParticipants().size());
		for (Struggle.Participant participant : struggle.getParticipants().subList(0, combatants)) {
			ServerPlayer player = server.getPlayerList().getPlayer(participant.getUUID());
			if (player != null) {
				InventorySnapshot snapshot = savedInventories.remove(player.getUUID());
				player.getInventory().clearContent();
				if (snapshot != null) {
					snapshot.restore(player);
				}
				boolean won = participant.getUUID().equals(winner.getUUID());
				player.displayClientMessage(Component.literal(won ? "You win!" : "You lose!"), true);
			}
			participant.setReady(false);
			participant.setScore(100);
		}

		struggle.setInProgress(false);
		roundTicksLeft.remove(struggle.getName());
		worldData.setDirty();
		PacketHandler.sendToAll(new SCSyncWorldData(server));
	}

	private static void broadcast(MinecraftServer server, Struggle struggle, Component message) {
		int combatants = Math.min(2, struggle.getParticipants().size());
		for (Struggle.Participant participant : struggle.getParticipants().subList(0, combatants)) {
			ServerPlayer player = server.getPlayerList().getPlayer(participant.getUUID());
			if (player != null) {
				player.displayClientMessage(message, true);
			}
		}
	}

	/**
	 *  Snapshot of a player's inventory contents, kept in memory only while they're locked into a match.
	 */
	private static class InventorySnapshot {
		final List<ItemStack> items = new ArrayList<>();
		final List<ItemStack> armor = new ArrayList<>();
		final List<ItemStack> offhand = new ArrayList<>();

		static InventorySnapshot capture(ServerPlayer player) {
			InventorySnapshot snapshot = new InventorySnapshot();
			Inventory inventory = player.getInventory();
			for (ItemStack stack : inventory.items)
				snapshot.items.add(stack.copy());
			for (ItemStack stack : inventory.armor)
				snapshot.armor.add(stack.copy());
			for (ItemStack stack : inventory.offhand)
				snapshot.offhand.add(stack.copy());
			return snapshot;
		}

		void restore(ServerPlayer player) {
			Inventory inventory = player.getInventory();
			for (int i = 0; i < items.size() && i < inventory.items.size(); i++)
				inventory.items.set(i, items.get(i));
			for (int i = 0; i < armor.size() && i < inventory.armor.size(); i++)
				inventory.armor.set(i, armor.get(i));
			for (int i = 0; i < offhand.size() && i < inventory.offhand.size(); i++)
				inventory.offhand.set(i, offhand.get(i));
			player.inventoryMenu.broadcastChanges();
		}
	}
}
