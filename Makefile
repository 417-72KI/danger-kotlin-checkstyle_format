.SILENT:
.PHONY: tag release printVersion format test danger-local

tag: test
	./gradlew releaseTag

release: tag

printVersion:
	./gradlew printVersion

format:
	./gradlew ktlintFormat

test:
	./gradlew --continue clean ktlintCheck test --stacktrace

danger-local:
	@docker build . -t danger-kotlin-checkstyle_format-debugger
	@docker run --rm \
	-v `pwd`:/work \
	-w /work \
	-e DANGER_GITHUB_API_TOKEN=${GITHUB_TOKEN} \
	-e WORKING_DIR:/work \
	-it danger-kotlin-checkstyle_format-debugger \
	pr "$(shell gh pr view --json url --jq .url)"
