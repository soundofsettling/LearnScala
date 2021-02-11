package concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Random, Success, Try}

object FuturesAndPromises extends App {

  def calculateMeaningOfLife: Int = {
    Thread.sleep(2000)
    42
  }

  val aFuture = Future {
    calculateMeaningOfLife  // calculates the meaning of life on ANOTHER thread
  }

  println(aFuture.value)

  println("waiting on the future")
  aFuture.onComplete((t: Try[Int]) => {
    t match {
      case Success(meaning) => println(s"the meaning of life is $meaning")
      case Failure(e) => println(s"I have failed with $e")
    }
  })

  Thread.sleep(5000)

  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile) =
      println(s"${this.name} has poked ${anotherProfile.name}")
  }

  object SocialNetwork {
    // "database"
    val names = Map(
      "fb.id.1-zuck" -> "Mark",
      "fb.id.2-bill" -> "Bill",
      "fb.id.0-dummy" -> "Dummy"
    )
    val friends = Map(
      "fb.id.1-zuck" -> "fb.id.2-bill"
    )

    val random = new Random()

    // "API"
    def fetchProfile(id: String): Future[Profile] = Future {
      // simulate fetching from database
      Thread.sleep(random.nextInt(2000))
      Profile(id, names(id))
    }

    def fetchBestFriend(profile: Profile): Future[Profile] = Future {
      // simulate fetching from database
      Thread.sleep(random.nextInt(2000))
      val bfId = friends(profile.id)
      Profile(bfId, names(bfId))
    }

  }

  // client: Mark to poke Bill
  val mark = SocialNetwork.fetchProfile("fb.id.1-zuck")
  mark.onComplete {
    case Success(marksProfile) => {
      val bill = SocialNetwork.fetchBestFriend(marksProfile)
      bill.onComplete {
        case Success(billsProfile) => marksProfile.poke(billsProfile)
        case Failure(e) => e.printStackTrace()
      }
    }
    case Failure(ex) => ex.printStackTrace()
  }

  Thread.sleep(8000)


  // functional composition of futures
  // map, flatMap, filter

  val nameOnTheWall = mark.map(profile => profile.name)
  // map transforms a future of a given type into a future of a different type
  // if the original future fails with an exception, this map will fail with the same exception

  val marksBestFriend = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))

  val zucksBestFriendFiltered = marksBestFriend.filter(profile => profile.name.startsWith("Z"))

  for {
    mark <- SocialNetwork.fetchProfile("fb.id.1-zuck")
    bill <- SocialNetwork.fetchBestFriend(mark)
  } mark.poke(bill)

  Thread.sleep(1000)




  // recovery in futures
  val aProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recover({
    case e: Throwable => Profile("fb.id.0-dummy", "Forever alone")
  })

  val aFetchedProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recoverWith({
    case e: Throwable => SocialNetwork.fetchProfile("fb.id.0-dummy")
  })

  val fallBackResult = SocialNetwork.fetchProfile("unknown id").fallbackTo(SocialNetwork.fetchProfile("fb.id.0-dummy"))

}
