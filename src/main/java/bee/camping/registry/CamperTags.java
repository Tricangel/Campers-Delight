package bee.camping.registry;

import bee.camping.CampersDelight;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CamperTags {

    public static final TagKey<Item> CHOCOLATE = TagKey.create(Registries.ITEM, CampersDelight.id("chocolate"));
    public static final TagKey<Item> SMORES = TagKey.create(Registries.ITEM, CampersDelight.id("smores"));

    public void init() {}

}
