include neutrino-plugins-ni-env.inc

DESCRIPTION = "STP-Update"

# Relax warnings to avoid -Werror breaks
TARGET_CFLAGS:append = " -Wno-format-security -Wno-format-overflow -Wno-sign-compare -Wno-pointer-sign"
