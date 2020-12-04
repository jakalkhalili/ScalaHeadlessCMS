package pl.alkhalili.cms.repository

import cats.effect.Effect
import cats.implicits._
import doobie._
import doobie.implicits._
import pl.alkhalili.cms.Repository
import pl.alkhalili.cms.domain.Page

class PageRepository[F[_] : Effect](tnx: Transactor[F]) extends Repository[F, Page] {
  override def all: fs2.Stream[F, Page] =
    sql"select * from pages"
      .query[Page]
      .stream
      .transact(tnx)

  override def get(id: Long): F[Page] =
    sql"select * from pages WHERE id=$id"
      .query[Page]
      .unique
      .transact(tnx)

  override def delete(id: Long): F[Boolean] = ???

  override def find[A](by: A): F[Page] = ???

  override def update[A, B](by: A, field: B): F[Boolean] = ???

  override def create(entity: Page): F[Unit] =
    sql"insert into pages(title, content) values (${entity.title}, ${entity.content})"
      .update
      .withUniqueGeneratedKeys[Long]("id")
      .transact(tnx)
      .void
}
