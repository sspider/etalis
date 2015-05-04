###############################################################
# Get script directory
###############################################################
if [ -L $0 ] ; then
    ME=$(readlink $0)
else
    ME=$0
fi
SCRIPT_DIR=$(dirname $ME)

source "$SCRIPT_DIR/jtalis-commons.sh"

###############################################################
# Call OS specific setup
###############################################################
if [ "osx" = "$OS_NAME" ]; then
	source "$SCRIPT_DIR/jtalis-utils-osx.sh"
else
	source "$SCRIPT_DIR/jtalis-utils-linux.sh"
fi

###############################################################
# Search definition
###############################################################
search() {
	file=$1
	cbk=$2

	if $cbk "$file"; then
		echo "$file"
		return 0
	fi

	for child in `find $file -type d -print`; do
		if $cbk "$child"; then
			echo  "$child"
			return 0
		fi		
	done
}

###############################################################
# Get SWI_HOME_DIR
###############################################################
if [ "" = "$SWI_HOME_DIR" ]; then
	echo Enter your SWI Prolog home directory:
	read SWI_HOME_DIR
	export SWI_HOME_DIR
fi

###############################################################
# Find export SWI_LIB_PATH and SWI_BIN_PATH
###############################################################
echo [SETUP INFO] Searching for SWI_LIB_DIR and SWI_BIN_DIR ...
export SWI_LIB_PATH=`search "$SWI_HOME_DIR" is_lib_dir`
export SWI_BIN_PATH=`search "$SWI_HOME_DIR" is_bin_dir`
echo [SETUP INFO] SWI_LIB_PATH found at: $SWI_LIB_PATH
echo [SETUP INFO] SWI_BIN_PATH found at: $SWI_BIN_PATH

###############################################################
# Change ~/.profile if needed
###############################################################
if [ "" != "`grep "SWI_HOME_DIR" ~/.profile`" ]; then
	echo [SETUP INFO] ~/.profile already contains SWI_HOME_DIR definition. Skipping ~/.profile environment setup.
else
	echo [SETUP INFO] Defining SWI Prolog environment variables in ~/.profile ...
	echo "" >> ~/.profile
	echo "# Changes from Jtalis. SWI Prolog confguration" >> ~/.profile
	echo "export SWI_HOME_DIR=$SWI_HOME_DIR" >> ~/.profile

	. ~/.profile
fi

###############################################################
# Install Interprolog and JPL dependencies in Maven repo
###############################################################
if [ ! -e ~/.m2/repository/com/veskogeorgiev/1.0.0/probin-1.0.0.jar ]; then
	echo [SETUP INFO] Installing probin-1.0.0.jar in local Maven repository ...
	mvn install:install-file\
	 -Dfile=$SCRIPT_DIR/libs/probin-1.0.0.jar\
	 -DgroupId=com.veskogeorgiev\
	 -DartifactId=probin\
	 -Dversion=1.0.0\
	 -Dpackaging=jar\
	 -DgeneratePom=true
fi
if [ ! -e ~/.m2/repository/interprolog/interprolog/1.0/interprolog-1.0.jar ]; then
	echo [SETUP INFO] Installing Interprolog.jar in local Maven repository ...
	mvn install:install-file\
	 -Dfile=$SCRIPT_DIR/libs/interprolog.jar\
	 -DgroupId=interprolog\
	 -DartifactId=interprolog\
	 -Dversion=1.0\
	 -Dpackaging=jar\
	 -DgeneratePom=true
fi
if [ ! -e ~/.m2/repository/jpl/jpl/1.0/jpl-1.0.jar ]; then
	echo [SETUP INFO] Installing jpl.jar in local Maven repository ...
	mvn install:install-file\
	 -Dfile=$SWI_HOME_DIR/lib/jpl.jar\
	 -DgroupId=jpl\
	 -DartifactId=jpl\
	 -Dversion=1.0\
	 -Dpackaging=jar\
	 -DgeneratePom=true
fi
if [ ! -e ~/.m2/repository/org/apache/incubator/kafka/kafka/0.7.2/kafka-0.7.2.jar ]; then
	echo [SETUP INFO] Installing kafka-0.7.2.jar in local Maven repository ...
	mvn install:install-file\
	 -Dfile=$SCRIPT_DIR/libs/kafka-0.7.2.jar\
	 -DgroupId=org.apache.incubator.kafka\
	 -DartifactId=kafka\
	 -Dversion=0.7.2\
	 -Dpackaging=jar\
	 -DgeneratePom=true
fi
