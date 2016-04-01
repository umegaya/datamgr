#!/bin/bash

# all commands are executed under /repo.
mysqldump -u root -psanel -h dms sd | sed -e 's/),(/),\n(/g'> masterdata.sql
echo "commit masterdata"