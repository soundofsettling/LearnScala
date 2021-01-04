def last[T](xs: List[T]): T = xs match {
  case List() => throw new Error("last of empty list")
  case List(x) => x
  case y :: ys => last(ys)
}

def init[T](xs: List[T]): List[T] = xs match {
  case List() => throw new Error("last of empty list")
  case List(x) => List(x)
  case y :: ys => y :: init(ys)
}

def concat[T](list1: List[T], list2: List[T]): List[T] = list1 match {
  case List() => list2
  case z :: zs => z :: concat(zs, list2)
}

def reverse[T](list1: List[T]): List[T] = list1 match {
  case List() => List()
  case z :: zs => reverse(zs) ++ List(z)
}

def removeAt[T](n: Int, xs: List[T]) = (xs take n) ::: (xs drop (n+1))

// def flatten(xs: List[Any]): List[Any] = ???