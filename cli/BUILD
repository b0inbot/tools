load("@boinsoft_tools//private:artifact.bzl", artifact = "tools_artifact")

java_library(
    name = "cli-lib",
    srcs = [
        "Args.java",
        "CLI.java",
        "SimpleCLI.java",
        "UsageException.java",
    ],
    visibility = ["//visibility:public"],
    deps = [
        artifact("io.vavr:vavr"),
        artifact("commons-cli:commons-cli"),
    ],
)
