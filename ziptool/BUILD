load("@boinsoft_tools//private:artifact.bzl", artifact = "tools_artifact")
load("@rules_java//java:java_binary.bzl", "java_binary")
load("@rules_java//java:java_library.bzl", "java_library")
load("@rules_java//java:java_test.bzl", "java_test")
load("@rules_pkg//pkg:zip.bzl", "pkg_zip")

pkg_zip(
    name = "one",
    srcs = [
        "one/1",
    ],
    compression_level = 4,
    package_dir = "a",
    strip_prefix = "/ziptool/one",
)

pkg_zip(
    name = "two",
    srcs = [
        "two/2",
    ],
    compression_level = 4,
    package_dir = "a",
    strip_prefix = "/ziptool/two",
)

pkg_zip(
    name = "three",
    srcs = [
        "three/1",
    ],
    compression_level = 4,
    package_dir = "a",
    strip_prefix = "/ziptool/three",
)

java_library(
    name = "zipoverlay-lib",
    srcs = [
        "ManyZipEntryStream.java",
        "ReadableZipEntry.java",
        "ZipOverlay.java",
    ],
    visibility = ["//visibility:public"],
    deps = [
        artifact("io.vavr:vavr"),
        artifact("commons-cli:commons-cli"),
        "@boinsoft_tools//cli:cli-lib",
    ],
)

java_library(
    name = "zipoverlay-tests",
    srcs = ["ManyZipEntryStreamTests.java"],
    deps = [
        ":zipoverlay-lib",
        artifact("io.vavr:vavr"),
        artifact("org.junit.jupiter:junit-jupiter-api"),
        artifact("org.junit.jupiter:junit-jupiter-params"),
    ],
)

java_test(
    name = "ManyZipEntryStreamTests",
    size = "small",
    args = [
        "execute",
        "--select-class",
        "boinsoft.tools.ziptool.ManyZipEntryStreamTests",
    ],
    data = [
        ":one",
        ":three",
        ":two",
    ],
    main_class = "org.junit.platform.console.ConsoleLauncher",
    test_class = "boinsoft.tools.zip_overlay.ManyZipEntryStreamTests",
    runtime_deps = [
        ":zipoverlay-lib",
        ":zipoverlay-tests",
        artifact("org.junit.jupiter:junit-jupiter-engine"),
        artifact("org.junit.platform:junit-platform-launcher"),
        artifact("org.junit.platform:junit-platform-console"),
        artifact("org.junit.platform:junit-platform-reporting"),
    ],
)

java_library(
    name = "ziptool-lib",
    srcs = [
        "ZipToolMain.java",
    ],
    visibility = ["//visibility:public"],
    deps = [
        artifact("io.vavr:vavr"),
        artifact("commons-cli:commons-cli"),
        ":zipoverlay-lib",
        "@boinsoft_tools//cli:cli-lib",
    ],
)

java_binary(
    name = "ziptool",
    main_class = "boinsoft.tools.ziptool.ZipToolMain",
    visibility = ["//visibility:public"],
    runtime_deps = [
        ":ziptool-lib",
    ],
)
