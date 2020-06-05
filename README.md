# jira-jar

You have successfully created an Atlassian Plugin!

Here are the SDK commands you'll use immediately:

* atlas-run   -- installs this plugin into the product and starts it on localhost
* atlas-debug -- same as atlas-run, but allows a debugger to attach at port 5005
* atlas-help  -- prints description for all commands in the SDK

Full documentation is always available at:

https://developer.atlassian.com/display/DOCS/Introduction+to+the+Atlassian+Plugin+SDK


-----------------------
# Tutorial: How to create an jira plugin (jar file) with dependent jar inside.

This tutorial guide you create a Jira plugin with dependent jar inside.

During the development Jira plugin, you may want to create your own library and use it in many plugins.

In this tutorial we will create 2 project Jira plugins. The first plugin is called jira-library. This plugin will have an exported service called LibraryService. And package it as jar file.

The second plugin will add library-jar as dependency. Create servlet for demo, call the function getName() from LibraryService.



The source code of the two plugins is available here:

https://github.com/thuytiendang206/jira-library

https://github.com/thuytiendang206/jira-jar



**Create jira-library**

Run command:

`atlas-create-jira-plugin`

Enter these params:
```
Define value for groupId: : tien.tutorial.jira.app.library
Define value for artifactId: : jira-library
Define value for version:  1.0.0-SNAPSHOT: :
Define value for package:  tien.tutorial.jira.app.library: :
Confirm properties configuration:
groupId: tien.tutorial.jira.app.library
artifactId: jira-library
version: 1.0.0-SNAPSHOT
package: tien.tutorial.jira.app.library
 Y: :Y
```


Open jira-library project and create an LibraryService interface:

```
public interface LibraryService
{
    String getName();
}
```

Create an implementation of LibraryService. Export as Service.

```
@ExportAsService ({LibraryService.class})
@Named ("libraryService")
public class LibraryServiceImpl implements LibraryService
{
    @ComponentImport
    private final ApplicationProperties applicationProperties;

    @Inject
    public LibraryServiceImpl(final ApplicationProperties applicationProperties)
    {
        this.applicationProperties = applicationProperties;
    }

    public String getName()
    {
        if(null != applicationProperties)
        {
            return "libraryService:" + applicationProperties.getDisplayName();
        }
        
        return "libraryService";
    }
}
```
Package jira-library as jar file. Run this command

```
atlas-package
```

Take a jira-library.jar file and push it in your local repository.


**Create jira-jar plugins**

Run command:
```
atlas-create-jira-plugin
```

Enter these params:

```
Define value for groupId: : tien.tutorial.jira.app.jar
Define value for artifactId: : jira-jar
Define value for version:  1.0.0-SNAPSHOT: :
Define value for package:  tien.tutorial.jira.app.jar: :
Confirm properties configuration:
groupId: tien.tutorial.jira.app.jar
artifactId: jira-jar
version: 1.0.0-SNAPSHOT
package: tien.tutorial.jira.app.jar
 Y: : Y

```

After that you have the template of jira-jar plugins.

Try to create the servlet module. Run this command

```
 atlas-create-jira-plugin-module 
```

Enter these params:

```
31: Webwork Plugin
32: Workflow Condition
33: Workflow Post Function
34: Workflow Validator
Choose a number (1/2/3/4/5/6/7/8/9/10/11/12/13/14/15/16/17/18/19/20/21/22/23/24/25/26/27/28/29/30/31/32/33/34): 21
[INFO] Google Analytics Tracking is enabled to collect AMPS usage statistics.
[INFO] Although no personal information is sent, you may disable tracking by adding <allowGoogleTracking>false</allowGoogleTracking> to the amps plugin configuration in your pom.xml
[INFO] Sending event to Google Analytics: AMPS:jira - Create Plugin Module - jira:Servlet
Enter New Classname MyServlet: : 
Enter Package Name tien.tutorial.jira.app.jar.servlet: : 
Show Advanced Setup? (Y/y/N/n) N: : N
[INFO] Adding the following items to the project:
[INFO]   [class: it.tien.tutorial.jira.app.jar.servlet.MyServletFuncTest]
[INFO]   [class: tien.tutorial.jira.app.jar.servlet.MyServlet]
[INFO]   [class: ut.tien.tutorial.jira.app.jar.servlet.MyServletTest]
[INFO]   [dependency: javax.servlet:servlet-api]
[INFO]   [dependency: org.apache.httpcomponents:httpclient]
[INFO]   [dependency: org.mockito:mockito-all]
[INFO]   [dependency: org.slf4j:slf4j-api]
[INFO]   [module: servlet]
[INFO]   i18n strings: 2
Add Another Plugin Module? (Y/y/N/n) N: : N
```

Modify the pom.xml of jira-jar plugin, adding dependency jira-library with scope COMPILE (important)

```
<dependency>
  <groupId>tien.tutorial.jira.app.library</groupId>
  <artifactId>jira-library</artifactId>
  <version>1.0.0</version>
  <scope>compile</scope>
</dependency>
```

Modify Class MyServlet, import and using LibraryService in jira-jar plugin

```
@Named
public class MyServlet extends HttpServlet {

  private static final Logger log = LoggerFactory.getLogger(MyServlet.class);

  private final LibraryService libraryService;

  @Inject
  public MyServlet(@ComponentImport LibraryService libraryService) {
    this.libraryService = libraryService;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setContentType("text/html");
    resp.getWriter()
        .write("<html><body>Hello World! " + libraryService.getName() + "</body></html>");
  }
}
```

Try to run jira-jar plugin with command
```
atlas-clean && atlas-run
```

Go to this link to check the service can run:  http://localhost:2990/jira/plugins/servlet/myservlet

It will show "Hello World! libraryService:JIRA"   ;) 

**Tip:** If you got the error cannot find the bean of your library dependency when running plugin. That mean spring scanner cannot auto scan your bean. So try to at the context:component-scan in file plugin-context.xml

```
<context:component-scan base-package="tien.tutorial.jira.app.library.api" />
```





