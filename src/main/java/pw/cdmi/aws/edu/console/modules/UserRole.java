package pw.cdmi.aws.edu.console.modules;

import java.util.Arrays;
import java.util.Optional;

public enum UserRole {
    Admin,
    SchoolManager,
    Teacher,
    Student,
    Guarder;

    public static UserRole fromName(String name){
        Optional<UserRole> optional = Arrays.stream(UserRole.values()).filter(t->t.name().equalsIgnoreCase(name)).findFirst();
        if(optional.isPresent()){
            return optional.get();
        }else{
            return null;
        }
    }
}
