SUMMARY = "Just-In-Time Compiler for Lua"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=d739bb9250a55c124a545b588fd76771"
HOMEPAGE = "http://luajit.org"

PV = "2.1"
SRCREV = "dd5032ed844c56964347c7916db66b0eb11d8091"
SRC_URI = "git://luajit.org/git/luajit-2.0.git;branch=v2.1;protocol=https \
           file://0001-Do-not-strip-automatically-this-leaves-the-stripping.patch \
           file://clang.patch"

PROVIDES = "virtual/lua"
RPROVIDES_${PN} += "virtual/lua"

S = "${WORKDIR}/git"

inherit pkgconfig binconfig siteinfo

BBCLASSEXTEND = "native"

# Host luajit needs to be compiled with the same pointer size
BUILD_CC_ARCH:append = " ${@['-m32',''][d.getVar('SITEINFO_BITS') != '32']}"

# The lua makefiles expect the TARGET_SYS to be from uname -s
LUA_TARGET_OS = "Unknown"
LUA_TARGET_OS:darwin = "Darwin"
LUA_TARGET_OS:linux = "Linux"
LUA_TARGET_OS:linux-gnueabi = "Linux"
LUA_TARGET_OS:mingw32 = "Windows"

# We don't want the lua buildsystem's compiler optimizations, or its
# stripping, and we don't want it to pick up CFLAGS or LDFLAGS, as those apply
# to both host and target compiles
EXTRA_OEMAKE = "\
    Q= E='@:' \
    \
    CCOPT= CCOPT_x86= CFLAGS= LDFLAGS= TARGET_STRIP='@:' \
    \
    'TARGET_SYS=${LUA_TARGET_OS}' \
    \
    'CC=${CC}' \
    'TARGET_AR=${AR} rcus' \
    'TARGET_CFLAGS=${CFLAGS}' \
    'TARGET_LDFLAGS=${LDFLAGS}' \
    'TARGET_SHLDFLAGS=${LDFLAGS}' \
    'HOST_CC=${BUILD_CC}' \
    'HOST_CFLAGS=${BUILD_CFLAGS}' \
    \
    'PREFIX=${prefix}' \
    'MULTILIB=${baselib}' \
    'LDCONFIG=:' \
"

do_compile() {
    oe_runmake
}

do_configure:append() {
    # Copy missing headers to the native sysroot if the asm directory is missing
    if [ ! -d "${STAGING_INCDIR_NATIVE}/asm" ]; then
        install -d ${STAGING_INCDIR_NATIVE}/asm
        cp -r ${STAGING_INCDIR}/asm/* ${STAGING_INCDIR_NATIVE}/asm/
    fi
}

# There's INSTALL_LIB and INSTALL_SHARE also, but the lua binary hardcodes the
# '/share' and '/' + LUA_MULTILIB paths, so we don't want to break those
# expectations.
EXTRA_OEMAKEINST = "\
    'DESTDIR=${D}' \
    'INSTALL_BIN=${D}${bindir}' \
    'INSTALL_INC=${D}${includedir}/luajit-$(MAJVER).$(MINVER)' \
    'INSTALL_MAN=${D}${mandir}/man1' \
"
do_install() {
    oe_runmake ${EXTRA_OEMAKEINST} install
    rmdir ${D}${datadir}/lua/5.* \
          ${D}${datadir}/lua \
          ${D}${libdir}/lua/5.* \
          ${D}${libdir}/lua
}

do_install:append() {
    ln -sf $(basename ${D}${bindir}/luajit-*) ${D}${bindir}/luajit
}

PACKAGES += "luajit-common"

# See the comment for EXTRA_OEMAKEINST. This is needed to ensure the hardcoded
# paths are packaged regardless of what the libdir and datadir paths are.
FILES:${PN} += "${prefix}/${baselib} ${prefix}/share"
FILES:${PN} += "${libdir}/libluajit-5.1.so.2 \
    ${libdir}/libluajit-5.1.so.${PV} \
"
FILES:${PN}-dev += "${libdir}/libluajit-5.1.a \
    ${libdir}/libluajit-5.1.so \
    ${libdir}/pkgconfig/luajit.pc \
"
FILES:luajit-common = "${datadir}/${BPN}-${PV}"

# mips64/ppc/ppc64/riscv64 is not supported in this release
COMPATIBLE_HOST:mipsarchn32 = "null"
COMPATIBLE_HOST:mipsarchn64 = "null"
COMPATIBLE_HOST:powerpc = "null"
COMPATIBLE_HOST:powerpc64 = "null"
COMPATIBLE_HOST:powerpc64le = "null"
COMPATIBLE_HOST:riscv64 = "null"
COMPATIBLE_HOST:riscv32 = "null"
