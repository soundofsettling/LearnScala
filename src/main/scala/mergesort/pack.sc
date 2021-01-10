// pack consecutive duplicates of a list's elements into sublists
// e.g. pack(List("a", "a", "a", "b", "c", "c", "a"))
// should give List(List("a", "a", "a"), List("b"), List("c", "c"), List("a"))

def pack[T](xs: List[T]): List[List[T]] = xs match {
  case Nil => Nil
  case x :: xs1 => {
    val (first, rest) = xs span (elem => elem == x)
    first :: pack(rest)
  }
}

pack(List("a", "a", "a", "b", "c", "c", "a"))

// Using pack, write a function encode that produces the run-length encoding of a
// list. The idea is to encode n consecutive duplicates of an element x as a
// pair (x, n). For instance,
// encode(List("a", "a", "a", "b", "c", "c", "a"))
// should give
// List(("a", 3), ("b", 1), ("c", 2), ("a", 1))

def encode[T](xs: List[T]): List[(T, Int)] =
  pack(xs) map (elem => (elem.head, elem.length))

encode(List("a", "a", "a", "b", "c", "c", "a"))