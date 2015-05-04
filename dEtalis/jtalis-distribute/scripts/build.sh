if [ -L $0 ] ; then
    ME=$(readlink $0)
else
    ME=$0
fi
SCRIPT_DIR=$(dirname $ME)

. "$SCRIPT_DIR/jtalis-setup.sh"

lib_path=$PATH:$SWI_LIB_PATH:$SWI_BIN_PATH
export LD_LIBRARY_PATH=$lib_path
export DYLD_LIBRARY_PATH=$lib_path

mvn -f $SCRIPT_DIR/../pom.xml install dependency:copy-dependencies
