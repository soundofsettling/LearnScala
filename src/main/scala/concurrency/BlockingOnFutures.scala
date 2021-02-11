package concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.duration.DurationInt

object BlockingOnFutures extends App {

  case class User(name: String)
  case class Transaction(sender: String, receiver: String, amount: Double, status: String)

  object BankingApp {
    val name = "ASD Bank"

    def fetchUser(name: String): Future[User] = Future({
      Thread.sleep(1000)    // simulate fetching from DB
      User(name)
    })

    def createTransaction(user: User, merchantName: String, amount: Double): Future[Transaction] = Future({
      Thread.sleep(2000)    // simulate fetching from DB
      Transaction(user.name, merchantName, amount, "Success")
    })

    def purchase(username: String, item: String, merchantName: String, price: Double): String = {
      // fetch the user from DB
      // create the txn
      // WAIT for the txn to finish!
      val transactionFuture = for {
        user <- fetchUser(username)
        transaction <- createTransaction(user, merchantName, price)
      } yield transaction.status

      Await.result(transactionFuture, 10.seconds)
    }

  }

  println( BankingApp.purchase("Ben", "iPhone 12", "QQQ Shop", 3000) )

}
