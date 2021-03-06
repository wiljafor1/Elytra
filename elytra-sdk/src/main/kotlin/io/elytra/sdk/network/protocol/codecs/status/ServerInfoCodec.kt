package io.elytra.sdk.network.protocol.codecs.status

import com.flowpowered.network.util.ByteBufUtils
import io.elytra.sdk.network.protocol.codecs.OutboundCodec
import io.elytra.sdk.network.protocol.message.status.ServerInfoMessage
import io.netty.buffer.ByteBuf

class ServerInfoCodec : OutboundCodec<ServerInfoMessage>() {
    override fun encode(buffer: ByteBuf, message: ServerInfoMessage): ByteBuf {
        ByteBufUtils.writeUTF8(buffer, message.body)
        return buffer
    }
}
