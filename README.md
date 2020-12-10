# MoviesApp

App developed in Kotlin under Android Studio, which displays a series of movies.
You can navigate to the details of these films and we will obtain a list of recommended films according to the selected film.
We can also mark or unmark any of these movies as a favorite. From our list of movies, browsing from our tabbar to favorite movies and deselecting it there or from the details of it by clicking on the heart-shaped icon, in the latter case it is found as a menu icon.
The lists contain a search bar at the top that will work by searching while we type a name or when we click on search.
As an extra functionality, the option to switch from vertical screen to horizontal screen has been added, in either of the two lists, so that you can see both the list of movies and that of favorites and interact with both by selecting or deselecting the favorite heart.

### Used libraries and explanation of them

#### Hilt
This library has been implemented to be able to make use of dependency injection. This library is an evolution of Dagger2 and provides us with these functions in many ways. In addition to adding some tools for ViewModels


    implementation 'com.google.dagger:hilt-android:2.28.1-alpha'
    implementation "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha01"
    kapt 'com.google.dagger:hilt-android-compiler:2.28.1-alpha'
    kapt "androidx.hilt:hilt-compiler:1.0.0-alpha01"

#### LifeCycle
This library has been implemented with the tools that accompany it and are necessary for the use of the LiveData elements, which allow us to create observers on elements of the viewmodel and adjust accordingly, thus obtaining a better sense of use for the user.

    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-common-java8:$lifecycle_version"
    implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'

#### Retrofit
To facilitate usp of the network calls, data collection, transformation to JSON and correct settings of this, we will use the following libraries.

    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.2.1'

#### Kotlin Corountines
To take advantage of the facilities of the Kotlin language even more, we have chosen to use "corountines", thus converting our app into asynchronous, for the use of network calls, databases, etc ...
In addition to having to implement them in the tests for their use.

    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_android_version"
    testImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.0'


#### Room
Due to the needs of the project, it is necessary to maintain a series of data on those favorite films, for this it is approved by the use of this library, which facilitates the use and maintenance of databases with labels and models

    implementation "androidx.room:room-runtime:$room_version"
    implementation "androidx.room:room-ktx:$room_version"
    kapt "androidx.room:room-compiler:$room_version"

#### Navigation
As one of the latest news that JetPack offers us, we have the Navigation component, to facilitate the development of the navigation of our App, controlling the flow, the return to the back, animations between fragments ...

    implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
    implementation "androidx.navigation:navigation-ui-ktx:$nav_version"

#### Glide
For the use of images and an easy loading of the same, we have used Glide, that through some simple guidelines and a URL, we can load images, forgetting a large part of their maintenance and always in a dynamic way.

    implementation 'com.github.bumptech.glide:glide:4.11.0'
    kapt 'com.github.bumptech.glide:compiler:4.11.0'


#### Test
Finally, a series of dependencies related to the tests have been implemented, so that such use is much faster and understandable. Like the Mockito tool that allows us to create a mock of our objects without having to declare it and through the declaration of situations we can dynamically assign data, or like Roboletric that has advanced functions in the use of Test.

    testImplementation 'org.mockito:mockito-core:3.1.0'
    testImplementation 'org.mockito:mockito-inline:3.1.0'
    testImplementation "com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0"
    testImplementation "androidx.arch.core:core-testing:2.1.0"
	testImplementation "org.robolectric:robolectric:4.3"
	testImplementation "androidx.test:runner:1.3.0"
    testImplementation "androidx.test.espresso:espresso-core:3.3.0"
    testImplementation 'com.linkedin.dexmaker:dexmaker-mockito:2.2.0'

Some other implemented libraries have been incorporated due to errors found in development and that are necessary to save them. Like the tool developed by Linkedin that corrects a series of problems in mocking

[![optiva_media_icon](https://media.glassdoor.com/sqll/725396/optiva-media-squarelogo-1484790118942.png "optiva_media_icon")](https://media.glassdoor.com/sqll/725396/optiva-media-squarelogo-1484790118942.png "optiva_media_icon")




