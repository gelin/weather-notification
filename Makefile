.PHONY: build
build:
	./gradlew assembleDebug

.PHONY: release
release:
	@stty -echo && read -p "Key password: " pwd && stty echo && \
	STORE_PASSWORD=$$pwd KEY_PASSWORD=$$pwd ./gradlew assembleRelease

.PHONY: %-build
%-build:
	./gradlew :$*:assembleDebug

.PHONY: %-release
%-release:
	@stty -echo && read -p "Key password: " pwd && stty echo && \
	STORE_PASSWORD=$$pwd KEY_PASSWORD=$$pwd ./gradlew :$*:assembleRelease

.PHONY: core-copy
core-copy:
	rsync -av core/build/outputs/apk/release/core-release.apk \
		ftp.gelin.ru:domains/gelin.ru/public_html/android/weather-notification/apks/weather-notification-$(shell ./gradlew -q core:printVersionName).apk

SKINNAMES = bigger-text black-text black-text-plus tulip-one white-text white-text-plus
SKINS = $(addprefix skin-,$(SKINNAMES))
APPS = core $(SKINS)

.PHONY: list
list:
	@echo $(APPS)

.PHONY: clean
clean:
	./gradlew clean

.PHONY: javadoc
javadoc:
	./gradlew aggregateJavadocs
