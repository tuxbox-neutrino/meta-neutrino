SUMMARY = "API reference for Neutrino's web interface"
DESCRIPTION = "An extension of Neutrino's web interface that shows the box's \
own description of its REST interface, /api/openapi.json, in the Swagger UI \
viewer: every route with what it takes and answers, and a form to try it on \
the box. The viewer comes with swagger-ui-dist."
HOMEPAGE = "https://github.com/tuxbox-neutrino/neutrino-webui-ext-api-docs"
SECTION = "network"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

inherit gitpkgv allarch
PKGV = "${GITPKGVTAG}"

SRC_URI = "git://github.com/tuxbox-neutrino/neutrino-webui-ext-api-docs.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/git"

# for the path of the web interface (N_PRIVATE_HTTPDDIR)
include ../neutrino/neutrino-common-vars.inc

# its page loads the web interface's scripts, which neutrino brings, and
# draws with the viewer swagger-ui-dist installs
RDEPENDS:${PN} = "neutrino swagger-ui-dist"

INHIBIT_DEFAULT_DEPS = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

# Into the web interface's extensions folder at the root of the
# filesystem, where neutrino installs the interface and nhttpd serves it.
do_install() {
	oe_runmake DESTDIR=${D} HTTPDDIR=${N_PRIVATE_HTTPDDIR} install
}

FILES:${PN} = "${N_PRIVATE_HTTPDDIR}/extensions/api-docs"
