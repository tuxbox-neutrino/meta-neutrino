# Common configure flags shared between Neutrino and neutrino-plugins.
# Keep this aligned with neutrino-common-vars.inc path variables.

NEUTRINO_OECONF_BASE = " \
    --with-target=native \
    --prefix=${N_PREFIX} \
"

NEUTRINO_OECONF_DIRS_COMMON = " \
    --with-configdir=${N_CONFIG_DIR} \
    --with-controldir=${N_CONTROLDIR} \
    --with-controldir_var=${N_CONTROL_DIR_VAR} \
    \
    --with-plugindir=${N_PLUGIN_DIR} \
    --with-plugindir_var=${N_PLUGIN_DIR_VAR} \
    --with-plugindir_mnt=${N_PLUGIN_DIR_MNT} \
    --with-webtvdir=${N_WEBTV_DIR} \
    --with-webtvdir_var=${N_WEBTV_DIR_VAR} \
    --with-webradiodir=${N_WEBRADIO_DIR} \
    --with-webradiodir_var=${N_WEBRADIO_DIR_VAR} \
    --with-luaplugindir=${N_LUAPLUGIN_DIR} \
    --with-luaplugindir_var=${N_LUAPLUGIN_DIR_VAR} \
    --with-gamesdir=${N_GAMES_DIR}  \
    --with-iconsdir=${N_ICONS_DIR}  \
    --with-iconsdir_var=${N_ICONS_DIR}  \
    \
    --with-zapitdir=${N_ZAPITDIR} \
    --with-datadir_var=${N_DATADIR_VAR} \
    \
    --with-fontdir=${N_FONTDIR} \
    --with-fontdir_var=${N_FONTDIR_VAR} \
    --with-libdir=${N_LIBDIR} \
    --with-localedir=${N_LOCALEDIR} \
    --with-localedir_var=${N_LOCALEDIR_VAR} \
    --with-themesdir=${N_THEMESDIR} \
    --with-themesdir_var=${N_THEMESDIR_VAR} \
    --with-logodir=${N_LOGODIR} \
    --with-logodir_var=${N_LOGODIR_VAR} \
    --with-private_httpddir=${N_PRIVATE_HTTPDDIR} \
    --with-public_httpddir=${N_PUBLIC_HTTPDDIR} \
    --with-hosted_httpddir=${N_HOSTED_HTTPDDIR} \
    --with-flagdir=${N_FLAGDIR} \
"
