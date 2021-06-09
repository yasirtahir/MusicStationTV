![# # Let's develop Music Station Android app for Huawei Vision S (Smart TV)](https://github.com/yasirtahir/MusicStationTV/raw/main/images/HuaweiVisionS.png)
 

## Introduction


In this demo, we will develop Music Station android app for Huawei Vision S (Smart TV) devices. Huawei Vision S is a brand new large screen category and important part of Huawei's _**"1+8+N"**_ full-scenario services and Huawei Developer Ecosystem. Since, Huawei Vision S system architecture supports AOSP project framework, we used Leanback Library which offers extensive features for large screens to develop our user experience.

  
## Pre-Requisites

  


Before getting started, following are the requirements:  

1.  Android Studio (During this tutorial, we used version 4.1.1)  
    
2.  Android SDK 24 or later
    
3.  Huawei Vision S for testing


  ## Development

1. Add Leanback Libraries and tag in the launching activity of your application
2. Add necessary permissions. In this demo, we used Audio Visualizer which requires RECORD_AUDIO permission.
3. The banner icon is required when we are developing apps for TV. The size for Huawei Vision S banner icon is **496x280** which must be added in the _**drawable**_ folder.
4. We developed our main screen by extending from BrowserFragment of Leanback. The Cards are displayed with Presenter. 
5. PlayerActivity which contains two sub-layouts, Player View and Error View.
6. User can play, pause or skip the song using Remote Control of TV.

When user click Select button on the Remote Control of Huawei Vision S on any item, the player view is opened and MediaPlayer start loading the song url. Once the song is loaded, it starts playing and the icon of Play changes to Pause. The Audio Visualizer takes AudioSessionId to sync with audio song. By default, seekbar is selected for the user to skip the songs using Right and Left buttons of the Remote Control. The duration of the song updates based on seekbar position.

  

## Run the Application

  

Download this repo code. Build the project, run the application and test on Huawei Vision S.



![Loading Animation](https://github.com/yasirtahir/MusicStationTV/raw/main/images/splash.gif)


![Side Menu](https://github.com/yasirtahir/MusicStationTV/raw/main/images/side_menu.gif)


![Main Content Navigation](https://github.com/yasirtahir/MusicStationTV/raw/main/images/main_navigation.gif)


![Player View](https://github.com/yasirtahir/MusicStationTV/raw/main/images/player.gif)


## Point to Ponder

1.  You must use default Launcher if you are developing app for Huawei Vision S.
    
2.  Leanback Launcher is not supported by Huawei Vision S.
    
3.  If you have same code base for Mobile and Vision S devices, you can use TVUtils class to check at run-time about the device and offer functionalities based on it.
    
4.  Make sure to add all the permissions like ***RECORD_AUDIO*** and ***INTERNET***.
    
5.  Make sure to add run-time permissions check. In this article, we used 3rd party Permission Check library with custom Dialog if user deny any of the required permission.
    
6.  Always use animation libraries like **Lottie** or **ViewAnimator** to enhance UI/UX in your application.
    
7.  We used  **AudioVisualizer** library to bring Music feel on our Player UI.
    


## References

### Android TV Documentation:  

[https://developer.android.com/training/tv/start](https://developer.android.com/training/tv/start "https://developer.android.com/training/tv/start")

[https://developer.android.com/training/tv/start/layouts](https://developer.android.com/training/tv/start/layouts)

[https://developer.android.com/training/tv/start/controllers](https://developer.android.com/training/tv/start/controllers)

[https://developer.android.com/training/tv/start/navigation](https://developer.android.com/training/tv/start/navigation)

  

### Lottie Android Documentation:  

http://airbnb.io/lottie/#/android

  

### Github Code Link:  

[](https://github.com/yasirtahir/SpeechSearch)[https://github.com/yasirtahir/MusicStationTV](https://github.com/yasirtahir/MusicStationTV)
