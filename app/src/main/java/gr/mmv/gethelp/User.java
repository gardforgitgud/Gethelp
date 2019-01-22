package gr.mmv.gethelp;

/**
 * Created by iceberg on 3/6/2018.
 */

public class User {

    private int id;
    private String email, name, tel, address, national_id;

    public User(int id, String email, String name, String tel, String address, String national_id) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.tel = tel;
        this.address = address;
        this.national_id = national_id;
    }

    public int getId() {

        return id;
    }
    public String getEmail() {

        return email;
    }
    public String getName() {

        return name;
    }
    public String getTel() {

        return tel;
    }
    public String getAddress() {

        return address;
    }
    public String getNational_id() {

        return national_id;
    }
}
