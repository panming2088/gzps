package com.augurit.agmobile.gzps.uploadevent.model;

import java.util.List;

/**
 * Created by liangsh on 2017/11/12.
 */
@Deprecated
public class EventListResult {


    /**
     * pageNo : 1
     * pageSize : 10
     * orderBy : create
     * orderDir : desc
     * countTotal : true
     * result : [[{"wfBusInstanceId":176871,"masterEntity":"GX_PROBLEM_REPORT","masterEntityKey":936,"procInstId":"gxxcyh.1730001","templateId":3382,"templateName":"管线巡查养护","templateTypeName":"工作流程","taskInstDbid":1730012,"create":"2017-11-12 17:25:32","activityName":"sendTask","activityChineseName":"任务派单","procStartdate":"2017-11-12 17:25:31"}],[{"fieldname":"templateName","displayname":"流程名称","primarykeyfieldflag":0},{"fieldid":760202,"fieldname":"SBSJ","displayname":"上报时间","tableid":7602,"datatypename":"DATE","nullflag":1,"primarykeyfieldflag":0,"relatedfieldflag":0,"listfieldflag":0,"queryfieldflag":0,"displayorder":2,"patrolfieldflag":0,"componentid":2,"colspan":1,"editflag":1,"validateid":0,"id":3},{"fieldid":760203,"fieldname":"JDMC","displayname":"街道名称","tableid":7602,"datatypename":"NVARCHAR2","datatypelength":100,"nullflag":1,"primarykeyfieldflag":0,"relatedfieldflag":0,"listfieldflag":0,"queryfieldflag":0,"displayorder":12,"patrolfieldflag":0,"componentid":1,"colspan":2,"editflag":1,"validateid":0,"id":13},{"fieldid":760204,"fieldname":"BHLX","displayname":"病害类型","tableid":7602,"datatypename":"NVARCHAR2","datatypelength":20,"nullflag":1,"primarykeyfieldflag":0,"relatedfieldflag":0,"listfieldflag":0,"queryfieldflag":0,"displayorder":4,"diccode":"disease_type","patrolfieldflag":0,"componentid":0,"colspan":1,"editflag":1,"validateid":0,"relateddiccode":"null","id":5},{"fieldid":760205,"fieldname":"JJCD","displayname":"紧急程度","tableid":7602,"datatypename":"NVARCHAR2","datatypelength":10,"nullflag":1,"primarykeyfieldflag":0,"relatedfieldflag":0,"listfieldflag":0,"queryfieldflag":0,"displayorder":5,"diccode":"emergency_degree","patrolfieldflag":0,"componentid":0,"colspan":1,"editflag":1,"validateid":0,"relateddiccode":"null","id":6},{"fieldid":760206,"fieldname":"SZWZ","displayname":"所在位置","tableid":7602,"datatypename":"NVARCHAR2","datatypelength":100,"nullflag":1,"primarykeyfieldflag":0,"relatedfieldflag":0,"listfieldflag":0,"queryfieldflag":0,"displayorder":14,"patrolfieldflag":0,"componentid":1,"colspan":2,"editflag":1,"validateid":0,"id":15},{"fieldid":760207,"fieldname":"WTMS","displayname":"问题描述","tableid":7602,"datatypename":"NVARCHAR2","datatypelength":200,"nullflag":1,"primarykeyfieldflag":0,"relatedfieldflag":0,"listfieldflag":0,"queryfieldflag":0,"displayorder":11,"patrolfieldflag":0,"componentid":1,"colspan":2,"editflag":1,"validateid":0,"id":12},{"fieldname":"activityChineseName","displayname":"节点名称","primarykeyfieldflag":0},{"fieldname":"create","displayname":"节点创建时间","primarykeyfieldflag":0},{"fieldname":"end","displayname":"节点结束时间","primarykeyfieldflag":0},{"fieldid":760201,"fieldname":"SBR","displayname":"上报人","tableid":7602,"datatypename":"NVARCHAR2","datatypelength":20,"nullflag":1,"primarykeyfieldflag":0,"relatedfieldflag":0,"listfieldflag":0,"queryfieldflag":0,"displayorder":1,"patrolfieldflag":0,"componentid":1,"colspan":1,"editflag":1,"validateid":0,"id":2}],{"jjcd":{"elementId":9330,"elementCode":"JJCD","elementName":"紧急程度","displayFlag":1},"id":{"elementId":9333,"elementCode":"ID","elementName":"主键ID","displayFlag":0},"sbr":{"elementId":9319,"elementCode":"SBR","elementName":"上报人","displayFlag":1},"jdmc":{"elementId":9328,"elementCode":"JDMC","elementName":"街道名称","displayFlag":1},"activityChineseName":{"elementId":9268,"elementCode":"activityChineseName","elementName":"节点名称","displayFlag":1},"wtms":{"elementId":9332,"elementCode":"WTMS","elementName":"问题描述","displayFlag":1},"bhlx":{"elementId":9329,"elementCode":"BHLX","elementName":"病害类型","displayFlag":1},"create":{"elementId":9271,"elementCode":"create","elementName":"节点创建时间","displayFlag":1},"sbsj":{"elementId":9327,"elementCode":"SBSJ","elementName":"上报时间","displayFlag":1},"end":{"elementId":9272,"elementCode":"end","elementName":"节点结束时间","displayFlag":1},"szwz":{"elementId":9331,"elementCode":"SZWZ","elementName":"所在位置","displayFlag":1},"templateName":{"elementId":9269,"elementCode":"templateName","elementName":"流程名称","displayFlag":1}},[{"jjcd":[{"id":292,"typecode":"emergency_degree","code":"1","name":"一般","status":1},{"id":293,"typecode":"emergency_degree","code":"2","name":"较紧急","status":1},{"id":294,"typecode":"emergency_degree","code":"3","name":"紧急","status":1}],"bhlx":[{"id":315,"typecode":"disease_type","code":"5","name":"裂缝、脱皮、风化","status":1},{"id":311,"typecode":"disease_type","code":"1","name":"缺失","status":1},{"id":312,"typecode":"disease_type","code":"2","name":"沉陷","status":1},{"id":313,"typecode":"disease_type","code":"3","name":"松动","status":1},{"id":314,"typecode":"disease_type","code":"4","name":"相邻不平顺","status":1},{"id":316,"typecode":"disease_type","code":"6","name":"盲道被占用、不规范","status":1},{"id":317,"typecode":"disease_type","code":"7","name":"盲条、盲点磨损","status":1},{"id":318,"typecode":"disease_type","code":"8","name":"无障碍坡道不规范","status":1},{"id":319,"typecode":"disease_type","code":"9","name":"破碎断裂","status":1},{"id":320,"typecode":"disease_type","code":"10","name":"坑洞","status":1},{"id":321,"typecode":"disease_type","code":"11","name":"线裂","status":1},{"id":322,"typecode":"disease_type","code":"12","name":"网裂","status":1},{"id":323,"typecode":"disease_type","code":"13","name":"啃边","status":1},{"id":324,"typecode":"disease_type","code":"14","name":"坑槽","status":1},{"id":325,"typecode":"disease_type","code":"15","name":"剥落","status":1},{"id":326,"typecode":"disease_type","code":"16","name":"拥包","status":1},{"id":327,"typecode":"disease_type","code":"17","name":"车辙","status":1}]}],[["2017-11-12","","1","1","","","测试"]]]
     * totalItems : 1
     * lastPage : true
     * prePage : 1
     * nextPage : 1
     * totalPages : 1
     * firstPage : true
     * offset : 0
     * orderBySetted : true
     */

    private int pageNo;
    private int pageSize;
    private String orderBy;
    private String orderDir;
    private boolean countTotal;
    private int totalItems;
    private boolean lastPage;
    private int prePage;
    private int nextPage;
    private int totalPages;
    private boolean firstPage;
    private int offset;
    private boolean orderBySetted;
    private String result;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderDir() {
        return orderDir;
    }

    public void setOrderDir(String orderDir) {
        this.orderDir = orderDir;
    }

    public boolean isCountTotal() {
        return countTotal;
    }

    public void setCountTotal(boolean countTotal) {
        this.countTotal = countTotal;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isOrderBySetted() {
        return orderBySetted;
    }

    public void setOrderBySetted(boolean orderBySetted) {
        this.orderBySetted = orderBySetted;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
