#!/bin/bash

set -ex

export LD_LIBRARY_PATH=release/lib
mkdir -p ${LD_LIBRARY_PATH}
cd swig/
rm -rf build && mkdir build && cd build
cmake .. && make -j 8
cp lib/* ../${LD_LIBRARY_PATH}
cd ..
mvn clean install -T 0.5C -Dmaven.test.skip=true
cp target/*.jar ${OPENHUFU_ROOT}/lib/aby4j.jar