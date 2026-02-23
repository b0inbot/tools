load("@boinsoft_tools//private:artifact.bzl", artifact = "tools_artifact")
load("@rules_java//java:java_binary.bzl", "java_binary")
load("@rules_java//java:java_library.bzl", "java_library")

java_library(
    name = "xmlformat-lib",
    srcs = [
        "XMLFormat.java",
    ],
    visibility = ["//visibility:public"],
    deps = [
        artifact("io.vavr:vavr"),
        artifact("commons-cli:commons-cli"),
        artifact("org.dom4j:dom4j"),
        "@boinsoft_tools//cli:cli-lib",
    ],
)

java_library(
    name = "xmltool-lib",
    srcs = [
        "Main.java",
    ],
    visibility = ["//visibility:public"],
    deps = [
        artifact("io.vavr:vavr"),
        artifact("commons-cli:commons-cli"),
        ":xmlformat-lib",
        "@boinsoft_tools//cli:cli-lib",
    ],
)

java_binary(
    name = "xmltool",
    main_class = "boinsoft.tools.xmltool.Main",
    visibility = ["//visibility:public"],
    runtime_deps = [
        ":xmltool-lib",
    ],
)

java_binary(
    name = "xmlformat",
    main_class = "boinsoft.tools.xmltool.XMLFormat",
    visibility = ["//visibility:public"],
    runtime_deps = [
        ":xmltool-lib",
    ],
)
