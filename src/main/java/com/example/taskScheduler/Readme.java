/*


Implement an InMemory Task scheduler Library that supports these functionalities:
Submit a task and a time at which the task should be executed. --> schedule(task, time)
Schedule a task at a fixed interval --> scheduleAtFixedInterval(task, interval) - interval is in seconds
The first instance will trigger it immediately and the next execution would start after interval seconds of completion of the preceding execution.
If a task has an interval of 10 seconds and submitted at 2:00 pm then
It will be executed at 2:00 pm
Once the execution is completed + 10 seconds(interval) it will trigger the next execution and so on.
Expectations
The number of worker threads should be configurable and manage them effectively.
Code/Design should be modular and follow design patterns.
Don’t use any external/internal libs that provide the same functionality and core APIs should be used.
 */