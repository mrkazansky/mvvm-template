# Tokoin challenge

- Use https://newsapi.org/ as data source
- Application must be written in Kotlin OR Swift
-  Application must be written in MVVM and MVP, and write a test case.
- Use any framework and library that you know and understand
- Application must have this feature:
    - News List with image
    - News detail with image
    - Link to open original news
    - Have 3 tab view at home and it will show list about:
        - Top Headline news with image
        - Custom news based on user preferences (user must be presented with keyword selection from: bitcoin, apple, earthquake, animal. User can only choose one keyword)
        - Profile
- User can register with username at profile and data (user preferences) will be saved on local storage

#### Build and run
##### Run
``
./gradlew assembleDebug
``
``
./gradlew installDebug
``

##### Test
``
./gradlew createDebugCoverageReport
``

Note
```
https://developer.android.com/training/testing/espresso/setup#set-up-environment
```

#### Project structure

###### App

- common: contains common usefule feature & function, such as md5
- data: Data layer
    - database: Contains local database class such as DAO, Entity
    - exception: Contains custom exception
    - models: Contains model for data layer
    - network : Contains network class such as Retrofit service
    - reopository: Contains Repository for database, network
- di: Contains Denpendi Injection module configuration 
- usecase: Contain Bussiness logic
- presentation: Contains UI implement
    - authenticator: contains Authentication service
    - base: contains Base class, extension & inteface
    - ui: contains UI & view model
###### Unit test
- common: Contain Unit test for common class such as Utils class
- data: Contain Unit test for repository classs
- di: Contain Dependency Injection module for unit testing purpose, such as mocking network
- presentaion: Contain Unit test for view model
- usecase: Contain Unit test for use case
###### Instrument test
- common: Contain Instrument test for common class such as AuthUtils
- data: Contain Instrument test for room database
- di : Contain Dependency Injection module for unit testing purpose
- helpers: Contain custom matcher to make testing easier
- presentation: Contain instrument test for UI such as Fragment

##### what could be improved
- Using Compose Jetpack to make declarative UI

