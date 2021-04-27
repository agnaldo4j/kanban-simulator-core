package com.kanban.usecase.board

import com.kanban.domain.KanbanSystemChanged
import com.kanban.usecase.board.Changeable.BoardCommand

import scala.util.{Failure, Try}

trait Changeable {
  def execute[RETURN](command: BoardCommand[RETURN]): Try[KanbanSystemChanged[RETURN]] = {
    command match {
      case _ => Failure(new IllegalStateException(s"Command not found ${command}"))
    }
  }
}

object Changeable {

  trait BoardCommand[RETURN]

}
