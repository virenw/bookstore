package code.snippet

import net.liftweb.util.Helpers._
import code.model.Book

/**
 * Created by viren on 3/12/14.
 */
object Books {

  //replace function (#>) is a CSS selector,
  //On the right side of the #> symbol is the content that you want to bind to that selector on the left side
  def list = "tr" #> Book.list.map(book => ".name" #> book.name & ".author" #> book.author.get.name
  & ".genre" #> book.genre & ".publisher" #> book.publisher.get.name)
}
