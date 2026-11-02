package bee.camping.item;

import net.minecraft.world.item.Item;

public class CampfireRoastableItem extends Item {
    final Item burntItem;
    public CampfireRoastableItem(Item burntItem, Properties properties) {
        super(properties);
        this.burntItem = burntItem;
    }

    public Item getBurntItem() {
        return burntItem;
    }
}
