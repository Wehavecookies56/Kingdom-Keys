package online.kingdomkeys.kingdomkeys.limit;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

public abstract class Limit extends ForgeRegistryEntry<Limit> {

	String name;
	int order;
	int[] levels;

	String translationKey;

	OrgMember owner;

	public Limit(ResourceLocation registryName, int order, int[] levels, OrgMember owner) {
		this.name = registryName.toString();
		this.levels = levels;
		setRegistryName(registryName);
		this.order = order;
		this.owner = owner;
		translationKey = "limit." + getRegistryName().getPath() + ".name";
	}

	public Limit(String registryName, int order, int[] levels, OrgMember owner) {
		this(new ResourceLocation(registryName), order, levels, owner);
	}

	public String getName() {
		return name;
	}

	public String getTranslationKey() {
		return translationKey;
	}

	public List<Integer> getLevels() {
		List<Integer> list = new ArrayList<Integer>();
		for (int l : levels) {
			list.add(l);
		}
		return list;
	}

	public int getOrder() {
		return order;
	}

	public OrgMember getOwner() {
		return owner;
	}

	public abstract void onUse(PlayerEntity player, LivingEntity target, int size);

}