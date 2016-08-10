#!/bin/bash

# all commands are executed under /repo.
mysqldump --no-create-info -u root -h dms sd | sed -e 's/),(/),\n(/g' | sed -e 's/^INSERT INTO `\([a-z_]*\)`/TRUNCATE `\1`;\nINSERT INTO `\1`/g' > masterdata.sql
mysqldump --no-data -u root -h dms sd | sed -e 's/),(/),\n(/g'> masterdata_schema.sql
curl -k https://dms/debug/export_csv.php | ruby /scripts/out.rb Assets/Resources/Bundles/Master
echo "commit masterdata"
