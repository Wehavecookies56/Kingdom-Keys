package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiHangarBlock;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.block.GummiHangarTileEntity;
import online.kingdomkeys.kingdomkeys.menu.GummiHangarMenu;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;

public record CSUpgradeGummiHangarPacket(int containerID) implements Packet {

	public static final Type<CSUpgradeGummiHangarPacket> TYPE = new Type<>(KingdomKeys.rl("cs_upgrade_gummi_ship"));

	public static final StreamCodec<FriendlyByteBuf, CSUpgradeGummiHangarPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			CSUpgradeGummiHangarPacket::containerID,
			CSUpgradeGummiHangarPacket::new
	);

    @Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		if (player.containerMenu.containerId != containerID)
			return;

		GummiHangarMenu container = (GummiHangarMenu) player.containerMenu;
		BlockPos origin = container.TE.getBlockPos();
		Level level = player.level();
		BlockState hangar = level.getBlockState(origin);
		int lvl = hangar.getValue(GummiHangarBlock.LEVEL);
		if(lvl < 4){
			int cost = Utils.getHangarCosts(lvl);
			PlayerData playerData = PlayerData.get(player);

			if (playerData.getMunny() >= cost) {
				playerData.setMunny(playerData.getMunny() - cost);
				level.setBlockAndUpdate(origin,hangar.setValue(GummiHangarBlock.LEVEL, lvl + 1));

                // Update TE energystorage
                BlockEntity tileEntity = level.getBlockEntity(origin);
                if(tileEntity instanceof GummiHangarTileEntity TE) {
                    int currEnergy = TE.energyStorage.getEnergyStored();
                    TE.energyStorage = Utils.getEnergyStoragePerLevel(lvl+1);
                    TE.energyStorage.setEnergy(currEnergy);
                    TE.invalidateCapabilities();
                }
				PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
			}
		}

	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
