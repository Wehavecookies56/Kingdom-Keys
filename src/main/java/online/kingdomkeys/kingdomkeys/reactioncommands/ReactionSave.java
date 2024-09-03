package online.kingdomkeys.kingdomkeys.reactioncommands;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.block.SavePointBlock;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.block.SavepointTileEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenSavePointScreen;
import online.kingdomkeys.kingdomkeys.world.SavePointStorage;

public class ReactionSave extends ReactionCommand {


    public ReactionSave(ResourceLocation registryName) {
        super(registryName, true);
    }

    @Override
    public void onUse(Player player, LivingEntity target, LivingEntity lockedOnEntity) {
        if (conditionsToAppear(player, target)) {
            ((ServerPlayer) player).setRespawnPosition(player.level().dimension(), player.getOnPos().above(), 0F, true, false);
            player.displayClientMessage(Component.translatable("block.minecraft.set_spawn"), true);
            if (player.getBlockStateOn().getValue(SavePointBlock.TIER) != SavePointStorage.SavePointType.NORMAL) {
                PacketHandler.sendTo(new SCOpenSavePointScreen((SavepointTileEntity) player.level().getBlockEntity(player.getOnPos()), player), (ServerPlayer) player);
            }
        }
    }

    @Override
    public boolean conditionsToAppear(Player player, LivingEntity target) {
        return player.getBlockStateOn().getBlock() instanceof SavePointBlock;
    }

    @Override
    public SoundEvent getUseSound(Player player, LivingEntity target) {
        return ModSounds.savespawn.get();
    }
}
