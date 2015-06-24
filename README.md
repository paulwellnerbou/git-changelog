# git-jira-log

A library and command line tool to extract all JIRA tickets out of commit messages between two GIT revisions and create a URL to the JIRA filter
showing those commits in JIRA.

## How to build

To build the project completely, package a distribution (see https://docs.gradle.org/current/userguide/application_plugin.html) and install
the artifacts into your local <code>.m2/repository</code>:

```
./gradlew clean build install distZip
```

## How to run

You can run the application with gradle:

	./gradlew run -Pargs='--repo=/path/to/your/git/repo --baseurl=http://jira.base.url/ --projects=PRJ1,PRJ2' 

## How to use the library

Basically all you have to do is add this as dependency. For maven, this would be:

        <dependency>
            <groupId>de.wellnerbou</groupId>
            <artifactId>git-jira-log</artifactId>
            <version>VERSION</version>
        </dependency>
        
Then, import the GitJira class, create an instance of the GitJira object and use it, as the command line
entry point (main() in GitJira) is doing (see https://github.com/paulwellnerbou/git-jira-log/blob/master/src/main/java/de/wellnerbou/gitjira/app/GitJira.java).

# Example: Changelog of [Jenkins]()' latest release

## Command line

```
./gradlew run -Pargs='--repo=/path/to/src/jenkinsci --baseurl=https://issues.jenkins-ci.org/ --projects=JENKINS'
```

## Java

	AppArgs appArgs = new AppArgs();
	appArgs.setJiraBaseUrl("https://issues.jenkins-ci.org/");
	appArgs.setJiraProjectPrefixes("JENKINS");
	appArgs.setRepo("/path/to/src/jenkinsci");
	GitJira gitJira = new GitJira(appArgs);
	final Changelog changelog = gitJira.changelog();
	gitJira.jiraFilterUrl(changelog.getTickets());
