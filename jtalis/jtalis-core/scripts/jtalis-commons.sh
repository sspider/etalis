if [ -L $0 ] ; then
    ME=$(readlink $0)
else
    ME=$0
fi
SCRIPT_DIR=$(dirname $ME)

################################################################
# Setup
################################################################
to_lower() {
	to_lower_out=$(echo "$1" | tr [:upper:] [:lower:])
	echo $to_lower_out
}

OS_NAME=`to_lower $(uname -s)`

if [ $OS_NAME = "darwin" ]; then
	OS_NAME="osx"
	ARCH="64"
elif [ $OS_NAME = "linux" ]; then
	ARCH=`uname -a`
	if [[ $ARCH =~ .*_64.* ]]; then
		ARCH=64
	else
		ARCH=32
	fi
fi
