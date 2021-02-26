package ResizerSourse;

import lombok.AllArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Queue;

@AllArgsConstructor
public class QueueImageResizer implements Runnable {

    private final Queue<File> QUEUE;
    private final int NEW_WIDTH;
    private final String DST_FOLDER;
    private final long START;

    @Override
    public void run() {
        try {

            File file = QUEUE.poll();
            if (file != null && file.isFile()) {
                BufferedImage image = ImageIO.read(file);
                File newFile = new File(DST_FOLDER + "/" + file.getName());
                ImageIO.write(ImageResizesMethods.resizeImageByImgscalr(image, NEW_WIDTH), "jpg", newFile);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Finished after start " + (System.currentTimeMillis() - START) + " ms");
    }
}
