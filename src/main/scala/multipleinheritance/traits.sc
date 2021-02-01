trait Base {
  def message: String
}

trait P extends Base {
  override def message = "I am P"
}

trait Q extends Base {
  override def message = "I am Q"
}

trait R extends Base {
  override def message = "I am R"
}

class A extends P with Q with R

(new A).message