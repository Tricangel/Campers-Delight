package bee.mellow.custom;

import bee.mellow.registry.ModItemComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;

public class CampfirePlaceable extends Item {
    public CampfirePlaceable(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
            player.startUsingItem(hand);
            return InteractionResult.PASS;
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity user) {
        return 60;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
        return ItemUseAnimation.EAT;

    }

    @Override
    public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime) {
        float saturation = 1;
        int nutrition = 2;

        if (itemStack.get(ModItemComponents.COOKED_LEVEL) != null) {
            if (itemStack.get(ModItemComponents.COOKED_LEVEL) < 3) {
                saturation = itemStack.get(ModItemComponents.COOKED_LEVEL);
                nutrition = itemStack.get(ModItemComponents.COOKED_LEVEL);
            } else {
                nutrition = 5 - itemStack.get(ModItemComponents.COOKED_LEVEL);
                saturation = 5 - itemStack.get(ModItemComponents.COOKED_LEVEL);
            }

        }

        entity.getActiveItem().set(DataComponents.FOOD, new FoodProperties(nutrition, saturation, true));

        return super.releaseUsing(itemStack, level, entity, remainingTime);
    }


}
