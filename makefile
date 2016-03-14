buildtool:
	@bash console/META-INF/gen-ctx.sh $(DBNAME) $(PASS)
	@docker run -v `pwd`/console:/build webratio/ant bash -c "cd /build/bin && ant mkwar -Dstage=dev"

deploy:
	-docker rm datamgr
	docker run --rm -ti -p 18080:8080 --name datamgr --link datamgr_source:dms umegaya/datamgr

# should specify DBNAME, PASS
ct: buildtool
	docker build -t umegaya/datamgr console

alpinehub:
	bash commiter/alpinehub.sh	

# should specify GH_USER, GH_TOKEN, GH_REPO
cm: alpinehub
	docker build -t umegaya/commiter --build-arg GH_USER=$(GH_USER) --build-arg GH_TOKEN=$(GH_TOKEN) --build-arg GH_REPO=$(GH_REPO) commiter

all: ct cm
	echo "done!!"