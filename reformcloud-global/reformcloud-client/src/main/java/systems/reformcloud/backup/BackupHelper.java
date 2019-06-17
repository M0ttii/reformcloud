/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.backup;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;
import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.backup.util.FTPUtil;
import systems.reformcloud.utility.StringUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author _Klaro | Pasqual K. / created on 12.05.2019
 */

public final class BackupHelper implements Serializable {

    private FTPConfig ftpConfig;

    private boolean deleted = false;

    private static BackupHelper instance;

    public BackupHelper(FTPConfig ftpConfig) {
        instance = this;
        this.ftpConfig = ftpConfig;
        this.start();
    }

    private void start() {
        ReformCloudLibraryService.EXECUTOR_SERVICE.execute(() -> {
            while (!deleted && !Thread.currentThread().isInterrupted()) {
                try {
                    if (this.ftpConfig.isSaveClient()) {
                        FTPClient ftpClient =
                            this.ftpConfig.isUseFTPS() ? new FTPSClient() : new FTPClient();
                        FTPUtil.openConnection(
                            ftpClient,
                            this.ftpConfig.getHost(),
                            this.ftpConfig.getPort(),
                            this.ftpConfig.getUserName(),
                            this.ftpConfig.getPassword()
                        );
                        FTPUtil.uploadDirectory(
                            ftpClient,
                            "reformcloud",
                            this.ftpConfig.getExcluded(),
                            this.ftpConfig.isDeleteLocalBackupAfterUpload()
                        );
                        FTPUtil.closeConnection(ftpClient);
                    }
                } catch (final IOException ex) {
                    StringUtil.printError(
                        ReformCloudClient.getInstance().getColouredConsoleProvider(),
                        "Error while opening ftp connection",
                        ex
                    );
                }

                ReformCloudLibraryService
                    .sleep(TimeUnit.MINUTES, this.ftpConfig.getSaveIntervalInMinutes());
            }
        });
    }

    public static BackupHelper getInstance() {
        return instance;
    }

    public void close() {
        instance = null;
        this.deleted = true;
    }
}
