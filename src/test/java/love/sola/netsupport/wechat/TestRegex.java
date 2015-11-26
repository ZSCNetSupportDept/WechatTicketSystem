package love.sola.netsupport.wechat;

import org.junit.Test;

/**
 * ***********************************************
 * Created by Sola on 2015/11/26.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class TestRegex {

	public static final String STUDENT_ID_REGEX = "^(2012|2013|2014|2015)[0-9]{9}";

	@Test
	public void testStudentId() {
		System.out.println("2011130201233".matches(STUDENT_ID_REGEX));
		System.out.println("2015130201233".matches(STUDENT_ID_REGEX));
	}

}
