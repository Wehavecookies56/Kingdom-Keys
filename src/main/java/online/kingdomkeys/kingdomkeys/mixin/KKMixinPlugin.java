package online.kingdomkeys.kingdomkeys.mixin;

import com.google.common.collect.ImmutableMap;
import net.minecraftforge.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class KKMixinPlugin implements IMixinConfigPlugin {

    private static final Supplier<Boolean> EFMLOADED = () -> LoadingModList.get().getModFileById("epicfight") != null;
    private static final Supplier<Boolean> APOTHEOSISLOADED = () -> LoadingModList.get().getModFileById("apotheosis") != null;
    private static final Supplier<Boolean> JERLOADED = () -> LoadingModList.get().getModFileById("jeresources") != null;

    private static final Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.of(
            "online.kingdomkeys.kingdomkeys.mixin.epicfight.PHumanoidRendererMixin", EFMLOADED,
            "online.kingdomkeys.kingdomkeys.mixin.epicfight.RenderEngineEventsMixin", EFMLOADED,
            "online.kingdomkeys.kingdomkeys.mixin.apotheosis.SalvaginMenuMixin", APOTHEOSISLOADED,
            "online.kingdomkeys.kingdomkeys.mixin.jer.ForgePlatformHelperMixin", JERLOADED,
            "online.kingdomkeys.kingdomkeys.mixin.jer.RegistrySetBuilderMixin", JERLOADED
    );

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return CONDITIONS.getOrDefault(mixinClassName, () -> true).get();
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
