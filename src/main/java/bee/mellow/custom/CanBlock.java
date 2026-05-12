package bee.mellow.custom;

import bee.mellow.custom.property.FillingProperty;
import bee.mellow.registry.ModItemComponents;
import bee.mellow.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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

public class CanBlock extends Block {
    public static final IntegerProperty FILLEDLEVEL = IntegerProperty.create("filledlevel", 0, 9);
    public static final EnumProperty<FillingProperty> FILLING = EnumProperty.create("filling", FillingProperty.class);
    public CanBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FILLEDLEVEL);
        builder.add(FILLING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            ItemStack stack = player.getItemInHand(hand);
            if (state.getValue(FILLEDLEVEL) == 0) {
                if (stack.getItem() == ModItems.MARSHMELLOW) {
                    level.setBlockAndUpdate(pos, state.setValue(FILLEDLEVEL, 9).setValue(FILLING, FillingProperty.MARSHMALLOW));
                } else if (stack.getItem() == ModItems.CHOCOLATE) {
                    level.setBlockAndUpdate(pos, state.setValue(FILLEDLEVEL, 9).setValue(FILLING, FillingProperty.CHOCOLATE));
                }
                return InteractionResult.SUCCESS;
            }
            if (state.getValue(FILLEDLEVEL) == 9) {
                level.setBlockAndUpdate(pos, state.setValue(FILLEDLEVEL, 8));
                return InteractionResult.SUCCESS;
            }
            if (stack.get(ModItemComponents.COOKED_LEVEL) == null || stack.get(ModItemComponents.COOKED_LEVEL) == 0 ) {
                if (state.getValue(FILLEDLEVEL) > 0) {
                    if (stack.getItem() == Items.STICK) {
                        if (state.getValue(FILLING).equals(FillingProperty.MARSHMALLOW)) {
                            stack.shrink(1);
                            player.addItem(ModItems.MARSHMELLOW_STICK.getDefaultInstance());
                            level.setBlockAndUpdate(pos, state.setValue(FILLEDLEVEL, state.getValue(FILLEDLEVEL) - 1));
                        }
                    } else if (stack.getItem() == ModItems.MARSHMELLOW_STICK) {
                        if (state.getValue(FILLING).equals(FillingProperty.CHOCOLATE)) {
                            player.setItemInHand(hand, ModItems.CHOCOLATE_COATED_MARSHMELLOW_STICK.getDefaultInstance());
                            level.setBlockAndUpdate(pos, state.setValue(FILLEDLEVEL, state.getValue(FILLEDLEVEL) - 1));
                        }
                    }
                }
                return InteractionResult.SUCCESS;

            }
        }


        return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.create(0.25, 0, 0.25, 0.75, 0.625, 0.75);
    }

    @Override
    protected boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) {
        return true;
    }
}
