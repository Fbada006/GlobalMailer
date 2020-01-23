<p align="center">
<img src="https://i.imgur.com/KcKik6As.jpg" width="100"/>
</p>
<p align="center">
<a href="https://travis-ci.com/Fbada006/GlobalMailer">
<img src="https://travis-ci.com/Fbada006/GlobalMailer.svg?branch=master"/>
</a>
<a href="https://codecov.io/gh/Fbada006/GlobalMailer">
  <img src="https://codecov.io/gh/Fbada006/GlobalMailer/branch/master/graph/badge.svg" />
</a>
<a href="https://www.codacy.com/manual/Fbada006/GlobalMailer?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Fbada006/GlobalMailer&amp;utm_campaign=Badge_Grade"><img src="https://api.codacy.com/project/badge/Grade/ed8c9e3d5f5f43559399c4b9f0df7492"/>
</a>
</p>

# Global Mailer

This is an Android Application that i built with the Android Architecture components using the 
recommended MVVM pattern. It displays a list of news articles by parsing the [news api](https://newsapi.org/).
I have also published it on the Google PlayStore [here](https://play.google.com/store/apps/details?id=com.droidafricana.globalmail)

## Prerequisite

minSdkVersion -> 21

Gradle build system

Be sure to create a [firebase project](https://firebase.google.com/) so that you can get your own
***google-services.json*** file to take full advantage of features like crashlytics. It can run without
the file but it is better if you do. Head over to the api above and get your own API-KEY as well
although there is a test key provided already for convenience.


## TOC

- [Architecture](#architecture)
- [Flow](#flow)
- [Libraries](#libraries)
- [Extras](#extras)
- [Screenshots](#screenshots)

## Architecture

The App is not organized into multiple modules but follows the same principles of 
the Presentation, Domain, and Data Layers. 
The presentation layer handles the UI work with the logic contained in the **ViewModel**. 
The UI uses a L**iveData** object from the ViewModel and observes it using the **Observer Pattern**. 
A ListAdapter handles the actual displaying of the news. Data over the network is retrieved using
**retrofit** and **coroutines** to handle background work asynchronously. Additionally, note that
the ViewModel uses the **viewModelScope** to launch the couroutines while Fragments use the **viewLifeCycleOwner**
to observe data.
The data layer uses the recommended **Repository Pattern** to make the network calls and store the data using 
**Room DB**. This layer is also responsible for mapping the entities that eventually get displayed in the UI.

## Flow

 **Main News Screen**
 
  This screen is visible to the user once the app is launched and immediately shows the default general
  data. The user then has the option of changing the preferences for use across the entire app to make
  other calls. The pattern used ensures that the user will always get data once there is an initial successful 
  network call. Clicking on an item will launch a Chrome-based browser that lives within the app.
 
 **Search Screen**
 
 This screen is accessible from all the fragments except the settings one. It uses a SearchView to search data
 and display it. It also has its own ViewModel and RecyclerView. The network call is triggered once the user
 presses the search button because of the api limitations.

 
## Libraries

This app takes use of the following libraries:

- [Jetpack](https://developer.android.com/jetpack)🚀
  - [Viewmodel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Manage UI data to survive configuration changes and is lifecycle-aware
  
  - [Data Binding](https://developer.android.com/topic/libraries/data-binding) - Declaratively bind observable data to UI elements
  - [Navigation](https://developer.android.com/guide/navigation/) - Handle everything needed for in-app navigation
  - [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) - Manage your Android background jobs
  - [Room DB](https://developer.android.com/topic/libraries/architecture/room) - Fluent SQLite database access
  - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Notify views when underlying database changes
- [Retrofit](https://square.github.io/retrofit/) - type safe http client with coroutines support 

- [Moshi](https://github.com/square/moshi) - JSON Parser that plays nicely with Kotlin 
- [okhttp-logging-interceptor](https://github.com/square/okhttp/blob/master/okhttp-logging-interceptor/README.md) - logging HTTP request related data.
- [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines) - Library Support for coroutines
- [Material Design](https://material.io/develop/android/docs/getting-started/) - build awesome beautiful UIs.🔥🔥
- [Firebase](https://firebase.google.com/) - Backend As A Service for faster mobile development.
  - [Crashylitics](https://firebase.google.com/docs/crashlytics) - Provide Realtime crash reports from users end.
- [Like Button](https://github.com/jd-alexander/LikeButton) - Twitter's heart animation for Android
- [Lottie](https://github.com/airbnb/lottie-android) - Render awesome After Effects animations natively on Android and iOS, Web, and React Native
- [Chrome Custom Tabs](https://developer.chrome.com/multidevice/android/customtabs) - Allows an app to customize how Chrome looks and feels
- [Picasso](https://square.github.io/picasso/) - Hassle-free image loading


## Extras

#### CI-Pipeline

[Travis CI](https://travis-ci.com/) is used for Continuous Integration every time an update is made
to the repo. The configuration is in the ***.travis.yml*** file

#### Code Analysis and test coverage

This code uses [Codacy](https://www.codacy.com/) for analyszing the quality of the code
while [CodeCov](https://codecov.io/gh) shows the test coverage. Tests are on the way.


## Screenshots
![Imgur](https://i.imgur.com/9Gtq3Gt.jpg) ![Imgur](https://i.imgur.com/MdtDD5J.jpg)

## License

 ```
   Copyright 2020 Ferdinand Bada

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 ```

