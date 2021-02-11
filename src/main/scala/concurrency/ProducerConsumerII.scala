package concurrency

import scala.collection.mutable
import scala.util.Random

object ProducerConsumerII extends App {

  def producerConsumerWithLargeBuffer(): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    val consumer = new Thread(() => {
      val random = new Random()
      while(true){
        buffer.synchronized {
          if(buffer.isEmpty){
            println("[consumer] buffer is empty. i will wait...")
            buffer.wait()
          }

          // when code gets here, there must be at least one value now
          val x = buffer.dequeue()
          println(s"[consumer] I have consumed $x")

          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
      }
    })

    val producer = new Thread(() => {
      val random = new Random()
      var i = 0
      while(true){
        buffer.synchronized {
          if(buffer.size == capacity){
            println("[producer] buffer is full. i'll wait...")
            buffer.wait()
          }

          println(s"[producer] I have produced $i")
          buffer.enqueue(i)

          buffer.notify()

          i+=1
        }
        Thread.sleep(random.nextInt(500))
      }
    })

    consumer.start()
    producer.start()
  }

  producerConsumerWithLargeBuffer()

}
