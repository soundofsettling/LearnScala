import scala.concurrent._, duration.Duration.Inf, java.util.concurrent.Executors
implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))

def fetchLinks(title: String): Seq[String] = {
  val resp = requests.get(
    "https://en.wikipedia.org/w/api.php",
    params = Seq(
      "action" -> "query",
      "titles" -> title,
      "prop" -> "links",
      "format" -> "json"
    ) )
  for{
    page <- ujson.read(resp)("query")("pages").obj.values.toSeq
    links <- page.obj.get("links").toSeq
    link <- links.arr
  } yield link("title").str
}

def fetchAllLinks(startTitle: String, depth: Int): Set[String] = {
  var seen = Set(startTitle)
  var current = Set(startTitle)
  for (i <- Range(0, depth)) {
    val nextTitleLists = for (title <- current) yield fetchLinks(title)
    current = nextTitleLists.flatten.filter(!seen.contains(_))
    seen = seen ++ current
  }
  seen
}

def fetchAllLinksParallel(startTitle: String, depth: Int): Set[String] = {
  var seen = Set(startTitle)
  var current = Set(startTitle)
  for (i <- Range(0, depth)) {
    val futures = for (title <- current) yield Future{ fetchLinks(title) }
    val nextTitleLists = futures.map(Await.result(_, Inf))
    current = nextTitleLists.flatten.filter(!seen.contains(_))
    seen = seen ++ current
  }
  seen
}

def fetchAllLinksRec(startTitle: String, depth: Int): Set[String] = {
  def rec(current: Set[String], seen: Set[String], recDepth: Int): Set[String] = {
    if (recDepth >= depth) seen
    else {
      val futures = for (title <- current) yield Future{ fetchLinks(title) }
      val nextTitles = futures.map(Await.result(_, Inf)).flatten
      rec(nextTitles.filter(!seen.contains(_)), seen ++ nextTitles, recDepth + 1)
    } }
  rec(Set(startTitle), Set(startTitle), 0)
}

val asyncHttpClient = org.asynchttpclient.Dsl.asyncHttpClient()
def fetchLinksAsync(title: String)(implicit ec: ExecutionContext): Future[Seq[String]] = {
  val p = Promise[String]
  val listenableFut = asyncHttpClient.prepareGet("https://en.wikipedia.org/w/api.php")
    .addQueryParam("action", "query").addQueryParam("titles", title)
    .addQueryParam("prop", "links").addQueryParam("format", "json")
    .execute()
  listenableFut.addListener(() => p.success(listenableFut.get().getResponseBody), null)
  val scalaFut: Future[String] = p.future
  scalaFut.map{ responseBody =>
    for{
      page <- ujson.read(responseBody)("query")("pages").obj.values.toSeq
      links <- page.obj.get("links").toSeq
      link <- links.arr
    } yield link("title").str
  }
}

def fetchAllLinksAsync(startTitle: String, depth: Int): Future[Set[String]] = {
  def rec(current: Set[String], seen: Set[String], recDepth: Int): Future[Set[String]] = {
    if (recDepth >= depth) Future.successful(seen)
    else {
      val futures = for (title <- current) yield fetchLinksAsync(title)
      Future.sequence(futures).map{nextTitleLists =>
        val nextTitles = nextTitleLists.flatten
        rec(nextTitles.filter(!seen.contains(_)), seen ++ nextTitles, recDepth + 1)
      }.flatten
    } }
  rec(Set(startTitle), Set(startTitle), 0)
}

fetchAllLinksAsync("Singapore", 3).foreach(println)