package com.kanban.usecase.organization

import com.kanban.domain.Domain.Id
import com.kanban.domain.{Board, KanbanSystem}
import com.kanban.usecase.organization.Queryable.{GetAllBoardsFrom, OrganizationQuery}

import scala.util.{Failure, Success, Try}

trait Queryable {
  def execute[RETURN](query: OrganizationQuery[RETURN]): Try[RETURN] = {
    query match {
      case GetAllBoardsFrom(organizationId, kanbanSystem) =>
        Success(kanbanSystem.allBoards(organizationId).asInstanceOf[RETURN])
      case _ => Failure(new IllegalStateException(s"Command not found: $query"))
    }
  }
}

object Queryable {

  trait OrganizationQuery[RETURN]

  case class GetAllBoardsFrom(
                                organizationId: Id,
                                kanbanSystem: KanbanSystem
                              ) extends OrganizationQuery[List[Board]]

}
