package com.kanban.domain

import com.kanban.domain.Domain.{Domain, Id}

import java.util.UUID
import scala.collection.SortedSet

case class Flow(
                 id: Id = UUID.randomUUID().toString,
                 audit: Audit = Audit(),
                 steps: SortedSet[FlowStep] = SortedSet.empty
               ) extends Domain
//{
//  def addTaskOnFlowStep(task: Task, flowStepId: String) = {
//    var flowStep = steps.find(fs => fs.id == flowStepId)
//    println(flowStep)
//    flowStep match {
//      case Some(flowStep) => flowStep.tasks + task
//      case None => None
//    }
//  }
//}

object Flow {
  def simpleOne(): Flow = {
    val flowSteps = FlowStep.simple()
    new Flow(steps = flowSteps)
  }
}
