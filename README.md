# git-changelog

A library and command line tool to extract a changelog out of commit messages between two GIT revisions. This changelog can be postprocessed and converted
to either an human readable git changelog listing all commits, or a JIRA filter URL.

## How to build

To build the project completely, package a distribution (see https://docs.gradle.org/current/userguide/application_plugin.html) and install
the artifacts into your local <code>.m2/repository</code>:

```
./gradlew clean build install distZip
```

The distribution will be packaged into <code>build/distributions</code>.

## How to run

You can run the application with gradle:

```
	./gradlew run -Pargs='--processor=jirafilter --repo=/path/to/your/git/repo --baseurl=http://jira.base.url/ --projects=PRJ1,PRJ2'
```

Or you can use the unzipped distribution build before:

	./git-changelog --processor=jirafilter --repo=/path/to/your/git/repo --baseurl=http://jira.base.url/ --projects=PRJ1,PRJ2

### Usage

```
usage: git-changelog [OPTIONS] <revFrom> <revTo>
    --processor <class name or id>   Available processors: jirafilter,
                                     basic
    --repo </path/to/repo>           Path to git repository to use,
                                     defaults to '.'.
```

The command line interface is still work in progress, it may be extracted into another library or groovy script and distributed as a separate artifact.

## Postprocessors

At the moment, there are two postprocessors implemented:

* jirafilter: Scans the git changelog for jira tickets and creates an URL to the JIRA filter.
* basic: Prints a human readable git changelog with short commit hashes and commit messages

## How to use the library

Basically all you have to do is add this as dependency. For maven, this would be:

        <dependency>
            <groupId>de.wellnerbou</groupId>
            <artifactId>git-changelog</artifactId>
            <version>VERSION</version>
        </dependency>
        
Then, import the GitChangelog class, create an instance of the GitChangelog object and use it, as the command line
entry point (main() in GitChangelog) is doing.

# Example: Changelog of [Jenkins](http://jenkins-ci.org/)' latest release

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
