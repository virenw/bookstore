package code.model

import org.squeryl.Schema

object Bookstore extends Schema{

  val authors = table[Author]("authors")
  val books = table[Book]("books")
  val publishers = table[Publisher]("publishers")

  // this is a test schema, we can expose the power tools ! :
  override def drop = super.drop

}
