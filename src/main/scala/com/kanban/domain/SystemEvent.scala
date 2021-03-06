package com.kanban.domain

import com.kanban.domain.Domain.Id

object SystemChangeable {

  trait SystemCommand

  case class AddOrganization(name: String, kanbanSystem: KanbanSystem) extends SystemCommand

  case class DeleteOrganization(id: Id, kanbanSystem: KanbanSystem) extends SystemCommand

}

object SystemQueryable {

  trait SystemQueryable

  case class GetOrganizationByName(name: String, kanbanSystem: KanbanSystem) extends SystemQueryable

  case class GetOrganizationById(id: Id, kanbanSystem: KanbanSystem) extends SystemQueryable

  case class GetAllOrganizations(kanbanSystem: KanbanSystem) extends SystemQueryable

}
