#!/bin/sh

#
#   Android Weather Notification.
#   Copyright (C) 2010  Denis Nelubin aka Gelin
#   
#   This program is free software; you can redistribute it and/or modify
#   it under the terms of the GNU General Public License as published by
#   the Free Software Foundation; either version 2 of the License, or
#   (at your option) any later version.
#   
#   This program is distributed in the hope that it will be useful,
#   but WITHOUT ANY WARRANTY; without even the implied warranty of
#   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#   GNU General Public License for more details.
#   
#   You should have received a copy of the GNU General Public License
#   along with this program; if not, write to the Free Software
#   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
#   
#   http://gelin.ru
#   mailto:den@gelin.ru
#   

#-60 - -10
t=60
while [ $t -ge 10 ]
do
    convert -size 25x25 xc:transparent \
        -font Liberation-Sans-Bold \
        -gravity center -pointsize 12 \
        -fill white -stroke none -annotate 0 "-$t°" \
        res/drawable/white_temp_minus_$t.png
    convert -size 25x25 xc:transparent \
        -font Liberation-Sans-Bold \
        -gravity center -pointsize 12 \
        -fill black -stroke none -annotate 0 "-$t°" \
        res/drawable/black_temp_minus_$t.png
    #echo $t
    t=$(expr $t - 1)
done

#-9 - -1
t=9
while [ $t -ge 1 ]
do
    convert -size 25x25 xc:transparent \
        -font Liberation-Sans-Bold \
        -gravity center -pointsize 12 \
        -fill white -stroke none -annotate 0 "-$t°" \
        res/drawable/white_temp_minus_$t.png
    convert -size 25x25 xc:transparent \
        -font Liberation-Sans-Bold \
        -gravity center -pointsize 12 \
        -fill black -stroke none -annotate 0 "-$t°" \
        res/drawable/black_temp_minus_$t.png
    #echo $t
    t=$(expr $t - 1)
done

#0
    convert -size 25x25 xc:transparent \
        -font Liberation-Sans-Bold \
        -gravity center -pointsize 12 \
        -fill white -stroke none -annotate 0 "0°" \
        res/drawable/white_temp_0.png
    convert -size 25x25 xc:transparent \
        -font Liberation-Sans-Bold \
        -gravity center -pointsize 12 \
        -fill black -stroke none -annotate 0 "0°" \
        res/drawable/black_temp_0.png
        
#1 - 9
t=1
while [ $t -le 9 ]
do
    convert -size 25x25 xc:transparent \
        -font Liberation-Sans-Bold \
        -gravity center -pointsize 12 \
        -fill white -stroke none -annotate 0 "$t°" \
        res/drawable/white_temp_plus_$t.png
    convert -size 25x25 xc:transparent \
        -font Liberation-Sans-Bold \
        -gravity center -pointsize 12 \
        -fill black -stroke none -annotate 0 "$t°" \
        res/drawable/black_temp_plus_$t.png
    #echo $t
    t=$(expr $t + 1)
done

#10 - 99
t=10
while [ $t -le 99 ]
do
    convert -size 25x25 xc:transparent \
        -font Liberation-Sans-Bold \
        -gravity center -pointsize 12 \
        -fill white -stroke none -annotate 0 "$t°" \
        res/drawable/white_temp_plus_$t.png
    convert -size 25x25 xc:transparent \
        -font Liberation-Sans-Bold \
        -gravity center -pointsize 12 \
        -fill black -stroke none -annotate 0 "$t°" \
        res/drawable/black_temp_plus_$t.png
    #echo $t
    t=$(expr $t + 1)
done

#100 - 130
t=100
while [ $t -le 130 ]
do
    convert -size 25x25 xc:transparent \
        -font Liberation-Sans-Bold \
        -gravity center -pointsize 12 \
        -fill white -stroke none -annotate 0 "$t°" \
        res/drawable/white_temp_plus_$t.png
    convert -size 25x25 xc:transparent \
        -font Liberation-Sans-Bold \
        -gravity center -pointsize 12 \
        -fill black -stroke none -annotate 0 "$t°" \
        res/drawable/black_temp_plus_$t.png
    #echo $t
    t=$(expr $t + 1)
done
