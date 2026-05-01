package bee.mellow.custom;

import bee.mellow.registry.ModItemComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CampfireRoastable extends Item {
    public CampfireRoastable(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResult.PASS;
    }

    @Override
    public boolean allowComponentsUpdateAnimation(Player player, InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining) {
        BlockState block = level.getBlockState(BlockPos.containing(livingEntity.pick(5, 1 , false).getLocation()));
        if (block.is(BlockTags.CAMPFIRES)) {
            int tick = 72000 - ticksRemaining;
            if (tick >= 10 && tick < 50 && itemStack.get(ModItemComponents.COOKED_LEVEL) == 0) itemStack.set(ModItemComponents.COOKED_LEVEL, 1);
            else if (tick >= 60 && tick < 80 && itemStack.get(ModItemComponents.COOKED_LEVEL) == 1) itemStack.set(ModItemComponents.COOKED_LEVEL, 2);
            else if (tick >= 80 && tick < 110 && itemStack.get(ModItemComponents.COOKED_LEVEL) == 2) itemStack.set(ModItemComponents.COOKED_LEVEL, 3);
            else if (tick >= 110 && tick < 200 && itemStack.get(ModItemComponents.COOKED_LEVEL) == 3) itemStack.set(ModItemComponents.COOKED_LEVEL, 4);
            else if (tick >= 200 && itemStack.get(ModItemComponents.COOKED_LEVEL) == 4) livingEntity.setItemInHand(livingEntity.getUsedItemHand(), Items.STICK.getDefaultInstance());

        }
        super.onUseTick(level, livingEntity, itemStack, ticksRemaining);
    }
}
