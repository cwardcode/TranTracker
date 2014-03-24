#!/bin/bash
for((; ; ))
do 
mysql --user=gpstracker --password=tracker gpstracker << EOF
insert into tracker_location(VehID_id, latitude, longitude, Speed) values ((select VehID from tracker_vehicle where title="HHS Express"), 35.31175, -83.181281, 8.94);
EOF
sleep 5

mysql --user=gpstracker --password=tracker gpstracker << EOF
insert into tracker_location(VehID_id, latitude, longitude, Speed) values ((select VehID from tracker_vehicle where title="HHS Express"), 35.31167, -83.18391, 8.94);
EOF
sleep 5

mysql --user=gpstracker --password=tracker gpstracker << EOF
insert into tracker_location(VehID_id, latitude, longitude, Speed) values ((select VehID from tracker_vehicle where title="HHS Express"), 35.31214,-83.10432, 8.94);
EOF
sleep 5

mysql --user=gpstracker --password=tracker gpstracker << EOF
insert into tracker_location(VehID_id, latitude, longitude, Speed) values ((select VehID from tracker_vehicle where title="HHS Express"), 35.31283, -83.18454, 8.94);
EOF
sleep 5

mysql --user=gpstracker --password=tracker gpstracker << EOF
insert into tracker_location(VehID_id, latitude, longitude, Speed) values ((select VehID from tracker_vehicle where title="HHS Express"), 3531349, -83.18449, 8.94);
EOF
sleep 5

mysql --user=gpstracker --password=tracker gpstracker << EOF
insert into tracker_location(VehID_id, latitude, longitude, Speed) values ((select VehID from tracker_vehicle where title="HHS Express"), 35.31491,-83.18412, 8.94);
EOF
sleep 5

mysql --user=gpstracker --password=tracker gpstracker << EOF
insert into tracker_location(VehID_id, latitude, longitude, Speed) values ((select VehID from tracker_vehicle where title="HHS Express"), 35.31521, -83.18432, 8.94);
EOF
sleep 5

mysql --user=gpstracker --password=tracker gpstracker << EOF
insert into tracker_location(VehID_id, latitude, longitude, Speed) values ((select VehID from tracker_vehicle where title="HHS Express"), 35.31557, -83.18535, 8.94);
EOF
sleep 5

mysql --user=gpstracker --password=tracker gpstracker << EOF
insert into tracker_location(VehID_id, latitude, longitude, Speed) values ((select VehID from tracker_vehicle where title="HHS Express"), 35.31611, -83.18614, 8.94);
EOF
sleep 5

mysql --user=gpstracker --password=tracker gpstracker << EOF
insert into tracker_location(VehID_id, latitude, longitude, Speed) values ((select VehID from tracker_vehicle where title="HHS Express"), 35.31609, -83.18694, 8.94);
EOF
sleep 5

mysql --user=gpstracker --password=tracker gpstracker << EOF
insert into tracker_location(VehID_id, latitude, longitude, Speed) values ((select VehID from tracker_vehicle where title="HHS Express"), 35.31611, -83.18614, 8.94);
EOF
sleep 5

mysql --user=gpstracker --password=tracker gpstracker << EOF
insert into tracker_location(VehID_id, latitude, longitude, Speed) values ((select VehID from tracker_vehicle where title="HHS Express"), 35.31557, -83.18535, 8.94);
EOF
sleep 5

mysql --user=gpstracker --password=tracker gpstracker << EOF
insert into tracker_location(VehID_id, latitude, longitude, Speed) values ((select VehID from tracker_vehicle where title="HHS Express"), 35.31521, -83.18432, 8.94);
EOF
sleep 5

mysql --user=gpstracker --password=tracker gpstracker << EOF
insert into tracker_location(VehID_id, latitude, longitude, Speed) values ((select VehID from tracker_vehicle where title="HHS Express"), 35.31491,-83.18412, 8.94);
EOF
sleep 5

mysql --user=gpstracker --password=tracker gpstracker << EOF
insert into tracker_location(VehID_id, latitude, longitude, Speed) values ((select VehID from tracker_vehicle where title="HHS Express"), 3531349, -83.18449, 8.94);
EOF
sleep 5

mysql --user=gpstracker --password=tracker gpstracker << EOF
insert into tracker_location(VehID_id, latitude, longitude, Speed) values ((select VehID from tracker_vehicle where title="HHS Express"), 35.31283, -83.18454, 8.94);
EOF
sleep 5

mysql --user=gpstracker --password=tracker gpstracker << EOF
insert into tracker_location(VehID_id, latitude, longitude, Speed) values ((select VehID from tracker_vehicle where title="HHS Express"), 35.31214,-83.10432, 8.94);
EOF
sleep 5

mysql --user=gpstracker --password=tracker gpstracker << EOF
insert into tracker_location(VehID_id, latitude, longitude, Speed) values ((select VehID from tracker_vehicle where title="HHS Express"), 35.31167, -83.18391, 8.94);
EOF
sleep 5

mysql --user=gpstracker --password=tracker gpstracker << EOF
insert into tracker_location(VehID_id, latitude, longitude, Speed) values ((select VehID from tracker_vehicle where title="HHS Express"), 35.31175, -83.181281, 8.94);
EOF
done
