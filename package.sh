#!/bin/bash

set -ex

export LD_LIBRARY_PATH=release/lib
mkdir -p ${LD_LIBRARY_PATH}
if [[ $1 == "all" ]];then
    cd swig/
    rm -rf build && mkdir build
    cd build
    cmake .. && make -j 8
    cd ../..
fi
cp swig/build/lib/* ${LD_LIBRARY_PATH}
mvn clean install -T 0.5C -DskipTests
cp target/*.jar ${LD_LIBRARY_PATH}/aby4j.jar
cp target/*.jar ${OPENHUFU_ROOT}/lib/aby4j.jar