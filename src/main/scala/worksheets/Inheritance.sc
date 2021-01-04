abstract class IntSet {
  def incl(x: Int): IntSet
  def contains(x: Int): Boolean
  def union(other: IntSet): IntSet
}

class NonEmpty(elem: Int, leftSubTree: IntSet, rightSubTree: IntSet) extends IntSet {
  def contains(x: Int): Boolean = {
    if (x < elem) leftSubTree contains x
    else if (x > elem) rightSubTree contains x
    else true
  }

  def incl(x: Int): IntSet = {
    if (x < elem) new NonEmpty(elem, leftSubTree incl x, rightSubTree)
    else if (x > elem) new NonEmpty(elem, leftSubTree, rightSubTree incl x)
    else this
  }

  def union(other: IntSet): IntSet =
    ((leftSubTree union rightSubTree) union other) incl elem

  override def toString = "{" + leftSubTree + elem + rightSubTree + "}"
}

class Empty extends IntSet {
  def contains(x: Int): Boolean = false
  def incl(x: Int): IntSet = new NonEmpty(x, new Empty, new Empty)
  def union(other: IntSet): IntSet = other

  override def toString = "."
}

object Empty extends IntSet {
  def contains(x: Int): Boolean = false
  def incl(x: Int): IntSet = new NonEmpty(x, Empty, Empty)
  def union(other: IntSet): IntSet = other
}

val t1 = new NonEmpty(3, new Empty, new Empty)
val t2 = t1 incl 4
