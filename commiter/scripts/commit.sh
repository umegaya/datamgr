#!/bin/bash

clean_repository() {
        git reset --hard && git clean -d -f
        git checkout master
}

make_pull_request() {
        local branch=$1/autocommit-`date +%s`
        git checkout -b $branch
        git commit -a -m "commit masterdata by $1"
        git push origin HEAD:$branch
        # make pull request
        hub pull-request -b master -h $branch -m "commit masterdata"
}

cd repo
        clean_repository
        bash /scripts/modify.sh
        out=`make_pull_request $1`
	echo $out
        