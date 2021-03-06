package vn.edu.hust.student.dynamicpool.dal.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.util.ajax.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.edu.hust.student.dynamicpool.dal.client.http.HttpClientController;
import vn.edu.hust.student.dynamicpool.dal.processor.Processor;
import vn.edu.hust.student.dynamicpool.dal.server.socket.ClientSocketController;
import vn.edu.hust.student.dynamicpool.dal.statics.Field;
import vn.edu.hust.student.dynamicpool.dal.utils.xml.ServerXMLConfigReader;
import vn.edu.hust.student.dynamicpool.exception.DALException;

public class ClientMainController {
	private static ClientMainController _instance;

	private HttpClientController httpClientController;
	private ClientSocketController clientSocketController;
	private JSON json;
	private Logger logger = LoggerFactory.getLogger(ClientMainController.class);

	private ClientMainController() {
		loadLog4j();
		getLogger().info("Reading config file from path conf/client.xml");
		ServerXMLConfigReader configReader;
		try {
			configReader = new ServerXMLConfigReader("conf/client.xml");
			clientSocketController = new ClientSocketController(
					configReader.getSocketServerConfig());
			Map<String, Class<? extends Processor>> processorMap = configReader
					.getProcessorMap();
			this.getClientSocketController().initProcessor(processorMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		httpClientController = new HttpClientController();
		json = new JSON();
	}

	public static ClientMainController getInstance() {
		if (_instance == null)
			_instance = new ClientMainController();
		return _instance;
	}

	public void loadLog4j() {
		String log4JPropertyFile = "conf/log4j.properties";
		Properties p = new Properties();

		try {
			p.load(new FileInputStream(log4JPropertyFile));
			PropertyConfigurator.configure(p);
		} catch (IOException e) {
			System.out.println("Opps, cannot load log4j.properties");
		}
	}

	@SuppressWarnings("unchecked")
	public void start(String key) throws DALException {
		logger.info("Starting Socket Client...");
		String ip = null;
		int port = 0;
		try {
			Integer.parseInt(key);
			String response = httpClientController.authentication(key);
			Map<String, Object> params = (Map<String, Object>) json
					.fromJSON(response);
			if (params.containsKey(Field.ERROR)) {
				if (params.get(Field.ERROR) != null) {
					throw new DALException((String) params.get(Field.ERROR),
							null);
				} else {
					ip = (String) params.get("ip");
					port = Integer.parseInt(params.get("port").toString());
				}
			}
		} catch (NumberFormatException e) {
			logger.info("key {} is not integer. try connect to format string 'ip:port'", key);
			String[] input = key.split(":");
			if (input.length == 2) {
				ip = input[0];
				port = Integer.parseInt(input[1]);
			}
		} catch (MalformedURLException e) {
			throw new DALException("URL invalid", e);
		} catch (IOException e) {
			throw new DALException("cannot connect to server", e);
		}

		if (ip != null) {
			if (!this.getClientSocketController().start(ip, port)) {
				throw new DALException("cannot connect to host", null);
			}
			logger.info("start socket successful");
		} else {
			logger.error("cannot start server with ip null");
			throw new DALException("Invalid IP", null);
		}
	}

	public void stop() {
		this.getClientSocketController().stop();
	}

	public HttpClientController getHttpClientController() {
		return httpClientController;
	}

	public void setHttpClientController(
			HttpClientController httpClientController) {
		this.httpClientController = httpClientController;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public ClientSocketController getClientSocketController() {
		return clientSocketController;
	}

	public void setClientSocketController(
			ClientSocketController clientSocketController) {
		this.clientSocketController = clientSocketController;
	}
}
