load("@boinsoft_tools//private:artifact.bzl", artifact = "tools_artifact")
#load("@contrib_rules_jvm//java:defs.bzl", "java_junit5_test", "java_test_suite")

exports_files([
    "one.zip",
    "two.zip",
])

java_library(
    name = "zip_overlay-lib",
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
    name = "zip_overlay-tests",
    srcs = ["ManyZipEntryStreamTests.java"],
    deps = [
        ":zip_overlay-lib",
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
        "boinsoft.tools.zip_overlay.ManyZipEntryStreamTests",
    ],
    data = [
        ":one.zip",
        ":two.zip",
    ],
    main_class = "org.junit.platform.console.ConsoleLauncher",
    test_class = "boinsoft.tools.zip_overlay.ManyZipEntryStreamTests",
    runtime_deps = [
        ":zip_overlay-lib",
        ":zip_overlay-tests",
        artifact("org.junit.jupiter:junit-jupiter-engine"),
        artifact("org.junit.platform:junit-platform-launcher"),
        artifact("org.junit.platform:junit-platform-console"),
        artifact("org.junit.platform:junit-platform-reporting"),
        #     "@contrib_rules_jvm//java/src/com/github/bazel_contrib/contrib_rules_jvm/junit5",
    ],
)

java_binary(
    name = "zip_overlay",
    main_class = "boinsoft.tools.zip_overlay.ZipOverlay",
    visibility = ["//visibility:public"],
    runtime_deps = [
        ":zip_overlay-lib",
    ],
)
