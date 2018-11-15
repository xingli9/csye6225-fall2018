
#!/bin/bash

sudo systemctl stop tomcat.service

sudo rm -rf /opt/tomcat/webapps/docs  /opt/tomcat/webapps/examples /opt/tomcat/webapps/host-manager  /opt/tomcat/webapps/manager /opt/tomcat/webapps/ROOT

sudo chown tomcat:tomcat /opt/tomcat/webapps/csye6225Webapp-1.0-SNAPSHOT.war
sudo rm -rf /opt/tomcat/logs/catalina*
sudo rm -rf /opt/tomcat/logs/*.log
sudo rm -rf /opt/tomcat/logs/*.txt


exit 0


