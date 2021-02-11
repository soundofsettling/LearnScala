package concurrency

object Deadlock extends App {

  case class Friend(name: String) {
    def bow(other: Friend) = {
      this.synchronized{
        println(s"$this: i am bowing to my friend $other")
        other.rise(this)
        println(s"$this: my friend $other has risen")
      }
    }

    def rise(other: Friend) = {
      this.synchronized{
        println(s"$this: i am rising to my friend $other")
      }
    }
  }

  val sam = Friend("Sam")
  val pierre = Friend("Pierre")

  new Thread(() => sam.bow(pierre)).start()
  new Thread(() => pierre.bow(sam)).start()

}
