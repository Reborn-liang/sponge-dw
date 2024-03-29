package cn.nearf.ggz.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {
	public static final long OneDayMilSeconds = 24 * 3600 * 1000l;
	public static final long OneHourMilSeconds = 3600 * 1000l;
	public static final long HalfHourMilSeconds = 1800 * 1000l;

	private static SimpleDateFormat msSdf() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	}
	private static SimpleDateFormat fullSdf() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	private static final SimpleDateFormat sdf() {
		return new SimpleDateFormat("yyyy-MM-dd");
	}
	
	
	public static Date getDate(int year, int month, int day) {
		try {
			String dateStr = String.format("%04d-%02d-%02d", year, month, day);
			return sdf().parse(dateStr);
		} catch (Exception e) {
		}
		return null;
	}
	
	public static Date getSettleDate(){
		Calendar cal = Calendar.getInstance();
		int h = cal.get(Calendar.HOUR_OF_DAY);
		if(h>=0 && h<6){
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		return cal.getTime();
	}
	
	public static String getMSDate(long datems) {
		Date date = new Date(datems);
		
		return SimpleDateFormat.getDateInstance().format(date);
	}
	
	public static String getStringFromDate(Date date) {
		if (date == null) {
			return "";
		}
		return fullSdf().format(date);
	}
	
	public static String getYMDHMSSFromDate(Date date) {
		if (date == null) {
			return "";
		}
		return msSdf().format(date);
	}
	
	public static String getYMDFromDate(Date date) {
		if (date == null) {
			return "";
		}
		return sdf().format(date);
	}
	
	
	public static String getCurrentDate() {
		Date date = new Date();
		return fullSdf().format(date);
	}
	
	public static int getCurrentHour() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
	
	public static Date getDateFromString(String dateString) {
		return getDateFromString(dateString, null);
	}
	
	public static Date getDateFromString(String dateString, Date defaultDate) {
		Date date = defaultDate;
		try {
			date = fullSdf().parse(dateString);
		} catch (Exception e) {
			try {
				date = sdf().parse(dateString);
			} catch (Exception ex) {
			}
		}
		return date;
	}
	
	public static Date getDateFromStringWithFormat(String dateString, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(dateString);
		} catch (Exception e) {
		}
		return null;
	}
	
	
	public static int getWeekDay(Date date) {
		try {
			Calendar cal = Calendar.getInstance();
	        cal.setTime(date);
	        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
	        if (w <= 0)
	            w = 7;
	        return w;
		} catch (Exception e) {
		}
		return 0;
	}
	
	public static String getTimeFromMS(long ms) {
		long hour = ms / 3600000l;
		long min = (ms % 3600000l) / 60000l;
		long sec = (ms % 60000l) / 1000l;
		long lms = (ms % 1000l);
		
		return String.format("%d:%02d:%02d", hour, min, sec);
	}
	
	
	///////////////
	///////////////
	///////////////
	
	
	
	
	public static String getMonthDay(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);
	} 
	
	public static String getDateStr(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	} 
	
	public static String getMonthStartDay(Date date) {
		Calendar a = Calendar.getInstance();
		a.setTime(date);
		a.set(Calendar.DATE, 1);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(a.getTime());
	} 
	
	public static String getMonthEndDay(Date date) {
		Calendar a = Calendar.getInstance();
		a.setTime(date);
		a.set(Calendar.DATE, a.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(a.getTime());
	} 
	
	public static Date getMonthStart(Date date) {
		Calendar a = Calendar.getInstance();
		a.setTime(date);
		a.set(Calendar.DATE, 1);
		
		return a.getTime();
	} 
	
	public static Date getMonthEnd(Date date) {
		Calendar a = Calendar.getInstance();
		a.setTime(date);
		a.set(Calendar.DATE, a.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		return a.getTime();
	} 
	
	public static Date getDateByDay(Date date,int day) {
		Date d = new Date(date.getTime() + day*1000*60*60*24l -1);
		return d;
	} 
	
	public static int getIntervalDays(Date startday,Date endday){  
		Calendar a = Calendar.getInstance();
		a.setTime(startday);
		Calendar b = Calendar.getInstance();
		b.setTime(endday);
		return getDaysBetween(a,b);
	}  
	
    private static int getDaysBetween (Calendar d1, Calendar d2) {  
        if (d1.after(d2)) {  // swap dates so that d1 is start and d2 is end  
            java.util.Calendar swap = d1;  
            d1 = d2;  
            d2 = swap;  
        }  
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);  
        int y2 = d2.get(Calendar.YEAR);  
        if (d1.get(Calendar.YEAR) != y2) {  
            d1 = (Calendar) d1.clone();  
            do {  
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);//得到当年的实际天数  
                d1.add(Calendar.YEAR, 1);  
            } while (d1.get(Calendar.YEAR) != y2);  
        }  
        return days + 1;  
    }   
    
    //下个月第一天
	public static Date getNextMonthFristDay(Date d, int m) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.MONTH, c.get(Calendar.MONTH) + m);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}
    
    
    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 返回日期的月份，1-12,即yyyy-MM-dd中的MM
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 返回日期的年,即yyyy-MM-dd中的yyyy
     *
     * @param date
     *            Date
     * @return int
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static int getMaxDaysOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return calendar.get(Calendar.DAY_OF_MONTH);
    }
    
    public static int getMaxDaysOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
    
    public static int getMaxDaysOfYear(int year) {
    	Date yearEnd = getDate(year, 12, 31);
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(yearEnd);
        return calendar.get(Calendar.DAY_OF_YEAR);
	}
    
    public static int getDayOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }
    
    public static Date addDay(Date date, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, amount);
        return calendar.getTime();
    }
    
    public static int getDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    
    public static int getDayOfMonth(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    //两个日期之间月份间隔
	public static int getIntervalMonths(Date startDate, Date endDate) {
        if (startDate.after(endDate)) {  // swap dates so that d1 is start and d2 is end  
            Date swap = startDate;  
            startDate = endDate;  
            endDate = swap;  
        }  
		
		int result = 0;
		int startYear = getYear(startDate);
		int startMonth = getMonth(startDate);
		int startDay = getDay(startDate);
		int endYear = getYear(endDate);
		int endMonth = getMonth(endDate);
		int endDay = getDay(endDate);
		if (startDay > endDay) { // 1月17 大于 2月28
			if (endDay == getMaxDaysOfMonth(getYear(new Date()), 2)) { // 也满足一月
				result = (endYear - startYear) * 12 + endMonth - startMonth;
			} else {
				result = (endYear - startYear) * 12 + endMonth - startMonth - 1;
			}
		} else {
			result = (endYear - startYear) * 12 + endMonth - startMonth;
		}

		return result + 1;
	}  
	
	public static Date getLastYear(Date date) {
		Calendar a = Calendar.getInstance();
		a.setTime(date);
		a.set(Calendar.YEAR, date.getYear() - 1 + 1900);
		
		return a.getTime();
	} 
	
	public static Date getLastMonth(Date date) {
		Calendar a = Calendar.getInstance();
		a.setTime(date);
		a.set(Calendar.MONTH, date.getMonth() -1);
		
		return a.getTime();
	}
	
	public static String getDateStringWithFormat(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	
	
	public static List<Date> getMonthBetweenTwoDate(Date beginDate, Date endDate) {  
        List<Date> lDate = new ArrayList<Date>();  
        Calendar cal = Calendar.getInstance();  
        lDate.add(beginDate);
        // 使用给定的 Date 设置此 Calendar 的时间  
        cal.setTime(beginDate);  
        boolean bContinue = true;  
        while (bContinue) {  
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量  
            cal.add(Calendar.MONTH, 1);  
            // 测试此日期是否在指定日期之后  
            if (endDate.after(cal.getTime())) {  
            	Date d =  cal.getTime();
            	lDate.add(d);  
            } else {  
                break;  
            }  
        }  
        return lDate;  
    }
	
	public static int getYearMonthDaySeq(Date date) {
		return ObjectUtils.getIntValue(getDateStringWithFormat(date, "yyyyMMdd"));
	}
	
	public static void main(String[] args) throws ParseException {
		String paramStr = "2017-08-01 15:34:38";
		Date date = DateUtils.getDateFromStringWithFormat(paramStr, "yyyy-MM-dd HH:mm:ss");
		System.err.println(date);
		
		System.out.println(getMaxDaysOfYear(2016));
				
//		String period = ",1";
//		if (period != null && period.trim().length() > 0) {
//			period = period.trim();
//			if (period.indexOf(",") != 0) {
//				period = "," + period;
//			}
//			if (period.lastIndexOf(",") != period.length() - 1) {
//				period = period + ",";
//			}
//		}
//		
//		System.out.println(period);
//		
//		Date date = new Date();
//		date.setTime(date.getTime() - 3600 * 1000 * 24);
//		System.out.println(date);
//		System.out.println(getWeekDay(date));
//		
//		
//		Calendar c = Calendar.getInstance();
//		Date d = new SimpleDateFormat("yyyyMMdd").parse("20161211");
//		c.setTime(d);
//		System.out.println(c.getTime());
//		c.set(Calendar.MONTH, c.get(Calendar.MONTH)+1);
//		c.set(Calendar.DAY_OF_MONTH, 1);
//		System.out.println("下个月的第一天: " + c.getTime());
//		c.set(Calendar.MONTH, c.get(Calendar.MONTH)+1);
//		c.set(Calendar.DAY_OF_MONTH, 0);
//		System.out.println("下个月的最后一天: " + c.getTime());
	}
	
	
}
