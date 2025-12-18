include neutrino-plugins-ni-env.inc

DESCRIPTION = "Hide channellogos."

# Relax warnings to avoid -Werror breaks
TARGET_CFLAGS:append = " -Wno-shadow -Wno-array-bounds -Wno-unused-parameter -Wno-unused-variable"
