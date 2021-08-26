package com.augurit.agmobile.mapengine.common.baidutransform.test;


import com.augurit.agmobile.mapengine.common.baidutransform.pointer.WGSPointer;

/**
 * Created by xcl on 2017/12/4.
 */

public class GeoTest {
    /* static GCJPointer guangzhouGCJPointer = new GCJPointer(23.172295496717588, 113.41189494104974);
     WGSPointer shanghaiWGSPointer = new WGSPointer(31.1774276, 121.5272106);
     GCJPointer shanghaiGCJPointer = new GCJPointer(31.17530398364597, 121.531541859215);
     WGSPointer shenzhenWGSPointer = new WGSPointer(22.543847, 113.912316);
     GCJPointer shenzhenGCJPointer = new GCJPointer(22.540796131694766, 113.9171764808363);
     WGSPointer beijingWGSPointer = new WGSPointer(39.911954, 116.377817);
     GCJPointer beijingGCJPointer = new GCJPointer(39.91334545536069, 116.38404722455657);

   @Test
   public void testWGS2GCJ() {
     assertEquals(shanghaiGCJPointer, shanghaiWGSPointer.toGCJPointer());
     assertEquals(shenzhenGCJPointer, shenzhenWGSPointer.toGCJPointer());
     assertEquals(beijingGCJPointer, beijingWGSPointer.toGCJPointer());
   }

   public void testGCJ2WGS() {
     assertTrue(shanghaiWGSPointer.distance(shanghaiGCJPointer.toWGSPointer()) < 5);
     assertTrue(shenzhenGCJPointer.distance(shenzhenGCJPointer.toWGSPointer()) < 5);
     assertTrue(beijingGCJPointer.distance(beijingGCJPointer.toWGSPointer()) < 5);
   }

   public void testGCJ2ExtactWGS() {
     assertTrue(shanghaiWGSPointer.distance(shanghaiGCJPointer.toExactWGSPointer()) < 0.5);
     assertTrue(shenzhenGCJPointer.distance(shenzhenGCJPointer.toExactWGSPointer()) < 0.5);
     assertTrue(beijingGCJPointer.distance(beijingGCJPointer.toExactWGSPointer()) < 0.5);
   }*/
    public static void testBD2WGS() {
        WGSPointer shanghaiWGSPointer = new WGSPointer(23.17474418, 113.40653061);
//    	WGSPointer guangzhouWGSPointer2 = new WGSPointer(41.7391270423,123.4254369308);
//    	WGSPointer guangzhouWGSPointer1 = new WGSPointer(41.66267487582583,123.36553568741394);
//    	WGSPointer guangzhouBDPointer2 =new WGSPointer(41.7391270423,123.4254369308);

//    	WGSPointer guangzhouBDPointer2 = new BDPointer(41.879573,123.293441).toExactWGSPointer();
//    	WGSPointer guangzhouBDPointer3 = new BDPointer(41.857034,123.595605).toExactWGSPointer();
//    	WGSPointer guangzhouBDPointer4 = new BDPointer(41.843631,123.299661).toExactWGSPointer();
//    	WGSPointer guangzhouBDPointer5 = new BDPointer(41.775196,123.434349).toExactWGSPointer();
//    	WGSPointer guangzhouBDPointer6 = new BDPointer(41.777921,123.310405).toExactWGSPointer();
//    	WGSPointer guangzhouBDPointer7 = new BDPointer(41.818927,123.599272).toExactWGSPointer();
//    	WGSPointer guangzhouBDPointer8 = new BDPointer(41.525374,123.381537).toExactWGSPointer();
        System.out.println("xy=="+shanghaiWGSPointer.toGCJPointer().toString());

    }

    public static void main(String[] args) {
        testBD2WGS();
    }
}
