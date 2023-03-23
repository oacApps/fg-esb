#!/bin/bash
echo "Starting cleaning"
echo "------------------------------------"
echo "Current disk status"
df -H
echo "Cleaning docker log"
sudo sh -c "truncate -s 0 /var/lib/docker/containers/*/*-json.log"
echo "After cleaning docker log  disk status"
df -H

echo "Cleaning Application Log"
sudo find /var/log/esb/*/archive/*.log -mtime +3 -type f -delete
sudo find /var/log/esb/*/tomcat/*.log -mtime +3 -type f -delete
echo "After cleaning Application logs  disk status"
df -H
