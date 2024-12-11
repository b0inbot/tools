load("@rules_jvm_external//:defs.bzl", "artifact")

def tools_artifact(a):
    return artifact(a, repository_name = "boinsoft_tools_maven")
