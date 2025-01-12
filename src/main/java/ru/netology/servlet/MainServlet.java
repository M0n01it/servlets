package ru.netology.servlet;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.config.AppConfig;
import ru.netology.controller.PostController;
import ru.netology.exception.NotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainServlet extends HttpServlet {
  private PostController controller;
  private ApplicationContext context;

  private static final String API_POSTS = "/api/posts";
  private static final Pattern ID_PATTERN = Pattern.compile("/api/posts/(\\d+)");

  @Override
  public void init() throws ServletException {
    super.init();
    // Инициализация Spring Context
    context = new AnnotationConfigApplicationContext(AppConfig.class);
    // Получение бина PostController из контекста
    controller = context.getBean(PostController.class);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();

      switch (method) {
        case "GET":
          if (path.equals(API_POSTS)) {
            controller.all(resp);
            return;
          }
          Matcher getMatcher = ID_PATTERN.matcher(path);
          if (getMatcher.matches()) {
            long id = Long.parseLong(getMatcher.group(1));
            controller.getById(id, resp);
            return;
          }
          break;
        case "POST":
          if (path.equals(API_POSTS)) {
            controller.save(req.getReader(), resp);
            return;
          }
          break;
        case "DELETE":
          Matcher deleteMatcher = ID_PATTERN.matcher(path);
          if (deleteMatcher.matches()) {
            long id = Long.parseLong(deleteMatcher.group(1));
            controller.removeById(id, resp);
            return;
          }
          break;
        default:
          break;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (NotFoundException e) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}