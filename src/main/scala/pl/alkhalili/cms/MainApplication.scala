package pl.alkhalili.cms

import cats.data.Kleisli
import cats.effect.{Blocker, ContextShift, Effect, ExitCode, IO, IOApp, Resource}
import cats.implicits._
import doobie.ExecutionContexts
import org.http4s._
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import pl.alkhalili.cms.repository.PageRepository
import pl.alkhalili.cms.services.{HealthService, PageService}

import scala.concurrent.ExecutionContext

object MainApplication extends IOApp {
  private val databaseSetup: Resource[IO, DatabaseManager[IO]] = for {
    xc <- ExecutionContexts.fixedThreadPool[IO](32)
    blocker <- Blocker[IO]
    transactor <- DatabaseManager.transactor[IO](blocker, xc)
  } yield new DatabaseManager[IO](transactor)

  override def run(args: List[String]): IO[ExitCode] =
    databaseSetup.use(db => db.migrate) *> databaseSetup.use(appStream)

  private def appStream(databaseManager: DatabaseManager[IO]): IO[ExitCode] = for {
    exitCode <- BlazeServerBuilder[IO](ExecutionContext.global)
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(routes(databaseManager))
      .serve.compile.lastOrError
  } yield exitCode

  /**
   * Generates routes from services defined in `services` package
   *
   * @return Kleisli[F, Request[F], Response[F]]
   */
  private def routes[F[_] : Effect : ContextShift](databaseManager: DatabaseManager[F]): Kleisli[F, Request[F], Response[F]] = {
    def routesFromServices: Kleisli[F, Request[F], Response[F]] = (
      new HealthService[F].routes <+>
        // CRUD services
        new PageService[F](new PageRepository[F](databaseManager.transactor)).routes
      ).orNotFound

    routesFromServices
  }
}
