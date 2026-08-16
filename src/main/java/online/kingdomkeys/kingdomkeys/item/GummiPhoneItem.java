package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class GummiPhoneItem extends Item implements IItemCategory, ICreativeTab {
	public GummiPhoneItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		Player player = context.getPlayer();
		InteractionHand hand = context.getHand();
		if (!world.isClientSide) {
			ItemStack stack = player.getItemInHand(hand);
			GummiStructure gummiStruct;
            float damage = 0;
            int fuel = 0;
			if(stack.has(ModComponents.GUMMI_STRUCTURE)){
				gummiStruct = stack.get(ModComponents.GUMMI_STRUCTURE);
			} else {
                return InteractionResult.FAIL;
            }
            if(stack.has(ModComponents.GUMMI_DAMAGE)) {
                damage = stack.get(ModComponents.GUMMI_DAMAGE);
            }
            if(stack.has(ModComponents.GUMMI_FUEL)) {
                fuel = stack.get(ModComponents.GUMMI_FUEL);
            }

            if (gummiStruct != null || gummiStruct.getBlocks().length > 0) {
                Vec3i size = Utils.getRealGummiStructureSize(gummiStruct);
                ((ServerLevel) world).sendParticles(ParticleTypes.FIREWORK, context.getClickedPos().getX() + 0.5F, context.getClickedPos().getY()+1, context.getClickedPos().getZ()+ 0.5F, size.getX() * size.getY() * size.getZ()*10, 0, 0, 0, 0.2);
                GummiShipEntity gummi = new GummiShipEntity(world, gummiStruct);
				gummiStruct.serializeNBT(world.registryAccess());
                gummi.setPos(context.getClickedPos().getX() + 0.5F, context.getClickedPos().getY()+1, context.getClickedPos().getZ()+ 0.5F);
                gummi.setYRot(player.getYRot());
                if(damage > 0) {
                    gummi.setDamage(damage);
                    stack.remove(ModComponents.GUMMI_DAMAGE);
                }
                if(fuel > 0) {
                    gummi.setFuel(fuel);
                    stack.remove(ModComponents.GUMMI_FUEL);
                }
                world.addFreshEntity(gummi);
                stack.remove(ModComponents.GUMMI_STRUCTURE);
			}
		}
		return InteractionResult.SUCCESS;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext pContext, List<Component> tooltip, TooltipFlag pTooltipFlag) {
		if (stack.has(ModComponents.GUMMI_STRUCTURE)) {
			GummiStructure structure = stack.get(ModComponents.GUMMI_STRUCTURE);
			tooltip.add(Component.translatable("kingdomkeys.gummi.phone.call_ship").withStyle(ChatFormatting.GRAY).append(ChatFormatting.RED+structure.getName()));
            tooltip.add(Component.translatable("kingdomkeys.gummi.phone.health").withStyle(ChatFormatting.GRAY).append(ChatFormatting.GRAY+""+(Utils.getShipStats(structure).armour() - stack.get(ModComponents.GUMMI_DAMAGE).intValue()+"/"+Utils.getShipStats(structure).armour())));
            tooltip.add(Component.translatable("kingdomkeys.gummi.phone.fuel").withStyle(ChatFormatting.GRAY).append(ChatFormatting.GRAY+""+stack.get(ModComponents.GUMMI_FUEL)));
		} else {
			tooltip.add(Component.translatable("kingdomkeys.gummi.phone.no_ship").withStyle(ChatFormatting.GRAY));
			tooltip.add(Component.translatable("kingdomkeys.gummi.phone.store_hint").withStyle(ChatFormatting.GRAY));
		}
		super.appendHoverText(stack, pContext, tooltip, pTooltipFlag);
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.MISC;
	}

	@Override
	public ICreativeTab.Tab getTab() {
		return Tab.GUMMI;
	}
}
