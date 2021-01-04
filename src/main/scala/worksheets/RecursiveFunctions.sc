import scala.math.abs

def and(x: Boolean, y: => Boolean) = if(x) y else false

def loop(): Boolean = loop

and(true, false)

and(true, true)

and(false, loop)

def squareRoot(x: Double) : Double = {
  def isGoodEnough(guess: Double): Boolean =
    abs(guess*guess - x)/x < 0.001

  def improve(guess: Double): Double =
    (guess + x/guess)/2

  def squareRootEstimate(guess: Double): Double =
    if( isGoodEnough(guess) ) guess
    else squareRootEstimate( improve(guess) )

  squareRootEstimate(1.0)
}

def tailRecursiveFactorial(x: Int): Int ={
  def loop(accumulator: Int, x: Int): Int =
    if(x == 0) accumulator else loop(accumulator*x, x-1)
  loop(1, x)
}

tailRecursiveFactorial(4)

squareRoot(2)
squareRoot(4)

def sum(f: Int => Int, a: Int, b: Int): Int = {
  def loop(a: Int, acc: Int): Int = {
    if (a>b) acc
    else loop(a+1, f(a) + acc)
  }
  loop(a, 0)
}

sum((x: Int) => x , 1, 7)


def product(f: Int => Int)(a: Int, b: Int): Int =
  if (a>b) 1
  else f(a) * product(f)(a+1, b)
product(x => x * x)(3, 5)

def factorial(n: Int) = product(x => x)(1, n)
factorial(4)

def mapReduce(f: Int => Int, combine: (Int, Int) => Int, unitValue: Int)(a: Int, b: Int): Int =
  if(a > b) unitValue
  else combine(  f(a), mapReduce(f, combine, unitValue)(a+1, b)  )

def productGeneralised(f: Int => Int)(a: Int, b: Int): Int = mapReduce(f, (x,y) => x*y, 1)(a, b)
productGeneralised(x => x * x)(3, 5)

val tolerance = 0.0001
def isCloseEnough(x: Double, y: Double) =
  abs((x-y)/x) / x < tolerance

def fixedPoint(f: Double => Double)(firstGuess: Double) = {
  def iterate(guess: Double): Double = {
    println("guess is " + guess)
    val next = f(guess)
    if(isCloseEnough(guess, next)) next else iterate(next)
  }
  iterate(firstGuess)
}

fixedPoint(x => 1 + x/2)(1)
def sqrtByFixedPoint(x: Double) = fixedPoint(y => (y + x/y)/2)(1.0)

sqrtByFixedPoint(25)

def averageDamp(f: Double => Double)(x: Double) = (x + f(x))/2

def squareRootAverageDump(x: Double) = fixedPoint(averageDamp(y => x/y))(1)

squareRootAverageDump(25)