package com.kanban.domain

import com.kanban.domain.Domain.{ComparableByInteger, Domain, Id}

import java.util.UUID

sealed abstract class Service

case object Expedite extends Service

case object FixedDate extends Service

case object Standard extends Service

case object Intangible extends Service

object Task {
  def simpleOneWithName(name: String, order: Int): Task = {
    simpleOne(
      name = name,
      order = order,
    )
  }

  def simpleOne(
                 id: String = UUID.randomUUID().toString,
                 name: String,
                 order: Int,
               ): Task = {
    new Task(
      id = id,
      name = name,
      order = order,
    )
  }
}

case class Task(
                 id: Id = UUID.randomUUID().toString,
                 name: String,
                 audit: Audit = Audit(),
                 order: Int,
                 serviceClass: Service = Standard
               ) extends Domain with ComparableByInteger

