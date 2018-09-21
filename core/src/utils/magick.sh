#!/bin/bash
imagePath="/home/ravi/Documents/Dropbox/Artwork/Images/png/1024x1024.png"
outputPath="/home/ravi/Projects/Android-Studio/JauntyMarble/android/res"
echo $imagePath
convert $imagePath -resize 48x48 "$outputPath/drawable-mdpi/ic_launcher.png"
convert $imagePath -resize 72x72 "$outputPath/drawable-hdpi/ic_launcher.png"
convert $imagePath -resize 96x96 "$outputPath/drawable-xhdpi/ic_launcher.png"
convert $imagePath -resize 144x144 "$outputPath/drawable-xxhdpi/ic_launcher.png"
convert $imagePath -resize 192x192 "$outputPath/drawable-xxxhdpi/ic_launcher.png"

#copy the splash
convert /home/ravi/Documents/Dropbox/Artwork/Images/png/cslogo.png -resize 1024x1024 /home/ravi/Projects/Android-Studio/JauntyMarble/android/assets/images/splash.png


