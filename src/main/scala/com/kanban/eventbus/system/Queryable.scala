package com.kanban.eventbus.system

import com.kanban.domain.{KanbanSystem, KanbanSystemChanged}
import com.kanban.domain.SystemQueryable.{GetAllOrganizations, GetOrganizationByName}
import com.kanban.eventbus.EventBusQuery.{GetAllOrganizationsFromSystem, GetOrganizationByNameFromSystem, Query}
import com.kanban.usecase.system.SystemUseCase

import scala.util.{Failure, Try}

trait SystemQueryable {
  val systemState: KanbanSystem

  def execute[RETURN](event: Query): Try[RETURN] = {
    event match {
      case GetOrganizationByNameFromSystem(name) => SystemUseCase.execute(GetOrganizationByName(name, systemState))
      case GetAllOrganizationsFromSystem() => SystemUseCase.execute(GetAllOrganizations(systemState))
      case _ => Failure(new IllegalStateException(s"Event not found: $event"))
    }
  }
}

