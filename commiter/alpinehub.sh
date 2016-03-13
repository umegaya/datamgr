#!/bin/bash
HUB_VERSION=2.2.3
pushd `dirname $0`
	if [ ! -f "./hub" ]; then
	curl -L -O https://github.com/github/hub/archive/v$HUB_VERSION.tar.gz && \
		tar -zxf v$HUB_VERSION.tar.gz && \
		cd hub-$HUB_VERSION && \
		docker run --rm -v "$PWD":/src umegaya/alpinego bash -c "cd /src && ./script/build"
		cp hub ../
		rm -r hub-$HUB_VERSION v$HUB_VERSION.tar.gz
	fi
popd