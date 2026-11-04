package bee.camping.item;

import bee.camping.registry.CamperDataComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.predicates.DataComponentPredicate;

public record CookedLevelPredicate(int level) implements DataComponentPredicate {
    public static final Codec<CookedLevelPredicate> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.fieldOf("level").forGetter(CookedLevelPredicate::level)
    ).apply(i, CookedLevelPredicate::new));


    @Override
    public boolean matches(DataComponentGetter dataComponentGetter) {
        return dataComponentGetter.getOrDefault(CamperDataComponents.COOK_TIME, CookedLevel.RAW).ordinal() == level;
    }
}
