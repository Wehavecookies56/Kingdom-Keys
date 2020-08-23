package online.kingdomkeys.kingdomkeys.item;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.datagen.init.KeybladeStats.Recipe;
import online.kingdomkeys.kingdomkeys.lib.Lists;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeLevel;

public class MagicSpellItem extends Item {
	String magic;

	public MagicSpellItem(Properties properties, String name) {
		super(properties);
		this.magic = name;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		if (!world.isRemote) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);

			/*File f = new File("output.txt");
			try {
				FileWriter fw = new FileWriter(f,false);

				for (int i = 0; i < Lists.keyblades.size(); i++) {
					KeybladeItem keyblade = (KeybladeItem) Lists.keyblades.get(i).get();
					String name = snakeToCamel(keyblade.getRegistryName().getPath());
					int baseStr = keyblade.getStrength(0);
					int baseMag = keyblade.getMagic(0);
						fw.append("\n" + "\n" +
								"			getBuilder(Strings."+name+").keychain(Strings."+name+"Chain).baseStats("+baseStr+","+baseMag+")\r\n" + 
								"                .abilities(\"Treasure Magnet\", \"Formchange Extender\")\r\n" + 
								"                .level( new KeybladeLevel.KeybladeLevelBuilder().withStats("+baseStr+","+(baseMag+1)+").withMaterials(new Recipe()\r\n" + 
								"                        .addMaterial(Strings.SM_Fluorite, 1).addMaterial(Strings.SM_WellspringShard, 2)).build())\r\n" + 
								"                .level( new KeybladeLevel.KeybladeLevelBuilder().withStats("+(baseStr+1)+","+(baseMag+1)+").withMaterials(new Recipe()\r\n" + 
								"                        .addMaterial(Strings.SM_Fluorite, 1).addMaterial(Strings.SM_WellspringShard, 3)).build())\r\n" + 
								"                .level( new KeybladeLevel.KeybladeLevelBuilder().withStats("+(baseStr+1)+","+(baseMag+2)+").withMaterials(new Recipe()\r\n" + 
								"                        .addMaterial(Strings.SM_Fluorite, 1).addMaterial(Strings.SM_WellspringShard, 4)).build())\r\n" + 
								"                .level( new KeybladeLevel.KeybladeLevelBuilder().withStats("+(baseStr+2)+","+(baseMag+2)+").withMaterials(new Recipe()\r\n" + 
								"                        .addMaterial(Strings.SM_Damascus, 1).addMaterial(Strings.SM_WellspringStone, 1)).build())\r\n" + 
								"                .level( new KeybladeLevel.KeybladeLevelBuilder().withStats("+(baseStr+2)+","+(baseMag+3)+").withMaterials(new Recipe()\r\n" + 
								"                        .addMaterial(Strings.SM_Damascus, 1).addMaterial(Strings.SM_WellspringStone, 2)\r\n" + 
								"                        .addMaterial(Strings.SM_WrithingStone, 2)).build())\r\n" + 
								"                .level( new KeybladeLevel.KeybladeLevelBuilder().withStats("+(baseStr+3)+","+(baseMag+3)+").withMaterials(new Recipe()\r\n" + 
								"                        .addMaterial(Strings.SM_Damascus, 1).addMaterial(Strings.SM_WellspringStone, 3)\r\n" + 
								"                        .addMaterial(Strings.SM_WrithingStone, 2)).build())\r\n" + 
								"                .level( new KeybladeLevel.KeybladeLevelBuilder().withStats("+(baseStr+3)+","+(baseMag+4)+").withMaterials(new Recipe()\r\n" + 
								"                        .addMaterial(Strings.SM_Adamantite, 1).addMaterial(Strings.SM_WellspringGem, 1)\r\n" + 
								"                        .addMaterial(Strings.SM_WrithingGem, 1)).build())\r\n" + 
								"                .level( new KeybladeLevel.KeybladeLevelBuilder().withStats("+(baseStr+4)+","+(baseMag+4)+").withMaterials(new Recipe()\r\n" + 
								"                        .addMaterial(Strings.SM_Adamantite, 1).addMaterial(Strings.SM_WellspringGem, 2)\r\n" + 
								"                        .addMaterial(Strings.SM_WrithingStone, 2)).build())\r\n" + 
								"                .level( new KeybladeLevel.KeybladeLevelBuilder().withStats("+(baseStr+5)+","+(baseMag+4)+").withMaterials(new Recipe()\r\n" + 
								"                        .addMaterial(Strings.SM_Adamantite, 1).addMaterial(Strings.SM_WellspringGem, 3)\r\n" + 
								"                        .addMaterial(Strings.SM_WrithingStone, 2)).build())\r\n" + 
								"                .level( new KeybladeLevel.KeybladeLevelBuilder().withStats("+(baseStr+5)+","+(baseMag+5)+").withMaterials(new Recipe()\r\n" + 
								"                        .addMaterial(Strings.SM_Electrum, 1).addMaterial(Strings.SM_WellspringCrystal, 1)\r\n" + 
								"                        .addMaterial(Strings.SM_WrithingCrystal, 1)).build())\r\n" + 
								"                .desc("+"\""+keyblade.getDescription()+"\""+");");
						
				}
				fw.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

			if (playerData != null && playerData.getMagicList() != null) {
				if (!playerData.getMagicList().contains(magic)) {
					playerData.addMagicToList(magic);
					if (!ItemStack.areItemStacksEqual(player.getHeldItemMainhand(), ItemStack.EMPTY) && player.getHeldItemMainhand().getItem() == this) {
						player.getHeldItemMainhand().shrink(1);
					} else if (!ItemStack.areItemStacksEqual(player.getHeldItemOffhand(), ItemStack.EMPTY) && player.getHeldItemOffhand().getItem() == this) {
						player.getHeldItemOffhand().shrink(1);
					}
					player.sendMessage(new TranslationTextComponent("Unlocked " + magic.substring(magic.indexOf(":") + 1)));
				} else {
					player.sendMessage(new TranslationTextComponent(magic.substring(magic.indexOf(":") + 1) + " Already unlocked"));
				}
				PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
			}
		}
		return ActionResult.resultSuccess(player.getHeldItem(hand));
	}

	public static String snakeToCamel(String str) {
		// Capitalize first letter of string
		str = str.substring(0, 1).toUpperCase() + str.substring(1);

		// Run a loop till string
		// string contains underscore
		while (str.contains("_")) {

			// Replace the first occurrence
			// of letter that present after
			// the underscore, to capitalize
			// form of next letter of underscore
			str = str.replaceFirst("_[a-z]", String.valueOf(Character.toUpperCase(str.charAt(str.indexOf("_") + 1))));
		}
		str = str.substring(0,1).toLowerCase()+str.substring(1);
		// Return string
		return str;
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("Unlock " + magic));
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
