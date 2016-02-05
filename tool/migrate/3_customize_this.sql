/* declare common relation pattern between related column and table name */
LOCK TABLES `relation_rule` WRITE;
/* regard column which name ends with {table_name}_id as relation to {table_name}, which has primary key 'id'. */
INSERT INTO `relation_rule` VALUES ('%s_id$','id');
UNLOCK TABLES;
