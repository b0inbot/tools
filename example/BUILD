load("@boinsoft_tools//:rules/ziptool.bzl", "ziptool")
load("@rules_pkg//pkg:zip.bzl", "pkg_zip")
load("@rules_shell//shell:sh_test.bzl", "sh_test")

pkg_zip(
    name = "one",
    srcs = [
        "one/file1",
    ],
    compression_level = 4,
    package_dir = "a",
    strip_prefix = "/example/one",
)

pkg_zip(
    name = "two",
    srcs = [
        "two/file2",
    ],
    compression_level = 4,
    package_dir = "a",
    strip_prefix = "/example/two",
)

pkg_zip(
    name = "three",
    srcs = [
        "three/file2",
    ],
    compression_level = 4,
    package_dir = "a",
    strip_prefix = "/example/three",
)

ziptool.overlay(
    name = "out",
    deps = [
        ":one",
        ":three",
        ":two",
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
