package com.augurit.agmobile.mapengine.layerdownload.model;

/**
 * 描述：瓦片
 *
 * @author 创建人 ：liangsh
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layerdownload.model
 * @createTime 创建时间 ：2016-09-26
 * @modifyBy 修改人 ：liangsh
 * @modifyTime 修改时间 ：2016-09-26
 * @modifyMemo 修改备注：
 */
public class Tile{
    private int level;
    private int row;
    private int col;

    public Tile(int level, int row, int col){
        this.level = level;
        this.row = row;
        this.col = col;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
