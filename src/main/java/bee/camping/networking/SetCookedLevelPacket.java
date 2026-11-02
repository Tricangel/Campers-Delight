package bee.camping.networking;

import bee.camping.CampersDelight;
import bee.camping.item.CampfireRoastableItem;
import bee.camping.registry.CamperAttachments;
import bee.camping.registry.CamperDataComponents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
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
        Player player = context.player();
        if (!player.getStringUUID().equals(packet.uuid())) return;

        player.getWeaponItem().set(CamperDataComponents.COOK_TIME, packet.level());

        if (packet.level == 5) {
            Item item = player.getWeaponItem().getItem() instanceof CampfireRoastableItem roastable ? roastable.getBurntItem() : Items.STICK;
            player.setItemInHand(InteractionHand.MAIN_HAND, item.getDefaultInstance());
        }
    }
}
