package siptek.kantinemama.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import siptek.kantinemama.model.AppState;
import siptek.kantinemama.model.AppStateSnapshot;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

public class PersistenceManager {
    private static final String FILE_PATH = System.getProperty("user.home") + "/.kantinemama/data.xml";
    private static final XStream xstream = new XStream();

    static {
        xstream.addPermission(AnyTypePermission.ANY);
    }

    public static void saveState(AppState state) {
        try {
            AppStateSnapshot snapshot = new AppStateSnapshot();
            snapshot.setMenuItems(new ArrayList<>(state.getMenuItems()));
            snapshot.setCurrentCart(new ArrayList<>(state.getCurrentCart()));
            snapshot.setOrders(new ArrayList<>(state.getOrders()));
            snapshot.setTables(new ArrayList<>(state.getTables()));
            snapshot.setStokItems(new ArrayList<>(state.getStokItems()));
            snapshot.setTransaksiHarians(new ArrayList<>(state.getTransaksiHarians()));
            snapshot.setCurrentRole(state.getCurrentRole());

            Path path = Paths.get(FILE_PATH);
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            String xml = xstream.toXML(snapshot);
            Files.writeString(path, xml);
            System.out.println("AppState successfully saved to " + FILE_PATH);
        } catch (Exception e) {
            System.err.println("Failed to save AppState: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Optional<AppStateSnapshot> loadState() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("No saved state found at " + FILE_PATH + ". Falling back to defaults.");
            return Optional.empty();
        }

        try {
            String xml = Files.readString(file.toPath());
            AppStateSnapshot snapshot = (AppStateSnapshot) xstream.fromXML(xml);
            System.out.println("AppState successfully loaded from " + FILE_PATH);
            return Optional.of(snapshot);
        } catch (Exception e) {
            System.err.println("Failed to load AppState from " + FILE_PATH + ": " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
