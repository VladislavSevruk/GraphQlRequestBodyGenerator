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
  * [Prepare POJO model](#prepare-pojo-model)
    * [Field marking strategy](#field-marking-strategy)
    * [Annotations](#annotations)
      * [GqlField](#gqlfield)
      * [GqlDelegate](#gqldelegate)
      * [GqlIgnore](#gqlignore)
      * [GqlQuery](#gqlquery)
  * [Generate query body](#generate-query-body)
    * [Provide query name](#provide-query-name)
    * [Arguments](#arguments)
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
### Prepare POJO model
First of all we need to prepare POJO models that will be used for query body generation according to current field 
marking strategy.

### Field marking strategy
There are two predefined field marking strategy that define the way how builder determine if field should be used for 
query generation:
  * Use all fields except ones with [GqlIgnore](#gqlignore) annotation \[default]
  * Use only fields with one of [GqlField](#gqlfield) or [GqlDelegate](#gqldelegate) annotations

Current strategy can be set using [FieldMarkingStrategyManager](src/main/java/com/github/vladislavsevruk/generator/strategy/marker/FieldMarkingStrategyManager.java):
```kotlin
FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
// or
FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
```

### Annotations
#### GqlField
[GqlField](src/main/java/com/github/vladislavsevruk/generator/annotation/GqlField.java) annotation marks model fields 
that should be treated as [GraphQL field](http://spec.graphql.org/June2018/#sec-Language.Fields). By default field name 
will be used for query generation but you can override it using __name__ method. If you mark field using 
__withSelectionSet__ method it will be treated as field that have [selection set](http://spec.graphql.org/June2018/#sec-Selection-Sets) 
with nested fields. Annotation also has method __nonNullable__ that allow to mark field that was denoted as 
[non-null](http://spec.graphql.org/June2018/#sec-Type-System.Non-Null) at GraphQL schema:
```java
public class User {
    @GqlField(nonNullable = true)
    private Long id;
    @GqlField
    private String sex;
    @GqlField(name = "wish_list_items_urls")
    private List<String> withListItemsUrls;
    @GqlField(name = "user_contacts", withSelectionSet = true)
    private Contacts contacts;
    @GqlField(nonNullable = true, withSelectionSet = true)
    private List<Order> orders;
}

public class Contacts {
    @GqlField(nonNullable = true)
    private String email;
    @GqlField(name = "phone_number")
    private String phoneNumber;
}

public class Order {
    @GqlField(nonNullable = true)
    private Long id;
    @GqlField(nonNullable = true, name = "is_delivered")
    private Boolean isDelivered;
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
    @GqlField(name = "first_name")
    private String firstName;
    @GqlField(name = "last_name")
    private String lastName;
}
```
is equivalent to:
```java
public class User {
    @GqlField(name = "first_name")
    private String firstName;
    @GqlField(name = "last_name")
    private String lastName;
}
```

#### GqlIgnore
[GqlIgnore](src/main/java/com/github/vladislavsevruk/generator/annotation/GqlIgnore.java) is used with "all fields 
except ignored" [field marking strategy](#field-marking-strategy) for marking field that shouldn't be used for query 
generation:
```java
public class UserInfo {
    private String firstName;
    @GqlIgnore
    private String fullName;
    private String lastName;
}
```

#### GqlQuery
[GqlQuery](src/main/java/com/github/vladislavsevruk/generator/annotation/GqlQuery.java) is designed to set default query
name that will be used for query generation:
```java
@GqlQuery(name = "users")
public class User {
    ...
}
```
If query name was not set class name will be used instead.

### Generate query body
Once POJO models are ready we can generate GraphQL query using [GqlQueryGenerator](src/main/java/com/github/vladislavsevruk/generator/GqlQueryGenerator.java).
This class has several predefined methods with different fields picking strategies for query body generation, like:
- pick all marked fields
```kotlin
String queryBody = GqlQueryGenerator.allFields(User.class);
```
- pick only fields with __id__ name or fields that have nested field with __id__ name
```kotlin
String queryBody = GqlQueryGenerator.onlyId(User.class);
```
- pick only fields that are marked as [non-null](http://spec.graphql.org/June2018/#sec-Type-System.Non-Null)
```kotlin
String queryBody = GqlQueryGenerator.onlyNonNullable(User.class);
```
- pick all except fields with [selection set](http://spec.graphql.org/June2018/#sec-Selection-Sets) 
```kotlin
String queryBody = GqlQueryGenerator.withoutFieldsWithSelectionSet(User.class);
```

Also you can provide your own custom fields picking strategy that implements [FieldsPickingStrategy](src/main/java/com/github/vladislavsevruk/generator/strategy/picker/FieldsPickingStrategy.java)
functional interface:
```kotlin
String queryBody = GqlQueryGenerator.customQuery(User.class,
        field -> field.getName().contains("Name"));
```

If you want to use generic models it's recommended to provide them via [TypeProvider](https://github.com/VladislavSevruk/TypeResolver/blob/develop/src/main/java/com/github/vladislavsevruk/resolver/type/TypeProvider.java):
```kotlin
String queryBody = GqlQueryGenerator.allFields(new TypeProvider<User<UserInfo>>() {});
```

#### Provide query name
Some queries may have similar structure so it may be convenient to use same POJO models for them. But as they have 
different names setting it through [GqlQuery](#gqlquery) annotation may not make a trick so you can provide query name 
to method directly:
```kotlin
String queryBody = GqlQueryGenerator.allFields("active_users", User.class);
```

#### Arguments
Some queries may require query arguments (to pick specific item or filter items list, for example) so you can provide
necessary arguments to query using [QueryArgument](src/main/java/com/github/vladislavsevruk/generator/param/QueryArgument.java)
class:
```kotlin
QueryArgument<Long> idArgument = new QueryArgument<>("id", 1L);
String queryBody = GqlQueryGenerator.allFields("user", User.class, idArgument);
```
If you need to provide several arguments you can use varargs:
```kotlin
QueryArgument<List<String>> firstNameArgument = new QueryArgument<>("last_name",
        Arrays.asList("John", "Jane"));
QueryArgument<String> lastNameArgument = new QueryArgument<>("last_name", "Doe");
String queryBody = GqlQueryGenerator.allFields("active_users", User.class,
        firstNameArgument, lastNameArgument);
```
or iterables:
```kotlin
QueryArgument<List<String>> firstNameArgument = new QueryArgument<>("last_name",
        Arrays.asList("John", "Jane"));
QueryArgument<String> lastNameArgument = new QueryArgument<>("last_name", "Doe");
List<QueryArgument<?>> arguments = Arrays.asList(firstNameArgument, lastNameArgument);
String queryBody = GqlQueryGenerator.allFields("active_users", User.class, arguments);
```

## License
This project is licensed under the MIT License, you can read the full text [here](LICENSE).
