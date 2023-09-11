# leandrojesuseng
# - criar a imagem docker
# docker build -t leandrojesuseng/spring-boot-crud .
# - executar o container, spring boot configurado na 80, mas 9001 para o host
# docker run -p 9001:80 leandrojesuseng/spring-boot-crud

# docker tag leandrojesuseng/spring-boot-crud leandrojesuseng/spring-boot-crud 
# docker push leandrojesuseng/spring-boot-crud:latest

FROM openjdk:11
RUN addgroup spring 
RUN adduser --ingroup spring spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY data-Init/ data/
RUN chown -R spring data/ 
USER spring:spring
ENTRYPOINT ["java", "-jar", "/app.jar", "> saida.log", "2> errors.txt", "< /dev/null"]