
       .d8888b.  8888888b.  8888888888 888      888
      d88P  Y88b 888   Y88b 888        888      888
      Y88b.      888    888 888        888      888
       "Y888b.   888   d88P 8888888    888      888
          "Y88b. 8888888P"  888        888      888
            "888 888        888        888      888
      Y88b  d88P 888        888        888      888
       "Y8888P"  888        8888888888 88888888 88888888

This is a Spring Initializer CLI tool (based on https://start.spring.io/) for generating quickstart projects 
that can be easily customized.
Possible customizations include a project's dependencies,
Java version, and build system or build structure.

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

### Spell configuration commands

- **config**: Show configuration file
- **set-group**: Set default group name
- **set-artifact**: Set default artifact name
- **set-template**: Create/replace template
- **iset-template**: Create/replace template interactively
- **remove-template**: Remove template

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

- jar: [spell-jar-v0.3.0.zip](https://github.com/spell-cli-tool/spell/releases/download/v0.3.0/spell-jar-v0.3.0.zip)
- Windows: [native-windows-v0.3.0.zip](https://github.com/spell-cli-tool/spell/releases/download/v0.3.0/native-windows-v0.3.0.zip)
- Linux: [native-linux-v0.3.0.zip](https://github.com/spell-cli-tool/spell/releases/download/v0.3.0/native-linux-v0.3.0.zip)
- macOS: [native-macos-arm64-v0.3.0.zip](https://github.com/spell-cli-tool/spell/releases/download/v0.3.0/native-macos-arm64-v0.3.0.zip)

All releases page: [https://github.com/spell-cli-tool/spell/releases](https://github.com/spell-cli-tool/spell/releases)

### 'icreate' command

![](https://github.com/spell-cli-tool/spell/blob/master/assets/spell-demo.gif)

### 'create' command

```
create -t gradle-project -l java -g org.example -a demo -j 25 -d devtools,lombok,web 
```

### 'param' command

**param** command shows possible values of parameters for command 'create'.

### 'dependency' command

**dependency** command shows details about Spring dependencies. For example: Spring Web [web] 

![](https://github.com/spell-cli-tool/spell/blob/master/assets/dependency-demo.png)

## License

This app is licensed under the Apache License Version 2.0. See the LICENSE file for more details.

## Contact information

If you have any questions or suggestions, please feel free to reach out to me:

 - Email: ivv.kopylov@gmail.com
