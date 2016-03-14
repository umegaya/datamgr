buildtool:
	@bash console/META-INF/gen-ctx.sh $(DBNAME) $(PASS)
	@docker run -v `pwd`/console:/build webratio/ant bash -c "cd /build/bin && ant mkwar -Dstage=dev"

deploy:
	-docker rm datamgr
	docker run --rm -ti -p 18080:8080 --name datamgr --link datamgr_source:dms umegaya/datamgr

ct: buildtool
	docker build -t umegaya/datamgr console

alpinehub:
	bash commiter/alpinehub.sh	

cm: alpinehub
	docker build -t umegaya/commiter commiter

all: ct cm
	echo "done!!"