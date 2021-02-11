package concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future, Promise}
import scala.util.Success

object Promises extends App {

  val promise = Promise[Int]()
  val futureOfPromise = promise.future

  futureOfPromise.onComplete({
    case Success(r) => println(s"[consumer] I've received $r")
  })

  val producer = new Thread(() => {
    println("[producer] crunching numbers...")
    Thread.sleep(1000)

    promise.success(42) // fulfilling the promise

    println("[producer] done")
  })

  producer.start()
  Thread.sleep(5000)

}
