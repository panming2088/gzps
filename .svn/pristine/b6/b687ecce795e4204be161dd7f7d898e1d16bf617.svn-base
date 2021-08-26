package com.augurit.am.cmpt.coordt.util;


import com.augurit.am.cmpt.coordt.model.CoincidentPoint;
import com.augurit.am.cmpt.coordt.model.Coordinate;
import com.augurit.am.fw.utils.log.LogUtil;

import java.util.List;

/**
 * 包名：com.augurit.am.cmpt.coordt.util
 * 文件描述：运算的工具类
 * 创建人：caipeng
 * 创建时间：2016-08-31 11:05
 * 修改人：xuciluan
 * 修改时间：2016-08-31 11:05
 * 修改备注：
 */
public final class CoordConvertorAction {
    private CoordConvertorAction() {
    }

    //转置矩阵
    public static double[][] trans(double[][] data) {
        int i = data.length;
        int j = data[0].length;
        double[][] data2 = new double[j][i];
        for (int k2 = 0; k2 < j; k2++) {
            for (int k1 = 0; k1 < i; k1++) {
                data2[k2][k1] = data[k1][k2];
            }
        }
        //将矩阵转置便可得到伴随矩阵
        return data2;
    }

    //矩阵相乘
    public static double[][] multi(double[][] data, double[][] data2) {
        int x = data.length;
        int y1 = data[0].length;
        int y2 = data2.length;
        int z = data2[0].length;
        double[][] data3 = new double[x][z];
        if (y1 != y2) {
            LogUtil.w("矩阵无法相乘");
        } else {
            for (int i = 0; i < x; i++) {
                for (int k = 0; k < z; k++) {
                    data3[i][k] = 0;
                    for (int j = 0; j < y1; j++) {
                        data3[i][k] += data[i][j] * data2[j][k];
                    }
                }
            }
        }
        return data3;
    }

    //矩阵数乘
    public static double[][] kmulti(double k, double[][] data) {
        int x = data.length;
        int y = data[0].length;
        double[][] data2 = new double[x][y];

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                data2[i][j] = k * data[i][j];
            }
        }
        return data2;
    }

    //矩阵相加
    public static double[][] add(double[][] data, double[][] data2) {
        int x1 = data.length;
        int y1 = data[0].length;
        int x2 = data2.length;
        int y2 = data2[0].length;
        double[][] data3 = new double[x1][y1];
        if (x1 != x2 || y1 != y2) {
            LogUtil.w("矩阵无法相加");
        } else {
            for (int i = 0; i < x1; i++) {
                for (int j = 0; j < y1; j++) {
                    data3[i][j] = data[i][j] + data2[i][j];
                }
            }
        }
        return data3;
    }

    //矩阵转置
    public static double[][] inv(double[][] data) {
        int m_length = data.length;
        double[][] temp = new double[m_length][2 * m_length];
        double[][] back_temp = new double[m_length][m_length];
        for (int x = 0; x < m_length; x++) {
            for (int y = 0; y < temp[0].length; y++) {
                if (y > m_length - 1) {
                    if (x == (y - m_length))
                        temp[x][y] = 1;
                    else
                        temp[x][y] = 0;
                } else {
                    temp[x][y] = data[x][y];
                }
            }
        }
        for (int x = 0; x < m_length; x++) {
            double var = temp[x][x];
            for (int w = x; w < temp[0].length; w++) {
                if (temp[x][x] == 0) {
                    int k;
                    for (k = x + 1; k < temp.length; k++) {
                        if (temp[k][k] != 0) {
                            for (int t = 0; t < temp[0].length; t++) {
                                double tmp = temp[x][t];
                                temp[x][t] = temp[k][t];
                                temp[k][t] = tmp;
                            }
                            break;
                        }
                    }
                    if (k >= temp.length)
                        return back_temp;
                    var = temp[x][x];
                }
                temp[x][w] /= var;
            }
            for (int z = 0; z < m_length; z++) {
                double var_tmp = 0.0;
                for (int w = x; w < temp[0].length; w++) {
                    if (w == x)
                        var_tmp = temp[z][x];
                    if (x != z)
                        temp[z][w] -= (var_tmp * temp[x][w]);
                }
            }
        }
        for (int x = 0; x < m_length; x++) {
            for (int y = 0; y < m_length; y++) {
                back_temp[x][y] = temp[x][y + m_length];
            }
        }
        return back_temp;
    }

    //将重合点coincidentPoint中原坐标coor_1的第n组到第m组之间的数据添加到B中
    public static double[][] newB2(int n, int m, List coincidentPoint) {
        int i = m - n + 1;
        double[][] B = new double[2 * i][4];
        for (int j = n - 1; j < i; j++) {
            CoincidentPoint CoincidentPoint = (CoincidentPoint) coincidentPoint.get(j);
            Coordinate XY_1 = CoincidentPoint.getCoor_1();
            double X_1 = XY_1.getX();
            double Y_1 = XY_1.getY();
            B[2 * j + 0][0] = 1;//dx
            B[2 * j + 0][1] = 0;//dy
            B[2 * j + 0][2] = X_1;//a
            B[2 * j + 0][3] = -Y_1;//b
            B[2 * j + 1][0] = 0;
            B[2 * j + 1][1] = 1;
            B[2 * j + 1][2] = Y_1;
            B[2 * j + 1][3] = X_1;
        }
        return B;
    }

    //将重合点coincidentPoint中原坐标coor_2第n组到第m组之间的数据添加到L中
    public static double[][] newL2(int n, int m, List coincidentPoint) {
        int i = m - n + 1;
        double[][] L = new double[2 * i][1];
        for (int j = n - 1; j < i; j++) {
            CoincidentPoint CoincidentPoint = (CoincidentPoint) coincidentPoint.get(j);
            Coordinate XY_2 = CoincidentPoint.getCoor_2();
            double X_2 = XY_2.getX();
            double Y_2 = XY_2.getY();
            L[2 * j + 0][0] = X_2;
            L[2 * j + 1][0] = Y_2;
        }
        return L;
    }

    //将重合点coincidentPoint中原坐标coor_1的第n组到第m组之间的数据添加到B中
    public static double[][] newB3(int n, int m, List coincidentPoint) {
        int i = m - n + 1;
        double[][] B = new double[3 * i][7];
        for (int j = n - 1; j < i; j++) {
            CoincidentPoint CoincidentPoint = (CoincidentPoint) coincidentPoint.get(j);
            Coordinate XYZ_1 = CoincidentPoint.getCoor_1();
            double X_1 = XYZ_1.getX();
            double Y_1 = XYZ_1.getY();
            double Z_1 = XYZ_1.getZ();
            B[3 * j + 0][0] = 1;//dx
            B[3 * j + 0][1] = 0;//dy
            B[3 * j + 0][2] = 0;//dz
            B[3 * j + 0][3] = X_1;//m+1
            B[3 * j + 0][4] = 0;//wx
            B[3 * j + 0][5] = -Z_1;//wy
            B[3 * j + 0][6] = Y_1;//wz
            B[3 * j + 1][0] = 0;
            B[3 * j + 1][1] = 1;
            B[3 * j + 1][2] = 0;
            B[3 * j + 1][3] = Y_1;
            B[3 * j + 1][4] = Z_1;
            B[3 * j + 1][5] = 0;
            B[3 * j + 1][6] = -X_1;
            B[3 * j + 2][0] = 0;
            B[3 * j + 2][1] = 0;
            B[3 * j + 2][2] = 1;
            B[3 * j + 2][3] = Z_1;
            B[3 * j + 2][4] = -Y_1;
            B[3 * j + 2][5] = X_1;
            B[3 * j + 2][6] = 0;
        }
        return B;
    }

    //将重合点coincidentPoint中原坐标coor_2第n组到第m组之间的数据添加到L中
    public static double[][] newL3(int n, int m, List coincidentPoint) {
        int i = m - n + 1;
        double[][] L = new double[3 * i][1];
        for (int j = n - 1; j < i; j++) {
            CoincidentPoint CoincidentPoint = (CoincidentPoint) coincidentPoint.get(j);
            Coordinate XYZ_2 = CoincidentPoint.getCoor_2();
            double X_2 = XYZ_2.getX();
            double Y_2 = XYZ_2.getY();
            double Z_2 = XYZ_2.getZ();
            L[3 * j + 0][0] = X_2;
            L[3 * j + 1][0] = Y_2;
            L[3 * j + 2][0] = Z_2;
        }
        return L;
    }

}
