package com.kanban.domain

import scala.collection.SortedSet

object KanbanSystemMain extends App {
  def run(args: String) = {
//    var kanbanSystem = KanbanSystem()

    val board = Board.simpleOneWithName("Basic Board")

    val flow = board.flow
    val steps = flow.steps
    val firstFlowStepOption = steps.find(fs => fs.order == 0)

    // como retornar
    firstFlowStepOption match {
      case Some(flowStep) => flowStep
      case None => None
    }

    val firstFlowStep: FlowStep = firstFlowStepOption.get

    // remove first flowStep with no tasks
    val stepsWithoutFirstFlowStep = steps - firstFlowStep

    val tasks = SortedSet(
      Task.simpleOneWithName("add client", order = 0),
      Task.simpleOneWithName("load all clients", order = 1),
      Task.simpleOneWithName("load client details", order = 2),
      Task.simpleOneWithName("update client", order = 3),
      Task.simpleOneWithName("remove client", order = 4)
    )

    val newFlowStep = firstFlowStep.copy(tasks = tasks)

    // add first flowStep with all tasks
    val newFlowSteps = stepsWithoutFirstFlowStep + newFlowStep

    val newFlow = flow.copy(steps = newFlowSteps)
    val newBoard = board.copy(flow = newFlow)

    showBoard(newBoard)

//    flow.addTaskOnFlowStep(task1, firstFlowStep.id)
//    flow.addTaskOnFlowStep(task2, firstFlowStep.id)
//    flow.addTaskOnFlowStep(task3, firstFlowStep.id)
//    flow.addTaskOnFlowStep(task4, firstFlowStep.id)
//    flow.addTaskOnFlowStep(task5, firstFlowStep.id)

//    board.addTaskOnFlowStep(Task())
//
//    val changed = kanbanSystem.addOrganization(Organization(name = "Novo", boards = Map("1" -> board)))
//    var kanbaSystem = changed.kanbanSystem
//
//    kanbaSystem.simulate()
//    kanbaSystem.moveTask(1)
//    kanbaSystem.moveWorer()
//
//    kanbaSystem.simulate()
//    kanbaSystem.showMetrics()

  }

  def showBoard(board: Board) = {
    println(s"### ${ board.name } ###")
    for (flowStep <- board.flow.steps) {
      println(s"  [${ flowStep.order }]${ flowStep.name }")
      for (task <- flowStep.tasks) {
        println(s"    [T]${ task.name }")
      }
    }

    println("# Workers #")
    for (worker <- board.workers) {
      println(s"  ${ worker.name } [${ worker.abilities}]")
    }
  }

  run(null)
}
