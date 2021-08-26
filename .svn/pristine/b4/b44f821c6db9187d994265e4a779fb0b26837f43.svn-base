
  //当前日历显示的年份
  var showYear;
  //当前日历显示的月份
  var showMonth;
  //初始化日历
  function init(year,month,signList){
    showYear=year;
    showMonth=month;
    draw(signList);
  }

  function draw(signList){
    //绑定日历
    var str =drawCal(showYear,showMonth,signList);
    $("#calendar").html(str);
  }


  function getDaysInmonth(iMonth, iYear){
   var dPrevDate = new Date(iYear, iMonth, 0);
   return dPrevDate.getDate();
  }

  function bulidCal(iYear, iMonth) {
   var aMonth = new Array();
   aMonth[0] = new Array(7);
   aMonth[1] = new Array(7);
   aMonth[2] = new Array(7);
   aMonth[3] = new Array(7);
   aMonth[4] = new Array(7);
   aMonth[5] = new Array(7);
   aMonth[6] = new Array(7);
   var dCalDate = new Date(iYear, iMonth - 1, 1);
   var iDayOfFirst = dCalDate.getDay();
   var iDaysInMonth = getDaysInmonth(iMonth, iYear);
   var iVarDate = 1;
   var d, w;
   aMonth[0][0] = "日";
   aMonth[0][1] = "一";
   aMonth[0][2] = "二";
   aMonth[0][3] = "三";
   aMonth[0][4] = "四";
   aMonth[0][5] = "五";
   aMonth[0][6] = "六";
   for (d = iDayOfFirst; d < 7; d++) {
    aMonth[1][d] = iVarDate;
    iVarDate++;
   }
   for (w = 2; w < 7; w++) {
     for (d = 0; d < 7; d++) {
       if (iVarDate <= iDaysInMonth) {
          aMonth[w][d] = iVarDate;
          iVarDate++;
         }
      }
   }
   return aMonth;
  }

  function ifHasSigned(signList,day){
   var signed = false;
   var str = signList.monthlySignDate;
   $.each(str,function(index,item){
    if(item == day) {
     signed = true;
     return false;
    }
   });
   return signed ;
  }

   function drawCal(iYear, iMonth ,signList) {
   var myMonth = bulidCal(iYear, iMonth);
   var htmls = new Array();
   htmls.push("<div class='sign' id='sign_cal'>");
   htmls.push("<table>");
   htmls.push("<tr>");
   htmls.push("<th>" + myMonth[0][0] + "</th>");
   htmls.push("<th>" + myMonth[0][1] + "</th>");
   htmls.push("<th>" + myMonth[0][2] + "</th>");
   htmls.push("<th>" + myMonth[0][3] + "</th>");
   htmls.push("<th>" + myMonth[0][4] + "</th>");
   htmls.push("<th>" + myMonth[0][5] + "</th>");
   htmls.push("<th>" + myMonth[0][6] + "</th>");
   htmls.push("</tr>");
   var d, w;
   for (w = 1; w < 7; w++) {
    htmls.push("<tr>");
    for (d = 0; d < 7; d++) {
     var isSigned =ifHasSigned(signList,myMonth[w][d]);
     console.log(ifHasSigned);
     if(isSigned){
      htmls.push("<td class='on'>" + (!isNaN(myMonth[w][d]) ? myMonth[w][d] : " ") + "</td>");
     } else {
      htmls.push("<td>" + (!isNaN(myMonth[w][d]) ? myMonth[w][d] : " ") + "</td>");
     }
    }
    htmls.push("</tr>");
   }
   htmls.push("</table>");
   htmls.push("</div>");
   return htmls.join('');
  }
