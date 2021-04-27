package com.kanban.usecase.system

import com.kanban.domain.Organization
import com.kanban.domain.KanbanSystemChanged
import com.kanban.domain.SystemChangeable.{AddOrganization, DeleteOrganization, SystemCommand}
import com.kanban.usecase.exceptions.OrganizationAlreadyExists

import scala.util.{Failure, Success, Try}

trait Changeable {
  def execute[RETURN](command: SystemCommand): Try[KanbanSystemChanged[RETURN]] = command match {
    case AddOrganization(name, kanbanSystem) =>
      kanbanSystem.organizationByName(name) match {
        case Some(_) => Failure(new OrganizationAlreadyExists(name))
        case None => Success(kanbanSystem.addOrganization(Organization(name = name)).asInstanceOf[KanbanSystemChanged[RETURN]])
      }
    case DeleteOrganization(id, kanbanSystem) =>
      Success(kanbanSystem.removeOrganization(organizationId = id).asInstanceOf[KanbanSystemChanged[RETURN]])
    case _ => Failure(new IllegalStateException(s"Command not found ${command}"))
  }
}
