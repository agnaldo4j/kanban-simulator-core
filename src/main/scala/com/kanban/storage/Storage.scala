package com.kanban.storage

import com.kanban.domain.KanbanSystem
import com.kanban.eventbus.EventBusCommand.Command
import scala.util.Try

trait Storage:
  def log(event: Command): Try[Command]

  def load(): List[Command]

  def loadSystem(): KanbanSystem

  def snapshot(kanbanSystem: KanbanSystem): Unit
