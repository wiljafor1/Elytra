package io.elytra.sdk.events.entity

import io.elytra.api.entity.Entity
import io.elytra.api.events.ElytraEvent

class EntitySpawnEvent(val entity: Entity) : ElytraEvent
