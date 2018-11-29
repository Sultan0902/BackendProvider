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
------------------------------------

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
Default Responses handled with Backend Provider
-----------------------------------------------

Backend Provider default interceptor can map API response on these two type of Response Model

1. GeneralResponse
-------------------

This type of reponse model only maps the Http Response `statusCode`, `message` and `success` boolean on the below `GeneralResponse` class.

```xml
class GeneralResponse constructor(
        @SerializedName("code")
        val code: Int = 400,
        @SerializedName("message")
        val message: String = "",
        @SerializedName("isSuccess")
        val isSuccess : Boolean = false){
}

```

2. DataResponse<T>
-------------------

This type of generic reponse model maps the Http Response `statusCode`, `message`, `success` boolean as well as the `data` present in the backend API response on the below `DataResponse` class.

Datatype of `data` is generic, so user could define the type of data when he is using this response model.

```xml
class DataResponse<T> constructor(
        @SerializedName("code")
        val code: Int = 400,
        @SerializedName("message")
        val message: String = "",
        @SerializedName("data")
        val data: T? = null,
        @SerializedName("isSuccess")
        val isSuccess : Boolean = false){
}
```
Adding BackendProvider in your code
-----------------------------------

Your can add `BackendProvider` in your code in a simple manner. First we need to set the values in the method and then we need to build the BackendProvider using `build()`. `Build()` will create the Retrofit object return you the object of API interface, which you can use to call the backend API.

For Example: 
Here we have `BackendApi` which is the API interface which contains the declaration of the backend API method which will be used to get the response. To do this we need to call the constructor of the BackendProvider providing the Context and Class as the parameter. 

We need to provide the `BaseUrl` as parameter in the `setBaseUrl(baseUrl: String)` method. This method is mandatory, other wise it will give you an error if you do not set the value of `BaseUrl`.

You can also set the logging level of the retrofit class by passing the value of Enum `BackendLogLevel` as a parameter in the `setLogLevel(backendLogLevel: BackendLogLevel)` method. The default value of loglevel is `BackendLogLevel.NONE`.

 ```xml
 val backendApi : BackendApi = BackendProvider<BackendApi>(this, BackendApi::class.java)
                 .setBaseUrl("https://www.watshood.com/")
                 .setLogLevel(BackendLogLevel.BASIC)
                 .build() as BackendApi
```


You can set the values for `backendConnectionTimeout`, `backendReadTimeout` and `backendWriteTimeout` using setter method. However, the default values of these Timeout is 30 seconds.

You can also se the values of `internetError` and `backendConnectivityError` using setters. THe default value of `internetError` is `No internet connection` and default value of `backendConnectivityError` is `Unable to connect. Some error occured`. 

You can provide your own custom values for these variables.

Handling Custom Response with Backend Provider
-----------------------------------------------

If the response model of backend API is custom and does not match with the default response model then user can define his own   `custom model class` and can map his response accordingly. In this case user need to provide a `custom call interceptor` as well to manage his response and map it to the specific model.

For Example: 
We have a response model `LocationDetailsResponseModel` which maps the response from google location API. 

```xml
class LocationDetailsResponseModel {
    @SerializedName("results")
    @Expose
    private String results = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusCode")
    private int statusCode;
    @SerializedName("success")
    private boolean success;
}
```

To map the response to this model, we need to provide a custom call interceptor. Here we have a custom call interceptor `locationApiCallsInterceptor`

```xml

val locationApiCallsInterceptor = Interceptor { chain ->
        val jsonUtility =  JsonUtitlity()
        try {
            val originalResponse = chain.proceed(chain.request())
            var response = jsonUtility.fromJson(originalResponse.body()!!.string(), JsonObject::class.java)
            Log.i("BackendApi Response", response.toString())

            if (response == null) {
                response = jsonUtility.fromJson("{}", JsonObject::class.java)
            }

            response.addProperty("statusCode", originalResponse.code())
            response.addProperty("success", originalResponse.isSuccessful() && response.get("status").toString().toUpperCase() == "OK" || response.get("status").toString().toUpperCase() == "\"OK\"")

            val responseBody = ResponseBody.create(originalResponse.body()!!.contentType(), response.toString())

             originalResponse.newBuilder().body(responseBody).build()
        } catch (e: Exception) {
            Log.e("BackendApi Error ", e.message)
            throw IOException("Some error occured")
        }
          
```

You can provide your custom call interceptor and set the it in the BackendProvider. 
In our case we the API interface which contains the backend call method declaration is `LocationApi`, so we can set our customer call interceptor like this:

```xml

val interceptorList : ArrayList<Interceptor> = ArrayList()
        interceptorList.add(locationApiCallsInterceptor)

val locationApi: LocationApi = BackendProvider<LocationApi>(this, LocationApi::class.java)
                 .setBaseUrl("https://maps.googleapis.com")
                 .setLogLevel(BackendLogLevel.BODY)
                 .build(interceptorList) as LocationApi

```

Here `interceptorList` is the list of custom interceptor you can pass as a parameter to the `build()` method. This list can contain the interceptors which can be used to handle and mapping the backend call APIs and responses.


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
