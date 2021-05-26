# RecycleMania
An interactive educational recycling game. RecycleMania uses a barcode scanner to automatically categorize items, give feedback on how to recycle them, and rewards the user points in order to incentivize daily recycling. The app features a manual categorization option to allow the user to earn extra points for expanding our public dataset on recyclable items. 

## Mockup
https://www.figma.com/proto/Og3jNiLWnn4bgpGxlFuUjl/Recycle-Mania?node-id=14%3A53&scaling=min-zoom

## Technology
- Upcitemdb for UPC barcode lookup
    - https://www.upcitemdb.com/ 
    - Currently using the trial for development
    - 100 requests per day for the trial
- okHttpClient for http requests 
    - https://square.github.io/okhttp/
- Firebase Firestore for the database
    - https://firebase.google.com/docs/firestore
- Fragments for list views
    - https://developer.android.com/guide/fragments
    - Used for the manual categorization screens 

## Important Files
- src/main/java files - MainActivity.java where the homescreen starts
- AndroidManifest.xml - where permissions need to be declared
- src/main/res/layout - these xml files are where UI elements can be made / edited

## Testing Setup
- Tools > AVD Manager > Create new virtual device
- For consistency's sake create a Pixel 3, API 28 
- Otherwise can enable developer mode on Android phone and connect via USB
