package com.kanban.eventbus.system

import com.kanban.domain.{KanbanSystem, KanbanSystemChanged}
import com.kanban.domain.SystemChangeable.AddOrganization
import com.kanban.eventbus.EventBusCommand.{AddOrganizationOnSystem, Command}
import com.kanban.storage.Storage
import com.kanban.usecase.system.SystemUseCase

import scala.util.{Failure, Try}

trait SystemChangeable {
  val systemState: KanbanSystem

  def execute[RETURN](event: Command): Try[KanbanSystemChanged[RETURN]] = {
    event match {
      case AddOrganizationOnSystem(name) =>
        SystemUseCase.execute[RETURN](AddOrganization(name, systemState))
      case _ =>
        Failure(new IllegalStateException(s"Event not found: $event"))
    }
  }
}
