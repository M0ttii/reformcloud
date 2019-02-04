/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.versioneering;

import com.google.gson.stream.JsonReader;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.utility.StringUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * @author _Klaro | Pasqual K. / created on 08.01.2019
 */

final class VersionLoader {
    static String getNewestVersion() {
        try {
            //TODO: change link
            URLConnection urlConnection = new URL("https://dl.klarcloudservice.de/update/internal/version.json").openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            urlConnection.setUseCaches(false);
            urlConnection.connect();

            try (JsonReader jsonReader = new JsonReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8))) {
                return ReformCloudLibraryService.PARSER.parse(jsonReader).getAsJsonObject().get("version").getAsString();
            }
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while checking newest version", ex);
        }
        return StringUtil.NULL;
    }
}
