package bee.camping.networking;

import bee.camping.CampersDelight;
import bee.camping.registry.CamperAttachments;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public record SetRoastingPacket(String uuid, boolean isRoasting) implements CustomPacketPayload {
    public static final Identifier ID = CampersDelight.id("set_roasting");
    public static final Type<SetRoastingPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SetRoastingPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, SetRoastingPacket::uuid,
            ByteBufCodecs.BOOL, SetRoastingPacket::isRoasting,
            SetRoastingPacket::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void execute(SetRoastingPacket packet, ServerPlayNetworking.Context context) {
        Player player = context.player();
        if (!player.getStringUUID().equals(packet.uuid())) return;

        player.setAttached(CamperAttachments.COOKING_CAMPFIRE_ROASTABLE, packet.isRoasting());
    }
}
