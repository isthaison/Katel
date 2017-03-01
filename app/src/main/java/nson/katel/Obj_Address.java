package nson.katel;

/**
 * Created by Nson on 2/28/2017.
 */

public class Obj_Address {
   public String duong;
   public String thanhpho;
   public String tinh;
   public String nuoc;
   public String ma;

    public Obj_Address() {
    }

    public Obj_Address(String duong, String ma, String nuoc, String tinh, String thanhpho) {
        this.duong = duong;
        this.ma = ma;
        this.nuoc = nuoc;
        this.tinh = tinh;
        this.thanhpho = thanhpho;
    }
}
