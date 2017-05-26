# Change Log

## Version 0.2.0 _(2017-05-26)_

* Fix: Avoid potential collisions between property names and identifiers used within generated `JsonAdapter`s.
* Fix: Emit a type witness when requesting an adapter for a `@Nullable` parameterized property. Previously, javac was unable to infer the correct type argument for the method call and compilation would fail.
* New: Update to AutoValue 1.4. This release allows extensions to opt out of generating a class in the AutoValue hierarchy, so we can avoid creating useless subclasses of your `@AutoMoshi` annotated classes.
* New: Update to Moshi 1.5.

## Version 0.1.0 _(2017-01-09)_

Initial release.
