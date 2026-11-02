package bee.camping.registry;

import bee.camping.CampersDelight;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

public class CamperDataComponents {

    public static final DataComponentType<Integer> COOK_TIME = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            CampersDelight.id("cook_time"),
            DataComponentType.<Integer>builder().ignoreSwapAnimation().persistent(Codec.INT).build()
    );

    public static void init() {}
}
