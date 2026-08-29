# Fix Hilt Build Error: Could not find class file for UniApplication

The build fails because of a case mismatch between `applicationId` ("com.onell.UniApp") and the actual package name ("com.onell.uniapp"). This causes Hilt's annotation processor to fail when searching for the Application class on case-sensitive file systems.

## Proposed Changes

### Build Configuration

#### [MODIFY] [build.gradle.kts](file:///home/onell/AndroidStudioProjects/UniApp/app/build.gradle.kts)
- Update `applicationId` to `com.onell.uniapp` to match the `namespace` and source package.

### Instrumentation Tests

#### [MODIFY] [ExampleInstrumentedTest.kt](file:///home/onell/AndroidStudioProjects/UniApp/app/src/androidTest/java/com/onell/uniapp/ExampleInstrumentedTest.kt)
- Update the expected package name in the assertion.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:hiltJavaCompileDebug` to verify Hilt compilation works.
- Run `./gradlew connectedDebugAndroidTest` (if a device is available) or at least ensure the project builds.
