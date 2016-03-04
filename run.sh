#!/bin/bash

bash /migrate/migrate.sh
catalina.sh run 2>&1 > /datamgr.log

