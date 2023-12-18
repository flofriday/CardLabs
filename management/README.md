## Getting started
### Local development
To execute the management locally on your machine, you must create a new IntelliJ Spring Boot configuration. Here 
you use as SDK `Java 21`, as module `gameserver.main` as boot class
`at.tuwien.ase.cardlabs.gameserver.GameserverApplication` and as active profile `local`. <br/>
If the output should include debug output, then add the profile `debug` to active profiles.

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

## Logging guidelines
### Log Message Level
Use the `.info` logging level that initializes a new process such as when a REST call is handled or the matchmaking
logic is executed. <br/>
The first level in the subprocess uses the `.debug` logging level. Any other logging messages use the `.trace` logging
level excluding the use cases for `.warn` and `.error`
### Log Message
(a) Don't ever log sensitive information such as passwords. <br/>
(b) The log messages are in English. <br/>
(c) Add context to log messages. Otherwise they are useless. <br/>
(d) Log in machine parsable format. <br/>
(e) Log in human readable format.
### How to add context
(a) When a process contains a unique caller such as a user, then this should always be logged using its unique
identifier and stating the caller type. <br/>
(b) When a process modifies data such as creating a user or deleting a user, then the unique identifier of the modified
data should be logged. <br/>
(c) Add the current state in which the process is when the log message is created. Examples are 'Creating' or 'Created'
for either indicating that something should be created and something has been created respectively.
### Examples
```
"Attempting to create an account with the username ${account.username}"
"User ${user.id} attempts to create a bot with name ${botCreate.name}"
"User ${user.id} attempts to patch the bot $botId"
"User ${user.id} attempts to delete the bot $botId"
```