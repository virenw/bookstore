package code.snippet

import code.model.{Author, Bookstore}
import net.liftweb.util.Helpers._
import java.text.DateFormat
import java.util.{Date, Calendar}
import net.liftweb.record.field.OptionalDateTimeField

class Authors {

  def list = "tr" #> Author.list.map( author => ".name" #> author.name & ".age" #> author.age.get
    & ".bday" #> birthdayToString(author.birthday)
  )

  private def birthdayToString(date: OptionalDateTimeField[Author]): String = {
    if(date.get.isDefined){
      DateFormat.getDateInstance(DateFormat.MEDIUM).format(date.get.get.getTime)
    }else {
      ""
    }
  }
}
