package code.snippet

import net.liftweb.http.LiftScreen
import code.model.{Bookstore, Publisher}

/**
 * Created by viren on 3/5/14.
 */
class PublisherScreen extends LiftScreen{

  //screenvar to hold a new instance of publisher,
  //it is is local to this screen and cannot be shared directly
  object publisher extends ScreenVar(Publisher.createRecord)
  //register and display only the publisher field
  addFields(() => publisher.is.name)
  //finish method inserts the publisher into our Bookstore schema/db
  def finish() {
    Bookstore.publishers.insert(publisher)
  }
}
