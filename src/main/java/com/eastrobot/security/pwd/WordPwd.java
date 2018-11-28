package com.eastrobot.security.pwd;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.amazonaws.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import ooo.connector.BootstrapSocketConnector;

import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNameAccess;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.style.XStyle;
import com.sun.star.style.XStyleFamiliesSupplier;
import com.sun.star.text.HoriOrientation;
import com.sun.star.text.TextContentAnchorType;
import com.sun.star.text.WrapTextMode;
import com.sun.star.text.XSentenceCursor;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.uno.AnyConverter;
import com.sun.star.uno.Type;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

import java.io.File;
import java.util.HashMap;

/**
 * Created by carxiong on 23/11/2018.
 */
public class WordPwd {

    private XComponentContext mxRemoteContext;
    private XMultiComponentFactory mxRemoteServiceManager;
    private XTextCursor mxDocCursor;
    private XText mxDocText;
    private XTextDocument mxDoc;
    private XSentenceCursor xSentenceCursor;
    private Object desktop;
    private XComponent xEmptyWriterComponent;

    private String OPENOFFICE_PATH ="C:/Program Files/OpenOffice 4/program/";

    private static Log logger = LogFactory.getLog(WordPwd.class);

    public WordPwd(String openOfficePath)
    {
        this.OPENOFFICE_PATH=openOfficePath;
    }

    /**
     * get the remote service manager
     *
     * @return
     * @throws java.lang.Exception
     */
    private XMultiComponentFactory getRemoteServiceManager()
            throws java.lang.Exception {
        if (mxRemoteContext == null && mxRemoteServiceManager == null) {
            // get the remote office context
//            mxRemoteContext = null ;BootstrapSocketConnector
//                    .bootstrap(OPENOFFICE_PATH);
            if (logger.isInfoEnabled()) {
                logger.info("Connected to a running office ...");
            }
            mxRemoteServiceManager = mxRemoteContext.getServiceManager();
            String available = (mxRemoteServiceManager != null ? "available"
                    : "not available");
            if (logger.isInfoEnabled()) {
                logger.info("remote ServiceManager is " + available);
            }
        }
        return mxRemoteServiceManager;
    }

    /**
     * 判断文件是否存在,并将文件路径格式化为:file:///D:/doc/myword.doc的形式;
     *
     * @param fileUrl
     * @return
     * @throws Exception
     */
    private String formatFileUrl(String fileUrl) throws Exception {
        try {
            StringBuffer sUrl = new StringBuffer("file:///");
            File sourceFile = new File(fileUrl);
            sUrl.append(sourceFile.getCanonicalPath().replace('\\', '/'));
            return sUrl.toString();
        } catch (Exception e) {
            if (logger.isInfoEnabled()) {
                logger.info("formatFileUrl=" + e.toString() + "fileUrl="
                        + fileUrl);
            }
        }
        return "";
    }

    /**
     * get the interfaces to control the UNO
     *
     * @param docType
     * @return
     * @throws java.lang.Exception
     */
    private XComponent newDocComponent(String fileUrl, String password)
            throws java.lang.Exception {
        mxRemoteServiceManager = this.getRemoteServiceManager();
        // get the Desktop service
        desktop = mxRemoteServiceManager.createInstanceWithContext(
                "com.sun.star.frame.Desktop", mxRemoteContext);
        // retrieve the current component and access the controller
        XComponentLoader xComponentLoader = (XComponentLoader) UnoRuntime
                .queryInterface(XComponentLoader.class, desktop);
        // set the OpenOffice not open
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("Hidden", Boolean.TRUE);
        if( !StringUtils.isNullOrEmpty(password)){
            map.put("Password", password);
        }
        PropertyValue[] propertyValue = MakePropertyValue(map);
        fileUrl = formatFileUrl(fileUrl);
        return xComponentLoader.loadComponentFromURL(fileUrl, "_blank", 0,
                propertyValue);
    }

    /**
     * 保存文件
     *
     * @param xDoc
     * @param storeUrl
     * @throws java.lang.Exception
     */
    private void storeDocComponent(XComponent xDoc, String storeUrl,
                                   String password) throws java.lang.Exception {
        XStorable xStorable = (XStorable) UnoRuntime.queryInterface(
                XStorable.class, xDoc);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("FilterName", "MS Word 97");
        if(!StringUtils.isNullOrEmpty(password)){
            map.put("Password", password);
        }
        PropertyValue[] storeProps = MakePropertyValue(map);
        storeUrl = formatFileUrl(storeUrl);
        xStorable.storeAsURL(storeUrl, storeProps);
    }

    /**
     * 设置属性值;
     *
     * @param cName
     * @param uValue
     * @return
     */
    private final PropertyValue[] MakePropertyValue(HashMap<String, Object> map) {
        int total = 0;
        if (null != map && map.size() > 0) {
            total = map.size();
        }
        PropertyValue[] tempMakePropertyValue = new PropertyValue[total];
        Iterator iter = map.entrySet().iterator();
        int i = 0;
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = entry.getKey().toString();
            Object val = entry.getValue();
            tempMakePropertyValue[i] = new PropertyValue();
            tempMakePropertyValue[i].Name = key;
            tempMakePropertyValue[i].Value = val;
            i = i + 1;
        }
        return tempMakePropertyValue;
    }

    /**
     * 关闭组件;
     *
     * @throws Exception
     */
    private void close() throws Exception {
        com.sun.star.util.XCloseable xCloseable = (com.sun.star.util.XCloseable) UnoRuntime
                .queryInterface(com.sun.star.util.XCloseable.class, mxDoc);
        if (xCloseable != null) {
            xCloseable.close(false);
        } else {
            com.sun.star.lang.XComponent xComponent = (com.sun.star.lang.XComponent) UnoRuntime
                    .queryInterface(com.sun.star.lang.XComponent.class, mxDoc);
            if (null != xComponent)
                xComponent.dispose();
        }
    }

    /**
     * 插入字符串到word文件中;
     * @param fileUrl:word文件物理路径
     * @param content:文件中插入的内容
     * @param password:打开word文件的密码
     * @throws java.lang.Exception
     */
    public void insertStrToWord(String fileUrl, String content, String password)
            throws Exception {
        try {
            XComponent xEmptyWriterComponent = newDocComponent(fileUrl, password);
            XTextDocument mxDoc = (XTextDocument) UnoRuntime.queryInterface(
                    XTextDocument.class, xEmptyWriterComponent);
            mxDocText = mxDoc.getText();
            mxDocCursor = mxDocText.createTextCursor();
            xSentenceCursor = (XSentenceCursor) UnoRuntime.queryInterface(
                    XSentenceCursor.class, mxDocCursor);
            mxDocText.insertString(xSentenceCursor, content, true);
            // 保存;
            storeDocComponent(xEmptyWriterComponent, fileUrl, password);
        } catch (Exception e) {
            if (logger.isInfoEnabled()) {
                logger.info("插入字符串到word文件中" + e);
            }
        } finally {
            close();
        }

    }
}
