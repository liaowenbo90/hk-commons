/**
 *
 */
package com.hk.commons.poi.excel.read;

import com.hk.commons.poi.ReabableException;
import com.hk.commons.poi.excel.model.ReadParam;
import com.hk.commons.poi.excel.model.ReadResult;
import com.hk.commons.poi.excel.read.handler.ReadHandler;
import com.hk.commons.poi.excel.util.ReadExcelUtils;
import com.hk.commons.util.AssertUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author kally
 */
abstract class AbstractReadExcel<T> implements ReadExcel<T> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 转换的Bean 类
     */
    protected final Class<T> beanClazz;

    protected AbstractReadExcel(ReadParam<T> readParam) {
        AssertUtils.notNull(readParam, "ReadParam must not be null");
        AssertUtils.isTrue(readParam.getTitleRow() < readParam.getDataStartRow(), "标题行必须小于数据开始行");
        this.beanClazz = readParam.getBeanClazz();
        readParam.setColumnProperties(getColumnProperty().getColumnProperties());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.hk.commons.poi.excel.read.ReadableExcel#read(com.hk.commons.poi.excel.
     * model.ReadableParam, java.io.File)
     */
    @Override
    public final ReadResult<T> read(File file) {
        try {
            ReadHandler<T> readableHandler = createReadableHandler(file);
            return readableHandler.process(file);
        } catch (EncryptedDocumentException | IOException | SAXException | OpenXML4JException e) {
            logger.error(e.getMessage(), e);
            throw new ReabableException(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.hk.commons.poi.excel.read.ReadableExcel#read(com.hk.commons.poi.excel.
     * model.ReadableParam, java.io.InputStream)
     */
    @Override
    public ReadResult<T> read(InputStream in) {
        try {
            if (!in.markSupported()) {
                in = new ByteArrayInputStream(IOUtils.toByteArray(in));
            }
            ReadHandler<T> readableHandler = createReadableHandler(in);
            return readableHandler.process(in);
        } catch (EncryptedDocumentException | IOException | SAXException | OpenXML4JException e) {
            logger.error(e.getMessage(), e);
            throw new ReabableException(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 从流中创建解析文件处理器
     *
     * @param in
     * @return {@link ReadableHandler}
     */
    protected abstract ReadHandler<T> createReadableHandler(InputStream in) throws IOException;

    /**
     * 从File中创建解析文件处理器
     *
     * @param file
     * @return {@link ReadableHandler}
     */
    protected abstract ReadHandler<T> createReadableHandler(File file) throws IOException;

    /**
     * 字段列与属性的映射
     *
     * @return
     */
    protected abstract ColumnProperty getColumnProperty();

    /**
     * @author kally
     * @date 2018年1月11日上午11:39:42
     */
    interface ColumnProperty {

        Map<Integer, String> getColumnProperties();

    }

    /**
     * 使用注解解析
     *
     * @author kally
     * @date 2018年1月11日上午11:44:24
     */
    protected class AnnotationColumnProperty implements ColumnProperty {

        @Override
        public Map<Integer, String> getColumnProperties() {
            return ReadExcelUtils.getReadExcelAnnotationMapping(beanClazz);
        }
    }
}