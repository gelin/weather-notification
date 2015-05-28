#!/bin/sh

BASEPATH=$(dirname "$0")/../..

javadoc -d "$BASEPATH/gelin.bitbucket.org/weather-notification/javadoc" \
-sourcepath "$BASEPATH/weather-notification/libs/libweather/src:$BASEPATH/weather-notification/libs/libweatherskin/src:$BASEPATH/weather-notification/core/src" \
-subpackages "ru.gelin.android.weather" \
-source "1.6" \
-windowtitle "Weather API Specification" \
-group "libweather" "ru.gelin.android.weather:ru.gelin.android.weather.notification" \
-group "libweatherskin" "ru.gelin.android.weather.notification.skin:ru.gelin.android.weather.notification.skin.impl" \
-group "coreapp" "ru.gelin.android.weather.*" \
-linkoffline "http://d.android.com/reference/" "/home/gelin/opt/android-sdk/docs/reference/"

#hg add
#hg ci -m "Updated Javadoc"
#hg push --insecure
