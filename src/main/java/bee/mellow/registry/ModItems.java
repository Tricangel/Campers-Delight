package bee.mellow.registry;

import bee.mellow.Marshmellowed;
import bee.mellow.custom.CampfireRoastable;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class ModItems {

    public static final Item MARSHMELLOW_STICK = register("marshmellow_stick", CampfireRoastable::new, new Item.Properties()
            .component(ModItemComponents.COOKED_LEVEL, 0).stacksTo(1));

    public static final Item CHOCOLATE_COATED_MARSHMELLOW_STICK = register("chocolate_coated_marshmellow_stick", CampfireRoastable::new, new Item.Properties()
            .component(ModItemComponents.COOKED_LEVEL, 0).stacksTo(1));

    public static final Item SMORE = register("smore", Item::new, new Item.Properties()
            .component(ModItemComponents.COOKED_LEVEL, 0));

    public static final Item MARSHMELLOW = register("marshmellow", Item::new, new Item.Properties());

    public static final Item GRAHAM_CRACKER = register("graham_cracker", Item::new, new Item.Properties());
    public static final Item CHOCOLATE = register("chocolate", Item::new, new Item.Properties());

    public static final Item TONGS = register("tongs", Item::new, new Item.Properties().stacksTo(1));


    public static void init() {
    }

    public static <GenericItem extends Item> GenericItem register(String name, Function<Item.Properties, GenericItem> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Marshmellowed.MOD_ID, name));
        GenericItem item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

}
