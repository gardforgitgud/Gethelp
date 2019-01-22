package gr.mmv.gethelp;

/**
 * Created by iceberg on 3/6/2018.
 */

public class Officer {
    private int id;
    private String email, name, national_id, role, tel;

    public Officer(int id, String email, String name, String national_id, String role, String tel){
        this.id = id;
        this.email = email;
        this.name = name;
        this.national_id = national_id;
        this.role = role;
        this.tel = tel;
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
    public String getNational_id() {

        return national_id;
    }
    public String getRole() {

        return role;
    }
    public String getTel() {

        return tel;
    }






}
