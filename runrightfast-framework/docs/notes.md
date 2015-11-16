## JDK 8 ForkJoinPool outperformed Reactor WorkQueue backed ExecutionContext - 2015-11-15
- see test : co.runrightfast.zest.composites.services.akka.ActorSystemServicePerfTest
    - perf test : send 1 million messages to an actor which simply sends them back. 
    - hardware : Intel(R) Core(TM) i7-3770K CPU @ 3.50GHz  - 4 cores / 8 CPUs
    - perf test results : 2.6 sec
    - using Reactor WorkQueue (LMAX Disruptor based) dispatcher, the perf was about 2.5x slower
- based on these tests, reactor was taken out of the project. 