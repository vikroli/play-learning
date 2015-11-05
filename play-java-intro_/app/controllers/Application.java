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
		MyNumber number = Form.form(MyNumber.class).bindFromRequest().get();
		JPA.em().persist(number);
		return redirect(routes.Application.index());
	}

	@Transactional(readOnly = true)
	public Result getNumbers() {
		@SuppressWarnings("unchecked")
		List<MyNumber> numbers = (List<MyNumber>) JPA.em().createQuery("select n from MyNumber n").getResultList();
		return ok(toJson(numbers));
	}

	@Transactional(readOnly = true)
	public Result getCount() {
		long count = (long) JPA.em().createQuery("select count(value) from MyNumber n").getSingleResult();
		return ok(toJson(count));
	}

	@Transactional(readOnly = true)
	public Result getAverage() {
		try {
			double average = (double) JPA.em().createQuery("select avg(value) from MyNumber n").getSingleResult();
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

	@Transactional
	public Result deleteNumber() {
		MyNumber mn = Form.form(MyNumber.class).bindFromRequest().get();
		int i = JPA.em().createQuery("delete from MyNumber where id=" + mn.id).executeUpdate();
		if (i == 0)
			return badRequest(toJson("id is not valid"));
		return redirect(routes.Application.index());
	}

	@Transactional
	public Result updateNumber() {
		MyNumber newNumber = Form.form(MyNumber.class).bindFromRequest().get();
		long id = newNumber.id;
		MyNumber oldNumber = JPA.em().find(MyNumber.class, id);
		if (oldNumber == null)
			return badRequest(toJson("id is not valid"));
		oldNumber.value = newNumber.value;
		JPA.em().merge(oldNumber);
		return redirect(routes.Application.index());
	}
}
