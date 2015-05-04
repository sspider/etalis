if [ $# -lt 2 ]; then
	echo Usage: {pom.xml} {config.xml}
	exit
fi

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

mvn -f "$1" exec:java -Dexec.mainClass="com.jtalis.core.JtalisMain" -Dexec.args="$2"
