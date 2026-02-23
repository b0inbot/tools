load("@boinsoft_tools//private:artifact.bzl", artifact = "tools_artifact")
load("@rules_java//java:java_library.bzl", "java_library")

java_library(
    name = "cli-lib",
    srcs = [
        "Args.java",
        "CLI.java",
        "MultiCommand.java",
        "SimpleCLI.java",
        "UsageException.java",
    ],
    visibility = ["//visibility:public"],
    deps = [
        artifact("io.vavr:vavr"),
        artifact("commons-cli:commons-cli"),
    ],
)
