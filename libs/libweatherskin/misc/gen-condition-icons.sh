#!/bin/sh

#
#   Android Weather Notification.
#   Copyright (C) 2014 gelin
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

xhdpi_params_24="48x48 drawable-xhdpi"
hdpi_params_24="36x36 drawable-hdpi"
mdpi_params_24="24x24 drawable-mdpi"
ldpi_params_24="18x18 drawable-ldpi"

xhdpi_params_64="128x128 drawable-xhdpi"
hdpi_params_64="96x96 drawable-hdpi"
mdpi_params_64="64x64 drawable-mdpi"
ldpi_params_64="48x48 drawable-ldpi"

gen_image() {
    img_size=$1
    res_folder=$2
    file_name=$3
    file_suffix=$4

    file="res/$res_folder/${file_name}_${file_suffix}.png"
    echo "Generating $file"

    convert -resize $img_size misc/${file_name}.svg $file
}

for name in $(ls res/drawable/condition_*.xml)
do
    name=$(basename "$name")
    name="${name%.*}"
    gen_image $xhdpi_params_24 $name 24
    gen_image $hdpi_params_24 $name 24
    gen_image $mdpi_params_24 $name 24
    gen_image $ldpi_params_24 $name 24
    gen_image $xhdpi_params_64 $name 64
    gen_image $hdpi_params_64 $name 64
    gen_image $mdpi_params_64 $name 64
    gen_image $ldpi_params_64 $name 64
done
