if [ -L $0 ] ; then
    ME=$(readlink $0)
else
    ME=$0
fi
SCRIPT_DIR=$(dirname $ME)

is_bin_dir() {
	dir=$1

	if [[ -f "$dir/swipl" && -f "$dir/swipl-ld" && -f "$dir/swipl-rc" ]]; then
		return 0
	fi
	return 1
}

is_lib_dir() {
	dir=$1

	if [[ -f "$dir/libswipl.dylib" ]]; then
		return 0
	fi
	return 1
}
