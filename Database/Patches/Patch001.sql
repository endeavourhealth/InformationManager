
/*Script to add description column.  Only required if you already have the table defined and dont want to drop it and recreate it*/
alter table information_model.concept
add column description text comment 'Formatted HTML text description of the concept';