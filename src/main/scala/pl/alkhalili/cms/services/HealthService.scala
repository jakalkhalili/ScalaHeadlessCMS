package pl.alkhalili.cms.services

import cats.effect.Effect
import org.http4s.HttpRoutes
import pl.alkhalili.cms.Service

class HealthService[F[_] : Effect] extends Service[F] {
  override def routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "health" => Ok("shcms/0.0.1")
  }
}
