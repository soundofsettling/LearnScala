def isPrime(n: Int): Boolean = (2 until n) forall (d => n%d != 0)
isPrime(8)
isPrime(7)
isPrime(121)