FROM alpine:latest

ARG GH_USER
ARG GH_TOKEN
ARG GH_REPO

RUN apk add --update socat && \
	apk add --update mysql-client && \
	apk add --update git && \
	apk add --update bash && \
	apk add --update ruby && \
	rm -rf /var/cache/apk/*

ADD hub /usr/local/bin/hub
ADD config /root/.config
RUN cat /root/.config/hub.tmpl | sed -e s/USER/$GH_USER/g | sed -e s/TOKEN/$GH_TOKEN/g > /root/.config/hub
RUN hub clone https://$GH_USER:$GH_TOKEN@github.com/$GH_REPO repo && cd repo && hub release

ADD scripts scripts
ADD bashttpd bashttpd
ADD bashttpd.conf bashttpd.conf
RUN chmod 755 bashttpd

ARG GITUSER=datamgr-commiter
ARG GITMAIL=commiter@datamgr.tokyo
RUN git config --global user.name $GITUSER
RUN git config --global user.email $GITMAIL

CMD ["socat", "TCP4-LISTEN:8888,fork", "EXEC:./bashttpd"]
