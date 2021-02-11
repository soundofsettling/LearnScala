package concurrency

object ThreadCommunication extends App {

  /*
  the producer-consumer problem
  producer -> container containing a resource -> consumer
   */

  class SimpleContainer extends App {
    private var value: Int = 0

    def isEmpty: Boolean = value == 0

    // the "consuming" method
    def get = {
      val result = value
      value = 0
      result
    }

    // the "producing" method
    def set(newValue: Int) = value = newValue
  }

  def naiveProducerConsumer(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting...")
      while(container.isEmpty){
        println("[consumer] actively waiting...")
      }
      println(s"[consumer] I have consumed ${container.get}")
    })

    val producer = new Thread(() => {
      println("[producer] computing, waiting hard...")
      Thread.sleep(500)
      val value = 42
      println(s"[producer] I have produced after a lot of hard work, the value $value")
      container.set(value)
    })

    consumer.start()
    producer.start()
  }

  //naiveProducerConsumer()

  def smartProducerConsumer(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting...")
      container.synchronized{
        container.wait()
      }

      // when the code reaches this point, consumer must have some value
      println(s"[consumer] I have consumed " + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] working hard...")
      Thread.sleep(2000)
      val value = 42
      container.synchronized{
        println(s"[producer] i'm producing " + value)
        container.set(value)
        container.notify()
      }
    })

    consumer.start()
    producer.start()
  }

  smartProducerConsumer()

}