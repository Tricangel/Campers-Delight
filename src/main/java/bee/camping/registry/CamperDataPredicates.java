package bee.camping.registry;

import bee.camping.CampersDelight;
import bee.camping.item.CookedLevelPredicate;
import net.minecraft.core.Registry;
import net.minecraft.core.component.predicates.DataComponentPredicate;
import net.minecraft.core.registries.BuiltInRegistries;

public class CamperDataPredicates {

    public static final DataComponentPredicate.Type<CookedLevelPredicate> COOKED_LEVEL = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_PREDICATE_TYPE,
            CampersDelight.id("cooked_level"),
            new DataComponentPredicate.ConcreteType<>(CookedLevelPredicate.CODEC)
    );

    public static void init() {}

}
