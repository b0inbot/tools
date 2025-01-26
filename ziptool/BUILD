load("@boinsoft_tools//private:artifact.bzl", artifact = "tools_artifact")

exports_files([
    "one.zip",
    "two.zip",
])

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
        ":one.zip",
        ":two.zip",
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
