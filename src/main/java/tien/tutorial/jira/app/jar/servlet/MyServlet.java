package tien.tutorial.jira.app.jar.servlet;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tien.tutorial.jira.app.library.api.LibraryService;

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