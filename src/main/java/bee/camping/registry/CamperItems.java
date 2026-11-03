package bee.camping.registry;

import bee.camping.CampersDelight;
import bee.camping.item.CampfireRoastableItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.Function;

public class CamperItems {

    public static final Item MARSHMALLOW = register("marshmallow", Item::new, new Item.Properties().food(new FoodProperties(1, 2, false)));
    public static final Item CHOCOLATE = register("chocolate", Item::new, new Item.Properties().food(new FoodProperties(1, 2, false)));

    public static final Item MARSHMALLOW_STICK = register("marshmallow_stick", p -> new CampfireRoastableItem(Items.STICK, p), new Item.Properties().food(new FoodProperties(1, 2, true)));
    public static final Item CHOCOLATE_COATED_MARSHMALLOW_STICK = register("chocolate_coated_marshmallow_stick", p -> new CampfireRoastableItem(Items.STICK, p), new Item.Properties().food(new FoodProperties(1, 2, true)));


    public static void init() {
    }

    public static <GenericItem extends Item> GenericItem register(String name, Function<Item.Properties, GenericItem> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, CampersDelight.id(name));
        GenericItem item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

}
