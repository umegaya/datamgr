buildtool:
	@bash console/META-INF/gen-ctx.sh $(DBNAME) $(PASS)
	@docker run -v `pwd`/console:/build webratio/ant bash -c "cd /build/bin && ant mkwar -Dstage=dev"

deploy:
	-docker kill datamgr
	-docker rm datamgr
	docker run -d --name datamgr --link datamgr_source:dms umegaya/datamgr
	sleep 3
	docker exec -ti datamgr bash -c "tail -f /datamgr.log"

# should specify DBNAME, PASS
ct: buildtool
	docker build -t umegaya/datamgr console

alpinehub:
	bash commiter/alpinehub.sh	

# should specify GH_USER, GH_TOKEN, GH_REPO
cm: alpinehub
	docker build -t umegaya/commiter --build-arg GH_USER=$(GH_USER) --build-arg GH_TOKEN=$(GH_TOKEN) --build-arg GH_REPO=$(GH_REPO) commiter

fe: 
	cd front/cert && bash gen.sh
	cd front && sed -e 's/_SERVER_NAME_/$(SERVER)/g' nginx.conf.tmpl > nginx.conf
	docker build -t umegaya/front front

all: ct cm
	echo "done!!"

# should specify HOST, and DBHOST if db and web run on different server
ifndef DBHOST
DBHOST=$(HOST)
endif
attach:
	echo "Hosts: $(HOST) $(DBHOST)"
	@-docker kill dm4_$(HOST) cm4_$(HOST)
	@-docker rm dm4_$(HOST) cm4_$(HOST)
	docker run -d --name cm4_$(HOST) --add-host dms:$(HOST) --add-host db:$(DBHOST) umegaya/commiter
	docker run -d --name dm4_$(HOST) --link cm4_$(HOST):cmt --add-host dms:$(HOST) umegaya/datamgr
