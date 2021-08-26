package com.augurit.am.cmpt.shapefile.reader;

import com.augurit.am.cmpt.shapefile.model.Point;
import com.augurit.am.cmpt.shapefile.model.PointShp;
import com.augurit.am.cmpt.shapefile.model.PolyLineAndPolygonShp;
import com.augurit.am.cmpt.shapefile.model.ShapeFile;
import com.augurit.am.cmpt.shapefile.util.ByteUtil;
import com.augurit.am.cmpt.shapefile.util.FormatTransfer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class ShapeFileReader {
	
	private String filename;
	private ShapeFile shapeFile;
	
	public ShapeFileReader(String filename){
		this.filename = filename;
		shapeFile = new ShapeFile();
	}
	
	public void read(){
		InputStream in = null;
		try {
			in = new FileInputStream(filename);
			readShpHead(in);
			switch(shapeFile.getShapeType()){
			case 1:
				readPointShp(in);
				break;
			case 3:
				
			case 5:
				readPolyLineAndPolygonShp(in);
				break;
				
			case 11:
				readPointZShp(in);
				break;
			default:
				break;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public void readShpHead(InputStream in) {
		int fileCode;
		int unused;
		int fileLength;
		int version;
		int shapeType;
		double xmin;
		double ymin;
		double xmax;
		double ymax;
		double zmin;
		double zmax;
		double mmin;
		double mmax;
		// 读取坐标文件头的内容开始
		fileCode = ByteUtil.byteArrayToInt(readbytes(in, 4));
		for (int i = 0; i < 5; i++) {
			unused = ByteUtil.byteArrayToInt(readbytes(in, 4));
		}
		fileLength = ByteUtil.byteArrayToInt(readbytes(in, 4));
		// fileLength = FormatTransfer.reverseInt(fileLength);
		version = ByteUtil.byteArrayToInt(readbytes(in, 4));
		version = FormatTransfer.reverseInt(version);
		shapeType = ByteUtil.byteArrayToInt(readbytes(in, 4));
		shapeType = FormatTransfer.reverseInt(shapeType);
		xmin = ByteUtil.getDouble(readbytes(in, 8));
		ymin = ByteUtil.getDouble(readbytes(in, 8));
		xmax = ByteUtil.getDouble(readbytes(in, 8));
		ymax = ByteUtil.getDouble(readbytes(in, 8));
		zmin = ByteUtil.getDouble(readbytes(in, 8));
		zmax = ByteUtil.getDouble(readbytes(in, 8));
		mmin = ByteUtil.getDouble(readbytes(in, 8));
		mmax = ByteUtil.getDouble(readbytes(in, 8));
		shapeFile.setFileCode(fileCode);
		shapeFile.setFileLength(fileLength);
		shapeFile.setVersion(version);
		shapeFile.setShapeType(shapeType);
		shapeFile.setXmin(xmin);
		shapeFile.setYmin(ymin);
		shapeFile.setXmax(xmax);
		shapeFile.setYmax(ymax);
		shapeFile.setZmin(zmin);
		shapeFile.setZmax(zmax);
		shapeFile.setMmin(mmin);
		shapeFile.setMmax(mmax);
//		System.out.println(fileCode);
//		System.out.println(fileLength);
//		System.out.println(version);
//		System.out.println(shapeType);
//		System.out.println(xmin);
//		System.out.println(ymin);
//		System.out.println(xmax);
//		System.out.println(ymax);
//		System.out.println(zmin);
//		System.out.println(zmax);
//		System.out.println(mmin);
//		System.out.println(mmax);

		// 读取坐标文件头的内容结束
	}
	
	public void readPointShp(InputStream in) {
		int recordNumber;
		int contentLength;
		int num = 0;
		byte[] recordNumberBytes = new byte[4];
		try {
			while ((in.read(recordNumberBytes)) != -1) {
				PointShp point = new PointShp();
				num++;
				recordNumber = ByteUtil.byteArrayToInt(recordNumberBytes);
				contentLength = ByteUtil.byteArrayToInt(readbytes(in, 4));
				
//				ContentLength = changeByteOrder(ContentLength);
				int shapeType;
				double x;
				double y;
				shapeType = ByteUtil.byteArrayToInt(readbytes(in, 4));
				shapeType = FormatTransfer.reverseInt(shapeType);
				x = ByteUtil.getDouble(readbytes(in, 8));
				y = ByteUtil.getDouble(readbytes(in, 8));
				point.setRecordNumber(recordNumber);
				point.setContentLength(contentLength);
				point.setShapeType(shapeType);
				point.setX(x);
				point.setY(y);
				shapeFile.getPointShps().add(point);
//				System.out.print(RecordNumber + " ");
//				System.out.print(shapeType+ " ");
//				System.out.print(x+ " ");
//				System.out.println(y +"\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public  void readPolyLineAndPolygonShp(InputStream in) {
		int recordNumber;
		int contentLength;
		int num = 0;
		int i;
		byte[] recordNumberBytes = new byte[4];
		try {
			while ((in.read(recordNumberBytes)) != -1) {
				PolyLineAndPolygonShp poly = new PolyLineAndPolygonShp();
				num++;
				recordNumber = ByteUtil.byteArrayToInt(recordNumberBytes);
				contentLength = ByteUtil.byteArrayToInt(readbytes(in, 4));
				poly.setRecordNumber(recordNumber);
				poly.setContentLength(contentLength);
				int shapeType;
				double box[] = new double[4];
				int numParts;//子线段个数,表示构成当前线目标的子线段的个数
				int numPoints;//坐标点数,表示构成当前线目标所包含的坐标点个数
				int parts[];//记录了每个子线段的坐标在 Points 数组中的起始位置
				shapeType = ByteUtil.byteArrayToInt(readbytes(in, 4));
				shapeType = FormatTransfer.reverseInt(shapeType);
				poly.setShapeType(shapeType);
				// 读 Box
				for (i = 0; i < 4; i++){
					box[i] = ByteUtil.getDouble(readbytes(in, 8));
				}
				poly.setBox(box);
				// 读 NumParts 和 NumPoints
				numParts = ByteUtil.byteArrayToInt(readbytes(in, 4));
				numPoints = ByteUtil.byteArrayToInt(readbytes(in, 4));
				numParts = FormatTransfer.reverseInt(numParts);
				numPoints = FormatTransfer.reverseInt(numPoints);
				poly.setNumParts(numParts);
				poly.setNumPoints(numPoints);
				// 读 Parts 和 Points
				parts = new int[numParts];
				for (i = 0; i < numParts; i++){
					parts[i] = ByteUtil.byteArrayToInt(readbytes(in, 4));
					parts[i] = FormatTransfer.reverseInt(parts[i]);
				}
				poly.setParts(parts);
				Point[] points = new Point[numPoints];
//				int pointsIndex = 0;
				int pointNum;
				for (i = 0; i < numParts; i++) {
					if (i != numParts - 1)
						pointNum = parts[i + 1] - parts[i];
					else
						pointNum = numPoints - parts[i];
					double pointsX[] = new double[pointNum];
					double pointsY[] = new double[pointNum];

					pointsX = new double[pointNum];
					pointsY = new double[pointNum];
					int j;
					for (j = 0; j < pointNum; j++) {
						pointsX[j] = ByteUtil.getDouble(readbytes(in, 8));
						pointsY[j] = ByteUtil.getDouble(readbytes(in, 8));
						Point point = new Point();
						point.setX(pointsX[j]);
						point.setY(pointsY[j]);
						points[j] = point;
					}
				}
				poly.setPoints(points);
				shapeFile.getPolyLineAndPolygonShp().add(poly);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readPointZShp(InputStream in){
		int recordNumber;
		int contentLength;
		int num = 0;
		byte[] recordNumberBytes = new byte[4];
		try {
			while ((in.read(recordNumberBytes)) != -1) {
				PointShp point = new PointShp();
				num++;
				recordNumber = ByteUtil.byteArrayToInt(recordNumberBytes);
				contentLength = ByteUtil.byteArrayToInt(readbytes(in, 4));
				
//				ContentLength = changeByteOrder(ContentLength);
				int shapeType;
				double x;
				double y;
				double z;
				double m;
				shapeType = ByteUtil.byteArrayToInt(readbytes(in, 4));
				shapeType = FormatTransfer.reverseInt(shapeType);
				x = ByteUtil.getDouble(readbytes(in, 8));
				y = ByteUtil.getDouble(readbytes(in, 8));
				z = ByteUtil.getDouble(readbytes(in, 8));
				m = ByteUtil.getDouble(readbytes(in, 8));
				point.setRecordNumber(recordNumber);
				point.setContentLength(contentLength);
				point.setShapeType(shapeType);
				point.setX(x);
				point.setY(y);
				point.setZ(z);
				point.setZ(z);
				point.setM(m);
				shapeFile.getPointShps().add(point);
//				System.out.print(RecordNumber + " ");
//				System.out.print(shapeType+ " ");
//				System.out.print(x+ " ");
//				System.out.println(y +"\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public static byte[] readbytes(InputStream in, int bytes) {
		byte[] tempbytes = new byte[bytes];
		try {
			// 一次读多个字节
			int byteread = 0;
			byteread = in.read(tempbytes);
			// ReadFromFile.showAvailableBytes(in);
			// 读入多个字节到字节数组中，byteread为一次读入的字节数
			// while ((byteread = in.read(tempbytes)) != -1) {
			// System.out.write(tempbytes, 0, byteread);
			// }
		} catch (Exception e1) {
			e1.printStackTrace();
			if (in != null) {
				try {
					in.close();
				} catch (IOException e2) {
				}
			}
			return tempbytes;
		}
		return tempbytes;
	}

	public ShapeFile getShapeFile() {
		return shapeFile;
	}

	public void setShapeFile(ShapeFile shapeFile) {
		this.shapeFile = shapeFile;
	}

}
