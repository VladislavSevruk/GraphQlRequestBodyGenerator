[![Build Status](https://travis-ci.org/VladislavSevruk/GraphQlRequestBodyGenerator.svg?branch=develop)](https://travis-ci.com/VladislavSevruk/GraphQlRequestBodyGenerator)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=VladislavSevruk_GraphQlRequestBodyGenerator&metric=alert_status)](https://sonarcloud.io/dashboard?id=VladislavSevruk_GraphQlRequestBodyGenerator)
[![Code Coverage](https://sonarcloud.io/api/project_badges/measure?project=VladislavSevruk_GraphQlRequestBodyGenerator&metric=coverage)](https://sonarcloud.io/component_measures?id=VladislavSevruk_GraphQlRequestBodyGenerator&metric=coverage)

# GraphQL Request Body Generator
This utility library helps to generate request body for [GraphQL](http://spec.graphql.org/June2018/) queries using POJOs.

## Table of contents
* [Getting started](#getting-started)
  * [Maven](#maven)
  * [Gradle](#gradle)
* [Usage](#usage)
  * [Field marking strategy](#field-marking-strategy)
  * [Prepare POJO model](#prepare-pojo-model)
    * [Selection set](#selection-set)
      * [GqlField](#gqlfield)
      * [GqlDelegate](#gqldelegate)
      * [GqlIgnore](#gqlignore)
    * [Input object value](#input-object-value)
      * [GqlField](#gqlfield-1)
      * [GqlInput](#gqlinput)
      * [GqlDelegate](#gqldelegate-1)
      * [GqlIgnore](#gqlignore-1)
  * [Generate request body](#generate-request-body)
    * [Operation selection set](#operation-selection-set)
    * [Arguments](#arguments)
      * [Input argument](#input-argument)
* [License](#license)

## Getting started
To add library to your project perform next steps:

### Maven
Add the following dependency to your pom.xml:
```xml
<dependency>
      <groupId>com.github.vladislavsevruk</groupId>
      <artifactId>graphql-request-body-generator</artifactId>
      <version>1.0.0</version>
</dependency>
```
### Gradle
Add the following dependency to your build.gradle:
```groovy
implementation 'com.github.vladislavsevruk:graphql-request-body-generator:1.0.0'
```

## Usage
First of all we need to choose field marking strategy that will be used for GraphQL operation generation.

### Field marking strategy
There are two predefined field marking strategy that define the way how builder determine if field should be used for 
GraphQL operation generation:
  * Use all fields except ones with [GqlIgnore](src/main/java/com/github/vladislavsevruk/generator/annotation/GqlIgnore.java)
    annotation \[default]
  * Use only fields with one of [GqlField](src/main/java/com/github/vladislavsevruk/generator/annotation/GqlField.java) 
    or [GqlDelegate](src/main/java/com/github/vladislavsevruk/generator/annotation/GqlDelegate.java) annotations

Current strategy for both [operation selection set](http://spec.graphql.org/June2018/#sec-Selection-Sets) and mutation
[input object values](http://spec.graphql.org/June2018/#sec-Input-Object-Values) can be set using 
[FieldMarkingStrategySourceManager](src/main/java/com/github/vladislavsevruk/generator/strategy/marker/FieldMarkingStrategySourceManager.java):
```kotlin
FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
// or
FieldMarkingStrategySourceManager.input().useOnlyMarkedFieldsStrategy();
```

However, you can set your own custom [FieldMarkingStrategy](src/main/java/com/github/vladislavsevruk/generator/strategy/marker/FieldMarkingStrategy.java):
```kotlin
FieldMarkingStrategySourceManager.selectionSet().useCustomStrategy(field -> true);
```

### Prepare POJO model
Then we need to prepare POJO models that will be used for GraphQL operation generation according to chosen field marking
strategy.

### Selection set
Selection set generation is based only on fields that are declared at class or it superclasses and doesn't involve 
declared methods.

#### GqlField
[GqlField](src/main/java/com/github/vladislavsevruk/generator/annotation/GqlField.java) annotation marks model fields 
that should be treated as [GraphQL field](http://spec.graphql.org/June2018/#sec-Language.Fields):
```java
public class User {
    @GqlField
    private Long id;
}
```

By default field name will be used for generation but you can override it using __name__ method:
```java
public class User {
    @GqlField(name = "wishListItemsUrls")
    private List<String> wishListItems;
}
```

Some fields may contain a [selection set](http://spec.graphql.org/June2018/#sec-Selection-Sets) so if you mark field 
using __withSelectionSet__ method it will be treated as field that have nested fields:
```java
public class User {
    @GqlField(withSelectionSet = true)
    private Contacts contacts;
    @GqlField(withSelectionSet = true)
    private List<Order> orders;
}

public class Contacts {
    @GqlField
    private String email;
    @GqlField
    private String phoneNumber;
}

public class Order {
    @GqlField
    private Long id;
    @GqlField
    private Boolean isDelivered;
}
```

Annotation also has method __nonNull__ that allow to mark field that was denoted as 
[non-null](http://spec.graphql.org/June2018/#sec-Type-System.Non-Null) at GraphQL schema:
```java
public class User {
    @GqlField(nonNull = true)
    private Long id;
}
```

If field requires [arguments](http://spec.graphql.org/June2018/#sec-Language.Arguments) for GraphQL operation they can 
be provided via __arguments__ function:
```java
public class User {
    @GqlField(arguments = { @GqlFieldArgument(name = "width", value = "100"),
                            @GqlFieldArgument(name = "height", value = "50") })
    private String profilePic;
}
```

Values of [GqlFieldArgument](src/main/java/com/github/vladislavsevruk/generator/annotation/GqlFieldArgument.java) 
annotation will be added to selection set "as is" so you may need to escape quotes for literal values:
```java
public class User {
    @GqlField(withSelectionSet = true,
              arguments = @GqlFieldArgument(name = "sortBy", value = "\"orderDate DESC\""))
    private List<Order> orders;
}

public class Order {
    @GqlField
    private Long id;
    @GqlField
    private Date orderDate;
}
```

Also [alias](http://spec.graphql.org/June2018/#sec-Field-Alias) can be specified for field using self-titled method:
```java
public class User {
    @GqlField(name = "profilePic",
              alias = "smallPic",
              arguments = { @GqlFieldArgument(name = "size", value = "64")})
    private String smallPic;
    @GqlField(name = "profilePic",
              alias = "bigPic",
              arguments = { @GqlFieldArgument(name = "size", value = "1024")})
    private String bigPic;
}
```

#### GqlDelegate
[GqlDelegate](src/main/java/com/github/vladislavsevruk/generator/annotation/GqlDelegate.java) is used for complex fields
to treat its inner fields like they are declared at same class where field itself declared: 
```java
public class User {
    @GqlDelegate
    private UserInfo userInfo;
} 

public class UserInfo {
    @GqlField
    private String firstName;
    @GqlField
    private String lastName;
}
```

Code above is equivalent to:
```java
public class User {
    @GqlField
    private String firstName;
    @GqlField
    private String lastName;
}
```

#### GqlIgnore
[GqlIgnore](src/main/java/com/github/vladislavsevruk/generator/annotation/GqlIgnore.java) is used with "all fields 
except ignored" [field marking strategy](#field-marking-strategy) for marking field that shouldn't be used for 
generation:
```java
public class UserInfo {
    private String firstName;
    @GqlIgnore
    private String fullName;
    private String lastName;
}
```

### Input object value
Input object value generation goes through fields that are declared at class or it superclasses and gets values from 
related setter methods (or field itself if no related setter found).

#### GqlField
[GqlField](src/main/java/com/github/vladislavsevruk/generator/annotation/GqlField.java) annotation is also used for 
[input object value](http://spec.graphql.org/June2018/#sec-Input-Object-Values) generation and marks 
[input values](http://spec.graphql.org/June2018/#sec-Input-Values) of any type:
```java
public class User {
    @GqlField
    private Long id;
    @GqlField
    private Contacts contacts;
}

public class Contacts {
    @GqlField
    private String email;
    @GqlField
    private String phoneNumber;
}
```

By default field name will be used for generation but you can override it using __name__ method:
```java
public class User {
    @GqlField(name = "wishListItemsUrls")
    private List<String> wishListItems;
}
```

#### GqlInput
[GqlInput](src/main/java/com/github/vladislavsevruk/generator/annotation/GqlInput.java) annotation is used with methods 
and helps to point to related GraphQL field if method name doesn't match field setter pattern:
```java
public class User {
    @GqlField
    private String name;
    @GqlField
    private String surname;

    @GqlInput(name = "name")
    public String getFirstName() {
        return name;
    }

    @GqlInput(name = "surname")
    public String getLastName() {
        return surname;
    }
}
```

If provided name doesn't match any of GraphQL fields at model result of method execution as new field:
```java
public class User {
    @GqlField
    private String firstName;
    @GqlField
    private String lastName;

    @GqlInput(name = "fullName")
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
```

#### GqlDelegate
[GqlDelegate](src/main/java/com/github/vladislavsevruk/generator/annotation/GqlDelegate.java) is used for complex values
to treat its inner fields like they are declared at same class where field itself declared: 
```java
public class User {
    @GqlDelegate
    private UserInfo userInfo;
} 

public class UserInfo {
    @GqlField
    private String firstName;
    @GqlField
    private String lastName;
}
```

Models above are equivalent to:
```java
public class User {
    @GqlField
    private String firstName;
    @GqlField
    private String lastName;
}
```

Like [GqlInput](#gqlinput) __GqlDelegate__ can be applied to methods without related GraphQL field. In this case return 
value of method will be treated as delegated field:
```java
public class User {
    @GqlDelegate
    public UserInfo getUserInfo() {
        // UserInfo generation
    }
} 

public class UserInfo {
    @GqlField
    private String firstName;
    @GqlField
    private String lastName;
}
```

Code above is equivalent to:
```java
public class User {
    @GqlField
    private String firstName;
    @GqlField
    private String lastName;
}
```


#### GqlIgnore
[GqlIgnore](src/main/java/com/github/vladislavsevruk/generator/annotation/GqlIgnore.java) is used with "all fields 
except ignored" [field marking strategy](#field-marking-strategy) for marking field that shouldn't be used for 
generation:
```java
public class UserInfo {
    private String firstName;
    @GqlIgnore
    private String fullName;
    private String lastName;
}
```

### Generate request body
Once POJO models are ready we can generate GraphQL operation using 
[GqlRequestBodyGenerator](src/main/java/com/github/vladislavsevruk/generator/GqlRequestBodyGenerator.java):
```kotlin
// query
String query = GqlRequestBodyGenerator.query("allUsers").selectionSet(User.class).generate();

// prepare input model
User newUser = new User();
newUser.setFirstName("John");
newUser.setLastName("Doe");

// mutation
GqlInputArgument input = GqlInputArgument.of(newUser);
String mutation = GqlRequestBodyGenerator.mutation("newUser").arguments(input).selectionSet(User.class).generate();
```

Generated operation is wrapped into json and can be passed as body to any API Client.

#### Operation selection set
By default, all marked fields will be used for selection set generation. However, you can generate selection set using 
one of [predefined fields picking strategies](src/main/java/com/github/vladislavsevruk/generator/strategy/picker/selection/SelectionSetGenerationStrategy.java):
- pick all marked fields
```kotlin
String query = GqlRequestBodyGenerator.query("allUsers")
        .selectionSet(User.class, SelectionSetGenerationStrategy.ALL_FIELDS).generate();
```

- pick only fields with __id__ name or fields that have nested field with __id__ name
```kotlin
String query = GqlRequestBodyGenerator.query("allUsers")
        .selectionSet(User.class, SelectionSetGenerationStrategy.ONLY_ID).generate();
```

- pick only fields that are marked as [non-null](http://spec.graphql.org/June2018/#sec-Type-System.Non-Null)
```kotlin
String query = GqlRequestBodyGenerator.query("allUsers")
        .selectionSet(User.class, SelectionSetGenerationStrategy.ONLY_NON_NULL).generate();
```

- pick all except fields with [selection set](http://spec.graphql.org/June2018/#sec-Selection-Sets) 
```kotlin
String query = GqlRequestBodyGenerator.query("allUsers")
        .selectionSet(User.class, SelectionSetGenerationStrategy.WITHOUT_SELECTION_SETS).generate();
```

Also you can provide your own custom fields picking strategy that implements 
[FieldsPickingStrategy](src/main/java/com/github/vladislavsevruk/generator/strategy/picker/selection/FieldsPickingStrategy.java)
functional interface:
```kotlin
String query = GqlRequestBodyGenerator.query("allUsers")
        .selectionSet(User.class, field -> field.getName().contains("Name")).generate();
```

If you want to use generic models it's recommended to provide them via 
[TypeProvider](https://github.com/VladislavSevruk/TypeResolver/blob/develop/src/main/java/com/github/vladislavsevruk/resolver/type/TypeProvider.java):
```kotlin
String query = GqlRequestBodyGenerator.query("allUsers")
        .selectionSet(new TypeProvider<User<UserInfo>>() {}).generate();
```

#### Arguments
Some operations may require [arguments](http://spec.graphql.org/June2018/#sec-Language.Arguments) (to pick specific item 
or filter items list, for example) so you can provide necessary arguments to operation using 
[GqlArgument](src/main/java/com/github/vladislavsevruk/generator/param/GqlArgument.java) class:
```kotlin
GqlArgument<Long> idArgument = GqlArgument.of("id", 1L);
String query = GqlRequestBodyGenerator.query("user").arguments(idArgument).selectionSet(User.class).generate();
```

If you need to provide several arguments you can use varargs:
```kotlin
GqlArgument<List<String>> firstNameArgument = GqlArgument.of("lastName", Arrays.asList("John", "Jane"));
GqlArgument<String> lastNameArgument = GqlArgument.of("lastName", "Doe");
String query = GqlRequestBodyGenerator.query("activeUsers").arguments(firstNameArgument, lastNameArgument)
        .selectionSet(User.class).generate();
```

or iterables:
```kotlin
GqlArgument<List<String>> firstNameArgument = GqlArgument.of("lastName", Arrays.asList("John", "Jane"));
GqlArgument<String> lastNameArgument = GqlArgument.of("lastName", "Doe");
List<GqlArgument<?>> arguments = Arrays.asList(firstNameArgument, lastNameArgument);
String query = GqlRequestBodyGenerator.query("activeUsers").arguments(arguments)
        .selectionSet(User.class).generate();
```

#### Input argument
Mutations usually use __input__ argument to pass complex [input objects](http://spec.graphql.org/June2018/#sec-Input-Objects).
You can use [GqlInputArgument](src/main/java/com/github/vladislavsevruk/generator/param/GqlInputArgument.java) to pass 
__input object__ for mutation:
```kotlin
// prepare input model
User newUser = new User();
newUser.setFirstName("John");
newUser.setLastName("Doe");

// mutation
GqlInputArgument<User> inputArgument = GqlInputArgument.of(newUser);
// the same as GqlArgument<User> inputArgument = GqlArgument.of("input", newUser);
String mutation = GqlRequestBodyGenerator.mutation("newUser").arguments(inputArgument)
        .selectionSet(User.class).generate();
```

By default, __input object__ will be generated using non-null field values but like [selection set](#operation-selection-set)
__input object__ can be generated using 
[predefined fields picking strategies](src/main/java/com/github/vladislavsevruk/generator/strategy/picker/mutation/InputGenerationStrategy.java):
- pick all marked fields
```kotlin
String query = GqlRequestBodyGenerator.mutation("newUser")
        .arguments(InputGenerationStrategy.ALL_FIELDS, inputArgument).selectionSet(User.class).generate();
```

- pick only fields with non-null value
```kotlin
String query = GqlRequestBodyGenerator.mutation("newUser")
        .arguments(InputGenerationStrategy.WITHOUT_NULLS, inputArgument).selectionSet(User.class).generate();
```

## License
This project is licensed under the MIT License, you can read the full text [here](LICENSE).
