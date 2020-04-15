# AndroidTemplate
<a href="http://jonathanfstewart.com:8090/viewType.html?buildTypeId=AndroidTemplate_Build&guest=1">
<img src="http://jonathanfstewart.com:8090/app/rest/builds/buildType:(id:AndroidTemplate_Build)/statusIcon"/>
</a>


# Android MVVM Template 

## This app was built following Google's suggestions for new Android projects

This is a template Android Kotlin project for getting started on new projects. It includes basic libraries and architecture. Currently it searches for GitHub repos.

This app follows the MVVM architecture described [here](https://developer.android.com/jetpack/docs/guide?gclid=CjwKCAjwvOHzBRBoEiwA48i6ArUkJ806jPQEcl1dXCM2Q2CRx_pyRXBzIgtFertJr5HJeEXpxNbbhRoC-Q0QAvD_BwE) and surmised in the image below

![MVVM Architecture Image](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)

The app allows users to search for github repos by keyword, and view repo and owner details

### To build using Android Studio:
  1. pull master (or develop if your feeling lucky
  1. import into android stuido
  1. wait for all the gradle magic and indexing to happen
  1. keep waiting be patient
  1. click the run arrow (that should step you thorugh the build process from there) 
  
### To build using command line
  1. pull master (or develop if your feeling lucky
  1. navigate to the project route directory 
  1. run `./gradlew assembleDebug`
  1. run `adb install app/build/outputs/apk/debug/app-debug.apk`
  
### Instructions for Use
   1. Open app
   1. Type a query into the search box at the top of the screen and hit enter
   1. Tap a result to see details about the repo and owner
   1. Check out the about tab for information on the libraries used

