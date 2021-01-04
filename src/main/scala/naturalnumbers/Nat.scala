package naturalnumbers

abstract class Nat {
  def isZero: Boolean
  def predecessor: Nat
  def successor: Nat = new Succ(this)
  def + (that: Nat): Nat
  def - (that: Nat): Nat
}

object Zero extends Nat{
  def isZero = true

  override def predecessor: Nat = throw new Error("0.predecessor does not exist for natural numbers")

  def +(that: Nat) = that

  def -(that: Nat) = if(this.isZero) this else throw new Error("negative number")
}

class Succ(n: Nat) extends Nat{
  override def isZero: Boolean = false

  override def predecessor: Nat = n

  def +(that: Nat) = new Succ(n + that)

  def -(that: Nat) = if(that.isZero) this else n - that.predecessor
}