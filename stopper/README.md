This module is needed to deploy the `core` module to Maven
Central Repository without also deploying the `samples` and
`benchmark` modules.
 
If `benchmark` is the last module in the Maven Reactor sequence
and it is set not to deploy (see `skipNexusStagingDeployMojo`
ìn `benchmark/pom.xml`) then nothing at all will be deployed.

References:

* [Sonatype Manual](https://help.sonatype.com/repomanager2/staging-releases/configuring-your-project-for-deployment#ConfiguringYourProjectforDeployment-DeploymentwiththeNexusStagingMavenPlugin) - search for `skipNexusStagingDeployMojo`
* [Sonatype JIRA issue NEXUS-9138](https://issues.sonatype.org/browse/NEXUS-9138)
* [Stack Overflow question](https://stackoverflow.com/questions/25305850/how-to-disable-nexus-staging-maven-plugin-in-sub-modules)
