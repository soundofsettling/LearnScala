val nums = Vector(1, 2, 3, -88)

44 +: nums
nums :+ 55

nums.sum
nums.max

val numsArray = Array(1, 2, 3, -88)
numsArray map (x => x*2)

val aStringIsASequenceLikeStructure = "Hello World"
aStringIsASequenceLikeStructure filter (c => c.isUpper)

aStringIsASequenceLikeStructure exists (c => c.isUpper)
aStringIsASequenceLikeStructure forall (c => c.isUpper)

val pairs = List(1, 2, 3) zip aStringIsASequenceLikeStructure
pairs.unzip

aStringIsASequenceLikeStructure flatMap (c => List('.', c))


val r: Range = 1 until 5
val s: Range = 1 to 5
1 to 10 by 3
6 to 1 by -2

// to list all combinations of numbers x and y where
// x is drawn from 1..M and
// y is drawn from 1..N
def combinations(M: Int, N: Int) =
  (1 to M) flatMap (x => (1 to N) map (y => (x, y)))

combinations(3, 5)

def isPrimeNumber(n: Int): Boolean =
  (2 until n) forall (x => n%x != 0)

isPrimeNumber(27)
isPrimeNumber(37)

val aWord = "theCHAINsmokers"
aWord.toLowerCase.groupBy((x: Char) => x).mapValues(_.length).toList