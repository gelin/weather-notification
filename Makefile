.PHONY: build
build:
	./gradlew assembleDebug

.PHONY: core-build
core-build:
	./gradlew :core:assembleDebug

.PHONY: core-release
core-release:
	@stty -echo && read -p "Key password: " pwd && stty echo && \
	STORE_PASSWORD=$$pwd KEY_PASSWORD=$$pwd ./gradlew :core:assembleRelease
