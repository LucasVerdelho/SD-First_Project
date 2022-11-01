package ServerSide;

import java.io.Serializable;

public abstract class User implements Serializable {
    
    
    private String userName;
    private String password;


    public User(String userName, String password) {
        
        this.userName = userName;
        this.password = password;
    }


    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //Considerando que existem apenas 2 tipos de utilizador
    public int getType(){

        int r=1;
        if(this instanceof Admin){
            r=0;
        }

        return r;


    }


}
