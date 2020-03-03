# Releasing to Maven Central

The documentation by Sonatype is at
 https://central.sonatype.org/pages/producers.html

To create a new release, run the following in a terminal:
 
 1. mvn -P ossrh release:prepare
 2. mvn -P ossrh release:perform
 
Step 1 creates a git tag after committing a change to the POMs with a release
 version number. The user is prompted for the release version number and the
 next snapshot number. Numbering follows
 [semantic versioning](https://semver.org/).

Two commits will be added similar to:

* [maven-release-plugin] prepare release dynamic-proxies-samples-1.0.0
* [maven-release-plugin] prepare for next development
 
Step 2 sends the release to Maven Central.
 You must be registered with Sonatype in order to do a release.
 Your user token can be found in your profile at https://oss.sonatype.org/
 Signing of jars is done using a PGP private key, set up as described at
 https://central.sonatype.org/pages/working-with-pgp-signatures.html.
 The bare minimum from this document is to run
 `gpg --gen-key` and
 `gpg2 --keyserver hkp://pool.sks-keyservers.net --send-keys <keyid>`

The following is required in ~/.m2/settings.xml.
The gpg profile will prompt for a password for the PGP private key.
The server element holds credentials for Sonatype.

```
<?xml version="1.0" encoding="UTF-8"?>
<settings>
    <profiles>
        <profile>
            <id>gpg</id>
            <properties>
                <gpg.useagent>true</gpg.useagent>
            </properties>
        </profile>
    </profiles>
    <servers>
        <server>
            <id>ossrh</id>
            <username>from-your-sonatype-user-token</username>
            <password>from-your-sonatype-user-token</password>
        </server>
    </servers>
</settings>
```

To ensure that the password prompting is done by the GPG agent, add
 `export GPG_TTY=$(tty)` to ~/.zprofile or equivalent.

The release jar, sources jar and javadoc jar can also be found in the target 
 directory and can be rebuilt at any time by checking out the tag and running
 `mvn package`.

The release is done by the
 [Maven Release Plugin](https://maven.apache.org/maven-release/maven-release-plugin/).
