#Coordinator 
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Coordinator-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1656) ![Maven Central](https://img.shields.io/maven-central/v/me.panavtec/coordinator.svg)
 
Coordinator is a Java library that is used to coordinate various asynchronous actions. Usually we found cases that you need to check if other thread is completed to execute an action, coordinator solves this problem without using flags.

![Logo](art/coordinator.png)

##Importing to your project
Add this dependency to your build.gradle file:

```java
dependencies {
    compile 'me.panavtec:coordinator:{Lib version, see the mvn central badge}'
    provided 'me.panavtec:coordinator-compiler:{Lib version, see the mvn central badge}'
}
```

##Basic usage

Coordinator can be used with dependency injection and without. Basically it needs a CompletedAction and a set of actions to work. The completedAction is a Runnable that runs autommatically when all cations are complete. The list of actions are just integers and are used as ids to let know coordinator what actions are completed.

With Annotations:

```java
@Actions({ AN_ACTION, ANOTHER_ACTION }) Coordinator coordinator;
@CoordinatorComplete Runnable coordinatorComplete;

public Constructor() {
	Coordinator.inject(this);
}

public void someAsyncMethod() {
	coordinator.completeAction(AN_ACTION);
}

public void someAsyncMethod2() {
	coordinator.completeAction(ANOTHER_ACTION);
}

```

And coordinator autommatically runs the runnable when the 2 actions are complete. We can annotate methods instead of having Runnable members in your class:

```java
@CoordinatorComplete public void someAsyncMethod() {
}
```

Do you need some special action when and actions is triggered? Here is how to do it:
```java
@CoordinatedAction(action = AN_ACTION) Runnable completeAction;
```

With this, the "completeAction" runnable is called when you call "coordinator.completeAction". Also you can annotate a method instead a class member.

Need more than 1 coordinator for a class? No problem! Just set a Id to coordinator and actions:

```java
@Actions(coordinatorId = 2, value = { AN_ACTION, ANOTHER_ACTION }) Coordinator coordinator;
@CoordinatorComplete(coordinatorId = 2) Runnable coordinatorComplete;
```

Don't like annotations? Don't worry! You can instantiate Coordinator manually:

```java
new Coordinator(new Runnable() {
	@Override public void run() {
		launchMyAwesomeAction();
	}
}, AN_ACTION, ANOTHER_ACTION);
```

Developed by
============
Christian Panadero Martinez - <a href="http://panavtec.me">http://panavtec.me</a> - <a href="https://twitter.com/panavtec">@PaNaVTEC</a>

License
=======

    Copyright 2015 Christian Panadero Martinez

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
