package io.elytra.sdk.network

import com.flowpowered.network.Message
import com.flowpowered.network.MessageHandler
import com.flowpowered.network.protocol.AbstractProtocol
import com.flowpowered.network.session.BasicSession
import com.mojang.authlib.GameProfile
import io.elytra.api.events.EventBus
import io.elytra.api.utils.Asyncable
import io.elytra.api.utils.Tickable
import io.elytra.sdk.network.events.SessionDisconnectEvent
import io.elytra.sdk.network.pipeline.CodecsHandler
import io.elytra.sdk.network.pipeline.EncryptionHandler
import io.elytra.sdk.network.protocol.PacketProvider
import io.elytra.sdk.network.protocol.message.DisconnectMessage
import io.elytra.sdk.network.protocol.packets.BasicPacket
import io.elytra.sdk.network.protocol.packets.HandshakePacket
import io.elytra.sdk.network.protocol.packets.Protocol
import io.elytra.sdk.server.Elytra
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandler
import io.netty.handler.codec.CodecException
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingDeque
import javax.crypto.SecretKey
import kotlin.random.Random


class NetworkSession(
	channel: Channel,
	var protocol: Protocol = Protocol.HANDSHAKE,
	@Volatile private var disconnected: Boolean = false,
	private var packetProvider: PacketProvider = PacketProvider(),
	private val messageQueue: BlockingQueue<Message> = LinkedBlockingDeque()
) : BasicSession(channel, HandshakePacket()), Tickable {

	private var connectionTimer: Int = 0
	var sessionState: SessionState = SessionState.HELLO
	val verifyToken: ByteArray = Random.nextBytes(4)
	var gameProfile: GameProfile? = null
	var encrypted: Boolean = false

	override fun sendWithFuture(message: Message?): ChannelFuture? {
		if (!isActive) {
			return null
		}
		return super.sendWithFuture(message)
	}

	override fun disconnect() {
		disconnect("No reason specified.")
	}

	override fun tick() {
		if(protocol == Protocol.LOGIN){
			if(sessionState == SessionState.READY_TO_ACCEPT){
				tryLogin()
			}else if(sessionState == SessionState.DELAY_ACCEPT){
				println("ACCEPT")
			}
			if (connectionTimer++ == 600) {
				disconnect("JABIRACA")
			}
		}

		var message: Message?
		while (messageQueue.poll().also { message = it } != null) {
			if (disconnected) break
			super.messageReceived(message)
		}

		/*if (disconnected && getProtocol() == packetProvider.playPacket) {
			player?.let { PlayerDisconnectEvent(it, "No reason specified") }?.let { EventBus.post(it) }
		}*/
	}

	fun protocol(protocol: Protocol){
		this.protocol = protocol
		when(protocol) {
			Protocol.LOGIN -> setProtocol(packetProvider.loginPacket)
			Protocol.PLAY -> setProtocol(packetProvider.playPacket)
			Protocol.STATUS -> setProtocol(packetProvider.statusPacket)
			Protocol.HANDSHAKE -> setProtocol(packetProvider.handshakePacket)
			else -> disconnect()
		}
	}

	public override fun setProtocol(protocol: AbstractProtocol?) {
		updatePipeline("codecs", CodecsHandler(protocol as BasicPacket))
		super.setProtocol(protocol)
	}

	override fun onDisconnect() {
		disconnected = true
	}

	override fun onInboundThrowable(throwable: Throwable?) {
		if (throwable is CodecException) {
			println("Error in inbound network: $throwable")
			return
		}

		disconnect("decode error: ${throwable?.message}")
	}

	override fun onHandlerThrowable(message: Message?, handle: MessageHandler<*, *>?, throwable: Throwable?) {
		println("Error while handling $message (${handle?.javaClass?.simpleName}) - $throwable")
	}

	override fun messageReceived(message: Message) {
		if (message is Asyncable) {
			super.messageReceived(message)
			return
		}

		messageQueue.add(message)
	}

	fun disconnect(reason: String) {
		if (protocol != Protocol.PLAY) {
			channel.close()
			return
		}

		println("${gameProfile?.name} : kicked due $reason")
		EventBus.post(SessionDisconnectEvent(sessionId))
		sendWithFuture(DisconnectMessage(reason))?.addListener(ChannelFutureListener.CLOSE)
	}

	private fun updatePipeline(key: String, handler: ChannelHandler) {
		channel.pipeline().replace(key, key, handler);
	}

	fun enableEncryption(sharedSecret: SecretKey) {
		encrypted = true
		channel.pipeline().addFirst("decrypt", EncryptionHandler(sharedSecret))
	}

	//TODO Then put this somewhere else maybe
	fun tryLogin() {
		if (!gameProfile!!.isComplete) {
			gameProfile = GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + gameProfile!!.name.toLowerCase()).toByteArray(StandardCharsets.UTF_8)), gameProfile!!.name)
		}

		Elytra.server.playerRegistry.initialize(this, gameProfile!!)
	}
}