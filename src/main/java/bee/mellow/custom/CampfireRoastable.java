package bee.mellow.custom;

import bee.mellow.Marshmellowed;
import bee.mellow.registry.ModItemComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Random;

public class CampfireRoastable extends Item {
    public CampfireRoastable(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        BlockState block = level.getBlockState(BlockPos.containing(player.pick(5, 1 , false).getLocation()));
        if (block.is(BlockTags.CAMPFIRES)) {
            player.getItemInHand(hand).set(ModItemComponents.CAN_EAT, false);
        }
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
        return itemStack.getOrDefault(ModItemComponents.CAN_EAT, false) ? ItemUseAnimation.EAT : ItemUseAnimation.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity user) {
        return itemStack.getOrDefault(ModItemComponents.CAN_EAT, false) ? 40 : 72000;
    }

    @Override
    public boolean allowComponentsUpdateAnimation(Player player, InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining) {
        BlockState block = level.getBlockState(BlockPos.containing(livingEntity.pick(5, 1 , false).getLocation()));
        if (block.is(BlockTags.CAMPFIRES)) {
            itemStack.set(ModItemComponents.CAN_EAT, false);
            int tick = getUseDuration(itemStack, livingEntity) - ticksRemaining;
            if (tick >= 10 && tick < 50 && itemStack.get(ModItemComponents.COOKED_LEVEL) == 0) itemStack.set(ModItemComponents.COOKED_LEVEL, 1);
            else if (tick >= 60 && tick < 80 && itemStack.get(ModItemComponents.COOKED_LEVEL) == 1) itemStack.set(ModItemComponents.COOKED_LEVEL, 2);
            else if (tick >= 80 && tick < 110 && itemStack.get(ModItemComponents.COOKED_LEVEL) == 2) itemStack.set(ModItemComponents.COOKED_LEVEL, 3);
            else if (tick >= 110 && tick < 200 && itemStack.get(ModItemComponents.COOKED_LEVEL) == 3) itemStack.set(ModItemComponents.COOKED_LEVEL, 4);
            else if (tick >= 200 && itemStack.get(ModItemComponents.COOKED_LEVEL) == 4) livingEntity.setItemInHand(livingEntity.getUsedItemHand(), Items.STICK.getDefaultInstance());
            float saturation;
            int nutrition;

            int cookedTime = itemStack.getOrDefault(ModItemComponents.COOKED_LEVEL, 0);
            if (cookedTime < 3) {
                saturation = cookedTime;
                nutrition = cookedTime;
            } else {
                saturation = 5 -cookedTime;
                nutrition = 5 -cookedTime;
            }
            itemStack.set(DataComponents.FOOD, new FoodProperties(nutrition, saturation, true));
            itemStack.set(DataComponents.CONSUMABLE, new Consumable(35, ItemUseAnimation.EAT, SoundEvents.GENERIC_EAT, true, List.of()));
        } else if (itemStack.get(DataComponents.CONSUMABLE) != null){
            itemStack.set(ModItemComponents.CAN_EAT, true);
            Consumable consumable = itemStack.get(DataComponents.CONSUMABLE);
            if (consumable.shouldEmitParticlesAndSounds(ticksRemaining)) {
                consumable.emitParticlesAndSounds(RandomSource.create(), livingEntity, itemStack, 10);
            }
        }
    super.onUseTick(level, livingEntity, itemStack, ticksRemaining);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        if (itemStack.get(DataComponents.CONSUMABLE) != null) {
            if (itemStack.getOrDefault(ModItemComponents.CAN_EAT, true)) {
                Consumable consumable = itemStack.get(DataComponents.CONSUMABLE);
                consumable.onConsume(level, entity, itemStack);
            }
        }
        return super.finishUsingItem(itemStack, level, entity);
    }
}
