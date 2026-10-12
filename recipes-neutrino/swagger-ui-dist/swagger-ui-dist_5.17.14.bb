# for the path of the web interface (N_PRIVATE_HTTPDDIR)
include ../neutrino/neutrino-common-vars.inc

SUMMARY = "Swagger UI for the API reference of the Neutrino web interface"
DESCRIPTION = "The Swagger UI bundle and stylesheet that the API reference of \
the Neutrino web interface, the extension api-docs, draws with. Optional: \
about 425 KB compressed; development images carry it with the extension, \
other images get it from the feed. Without it the page says that the viewer \
is missing."
HOMEPAGE = "https://github.com/swagger-api/swagger-ui"
SECTION = "network"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
                    file://NOTICE;md5=342625133694cfa71ee5eb6126af5825"

# The prebuilt distribution package from the npm registry, fetched as a plain
# archive: nothing here needs node, and the two files the page loads are
# taken as they are. Unpacked one level down: the archive's top directory is
# called package, which is also the directory do_package builds in.
SRC_URI = "https://registry.npmjs.org/swagger-ui-dist/-/swagger-ui-dist-${PV}.tgz;subdir=${BP}"
SRC_URI[sha256sum] = "c57badf459aa6e65cc036b3862d0502a63f9a22546407ffcb0e64f85f816bb28"

S = "${WORKDIR}/${BP}/package"

inherit allarch

INHIBIT_DEFAULT_DEPS = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

# Where neutrino installs and nhttpd serves its web interface: the configured
# directory as it stands, at the root of the filesystem and not under the prefix.
SWAGGER_VENDOR_DIR = "${N_PRIVATE_HTTPDDIR}/swagger/vendor"

# Stored compressed: nhttpd answers swagger-ui-bundle.js out of
# swagger-ui-bundle.js.gz with Content-Encoding gzip, which takes the pair from
# about 1.6 MB to about 425 KB. LICENSE and NOTICE go along, as Apache-2.0
# asks of a redistribution.
do_install() {
	install -d ${D}${SWAGGER_VENDOR_DIR}
	for f in swagger-ui-bundle.js swagger-ui.css; do
		gzip -9 -n -c ${S}/$f > ${D}${SWAGGER_VENDOR_DIR}/$f.gz
		chmod 0644 ${D}${SWAGGER_VENDOR_DIR}/$f.gz
	done
	install -m 0644 ${S}/LICENSE ${S}/NOTICE ${D}${SWAGGER_VENDOR_DIR}/
}

FILES:${PN} = "${SWAGGER_VENDOR_DIR}"
