package bee.camping.item;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

public enum CookedLevel implements StringRepresentable {
    RAW("raw", Component.translatable("cooked_level.campers-delight.raw"), 5, 1.4f),
    SLIGHTLY_COOKED("slightly_cooked", Component.translatable("cooked_level.campers-delight.slightly_cooked"), 6, 1.6f),
    PERFECTLY_COOKED("perfectly_cooked", Component.translatable("cooked_level.campers-delight.perfectly_cooked"), 8, 2.3f),
    ROASTED("roasted", Component.translatable("cooked_level.campers-delight.roasted"), 5, 1.7f),
    BURNT("burnt", Component.translatable("cooked_level.campers-delight.burnt"), 1, -1.5f),
    CHARRED("charred", Component.empty(), -10, -10f)
    ;

    CookedLevel(String name, Component translation, int nutrition, float saturation) {
        this.name = name;
        this.translation = translation;
        this.nutrition = nutrition;
        this.saturation = saturation;
    }

    public static final EnumCodec<CookedLevel> CODEC = StringRepresentable.fromEnum(CookedLevel::values);
    public static final StreamCodec<RegistryFriendlyByteBuf, CookedLevel> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, CookedLevel::getSerializedName,
            CookedLevel::valueOf
    );
    final String name;
    public final Component translation;
    public final int nutrition;
    public final float saturation;

    @Override
    public String getSerializedName() {
        return name;
    }
}
