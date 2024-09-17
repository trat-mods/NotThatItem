package net.nti.network.packet;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record FilterStackIdChangePayload(String id) implements CustomPayload {
    public static final Id<FilterStackIdChangePayload> ID = CustomPayload.id("ntifilterpacket");
    public static final PacketCodec<PacketByteBuf, FilterStackIdChangePayload> CODEC = PacketCodec.of(FilterStackIdChangePayload::write, FilterStackIdChangePayload::read);

    public static FilterStackIdChangePayload read(PacketByteBuf buf) {
        String id = buf.readString();
        return new FilterStackIdChangePayload(id);
    }

    public void send() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        write(buf);
        ClientPlayNetworking.send(this);
        //ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, PACKET_ID, buf);
    }


    public void write(PacketByteBuf buf) {
        buf.writeString(id);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
