package com.kanban.storage

import com.kanban.domain.KanbanSystem
import com.kanban.eventbus.EventBusCommand
import com.kanban.storage.Storage

import scala.util.{Success, Try, Failure}

object DefaultStorage {
  def apply(): Storage = new DefaultStorage()
}

class DefaultStorage extends Storage {
  override def log(event: EventBusCommand.Command): Try[EventBusCommand.Command] = {
    println("log")
    Success(event)
  }

  override def load(): List[EventBusCommand.Command] = List()

  override def loadSystem(): KanbanSystem = KanbanSystem()

  override def snapshot(kanbanSystem: KanbanSystem): Unit = println("snapshot")
}
