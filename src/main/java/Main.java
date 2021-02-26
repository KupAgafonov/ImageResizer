import ResizerSourse.PackageImagesResizer;
import ResizerSourse.QueueImageResizer;
import ResizerSourse.ResizePictureThread;
import SystemInfo.InfoCPU;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {

    private static final int NEW_WIDTH = 300;
    private static final String SRC_FOLDER = "/users/kupag/Desktop/src";
    private static final String DST_FOLDER = "/users/kupag/Desktop/dst";
    private static final long START = System.currentTimeMillis();

    private static final File[] FILES = new File(SRC_FOLDER).listFiles();

    private static final int CORE_COUNT = InfoCPU.getAvailableProcessorsByRuntime();

    private static final List<File> FILE_LIST = getFilesList(SRC_FOLDER);

    private static final Queue<File> QUEUE = new ConcurrentLinkedQueue<>(FILE_LIST);

    public static void main(String[] args) {

        try {
            FileUtils.deleteDirectory(new File(DST_FOLDER));
            createdFolder(DST_FOLDER);

//            startPackageImageResizer(FILES, NEW_WIDTH, DST_FOLDER, START).start();

//            startCoreCountThreadFromPackageImageResizer(CORE_COUNT, FILES, NEW_WIDTH, DST_FOLDER, START);

//            startQueueImageResizer(QUEUE, NEW_WIDTH, DST_FOLDER, START);

//            startTreadForEachImage(FILE_LIST, NEW_WIDTH, DST_FOLDER, START);

            startResizePictureThread(CORE_COUNT, FILE_LIST, NEW_WIDTH, DST_FOLDER, START);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void createdFolder(String target){
        File folder = new File(target);
        if(!folder.exists()){
            folder.mkdir();
        }
    }

    @SneakyThrows
    public static List<File> getFilesList(String target) {
        return Files.walk(Paths.get(target))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());
    }

    public static Thread startPackageImageResizer(File[] files, int newWidth, String dstFolder, long start) {
        Thread thread = new Thread(new PackageImagesResizer(files, newWidth, dstFolder, start));
        return thread;
    }

    public static void startCoreCountThreadFromPackageImageResizer(int coreCount, File[] files, int newWidth, String dstFolder, long start) {

        int step = Objects.requireNonNull(files).length / coreCount;
        int index = 0;
        File[] temp = new File[step];

        for (File file : files) {
            temp[index++] = file;
            if (index == step) {
                new PackageImagesResizer(temp, NEW_WIDTH, DST_FOLDER, START).start();
                index = 0;
                temp = new File[step];
            }
        }
    }

    public static void startQueueImageResizer(Queue queue, int newWidth, String dstFolder, long start) {
        while (!queue.isEmpty()) {
            Thread thread = new Thread(new QueueImageResizer(queue, newWidth, dstFolder, start));
            thread.start();
        }
    }

    public static void startTreadForEachImage(List<File> fileList, int newWidth, String dstFolder, long start) {
        for (File file : fileList) {
            new ResizePictureThread(file, newWidth, dstFolder, start).run();
        }
    }


    public static void startResizePictureThread(int coreCount, List<File> fileList, int newWidth, String dstFolder, long start) {

        ExecutorService executorService = Executors.newFixedThreadPool(CORE_COUNT);
//        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

        for (File file : fileList) {
            executorService.execute(new ResizePictureThread(file, newWidth, dstFolder, start));
//            service.schedule(new ResizePictureThread(file, newWidth, dstFolder, start), 10, TimeUnit.MICROSECONDS);
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

    }
}
