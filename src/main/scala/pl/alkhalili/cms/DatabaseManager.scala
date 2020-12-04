package pl.alkhalili.cms

import cats.effect.{Blocker, ContextShift, Effect, Resource}
import doobie.hikari.HikariTransactor
import org.flywaydb.core.Flyway

import scala.concurrent.ExecutionContext

class DatabaseManager[F[_] : Effect](tnx: HikariTransactor[F]) {
  def migrate(implicit F: Effect[F]): F[Unit] =
    tnx.configure(dataSource =>
      F.pure {
        val flyway = Flyway.configure().dataSource(dataSource).load()
        flyway.migrate()
        ()
      }
    )

  def transactor: HikariTransactor[F] = tnx
}

object DatabaseManager {
  // FIXME: use postgres or mysql
  def transactor[F[_] : Effect : ContextShift](blocker: Blocker, xc: ExecutionContext): Resource[F, HikariTransactor[F]] =
    HikariTransactor.newHikariTransactor[F](
      "org.h2.Driver",
      "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
      "",
      "",
      xc,
      blocker
    )
}
