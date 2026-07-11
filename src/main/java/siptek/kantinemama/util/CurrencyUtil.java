package siptek.kantinemama.util;

import java.util.Locale;

public class CurrencyUtil {
    private static final Locale ID_LOCALE = new Locale("id", "ID");

    public static String formatRupiah(double amount) {
        return String.format(ID_LOCALE, "Rp %,.0f", amount);
    }

    public static String formatRaw(double amount) {
        return String.format(ID_LOCALE, "%,.0f", amount);
    }
}
