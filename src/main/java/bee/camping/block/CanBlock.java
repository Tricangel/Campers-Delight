package bee.camping.block;

import bee.camping.item.CookedLevel;
import bee.camping.registry.CamperDataComponents;
import bee.camping.registry.CamperItems;
import bee.camping.registry.CamperTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Optional;

public class CanBlock extends Block {
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 9);
    public static final EnumProperty<CanFilling> FILLING = EnumProperty.create("filling", CanFilling.class);
    public CanBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
        builder.add(FILLING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        ItemStack stack = player.getItemInHand(hand);
        int contentsLevel = state.getValue(LEVEL);
        if (contentsLevel == 0) {
            for (CanFilling filling : CanFilling.values()) {
                if ((filling.contents.isPresent() &&  stack.is(filling.contents.get()))
                || (filling.contentsTag.isPresent() &&  stack.is(filling.contentsTag.get()))) {
                    level.setBlockAndUpdate(pos, state.setValue(FILLING, filling).setValue(LEVEL, 9));
                    return InteractionResult.SUCCESS;
                }
            }
        }

        if (contentsLevel == 9) {
            level.setBlockAndUpdate(pos, state.setValue(LEVEL, 8));
            return InteractionResult.SUCCESS;
        }

        if (stack.getOrDefault(CamperDataComponents.COOK_TIME, CookedLevel.RAW) == CookedLevel.RAW) {
            if (contentsLevel > 0) {
                CanFilling filling = state.getValue(FILLING);
                if (stack.is(filling.input)) {
                    stack.shrink(1);
                    if (level instanceof ServerLevel serverLevel) serverLevel.playSound(null, pos, SoundEvents.WOOD_HIT, SoundSource.BLOCKS);
                    player.addItem(new ItemStack(filling.result));
                    level.setBlockAndUpdate(pos, state.setValue(LEVEL, contentsLevel - 1));
                    return InteractionResult.SUCCESS;
                }
            }

        }


        return InteractionResult.FAIL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.create(0.25, 0, 0.25, 0.75, 0.625, 0.75);
    }

    @Override
    protected boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) {
        return true;
    }
    
    
    public enum CanFilling implements StringRepresentable {
        MARSHMALLOW("marshmallow", CamperItems.BAG_OF_MARSHMALLOWS, Items.STICK, CamperItems.MARSHMALLOW_STICK),
        CHOCOLATE("chocolate", CamperTags.CHOCOLATE, CamperItems.MARSHMALLOW_STICK, CamperItems.CHOCOLATE_COATED_MARSHMALLOW_STICK);

        final String name;
        final Optional<Item> contents;
        final Optional<TagKey<Item>> contentsTag;
        final Item input;
        final Item result;
        CanFilling(String name, Item contents, Item input, Item result) {
            this.name = name;
            this.input = input;
            this.result = result;
            this.contents = Optional.of(contents);
            this.contentsTag = Optional.empty();
        }

        CanFilling(String name, TagKey<Item> tagKey, Item input, Item result) {
            this.name = name;
            this.input = input;
            this.result = result;
            this.contents = Optional.empty();
            this.contentsTag = Optional.of(tagKey);
        }
        
        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
