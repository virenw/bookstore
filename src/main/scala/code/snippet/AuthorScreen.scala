package code.snippet

import net.liftweb.http.{S, LiftScreen}
import code.model.{Bookstore, Author}

object AuthorScreen extends LiftScreen{
  object author extends ScreenVar(Author.createRecord)
  addFields(() => author.is.name)
  addFields(() => author.is.age)
  addFields(() => author.is.birthday)
  def finish() {
    Bookstore.authors.insert(author)
    S.notice("Author '%s' saved " format author.name)
  }
}
