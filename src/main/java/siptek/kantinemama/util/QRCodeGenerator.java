package siptek.kantinemama.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;

public class QRCodeGenerator {
    public static Image generate(String data, int size) throws WriterException {
        BitMatrix matrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(matrix);
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }
}
