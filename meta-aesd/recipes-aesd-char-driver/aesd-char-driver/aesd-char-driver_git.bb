LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "git://git@github.com/cu-ecen-aeld/assignments-3-and-later-sk614.git;protocol=ssh;branch=main \
           file://aesdchar \
"

SRCREV = "e42a070a6045dfa11a86ba7ab3e27b9e726d8ff0"

PV = "1.0+git${SRCPV}"

S = "${WORKDIR}/git/aesd-char-driver"

inherit module update-rc.d

INITSCRIPT_NAME = "aesdchar"
INITSCRIPT_PARAMS = "start 98 2 3 4 5 . stop 02 0 1 6 ."

FILES:${PN} += "${sysconfdir}/init.d/aesdchar"

do_compile() {
    unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS

    oe_runmake \
        KERNELDIR=${STAGING_KERNEL_DIR} \
        CC="${KERNEL_CC}" \
        LD="${KERNEL_LD}" \
        AR="${KERNEL_AR}" \
        O=${STAGING_KERNEL_BUILDDIR}
}

do_install() {
    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra

    install -m 0644 ${S}/aesdchar.ko \
        ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/aesdchar.ko

    install -d ${D}${sysconfdir}/init.d

    install -m 0755 ${WORKDIR}/aesdchar \
        ${D}${sysconfdir}/init.d/aesdchar
}

RDEPENDS:${PN} += "kernel-module-aesdchar"
