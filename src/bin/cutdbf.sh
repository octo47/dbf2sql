#!/bin/sh

HOMEDIR=`dirname $0`
CP="$HOMEDIR:$HOMEDIR/etc"

for dir in modules libs; do
    if [ -d $HOMEDIR/$dir ]; then
        for f in `ls $HOMEDIR/$dir`; do
           CP="$CP:$HOMEDIR/$dir/$f"
        done
    fi

done

java -cp $CP dbf2sql.CutDbfMain $@
