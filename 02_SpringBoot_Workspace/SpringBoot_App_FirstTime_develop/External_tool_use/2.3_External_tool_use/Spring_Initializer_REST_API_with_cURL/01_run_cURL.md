# Spring Initialzr REST API with cURL
```{bash}
curl -s https://start.spring.io/starter.zip -o myapp1.zip
curl -s https://start.spring.io/starter.zip -o myapp2.zip -d type=gradle-project
curl -s https://start.spring.io/starter.zip -o myapp3.zip -d type=maven-project -d dependencies=web
curl -s https://start.spring.io/pom.xml -d packaging=war -o pom.xml -d dependencies=web,data-jpa
curl -s https://start.spring.io/build.gradle -o build.gradle -d dependencies=web,data-jpa
```
