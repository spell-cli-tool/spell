
       .d8888b.  8888888b.  8888888888 888      888
      d88P  Y88b 888   Y88b 888        888      888
      Y88b.      888    888 888        888      888
       "Y888b.   888   d88P 8888888    888      888
          "Y88b. 8888888P"  888        888      888
            "888 888        888        888      888
      Y88b  d88P 888        888        888      888
       "Y8888P"  888        8888888888 88888888 88888888

This is a Spring Initializr (https://start.spring.io/) CLI tool for generating quickstart projects 
that can be easily customized.
Possible customizations include a project's dependencies, Java version, and build system or
build structure.

## Main commands
- **help**: Display help about available commands.
- **icreate**: Create Spring Boot projects interactively. This command allows you to 
create new Spring Boot projects with ease. Simply run the command and follow the prompts to create a new project.
- **create**: Create a Spring Boot project non-interactively with params. See also 'dependency' and 'param' commands.

## All commands

### Spring Initializer commands:

- **icreate**: Create Spring Boot projects interactively
- **create**: Create a Spring Boot project non-interactively with params
- **dependency**: Show details about Spring dependencies
- **param**: Show values (examples) for params of the 'create' command

### Basic OS commands

- **ls**: Show list of files and directories
- **pwd**: Show full path to the current directory

### Spring shell built-In commands

-   **help**: Display help about available commands
-   **stacktrace**: Display the full stacktrace of the last error
-   **clear**: Clear the shell screen
-   **quit, exit**: Exit the shell
-   **history**: Display or save the history of previously run commands
-   **version**: Show version info

## Installation and usage

Download appropriate zip archive with executable binary file for Windows, 
macOS, or Linux, unzip and launch spell executable file.
Also it's possible to download zip archive with executable jar and launch tool with command `java -jar spell-{version}.jar`.

- jar: [package-spell-jar-v0.1.1.zip](https://github.com/ivvkopylov/spell/releases/download/v0.1.1/package-spell-jar-v0.1.1.zip)
- Windows: [package-native-windows-v0.1.1.zip](https://github.com/ivvkopylov/spell/releases/download/v0.1.1/package-native-windows-v0.1.1.zip)
- Linux: [package-native-ubuntu-v0.1.1.zip](https://github.com/ivvkopylov/spell/releases/download/v0.1.1/package-native-ubuntu-v0.1.1.zip)
- macOS: [package-native-macos-v0.1.1.zip](https://github.com/ivvkopylov/spell/releases/download/v0.1.1/package-native-macos-v0.1.1.zip)

All releases page: [https://github.com/ivvkopylov/spell/releases](https://github.com/ivvkopylov/spell/releases)

### 'icreate' command

![](https://github.com/ivvkopylov/spell/blob/master/assets/spell-demo.gif)

### 'create' command

```
create -t gradle-project -l java -g org.example -a demo -j 17 -d devtools,lombok,web 
```

### 'param' command

**param** command shows possible values of parameters for command 'create'.
```
!!! Some values can be changed over time !!!
```
**Table 1** (values for the first of June 2023)

| Param               | Possible values / Examples                                                                                                                                                                                                                                                                                                                                                                                                                                                               |
|---------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| -t, --type          | gradle-project, gradle-project-kotlin, gradle-build, maven-project, maven-build                                                                                                                                                                                                                                                                                                                                                                                                          |
| -l, --language      | java, kotlin, groovy                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| -b, --boot-version  | 3.1.1.BUILD-SNAPSHOT, 3.1.0.RELEASE, 3.0.8.BUILD-SNAPSHOT, 3.0.7.RELEASE, 2.7.13.BUILD-SNAPSHOT, 2.7.12.RELEASE                                                                                                                                                                                                                                                                                                                                                                          |
| -g, --group-id      | com.example                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| -a, --artifact-id   | demo                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| -n, --name          | demo                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| -p, --packaging     | jar, war                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| -j, --java-version  | 20, 17, 11, 1.8                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
| -d, --dependencies  | native, devtools, lombok, configuration-processor, docker-compose, web, webflux, graphql, data-rest, session, data-rest-explorer, hateoas, web-services, jersey, vaadin, thymeleaf, freemarker, mustache, groovy-templates, security, oauth2-client, oauth2-authorization-server, oauth2-resource-server, data-ldap, okta, jdbc, data-jpa, data-jdbc, data-r2dbc, mybatis, liquibase, flyway, jooq, db2, derby, h2, hsql, mariadb, sqlserver, mysql, oracle, postgresql, data-redis, ... |

### 'dependency' command

**dependency** command shows details about Spring dependencies. For example: Spring Web [web] 

![](https://github.com/ivvkopylov/spell/blob/master/assets/dependency-demo.png)

## License

This app is licensed under the Apache License Version 2.0. See the LICENSE file for more details.

## Contact information

If you have any questions or suggestions, please feel free to reach out to me:

 - Email: spell.cli.tool@gmail.com
