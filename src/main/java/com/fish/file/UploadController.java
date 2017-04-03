package com.fish.file;

import com.fish.bean.Node;
import com.fish.zookeeper.ZookeeperConfigNode;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


@Controller	
@RequestMapping("uploadController")
public class UploadController {
	
	protected transient final Logger logger = LoggerFactory.getLogger(getClass());

	@Resource(name = "zookeeperNodeData")
	private ZookeeperConfigNode zookeeperConfigNode;

	/**
	 * 上传

	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("upload")
	@ResponseBody
	public String upload(HttpServletRequest request, HttpServletResponse response) {

		final String nodePath = request.getParameter("nodeName");

		String success = new AbstractUpload() {

			/**
			 * 检查文件，包括文件大小及文件扩展名
			 * @param originalFilename
			 * @return
			 */
			@Override
			protected String checkFile(byte[] bytes, String originalFilename) {
				logger.debug("检查文件，包括文件大小及文件扩展名");
				return null;
			}

			/**
			 * 处理文件（上传操作）
			 * @return 返回文件在服务器中的访问路径
			 */
			@Override
			protected List<String> processesFile(byte[] bytes) {
				return UploadController.this.processesFile(bytes);
			}

			@Override
			protected Boolean postProcesses(List<String> fileContent, String originalFilename) {

				if (CollectionUtils.isNotEmpty(fileContent)) {

					createNode(nodePath + "/" + originalFilename, "");

					for (String content : fileContent) {
						if (StringUtils.isNotEmpty(content)) {
							if (content.startsWith("#")) continue;

							String[] contents = content.split("=");

							StringBuilder key = new StringBuilder(nodePath).append("/").append(originalFilename).append("/").append(contents[0]);
							String value = contents.length > 1 ? contents[1] : "";

							createNode(key.toString(), value);
						}
					}
				}
				return null;
			}

		}.upload(request, response);

		try {
			Thread.sleep(5000);
			response.sendRedirect("/Zookeeper-manager-web/base/index");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return success;
	}

	private void createNode(String path, String data) {
		try {
			zookeeperConfigNode.createNode(path, CreateMode.PERSISTENT, data);
			Node node = zookeeperConfigNode.createNode(path);
			zookeeperConfigNode.put(path, node);
			logger.debug("create node path:[{}], data:[{}]", path, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private List<String> processesFile(byte[] bytes) {
		logger.debug("处理文件（上传操作）");
		List<String> fileContent = Lists.newArrayList();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
			String content;
			while (null != ( content = reader.readLine())) {
				fileContent.add(content);
			}
		} catch (Exception e) {
			return fileContent;
		} finally {
			if (null != reader) try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileContent;
	}



}
