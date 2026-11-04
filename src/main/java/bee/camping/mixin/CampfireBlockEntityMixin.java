package bee.camping.mixin;

import bee.camping.item.CampfireRoastableItem;
import bee.camping.item.CookedLevel;
import bee.camping.networking.SetCookedLevelPacket;
import bee.camping.registry.CamperDataComponents;
import bee.camping.registry.CamperTags;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlockEntity.class)
public abstract class CampfireBlockEntityMixin {

    @Inject(method = "cookTick", at = @At("HEAD"))
    private static void cookTick(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState, CampfireBlockEntity campfireBlockEntity, RecipeManager.CachedCheck<SingleRecipeInput, CampfireCookingRecipe> cachedCheck, CallbackInfo ci) {

        for (int i = 0; i < campfireBlockEntity.getItems().size(); i++) {
            ItemStack stack = campfireBlockEntity.getItems().get(i);
            if (stack.isEmpty() || !stack.is(CamperTags.SMORES)) continue;

            CookedLevel level = stack.getOrDefault(CamperDataComponents.COOK_TIME, CookedLevel.RAW);
            int tick = campfireBlockEntity.cookingProgress[i];

            if (level == CookedLevel.BURNT || level == CookedLevel.CHARRED) continue;

            int timeToCook = switch (level) {
                case RAW -> 50; //4 //0.5
                case SLIGHTLY_COOKED -> 40; //5 //0.75
                case PERFECTLY_COOKED -> 30; //5 //1
                case ROASTED -> 30; //4 //0.5
                default -> throw new IllegalStateException("Unexpected value: " + level);
            };

            if (tick > 10 && tick % timeToCook == 0) {
                CookedLevel newLevel = CookedLevel.values()[level.ordinal() + 1];

                stack.set(DataComponents.ITEM_NAME, newLevel.translation.copy().append(stack.getItem().getName()));
                stack.set(CamperDataComponents.COOK_TIME, newLevel);
                stack.set(DataComponents.FOOD, new FoodProperties(newLevel.nutrition + 2, newLevel.saturation + 0.5f, true));

                campfireBlockEntity.getItems().set(i, stack);
                campfireBlockEntity.setChanged();
                serverLevel.sendBlockUpdated(blockPos, blockState, blockState, 3);
                serverLevel.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(null, blockState));
            }

        }

    }

}
