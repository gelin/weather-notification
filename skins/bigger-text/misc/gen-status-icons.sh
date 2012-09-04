#!/bin/sh

#
#   Android Weather Notification.
#   Copyright (C) 2010 gelin, 2011 mihovilic
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

font=Droid-Sans-Bold

xhdpi_params="50x50 24 drawable-xhdpi"
hdpi_params="38x38 19 drawable-hdpi"
mdpi_params="25x25 12 drawable"
ldpi_params="19x19 9 drawable-ldpi"

gen_image() {
    img_size=$1
    point_size=$2
    res_folder=$3
    text=$4
    file_suffix=$5
    
    echo "Generating res/$res_folder/white_temp_$file_suffix.png"
    
    convert -size $img_size xc:transparent \
        -font $font \
        -gravity center -pointsize $point_size \
        -fill white -stroke none -annotate 0 "$text" \
        res/$res_folder/white_temp_$file_suffix.png
}

#-60 - -10
t=60
while [ $t -ge 10 ]
do
    gen_image $xhdpi_params "-$t°" minus_$t
    gen_image $hdpi_params "-$t°" minus_$t
    gen_image $mdpi_params "-$t°" minus_$t
    gen_image $ldpi_params "-$t°" minus_$t
    #echo $t
    t=$(expr $t - 1)
done

#-9 - -1
t=9
while [ $t -ge 1 ]
do
    gen_image $xhdpi_params "-$t°" minus_$t
    gen_image $hdpi_params "-$t°" minus_$t
    gen_image $mdpi_params "-$t°" minus_$t
    gen_image $ldpi_params "-$t°" minus_$t
    #echo $t
    t=$(expr $t - 1)
done

#0
    gen_image $xhdpi_params "0°" 0
    gen_image $hdpi_params "0°" 0
    gen_image $mdpi_params "0°" 0
    gen_image $ldpi_params "0°" 0

#1 - 9
t=1
while [ $t -le 9 ]
do
    gen_image $xhdpi_params "$t°" plus_$t
    gen_image $hdpi_params "$t°" plus_$t
    gen_image $mdpi_params "$t°" plus_$t
    gen_image $ldpi_params "$t°" plus_$t
    #echo $t
    t=$(expr $t + 1)
done

#10 - 99
t=10
while [ $t -le 99 ]
do
    gen_image $xhdpi_params "$t°" plus_$t
    gen_image $hdpi_params "$t°" plus_$t
    gen_image $mdpi_params "$t°" plus_$t
    gen_image $ldpi_params "$t°" plus_$t
    #echo $t
    t=$(expr $t + 1)
done

#100 - 130
t=100
while [ $t -le 130 ]
do
    gen_image $xhdpi_params "$t°" plus_$t
    gen_image $hdpi_params "$t°" plus_$t
    gen_image $mdpi_params "$t°" plus_$t
    gen_image $ldpi_params "$t°" plus_$t
    #echo $t
    t=$(expr $t + 1)
done
