package io.elytra.sdk.network.protocol

object ProtocolInfo {
    /**
	 * The protocol version supported by the server.
	 */
    const val CURRENT_PROTOCOL = 578

    /**
	 * The game version supported by the server.
	 */
    const val MINECRAFT_VERSION = "1.15.2"

    //region PLAY
    const val CONFIRM_TELEPORT: Int = 0x00
    const val I_CUSTOM_PAYLOAD: Int = 0x0B // 0x9
    const val I_CHAT: Int = 0x03
    const val I_PLAYER_POSITION = 0x11
    const val I_PLAYER_LOOK = 0x12
    const val I_PLAYER_ROTATION = 0x13
    const val I_PLAYER_MOVEMENT = 0x14
    const val I_KEEP_ALIVE: Int = 0x0F
    const val I_TAB_COMPLETE: Int = 0x06
    const val O_TAB_COMPLETE: Int = 0x11
    const val I_PLAYER_ABILITIES: Int = 0x19
    const val O_PLAYER_ABILITIES: Int = 0x32
    const val O_CHUNK_LOAD: Int = 0x22
    const val O_UNLOAD_CHUNK: Int = 0x1E
    const val O_PLAY_EFFECT: Int = 0x23
    const val O_UPDATE_HEALTH: Int = 0x49
    const val O_SET_COMPRESSION: Int = 0x03
    const val O_CHAT: Int = 0x0F
    const val O_SPAWN_PLAYER = 0x05
    const val O_ENTITY_ANIMATION = 0x06
    const val O_DECLARE_COMMANDS = 0x12
    const val O_ENTITY_TELEPORT = 0x2B
    const val O_SPAWN_POSITION = 0x4E
    const val O_CHANGE_GAME_STATE = 0x1F
    const val O_CUSTOM_PAYLOAD: Int = 0x19
    const val O_KEEP_ALIVE: Int = 0x21
    const val CLIENT_SETTINGS: Int = 0x05
    const val HELD_ITEM_CHANGE: Int = 0x40
    const val SERVER_DIFFICULTY: Int = 0x0E
    const val JOIN_GAME: Int = 0x26
    const val O_PLAYER_POS_LOOK: Int = 0x36
    const val ENTITY_STATUS: Int = 0x1C
    const val PLAY_DISCONNECT: Int = 0x1B
    const val PLAYER_LIST_ITEM: Int = 0x34
    const val BOSS_INFO: Int = 0x0D
    const val TITLE: Int = 0x50
    //endregion

    //region STATUS
    const val I_PING: Int = 0x01
    const val O_PING: Int = 0x01
    const val SERVER_QUERY: Int = 0x00
    const val SERVER_INFO: Int = 0x00
    //endregion

    //region LOGIN
    const val LOGIN_START: Int = 0x00
    const val ENCRYPTION_RESPONSE: Int = 0x01
    const val ENCRYPTION_REQUEST: Int = 0x01
    const val LOGIN_SUCCESS: Int = 0x02
    const val LOGIN_DISCONNECT: Int = 0x00
    //endregion

    const val HANDSHAKE: Int = 0x00
}
