import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TestingPerformanceForImageResize {

    private static final int NEW_WIDTH = 300;
    private static final String SRC_FOLDER = "/users/kupag/Desktop/src";
    private static final String DST_FOLDER = "/users/kupag/Desktop/dst";

    private static final File[] FILES = new File(SRC_FOLDER).listFiles();
    private static final int CORE_COUNT = 4;

    private static final List<File> FILE_LIST = Main.getFilesList(SRC_FOLDER);
    private static final Queue<File> QUEUE = new ConcurrentLinkedQueue<>(FILE_LIST);


    @BeforeEach
    @Before
    public void setUp() throws Exception {
        FileUtils.deleteDirectory(new File(DST_FOLDER));
        Main.createdFolder(DST_FOLDER);
    }

    @SneakyThrows
    @Test
    public void testPackageImageResizerMethod() {
        Thread thread = Main.startPackageImageResizer(FILES, NEW_WIDTH, DST_FOLDER, System.currentTimeMillis());
        thread.start();

        Thread.sleep(40000);

        int expected = 79;
        int actual = Main.getFilesList(DST_FOLDER).size();
        Assert.assertEquals(expected, actual);

    }

    @SneakyThrows
    @Test
    public void testCoreCountThreadFromPackageImageResizer() {
        Main.startCoreCountThreadFromPackageImageResizer(CORE_COUNT, FILES, NEW_WIDTH, DST_FOLDER, System.currentTimeMillis());

        Thread.sleep(40000);
        int expected = 79;
        int actual = Main.getFilesList(DST_FOLDER).size();
        Assert.assertEquals(expected, actual);
    }

    @SneakyThrows
    @Test
    public void testQueueImageResizer() {
        Main.startQueueImageResizer(QUEUE, NEW_WIDTH, DST_FOLDER, System.currentTimeMillis());

        Thread.sleep(40000);

        int expected = 79;
        int actual = Main.getFilesList(DST_FOLDER).size();
        Assert.assertEquals(expected, actual);
    }

    @SneakyThrows
    @Test
    public void testTreadForEachImage() {
        Main.startTreadForEachImage(FILE_LIST, NEW_WIDTH, DST_FOLDER, System.currentTimeMillis());

//        Thread.sleep(40000);

        int expected = 79;
        int actual = Main.getFilesList(DST_FOLDER).size();
        Assert.assertEquals(expected, actual);
    }

    @SneakyThrows
    @Test
    public void testResizePictureThread() {
        Main.startResizePictureThread(CORE_COUNT, FILE_LIST, NEW_WIDTH, DST_FOLDER, System.currentTimeMillis());

//        Thread.sleep(40000);

        int expected = 79;
        int actual = Main.getFilesList(DST_FOLDER).size();
        Assert.assertEquals(expected, actual);
    }
}
