package bee.mellow.registry;

import bee.mellow.Marshmellowed;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class ModItemComponents {

    public static final DataComponentType<Integer> COOKED_LEVEL = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(Marshmellowed.MOD_ID, "cooked_level"),
            DataComponentType.<Integer>builder().persistent(Codec.INT).build()
    );
    public static void init() {}
}
