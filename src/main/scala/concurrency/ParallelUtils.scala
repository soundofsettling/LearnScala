package concurrency

import java.util.concurrent.ForkJoinPool
import scala.collection.parallel.CollectionConverters._
import scala.collection.parallel.{ForkJoinTaskSupport, Task, TaskSupport}
import scala.collection.parallel.immutable.ParVector

object ParallelUtils extends App {

  // parallel collections
  val parallelList = List(1, 2, 3).par

  val aParVector = ParVector[Int](1, 2, 3)

  def measure[T](operation: => T): Long = {
    val time = System.currentTimeMillis()
    operation
    System.currentTimeMillis() - time
  }

  val list = (1 to 1000000000).toList
  val serialTime = measure({
    list.map(_ + 1)
  })
  val parallelTime = measure({
    list.par.map(_ + 1)
  })

  println(s"serial time: $serialTime")
  println(s"parallel time: $parallelTime")



  // fold, reduce with non-associative operators - you won't want to parallelise these
  println(List(1,2,3).reduce(_ - _))          // yields correct result
  println(List(1,2,3).par.reduce(_ - _))      // yields unpredictable result


  var sum = 0
  List(1,2,3).par.foreach(sum += _)
  println(sum)      // not guaranteed to be 6. par spins off several threads, race conditions may happen



  // configuring parallel collections
  // tasksupport is a member of a parallel collection
  aParVector.tasksupport = new ForkJoinTaskSupport(new ForkJoinPool(2))
  /* alternatives to ForkJoinTaskSupport:
    - ThreadPoolTaskSupport - deprecated
    - ExecutionContextTaskSupport(EC)
    - you can also hand-craft your task support, like so:
   */
  aParVector.tasksupport = new TaskSupport {
    override def execute[R, Tp](fjtask: Task[R, Tp]): () => R = ???

    override def executeAndWaitResult[R, Tp](task: Task[R, Tp]): R = ???

    override def parallelismLevel: Int = ???

    override val environment: AnyRef = ???
  }

}
