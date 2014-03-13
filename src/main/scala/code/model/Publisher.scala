package code.model

import net.liftweb.record.{MetaRecord, Record}
import net.liftweb.record.field.{LongField, LongTypedField, StringField}
import net.liftweb.squerylrecord.KeyedRecord
import net.liftweb.squerylrecord.RecordTypeMode._
import org.squeryl.Query
import org.squeryl.annotations.Column

class Publisher private () extends Record[Publisher] with KeyedRecord[Long] {

  def meta = Publisher

  @Column(name="id")
  val idField = new LongField(this, 1)
  //name field with length validation
  val name = new StringField(this, ""){
    override def displayName = "Name"
    //using the :: (cons) operator add custom validation to defaults
    override def validations = valMinLen(5, "Must be more than 5 characters") _ :: super.validations
  }

  def books: Query[Book] = Bookstore.books.where(_.publisherId === id)
}

object Publisher extends Publisher with MetaRecord[Publisher] {
  def list:Query[Publisher] = Bookstore.publishers.where(_.id.isNotNull)
}
