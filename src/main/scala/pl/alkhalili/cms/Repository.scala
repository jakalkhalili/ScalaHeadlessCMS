package pl.alkhalili.cms

trait Repository[F[_], U] {
  def all: fs2.Stream[F, U]

  def get(id: Long): F[U]

  def delete(id: Long): F[Boolean]

  def find[A](by: A): F[U]

  def update[A, B](by: A, field: B): F[Boolean]

  def create(entity: U): F[Unit]
}
