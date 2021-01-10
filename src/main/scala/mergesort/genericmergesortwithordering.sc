import math.Ordering

def msort[T](xs: List[T])(implicit ord: Ordering[T]): List[T] = {
  val n = xs.length / 2
  if (n == 0) xs
  else {

    def merge(xs: List[T], ys: List[T]): List[T] = (xs, ys) match {
      case (Nil, ys) => ys
      case (xs, Nil) => xs
      case (x :: xs1, y :: ys1) =>
        if ( ord.lt(x, y) ) x :: merge(xs1, ys)
        else y :: merge(xs, ys1)
    }

    val (first, second) = xs splitAt n
    merge(msort(first), msort(second)(ord))
  }
}

val nums = List(2, -4, 5, 7, 1)
msort(nums)(Ordering.Int)
msort(nums)

val fruits = List("apple", "pineapple", "orange", "banana")
msort(fruits)(Ordering.String)
msort(fruits)