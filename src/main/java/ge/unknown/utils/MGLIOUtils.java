package ge.unknown.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static ge.unknown.utils.constants.Constants.Products.VALID_EXTENSIONS;
import static ge.unknown.utils.constants.Constants.Products.VALID_OBJECT_NAMES;

/**
 * Created by Butqucha on 10/29/15.
 */
public class MGLIOUtils {

    public static void copyFolder(File source, File destination) {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdirs();
            }
            String files[] = source.list();
            for (String file : files) {
                File srcFile = new File(source, file);
                File destFile = new File(destination, file);
                copyFolder(srcFile, destFile);
            }
        } else {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new FileInputStream(source);
                out = new FileOutputStream(destination);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            } catch (Exception e) {
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static boolean deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.delete()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static HashMap<String, Object> getDirectoryList(String dir) {
        String path = "";
        if (!MGLStringUtils.IsNullOrBlank(dir)) {
            path = dir;
            File f = new File(path);
            if (!f.exists() || f.isFile())
                path = "";
        }
        return listDirectory(path);
    }

    public static HashMap<String, Object> listDirectory(String dirPath) {
        HashMap<String, Object> results = new HashMap<String, Object>(1);
        List<HashMap> list = new ArrayList<HashMap>(1);
        File[] files = new File(dirPath).listFiles();
        for (File file : files) {
            HashMap<String, String> map = new HashMap<String, String>(1);
            String type = file.isDirectory() ? "dir" : "file";
            map.put(type, file.getAbsolutePath());
            list.add(map);
        }
        results.put("data", list);
        return results;
    }

    public static boolean saveFile(String file, String str) throws IOException {
        byte dataToWrite[] = str.getBytes();
        FileOutputStream out = new FileOutputStream(file);
        out.write(dataToWrite);
        out.close();
        return true;
    }

    public static String checkDirs(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.setReadable(true, false);
            dir.setExecutable(true, false);
            dir.setWritable(true, false);
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    public static String readFile(String path) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return new String(encoded, "UTF-8");
        } catch (IOException e) {
        }
        return "";
    }

    public static boolean limitFileSize(MultipartFile[] files) {
        for (MultipartFile file : files) {
            long fileSizeInBytes = file.getSize();
            long fileSizeInKB = fileSizeInBytes / 1024;
            long fileSizeInMB = fileSizeInKB / 1024;
            if (fileSizeInMB > 5) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidObjectPath(String objectName) {
        return Arrays.asList(VALID_OBJECT_NAMES).contains(objectName);
    }

    public static boolean isValidExtension(String ext) {
        return Arrays.asList(VALID_EXTENSIONS).contains(ext);
    }

    public static class ImageUtils {

        private static final int IMG_WIDTH = 100;
        private static final int IMG_HEIGHT = 100;

        public static String resizeImage(String fileName) {
            try {

                File file = new File(fileName);
                String path = file.getAbsolutePath();
                String extension = null;
                int index = path.lastIndexOf('.');
                if (index >= 0) extension = path.substring(index + 1);

                if (!MGLStringUtils.IsNullOrBlank(extension) && file.exists()) {

                    extension = extension.toLowerCase();
                    BufferedImage originalImage = ImageIO.read(file);
                    int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

                    String filePath = MGLStringUtils.substringUpToLastOccurance(path, File.separatorChar);
                    Path p = Paths.get(path);
                    String name = p.getFileName().toString();
                    String thumb = MGLStringUtils.substringUpToLastOccurance(name, '.');
                    String returnPath = thumb = String.format("%s%s%s", thumb, "_thumb.", extension);
                    thumb = String.format("%s%s%s", filePath, File.separator, thumb);
                    BufferedImage resizeImageJpg = resizeImage(originalImage, type);
                    ImageIO.write(resizeImageJpg, extension, new File(thumb));
                    return returnPath;
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            return "";
        }

        private static BufferedImage resizeImage(BufferedImage originalImage, int type) {
            BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
            g.dispose();
            return resizedImage;
        }

    }

}
