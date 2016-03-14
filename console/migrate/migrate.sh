#!/bin/bash

dir=`dirname $0`
ver=`mysql -u root -psanel -h dms manager -e "select * from version;"`
if [ "$?" != "0" ]; then
	mysql -u root -psanel -h dms -e "create database if not exists manager;"
	ver="0"
fi
pfx="./$ver"
newver="$ver"
for filepath in $(ls $dir/*.sql); do
	if [[ "$filepath" > "$pfx" ]]; then
		mysql -u root -psanel -h dms manager < $filepath
		tmp=`echo $filepath | awk 'match($0, /([0-9]+)/) {print substr($0, RSTART, RLENGTH)}'`
		if [[ "$newver" < "$tmp" ]]; then
			echo "version: $newver => $tmp"
			newver="$tmp"
		fi
	fi
done

if [[ "$newver" > "$ver" ]]; then
	echo "version become $newver"
	mysql -u root -psanel -h dms manager -e "replace into version values($newver)"
fi
