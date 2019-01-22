package gr.mmv.gethelp;

/**
 * Created by iceberg on 5/2/2018.
 */

public class Emergency {

    private int id;
    private String status, date_time, note, lat, lon, emer_img, user_report_name, user_report_tel;

    public Emergency(int id, String status, String date_time, String note, String lat, String lon, String emer_img, String user_report_name, String user_report_tel) {
        this.id = id;
        this.status = status;
        this.date_time = date_time;
        this.note = note;
        this.lat = lat;
        this.lon = lon;
        this.emer_img = emer_img;
        this.user_report_name = user_report_name;
        this.user_report_tel = user_report_tel;
    }

    public int getId(){
        return id;
    }
    public String getStatus() {
        return status;
    }
    public String getDate_time() {
        return date_time;
    }
    public String getNote() {
        return note;
    }
    public String getLat() {
        return lat;
    }
    public String getLon(){
        return lon;
    }
    public String getEmer_img() {
        return emer_img;
    }
    public String getUser_report_name(){
        return user_report_name;
    }
    public String getUser_report_tel(){
        return user_report_tel;
    }
}
