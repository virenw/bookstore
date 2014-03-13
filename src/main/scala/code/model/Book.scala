package code.model

import net.liftweb.record.{MetaRecord, Record}
import net.liftweb.squerylrecord.KeyedRecord
import net.liftweb.squerylrecord.RecordTypeMode._
import org.squeryl.annotations.Column
import net.liftweb.record.field._
import org.squeryl.Query

trait Genre extends Enumeration {
  type Genre = Value
  val SciFi = Value(1, "Sci-Fi")
  val Boring = Value(2, "Boring")
  val GetRichQuickScam = Value(3, "GetRichQuickScam")
  val Novel = Value(4, "Novel")
  val Culinary = Value(5, "Culinary")
}
object Genre extends Genre

class Book private () extends Record[Book] with KeyedRecord[Long]{

  def meta = Book

  @Column(name="id")
  val idField = new LongField(this, 100)
  val name = new StringField(this, ""){
    override def displayName = "Name"
  }
  val publishedInYear = new IntField(this, 1990){
    override def displayName = "Year Published"
  }

  val publisherId = new LongField(this, 0)

  val authorId = new LongField(this, 0)

  val genre = new EnumField[Book,Genre](this, Genre){
    override def displayName = "Genre"
  }

  val secondaryGenre = new OptionalEnumField(this, Genre){
    override def displayName = "Secondary Genre"
  }

  def author = Bookstore.authors.lookup(authorId.value)
  //def publisher = TestSchema.publishers.lookup(publisherId)

  //def author = TestSchema.authors.where(a => a.id === authorId)

//  def publisher = Bookstore.publishers.where(p => p.id === publisherId)
  def publisher = Bookstore.publishers.lookup(publisherId.value)
}

object Book extends Book with MetaRecord[Book] {
  def list:Query[Book] = Bookstore.books.where(_.id.isNotNull)
}
