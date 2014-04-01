package code.snippet

import net.liftweb.http.PaginatorSnippet
import code.model.Author
import net.liftweb.util.Helpers._

/**
 * This Snippet uses PaginatorSnippet for paging of results.
 * Created by viren on 3/30/14.
 */
class AuthorsPageSnippet extends PaginatorSnippet[Author] {

  override def itemsPerPage = 3

  //items displayed on the current page
  def page = Author.searchForAuthors(itemsPerPage * curPage, itemsPerPage)

  //total number of items
  def count = Author.list.size

  //replace function (#>) is a CSS selector,
  //On the right side of the #> symbol is the content that you want to bind to that selector on the left side
  //for example this binds author name to name class which is used in the html file list_authors_page html.
  def list = "tr" #> page.map(author => ".name" #> author.name & ".age" #> author.age.get
    //& ".bday" #> birthdayToString(author.birthday)
  )
}
