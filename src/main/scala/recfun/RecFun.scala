package recfun

object RecFun extends RecFunInterface {

  def main(args: Array[String]): Unit = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(s"${pascal(col, row)} ")
      println()
    }
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if(c == 0 || c == r) 1
    else pascal(c-1, r-1) + pascal(c, r-1)
  }

  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean = {
    def balanced(chars: List[Char], numOfOpenBrackets: Int): Boolean =
      if(chars.isEmpty) numOfOpenBrackets == 0
      else {
        if(chars.head == '(') balanced(chars.tail, numOfOpenBrackets+1)
        else if(chars.head == ')') numOfOpenBrackets > 0 && balanced(chars.tail, numOfOpenBrackets-1)
        else balanced(chars.tail, numOfOpenBrackets)
      }
    balanced(chars, 0)
  }

  /**
   * Exercise 3
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    def loop(money: Int, coins: List[Int]): Int = {
      if(money < 0 || coins.isEmpty) 0
      else if(money == 0) 1
      else loop(money, coins.tail) + loop(money - coins.head, coins)
    }
    loop(money, coins)
  }
}
