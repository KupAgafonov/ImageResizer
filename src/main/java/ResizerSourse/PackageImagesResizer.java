package ResizerSourse;

import lombok.AllArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@AllArgsConstructor
public class PackageImagesResizer extends Thread {

    private final File[] FILES;
    private final int NEW_WIDTH;
    private final String DST_FOLDER;
    private final long START;

    @Override
    public void run() {
        try {
            for (File file : FILES) {
                BufferedImage image = ImageIO.read(file);
                if (image == null) {
                    continue;
                }
                File newFile = new File(DST_FOLDER + "/" + file.getName());
                ImageIO.write(ImageResizesMethods.resizeImageByImgscalr(image, NEW_WIDTH), "jpg", newFile);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Finished after start " + (System.currentTimeMillis() - START) + " ms");
    }

}

