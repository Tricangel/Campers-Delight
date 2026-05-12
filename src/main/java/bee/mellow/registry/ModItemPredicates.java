package bee.mellow.registry;

import bee.mellow.Marshmellowed;
import bee.mellow.custom.CookedLevelPredicate;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.predicates.DamagePredicate;
import net.minecraft.core.component.predicates.DataComponentPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;


public class ModItemPredicates {

    public static final DataComponentPredicate.Type<CookedLevelPredicate> COOKED_LEVEL = register("cooked_level", CookedLevelPredicate.CODEC);

    private static <T extends DataComponentPredicate> DataComponentPredicate.Type<T> register(final String id, final Codec<T> codec) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_PREDICATE_TYPE, Identifier.fromNamespaceAndPath(Marshmellowed.MOD_ID, id), new DataComponentPredicate.ConcreteType<T>(codec));
    }


    public static void init() {}

}
