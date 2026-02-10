# Neutrino box type/model mapping for Neutrino build configuration.
#
# This maps MACHINE/MACHINEBUILD to valid --with-boxtype/--with-boxmodel values.
# Override with NEUTRINO_BOXTYPE/NEUTRINO_BOXMODEL in local.conf if needed.

def neutrino_boxmap(d):
    import bb

    machine = (d.getVar("MACHINE") or "").strip().lower()
    machinebuild = (d.getVar("MACHINEBUILD") or "").strip().lower()
    warn = getattr(bb, "warnonce", bb.warn)

    # Resolve the candidate model name (explicit override wins).
    candidate = machinebuild or machine

    alias_map = {
        "qemux86": "generic",
        "qemux86-64": "generic",
        "qemux86_64": "generic",
        "mutant51": "hd51",
        "ax51": "hd51",
        "ax60": "hd60",
        "nevis": "hd1",
        "apollo": "hd2",
    }

    boxmodels_generic = {"generic", "raspi"}
    boxmodels_coolstream = {"hd1", "hd2"}
    boxmodels_armbox = {
        "hd60", "hd61", "multibox", "multiboxse", "hd51",
        "bre2ze4k", "h7", "e4hdultra", "protek4k",
        "osmini4k", "osmio4k", "osmio4kplus",
        "vusolo4k", "vuduo4k", "vuduo4kse", "vuultimo4k",
        "vuuno4k", "vuuno4kse", "vuzero4k",
    }
    boxmodels_mipsbox = {"vuduo", "vuduo2", "gb800se", "osnino", "osninoplus", "osninopro"}
    known_boxmodels = boxmodels_generic | boxmodels_coolstream | boxmodels_armbox | boxmodels_mipsbox

    boxmodel = alias_map.get(candidate, candidate)

    # MACHINE is the canonical OE machine name. If MACHINEBUILD is a brand/model
    # alias that we do not map explicitly, use MACHINE when it is known.
    if boxmodel not in known_boxmodels and machine in known_boxmodels:
        boxmodel = machine

    if boxmodel in boxmodels_generic:
        boxtype = "generic"
    elif boxmodel in boxmodels_coolstream:
        boxtype = "coolstream"
    elif boxmodel in boxmodels_armbox:
        boxtype = "armbox"
    elif boxmodel in boxmodels_mipsbox:
        boxtype = "mipsbox"
    else:
        arch = (d.getVar("TARGET_ARCH") or "").lower()
        if arch.startswith("arm") or arch == "aarch64":
            boxtype = "armbox"
        elif arch.startswith("mips"):
            boxtype = "mipsbox"
        elif arch in {"x86_64", "x86", "i386", "i486", "i586", "i686"}:
            boxtype = "generic"
            boxmodel = "generic"
        else:
            boxtype = "generic"
            boxmodel = "generic"

        warn("Neutrino boxmap: unknown machine '%s' (build '%s'); using boxtype '%s' boxmodel '%s'" %
             (machine or "?", machinebuild or "?", boxtype, boxmodel))

    if not boxmodel:
        boxmodel = "generic"
        warn("Neutrino boxmap: empty boxmodel, defaulting to 'generic'")

    return (boxtype, boxmodel)


def neutrino_boxmap_type(d):
    return neutrino_boxmap(d)[0]


def neutrino_boxmap_model(d):
    return neutrino_boxmap(d)[1]


NEUTRINO_BOXTYPE ?= "${@neutrino_boxmap_type(d)}"
NEUTRINO_BOXMODEL ?= "${@neutrino_boxmap_model(d)}"
