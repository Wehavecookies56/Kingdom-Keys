package online.kingdomkeys.kingdomkeys.advancements;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class KKLevelUpTrigger implements CriterionTrigger<KKLevelUpTrigger.Instance> {

	public static KKLevelUpTrigger TRIGGER_LEVELUP;

	private static final ResourceLocation ID = new ResourceLocation(KingdomKeys.MODID, "level_up");
	private final Map<PlayerAdvancements, Listeners> listeners = Maps.newHashMap();

	@Override
	@Nonnull
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public void addPlayerListener(PlayerAdvancements pPlayerAdvancements, Listener<Instance> pListener) {
		KKLevelUpTrigger.Listeners listeners = this.listeners.get(pPlayerAdvancements);
		if (listeners == null) {
			listeners = new KKLevelUpTrigger.Listeners(pPlayerAdvancements);
			this.listeners.put(pPlayerAdvancements, listeners);
		}
		listeners.add(pListener);
	}

	@Override
	public void removePlayerListener(PlayerAdvancements pPlayerAdvancements, Listener<Instance> pListener) {
		KKLevelUpTrigger.Listeners listeners = this.listeners.get(pPlayerAdvancements);

		if (listeners != null) {
			listeners.remove(pListener);
			if (listeners.isEmpty()) {
				this.listeners.remove(pPlayerAdvancements);
			}
		}
	}

	@Override
	public void removePlayerListeners(PlayerAdvancements pPlayerAdvancements) {
		this.listeners.remove(pPlayerAdvancements);
	}

	public Instance createInstance(JsonObject pJson, DeserializationContext pContext) {
		int lvl = pJson.has("lvl") ? GsonHelper.getAsInt(pJson, "lvl") : 0;
		return new Instance(lvl);
	}

	public void trigger(ServerPlayer player, int lvl) {
		KKLevelUpTrigger.Listeners listeners = this.listeners.get(player.getAdvancements());
		if (listeners != null) {
			listeners.trigger(player, lvl);
		}
	}

	static class Instance implements CriterionTriggerInstance {

		int lvl;

		public Instance(int lvl) {
			this.lvl = lvl;
		}

		@Override
		public ResourceLocation getCriterion() {
			return KKLevelUpTrigger.ID;
		}

		public boolean matches(int lvl) {
			return lvl >= this.lvl;
		}

		public JsonObject serializeToJson(SerializationContext pConditions) {
			JsonObject jsonobject = new JsonObject();
			if (this.lvl != 0) {
				jsonobject.addProperty("lvl", lvl);
			}

			return jsonobject;
		}
	}

	static class Listeners {

		private final PlayerAdvancements playerAdvancements;
		private final Set<Listener<Instance>> listeners = Sets.newHashSet();

		Listeners(PlayerAdvancements playerAdvancements) {
			this.playerAdvancements = playerAdvancements;
		}

		boolean isEmpty() {
			return listeners.isEmpty();
		}

		void add(CriterionTrigger.Listener<KKLevelUpTrigger.Instance> listener) {
			this.listeners.add(listener);
		}

		void remove(CriterionTrigger.Listener<KKLevelUpTrigger.Instance> listener) {
			this.listeners.remove(listener);
		}

		public void trigger(Player player, int lvl) {
			List<Listener<Instance>> list = null;

			for (CriterionTrigger.Listener<KKLevelUpTrigger.Instance> listener : this.listeners) {
				if (list == null) {
					list = Lists.newArrayList();
				}
				list.add(listener);
			}
			if (list != null) {
				for (CriterionTrigger.Listener<KKLevelUpTrigger.Instance> listener1 : list) {

					if (listener1.getTriggerInstance().matches(lvl))
						listener1.run(this.playerAdvancements);
				}
			}
		}
	}

}