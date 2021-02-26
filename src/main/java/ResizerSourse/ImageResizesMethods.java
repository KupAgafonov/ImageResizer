package ResizerSourse;

import marvin.image.MarvinImage;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import org.imgscalr.Scalr;
import org.marvinproject.image.transform.scale.Scale;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Objects;

public class ImageResizesMethods
{
    private static int getProportionalHeight(BufferedImage image, int newWidth) {
        return (int) Math.round(image.getHeight() / (image.getWidth() / (double) newWidth));  // но вроде бы так (image.getHeight * new_width)/image.get_width
    }

    private static BufferedImage resizeImageBySkillBoxLectures(BufferedImage image, int newWidth) {

        int newHeight = getProportionalHeight(image, newWidth);

        BufferedImage newImage = new BufferedImage(
                newWidth, getProportionalHeight(image, newWidth), BufferedImage.TYPE_INT_RGB
        );

        int widthStep = image.getWidth() / newWidth;
        int heightStep = image.getHeight() / newHeight;

        for (int x = 0; x < newWidth; x++) {
            for (int y = 0; y < newHeight; y++) {
                int rgb = image.getRGB(x * widthStep, y * heightStep);
                newImage.setRGB(x, y, rgb);
            }
        }
        return newImage;
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth) {

        int targetHeight = getProportionalHeight(originalImage, targetWidth);

        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    private static BufferedImage resizeImageGetScaledInstance(BufferedImage originalImage, int newWidth) {

        int targetHeight = getProportionalHeight(originalImage, newWidth);

        Image resultingImage = originalImage.getScaledInstance(newWidth, targetHeight, Image.SCALE_AREA_AVERAGING);
        BufferedImage outputImage = new BufferedImage(newWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

    private static BufferedImage resizeImageThumbnails(BufferedImage originalImage, int targetWidth, int targetHeight) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(originalImage)
                .size(targetWidth, targetHeight)
                .outputFormat("JPEG")
                .outputQuality(1)
                .toOutputStream(outputStream);
        byte[] data = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        return ImageIO.read(inputStream);
    }

    private static void resizeImagePackageThumbnails(String dstFolder, int targetWidth, int targetHeight) throws Exception {
        Thumbnails.of(Objects.requireNonNull(new File(dstFolder).listFiles()))
                .size(targetWidth, targetHeight)
                .outputFormat("JPEG")
                .outputQuality(0.80)
                .toFiles(Rename.PREFIX_DOT_THUMBNAIL);
    }

    private static BufferedImage resizeImagePackageMarvin(BufferedImage originalImage, int targetWidth, int targetHeight) throws Exception {
        MarvinImage image = new MarvinImage(originalImage);
        Scale scale = new Scale();
        scale.load();
        scale.setAttribute("newWidth", targetWidth);
        scale.setAttribute("newHeight", targetHeight);
        scale.process(image.clone(), image, null, null, false);
        return image.getBufferedImageNoAlpha();
    }

    private static BufferedImage simpleResizeImageByImgscalr(BufferedImage originalImage, int newWidth) {
        return Scalr.resize(originalImage, newWidth);
    }

    protected static BufferedImage resizeImageByImgscalr(BufferedImage originalImage, int newWidth) {
        return Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, newWidth, Scalr.THRESHOLD_QUALITY_BALANCED);
    }
}
