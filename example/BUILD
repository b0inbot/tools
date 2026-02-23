load("@boinsoft_tools//:rules/ziptool.bzl", "ziptool")
load("@rules_shell//shell:sh_test.bzl", "sh_test")

ziptool.overlay(
    name = "out",
    deps = [
        "one.zip",
        "two.zip",
    ],
)

sh_test(
    name = "ziptool_overlay_test",
    size = "small",
    srcs = ["ziptool_overlay_test.sh"],
    data = [
        "expected-output.txt",
        ":out",
    ],
    deps = [],
)
