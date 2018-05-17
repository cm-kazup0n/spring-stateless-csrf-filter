# spring-stateless-csrf-filter

## Usage

TBD

## Run Examples

Here's two examples for thymeleaf 2.x and 3.x.

1. `git clone git@github.com:cm-kazup0n/spring-statelss-csrf-filter.git`
1. `cd spring-statelss-csrf-filter`
1. `./gradlew :example-thymeleaf2:run` or `./gradlew :example-thymeleaf3:run`
1. Open [http://localhost:8080/users](http://localhost:8080/users) with browsers

then

- You can create User with the form.
- You can't create User if you change value of the hidden input before submit form. (e.g. with chrome's devtool)

