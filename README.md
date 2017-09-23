# Jupiter's conclusion
This is a whole different way of coding compared to the normal Imperative programming. It takes time to get used to but is worth learning because of the benefits.

You no need to manage states as much like before and in turn the code is much cleaner and readable.

# rxjava-simple
Trying out ReactiveX.io implementation in Java.

There are other implementation of Reactive framework like AKKA, reactor, and the likes. Also the interfaces albeit in separate packages are introduced as part of Java 9

This project however uses JDK8 as JDK9 is not compatible with gradle at this point of writing

Javadoc @ http://reactivex.io/RxJava/2.x/javadoc

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
* .first (returns Single) -only takes the first element from the stream
* .firstElement (returns Maybe) - completes without an error
* .ignoreElements (returns Completable) - if you only care if it complets or fails
