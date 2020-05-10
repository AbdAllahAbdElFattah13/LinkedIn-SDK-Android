# LinkedIn-SDK-Android
[ ![Download](https://api.bintray.com/packages/abdallahabdelfattah13/maven/linkedin-sdk/images/download.svg?version=1.1.0) ](https://bintray.com/abdallahabdelfattah13/maven/linkedin-sdk/1.1.0/link)

A lightweight android library to implement Login with LinkedIn in your Android app, that supports both Activities and Fragments.

Inspired by [shantanu-deshmukh/LinkedIn-SDK-Android](https://github.com/shantanu-deshmukh/LinkedIn-SDK-Android)


Table of contents
=================
- [SDK Structure](#SDK-Structure)
- [Main changes over the original](#Main-changes-over-the-original)
- [Why this UnOfficial SDK?](#why-this-unofficial-sdk-)
- [Adding the SDK to your Project](#adding-the-sdk-to-your-project)
- [Usage](#usage)
  * [Authenticating](#authenticating)
    + [LinkedInUser Class](#linkedinuser-class)
- [Security](#security)
- [Contributing](#contributing)


Main changes over the original
========================
- [x] Add support for usage from fragments.
- [ ] Better error handling.
- [x] Kotlin-lize the SDK since now Kotlin is the main dev language for Android.

SDK Structure 
========================
The SDK follows the [clean architecture principles](https://five.agency/android-architecture-part-1-every-new-beginning-is-hard/), so it's mainly devided into use cases, data, presentation(MVVM) and ui layers. Each has its own package, with manual [dependancy injection](https://en.wikipedia.org/wiki/Dependency_injection) system to inject them when needed.

Why this UnOfficial SDK?
========================
* [Existing SDKs have been discontinued.](https://engineering.linkedin.com/blog/2018/12/developer-program-updates)
* [Official docs](https://developer.linkedin.com/docs/android-sdk-auth) on developer.linkedin.com are outdated. 

Adding the SDK to your Project
===============================
Just add the dependency to your app level `build.gradle` file

```gradle
dependencies {
    implementation 'com.AbdAllahAbdElFattah13:linkedinsdk:1.1.0'
}
```

> If you are getting `Failed to resolve` ERROR, make sure that Jcenter repository is added to your project level `build.gradle` file. This is done by default in recent versions of Android Studio.

Usage
=====

Authenticating
--------------

1. Add internet permission to your `AndroidManifest.xml` file if it's not already added.

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

2. Initiate Login Request. (You might want to do this on click of a login button)
    * From within fragments:
    ```Java
    LinkedInFromFragmentBuilder.getInstance(MainActivity.this)
            .setClientID("<YOUR_CLIENT_ID_HERE>")
            .setClientSecret("<YOUR_CLIENT_SECRET_HERE>")
            .setRedirectURI("<YOUR_REDIRECT_URL_HERE>")
            .authenticate(LINKEDIN_REQUEST_CODE);
    ```

    * From within activities:
    ```Java
    LinkedInFromActivityBuilder.getInstance(MainActivity.this)
            .setClientID("<YOUR_CLIENT_ID_HERE>")
            .setClientSecret("<YOUR_CLIENT_SECRET_HERE>")
            .setRedirectURI("<YOUR_REDIRECT_URL_HERE>")
            .authenticate(LINKEDIN_REQUEST_CODE);
    ```
> You can download the official Sign In with LinkedIn button images from [here](https://content.linkedin.com/content/dam/developer/branding/signin_with_linkedin-buttons.zip)

3. Handling Result: the sdk returns `LinkedInUser` object which contains the result data.

```java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LINKEDIN_REQUEST_CODE && data != null) {
            if (resultCode == RESULT_OK) {
                //Successfully signed in
                LinkedInUser user = data.getParcelableExtra("social_login");

                //acessing user info
                Log.i("LinkedInLogin", user.getFirstName());

            } else {

                if (data.getIntExtra("err_code", 0) == LinkedInBuilder.ERROR_USER_DENIED) {
                    //Handle : user denied access to account

                } else if (data.getIntExtra("err_code", 0) == LinkedInBuilder.ERROR_FAILED) {
                    
                    //Handle : Error in API : see logcat output for details
                    Log.e("LINKEDIN ERROR", data.getStringExtra("err_message"));
                }
            }
        }

    }

```


### LinkedInUser Class
|  Return       |  Method          | Description |
| ------------- |:-------------:|:-------------:|
| String    | `getId()` | Returns LinkedIn user ID |
| String    | `getEmail()`      | Returns users email (May return `null`)  |
| String    | `getFirstName()`      | Returns first name of the user|
| String    | `getLastName()`      | Returns last name of the user|
| String    | `getProfileUrl()`      | Returns profile url of the user|
| String    | `getAccessToken()`      | Returns access token that can be used to retrive data later. You might want to store it for later use.|
| long      | `getAccessTokenExpiry()`      | Expiry timestamp of the access token in Millisecond. |



Security
========
To protect against [CSRF](https://en.wikipedia.org/wiki/Cross-site_request_forgery) during authorization, the sdk uses a 16 character token by default. If you want to use your own CSRF token, then use the `setState` method of the `LinkedInBuilder` class.

* From within fragments:
```Java
LinkedInFromFragmentBuilder.getInstance(MainActivity.this)
        .setClientID("<YOUR_CLIENT_ID_HERE>")
        .setClientSecret("<YOUR_CLIENT_SECRET_HERE>")
        .setRedirectURI("<YOUR_REDIRECT_URL_HERE>")
        .setState("<YOUR_CSRF_TOKEN_HERE>")
        .authenticate(LINKEDIN_REQUEST_CODE);
```

* From within activities:
```Java
LinkedInFromActivityBuilder.getInstance(MainActivity.this)
        .setClientID("<YOUR_CLIENT_ID_HERE>")
        .setClientSecret("<YOUR_CLIENT_SECRET_HERE>")
        .setRedirectURI("<YOUR_REDIRECT_URL_HERE>")
        .setState("<YOUR_CSRF_TOKEN_HERE>")
        .authenticate(LINKEDIN_REQUEST_CODE);
```

Contributing
============
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.