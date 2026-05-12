package bee.mellow.custom;

import bee.mellow.registry.ModItemComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.predicates.DataComponentPredicate;

public record CookedLevelPredicate(int level) implements DataComponentPredicate {
    public static final Codec<CookedLevelPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
     Codec.INT.fieldOf("level").forGetter(CookedLevelPredicate::level)
    ).apply(instance, CookedLevelPredicate::new)
    );

    @Override
    public boolean matches(DataComponentGetter components) {
        Integer cookedLevel = components.get(ModItemComponents.COOKED_LEVEL);

        if (cookedLevel == null) return false;

        return cookedLevel == level;
    }
}
