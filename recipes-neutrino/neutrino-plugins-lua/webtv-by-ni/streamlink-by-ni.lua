-- 13/03/2026 by developers
-- Version 0.3.0 (faster startup with quality profile + short cache)

local function pop(cmd)
	local f = assert(io.popen(cmd, "r"))
	local s = f:read("*a")
	f:close()
	return s
end

local function read_file_line(path)
	local f = io.open(path, "r")
	if not f then
		return nil
	end
	local line = f:read("*l")
	f:close()
	return line
end

local function get_cfg_dir()
	if DIR and DIR.CONFIGDIR then
		return DIR.CONFIGDIR
	end
	return "/var/tuxbox/config"
end

local function get_livestream_resolution()
	local fallback = 1280
	if configfile and configfile.new then
		local c = configfile.new()
		if c then
			c:loadConfig(get_cfg_dir() .. "/neutrino.conf")
			return c:getInt32("livestreamResolution", fallback)
		end
	end

	local line = read_file_line(get_cfg_dir() .. "/neutrino.conf")
	if not line then
		return fallback
	end
	return fallback
end

local function quality_profile()
	local res = get_livestream_resolution()
	if res <= 640 then
		return "480p,360p,best"
	end
	if res <= 1280 then
		return "720p,480p,360p,best"
	end
	-- Full quality profile for FullHD+ displays.
	return "1080p,720p,3300k,2300k,2100k,480p,best"
end

local function cache_file_for_url(u)
	local key = tostring(u or ""):gsub("[^%w]", "_")
	if #key > 140 then
		key = key:sub(1, 140)
	end
	return "/tmp/neutrino-streamlink-" .. key .. ".cache"
end

local function read_cache(u)
	local path = cache_file_for_url(u)
	local f = io.open(path, "r")
	if not f then
		return nil
	end
	local ts = tonumber(f:read("*l") or "")
	local val = f:read("*l")
	f:close()
	if not ts or not val or val == "" then
		return nil
	end
	-- Keep cache short to avoid stale signed URLs.
	if (os.time() - ts) > 300 then
		return nil
	end
	return val
end

local function write_cache(u, val)
	if not val or val == "" then
		return
	end
	local path = cache_file_for_url(u)
	local f = io.open(path, "w")
	if not f then
		return
	end
	f:write(tostring(os.time()))
	f:write("\n")
	f:write(val)
	f:write("\n")
	f:close()
end

local function is_http_url(u)
	return u and (u:match("^https://") or u:match("^http://"))
end

local url = arg[1]
if not url then
	print("Keine URL uebergeben")
	return nil
end

local final_url = url

local function json_escape(s)
	s = tostring(s or "")
	s = s:gsub("\\", "\\\\")
	s = s:gsub("\"", "\\\"")
	s = s:gsub("\b", "\\b")
	s = s:gsub("\f", "\\f")
	s = s:gsub("\n", "\\n")
	s = s:gsub("\r", "\\r")
	s = s:gsub("\t", "\\t")
	return s
end

-- stvp links resolve via redirect
if url:match("stvp") then
	local cmd = string.format(
		"timeout -k 1 8 curl -kLs --max-time 6 -o /dev/null -w %%{url_effective} %q",
		url
	)
	final_url = pop(cmd)
	final_url = final_url:gsub("%s+$", "")
end

if not final_url or final_url == "" then
	return nil
end

local cached_url = read_cache(final_url)
if cached_url and cached_url ~= "" then
	return string.format("{\"url\":\"%s\"}", json_escape(cached_url))
end

-- Keep streamlink optional. If command is missing or returns empty,
-- use the resolved URL directly.
local qualities = quality_profile()
local cmd = string.format(
	"timeout -k 2 12 streamlink --retry-open 1 --retry-streams 1 --stream-timeout 5 %q %s --stream-url 2>/dev/null",
	final_url,
	qualities
)

local stream_url = pop(cmd)
stream_url = stream_url:gsub("%s+$", "")
if not is_http_url(stream_url) then
	stream_url = final_url
else
	write_cache(final_url, stream_url)
end

return string.format("{\"url\":\"%s\"}", json_escape(stream_url))
