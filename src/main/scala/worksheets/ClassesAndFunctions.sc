class Rational(x: Int, y: Int){
  require(y!=0, "denominator must be non-zero")

  def this(x: Int) = this(x, 1)

  private def gcd(a: Int, b: Int): Int = if(b==0) a else gcd(b, a%b)
  private val g = gcd(x, y)   // val keyword means g is calculated
                              // immediately, upon class initialisation

  def numer = x/g
  def denom = y/g

  def < (that: Rational) = numer * that.denom < that.numer * denom

  def max(that: Rational) = if (this < that) that else this

  def +(that: Rational) =
    new Rational(numer * that.denom + that.numer * denom ,
      denom * that.denom)

  def unary_- : Rational = new Rational(-numer, denom)

  def - (that: Rational) = this + -that

  override def toString() =
    numer + "/" + denom
}

var x = new Rational(1, 3)
var y = new Rational(5, 7)
var z = new Rational(3, 2)
x.numer
x.denom
x - y - z
y + y
x < y
x max y

new Rational(2)