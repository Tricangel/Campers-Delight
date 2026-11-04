package bee.camping.registry;

import bee.camping.CampersDelight;
import bee.camping.item.CampfireRoastableItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.Function;

public class CamperItems {

    public static final Item BAG_OF_MARSHMALLOWS = register("bag_of_marshmallows", Item::new, new Item.Properties().food(new FoodProperties(3, 0.75f, false)));

    public static final Item MARSHMALLOW_STICK = register("marshmallow_stick", p -> new CampfireRoastableItem(Items.STICK, p), new Item.Properties().stacksTo(1).usingConvertsTo(Items.STICK).food(new FoodProperties(4, 0.5f, true)));
    public static final Item CHOCOLATE_COATED_MARSHMALLOW_STICK = register("chocolate_coated_marshmallow_stick", p -> new CampfireRoastableItem(Items.STICK, 1, p), new Item.Properties().stacksTo(1).usingConvertsTo(Items.STICK).food(new FoodProperties(5, 0.5f, true)));

    public static final Item SMORE = register("smore", Item::new, new Item.Properties().food(new FoodProperties(6, 0.75f, false)));

    public static final CreativeModeTab CAMPER_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CampersDelight.id("item_group"),
            FabricItemGroup.builder()
                    .icon(BAG_OF_MARSHMALLOWS::getDefaultInstance)
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(BAG_OF_MARSHMALLOWS);
                        output.accept(MARSHMALLOW_STICK);
                        output.accept(CHOCOLATE_COATED_MARSHMALLOW_STICK);
                        output.accept(SMORE);
                        output.accept(CamperBlocks.CAN);

                    })
                    .title(Component.translatable("itemgroup.campers-delight"))
                    .build());
    public static void init() {
    }

    public static <GenericItem extends Item> GenericItem register(String name, Function<Item.Properties, GenericItem> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, CampersDelight.id(name));
        GenericItem item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

}
