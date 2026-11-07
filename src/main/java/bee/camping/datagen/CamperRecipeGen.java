package bee.camping.datagen;

import bee.camping.registry.CamperBlocks;
import bee.camping.registry.CamperItems;
import bee.camping.registry.CamperTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class CamperRecipeGen extends FabricRecipeProvider {
    public CamperRecipeGen(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
        return new RecipeProvider(provider, recipeOutput) {
            @Override
            public void buildRecipes() {
                shapeless(RecipeCategory.FOOD, CamperItems.BAG_OF_MARSHMALLOWS)
                        .requires(Items.SUGAR, 2)
                        .requires(Items.PAPER, 2)
                        .requires(Items.SWEET_BERRIES)
                        .unlockedBy(getHasName(Items.SWEET_BERRIES), has(Items.SWEET_BERRIES))
                        .save(output);

                shapeless(RecipeCategory.FOOD, CamperItems.GRAHAM_CRACKER)
                        .requires(Items.WHEAT, 2)
                        .requires(Items.SUGAR, 2)
                        .unlockedBy(getHasName(Items.WHEAT), has(Items.WHEAT))
                        .save(output);

                shapeless(RecipeCategory.FOOD, CamperItems.SMORE)
                        .requires(CamperItems.BAG_OF_MARSHMALLOWS)
                        .requires(CamperTags.CHOCOLATE)
                        .requires(CamperItems.GRAHAM_CRACKER)
                        .unlockedBy(getHasName(CamperItems.BAG_OF_MARSHMALLOWS), has(CamperItems.BAG_OF_MARSHMALLOWS))
                        .save(output);

                shaped(RecipeCategory.MISC, CamperBlocks.CAN)
                        .pattern("i")
                        .pattern("r")
                        .pattern("i")
                        .define('i', Items.IRON_INGOT)
                        .define('r', Items.RED_DYE)
                        .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                        .save(output);
            }
        };
    }

    @Override
    public String getName() {
        return "";
    }
}
