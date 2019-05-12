/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.backup.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import systems.reformcloud.utility.files.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 12.05.2019
 */

public final class FTPUtil implements Serializable {
    public static void uploadDirectory(FTPClient ftpClient, String dirPath, List<String> excluded) throws IOException {
        createDirectory();
        final String name = getCurrentFileName();
        final String dir = "reformcloud/addons/backup/waiting/" + name;
        ZipUtil.zipDirectoryToFile(new File(dirPath), dir, excluded);
        ftpClient.makeDirectory(dirPath);
        uploadFile(ftpClient, dir, dirPath + "/" + name);
        deleteFile(dir);
    }

    public static void uploadFile(FTPClient ftpClient, String zipPath, String remoteDir) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(zipPath));
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.storeFile(remoteDir, fileInputStream);
        fileInputStream.close();
    }

    public static void openConnection(FTPClient ftpClient, String host, int port, String userName, String password) throws IOException {
        ftpClient.connect(host, port);
        ftpClient.login(userName, password);
        ftpClient.enterLocalPassiveMode();
    }

    public static void closeConnection(FTPClient ftpClient) throws IOException {
        ftpClient.logout();
        ftpClient.disconnect();
    }

    private static void createDirectory() {
        FileUtils.createDirectory(Paths.get("reformcloud/addons/backup/waiting"));
    }

    private static void deleteFile(String name) {
        FileUtils.deleteFileIfExists(name);
    }

    private static String getCurrentFileName() {
        return "backup-" + System.currentTimeMillis() + ".zip";
    }
}
