package com.kanban

import com.kanban.domain.KanbanSystemChanged
import com.kanban.domain.Organization
import com.kanban.storage.{DefaultStorage, JournalingSystem}
import zio.console._
import zio._
import com.kanban.eventbus.{EventBus, EventBusCommand}
import com.kanban.eventbus.EventBusCommand._
import com.kanban.domain.KanbanSystemChanged

import scala.util.{Failure, Success, Try}

object MyApp extends zio.App {
    def run(args: List[String]) = myAppLogicUglyWay.exitCode
}

val myAppLogic = for {
    _ <- putStrLn("Hello! what ir your name?")
    name <- getStrLn
    _ <- putStrLn(s"Hello, ${name}, welcome to ZIO!")
} yield ()

val myAppLogicUglyWay = putStrLn("Hello! what ir your Ugly name?")
  .zipRight(getStrLn)
  .flatMap( name => putStrLn(s"Hello, ${name}, welcome to Ugly ZIO!"))

// Witout main control
@main def hello: Unit = {
    val runtime = Runtime.default
    //runtime.unsafeRun(Task(println("Hello world")))

    val storage = DefaultStorage()
    val journalingSystem = JournalingSystem(storage)
    val eventBus = EventBus()

    val event = AddOrganizationOnSystem("Teste")

    def execute: ZIO[zio.ZEnv, Throwable, KanbanSystemChanged[Organization]] = {
        for {
            savedEvent <- IO.fromTry(journalingSystem.log(event))
            changed <- ZIO.fromTry(eventBus.emit[Organization](savedEvent))
        } yield changed
    }

    val result = runtime.unsafeRun(execute)
    println(result)
}
