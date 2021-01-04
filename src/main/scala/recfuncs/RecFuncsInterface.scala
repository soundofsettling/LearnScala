package recfuncs

trait RecFuncsInterface {
  def pascal(c: Int, r: Int): Int
  def balance(chars: List[Char]): Boolean
  def countChange(money: Int, coins: List[Int]): Int
}
