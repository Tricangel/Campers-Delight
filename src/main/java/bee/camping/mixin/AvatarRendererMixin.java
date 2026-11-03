package bee.camping.mixin;

import bee.camping.client.data.CampfireRoastableRenderState;
import bee.camping.item.CampfireRoastableItem;
import bee.camping.registry.CamperAttachments;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity> extends LivingEntityRenderer<AvatarlikeEntity, AvatarRenderState, PlayerModel> {
	public AvatarRendererMixin(EntityRendererProvider.Context context, PlayerModel entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(at = @At("HEAD"), method = "getArmPose(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;", cancellable = true)
	private static void init(Avatar avatar, ItemStack itemStack, InteractionHand interactionHand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
		if (itemStack.getItem() instanceof CampfireRoastableItem) cir.setReturnValue(HumanoidModel.ArmPose.CAMPERS_DELIGHT_CAMPFIRE_ROASTABLE);
	}

	@Inject(at = @At("HEAD"), method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", cancellable = true)
	private void extractRenderState(AvatarlikeEntity avatar, AvatarRenderState avatarRenderState, float f, CallbackInfo ci) {
		CampfireRoastableRenderState state = new CampfireRoastableRenderState();
		state.isRoasting = true;
		if (avatar.getAttachedOrSet(CamperAttachments.COOKING_CAMPFIRE_ROASTABLE, false)) avatarRenderState.setData(CampfireRoastableRenderState.KEY, state);
	}
}