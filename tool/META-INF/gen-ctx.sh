#!/bin/bash
if [ -z $1 ]; then
	echo "specify your game database name as DBNAME=_name_ PASS=_pass_"
	exit 1
fi
DBNAME=$1
if [ -z $2 ]; then
	echo "specify your game database name as DBNAME=_name_ PASS=_pass_"
	exit 1
fi
PASS=$2	

cat << CTX > `dirname $0`/context.xml.dev
<Context reloadable="true">
	<Parameter name="isDevelop" value="true" override="false"/>
	<Resource
		name="jdbc/manager"
		auth="Container"
		type="javax.sql.DataSource"
		maxActive="10"
		maxIdle="30"
		maxWait="10000"
		testOnBorrow="true"
		validationQuery="SELECT 1"
		username="root"
		password="${PASS}"
		driverClassName="com.mysql.jdbc.Driver"
		url="jdbc:mysql://dms:3306/manager?useUnicode=true&amp;characterEncoding=utf8"
	/>
	<Resource
		name="jdbc/game-readwrite"
		auth="Container"
		type="javax.sql.DataSource"
		maxActive="10"
		maxIdle="30"
		maxWait="10000"
		testOnBorrow="true"
		validationQuery="SELECT 1"
		username="root"
		password="${PASS}"
		driverClassName="com.mysql.jdbc.Driver"
		url="jdbc:mysql://dms:3306/${DBNAME}?useUnicode=true&amp;characterEncoding=utf8"
	/>
	<Resource
		name="jdbc/game-readonly"
		auth="Container"
		type="javax.sql.DataSource"
		maxActive="10"
		maxIdle="30"
		maxWait="10000"
		testOnBorrow="true"
		validationQuery="SELECT 1"
		username="root"
		password="${PASS}"
		driverClassName="com.mysql.jdbc.Driver"
		url="jdbc:mysql://dms:3306/${DBNAME}?useUnicode=true&amp;characterEncoding=utf8"
	/>
</Context>
CTX

