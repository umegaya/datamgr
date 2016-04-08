#!/bin/bash

# all commands are executed under /repo.
mysqldump -u root -h dms sd | sed -e 's/),(/),\n(/g'> masterdata.sql
curl -k https://dms/debug/export_csv.php | ruby /scripts/out.rb Assets/Resources/Bundles/Master
echo "commit masterdata"
