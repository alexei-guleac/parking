mvn install -Dcheckstyle.skip=true -DskiptTests && docker build ./ -t springbootapp && docker-compose up --build
