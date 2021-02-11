package concurrency

import java.util.concurrent.atomic.AtomicReference

object AtomicOpsAndReferences extends App {

  val atomic = new AtomicReference[Int](2)
  val currentVal = atomic.get()   // thread-safe read

  atomic.set(4)                   // thread-safe write
  // atomic reference can be mutated in-place

  atomic.getAndSet(5)  // thread-safe combo

  atomic.compareAndSet(10, 56)
  // the compare performs reference equality, not deep equality

  atomic.updateAndGet(_ + 1)      // update the value and return this updated val. thread-safe fn fun
  atomic.getAndUpdate(_ + 1)      // get this updated val THEN update the val

  atomic.accumulateAndGet(12, _ + _)    // thread-safe accumulation
  //atomic.getAndAccumulate()

}
