package io.elytra.sdk.world

import io.elytra.api.entity.Player
import io.elytra.api.world.Chunk
import io.elytra.api.world.ChunkColumn
import io.elytra.api.world.Position
import io.elytra.api.world.World

class ElytraWorld(
	private val name: String,
	private val chunkColumns: List<ChunkColumn> = mutableListOf()
) : World {

	override fun getChunkAt(x: Int, z: Int): Chunk? {
		TODO("Not yet implemented")
	}

	override fun setChunkAt(x: Int, y: Int, z: Int, blockId: Int) {
		TODO("Not yet implemented")
	}

	override fun teleport(player: Player, position: Position) {
		TODO("Not yet implemented")
	}

	override fun tick() {
		TODO("Not yet implemented")
	}
}
