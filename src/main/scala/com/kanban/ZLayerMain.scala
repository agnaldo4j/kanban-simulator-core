package com.kanban

import zio.{ExitCode, Has, Task, ZIO, ZLayer}

case class User(val name: String, val email: String)

type UserEmailerEnv = Has[UserEmailer.Service]

object UserEmailer {
  trait Service {
    def notify(user: User, message: String): Task[Unit]
  }

  val live: ZLayer[Any, Nothing, UserEmailerEnv] = ZLayer.succeed(
    new Service {
      override def notify(user: User, message: String): Task[Unit] =
        Task {
          println(s"Sending '$message' to ${user.email}")
        }
    }
  )

  def notify(user: User, message: String): ZIO[UserEmailerEnv, Throwable, Unit] =
    ZIO.accessM(_.get.notify(user, message))
}

type UserDbEnv = Has[UserDb.Service]

object UserDb {
  trait Service {
    def insert(user: User): Task[Unit]
  }

  val live: ZLayer[Any, Nothing, UserDbEnv] = ZLayer.succeed {
    new Service {
      override def insert(user: User): Task[Unit] = Task {
        println(s"[Database] insert into public.user values ('${user.name}')")
      }
    }
  }

  def insert(u: User): ZIO[UserDbEnv, Throwable, Unit] = ZIO.accessM(_.get.insert(u))
}

type UserSubscriptionEnv = Has[UserSubscription.Service]

object UserSubscription {
  import UserEmailer._
  import UserDb._

  class Service(notifier: UserEmailer.Service, userModel: UserDb.Service) {
    def subscribe(u: User): Task[User] = {
      for {
        _ <- userModel.insert(u)
        _ <- notifier.notify(u, s"Welcome, ${u.name}! Here are some ZIO articles for you here at Rock the JVM.")
      } yield u
    }
  }

  val live: ZLayer[UserEmailerEnv with UserDbEnv, Nothing, UserSubscriptionEnv] =
    ZLayer.fromServices[UserEmailer.Service, UserDb.Service, UserSubscription.Service] { (emailer, db) =>
      new Service(emailer, db)
    }

  def subscribe(u: User): ZIO[UserSubscriptionEnv, Throwable, User] = ZIO.accessM(_.get.subscribe(u))
}

val userBackendLayer: ZLayer[Any, Nothing, UserDbEnv with UserEmailerEnv] = UserDb.live ++ UserEmailer.live

val userSubscriptionLayer: ZLayer[Any, Throwable, UserSubscriptionEnv] = userBackendLayer >>> UserSubscription.live

val myHorizontalLogic: ZIO[Any, Throwable, Unit] = UserEmailer
  .notify(User("Daniel", "daniel@rockthejvm.com"), "Welcome to Rock the JVM!")
  //.provideLayer(UserEmailer.live)
  .provideLayer(userBackendLayer)

val myVerticalLogic: ZIO[Any, Any, ExitCode] = {
  val userRegistrationLayer = (UserDb.live ++ UserEmailer.live) >>> UserSubscription.live

  UserSubscription.subscribe(User("daniel", "daniel@rockthejvm.com"))
    .provideLayer(userRegistrationLayer)
    .catchAll(t => ZIO.succeed(t.printStackTrace()).map(_ => ExitCode.failure))
    .map { u =>
      println(s"Registered user: $u")
      ExitCode.success
    }
}

object ZLayerMain extends zio.App {
  def run(args: List[String]) = {
    //myHorizontalLogic.exitCode
    myVerticalLogic.exitCode
  }
}
