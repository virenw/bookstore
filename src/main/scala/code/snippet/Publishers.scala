package code.snippet

import net.liftweb.util.Helpers._
import code.model.Publisher

/**
 * Created by viren on 3/5/14.
 */
class Publishers {

  //replace function (#>) is a CSS selector,
  //On the right side of the #> symbol is the content that you want to bind to that selector on the left side
  def list = "tr" #> Publisher.list.map(publisher => ".name" #> publisher.name)
}
