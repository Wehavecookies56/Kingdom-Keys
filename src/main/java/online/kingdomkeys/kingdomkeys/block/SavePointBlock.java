package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.SavepointTileEntity;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCDeleteSavePointScreenshot;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.network.stc.SCUpdateSavePoints;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.SavePointStorage;

import javax.annotation.Nullable;
import java.util.List;

public class SavePointBlock extends BaseBlock implements EntityBlock, INoDataGen {
	private static final VoxelShape collisionShape = Block.box(1.0D, 0.0D, 1.0D, 16.0D, 1.0D, 16.0D);

	public static final EnumProperty<SavePointStorage.SavePointType> TIER = EnumProperty.create("tier", SavePointStorage.SavePointType.class);

	public SavePointBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(TIER, SavePointStorage.SavePointType.NORMAL));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		ItemStack held = pContext.getItemInHand();
		if (held.getItem() instanceof BlockItem blockItem) {
			if (blockItem.getBlock() == ModBlocks.savepoint.get()) {
				if (held.has(ModComponents.SAVE_POINT_TIER)) {
					return this.defaultBlockState().setValue(TIER, SavePointStorage.SavePointType.valueOf(held.get(ModComponents.SAVE_POINT_TIER)));
				}
			}
		}
		return this.defaultBlockState().setValue(TIER, SavePointStorage.SavePointType.NORMAL);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		if (stack.has(ModComponents.SAVE_POINT_TIER)) {
			if (tooltipComponents.get(0) != null) {
				if (stack.get(ModComponents.SAVE_POINT_TIER).equals(SavePointStorage.SavePointType.LINKED.getSerializedName().toUpperCase())) {
					tooltipComponents.set(0, Component.translatable("block." + KingdomKeys.MODID + ".linked_savepoint"));
				} else if (stack.get(ModComponents.SAVE_POINT_TIER).equals(SavePointStorage.SavePointType.WARP.getSerializedName().toUpperCase())) {
					tooltipComponents.set(0, Component.translatable("block." + KingdomKeys.MODID + ".warp_point"));
				}
			}
		}
		super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		super.createBlockStateDefinition(pBuilder);
		pBuilder.add(TIER);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return collisionShape;
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return collisionShape;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return Block.box(0D, 0D, 0D, 16.0D, 2.0D, 16.0D);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
		if (pNewState.getBlock() != this) {
			if (pState.getValue(TIER) != SavePointStorage.SavePointType.NORMAL) {
				if (!pLevel.isClientSide()) {
					SavepointTileEntity te = (SavepointTileEntity) pLevel.getBlockEntity(pPos);
					SavePointStorage storage = SavePointStorage.getStorage(pLevel.getServer());
					if (storage.savePointRegistered(te.getID())) {
						SavePointStorage.SavePoint removed = storage.getSavePoint(te.getID());
						storage.removeSavePoint(te.getID());
						for (Level level : pLevel.getServer().getAllLevels()) {
							for (Player playerFromList : level.players()) {
								PacketHandler.sendTo(new SCUpdateSavePoints(storage.getDiscoveredSavePoints(playerFromList)), (ServerPlayer) playerFromList);
								PacketHandler.sendTo(new SCDeleteSavePointScreenshot(removed.name(), removed.id()), (ServerPlayer) playerFromList);
							}
						}
					}
				}
			}
		}
		super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		if (!stack.isEmpty() && worldIn.getBlockEntity(pos) instanceof SavepointTileEntity savepoint) {
			if (worldIn.isClientSide)
				return ItemInteractionResult.SUCCESS;

			String list = switch(state.getValue(TIER)){
                case NORMAL -> ModConfigs.savePointRecovers;
                case LINKED -> ModConfigs.linkedSavePointRecovers;
                case WARP -> ModConfigs.warpPointRecovers;
            };

			if(stack.getItem() == ModItems.orichalcum.get()){
				if(savepoint.getHeal() > 1 && list.contains("HP")){
					stack.shrink(1);
					savepoint.setHeal(Math.max(savepoint.getHeal() - 4, 1));
					player.displayClientMessage(Component.translatable("savepoint.upgrade", Utils.translateToLocal("savepoint.healing"),Utils.getSavepointPercent(savepoint.getHeal())), true);
				} else {
					player.displayClientMessage(Component.translatable("savepoint.maxed", Utils.translateToLocal("savepoint.healing")), true);
				}
			} else if(stack.getItem() == ModItems.illusory_crystal.get()){
				if(savepoint.getMagic() > 1 && list.contains("MP")){
					stack.shrink(1);
					savepoint.setMagic(Math.max(savepoint.getMagic() - 4, 1));
					player.displayClientMessage(Component.translatable("savepoint.upgrade", Utils.translateToLocal("savepoint.magic"),Utils.getSavepointPercent(savepoint.getMagic())), true);
				} else {
					player.displayClientMessage(Component.translatable("savepoint.maxed", Utils.translateToLocal("savepoint.magic")), true);
				}
			} else if(stack.getItem() == ModItems.hungry_crystal.get()){
				if(savepoint.getHunger() > 1 && list.contains("HUNGER")){
					stack.shrink(1);
					savepoint.setHunger(Math.max(savepoint.getHunger() - 4, 1));
					player.displayClientMessage(Component.translatable("savepoint.upgrade", Utils.translateToLocal("savepoint.feed"),Utils.getSavepointPercent(savepoint.getHunger())), true);
				} else {
					player.displayClientMessage(Component.translatable("savepoint.maxed", Utils.translateToLocal("savepoint.feed")), true);
				}
			} else if(stack.getItem() == ModItems.remembrance_crystal.get()){
				if(savepoint.getFocus() > 1 && list.contains("FOCUS")){
					stack.shrink(1);
					savepoint.setFocus(Math.max(savepoint.getFocus() - 4, 1));
					player.displayClientMessage(Component.translatable("savepoint.upgrade", Utils.translateToLocal("savepoint.focus"),Utils.getSavepointPercent(savepoint.getFocus())), true);
				} else {
					player.displayClientMessage(Component.translatable("savepoint.maxed", Utils.translateToLocal("savepoint.focus")), true);
				}
			} else if(stack.getItem() == ModItems.evanescent_crystal.get()){
				if(savepoint.getDrive() > 1 && list.contains("DRIVE")){
					stack.shrink(1);
					savepoint.setDrive(Math.max(savepoint.getDrive() - 4, 1));
					player.displayClientMessage(Component.translatable("savepoint.upgrade", Utils.translateToLocal("savepoint.drive"),Utils.getSavepointPercent(savepoint.getDrive())), true);
				} else {
					player.displayClientMessage(Component.translatable("savepoint.maxed", Utils.translateToLocal("savepoint.drive")), true);
				}
			} else if(stack.getItem() == ModItems.orichalcumplus.get()){
				if(state.getValue(TIER) != SavePointStorage.SavePointType.WARP){
					stack.shrink(1);
					BlockState newState;
					if (state.getValue(TIER) == SavePointStorage.SavePointType.NORMAL) {
						newState = state.setValue(TIER, SavePointStorage.SavePointType.LINKED);
					} else {
						newState = state.setValue(TIER, SavePointStorage.SavePointType.WARP);
					}
					worldIn.setBlockAndUpdate(pos, newState);
					player.displayClientMessage(Component.translatable("savepoint.upgrade_type", newState.getValue(TIER).getSerializedName()), true);
				} else {
					player.displayClientMessage(Component.translatable("savepoint.max_upgrade"), true);
				}
			}
		}
		return ItemInteractionResult.CONSUME;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (entity instanceof Player player && !world.isClientSide()) {
            PlayerData playerData = PlayerData.get(player);
			if (playerData != null && world.getBlockEntity(pos) instanceof SavepointTileEntity savepoint) {
				String list = switch(state.getValue(TIER)){
					case NORMAL -> ModConfigs.savePointRecovers;
					case LINKED -> ModConfigs.linkedSavePointRecovers;
					case WARP -> ModConfigs.warpPointRecovers;
				};

				if(savepoint.getHeal() == 0 || savepoint.getHunger() == 0 || savepoint.getFocus() == 0 || savepoint.getMagic() == 0 || savepoint.getDrive() == 0) {
					player.displayClientMessage(Component.translatable("ERROR, this is probably an old savepoint, break and place it again to correct it"), true);
				} else {
					if (list.contains("HP") && entity.tickCount % savepoint.getHeal() == 0 && player.getHealth() < player.getMaxHealth()) {
						player.heal(1);
						showParticles(player, world, pos);
					}
					if (list.contains("HUNGER") && entity.tickCount % savepoint.getHunger() == 0 && player.getFoodData().getFoodLevel() < 20) {
						player.getFoodData().eat(1, 1);
						showParticles(player, world, pos);
					}
					if (list.contains("MP") && entity.tickCount % savepoint.getMagic() == 0 && playerData.getMP() < playerData.getMaxMP()) {
						playerData.addMP(1);
						PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
						showParticles(player, world, pos);
					}
					if (list.contains("FOCUS") && entity.tickCount % savepoint.getFocus() == 0 && playerData.getFocus() < playerData.getMaxFocus()) {
						playerData.addFocus(1);
						PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
						showParticles(player, world, pos);
					}
					if (list.contains("DRIVE") && entity.tickCount % savepoint.getDrive() == 0 && playerData.getDP() < playerData.getMaxDP()) {
						playerData.addDP(5);
						PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
						showParticles(player, world, pos);
					}

				}
			}
		}
		super.entityInside(state, world, pos, entity);
	}

	public void showParticles(Player player, Level world, BlockPos pos){
		if (player.tickCount % 5 == 0) {
			player.playSound(ModSounds.savepoint.get(), 1F, 1F);
		}
		world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.2, pos.getY() + 2.5, pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
		world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 2.5, pos.getZ() + 0.2, 0.0D, 0.0D, 0.0D);
		world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.8, pos.getY() + 2.5, pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
		world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 2.5, pos.getZ() + 0.8, 0.0D, 0.0D, 0.0D);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return type == ModEntities.TYPE_SAVEPOINT.get() ? SavepointTileEntity::tick : null;//EntityBlock.super.getTicker(pLevel, pState, pBlockEntityType);
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return ModEntities.TYPE_SAVEPOINT.get().create(pPos, pState);
	}
}
