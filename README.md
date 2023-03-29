# OpenHuFu-Plugin

OpenHuFu-Plugin is A java wrapper of ABY using SWIG.

### Prerequisites:
OpenHuFu Compile succeed

### Installation

1. not make install ABY before. (if installed, please remove installed files, especially files in `/usr/local/lib/cmake`)
2. init git submodule

```
git submodule update --init
```

3. check cmake version >= 3.16, gcc version >= 8.0, java jdk version >= 11, swig3.0, GMP, Boost >= 1.66.0

   (After installing the new version, you may need to manually update the dynamic link library (e.g. libstdc++.so.6), otherwise the dynamic link library will use the old version.)

4. set OPENHUFU_ROOT

```
export OPENHUFU_ROOT={OpenHuFu root path}
# e.g.
# export OPENHUFU_ROOT=~/OpenHuFu/release
```

5. run package.sh, and the package result will be installed in `${OPENHUFU_ROOT}/lib`

```
# for first installation, or cpp code is modified, add 'all' to update .so library
# after running the script, need to move .so .a files in swig/build/lib into java lib path manually, e.g., /usr/lib/jni
./package.sh
```

(When using, you should add the parameter `-Djava.library.path=${OPENHUFU_ROOT}/lib` to add the library path)

### Project structure

- `swig`: the swig interface of aby, use .i file to wrap C++ functions, the generated java code will be placed in `src/java/com/hufudb/openhufu/mpc/aby/*`, please do not add these generated files to git
- `src`: the ProtocolExecutor interface of aby(`Aby.java`, `AbyFactory.java`), wrapper for swig interface to interactive with OpenHuFu
