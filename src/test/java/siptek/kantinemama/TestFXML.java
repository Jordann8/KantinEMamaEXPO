package siptek.kantinemama;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import java.net.URL;

public class TestFXML {
    public static void main(String[] args) {
        try {
            Platform.startup(() -> {
                String[] files = {
                    "/siptek/kantinemama/view/owner/PengaturanSistem.fxml",
                    "/siptek/kantinemama/view/owner/PortalOwner.fxml",
                    "/siptek/kantinemama/view/owner/ManajemenKatalogMenu.fxml",
                    "/siptek/kantinemama/view/owner/LaporanKeuangan1.fxml"
                };
                for (String f : files) {
                    try {
                        URL url = TestFXML.class.getResource(f);
                        if (url == null) {
                            System.out.println("NOT FOUND: " + f);
                            continue;
                        }
                        FXMLLoader.load(url);
                        System.out.println("LOAD SUCCESS: " + f);
                    } catch (Exception e) {
                        System.out.println("LOAD FAILED: " + f);
                        e.printStackTrace();
                    }
                }
                Platform.exit();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
