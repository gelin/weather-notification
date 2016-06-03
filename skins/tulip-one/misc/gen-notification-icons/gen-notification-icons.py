#!/usr/bin/python3

from PIL import Image
from os import listdir
from os.path import realpath, dirname, join


def load_parts(resolution):
    res_path = join(dirname(realpath(__file__)), resolution)
    result = {}
    for file in listdir(res_path):
        if file.endswith('.png'):
            image = Image.open(join(res_path, file))
            result[file[:-4]] = image
    return result


def build_icon(parts):
    size = parts[0].size[1]
    icon = Image.new('RGBA', (size, size))
    x = (size - sum(part.size[0] for part in parts)) // 2
    for part in parts:
        icon.paste(part, (x, 0), part)  # with alpha channel
        x = x + part.size[0]
    return icon


def get_icon_name(index):
    name = 'temp'
    if index < 0:
        name += '_minus'
    if index > 0:
        name += '_plus'
    name += '_' + str(abs(index))
    name += '.png'
    return name


def select_icon_parts(index, parts):
    result = []
    if index < 0:
        result.append(parts['minus'])
    # if index > 0:
    #     result.append(parts['plus'])
    remaining = abs(index)
    if remaining >= 100:
        result.append(parts[str(remaining // 100)])
        remaining %= 100
        if remaining < 10:
            result.append(parts['0'])
    if remaining >= 10:
        result.append(parts[str(remaining // 10)])
        remaining %= 10
    result.append(parts[str(remaining)])
    if abs(index) < 100:
        result.append(parts['degree'])
    return result


def save_icon(resolution, index, image):
    path_suffix = '-' + resolution
    if resolution == 'mdpi':
        path_suffix = ''
    res_path = join('res/drawable' + path_suffix, get_icon_name(index))
    image.save(join(dirname(realpath(__file__)), '..', '..', res_path))
    return res_path


if __name__ == '__main__':
    for res in ('ldpi', 'mdpi', 'hdpi', 'xhdpi', 'xxhdpi', 'xxxhdpi'):
        parts = load_parts(res)
        for idx in range(-60, 131):
            icon = build_icon(select_icon_parts(idx, parts))
            file_name = save_icon(res, idx, icon)
            print(file_name)
