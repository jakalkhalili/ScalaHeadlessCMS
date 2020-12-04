package pl.alkhalili.cms.services

import cats.effect.Effect
import cats.implicits._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec._
import org.http4s.circe._
import pl.alkhalili.cms.Service
import pl.alkhalili.cms.domain.Page
import pl.alkhalili.cms.repository.PageRepository

class PageService[F[_] : Effect](val repository: PageRepository[F]) extends Service[F] {
  override def routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "pages" / LongVar(id) =>
      for {
        page <- repository.get(id)
        response <- Ok(page.asJson)
      } yield response
    case req@POST -> Root / "pages" => for {
      page <- req.as[Page]
      _ <- repository.create(page)
      response <- Ok()
    } yield response
  }
}
