buildtool:
	@bash tool/META-INF/gen-ctx.sh $(DBNAME) $(PASS)
	@docker run -v `pwd`/tool:/build webratio/ant bash -c "cd /build/bin && ant mkwar -Dstage=dev"

deploy:
	-docker rm datamgr
	docker run --rm -ti -p 18080:8080 --name datamgr --link datamgr_source:dms umegaya/datamgr

ct: buildtool
	docker build -t umegaya/datamgr .
