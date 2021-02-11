package concurrency

import java.util.concurrent.Executors

object Intro extends App {

  /*
    interface Runnable { public void run() }
   */

  // JVM threads

  /*
  val runnable = new Runnable {
    override def run(): Unit = println("running this in parallel")
  }
  val aThread = new Thread(runnable)

  aThread.start()   // gives a signal to the JVM to start a JVM thread, which runs on top of an OS thread

  runnable.run()    // doesn't do anything in parallel!

  aThread.join()    // blocks until aThread finishes running

  val threadHello = new Thread(() => (1 to 5) foreach(x => println(s"hello $x")))
  val threadBye = new Thread(() => (1 to 5) foreach(x => println(s"bye $x")))
  threadHello.start()
  threadBye.start()
  // different runs produce different results!


  // executors
  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("something in the thread pool"))     // will be executed by one of the 10 threads in the pool

  pool.execute(() => {
    Thread.sleep(1000)
    println("done after sleeping 1 second")
  })

  pool.execute(() => {
    Thread.sleep(1000)
    println("another done after sleeping 1 second")
    Thread.sleep(1000)
    println("another done after sleeping 2 seconds")
  })

  //pool.shutdown()
  //pool.execute(() => println("should not execute since pool has already been shut"))  // throws an exception in the calling (main) thread

  pool.shutdownNow()


  def runInParallel = {
    var x = 0
    val thread1 = new Thread(() => {
      x = 1
    })
    val thread2 = new Thread(() =>{
      x = 2
    })

    thread1.start()
    thread2.start()
    println(x)
  }

  for (_ <- 1 to 10000) runInParallel

  class BankAccount(var amount: Int) {
    override def toString: String = "" + amount
  }

  def buy(account: BankAccount, thing: String, price: Int) = {
    account.amount -= price
    //println(s"I've bought $thing at $price. Now my account balance is ${account.amount}")
  }

  for(_ <- 1 to 10000){
    val account = new BankAccount(50000)
    val thread1 = new Thread(() => buy(account, "shoes", 3000))
    val thread2 = new Thread(() => buy(account, "iPhone 12", 4000))

    thread1.start()
    thread2.start()
    Thread.sleep(10)
    if(account.amount != 43000) {
      println(s"Aha! This bank account balance is ${account.amount}")
    }
  }

  // option 1 - use synchronized: no two threads can evaluate a synchronized block at the same time
  def buySafe(account: BankAccount, thing: String, price: Int) = {
    account.synchronized({
      account.amount -= price
      println(s"I've bought $thing at $price. Now my account balance is ${account.amount}")
    })
  }

  for(_ <- 1 to 10000){
    val account = new BankAccount(50000)
    val thread1 = new Thread(() => buySafe(account, "shoes", 3000))
    val thread2 = new Thread(() => buySafe(account, "iPhone 12", 4000))

    thread1.start()
    thread2.start()
    Thread.sleep(10)
    if(account.amount != 43000) {
      println(s"Aha! This bank account balance is ${account.amount}")
    }
  }

  // option 2 - use the @volatile annotation
  class BankAccount(@volatile var amount: Int) {
    override def toString: String = "" + amount
  }

  */

  /*
  Exercise - construct 50 "inception" threads
  Thread1 -> Thread2 -> Thread 3 -> ...
  println("hello from thread #3")
  in REVERSE order
   */
  def inceptionThreads(maxThreads: Int, i: Int = 1): Thread = new Thread(() => {
    if(i < maxThreads){
      val newThread = inceptionThreads(maxThreads, i+1)
      newThread.start()
      newThread.join()
    }
    println(s"Hello from thread $i")
  })
  inceptionThreads(50).start()

  var x = 0
  val threads = (1 to 100) map(_ => new Thread(() => x += 1))
  threads foreach(_.start())
  // what is the (i) biggest possible value for x? 100
  // (ii) smallest possible value for x ? 1
  threads.foreach(_.join())
  println(x)

  var message = ""
  val awesomeThread = new Thread(() => {
    Thread.sleep(1000)
    message = "Scala is awesome"
  })
  message = "Scala stinks!"
  awesomeThread.start()
  Thread.sleep(2000)
  println(message)
  // what is the value of message? almost always "Scala is awesome"
  // it it guaranteed? not always
  // sleeping does NOT guarantee the order of execution. do not use them to synchronize threads

}