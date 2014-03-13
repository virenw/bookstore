package code.snippet

import net.liftweb.http.{S, LiftScreen}
import code.model.{Bookstore, Publisher, Author, Book}

/**
 * Form to add a Book
 */
object BookScreen extends LiftScreen{

  object book extends ScreenVar(Book.createRecord)

  addFields(() => book.is.name)
  val selectedAuthor = field("Choose Author", Author.list.toList)
  addFields(() => book.is.genre)
  addFields(() => book.is.secondaryGenre)
  val selectedPublisher = field("Choose Publisher", Publisher.list.toList)
  addFields(() => book.is.publishedInYear)

  def finish(){
//    println("Selected author: " + selectedAuthor(0).id)
    book.is.authorId.set(selectedAuthor(0).id)
//    println("Selected publisher: " + selectedPublisher(0).id)
    book.is.publisherId.set(selectedPublisher(0).id)
    Bookstore.books.insert(book)
//    S.redirectTo("/index.html")
    S.notice("Author '%s' saved " format book.name)
  }
}
