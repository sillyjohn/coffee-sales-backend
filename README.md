# Coffee Backend

Hi guys, this is my toy project to create a full stack application using knowledge I learnt before, and build something that allow me to manage my little coffee business and a ordering system(in the future?) for the customer.


## Application Properties
Please manually add this application properties into your project directory.
```xml
spring.application.name=Coffee Sales Backend

#Port
#port =

#Postgresql Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/coffeesalesdb
spring.datasource.username= {YOUR DATEBASE USRNAME}
spring.datasource.password= {YOUR DATABASE PWD}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

#Cors Configuration
spring.web.cors.allowed-origins=http://localhost:3000

#JWT Configuration
jwt.secret = ${}
jwt.expiration=3600000

#TRACE LOGGING, Enable if needed
#logging.level.org.springframework.security: TRACE


```

## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

