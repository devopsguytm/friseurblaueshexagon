FROM websphere-liberty:javaee7
RUN installUtility install --acceptLicense defaultServer 
COPY server.xml  /config/
COPY target/friseurblaueshexagon.at.war /config/dropins/