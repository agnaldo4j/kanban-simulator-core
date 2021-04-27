package com.kanban.storage

import com.kanban.domain.{KanbanSystem, KanbanSystemChanged}
import com.kanban.eventbus.{EventBusCommand, EventBusQuery}

import scala.util.{Try, Failure}

object JournalingSystem {
  def apply(storage: Storage = DefaultStorage()): JournalingSystem = new JournalingSystem(storage)
}

class JournalingSystem(val storage: Storage) {

  def reload() = {
    reloadSystem()
    reloadEvents()
  }

  def log(event: EventBusCommand.Command): Try[EventBusCommand.Command] = storage.log(event)

  def snapshot(systemState: KanbanSystem) = {
    storage.snapshot(systemState)
  }

  def emit[RETURN](event: EventBusCommand.Command): Try[KanbanSystemChanged[RETURN]] = {
    Failure(new IllegalStateException())
  }

  def emit[RETURN](event: EventBusQuery.Query): Try[RETURN] = {
    Failure(new IllegalStateException())
  }

  private def reloadSystem(): KanbanSystem = storage.loadSystem()

  private def reloadEvents() = {
    val events = storage.load()
    events.foreach(emit)
  }
}
