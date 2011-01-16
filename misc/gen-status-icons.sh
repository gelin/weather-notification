#!/bin/sh

#-60 - -10
t=60
while [ $t -ge 10 ]
do
    convert -size 200x25 xc:transparent \
        -font Liberation-Sans-Bold \
        -gravity center -pointsize 22 \
        -fill white -stroke none -annotate 0 "-$t°" \
        -trim +repage -bordercolor transparent -border 1x5 \
        res/drawable/temp_minus_$t.png
    #echo $t
    t=$(expr $t - 1)
done

#-9 - -1
t=9
while [ $t -ge 1 ]
do
    convert -size 200x25 xc:transparent \
        -font Liberation-Sans-Bold \
        -gravity center -pointsize 22 \
        -fill white -stroke none -annotate 0 "-$t°" \
        -trim +repage -bordercolor transparent -border 1x5 \
        res/drawable/temp_minus_$t.png
    #echo $t
    t=$(expr $t - 1)
done

#0
    convert -size 200x25 xc:transparent \
        -font Liberation-Sans-Bold \
        -gravity center -pointsize 22 \
        -fill white -stroke none -annotate 0 "0°" \
        -trim +repage -bordercolor transparent -border 1x5 \
        res/drawable/temp_0.png

#1 - 9
t=1
while [ $t -le 9 ]
do
    convert -size 200x25 xc:transparent \
        -font Liberation-Sans-Bold \
        -gravity center -pointsize 22 \
        -fill white -stroke none -annotate 0 "+$t°" \
        -trim +repage -bordercolor transparent -border 1x5 \
        res/drawable/temp_plus_$t.png
    #echo $t
    t=$(expr $t + 1)
done

#10 - 99
t=10
while [ $t -le 99 ]
do
    convert -size 200x25 xc:transparent \
        -font Liberation-Sans-Bold \
        -gravity center -pointsize 22 \
        -fill white -stroke none -annotate 0 "+$t°" \
        -trim +repage -bordercolor transparent -border 1x5 \
        res/drawable/temp_plus_$t.png
    #echo $t
    t=$(expr $t + 1)
done

#100 - 130
t=100
while [ $t -le 130 ]
do
    convert -size 200x25 xc:transparent \
        -font Liberation-Sans-Bold \
        -gravity center -pointsize 22 \
        -fill white -stroke none -annotate 0 "+$t°" \
        -trim +repage -bordercolor transparent -border 1x5 \
        res/drawable/temp_plus_$t.png
    #echo $t
    t=$(expr $t + 1)
done
