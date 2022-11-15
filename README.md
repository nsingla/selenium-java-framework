# Basic Framework to get you started with running UI tests using Selenium
This allows you to run UI tests using selenium webdriver. You can also run your tests in parallel by specifying the number of parallel threads as `-DthreadCount=` as runtime param.

## Requirement
* Java 11
* Maven >3.0

## Building the project:
* You can build the project with maven goals `clean install`

## How To Use

In order to include *selenium-java-framework* in your Maven project, first add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.nsingla</groupId>
    <artifactId>selenium-framework</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
</dependency>
```

And also add following repository to your pom:
```xml
<repositories>
    <repository>
        <id>ossrh</id>
        <name>Central Repository OSSRH</name>
        <url>https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/</url>
    </repository>
</repositories>
```

## Examples to run tests
Simply extend your test class with `SeleniumBase.java`

```java

import io.nsingla.selenium.SeleniumBase;

/**
 * This will enable your tests to fetch a webdriver object
 */
class YourTestClass extends SeleniumBase {

}
 ```

## Available runtime parameters
| Param           | Values                                                                 | Default   |
|-----------------|------------------------------------------------------------------------|-----------|
| mode            | LOCAL, REMOTE                                                          | LOCAL     |
| browser         | chrome, firefox, <br/>ie, edge, safari                                 | chrome    |
| headless        | true, false                                                            | false     |
| gridHost        | String                                                                 | localhost |
| gridPort        | String                                                                 | 4444      |
| consoleloglevel | OFF, SEVERE, WARNING, <br/>INFO, CONFIG, FINE, <br/>FINER, FINEST, ALL | OFF       |
| locale          | en, es ...                                                             | en        |
| downloadPath    | String                                                                 | null      |
| retryCount      | String                                                                 | 0         |
| threadCount     | String                                                                 | 1         |
- `mode` - To run tests locally or via grid
- `browser` - To specify which browser to run tests in
- `headless` - To run tests in a headless mode or via a GUI
- `gridHost` - Selenium Grid hostname
- `gridPort` - Selenium Grid Port Number
- `consoleloglevel` - Log Level for logging 
- `locale` - to set browser locale (in case website picks it based on your location)
- `downloadPath` - To specify a custom download path
- `retryCount` - Number of times a test should retry if failed (find how to enable your tests to rerun on failure [here](https://github.com/nsingla/junit5-framework/blob/master/README.md))
- `threadCount` - To specify how many tests you want to run in parallel (total across whole suite of classes)