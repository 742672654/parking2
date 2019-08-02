package com.example.parking.ui.dataChoice;

public class SpecificDateBean {

    private int year;
    private int monthOfYear;
    private int dayOfMonth;

    public SpecificDateBean(int year, int monthOfYear, int dayOfMonth) {
        this.year=year;
        this.monthOfYear=monthOfYear;
        this.dayOfMonth=dayOfMonth;
    }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public int getMonthOfYear() { return monthOfYear; }
    public void setMonthOfYear(int monthOfYear) { this.monthOfYear = monthOfYear; }
    public int getDayOfMonth() { return dayOfMonth; }
    public void setDayOfMonth(int dayOfMonth) { this.dayOfMonth = dayOfMonth; }

    @Override
    public String toString() {
        return "SpecificDate{" +
                "year='" + year + '\'' +
                ", monthOfYear='" + monthOfYear + '\'' +
                ", dayOfMonth='" + dayOfMonth + '\'' +
                '}';
    }
}
