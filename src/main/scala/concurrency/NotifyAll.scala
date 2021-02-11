package concurrency

object NotifyAll extends  App {

  val bell = new Object

  (1 to 10).foreach(i => new Thread(() => {
    bell.synchronized {
      println(s"[thread $i] waiting...")
      bell.wait()
      println(s"[thread $i] hooray!")
    }
  }).start())

  new Thread(() => {
    Thread.sleep(2000)
    println("[announcer] rock and roll!")
    bell.synchronized {
      bell.notify()
      //bell.notifyAll()
    }
  }).start()



}
