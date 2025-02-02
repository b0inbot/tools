load("@boinsoft_tools//:rules/ziptool.bzl", "ziptool")

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
