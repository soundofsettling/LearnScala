val n = 7

def isPrime(n: Int) =
  (2 until n) forall (n % _ != 0)

val sequences = (1 until n) map ( i => (1 until i) map (j => (i,j)) )

// (sequences foldRight Seq[Int]())(_ ++ _)

sequences.flatten

val sequences = (1 until n) flatMap ( i => (1 until i) map (j => (i,j))
  filter (pair => isPrime(pair._1 + pair._2)) )


for {
  i <- 1 until n
  j <- 1 until i
  if isPrime(i+j)
} yield (i, j)



def scalarProduct(xs: List[Double], ys: List[Double]): Double =
  (for( (x,y) <- xs zip ys ) yield x * y).sum


def forComprehension(M: Int, N: Int): Unit = {
  for {
    x <- 1 to M
    y <- 1 to N
  } yield (x,y)
}

forComprehension(9, 4)

for {
  x <- 1 to 9
  y <- 1 to 4
} yield (x,y)