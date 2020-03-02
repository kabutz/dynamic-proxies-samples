# Releasing to Maven Central

The documentation by Sonatype is at
 https://central.sonatype.org/pages/producers.html

To create a new release, run the following in a terminal:
 
 1. mvn release:prepare
 2. mvn -P ossrh release:perform
 3. mvn release:clean
 
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
 Signing of jars is done using a GPG key as described at
 https://central.sonatype.org/pages/working-with-pgp-signatures.html

The following is required in ~/.m2/settings.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<settings>
    <profiles>
        <profile>
            <id>ossrh</id>
            <activation>
                <activeByDefault>false</activeByDefault>
            </activation>
            <properties>
                <gpg.executable>gpg</gpg.executable>
                <gpg.passphrase>your-gpg-passphrase</gpg.passphrase>
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

Step 3 deletes support files used by `release:perform`.
 
The release jar, sources jar and javadoc jar can also be found in the target 
 directory and can be rebuilt at any time by checking out the tag and running
 `mvn package`.
 
The release is done by the
 [Maven Release Plugin](https://maven.apache.org/maven-release/maven-release-plugin/).
