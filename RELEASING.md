# Releasing

In order to sign and deploy release builds, the following Gradle properties must be defined:

```properties
repoUsername=username
repoPassword=abc123
signing.keyId=FEDCBA98
signing.password=abc123
signing.secretKeyRingFile=/path/to/secring.gpg
```

The most convenient way to do this is to create a `gradle.properties` file in the root directory of the project.
Alternate ways to define these properties can be found in the [Gradle user guide][gradle].

## Checklist

1. Change the version in `build.gradle` to a non-SNAPSHOT version.
2. Update `CHANGELOG.md` and `README.md` for the impending release.
4. `git commit -am "Prepare for release X.Y.Z"` (where X.Y.Z is the new version).
5. `./gradlew clean build uploadArchives`
6. Visit [Sonatype Nexus][nexus] and promote the artifact.
7. `git tag -s X.Y.Z -m "Version X.Y.Z"` (where X.Y.Z is the new version).
8. Change the version in `build.gradle` to the next SNAPSHOT version.
9. `git commit -am "Prepare next development version"`
10. `git push && git push --tags`

[gradle]: https://docs.gradle.org/current/userguide/build_environment.html
[nexus]: https://oss.sonatype.org/
