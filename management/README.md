## Getting started
### Local development
To execute the management locally on your machine, you must create a new IntelliJ Spring Boot configuration. Here 
you use as SDK `Java 21`, as module `gameserver.main` as boot class
`at.tuwien.ase.cardlabs.gameserver.GameserverApplication` and as active profile `local`.

### Run the pipeline jobs
To execute the different pipeline stages currently simply executing the commands is the easiest. Please refer to the
`.gitlab-ci.yml` for the most update commands.

#### Run the analysis job locally (on Windows)
```
.\gradlew.bat checkstyleMain
.\gradlew.bat checkstyleTest
.\gradlew.bat ktlintCheck
```

#### Run the build job locally (on Windows)
```
.\gradlew.bat assemble
```

#### Run the test job locally (on Windows)
```
.\gradlew.bat test --info
```
Note: in the `build/reports/test` directory there is a `index.html` file that contains details about the test execution