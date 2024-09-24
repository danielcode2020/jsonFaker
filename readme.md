to run backend from command line :
```
export JAVA_HOME=/usr/lib/jvm/jdk-17 && \
 mvn clean package -DskipTests=true &&  \
 java -Dspring.datasource.password=daniel \
  -Dspring.datasource.username=daniel  \
  -Dspring.datasource.url=jdbc:postgresql://localhost:5432/json_faker \
   -Dserver.port=9091 -jar target/jsonfaker-0.0.1.jar && \
    cd ..
```
