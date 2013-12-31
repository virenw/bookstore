package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._

import common._
import http._
import sitemap._
import Loc._
import net.liftmodules.JQueryModule
import net.liftweb.http.js.jquery._
import mapper._
import net.liftweb.http.Html5Properties
import net.liftweb.common.Full
import net.liftweb.squerylrecord.SquerylRecord
import org.squeryl.adapters.H2Adapter
import code.model.Bookstore
import javax.naming.InitialContext
import javax.sql.DataSource
import org.squeryl.Session
import org.h2.jdbcx.JdbcDataSource
import net.liftweb.squerylrecord.RecordTypeMode._


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    // where to search snippet
    LiftRules.addToPackages("code")

    DefaultConnectionIdentifier.jndiName = Props.get("jndi.name") openOr "jdbc/bookstoredb"
    //Database setup
    /*if (!DB.jndiJdbcConnAvailable_?) {
      val vendor =
        new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
          Props.get("db.url") openOr
            "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
          Props.get("db.user"), Props.get("db.password"))

      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)

      DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    }*/

    //Tell squeryl about the database adapter
    //(this is the deprecated way)
    //SquerylRecord.init(() => new H2Adapter)

    //following is the new way to setup the database adapter
    /*val ds = new JdbcDataSource()
    ds.setURL("jdbc:h2:lift_proto.db")
    ds.setUser("")
    ds.setPassword("")

    val ctx = new InitialContext()
    ctx.bind("jdbc/bookstoredb.db", ds)*/


    val ds = new InitialContext().lookup("java:comp/env/jdbc/bookstoredb").asInstanceOf[DataSource]
    SquerylRecord.initWithSquerylSession(Session.create(ds.getConnection(), new H2Adapter))


    /**
     * If this is development mode, then attempt to auto-generate the schema
     */
    /*if(Props.devMode)
      DB.use(DefaultConnectionIdentifier){ connection =>  Bookstore printDdl }*/
    inTransaction {
      Bookstore printDdl
    }

    //All Squeryl queries need to run in the context of a transaction
    //Configure a transaction around all HTTP requests
    S.addAround(new LoanWrapper {
      override def apply[T](f: => T): T = {
        val result = inTransaction {
          try {
            Right(f)
          } catch {
            case e: LiftFlowOfControlException => Left(e)
          }
        }

        result match {
          case Right(r) => r
          case Left(exception) => throw exception
        }
      }
    })

    // Build SiteMap
    val entries = List(
      Menu.i("Home") / "index", // the simple way to declare a menu

      Menu.i("Author") / "squeryl/add_author",

      // more complex because this menu allows anything in the
      // /static path to be visible
      Menu(Loc("Static", Link(List("static"), true, "/static/index"), 
	       "Static Content")))

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMap(SiteMap(entries:_*))

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)
    
    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))

    //Init the jQuery module, see http://liftweb.net/jquery for more information.
    LiftRules.jsArtifacts = JQueryArtifacts
    JQueryModule.InitParam.JQuery=JQueryModule.JQuery191
    JQueryModule.init()

  }
}
