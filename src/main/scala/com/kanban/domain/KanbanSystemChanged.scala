package com.kanban.domain

case class KanbanSystemChanged[RESULT](val kanbanSystem: KanbanSystem, val result: RESULT)
