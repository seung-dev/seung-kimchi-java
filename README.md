# seung-kimchi-java

##### Import Project in STS

- settings.gradle

```
rootProject.name = "my-project-name"

include "seung-kimchi-java"
project(":seung-kimchi-java").projectDir = new File("my-project-path/seung-kimchi-java")
```

- gradle.build

```
...
implementation(project(path: ":seung-kimchi-java", configuration: "default")) {
    exclude module: "slf4j-simple"
}
...
```

- jitpack gradle.build
```
...
repositories {
	mavenCentral()
	maven { url "https://jitpack.io" }
}
...
dependencies {
	...
	// seung
	implementation("com.github.seung-dev:seung-kimchi-java:1.0.11") {
		exclude module: "slf4j-simple"
	}
	...
}
...
```
