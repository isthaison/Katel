package nson.katel;

/**
 * Created by Nson on 2/28/2017.
 */

public class Obj_Address {
   public String street;//đường
   public String city;//thành phố
   public String state;//tỉnh
   public String country; //quốc gia
   public String postalCode;// Mã zip

    public Obj_Address() {
    }

    public Obj_Address(String street, String postalCode, String country, String state, String thanhpho) {
        this.street = street;
        this.postalCode = postalCode;
        this.country = country;
        this.state = state;
        this.city = thanhpho;
    }
}
