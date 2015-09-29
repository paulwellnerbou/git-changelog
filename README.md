# git-changelog

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

	./gradlew run -Pargs='--processor=jirafilter --repo=/path/to/your/git/repo --baseurl=http://jira.base.url/ --projects=PRJ1,PRJ2'

## How to use the library

Basically all you have to do is add this as dependency. For maven, this would be:

        <dependency>
            <groupId>de.wellnerbou</groupId>
            <artifactId>git-changelog</artifactId>
            <version>VERSION</version>
        </dependency>
        
Then, import the GitJira class, create an instance of the GitJira object and use it, as the command line
entry point (main() in GitJira) is doing (see https://github.com/paulwellnerbou/git-jira-log/blob/master/src/main/java/de/wellnerbou/gitjira/app/GitJira.java).

# Example: Changelog of [Jenkins]()' latest release

## Command line

```
./gradlew run -Pargs='--processor=jirafilter --repo=/path/to/src/jenkinsci --baseurl=https://issues.jenkins-ci.org/ --projects=JENKINS'
```

This is how the output will look like (or similar, if a newer version is released):

	No revs given, searching automatically for latest released tags...
	Found toRev tag jenkins-1.617
	Found fromRev tag jenkins-1.616
	Jira-Tickets mentioned in commits between jenkins-1.616 and jenkins-1.617:
	JENKINS-28654,JENKINS-28704,JENKINS-27739
	https://issues.jenkins-ci.org/issues/?jql=key%20in%20%28JENKINS-28654,JENKINS-28704,JENKINS-27739%29

## Java

	AppArgs appArgs = new AppArgs();
	appArgs.setJiraBaseUrl("https://issues.jenkins-ci.org/");
	appArgs.setJiraProjectPrefixes("JENKINS");
	appArgs.setRepo("/path/to/src/jenkinsci");
	GitJira gitChangelog = new GitJira(appArgs);
	final Changelog changelog = gitChangelog.changelog();
	gitChangelog.jiraFilterUrl(changelog.getTickets());
