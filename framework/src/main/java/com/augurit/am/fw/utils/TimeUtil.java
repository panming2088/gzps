package com.augurit.am.fw.utils;

import android.text.TextUtils;
import android.util.Log;

import com.augurit.am.fw.utils.model.TimeCompare;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.augurit.am.fw.utils.constant.TimeFormat.YYYY_MM_DD_HH_MM;

/**
 * Created by ac on 2016-07-26.
 */
public final class TimeUtil {
    private TimeUtil() {
    }

    /**
     * 获取格式为“HHmms”的当前时间字符串
     *
     * @return 格式为“HHmms”的当前时间字符串
     */
    public static String getHHmmssTimeStamp() {
        return getHHMMSSTimeStampFromSSIndex(0);
    }

    /**
     * To get the String of the time whose formation like "HHmms".
     *
     * @param index how many seconds before or after the current time.
     * @return
     */
    public static String getHHMMSSTimeStampFromSSIndex(int index) {
        DateFormat format = new SimpleDateFormat("HHmmss");
        String formatTime = format.format(getSSDate(index));
        return formatTime;
    }

    /**
     * To get the String of the time whose formation like "yyyyMMdd".
     *
     * @param index how many days before or after the current time.
     * @return
     */
    public static String getYYYYMMDDTimeStampFromDDIndex(int index) {
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        String formatTime = format.format(getDDDate(index));
        return formatTime;
    }

    /**
     * To get the Date before or after the current time that is compared with
     * seconds.
     *
     * @param index
     * @return
     */
    public static Date getSSDate(int index) {
        Date date = getDate(new Date(), AddDateType.SS, index);
        return date;
    }

    /**
     * To get the Date before or after the current time that is compared with
     * days.
     *
     * @param index
     * @return
     */
    public static Date getDDDate(int index) {
        Date date = getDate(new Date(), AddDateType.DAY, index);
        return date;
    }

    /**
     * To get the Date before or after the current time that is compared with
     * different type.
     *
     * @param date
     * @param dateType
     * @param index
     * @return
     */
    public static Date getDate(Date date, AddDateType dateType, int index) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (AddDateType.YEAR.equals(dateType)) {
            calendar.add(Calendar.YEAR, index);
            return calendar.getTime();
        }
        if (AddDateType.MONTH.equals(dateType)) {
            calendar.add(Calendar.MONTH, index);
            return calendar.getTime();
        }
        if (AddDateType.DAY.equals(dateType)) {
            calendar.add(Calendar.DAY_OF_MONTH, index);
            return calendar.getTime();
        }
        if (AddDateType.HH.equals(dateType)) {
            calendar.add(Calendar.HOUR_OF_DAY, index);
            return calendar.getTime();
        }
        if (AddDateType.MM.equals(dateType)) {
            calendar.add(Calendar.MINUTE, index);
            return calendar.getTime();
        }
        if (AddDateType.SS.equals(dateType)) {
            calendar.add(Calendar.SECOND, index);
            return calendar.getTime();
        }
        return date;
    }

    public static String getStringTimeYMDS(Date date) {
        if (date == null)
            return "";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(date);
    }

    public static String getStringTimeMDS(Date date) {
        if (date == null)
            return "";
        return new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())
                .format(date);
    }

    public static String getStringTimeDS(Date date) {
        if (date == null)
            return "";
        return new SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                .format(date);
    }

    public static Date getDateTimeYMDS(String dateString) throws ParseException {
        if (dateString == null || dateString.equals(""))
            return new Date();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .parse(dateString);
    }

    public static String getStringTimeYYMMDDSS(Date date) {
        if (date == null)
            return "";
        return new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                .format(date);
    }
    public static String getStringTimeYMDSFromDate(Date date) {
        if (date == null)
            return "";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(date);
    }

    public static String getStringTimeYMDFromDate(Date date) {
        if (date == null)
            return "";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                .format(date);
    }

    public static Date getDateTimeYYMMDDSS(String dateString)
            throws ParseException {
        if (dateString == null || dateString.equals(""))
            return new Date();
        return new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                .parse(dateString);
    }

    public static String getStringTimeYMD(Date date) {
        if (date == null)
            return "";
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(date);
    }

    public static Date getDateTimeYMD(String dateString) throws ParseException {
        if (dateString == null || dateString.equals(""))
            return new Date();
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .parse(dateString);
    }

    public static String getStringTimeYYMMDD(Date date) {
        if (date == null)
            return "";
        return new SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                .format(date);
    }

    public static Date getDateTimeYYMMDD(String dateString)
            throws ParseException {
        if (dateString == null || dateString.equals(""))
            return new Date();
        return new SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                .parse(dateString);
    }

    public static String getStringTimeYMDSChines(Date date) {
        if (date == null)
            return "";
        return new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒", Locale.getDefault())
                .format(date);
    }

    public static Date getDateTimeYMDSChines(String dateString)
            throws ParseException {
        if (dateString == null || dateString.equals(""))
            return new Date();
        return new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒", Locale.getDefault())
                .parse(dateString);
    }

    public static String getStringTimeYMDMChines(Date date) {
        if (date == null)
            return "";
        return new SimpleDateFormat("yyyy年MM月dd日HH时mm分", Locale.getDefault())
                .format(date);
    }

    public static String getStringTimeMdHmChines(Date date) {
        if (date == null)
            return "";
        return new SimpleDateFormat("MM月dd日HH时mm分", Locale.getDefault())
                .format(date);
    }

    public static Date getDateTimeYMDMChines(String dateString)
            throws ParseException {
        if (dateString == null || dateString.equals(""))
            return new Date();
        return new SimpleDateFormat("yyyy年MM月dd日HH时mm分", Locale.getDefault())
                .parse(dateString);
    }

    public static String getStringTimeHHmmChinese(Date date) {
        if (date == null)
            return "";
        return new SimpleDateFormat("HH时mm分", Locale.getDefault())
                .format(date);
    }

    public static String getStringTimeYMDChines(Date date) {
        if (date == null)
            return "";
        return new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault())
                .format(date);
    }

    public static Date getDateTimeYMDChines(String dateString)
            throws ParseException {
        if (dateString == null || dateString.equals(""))
            return new Date();
        return new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault())
                .parse(dateString);
    }

    public static Long getCurrentTimeMillis() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static String getTimeStamp() {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        return String.valueOf(ts.getTime());
    }

    public static Date getNewDate() {
        return new Date();
    }

    /**
     * @param date1 The time that will compare with date2 shouldn't be null and it
     *              has to be the correct date format:"yyyy-MM-dd".
     * @param date2 The time that is to be compared with date1 and it has to be
     *              the correct date format:"yyyy-MM-dd".If it is null,the default
     *              value will be the current time.
     * @param stype 0: how many days; 1: how many months; 2: how many years.
     * @return:TimeCompare
     */
    public static TimeCompare compareDate(String date1, String date2, int stype)
            throws ParseException {
        if (date1 == null) {
            throw new NullPointerException(
                    "The first Date parameter cannot be null");
        }
        TimeCompare timeCompare = new TimeCompare();
        int n = 0;

        String[] u = {"天", "月", "年"};
        String formatStyle = stype == 1 ? "yyyy-MM" : "yyyy-MM-dd";

        date2 = date2 == null ? getStringTimeYMD(getNewDate()) : date2;

        DateFormat df = new SimpleDateFormat(formatStyle, Locale.getDefault());
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(df.parse(date1));
        c2.setTime(df.parse(date2));
        // List list = new ArrayList();
        if (!c1.after(c2)) {
            timeCompare.setBigger(false);
            while (!c1.after(c2)) { // To compare Circularly , until c2 is after
                // c1, n-1 is the results
                // list.add(df.format(c1.getTime())); // Here you can put the
                // dates which are on interval in the array and print it out
                n++;
                if (stype == 1) {
                    c1.add(Calendar.MONTH, 1); // Compare month,plus one
                } else {
                    c1.add(Calendar.DATE, 1); // Compare day,plus one
                }
            }
        } else {
            timeCompare.setBigger(true);
            while (c1.after(c2) || c1.equals(c2)) { // To compare Circularly ,
                // until c1 is after c2, n-1
                // is the results
                // list.add(df.format(c1.getTime())); // Here you can put the
                // dates which are on interval in the array and print it out
                n++;
                if (stype == 1) {
                    c2.add(Calendar.MONTH, 1); // Compare month,plus one
                } else {
                    c2.add(Calendar.DATE, 1); // Compare day,plus one
                }
            }
        }
        if (stype == 2) {
            // The result for years comparation.No need to
            // minus one.
            n = n / 365;
        } else {
            n = n - 1;
        }
        // System.out.println(date1 + " -- " + date2 + " how many" + u[stype] +
        // ":"
        // + n);
        timeCompare.setDifference(n);
        return timeCompare;
    }

    // public static List<Date> getBetweenDates(Date fromDate, Date toDate
    //
    // ) {
    // ArrayList<Date> resultDates = new ArrayList<Date>();
    // Calendar calendar = Calendar.getInstance();
    // calendar.setTime(fromDate);
    // Date tmpDate = calendar.getTime();
    // long endTime = toDate.getTime();
    // while (tmpDate.before(toDate) || tmpDate.getTime() == endTime) {
    // resultDates.add(calendar.getTime());
    // calendar.add(Calendar.DAY_OF_YEAR, 1);
    // tmpDate = calendar.getTime();
    // }
    // // Date[] dates = new Date[resultDates.size()];
    // return resultDates;
    // }

    /**
     * To convert the milliseconds to special format,like 03:10.
     *
     * @param millisencond
     * @return
     */
    public static String convertMilliSecondToMinute(int millisencond) {
        int oneMinute = 1000 * 60;
        int minutes = millisencond / oneMinute;
        int sencond = (millisencond - minutes * oneMinute) / 1000;
        return getNum(minutes) + ":" + getNum(sencond);
    }

    /**
     * To convert the seconds to special format,like 03:10.
     *
     * @param seconds
     * @return
     */
    public static String convertSecondToMinute(int seconds) {
        int oneMinute = 60;
        int minutes = seconds / oneMinute;
        int mSecond = seconds - minutes * oneMinute;
        return getNum(minutes) + ":" + getNum(mSecond);
    }

    public static String getNum(int num) {
        if (num >= 10) {
            return "" + num;
        } else {
            return "0" + num;
        }
    }

    /**
     * Get the string format of current time by custom format,such as
     * "MM-dd HH:mm"
     *
     * @param customFormat
     * @return
     */
    public static String getCustomNowDateString(String customFormat) {
        Date nowDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat(customFormat);
        String nowDateString = dateFormat.format(nowDate);
        return nowDateString;
    }

    public static Date getDateTimeYMDM(String dateString) throws ParseException {
        return getDateTime(dateString, YYYY_MM_DD_HH_MM);
    }

    public static Date getDateTime(String dateString, String formateOfDate) throws ParseException {
        if (TextUtils.isEmpty(dateString))
            return new Date();
        return new SimpleDateFormat(formateOfDate, Locale.getDefault())
                .parse(dateString);
    }

    /**
     * 获取两个时间的时间查 如1天2小时30分钟
     */
    public static String getDatePoor(Date endDate, Date nowDate) {

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
       /* // 计算差多少天
        long day = diff / nd;*/
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return hour + "小时" + min + "分钟";
    }

    /**
     * Obtains relative times (It will caculate the relative difference betweent
     * the specific time and the current system time ), the format like
     * "XX minutes ago"
     *
     * @return
     */
    public static String getRelativeTime(String date) {
        Log.i("ACCAndroid_TimeUtil", "date=" + date);
        String time = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dt1 = sdf.parse(date);

            Calendar cl = Calendar.getInstance();
            int year2 = cl.get(Calendar.YEAR);
            int month2 = cl.get(Calendar.MONTH);
            int day2 = cl.get(Calendar.DAY_OF_MONTH);
            int hour2 = cl.get(Calendar.HOUR_OF_DAY);
            int minute2 = cl.get(Calendar.MINUTE);
            int second2 = cl.get(Calendar.SECOND);

            cl.setTime(dt1);
            int year1 = cl.get(Calendar.YEAR);
            int month1 = cl.get(Calendar.MONTH);
            int day1 = cl.get(Calendar.DAY_OF_MONTH);
            int hour1 = cl.get(Calendar.HOUR_OF_DAY);
            int minute1 = cl.get(Calendar.MINUTE);
            int second1 = cl.get(Calendar.SECOND);

            if (year1 == year2) {
                if (month1 == month2) {
                    if (day1 == day2) {
                        if (hour1 == hour2) {
                            if (minute1 == minute2) {
                                time = "刚才";
                            } else {
                                if (minute2 - minute1 < 0) {
                                    time = "刚才";
                                } else {
                                    time = (minute2 - minute1) + "分钟前";
                                }

                            }
                        } else if (hour2 - hour1 > 3) {
                            time = formatTime(hour1, minute1);
                        } else if (hour2 - hour1 == 1) {
                            if (minute2 - minute1 > 0) {
                                time = "1小时前";
                            } else {
                                time = (60 + minute2 - minute1) + "分钟前";
                            }
                        } else {
                            time = (hour2 - hour1) + "小时前";
                        }
                    } else if (day2 - day1 == 1) { // 昨天
                        if (hour1 > 12) {
                            time = (month1 + 1) + "月" + day1 + "日  下午";
                        } else {
                            time = (month1 + 1) + "月" + day1 + "日  上午";
                        }
                    } else {
                        time = (month1 + 1) + "月" + day1 + "日";
                    }
                } else {
                    time = (month1 + 1) + "月" + day1 + "日";
                }
            } else {
                time = year1 + "年" + month1 + "月" + day1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    // private static HashMap<String, String> _userLinkMapping = new
    // HashMap<String, String>();

    //    public static String stringifyStream(InputStream is) throws IOException {
    //        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    //        StringBuilder sb = new StringBuilder();
    //        String line;
    //
    //        while ((line = reader.readLine()) != null) {
    //            sb.append(line + "\n");
    //        }
    //
    //        return sb.toString();
    //    }

    // // Handle "yyyy-MM-dd'T'HH:mm:ss.SSS" from sqlite
    // public static final Date parseDateTimeFromSqlite(String dateString) {
    // try {
    // Log.d(TAG, String.format("in parseDateTime, dateString=%s", dateString));
    // return TwitterDatabase.DB_DATE_FORMATTER.parse(dateString);
    // } catch (ParseException e) {
    // Log.w(TAG, "Could not parse Twitter date string: " + dateString);
    // return null;
    // }
    // }

    //    public static int computeSampleSize(BitmapFactory.Options options,
    //                                        int minSideLength, int maxNumOfPixels) {
    //        int initialSize = computeInitialSampleSize(options, minSideLength,
    //                maxNumOfPixels);
    //
    //        int roundedSize;
    //        if (initialSize <= 8) {
    //            roundedSize = 1;
    //            while (roundedSize < initialSize) {
    //                roundedSize <<= 1;
    //            }
    //        } else {
    //            roundedSize = (initialSize + 7) / 8 * 8;
    //        }
    //
    //        return roundedSize;
    //    }

    //    private static int computeInitialSampleSize(BitmapFactory.Options options,
    //                                                int minSideLength, int maxNumOfPixels) {
    //        double w = options.outWidth;
    //        double h = options.outHeight;
    //
    //        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
    //                .sqrt(w * h / maxNumOfPixels));
    //        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
    //                Math.floor(w / minSideLength), Math.floor(h / minSideLength));
    //
    //        if (upperBound < lowerBound) {
    //            return lowerBound;
    //        }
    //
    //        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
    //            return 1;
    //        } else if (minSideLength == -1) {
    //            return lowerBound;
    //        } else {
    //            return upperBound;
    //        }
    //    }

    //    /**
    //     * 把Bitmap转Byte
    //     */
    //    public static byte[] Bitmap2Bytes(Bitmap bm) {
    //        ByteArrayOutputStream baos = new ByteArrayOutputStream();
    //        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    //        return baos.toByteArray();
    //    }

    //    /**
    //     * 将字节数组转换为ImageView可调用的Bitmap对象
    //     * @param bytes
    //     * @param opts
    //     * @return
    //     */
    //    public static Bitmap getPicFromBytes(byte[] bytes,
    //                                         BitmapFactory.Options opts) {
    //        if (bytes != null) {
    //            if (opts != null) {
    //                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
    //                        opts);
    //            } else {
    //                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    //            }
    //        }
    //        return null;
    //    }

    // private static final Pattern NAME_MATCHER = Pattern.compile("@.+?\\s");
    // private static final Linkify.MatchFilter NAME_MATCHER_MATCH_FILTER = new
    // Linkify.MatchFilter() {
    // @Override
    // public final boolean acceptMatch(final CharSequence s, final int start,
    // final int end) {
    //
    // String name = s.subSequence(start+1, end).toString().trim();
    // boolean result = _userLinkMapping.containsKey(name);
    // return result;
    // }
    // };
    //
    // private static final Linkify.TransformFilter
    // NAME_MATCHER_TRANSFORM_FILTER = new Linkify.TransformFilter() {
    //
    // @Override
    // public String transformUrl(Matcher match, String url) {
    // String name = url.subSequence(1, url.length()).toString().trim();
    // return _userLinkMapping.get(name);
    // }
    // };
    //
    // private static final String TWITTA_USER_URL = "twitta://users/";
    //
    // public static void linkifyUsers(TextView view) {
    // Linkify.addLinks(view, NAME_MATCHER, TWITTA_USER_URL,
    // NAME_MATCHER_MATCH_FILTER, NAME_MATCHER_TRANSFORM_FILTER);
    // }
    //
    // private static final Pattern TAG_MATCHER = Pattern.compile("#\\w+#");
    //
    // private static final Linkify.TransformFilter TAG_MATCHER_TRANSFORM_FILTER
    // =
    // new Linkify.TransformFilter() {
    // @Override
    // public final String transformUrl(Matcher match, String url) {
    // String result = url.substring(1, url.length()-1);
    // return "%23" + result + "%23";
    // }
    // };
    //
    // private static final String TWITTA_SEARCH_URL = "twitta://search/";
    //
    // public static void linkifyTags(TextView view) {
    // Linkify.addLinks(view, TAG_MATCHER, TWITTA_SEARCH_URL,
    // null, TAG_MATCHER_TRANSFORM_FILTER);
    // }
    //
    // public static boolean isTrue(Bundle bundle, String key) {
    // return bundle != null && bundle.containsKey(key) &&
    // bundle.getBoolean(key);
    // }
    //
    // private static Pattern USER_LINK =
    // Pattern.compile("@<a href=\"http:\\/\\/fanfou\\.com\\/(.*?)\" class=\"former\">(.*?)<\\/a>");
    // public static String preprocessText(String text){
    // //处理HTML格式返回的用户链接
    // Matcher m = USER_LINK.matcher(text);
    // while(m.find()){
    // _userLinkMapping.put(m.group(2), m.group(1));
    // Log.d(TAG, String.format("Found mapping! %s=%s", m.group(2),
    // m.group(1)));
    // }
    //
    // //将User Link的连接去掉
    // StringBuffer sb = new StringBuffer();
    // m = USER_LINK.matcher(text);
    // while(m.find()){
    // m.appendReplacement(sb, "@$2");
    // }
    // m.appendTail(sb);
    // return sb.toString();
    // }
    //
    // public static String getSimpleTweetText(String text){
    // return text.replaceAll("<.*?>", "")
    // .replace("&lt;", "<")
    // .replace("&gt;", ">")
    // .replace("&nbsp;", " ")
    // .replace("&amp;", "&")
    // .replace("&quot;", "\"");
    // }
    //
    // public static void setSimpleTweetText(TextView textView, String text){
    // String processedText = getSimpleTweetText(text);
    // textView.setText(processedText);
    // }
    //
    // public static void setTweetText(TextView textView, String text) {
    // String processedText = preprocessText(text);
    // textView.setText(Html.fromHtml(processedText), BufferType.SPANNABLE);
    // Linkify.addLinks(textView, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES);
    // Utils.linkifyUsers(textView);
    // Utils.linkifyTags(textView);
    // _userLinkMapping.clear();
    // }
    //
    //
    // public static Bitmap drawableToBitmap(Drawable drawable) {
    // Bitmap bitmap = Bitmap
    // .createBitmap(
    // drawable.getIntrinsicWidth(),
    // drawable.getIntrinsicHeight(),
    // drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
    // : Bitmap.Config.RGB_565);
    // Canvas canvas = new Canvas(bitmap);
    // drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
    // drawable.getIntrinsicHeight());
    // drawable.draw(canvas);
    // return bitmap;
    // }
    //
    // private static Pattern PHOTO_PAGE_LINK =
    // Pattern.compile("http://fanfou.com(/photo/[-a-zA-Z0-9+&@#%?=~_|!:,.;]*[-a-zA-Z0-9+&@#%=~_|])");
    // private static Pattern PHOTO_SRC_LINK =
    // Pattern.compile("src=\"(http:\\/\\/photo\\.fanfou\\.com\\/.*?)\"");
    // /**
    // * 获得消息中的照片页面链接
    // * @param text 消息文本
    // * @param size 照片尺寸
    // * @return 照片页面的链接，若不存在，则返回null
    // */
    // // public static String getPhotoPageLink(String text, String size){
    // // Matcher m = PHOTO_PAGE_LINK.matcher(text);
    // // if(m.find()){
    // // String THUMBNAIL=TwitterApplication.mContext
    // // .getString(R.string.pref_photo_preview_type_thumbnail);
    // // String MIDDLE=TwitterApplication.mContext
    // // .getString(R.string.pref_photo_preview_type_middle);
    // // String ORIGINAL=TwitterApplication.mContext
    // // .getString(R.string.pref_photo_preview_type_original);
    // // if (size.equals(THUMBNAIL) || size.equals(MIDDLE)){
    // // return "http://m.fanfou.com" + m.group(1);
    // // }else if (size.endsWith(ORIGINAL)){
    // // return m.group(0);
    // // }else{
    // // return null;
    // // }
    // // }else{
    // // return null;
    // // }
    // // }
    //
    // /**
    // * 获得照片页面中的照片链接
    // * @param pageHtml 照片页面文本
    // * @return 照片链接，若不存在，则返回null
    // */
    // public static String getPhotoURL(String pageHtml){
    // Matcher m = PHOTO_SRC_LINK.matcher(pageHtml);
    // if(m.find()){
    // return m.group(1);
    // }else{
    // return null;
    // }
    // }

    private static String formatTime(int hour, int minute) {
        String time = "";
        if (hour < 10) {
            time += "0" + hour + ":";
        } else {
            time += hour + ":";
        }

        if (minute < 10) {
            time += "0" + minute;
        } else {
            time += minute;
        }
        // System.out.println("format(hour, minute)=" + time);
        return time;
    }

    /**
     * 得到一天的开始
     *
     * @param date
     * @return
     */
    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public enum AddDateType {
        YEAR, MONTH, DAY, HH, MM, SS
    }
}
