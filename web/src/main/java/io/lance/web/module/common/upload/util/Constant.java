package io.lance.web.module.common.upload.util;

/**
 * Author Lance.
 * Date: 2017-09-07 13:55
 * Desc:
 */
public interface Constant {

    int BUFFER_LENGTH = 4096;

    int SIZE=4194304;//4M

    String CONTENT_RANGE_HEADER = "content-range";

    String START_FIELD = "start";

    /**
     * token
     */
    String TOKEN_FIELD = "token";
    String FILE_NAME_FIELD = "name";
    String FILE_SIZE_FIELD = "size";


    String SUCCESS = "success";
    String MESSAGE = "message";

}
