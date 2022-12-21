# Courier-Application
Android application for customer-courier interaction. Was part of diploma project with server *(note that server is shutdown for now)*. Full project description available at [documentation](https://github.com/UnbiasedShelf/Courier-Application/blob/main/docs/docs_rus.pdf) (only in russian).

## Used technologies
- Kotlin
- Coroutines
- Jetpack Compose
- Room
- Retrofit

## Setup
- Obtain Google Maps Api Key and provide it as a value of **MAPS_API_KEY** variable in the *local.properties* file
- Change value of **BASE_URL** to your own server URL in the [NetworkService.kt](https://github.com/UnbiasedShelf/Courier-Application/blob/main/app/src/main/java/by/bstu/vs/stpms/courier_application/model/network/NetworkService.kt) file

## Screenshots
![Created Orders Screen](https://github.com/UnbiasedShelf/Courier-Application/blob/main/docs/screenshot1.png)
![History Screen](https://github.com/UnbiasedShelf/Courier-Application/blob/main/docs/screenshot2.png)
![Route for Order](https://github.com/UnbiasedShelf/Courier-Application/blob/main/docs/screenshot3.png)
![Stats on Profile Screen](https://github.com/UnbiasedShelf/Courier-Application/blob/main/docs/screenshot4.png)
![One of the tabs on Order Creating Screen](https://github.com/UnbiasedShelf/Courier-Application/blob/main/docs/screenshot5.png)
