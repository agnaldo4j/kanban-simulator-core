package com.kanban.eventbus

import com.kanban.domain.KanbanSystem
import com.kanban.domain.KanbanSystemChanged
import EventBusCommand.Command
import EventBusQuery.Query
import com.kanban.eventbus.{EventBusCommand, EventBusQuery}
import com.kanban.eventbus.system.{SystemChangeable, SystemQueryable}
import scala.util.Try

object EventBus {
  def apply(systemState: KanbanSystem = KanbanSystem()): EventBus = new EventBus(systemState)
}

class EventBus(val systemState: KanbanSystem)
    extends SystemQueryable
    with SystemChangeable {

  def emit[RETURN](event: Command): Try[KanbanSystemChanged[RETURN]] = execute(event)
  def emit[RETURN](event: Query): Try[RETURN] = execute(event)
}
