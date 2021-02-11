package concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Random, Success, Try}

object FuturesExercises extends App {

    /*
    1. fulfill a future immediately with a value
    2. inSequence(fa, fb)
    3. first(fa, fb) => new future with the first value of the two futures
    4. last(fa, fb) => new future with the last value
    5. retryUntil[T](action: () => Future[T], condition: T => boolean): Future[T]
   */

  def instantFuture[T](value: T): Future[T] = Future {
    value
  }

  def inSequence[A, B](first: Future[A], second: Future[B]): Future[B] = {
    first.flatMap(_ => second)
  }

  def first[A](firstArg: Future[A], secondArg: Future[A]): Future[A] = {
    val aPromise = Promise[A]()

    firstArg.onComplete(aPromise.tryComplete)
    secondArg.onComplete(aPromise.tryComplete)

    aPromise.future
  }

  def last[A](firstArg: Future[A], secondArg: Future[A]): Future[A] = {
    val promise1 = Promise[A]()   // both futures will try to complete this - only the first one will really get to do that
    val promise2 = Promise[A]()   // the LAST future will complete this

    val checkAndComplete = (result: Try[A]) =>
      if(!promise1.tryComplete(result)) promise2.complete(result)

    firstArg.onComplete(checkAndComplete)
    secondArg.onComplete(checkAndComplete)

    promise2.future
  }



  val fast = Future({
    Thread.sleep(100)
    42
  })

  val slow = Future({
    Thread.sleep(300)
    555
  })

  first(fast, slow).foreach(f => println(s"first: $f"))
  last(fast, slow).foreach(f => println(s"last: $f"))

  Thread.sleep(1000)

  def retryUntil[A](action: () => Future[A], condition: A => Boolean): Future[A] = {
    action() filter (condition) recoverWith {
      case _ => retryUntil(action, condition)
    }
  }

  val random = new Random()
  val action = () => Future({
    Thread.sleep(100)
    val nextVal = random.nextInt(1000)
    println(s"generated $nextVal")
    nextVal
  })

  retryUntil(action, (x: Int) => x < 50) foreach(result => println(s"settled at $result"))
  Thread.sleep(20000)

}
