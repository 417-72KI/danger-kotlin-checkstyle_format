.SILENT:
.PHONY: tag release printVersion format test

tag: test
	./gradlew releaseTag

release: tag

printVersion:
	./gradlew printVersion

format:
	./gradlew ktlintFormat

test:
	./gradlew --continue clean ktlintCheck test --stacktrace
