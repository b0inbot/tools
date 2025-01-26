#load("@rules_java//java:defs.bzl", "JavaInfo")

def _ziptool_overlay_impl(ctx):
    if ctx.attr.output_file:
        out = ctx.actions.declare_file(ctx.attr.output_file)
    else:
        out = ctx.actions.declare_file(ctx.label.name + ".zip")

    args = ctx.actions.args()
    args.add("overlay")
    args.add(out)
    for z in ctx.files.deps:
        args.add(z)

    inputs = depset(ctx.files.deps)

    ctx.actions.run(
        executable = ctx.executable._ziptool,
        arguments = [args],
        inputs = inputs,
        outputs = [out],
        mnemonic = "ZipToolOverlay",
        progress_message = "Joining ZIP files for %s" % ctx.label,
        toolchain = "@bazel_tools//tools/jdk:current_java_toolchain",
    )

    return [
        DefaultInfo(files = depset([out])),
    ]

ziptool_overlay = rule(
    attrs = {
        "deps": attr.label_list(
            providers = [],
            allow_files = True,
        ),
        "output_file": attr.string(),
        "_ziptool": attr.label(
            executable = True,
            cfg = "exec",
            default = Label("@boinsoft_tools//:ziptool"),
            allow_files = True,
        ),
        "_java_toolchain": attr.label(
            default = "@bazel_tools//tools/jdk:current_java_toolchain",
        ),
    },
    provides = [DefaultInfo],
    implementation = _ziptool_overlay_impl,
    toolchains = [],
)

ziptool = struct(
    overlay = ziptool_overlay,
)
