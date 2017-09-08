package io.lance.web.module.common.upload.util;


import io.lance.web.module.common.upload.bean.Range;
import io.lance.web.module.common.upload.config.Configurations;

import javax.servlet.http.HttpServletRequest;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IO--closing, getting file name ... main function method
 */
public class IoUtil {
    static final Pattern RANGE_PATTERN = Pattern.compile("bytes \\d+-\\d+/\\d+");

    /**
     * According the key, generate a file (if not exist, then create
     * a new file).
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static File getFile(String filename) throws IOException {
        if (filename == null || filename.isEmpty())
            return null;
        String name = filename.replaceAll("/", Matcher.quoteReplacement(File.separator));
        File f = new File(Configurations.getFileRepository() + File.separator + name);
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        if (!f.exists())
            f.createNewFile();

        return f;
    }

    /**
     * Acquired the file.
     *
     * @param key
     * @return
     * @throws IOException
     */
    public static File getTokenedFile(String key) throws IOException {
        if (key == null || key.isEmpty())
            return null;

        File f = new File(Configurations.getFileRepository() + File.separator + key);
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        if (!f.exists())
            f.createNewFile();

        return f;
    }

    public static void storeToken(String key) throws IOException {
        if (key == null || key.isEmpty())
            return;

        File f = new File(Configurations.getFileRepository() + File.separator + key);
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        if (!f.exists())
            f.createNewFile();
    }

    /**
     * close the IO stream.
     *
     * @param stream
     */
    public static void close(Closeable stream) {
        try {
            if (stream != null)
                stream.close();
        } catch (IOException e) {
        }
    }

    /**
     * 获取Range参数
     *
     * @param req
     * @return
     * @throws IOException
     */
    public static Range parseRange(HttpServletRequest req) throws IOException {
        String range = req.getHeader(Constant.CONTENT_RANGE_HEADER);
        Matcher m = RANGE_PATTERN.matcher(range);
        if (m.find()) {
            range = m.group().replace("bytes ", "");
            String[] rangeSize = range.split("/");
            String[] fromTo = rangeSize[0].split("-");

            long from = Long.parseLong(fromTo[0]);
            long to = Long.parseLong(fromTo[1]);
            long size = Long.parseLong(rangeSize[1]);

            return new Range(from, to, size);
        }
        throw new IOException("Illegal Access!");
    }


    /**
     * @desc: 文件大小
     * @author lance
     * @time: 2017-09-08 09:31:28
     */
    public static String getFileSize(long fileSize) {
        String fileSizeStr = "";
        if (fileSize > 1024 * 1024) {
            fileSizeStr = (fileSize / 1024 / 1024) + "M";
        } else if (fileSize > 1024) {
            fileSizeStr = (fileSize / 1024) + "K";
        } else {
            fileSizeStr = (fileSize) + "B";
        }
        return fileSizeStr;
    }
}
