package bee.camping.networking;

import bee.camping.CampersDelight;
import bee.camping.item.CampfireRoastableItem;
import bee.camping.item.CookedLevel;
import bee.camping.registry.CamperAttachments;
import bee.camping.registry.CamperDataComponents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public record SetCookedLevelPacket(String uuid, int level) implements CustomPacketPayload {
    public static final Identifier ID = CampersDelight.id("start_roasting");
    public static final Type<SetCookedLevelPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SetCookedLevelPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, SetCookedLevelPacket::uuid,
            ByteBufCodecs.INT, SetCookedLevelPacket::level,
            SetCookedLevelPacket::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void execute(SetCookedLevelPacket packet, ServerPlayNetworking.Context context) {
        ServerPlayer player = context.player();
        if (!player.getStringUUID().equals(packet.uuid())) return;

        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND).copy();
        CookedLevel cookedLevel = CookedLevel.values()[packet.level()];


        stack.set(DataComponents.ITEM_NAME, cookedLevel.translation.copy().append(stack.getItem().getName()));
        stack.set(CamperDataComponents.COOK_TIME, cookedLevel);
        stack.set(DataComponents.FOOD, new FoodProperties(cookedLevel.nutrition, cookedLevel.saturation, true));
        player.setItemInHand(InteractionHand.MAIN_HAND, stack);

        player.level().playSound(null, player.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.1f, 1f);
        if (cookedLevel == CookedLevel.CHARRED) {
            Item item = player.getWeaponItem().getItem() instanceof CampfireRoastableItem roastable ? roastable.getBurntItem() : Items.STICK;
            player.setItemInHand(InteractionHand.MAIN_HAND, item.getDefaultInstance());
        }
    }
}
