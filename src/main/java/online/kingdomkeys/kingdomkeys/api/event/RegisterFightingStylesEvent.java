package online.kingdomkeys.kingdomkeys.api.event;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;
import online.kingdomkeys.kingdomkeys.integration.epicfight.style.KKFightingStyle;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class RegisterFightingStylesEvent extends Event {
    private final Map<ResourceLocation, KKFightingStyle> styles;

    public RegisterFightingStylesEvent(Map<ResourceLocation, KKFightingStyle> styles) {
        this.styles = styles;
    }

    public void register(KKFightingStyle style) {
        styles.put(style.getId(), style);
    }

    public KKFightingStyle get(ResourceLocation id) {
        return styles.get(id);
    }

    public Collection<KKFightingStyle> getStyles() {
        return Collections.unmodifiableCollection(styles.values());
    }
}
