# Backend Provider
A wrapper library for Retrofit. It can be used to handle the Backend API responses and map their value to the variables accordingly using JSON mapper or any other. By default, it contains the Http Interceptor which has a JSON mapper. However user can add a custom interceptor to handle the response and map them accordingly as well.

Latest Version : 1.0.0
--------------------------------

Include Backend Provider using Gradle
--------------------------------

In a Gradle Project, one would include Backend Provider in the project by adding 
in the build.gradle file of your app module

```groovy
dependencies {
compile 'com.sultan.utils:backendprovider:1.0.0'
  }
```
Add this in your project build.gradle `buildscript`:

```groovy
buildscript {
  repositories {
    jCenter()
   }
}
```

Include Backend Provider using Maven
-------------------------------

In a Maven project, one would include the runtime in the dependencies section
of your `pom.xml` (replacing `${backendprovider.version}` with the appropriate current
release version):

```xml
<dependency>
  <groupId>com.sultan.utils</groupId>
  <artifactId>backendprovider</artifactId>
  <version>{backendprovider.version}</version>
  <type>pom</type>
</dependency 
```



License
-------

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



 [1]: https://dl.bintray.com/sultan0902/BackendProvider
