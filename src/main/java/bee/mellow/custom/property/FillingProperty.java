package bee.mellow.custom.property;

import net.minecraft.util.StringRepresentable;

public enum FillingProperty implements StringRepresentable {
    MARSHMALLOW("marshmallow"),
    CHOCOLATE("chocolate");

    final String name;

    FillingProperty(String name) {
        this.name = name;
    }


    @Override
    public String getSerializedName() {
        return name;
    }
}
