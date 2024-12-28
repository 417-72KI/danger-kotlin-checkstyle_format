FROM ghcr.io/danger/danger-kotlin:1.3.3

WORKDIR /work

COPY gradle /work/gradle
COPY gradlew /work/gradlew

RUN ./gradlew -v >/dev/null

COPY buildSrc /work/buildSrc
COPY build.gradle.kts /work/build.gradle.kts
COPY settings.gradle.kts /work/settings.gradle.kts

RUN ./gradlew tasks >/dev/null

COPY gradle.properties /work/gradle.properties
COPY src /work/src

RUN ./gradlew installPluginFromLocal

ENTRYPOINT [ "danger-kotlin" ]
CMD [ "pr" ]
