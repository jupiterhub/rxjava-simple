# Jupiter's conclusion
TBD

# rxjava-simple
Trying out ReactiveX.io implementation in Java.

There are other implementation of Reactive framework like AKKA, reactor, and the likes. Also the interfaces albeit in separate packages are introduced as part of Java 9

This project however uses JDK8 as JDK9 is not compatible with gradle at this point of writing


## Notable classes
* Flowable (has backpressure, meaning it can create another request)
* Observable (no backpressure, only dispose)
* Publisher, Subscriber, Subscription, Processor (has the publisher and subscriber)
* Completable - complete or error )
* Single - item or error
* Maybe - item, complete, or erro
* More details on Backpressure http://reactivex.io/documentation/operators/backpressure.html

#### Notable Operators (methods)
* .map(s -> s.toUpperCase) applies operation to a data