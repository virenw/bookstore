package code.snippet

import net.liftweb.http.{S, LiftScreen}
import code.model.{Author, Book}

/**
 * Form to add a Book
 */
object BookScreen extends LiftScreen{

  object book extends ScreenVar(Book.createRecord)

  addFields(() => book.is.name)
  addFields(() => book.is.genre)
  addFields(() => book.is.secondaryGenre)
  addFields(() => book.is.publishedInYear)
  val author = field("Choose Author", Author.list.toList)

  def finish(){
    S.redirectTo("/index.html")
  }
}
