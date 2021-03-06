package io.elytra.sdk.network.protocol.handlers.play

import io.elytra.api.chat.ChatMode
import io.elytra.api.chat.TextComponent
import io.elytra.api.command.handler.CommandHandler
import io.elytra.api.io.i18n.MessageBuilder
import io.elytra.sdk.network.NetworkSession
import io.elytra.sdk.network.protocol.handlers.ElytraMessageHandler
import io.elytra.sdk.network.protocol.message.play.inbound.ChatMessage
import io.elytra.sdk.network.protocol.message.play.outbound.OutboundChatMessage
import io.elytra.sdk.server.ElytraServer
import io.elytra.sdk.utils.ElytraConsts
import org.koin.core.KoinComponent
import org.koin.core.inject

class ChatHandler : ElytraMessageHandler<ChatMessage>(), KoinComponent {
    val commandHandler: CommandHandler by inject()

    override fun handle(session: NetworkSession, message: ChatMessage) {
        val player = getPlayerOrDisconnect(session)

        if (message.content.startsWith(ElytraConsts.COMMAND_PREFIX)) {
            commandHandler.handle(player, message.content)
            return
        }

        val chatMessage = MessageBuilder("chat.format")
            .with("player" to session.gameProfile!!.name)
            .getOrBuild()
            .split("{message}", limit = 2)

        ElytraServer.broadcastPacket(OutboundChatMessage(TextComponent(chatMessage[0]) {
            addExtra(message.content) {
                replaceColors = false
            }

            if (chatMessage.size > 1 && chatMessage[1].isNotEmpty()) {
                addExtra(chatMessage[1])
            }
        }, ChatMode.PLAYER))
    }
}
