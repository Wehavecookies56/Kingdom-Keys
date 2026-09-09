package online.kingdomkeys.kingdomkeys.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.encounter.RoomEncounter;
import online.kingdomkeys.kingdomkeys.entity.mob.ForetellerEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenForetellerScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.TrainingHandler;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModJsonRegistries;

import java.util.List;

public interface DialogueAction {
    void run(ServerPlayer player, LivingEntity speaker);

    default boolean ends() {
        return false;
    }

    MapCodec<? extends DialogueAction> codec();

    Type<? extends DialogueAction> type();

    Codec<DialogueAction> CODEC = ModDialogue.ACTIONS.byNameCodec().dispatch(DialogueAction::type, Type::codec);

    record Type<T extends DialogueAction>(MapCodec<T> codec) { }

    /** The master's own shop, in the currency of his union. */
    record OpenShop() implements DialogueAction {

        public static final MapCodec<OpenShop> CODEC = MapCodec.unit(OpenShop::new);

        @Override
        public void run(ServerPlayer player, LivingEntity speaker) {
            if (!(speaker instanceof ForetellerEntity master)) {
                return;
            }

            PlayerData data = PlayerData.get(player);
            if (data == null) {
                return;
            }

            PacketHandler.sendTo(new SCOpenForetellerScreen(master.getUnion(), data.serializeNBT(player.level().registryAccess())), player);
        }

        @Override
        public boolean ends() {
            return true;
        }

        @Override
        public MapCodec<OpenShop> codec() {
            return CODEC;
        }

        @Override
        public Type<OpenShop> type() {
            return ModDialogue.OPEN_SHOP.get();
        }
    }

    /** Sets the player on a lesson or a bout, by name, out of whichever registry holds it. */
    record StartEncounter(boolean duel, ResourceLocation encounter) implements DialogueAction {
        public static final MapCodec<StartEncounter> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.BOOL.optionalFieldOf("duel", false).forGetter(StartEncounter::duel),
                ResourceLocation.CODEC.fieldOf("encounter").forGetter(StartEncounter::encounter)
            ).apply(instance, StartEncounter::new)
        );

        @Override
        public void run(ServerPlayer player, LivingEntity speaker) {
            RoomEncounter lesson = (duel ? ModJsonRegistries.DUEL_ENCOUNTER : ModJsonRegistries.TRAINING_ENCOUNTER).get().getValue(encounter);
            if (lesson != null) {
                TrainingHandler.start(player, speaker, lesson);
            }
        }

        @Override
        public boolean ends() {
            return true;
        }

        @Override
        public MapCodec<StartEncounter> codec() {
            return CODEC;
        }

        @Override
        public Type<StartEncounter> type() {
            return ModDialogue.START_ENCOUNTER.get();
        }
    }

    /** Hands something over. Goes to the overflow if there is no room for it. */
    record GiveItem(ItemStack item) implements DialogueAction {
        public static final MapCodec<GiveItem> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemStack.CODEC.fieldOf("item").forGetter(GiveItem::item)
            ).apply(instance, GiveItem::new)
        );

        @Override
        public void run(ServerPlayer player, LivingEntity speaker) {
            Utils.giveItems(player, true, List.of(item.copy()));
        }

        @Override
        public MapCodec<GiveItem> codec() {
            return CODEC;
        }

        @Override
        public Type<GiveItem> type() {
            return ModDialogue.GIVE_ITEM.get();
        }
    }

    record SetFlag(ResourceLocation flag, boolean set) implements DialogueAction {
        public static final MapCodec<SetFlag> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("flag").forGetter(SetFlag::flag),
                Codec.BOOL.optionalFieldOf("set", true).forGetter(SetFlag::set)
            ).apply(instance, SetFlag::new)
        );

        @Override
        public void run(ServerPlayer player, LivingEntity speaker) {
            PlayerData data = PlayerData.get(player);
            if (data == null) {
                return;
            }

            boolean changed = set ? data.addFlag(flag) : data.removeFlag(flag);

            if (changed) {
                PacketHandler.sendTo(new SCSyncPlayerData(player), player);
            }
        }

        @Override
        public MapCodec<SetFlag> codec() {
            return CODEC;
        }

        @Override
        public Type<SetFlag> type() {
            return ModDialogue.SET_FLAG.get();
        }
    }

    /** Ends it. An answer with no node to go to would end anyway; this says so out loud. */
    record Close() implements DialogueAction {
        public static final MapCodec<Close> CODEC = MapCodec.unit(Close::new);

        @Override
        public void run(ServerPlayer player, LivingEntity speaker) {
        }

        @Override
        public boolean ends() {
            return true;
        }

        @Override
        public MapCodec<Close> codec() {
            return CODEC;
        }

        @Override
        public Type<Close> type() {
            return ModDialogue.CLOSE.get();
        }
    }
}
