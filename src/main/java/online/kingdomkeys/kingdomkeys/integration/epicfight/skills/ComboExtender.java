package online.kingdomkeys.kingdomkeys.integration.epicfight.skills;

import java.util.List;
import java.util.UUID;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import yesman.epicfight.api.animation.AnimationProvider;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.skill.*;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

public class ComboExtender extends Skill {
	private static final UUID EVENT_UUID = UUID.fromString("a42e0198-fdbc-11eb-9a03-0242ac130003");
	private final RegistryObject<SkillDataKey<Integer>> combo = SkillDataKeys.COMBO_COUNTER;
	public static final DeferredRegister<SkillDataKey<?>> DATA_KEYS = DeferredRegister.create(new ResourceLocation(EpicFightMod.MODID, "skill_data_keys"), KingdomKeys.MODID);
	public static final RegistryObject<SkillDataKey<Integer>> FINISHER_DATA = DATA_KEYS.register("finisher_data", () -> SkillDataKey.createIntKey(0, false, ComboExtender.class));
	//private final SkillDataKey<Integer> finisherData = SkillDataKey.createDataKey(ValueType.INTEGER);
	private int numberOfNegativeCombo = 0;
	private int numberOfComboPlus = 0;
	private int numberOfFinishingPlus = 0;
	private int totalComboOffset = 0;
	private int finisherPlacement = 0;
	private final int lastBasicAttackFromEnd = 4;

	public ComboExtender(Builder builder) {
		super(builder);
	}

	@Override
	public void onInitiate(SkillContainer container) {
		super.onInitiate(container);
		PlayerPatch pp = container.getExecuter();
		if (!pp.isLogicalClient()) {
			PlayerEventListener listener = container.getExecuter().getEventListener();
			listener.addEventListener(PlayerEventListener.EventType.SKILL_EXECUTE_EVENT, EVENT_UUID, event -> {

				PlayerPatch<?> spp = container.getExecuter();
				Player player = spp.getOriginal();
				if (player.onGround() && !player.isSprinting() && event.getSkillContainer().getSkill() == EpicFightSkills.BASIC_ATTACK) {
					if (!this.isExecutableState(spp))
						return;
					PlayerData playerCapabilities = PlayerData.get(player);
					event.setCanceled(true);
					AnimationProvider<?> attackMotion;
					this.numberOfComboPlus = playerCapabilities.getNumberOfAbilitiesEquipped(Strings.comboPlus);
					this.numberOfNegativeCombo = playerCapabilities.getNumberOfAbilitiesEquipped(Strings.negativeCombo);
					this.numberOfFinishingPlus = playerCapabilities.getNumberOfAbilitiesEquipped(Strings.finishingPlus);
					this.totalComboOffset = this.numberOfComboPlus - this.numberOfNegativeCombo;

					CapabilityItem cap = spp.getHoldingItemCapability(InteractionHand.MAIN_HAND);
					List<AnimationProvider<?>> combo = cap.getAutoAttckMotion(spp);
					SkillDataManager dataManager = spp.getSkill(EpicFightSkills.BASIC_ATTACK).getDataManager();
					int comboCounter = dataManager.getDataValue(this.combo.get());

					int comboSize = combo.size();
					if ((comboSize - lastBasicAttackFromEnd) + this.totalComboOffset < 0)
						this.totalComboOffset -= (comboSize - lastBasicAttackFromEnd) + this.totalComboOffset;

					if (comboCounter >= (comboSize - lastBasicAttackFromEnd) + this.totalComboOffset) {
						SkillDataManager finishDataManager = spp.getSkill(this).getDataManager();
						if (finishDataManager.getDataValue(FINISHER_DATA.get()) == null) {
							container.getDataManager().registerData(FINISHER_DATA.get());
							container.getDataManager().setData(FINISHER_DATA.get(), 0);
						}
						finisherPlacement = finishDataManager.getDataValue(FINISHER_DATA.get());
						int finisher = (finisherPlacement % 2) + (comboSize - lastBasicAttackFromEnd);
						if (comboCounter >= (comboSize - lastBasicAttackFromEnd) + this.totalComboOffset + numberOfFinishingPlus) {
							comboCounter = 0;
							finisherPlacement = 0;
						} else {
							comboCounter++;
							finisherPlacement++;
						}
						attackMotion = combo.get(finisher);
						finishDataManager.setData(FINISHER_DATA.get(), finisherPlacement);
					} else {
						attackMotion = combo.get(comboCounter % (comboSize - 4));
						comboCounter++;
					}

					if (attackMotion != null) {
						spp.playAnimationSynchronized(attackMotion.get(), 0);
					}
					dataManager.setData(this.combo.get(), comboCounter);
					spp.updateEntityState();

				}
			});
		}
	}

	@Override
	public void onRemoved(SkillContainer container) {
		super.onRemoved(container);
		PlayerEventListener listener = container.getExecuter().getEventListener();

		listener.removeListener(PlayerEventListener.EventType.SKILL_EXECUTE_EVENT, EVENT_UUID);
	}

	@Override
	public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		super.executeOnServer(executer, args);
	}

	@Override
	public boolean isExecutableState(PlayerPatch<?> executer) {
		EntityState playerState = executer.getEntityState();
		Player player = executer.getOriginal();

		return !(player.isSpectator() || executer.isInAir() || !playerState.canBasicAttack());
	}
}
