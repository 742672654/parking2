package com.example.parking.bean.http;

import java.io.Serializable;
import java.util.List;

public class EscapeListToMinePageBean {

    private Integer code;//200,"message":"SUCCESS","data":
    private String message;//SUCCESS
    private EscapeListToMinePageData data;

    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public EscapeListToMinePageData getData() { return data; }
    public void setData(EscapeListToMinePageData data) { this.data = data; }

    @Override
    public String toString() {
        return "EscapeListToMinePageBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class EscapeListToMinePageData implements Serializable {

        private Integer pageSize;// 10,
        private Integer size;// 4,
        private Integer orderBy;// null,
        private Integer startRow;// 1,
        private Integer endRow;// 4,
        private Integer total;// 4,
        private Integer pages;// 1,
        private List<EscapeListToMinePageBeanList> list;

        public Integer getPageSize() { return pageSize; }
        public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
        public Integer getSize() { return size; }
        public void setSize(Integer size) { this.size = size; }
        public Integer getOrderBy() { return orderBy; }
        public void setOrderBy(Integer orderBy) { this.orderBy = orderBy; }
        public Integer getStartRow() { return startRow; }
        public void setStartRow(Integer startRow) { this.startRow = startRow; }
        public Integer getEndRow() { return endRow; }
        public void setEndRow(Integer endRow) { this.endRow = endRow; }
        public Integer getTotal() { return total; }
        public void setTotal(Integer total) { this.total = total; }
        public Integer getPages() { return pages; }
        public void setPages(Integer pages) { this.pages = pages; }
        public List<EscapeListToMinePageBeanList> getList() { return list; }
        public void setList(List<EscapeListToMinePageBeanList> list) { this.list = list; }

        @Override
        public String toString() {
            return "EscapeListToMinePageData{" +
                    "pageSize=" + pageSize +
                    ", size=" + size +
                    ", orderBy=" + orderBy +
                    ", startRow=" + startRow +
                    ", endRow=" + endRow +
                    ", total=" + total +
                    ", pages=" + pages +
                    ", list=" + list +
                    '}';
        }
    }

    public static class EscapeListToMinePageBeanList implements Serializable {
        public String CarNum;// "苏A",
        public Double EscapePrice;// 2.0,
        public String CreateTime;// "2019-09-02T08:06:09.000+0000",
        public String SubName;// "农行车位003",
        public Integer isdeleted;// 2,
        public String ParkDate;// "2019-09-02T08:03:52.000+0000"
        @Override
        public String toString() {
            return "EscapeListToMinePageBeanList{" +
                    "CarNum='" + CarNum + '\'' +
                    ", EscapePrice=" + EscapePrice +
                    ", CreateTime='" + CreateTime + '\'' +
                    ", SubName='" + SubName + '\'' +
                    ", isdeleted=" + isdeleted +
                    ", ParkDate='" + ParkDate + '\'' +
                    '}';
        }
    }
}

