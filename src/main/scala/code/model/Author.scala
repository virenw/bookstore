package code.model

import net.liftweb.record.{MetaRecord, Record}
import net.liftweb.record.field.{LongField, LongTypedField, OptionalDateTimeField, OptionalIntField, StringField}
import net.liftweb.squerylrecord.KeyedRecord
import net.liftweb.squerylrecord.RecordTypeMode._
import org.squeryl.Query
import org.squeryl.annotations.Column
import net.liftweb.util.Helpers._
import net.liftweb.http.{SHtml, LiftRules}
import net.liftweb.util.FormBuilderLocator
import net.liftweb.common.{Full, Box, Empty}
import scala.xml.Text

class Author private() extends Record[Author] with KeyedRecord[Long] {

  def meta = Author

  @Column(name="id")
  val idField = new LongField(this, 100)
  val name = new StringField(this, ""){
    override def displayName = "Name"
  }

  val age = new OptionalIntField(this){
    override def displayName = "Age"
  }
  val birthday = new OptionalDateTimeField(this){
    override def displayName = "Birthday"
  }

  // relationship helpers
  def books: Query[Book] = Bookstore.books.where(_.authorId === id)

}

object Author extends Author with MetaRecord[Author] {
  def list:Query[Author] = Bookstore.authors.where(_.id.isNotNull)
}
