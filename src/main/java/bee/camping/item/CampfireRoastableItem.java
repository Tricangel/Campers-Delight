package bee.camping.item;

import net.minecraft.world.item.Item;

public class CampfireRoastableItem extends Item {
    final Item burntItem;
    final int bonus;
    public CampfireRoastableItem(Item burntItem, Properties properties) {
        super(properties);
        this.bonus = 0;
        this.burntItem = burntItem;
    }

    public CampfireRoastableItem(Item burntItem, int bonus, Properties properties) {
        super(properties);
        this.bonus =bonus;
        this.burntItem = burntItem;
    }

    public int getBonus() {
        return bonus;
    }

    public Item getBurntItem() {
        return burntItem;
    }
}
