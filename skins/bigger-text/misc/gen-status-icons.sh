#!/bin/sh

#
#   Android Weather Notification.
#   Copyright (C) 2012 gelin, 2011 mihovilic
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

font=Droid-Sans-Mono-Regular

color_negative="#55e2ff"
color_positive="#ffe755"
color_zero="white"

xhdpi_params="50x50 45 drawable-xhdpi"
hdpi_params="38x38 35 drawable-hdpi"
mdpi_params="25x25 23 drawable"
ldpi_params="19x19 17 drawable-ldpi"

xhdpi_border_params="50x48 2 45 drawable-xhdpi"
hdpi_border_params="38x36 2 35 drawable-hdpi"
mdpi_border_params="25x24 1 23 drawable"
ldpi_border_params="19x18 1 17 drawable-ldpi"

gen_image() {
    img_size=$1
    point_size=$2
    res_folder=$3
    text=$4
    color=$5
    file_prefix=$6
    file_suffix=$7

    file="res/$res_folder/${file_prefix}_temp_${file_suffix}.png"
    echo "Generating $file"

    convert -size $img_size xc:transparent \
        -font $font \
        -gravity center -pointsize $point_size \
        -fill $color -stroke none -annotate 0 "$text" \
        $file
}

gen_bordered_image() {
    img_size=$1
    border_width=$2
    point_size=$3
    res_folder=$4
    text=$5
    color=$6
    file_prefix=$7
    file_suffix=$8

    file="res/$res_folder/${file_prefix}_temp_${file_suffix}.png"
    echo "Generating $file"

    convert -size $img_size xc:transparent \
        -font $font \
        -gravity center -pointsize $point_size \
        -fill $color -stroke none -annotate 0 "$text" \
        -background $color -gravity north -splice 0x${border_width} \
        $file
}

#-60 - -10
for t in $(seq 60 -1 10)
do
    gen_image $xhdpi_params "$t" "$color_negative" light minus_$t
    gen_image $hdpi_params "$t" "$color_negative" light minus_$t
    gen_image $mdpi_params "$t" "$color_negative" light minus_$t
    gen_image $ldpi_params "$t" "$color_negative" light minus_$t
done

#-9 - -1
for t in $(seq 9 -1 1)
do
    gen_image $xhdpi_params "$t" "$color_negative" light minus_$t
    gen_image $hdpi_params "$t" "$color_negative" light minus_$t
    gen_image $mdpi_params "$t" "$color_negative" light minus_$t
    gen_image $ldpi_params "$t" "$color_negative" light minus_$t
done

#0
    gen_image $xhdpi_params "0" "$color_zero" light 0
    gen_image $hdpi_params "0" "$color_zero" light 0
    gen_image $mdpi_params "0" "$color_zero" light 0
    gen_image $ldpi_params "0" "$color_zero" light 0

#1 - 9
for t in $(seq 1 9)
do
    gen_image $xhdpi_params "$t" "$color_positive" light plus_$t
    gen_image $hdpi_params "$t" "$color_positive" light plus_$t
    gen_image $mdpi_params "$t" "$color_positive" light plus_$t
    gen_image $ldpi_params "$t" "$color_positive" light plus_$t
done

#10 - 99
for t in $(seq 10 99)
do
    gen_image $xhdpi_params "$t" "$color_positive" light plus_$t
    gen_image $hdpi_params "$t" "$color_positive" light plus_$t
    gen_image $mdpi_params "$t" "$color_positive" light plus_$t
    gen_image $ldpi_params "$t" "$color_positive" light plus_$t
done

#100 - 130
for t in $(seq 0 30)
do
    text=$(printf %02d $t)
    gen_bordered_image $xhdpi_border_params "$text" "$color_positive" light plus_1$text
    gen_bordered_image $hdpi_border_params "$text" "$color_positive" light plus_1$text
    gen_bordered_image $mdpi_border_params "$text" "$color_positive" light plus_1$text
    gen_bordered_image $ldpi_border_params "$text" "$color_positive" light plus_1$text
done
