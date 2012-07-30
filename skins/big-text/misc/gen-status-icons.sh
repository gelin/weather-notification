#!/bin/sh

#
#   Android Weather Notification.
#   Copyright (C) 2012 gelin
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

font=Droid-Sans-Regular

xhdpi_params="50x50 48 drawable-xhdpi"
hdpi_params="38x38 36 drawable-hdpi"
mdpi_params="25x25 24 drawable"
ldpi_params="19x19 18 drawable-ldpi"

gen_image() {
    img_size=$1
    point_size=$2
    res_folder=$3
    text=$4
    file_suffix=$5
    color=$6

    file=res/$res_folder/u$file_suffix.png
    echo "Generating $file"
    
    convert -size $img_size xc:transparent \
        -font $font \
        -gravity center -pointsize $point_size \
        -fill $color -stroke none -annotate 0 "$text" \
        $file
}

chars="0 1 2 3 4 5 6 7 8 9 ° + -"

for char in $chars
do
    name=$(echo -n $char | uniname -cbegn 2>/dev/null | tail -1 | tr -d " " | tr ABCDEF abcdef)
    name=${name#00}
    #echo $name
    gen_image $xhdpi_params "$char" ${name}_white white
    gen_image $hdpi_params "$char" ${name}_white white
    gen_image $mdpi_params "$char" ${name}_white white
    gen_image $ldpi_params "$char" ${name}_white white
done