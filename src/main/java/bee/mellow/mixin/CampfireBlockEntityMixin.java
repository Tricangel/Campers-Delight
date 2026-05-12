package bee.mellow.mixin;

import bee.mellow.custom.CampfirePlaceable;
import bee.mellow.registry.ModItemComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
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

	@Inject(at = @At("HEAD"), method = "cookTick")
	private static void init(ServerLevel level, BlockPos pos, BlockState state, CampfireBlockEntity entity, RecipeManager.CachedCheck<SingleRecipeInput, CampfireCookingRecipe> recipeCache, CallbackInfo ci) {

		for (int i = 0; i < entity.getItems().size(); i++){
			ItemStack itemStack = entity.getItems().get(i);
			if (!itemStack.isEmpty()) {
				if (itemStack.getItem() instanceof CampfirePlaceable) {
					int tick = entity.cookingProgress[i];
					if (tick >= 10 && tick < 50 && (itemStack.get(ModItemComponents.COOKED_LEVEL) == null) || itemStack.get(ModItemComponents.COOKED_LEVEL) == 0) itemStack.set(ModItemComponents.COOKED_LEVEL, 1);
					else if (tick >= 60 && tick < 80 && itemStack.get(ModItemComponents.COOKED_LEVEL) == 1) itemStack.set(ModItemComponents.COOKED_LEVEL, 2);
					else if (tick >= 80 && tick < 110 && itemStack.get(ModItemComponents.COOKED_LEVEL) == 2) itemStack.set(ModItemComponents.COOKED_LEVEL, 3);
					else if (tick >= 110 && tick < 200 && itemStack.get(ModItemComponents.COOKED_LEVEL) == 3) itemStack.set(ModItemComponents.COOKED_LEVEL, 4);
					else if (tick >= 200 && itemStack.get(ModItemComponents.COOKED_LEVEL) == 4) itemStack = Items.STICK.getDefaultInstance();
				entity.items.set(i, itemStack);
				level.sendBlockUpdated(pos, state, state, 3);
				level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
				entity.setChanged();
				}
			}
		}


	}
}