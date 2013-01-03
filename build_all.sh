#!/bin/sh

TARGETS=$*

buildin() {
    BASE_DIR=$(pwd)
    cd $1
    ant $TARGETS
    cd $BASE_DIR
}

buildin core

buildin skins/black-text
buildin skins/white-text
buildin skins/black-text-plus
buildin skins/white-text-plus
buildin skins/bigger-text
