package validation;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Created by hodor on 06-01-2019.
 */
public class ValidatorBuilder {

    private Validator validator;

    public <T> ValidatorBuilder createFor(T t, Predicate<T> constraint, String errorMessage) {
        Objects.requireNonNull(constraint);
        Objects.requireNonNull(errorMessage);
        this.validator = new FirstOrderValidator<T>(t, constraint, errorMessage);
        return this;
    }

    public <T> ValidatorBuilder and(T t, Predicate<T> constraint, String errorMessage) {
        Objects.requireNonNull(constraint);
        Objects.requireNonNull(errorMessage);
        FirstOrderValidator<T> firstOrderValidator = new FirstOrderValidator<T>(t, constraint, errorMessage);
        this.validator = new SecondOrderAndValidator(this.validator, firstOrderValidator);
        return this;
    }

    public <T> ValidatorBuilder or(T t, Predicate<T> constraint, String errorMessage) {
        Objects.requireNonNull(constraint);
        Objects.requireNonNull(errorMessage);
        FirstOrderValidator<T> firstOrderValidator = new FirstOrderValidator<T>(t, constraint, errorMessage);
        this.validator = new SecondOrderOrValidator(this.validator, firstOrderValidator);
        return this;
    }

    public Validator build() {
        return validator;
    }

    private class FirstOrderValidator<T> implements Validator {

        private T t;

        private String errorMessage = "";

        private Predicate<T> predicate;

        public FirstOrderValidator(T t, Predicate<T> predicate, String msg) {
            Objects.requireNonNull(predicate);
            this.t = t;
            this.predicate = predicate;
            this.errorMessage = msg;
        }

        @Override
        public ConstraintViolations validate() {
            boolean testResult = this.predicate.negate().test(t);
            return new ConstraintViolations(testResult, testResult ? errorMessage : "");
        }
    }

    private class SecondOrderAndValidator implements Validator {

        private Validator first;

        private Validator second;

        private SecondOrderAndValidator(Validator first, Validator second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public ConstraintViolations validate() {

            ConstraintViolations constraintViolations = new ConstraintViolations(false, "");
            ConstraintViolations firstViolations = this.first.validate();
            if (firstViolations.hasVoilations()) {
                constraintViolations = firstViolations;
            } else {
                constraintViolations = second.validate();
            }
            return constraintViolations;
        }

    }

    private class SecondOrderOrValidator implements Validator {

        private Validator first;

        private Validator second;

        private SecondOrderOrValidator(Validator first, Validator second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public ConstraintViolations validate() {
            ConstraintViolations constraintViolations = new ConstraintViolations(false, "");

            ConstraintViolations firstViolations = this.first.validate();

            if (firstViolations.hasVoilations()) {
                constraintViolations = second.validate();
            }
            return constraintViolations;
        }

    }


}
