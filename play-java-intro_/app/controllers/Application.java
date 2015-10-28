package controllers;

import static play.libs.Json.toJson;
import static play.data.Form.form;

import java.util.List;
import models.MyNumber;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

  public Result index() {
    return ok(index.render());
  }

  @Transactional
  public Result addNumber() {

    System.out.println("...addNumber()");
    MyNumber number = Form.form(MyNumber.class).bindFromRequest().get();
    System.out.println("\tnumber: " + number.id + " -> " + number.value);
    JPA.em().persist(number);
    System.out.println("\tpersisted");
    return redirect(routes.Application.index());
  }

  @Transactional(readOnly = true)
  public Result getNumbers() {
    //   List<MyNumber> numbers = MyNumber.findAll();
   List<MyNumber> numbers = (List<MyNumber>) JPA.em().createQuery("select n from MyNumber n").getResultList();
    return ok(toJson(numbers));
  }

  @Transactional(readOnly = true)
  public Result getCount() {
    long count =
        (long) JPA.em().createQuery("select count(value) from MyNumber n").getSingleResult();
    return ok(toJson(count));
  }

  @Transactional(readOnly = true)
  public Result getAverage() {
    try {
      double average =
          (double) JPA.em().createQuery("select avg(value) from MyNumber n").getSingleResult();
      return ok(toJson(average));
    } catch (NullPointerException e) {
      return ok(toJson("no numbers to calculate"));
    }
  }

  @Transactional(readOnly = true)
  public Result getSum() {
    try {
      Long sum = (long) JPA.em().createQuery("select sum(value) from MyNumber n").getSingleResult();
      return ok(toJson(sum));
    } catch (NullPointerException e) {
      return ok(toJson("no numbers to calculate"));
    }
  }
  
  //@Transactional
 // public  Result deleteById(long id) {
          //MyNumber number = Form.form(MyNumber.class).bindFromRequest().get();
    //MyNumber number =
   //     (MyNumber) JPA.em().createQuery("select n from MyNumber n where id=" + id).getSingleResult();
   // if (number == null) {
   //   return badRequest(toJson("There is no MyNumber with this id"));
   // } else {
   //   JPA.em().createQuery("delete n from MyNumber n where id=" + id).getResult();
   //   return ok(toJson(number));
   // }
  //}
  
  
  
   @Transactional
  public Result deleteNumber() {
      long numberId = Form.form(Long.class).bindFromRequest().get();
      System.out.println("*** "+numberId+" ***");
     JPA.em().createQuery("delete from MyNumber where id="+numberId).getSingleResult();
           System.out.println("*** deleted ***");
    return getNumbers();
  }
 //@Transactional
 //public  Result deleteNumber() {
    // long id=0;
  //  MyNumber number = MyNumber.findById(id);
   // MyNumber numberDeleted =
      //  JPA.em().createQuery("delete n from MyNumber n where id=" + number.id).getSingleResult();
     // return ok(toJson(numberDeleted));
 //     return ok();
  //}
  // public static Result editNumber(){}

}
