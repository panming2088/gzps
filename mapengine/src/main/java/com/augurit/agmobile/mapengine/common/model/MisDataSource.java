/**
 * 
 * ACCMap - ACC map development platform
 * Copyright (c) 2015, AfirSraftGarrier, afirsraftgarrier@qq.com
 * 
 */
package com.augurit.agmobile.mapengine.common.model;

public class MisDataSource {
	/*
	 * "driver":"oracle.jdbc.driver.OracleDriver", "geoport":"", "geotype":"",
	 * "geouser":"", "id":225, "ip":"192.168.11.32", "ispooled":"",
	 * "maxconnection":"", "minconnection":"", "name":"浜哄彛鏁版嵁婧?",
	 * "password":"326828B23969016E", "sid":"hkdb", "type":"Oracle",
	 * "url":"jdbc:oracle:thin:@192.168.11.32:1521:hkdb", "user":"pygis",
	 * "version":""
	 */
	private String driver;

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getGeoport() {
		return geoport;
	}

	public void setGeoport(String geoport) {
		this.geoport = geoport;
	}

	public String getGeotype() {
		return geotype;
	}

	public void setGeotype(String geotype) {
		this.geotype = geotype;
	}

	public String getGeouser() {
		return geouser;
	}

	public void setGeouser(String geouser) {
		this.geouser = geouser;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIspooled() {
		return ispooled;
	}

	public void setIspooled(String ispooled) {
		this.ispooled = ispooled;
	}

	public String getMaxconnection() {
		return maxconnection;
	}

	public void setMaxconnection(String maxconnection) {
		this.maxconnection = maxconnection;
	}

	public String getMinconnection() {
		return minconnection;
	}

	public void setMinconnection(String minconnection) {
		this.minconnection = minconnection;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	private String geoport;
	private String geotype;
	private String geouser;
	private int id;
	private String ip;
	private String ispooled;
	private String maxconnection;
	private String minconnection;
	private String name;
	private String password;
	private String sid;
	private String type;
	private String url;
	private String user;
	private String version;

	// @Override
	// public Connection getConnection() throws SQLException {
	// TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public Connection getConnection(String theUsername, String thePassword)
	// throws SQLException {
	// TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public PrintWriter getLogWriter() throws SQLException {
	// TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public int getLoginTimeout() throws SQLException {
	// TODO Auto-generated method stub
	// return 0;
	// }
	//
	// @Override
	// public void setLogWriter(PrintWriter arg0) throws SQLException {
	// TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void setLoginTimeout(int arg0) throws SQLException {
	// TODO Auto-generated method stub
	//
	// }
}