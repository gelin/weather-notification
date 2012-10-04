#!/bin/sh

TARGETS=$*

ant $TARGETS

build_skin() {
    BASE_DIR=$(pwd)
    cd skins/$1
    ant $TARGETS
    cd $BASE_DIR
}

build_skin black-text
build_skin white-text
build_skin black-text-plus
build_skin white-text-plus
build_skin bigger-text
