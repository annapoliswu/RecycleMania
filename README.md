# RecycleMania
An interactive educational recycling game.

# Important files
- src/main/java files - MainActivity.java 
- AndroidManifest.xml is where permissions need to be declared
- src/main/res/layout - these xml files are where UI elements can be made / edited

# Testing Setup
- Tools > AVD Manager > Create new virtual device
- For consistency's sake create a Pixel 3, API 28 
- Otherwise can enable developer mode on Android phone and connect via USB

# Barcode scanning 
- Currently using trial of upcitemdb for upc code lookup
    - https://www.upcitemdb.com/ 
    - 100 requests a day for now
- okHttpClient for http requests 
    - https://square.github.io/okhttp/
    -