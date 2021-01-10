def squareList(xs: List[Int]): List[Int] =
  xs match {
    case Nil => xs
    case y :: ys => y * y :: squareList(ys)
  }

def squareList2(xs: List[Int]): List[Int] =
  xs map ((x: Int) => x * x)

val nums = List(2, -4, 5, 7, 1)
squareList(nums)
squareList2(nums)

nums filter (x => x > 0)
nums filterNot (x => x > 0)
nums partition (x => x > 0)

nums takeWhile (x => x > 0)
nums dropWhile (x => x > 0)
nums span (x => x > 0)