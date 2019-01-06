import validation.*;

import java.util.function.Predicate;

/**
 * Created by hodor on 06-01-2019.
 */
public class Runner {

    public static void main(String[] args) {

        Predicate<User> adultUserPolicy=user -> {
            boolean b = user.age >18;
            System.out.println("Adult user policy result " + b);
            return b;
        };
        Predicate<User> firstNameRequiredPolicy= user -> {
            boolean b = user.fName.length() > 0;
            System.out.println("First Name Required policy "+b);
            return b;
        };
        Predicate<User> lastNameRequiredPolicy= user -> {
            boolean b = user.lName.length() > 0;
            System.out.println("Last Name Required policy "+b);
            return b;
        };

        Predicate<Identification> identityRequiredPolicy=identification -> identification.identityNumber!=-0;
        Predicate<Identification> identityPassPost=identification -> "passport".equalsIgnoreCase(identification.identityType);


        User user = new User("abc", "singh", 2);
        Identification identification = new Identification(12345678,"Pasport");
        Validator<User> fistNameLastNameValidator = new Validator<User>(user,firstNameRequiredPolicy.and(lastNameRequiredPolicy), "First Name, Last Name Cannot be blank");
        Validator<User> adultUserRequired = new Validator<User>(user,adultUserPolicy, "Adult User is needed");
        Validator and = fistNameLastNameValidator.or(adultUserRequired);

//        System.out.println("Validator has error " + (and.hasError() ? and.getErrorMessage(): "No Error in User object"));

        ValidatorBuilder builder = new ValidatorBuilder();
        ConstraintViolations violations = builder.createFor(user, firstNameRequiredPolicy.and(lastNameRequiredPolicy), "FirstName and Last Name should not be null")
                .and(user, adultUserPolicy, "Adult User is needed")
                .or(identification, identityPassPost, "Identity should be passport")
                .build().validate();

        System.out.println("has error " +violations.hasVoilations());
        System.out.println("has error " +violations.getViolationsMessages());


    }
}
