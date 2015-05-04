if [ -L $0 ] ; then
    ME=$(readlink $0)
else
    ME=$0
fi
SCRIPT_DIR=$(dirname $ME)

xjc -p com.jtalis.core.config.beans $SCRIPT_DIR/XmlConfig.xsd -d $SCRIPT_DIR/../src/main/java
