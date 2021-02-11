package concurrency

import scala.collection.mutable
import scala.util.Random

object ProducerConsumerIII extends App {

  def multipleProducersAndConsumers(nConsumers: Int, nProducers: Int): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 20

    (1 to nConsumers) foreach(i => new Consumer(i, buffer).start())
    (1 to nConsumers) foreach(i => new Producer(i, buffer, capacity).start())
  }

  multipleProducersAndConsumers(3, 3)

  class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
    override def run(): Unit = {
      val random = new Random()
      while(true){
        buffer.synchronized {
          while(buffer.isEmpty){
            println(s"[consumer $id] buffer is empty. i will wait...")
            buffer.wait()
          }

          // when code gets here, there must be at least one value now
          val x = buffer.dequeue()
          println(s"[consumer] I have consumed $x")

          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
      }
    }
  }

  class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int) extends Thread {
    override def run(): Unit = {
      val random = new Random()
      var i = 0
      while(true){
        buffer.synchronized {
          while(buffer.size == capacity){
            println(s"[producer $id] buffer is full. i'll wait...")
            buffer.wait()
          }

          println(s"[producer $id] I have produced $i")
          buffer.enqueue(i)

          buffer.notify()

          i+=1
        }
        Thread.sleep(random.nextInt(500))
      }
    }
  }

}