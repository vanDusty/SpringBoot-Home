package cn.van.springboot.demo.date;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: LocalDateTimeTest
 *
 * @author: Van
 * Date:     2019-09-13 15:55
 * Description: JDK 1.8：日期和时间
 * Version： V1.0
 */
public class LocalDateTimeTest {


    /**
     * jdk 1.8 之前的日期时间
     */
    @Test
    public void dateFormatTest() {
        Date today = new Date();
        System.out.println(today);

        // 转为字符串
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String todayStr = sdf.format(today);
        System.out.println(todayStr);

        // 字符串转时间
        String anotherDayStr = "2019-1-1 12:00:00";
        try {
            Date anotherDay = sdf.parse(anotherDayStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印两种格式日期
     */
    @Test
    public void printNow() {

        LocalDateTime localDateNow = LocalDateTime.now();
        System.out.println("localDateNow:" + localDateNow);

        Date dateNow = new Date();
        System.out.println("dateNow:" + dateNow);

        // Date 格式化后
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(dateNow);
        System.out.println("dateNowStr:" + dateNowStr);
    }

    /**
     * LocalDate:只会获取日期
     */
    @Test
    public void localDate() {
        //获取当前年月日
        LocalDate today = LocalDate.now();
        System.out.println("当前年月日：" + today);

        //构造指定的年月日
        LocalDate anotherDay = LocalDate.of(2008, 8, 8);
        System.out.println("指定年月日：" + anotherDay);

        // 获取年的两种方式
        int thisYear = today.getYear();
        int thisYearAnother = today.get(ChronoField.YEAR);
        System.out.println("今年是" + thisYear + "年");
        System.out.println("今年是" + thisYearAnother + "年");

        // 获取月
        Month thisMonth = today.getMonth();
        System.out.println(thisMonth.toString());
        // 这是今年的第几个月
        int monthOfYear = today.get(ChronoField.MONTH_OF_YEAR);
        System.out.println("这个月是今年的第" + monthOfYear + "个月");

        // 获取日的两种方式
        int thisDay = today.getDayOfMonth();
        int thisDayAnother = today.get(ChronoField.DAY_OF_MONTH);
        System.out.println("今天是这个月的第" + thisDay + "天");
        System.out.println("今天是这个月的第" + thisDayAnother + "天");

        // 获取星期
        DayOfWeek thisDayOfWeek = today.getDayOfWeek();
        System.out.println(thisDayOfWeek.toString());
        // 今天是这周的第几天
        int dayOfWeek = today.get(ChronoField.DAY_OF_WEEK);
        System.out.println("今天是这周的第" + dayOfWeek + "天");
    }

    /**
     * LocalTime:只会获取时间
     */
    @Test
    public void localTime() {
        // 获取当前时间
        LocalTime nowTime = LocalTime.now();
        System.out.println("当前时间：" + nowTime);

        // 指定时间(最多可到纳秒)
        LocalTime anotherTime = LocalTime.of(20, 8, 8);
        System.out.println("指定时间：" + anotherTime);

        //获取小时的两种方式
        int hour = nowTime.getHour();
        int thisHour = nowTime.get(ChronoField.HOUR_OF_DAY);
        System.out.println("当前时：" + hour);
        System.out.println("当前时：" + thisHour);


        //获取分的两种方式
        int minute = nowTime.getMinute();
        int thisMinute = nowTime.get(ChronoField.MINUTE_OF_HOUR);
        System.out.println("当前分：" + minute);
        System.out.println("当前分：" + thisMinute);

        //获取秒的两种方式
        int second = nowTime.getSecond();
        int thisSecond = nowTime.get(ChronoField.SECOND_OF_MINUTE);
        System.out.println("当前秒：" + second);
        System.out.println("当前秒：" + thisSecond);
    }

    /**
     * 日期和时间都获取，相当于 LocalDate + LocalTime
     */
    @Test
    public void localDateTime() {

        // 当前日期和时间
        LocalDateTime today = LocalDateTime.now();
        System.out.println("现在是：" + today);


        // 获取LocalDate
        LocalDate todayDate = today.toLocalDate();
        System.out.println("今天日期是：" + todayDate);

        // 获取LocalTime
        LocalTime todayTime = today.toLocalTime();
        System.out.println("现在时间是：" + todayTime);

        // 指定日期和时间
        LocalDateTime anotherDay = LocalDateTime.of(2008, Month.AUGUST, 8, 8, 8, 8);
        System.out.println("生成的指定时间是：" + anotherDay);

        // 指定日期时间生成 LocalDateTime
        LocalDate anotherDate = anotherDay.toLocalDate();
        LocalTime anotherTime = anotherDay.toLocalTime();
        LocalDateTime createDate = LocalDateTime.of(anotherDate, anotherTime);
        System.out.println("生成的指定时间是：" + createDate);

        // 使用当前日期，指定时间生成的 LocalDateTime
        LocalDateTime likeTime = anotherTime.atDate(LocalDate.now());
        System.out.println("当前日期指定时间生成的 LocalDateTime:" + likeTime);

        // 使用当前时间， 指定日期生成的 LocalDateTime
        LocalDateTime likeDate = anotherDate.atTime(LocalTime.now());
        System.out.println("当前时间指定日期生成的 LocalDateTime：" + likeDate);

    }


    /**
     * 获取秒数，用于表示一个时间戳（精确到纳秒）
     */
    @Test
    public void getSeconds() {
        // 创建Instant对象
        Instant instant = Instant.now();
        // 获取秒数
        long currentSecond = instant.getEpochSecond();
        System.out.println("当前秒数：" + currentSecond);

        // 毫秒数
        long currentMilli = instant.toEpochMilli();
        System.out.println("当前毫秒数：" + currentMilli);

        // 获取毫秒数
        long timestamp = System.currentTimeMillis();
        System.out.println(timestamp);

    }

    /**
     * 计算Duration 表示一个时间段
     */
    @Test
    public void testssss() {
        // Duration.between()方法创建 Duration 对象
        LocalDateTime from = LocalDateTime.of(2019, Month.JANUARY, 1, 00, 0, 0);
        LocalDateTime to = LocalDateTime.of(2019, Month.SEPTEMBER, 12, 14, 28, 0);
        // 表示从 from 到 to 这段时间
        Duration duration = Duration.between(from, to);
        // 这段时间的总天数
        long days = duration.toDays();
        // 这段时间的小时数
        long hours = duration.toHours();
        // 这段时间的分钟数
        long minutes = duration.toMinutes();
        // 这段时间的秒数
        long seconds = duration.getSeconds();
        // 这段时间的毫秒数
        long milliSeconds = duration.toMillis();
        // 这段时间的纳秒数
        long nanoSeconds = duration.toNanos();

        System.out.println(days + "" + hours + "" + minutes + "" + seconds + "" + milliSeconds + "" +
                "" + nanoSeconds);
    }

    /**
     * 时间比较
     */
    @Test
    public void compareDate() {
        LocalDate dateTime_1 = LocalDate.of(2008, 8, 8);
        LocalDate dateTime_2 = LocalDate.of(2018, 8, 8);

        // 晚于
        boolean isAfter = dateTime_1.isAfter(dateTime_2);
        System.out.println(isAfter);

        // 早于
        boolean isBefore = dateTime_1.isBefore(dateTime_2);
        System.out.println(isBefore);
    }

    /**
     * 增加、减少年数、月数、天数
     */
    @Test
    public void addOr() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime nextYear = today.plusYears(1);
        LocalDateTime nextYearAnother = today.plus(1, ChronoUnit.YEARS);
        System.out.println("下一年的今天是：" + nextYear);
        System.out.println("下一年的今天是：" + nextYearAnother);

        //减少一个月
        LocalDateTime lastMonth = today.minusMonths(1);
        LocalDateTime lastMonthAnother = today.minus(1, ChronoUnit.MONTHS);
        System.out.println("一个月前是：" + lastMonth);
        System.out.println("一个月前是：" + lastMonthAnother);
    }

    /**
     * 修改时间
     */
    @Test
    public void editDate() {
        LocalDateTime today = LocalDateTime.now();
        //修改年为2020年
        LocalDateTime thisDay = today.withYear(2020);
        LocalDateTime thisDayAnother = today.with(ChronoField.YEAR, 2020);
        System.out.println("修改年后的时间为：" + thisDay);
        System.out.println("修改年后的时间为：" + thisDayAnother);
    }

    /**
     * 获取今年的第一天：TemporalAdjusters
     */
    @Test
    public void calculationDate() {
        LocalDate today = LocalDate.now();
        // 获取今年的第一天
        LocalDate date = today.with(firstDayOfYear());
        System.out.println("今年的第一天是：" + date);
    }

    /**
     * 格式化时间
     */
    @Test
    public void fromat() {

        LocalDate today = LocalDate.now();
        // 两种默认格式化时间方式
        String todayStr_1 = today.format(DateTimeFormatter.BASIC_ISO_DATE);
        String todayStr_2 = today.format(DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.println("格式化时间：" + todayStr_1);
        System.out.println("格式化时间：" + todayStr_2);
        //自定义格式化
        DateTimeFormatter dateTimeFormatter =   DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String todayStr_3 = today.format(dateTimeFormatter);
        System.out.println("自定义格式化时间：" + todayStr_3);

        // 时间解析
        LocalDate date_1 = LocalDate.parse("20080808", DateTimeFormatter.BASIC_ISO_DATE);
        LocalDate date_2 = LocalDate.parse("2008-08-08", DateTimeFormatter.ISO_LOCAL_DATE);

        System.out.println(date_1);
        System.out.println(date_2);
    }
}
