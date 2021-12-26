package tfar.dankstorage.network.server;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.inventory.AbstractContainerMenu;
import tfar.dankstorage.container.AbstractDankMenu;
import tfar.dankstorage.network.DankPacketHandler;
import tfar.dankstorage.world.DankInventory;

public class C2SMessageLockFrequency implements ServerPlayNetworking.PlayChannelHandler {

    public static void send(boolean slot) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeBoolean(slot);
        ClientPlayNetworking.send(DankPacketHandler.lock_id, buf);
    }

    public void handle(ServerPlayer player, boolean slot) {
        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof AbstractDankMenu dankMenu) {
            DankInventory inventory = dankMenu.dankInventory;
            inventory.toggleIdLock();
        }
    }

    @Override
    public void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        boolean slot = buf.readBoolean();
        server.execute(() -> handle(player, slot));
    }
}
