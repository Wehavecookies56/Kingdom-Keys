package online.kingdomkeys.kingdomkeys.integration.epicfight.skills;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.List;
import java.util.UUID;

public class ComboExtender extends Skill {
    private static final UUID EVENT_UUID = UUID.fromString("a42e0198-fdbc-11eb-9a03-0242ac130003");
    private final SkillDataManager.SkillDataKey<Integer> combo = (SkillDataManager.SkillDataKey<Integer>) SkillDataManager.SkillDataKey
            .findById(0);
    private final int NumberOfComboMinus = 0;
    private int NumberOfComboPlus = 0;
    private int TotalComboOffset = 0;

    public ComboExtender(Builder builder) {
        super(builder);
    }

    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        PlayerEventListener listener = container.getExecuter().getEventListener();
        listener.addEventListener(PlayerEventListener.EventType.SKILL_EXECUTE_EVENT_SERVER, EVENT_UUID, (event) -> {
            PlayerPatch spp = container.getExecuter();
            Player player = (Player) spp.getOriginal();
            if (player.isOnGround() && !player.isSprinting()) {
                event.setCanceled(true);
                IPlayerCapabilities playerCapabilities = ModCapabilities.getPlayer(player);
                StaticAnimation attackMotion;
                this.NumberOfComboPlus = playerCapabilities.getNumberOfAbilitiesEquipped(Strings.comboPlus);
                this.TotalComboOffset = this.NumberOfComboPlus - this.NumberOfComboMinus;
                CapabilityItem cap = spp.getHoldingItemCapability(InteractionHand.MAIN_HAND);
                List<StaticAnimation> combo = cap.getAutoAttckMotion(spp);
                SkillDataManager dataManager = spp.getSkill(EpicFightSkills.BASIC_ATTACK).getDataManager();
                int comboCounter = dataManager.getDataValue(this.combo);
                int comboSize = combo.size();

                if (comboCounter == (comboSize - 3) + this.TotalComboOffset)
                {
                    attackMotion = combo.get(comboSize - 3);
                    comboCounter = 0;
                }
                else
                {
                    attackMotion = combo.get(comboCounter % (comboSize - 3));
                    comboCounter++;
                }

                if (attackMotion != null) {
                    spp.playAnimationSynchronized(attackMotion, 0);
                }
                spp.updateEntityState();
                dataManager.setData(this.combo, comboCounter);
                //TODO psuedo code
        /* get combo step
            get listener for event combo
            cancel event
            if combo == totalCombo + list.size
                play last animation
            else
                play animation at list combo % list.size -1
            combo++


        */
            }
        });
    }

    @Override
    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
        PlayerEventListener listener = container.getExecuter().getEventListener();

        listener.removeListener(PlayerEventListener.EventType.SKILL_EXECUTE_EVENT_SERVER, EVENT_UUID);
    }

    @Override
    public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
        super.executeOnServer(executer, args);

    }
}
