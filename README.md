# Dynamic Proxies Samples

## Benchmarks

To exercise the benchmarks, run the following in a terminal:

 1. mvn clean install
 2. mvn verify -DskipTests -Prun-benchmarks
 
The first command will install the JAR for the core project in your local Maven
 repository. The second command will use the core JAR to run the benchmarks.

The forking required by JMH does not work when running the benchmarks using the
 Maven plugin in IntelliJ IDEA 2019.2.4.
 
A note about creating a JPMS (Java) module for the benchmarks.
 This does not work because there is a package clash between jmh-core and 
 jmh-generator-annprocess which prevents them being loaded as automatic modules
 at the same time.  jmh-core has a package `org.openjdk.jmh.generators.core`
 which clashes with `org.openjdk.jmh.generators` in jmh-generator-annprocess.
 
## Releasing
 
To create a new release, run the following in a terminal:
 
 1. mvn release:prepare
 2. mvn release:clean
 
Step 1 creates a git tag after committing a change to the POMs with a release
 version number. The user is prompted for the release version number and the
 next snapshot number. Numbering follows
 [semantic versioning](https://semver.org/).

Two commits will be added similar to:

* [maven-release-plugin] prepare release dynamic-proxies-samples-1.0.0
* [maven-release-plugin] prepare for next development
 
Step 2 deletes support files that would be used for `mvn release:perform`. We
 are not running that step which is for publishing to a remote maven
 repository. Such publication is highly discouraged when using automatic modules
 such as spark.core. The warning during the Maven compile step is:
```
********************************************************************************************************************************************
* Required filename-based automodules detected: [spark-core-2.8.0.jar]. Please don't publish this project to a public artifact repository! *
********************************************************************************************************************************************
```
 
The release jar and sources jar can be found in the target directory  and can be
 rebuilt at any time by checking out the tag and running `mvn package`.
 
The release is done by the
 [Maven Release Plugin](https://maven.apache.org/maven-release/maven-release-plugin/).

