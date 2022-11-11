[![Build Status](https://travis-ci.org/VladislavSevruk/GraphQlRequestBodyGenerator.svg?branch=master)](https://travis-ci.com/VladislavSevruk/GraphQlRequestBodyGenerator)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=VladislavSevruk_GraphQlRequestBodyGenerator&metric=alert_status)](https://sonarcloud.io/dashboard?id=VladislavSevruk_GraphQlRequestBodyGenerator)
[![Code Coverage](https://sonarcloud.io/api/project_badges/measure?project=VladislavSevruk_GraphQlRequestBodyGenerator&metric=coverage)](https://sonarcloud.io/component_measures?id=VladislavSevruk_GraphQlRequestBodyGenerator&metric=coverage)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.vladislavsevruk/graphql-request-body-generator/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.vladislavsevruk/graphql-request-body-generator)

# GraphQL Request Body Generator
This utility library helps to generate request body for [GraphQL](http://spec.graphql.org/June2018/) queries using POJOs.

## Table of contents
* [Getting started](#getting-started)
  * [Maven](#maven)
  * [Gradle](#gradle)
* [Usage](#usage)
  * [Field marking strategy](#field-marking-strategy)
  * [Prepare POJO model](#prepare-pojo-model)
    * [Automatic generation](#automatic-generation)
      * [Gradle plugin](#gradle-plugin)
    * [Manual generation](#manual-generation)
      * [Selection set](#selection-set)
        * [GqlField](#gqlfield)
        * [GqlDelegate](#gqldelegate)
        * [GqlUnion](#gqlunion)
        * [GqlIgnore](#gqlignore)
      * [Input object value](#input-object-value)
        * [GqlField](#gqlfield-1)
        * [GqlInput](#gqlinput)
        * [GqlDelegate](#gqldelegate-1)
        * [GqlIgnore](#gqlignore-1)
  * [Generate request body](#generate-request-body)
    * [Operation selection set](#operation-selection-set)
    * [Loop breaking strategy](#loop-breaking-strategy)
    * [Arguments](#arguments)
      * [Input argument](#input-argument)
      * [Variables](#variables)
      * [Delegate argument](#delegate-argument)
      * [Mutation argument strategy](#mutation-argument-strategy)
    * [Operation Alias](#operation-alias)
* [License](#license)

## Getting started
To add library to your project perform next steps:

### Maven
Add the following dependency to your ``pom.xml``:
```xml
<dependency>
      <groupId>com.github.vladislavsevruk</groupId>
      <artifactId>graphql-request-body-generator</artifactId>
      <version>1.0.16</version>
</dependency>
```
### Gradle
Add the following dependency to your ``build.gradle``:
```groovy
implementation 'com.github.vladislavsevruk:graphql-request-body-generator:1.0.16'
```

## Usage
First of all we need to choose field marking strategy that will be used for GraphQL operation generation.

### Field marking strategy
There are two predefined field marking strategy that define the way how builder determine if field should be used for
GraphQL operation generation:
* Use all fields except ones with [GqlIgnore](graphql-request-body-generator-annotation/src/main/java/com/github/vladislavsevruk/generator/annotation/GqlIgnore.java)
  annotation \[default]
* Use only fields with one of [GqlField](graphql-request-body-generator-annotation/src/main/java/com/github/vladislavsevruk/generator/annotation/GqlField.java)
  or [GqlDelegate](graphql-request-body-generator-annotation/src/main/java/com/github/vladislavsevruk/generator/annotation/GqlDelegate.java) annotations

Current strategy for both [operation selection set](http://spec.graphql.org/June2018/#sec-Selection-Sets) and mutation
[input object values](http://spec.graphql.org/June2018/#sec-Input-Object-Values) can be set using
[FieldMarkingStrategySourceManager](graphql-request-body-generator/src/main/java/com/github/vladislavsevruk/generator/strategy/marker/FieldMarkingStrategySourceManager.java):
```kotlin
FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
// or
FieldMarkingStrategySourceManager.input().useOnlyMarkedFieldsStrategy();
```

However, you can set your own custom [FieldMarkingStrategy](graphql-request-body-generator/src/main/java/com/github/vladislavsevruk/generator/strategy/marker/FieldMarkingStrategy.java):
```kotlin
FieldMarkingStrategySourceManager.selectionSet().useCustomStrategy(field -> true);
```

### Prepare POJO model
Then we need to prepare POJO models that will be used for GraphQL operation generation according to chosen field marking
strategy.

### Automatic generation
For automatic generation of POJO models at your project you can use following options:

### Gradle plugin
Add the following plugin to your ``build.gradle``:
```groovy
plugins {
  id 'io.github.vladislavsevruk.graphql-model-generator-plugin' version '1.0.0'
}
```
This plugin will add additional ``generateGraphqlModels`` task before ``javaCompile`` that will automatically generate
POJO models based on GraphQL schema file. Generation results can be customized using followed options of
``graphqlModelGenerator`` extension:

```groovy
import com.github.vladislavsevruk.generator.model.graphql.constant.*

graphqlModelGenerator {
  addJacksonAnnotations = false
  entitiesPrefix = ''
  entitiesPostfix = ''
  pathToSchemaFile = '/path/to/schema.graphqls'
  targetPackage = 'com.myorg'
  treatArrayAs = ElementSequence.LIST
  treatFloatAs = GqlFloatType.DOUBLE
  treatIdAs = GqlIntType.STRING
  treatIntAs = GqlIntType.INTEGER
  updateNamesToJavaStyle = true
  useLombokAnnotations = false
  usePrimitivesInsteadOfWrappers = false
  useStringsInsteadOfEnums = false
}
```

* __addJacksonAnnotations__ reflects if jackson annotations should be added to fields for proper mapping using Jackson 
library. Default value is ``false``;
* __entitiesPrefix__ is used for adding specific prefix to generated POJO model names. Default value is empty string;
* __entitiesPostfix__ is used for adding specific postfix to generated POJO model names. Default value is empty string;
* __pathToSchemaFile__ is used for setting location of GraphQL schema file. Default location is 
``src/main/resources/graphql/schema.graphqls``;
* __targetPackage__ is used for setting specific package name for generated POJO models. Default value is
``com.github.vladislavsevruk.model``;
* __treatArrayAs__ is used for setting entity that should be used for GraphQL array type generation. Can be one of 
* following values: ``ARRAY``, ``COLLECTION``, ``ITERABLE``, ``LIST``, ``SET``. Default value is ``LIST``;
* __treatFloatAs__ is used for setting entity that should be used for GraphQL array type generation. Can be one of
* following values: ``BIG_DECIMAL``, ``DOUBLE``, ``FLOAT``, ``STRING``. Default value is ``DOUBLE``;
* __treatIdAs__ is used for setting entity that should be used for GraphQL array type generation. Can be one of
* following values: ``BIG_INTEGER``, ``INTEGER``, ``LONG``, ``STRING``. Default value is ``STRING``;
* __treatIntAs__ is used for setting entity that should be used for GraphQL array type generation. Can be one of
* following values: ``BIG_INTEGER``, ``INTEGER``, ``LONG``, ``STRING``. Default value is ``INTEGER``;
* __updateNamesToJavaStyle__ reflects if entities and fields from GraphQL schema should be updated to follow default 
java convention (upper camel case for classes and lower camel case for fields). Default value is ``true``;
* __useLombokAnnotations__ reflects if lombok annotations should be used for POJO methods generation instead of 
ones generated by this plugin. Default value is ``false``;
* __usePrimitivesInsteadOfWrappers__ reflects if java primitives should be used for GraphQL scalar types instead of 
java primitive wrapper classes. Default value is ``false``;
* __useStringsInsteadOfEnums__ reflects if GraphQL enum fields at type entities should be converted to string type.
Default value is ``false``;

### Manual generation
However, you still can create and customize model by yourself following rules described below.

### Selection set
Selection set generation is based only on fields that are declared at class or it superclasses and doesn't involve
declared methods.

#### GqlField
[GqlField](graphql-request-body-generator-annotation/src/main/java/com/github/vladislavsevruk/generator/annotation/GqlField.java) annotation marks model fields
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

Values of [GqlFieldArgument](graphql-request-body-generator-annotation/src/main/java/com/github/vladislavsevruk/generator/annotation/GqlFieldArgument.java)
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
[GqlDelegate](graphql-request-body-generator-annotation/src/main/java/com/github/vladislavsevruk/generator/annotation/GqlDelegate.java) is used for complex fields
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

#### GqlUnion
[GqlUnion](graphql-request-body-generator-annotation/src/main/java/com/github/vladislavsevruk/generator/annotation/GqlUnion.java) annotation is used for fields
that should be treated as [union](http://spec.graphql.org/June2018/#sec-Unions):
```java
public class Order {
    @GqlField
    private Long id;
    @GqlField
    private Date orderDate;
    @GqlUnion({ @GqlUnionType(Book.class), @GqlUnionType(value = BoardGame.class, name = "Game") })
    private OrderItem orderItem;
}

public class OrderItem {
    @GqlField
    private Long id;
    @GqlField
    private String title;
}

public class Book extends OrderItem {
    @GqlField
    private Long numberOfPages;
}

public class BoardGame extends OrderItem {
    @GqlField
    private Long numberOfPlayers;
}
```

#### GqlIgnore
[GqlIgnore](graphql-request-body-generator-annotation/src/main/java/com/github/vladislavsevruk/generator/annotation/GqlIgnore.java) is used with "all fields
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
related getter methods (or field itself if no related getter found).

#### GqlField
[GqlField](graphql-request-body-generator-annotation/src/main/java/com/github/vladislavsevruk/generator/annotation/GqlField.java) annotation is also used for
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
[GqlInput](graphql-request-body-generator-annotation/src/main/java/com/github/vladislavsevruk/generator/annotation/GqlInput.java) annotation is used with methods
and helps to point to related GraphQL field if method name doesn't match field getter pattern:
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

If provided name doesn't match any of GraphQL fields at model result of method execution will be treated as new field:
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
[GqlDelegate](graphql-request-body-generator-annotation/src/main/java/com/github/vladislavsevruk/generator/annotation/GqlDelegate.java) is used for complex values
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
[GqlIgnore](graphql-request-body-generator-annotation/src/main/java/com/github/vladislavsevruk/generator/annotation/GqlIgnore.java) is used with "all fields
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
[GqlRequestBodyGenerator](graphql-request-body-generator/src/main/java/com/github/vladislavsevruk/generator/GqlRequestBodyGenerator.java):
```kotlin
// query
String query = GqlRequestBodyGenerator.query("allUsers").selectionSet(User.class).generate();

// prepare input model
User newUser = new User();
newUser.setFirstName("John");
newUser.setLastName("Doe");
// mutation
GqlInputArgument input = GqlInputArgument.of(newUser);
String mutation = GqlRequestBodyGenerator.mutation("newUser").arguments(input).selectionSet(User.class)
        .generate();
```

Generated operation is wrapped into json and can be passed as body to any API Client. If you want to generate pure 
unwrapped to JSON GraphQL query you can use ``GqlRequestBodyGenerator.unwrapped()`` first, all other methods calls 
remain the same:
```kotlin
// query
String query = GqlRequestBodyGenerator.unwrapped().query("allUsers").selectionSet(User.class)
        .generate();

// prepare input model
User newUser = new User();
newUser.setFirstName("John");
newUser.setLastName("Doe");
// mutation
GqlInputArgument input = GqlInputArgument.of(newUser);
String mutation = GqlRequestBodyGenerator.unwrapped().mutation("newUser").arguments(input)
        .selectionSet(User.class).generate();
```

#### Operation selection set
By default, all marked fields will be used for selection set generation. However, you can generate selection set using
one of [predefined fields picking strategies](graphql-request-body-generator/src/main/java/com/github/vladislavsevruk/generator/strategy/picker/selection/SelectionSetGenerationStrategy.java):
- pick all marked fields \[default]
```kotlin
String query = GqlRequestBodyGenerator.query("allUsers")
        .selectionSet(User.class, SelectionSetGenerationStrategy.allFields()).generate();
// same result as
String query2 = GqlRequestBodyGenerator.query("allUsers").selectionSet(User.class).generate();
```

- pick only fields with __id__ name or fields that have nested field with __id__ name
```kotlin
String query = GqlRequestBodyGenerator.query("allUsers")
        .selectionSet(User.class, SelectionSetGenerationStrategy.onlyId()).generate();
```

- pick only fields that are marked as [non-null](http://spec.graphql.org/June2018/#sec-Type-System.Non-Null)
```kotlin
String query = GqlRequestBodyGenerator.query("allUsers")
        .selectionSet(User.class, SelectionSetGenerationStrategy.onlyNonNull()).generate();
```

- pick all except fields with [selection set](http://spec.graphql.org/June2018/#sec-Selection-Sets)
```kotlin
String query = GqlRequestBodyGenerator.query("allUsers")
        .selectionSet(User.class, SelectionSetGenerationStrategy.fieldsWithoutSelectionSets()).generate();
```

Also you can provide your own custom fields picking strategy that implements
[FieldsPickingStrategy](graphql-request-body-generator/src/main/java/com/github/vladislavsevruk/generator/strategy/picker/selection/FieldsPickingStrategy.java)
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

#### Loop breaking strategy
Some models may contain circular type reference on each other, like
```java
public class Parent {
  @GqlField
  private Long id;
  @GqlField(withSelectionSet = true)
  private List<Child> children;
}

public class Child {
  @GqlField
  private Long id;
  @GqlField(withSelectionSet = true)
  private Parent parent;
}
```
which leads to stack overflow during selection set generation. To avoid this library uses loop breaking mechanism that
can be customized using one of 
[predefined loop breaking strategies](graphql-request-body-generator/src/main/java/com/github/vladislavsevruk/generator/strategy/looping/LoopBreakingStrategy.java):
- do not include detected looped item to selection set \[default]
```kotlin
String query = GqlRequestBodyGenerator.query("queryName").selectionSet(Parent.class)
        .generate();
```
will result to
```json
{"query":"{queryName{id children{id}}}"}
```
- allow received nesting level
```kotlin
int nestingLevel = 1;
String query = GqlRequestBodyGenerator.query("queryName")
        .selectionSet(Parent.class, EndlessLoopBreakingStrategy.nestingStrategy(nestingLevel))
        .generate();
```
will result to
```json
{"query":"{queryName{id children{id parent{id children{id}}}}}"}
```

This strategy will be used as default during generation but you can set another max nesting level for specific field
using ``maxNestingLoopLevel`` method at [GqlField](#gqlfield) and [GqlUnionType](#gqlunion) annotations.
```java
public class Parent {
  @GqlField
  private Long id;
  @GqlField(withSelectionSet = true, maxNestingLoopLevel = 1)
  private List<Child> children;
}

public class Child {
  @GqlField
  private Long id;
  @GqlField(withSelectionSet = true)
  private Parent parent;
}
```

Also you can provide your own custom loop breaking strategy that implements
[LoopBreakingStrategy](graphql-request-body-generator/src/main/java/com/github/vladislavsevruk/generator/strategy/looping/LoopBreakingStrategy.java)
functional interface:
```kotlin
String query = GqlRequestBodyGenerator.query("queryName")
        .selectionSet(Parent.class,
                (typeMeta, trace) -> typeMeta.getType().equals(Parent.class))
        .generate();
```

#### Arguments
Some operations may require [arguments](http://spec.graphql.org/June2018/#sec-Language.Arguments) (to pick specific item
or filter items list, for example) so you can provide necessary arguments to operation using
[GqlArgument](graphql-request-body-generator/src/main/java/com/github/vladislavsevruk/generator/param/GqlArgument.java) class:
```kotlin
GqlArgument<Long> idArgument = GqlArgument.of("id", 1L);
String query = GqlRequestBodyGenerator.query("user").arguments(idArgument)
        .selectionSet(User.class).generate();
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
You can use [GqlInputArgument](graphql-request-body-generator/src/main/java/com/github/vladislavsevruk/generator/param/GqlInputArgument.java) to pass
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
[predefined fields picking strategies](graphql-request-body-generator/src/main/java/com/github/vladislavsevruk/generator/strategy/picker/mutation/InputGenerationStrategy.java):
- pick all marked fields
```kotlin
String query = GqlRequestBodyGenerator.mutation("newUser")
        .arguments(InputGenerationStrategy.allFields(), inputArgument).selectionSet(User.class).generate();
```

- pick only fields with non-null value
```kotlin
String query = GqlRequestBodyGenerator.mutation("newUser")
        .arguments(InputGenerationStrategy.nonNullsFields(), inputArgument).selectionSet(User.class).generate();
```

But you can provide your own input fields picking strategy that implements
[InputFieldsPickingStrategy](graphql-request-body-generator/src/main/java/com/github/vladislavsevruk/generator/strategy/picker/mutation/InputFieldsPickingStrategy.java)
interface:
```kotlin
InputFieldsPickingStrategy inputFieldsPickingStrategy = field -> field.getName().contains("Name");
String query = GqlRequestBodyGenerator.mutation("newUser")
        .arguments(inputFieldsPickingStrategy, inputArgument).selectionSet(User.class).generate();
```

#### Variables
As GraphQL query can be parameterized with [variables](https://spec.graphql.org/June2018/#sec-Language.Variables) you
can generate GraphQL operation body that way passing values as operation arguments and using one of
[predefined variables generation strategies](graphql-request-body-generator/src/main/java/com/github/vladislavsevruk/generator/strategy/variable/VariableGenerationStrategy.java):
- generate only for arguments of [GqlVariableArgument](graphql-request-body-generator/src/main/java/com/github/vladislavsevruk/generator/param/GqlVariableArgument.java)
  type:
```kotlin
String variableName = "id";
GqlParameterValue<Integer> variable = GqlVariableArgument.of(variableName, variableName, 1, true);
String query = GqlRequestBodyGenerator.query("getProfile")
        .arguments(VariableGenerationStrategy.byArgumentType(), variable)
        .selectionSet(User.class).generate();
```

- generate only for arguments with value type annotated by
  [GqlVariableType](graphql-request-body-generator-annotation/src/main/java/com/github/vladislavsevruk/generator/annotation/GqlVariableType.java) type;
```java
@GqlVariableType(variableType = "ContactsData", variableName = "contactsData")
public class Contacts {
  @GqlField
  private String email;
  @GqlField
  private String phoneNumber;

  // getters and setters
    ...
}
```
```kotlin
Contacts contacts = new Contacts().setEmail("test@domain.com").setPhoneNumber("3751945");
String variableName = "id";
GqlArgument<Contacts> variable = GqlArgument.of("contacts", contacts);
String query = GqlRequestBodyGenerator.mutation("updateContacts")
        .arguments(VariableGenerationStrategy.annotatedArgumentValueType(), variable)
        .selectionSet(Contacts.class).generate();
```

Or you can provide your own variables generation strategy that implements
[VariablePickingStrategy](graphql-request-body-generator/src/main/java/com/github/vladislavsevruk/generator/strategy/variable/VariablePickingStrategy.java)
interface.

#### Delegate argument
If query or mutation has big arguments number it may be easier to create object to keep them all at one place and manage
together. In that case you can use [GqlDelegateArgument](graphql-request-body-generator/src/main/java/com/github/vladislavsevruk/generator/param/GqlDelegateArgument.java)
to simply pass such object as argument:
```kotlin
Contacts contacts = new Contacts().setEmail("test@domain.com").setPhoneNumber("3751945");
GqlArgument<Contacts> delegateArgument = GqlDelegateArgument.of(contacts);
String query = GqlRequestBodyGenerator.mutation("updateContacts")
        .arguments(delegateArgument).selectionSet(Contacts.class).generate();
```

If you want to generate variables for delegated values you need to pass flag to
[GqlDelegateArgument](graphql-request-body-generator/src/main/java/com/github/vladislavsevruk/generator/param/GqlDelegateArgument.java)
and add [GqlVariableType](graphql-request-body-generator-annotation/src/main/java/com/github/vladislavsevruk/generator/annotation/GqlVariableType.java)
annotations to fields that should be passed as variables:

```java
public class Contacts {
  @GqlField
  @GqlVariableType
  private String email;
  @GqlField
  @GqlVariableType
  private String phoneNumber;

  // getters and setters
    ...
}
```
```kotlin
Contacts contacts = new Contacts().setEmail("test@domain.com").setPhoneNumber("3751945");
GqlArgument<Contacts> delegateArgument = GqlDelegateArgument.of(contacts, true);
String query = GqlRequestBodyGenerator.mutation("updateContacts")
        .arguments(delegateArgument).selectionSet(Contacts.class).generate();
```

#### Mutation argument strategy
By default, only __input__ argument value is treated as [complex input objects](http://spec.graphql.org/June2018/#sec-Input-Objects)
according to GraphQL specification. However, you can use other
[model arguments strategies](graphql-request-body-generator/src/main/java/com/github/vladislavsevruk/generator/strategy/argument/ModelArgumentGenerationStrategy.java)
to override this behavior:
- treat only __input__ argument as potential complex input object
```kotlin
String query = GqlRequestBodyGenerator.mutation("newUser")
        .arguments(ModelArgumentGenerationStrategy.onlyInputArgument(), inputArgument)
        .selectionSet(User.class).generate();
```

- treat any argument as potential complex input object
```kotlin
String query = GqlRequestBodyGenerator.mutation("newUser")
        .arguments(ModelArgumentGenerationStrategy.anyArgument(), inputArgument)
        .selectionSet(User.class).generate();
```

You can also provide your own mutation arguments strategy that implements
[ModelArgumentStrategy](graphql-request-body-generator/src/main/java/com/github/vladislavsevruk/generator/strategy/argument/ModelArgumentStrategy.java)
interface:
```kotlin
ModelArgumentStrategy modelArgumentStrategy = argument -> argument.getName().contains("Model");
String query = GqlRequestBodyGenerator.mutation("newUser")
        .arguments(modelArgumentStrategy, inputArgument).selectionSet(User.class).generate();
```

### Operation Alias
When generating operation with [variables](#variables) resulted new operation will be anonymous by default. You can set
operation alias for convenience using ``operationAlias`` method:
```kotlin
String variableName = "id";
String operationAlias = "id";
GqlParameterValue<Integer> variable = GqlVariableArgument.of(variableName, variableName, 1, true);
String query = GqlRequestBodyGenerator.query("getProfile")
        .operationAlias("getProfileParameterized")
        .arguments(VariableGenerationStrategy.byArgumentType(), variable)
        .selectionSet(User.class).generate();
```

## License
This project is licensed under the MIT License, you can read the full text [here](LICENSE).
