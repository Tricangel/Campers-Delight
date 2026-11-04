package bee.camping.mixin.client;

import bee.camping.item.CampfireRoastableItem;
import bee.camping.item.CookedLevel;
import bee.camping.networking.SetCookedLevelPacket;
import bee.camping.networking.SetRoastingPacket;
import bee.camping.registry.CamperAttachments;
import bee.camping.registry.CamperDataComponents;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {


    @Shadow
    @Nullable
    public ClientLevel level;

    @Shadow
    @Nullable
    public LocalPlayer player;


    @Shadow
    @Nullable
    public HitResult hitResult;

    @Shadow
    @Final
    public Options options;

    @WrapOperation(method = "startAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;startDestroyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z"))
    private boolean startAttack(MultiPlayerGameMode instance, BlockPos blockPos, Direction direction, Operation<Boolean> original) {
        ClientLevel level = this.level;
        LocalPlayer player = this.player;
        if (player == null) return original.call(instance, blockPos, direction);
        ItemStack stack = player.getWeaponItem();

        if (level == null || !(stack.getItem() instanceof CampfireRoastableItem)) return original.call(instance, blockPos, direction);

        BlockHitResult hitResult = (BlockHitResult) this.hitResult;

        if (!level.getBlockState(hitResult.getBlockPos()).is(BlockTags.CAMPFIRES)) return original.call(instance, blockPos, direction);

        player.setAttached(CamperAttachments.COOKING_CAMPFIRE_ROASTABLE, true);
        player.setAttached(CamperAttachments.COOKING_CAMPFIRE_ROASTABLE_TIME, player.tickCount);
        ClientPlayNetworking.send(new SetRoastingPacket(player.getStringUUID(), true));

        return true;
    }

    @WrapOperation(method = "startAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;swing(Lnet/minecraft/world/InteractionHand;)V"))
    private void startAttack$swing(LocalPlayer instance, InteractionHand interactionHand, Operation<Void> original) {
        if (!(hitResult instanceof BlockHitResult blockHitResult)) {
            original.call(instance, interactionHand);
            return;
        }

        ItemStack stack = player.getWeaponItem();

        if (!level.getBlockState(blockHitResult.getBlockPos()).is(BlockTags.CAMPFIRES) || !(stack.getItem() instanceof CampfireRoastableItem)) {
            original.call(instance, interactionHand);
            return;
        }

    }

    @WrapMethod(method = "continueAttack")
    private void continueAttack(boolean bl, Operation<Void> original) {
        ClientLevel level = this.level;
        LocalPlayer player = this.player;
        if (player == null || !player.getAttachedOrSet(CamperAttachments.COOKING_CAMPFIRE_ROASTABLE, false)) {
            original.call(bl);
            return;
        }

        ItemStack stack = player.getWeaponItem();
        BlockHitResult hitResult = (BlockHitResult) this.hitResult;

        if (level == null || !(stack.getItem() instanceof CampfireRoastableItem campfireRoastableItem) || !level.getBlockState(hitResult.getBlockPos()).is(BlockTags.CAMPFIRES)) {
            original.call(bl);
            return;
        }

        int cookingTime = player.tickCount - player.getAttachedOrSet(CamperAttachments.COOKING_CAMPFIRE_ROASTABLE_TIME, 0);
        CookedLevel cookingLevel = stack.getOrDefault(CamperDataComponents.COOK_TIME, CookedLevel.RAW);
        if (cookingLevel.ordinal() > 4) return;

        int timeToCook = switch (cookingLevel) {
            case RAW -> 40; //4 //0.5
            case SLIGHTLY_COOKED -> 35; //5 //0.75
            case PERFECTLY_COOKED -> 15; //5 //1
            case ROASTED -> 20; //4 //0.5
            case BURNT -> 40; //2 //0.15
            default -> throw new IllegalStateException("Unexpected value: " + cookingLevel);
        };

        if (cookingTime > 10 && cookingTime % timeToCook == 0) {
            CookedLevel newLevel = CookedLevel.values()[cookingLevel.ordinal() + 1];

            stack.set(DataComponents.ITEM_NAME, newLevel.translation.copy().append(stack.getItem().getName()));
            stack.set(CamperDataComponents.COOK_TIME, newLevel);
            stack.set(DataComponents.FOOD, new FoodProperties(newLevel.nutrition, newLevel.saturation, true));
            ClientPlayNetworking.send(new SetCookedLevelPacket(player.getStringUUID(), newLevel.ordinal()));
        }
    }

    @Inject(method = "handleKeybinds", at = @At("HEAD"))
    private void handleKeybinds(CallbackInfo ci) {
        if (this.player != null) {
            if (!this.options.keyAttack.isDown()) {

                this.player.setAttached(CamperAttachments.COOKING_CAMPFIRE_ROASTABLE, false);
                ClientPlayNetworking.send(new SetRoastingPacket(this.player.getStringUUID(), false));

            }
        }
    }




}
