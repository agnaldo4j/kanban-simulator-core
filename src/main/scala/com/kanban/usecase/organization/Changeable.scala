package com.kanban.usecase.organization

import com.kanban.domain.Domain.Id
import com.kanban.domain.{Board, KanbanSystem, KanbanSystemChanged}
import com.kanban.usecase.organization.Changeable.{AddSimpleBoard, OrganizationCommand}

import scala.util.{Failure, Try}

trait Changeable {
  def execute[RETURN](command: OrganizationCommand[RETURN]): Try[KanbanSystemChanged[RETURN]] = {
    command match {
      case AddSimpleBoard(organizationId, name, kanbanSystem) => {
        val newKanban = Board.simpleOneWithName(name)
        kanbanSystem.addBoardOn(organizationId, newKanban).asInstanceOf[Try[KanbanSystemChanged[RETURN]]]
      }
      case _ => Failure(new IllegalStateException(s"Command not found ${command}"))
    }
  }
}

object Changeable {

  trait OrganizationCommand[RETURN]

  case class AddSimpleBoard(
                              organizationId: Id,
                              name: String,
                              kanbanSystem: KanbanSystem
                            ) extends OrganizationCommand[KanbanSystem]

}
