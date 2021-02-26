package ResizerSourse;

import lombok.AllArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@AllArgsConstructor
public class ResizePictureThread implements Runnable {
    private final File FILE;
    private final int NEW_WIDTH;
    private final String DST_FOLDER;
    private final long START;

    @Override
    public void run() {
        try {
            BufferedImage image = ImageIO.read(FILE);
            File newFile = new File(DST_FOLDER + "/" + FILE.getName());
            ImageIO.write(ImageResizesMethods.resizeImageByImgscalr(image, NEW_WIDTH), "jpg", newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Finished after start " + (System.currentTimeMillis() - START) + " ms");
    }
}
