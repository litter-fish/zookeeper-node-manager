package com.fish.file;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yudingming on 2016/12/16.
 */
public abstract class AbstractUpload {


    protected transient final Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * 文件上传接口
     * @param request
     * @param response
     * @return
     */
    public final String upload(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("处理文件上传操作");
        //创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断 request 是否有文件上传,即多部分请求
        List<String> filePath = Lists.newArrayList();
        if (multipartResolver.isMultipart(request)) {
            logger.debug("请求是文件上传操作");
            //转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iterator =  multiRequest.getFileNames();
            while (iterator.hasNext()) {
                // 获取一个input中所有的文件
                List<MultipartFile> multipartFiles = multiRequest.getFiles(iterator.next());
                if (CollectionUtils.isNotEmpty(multipartFiles)) {
                    for (MultipartFile multipartFile : multipartFiles) {
                        byte[] bytes;
                        try {
                            bytes = multipartFile.getBytes();
                        } catch (Exception e) {
                            e.printStackTrace();
                            continue;
                        }
                        String originalFilename = multipartFile.getOriginalFilename();
                        originalFilename = originalFilename.substring(0, originalFilename.lastIndexOf("."));
                        logger.debug("循环处理文件,filename:[{}]", originalFilename);
                        String maResult = checkFile(bytes, multipartFile.getOriginalFilename());

                        if (null == maResult) {
                            List<String> fileContent = processesFile(bytes);
							postProcesses(fileContent, originalFilename);
                        } else {
                            return maResult;
                        }
                    }
                }
            }
        }

        return filePath.toString();
    }

    /**
     * 检查文件，子类给出实现
     * @param bytes
     * @param originalFilename
     * @return
     */
    protected abstract String checkFile(byte[] bytes, String originalFilename);

    /**
     * 处理文件，上传还是解析等操作子类给出实现
     * @param bytes
     * @return
     */
    protected abstract List<String> processesFile(byte[] bytes);

	/**
	 *
	 * @param originalFilename
	 * @return
	 */
	protected abstract  Boolean postProcesses(List<String> fileContent, String originalFilename);

}
