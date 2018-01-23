import org.junit.Test;

import fr.sedoo.commons.util.TemplatedStringParser;
import junit.framework.Assert;

public class FileMatchingTest {
	
	@Test
	public void test1() throws Exception {
		String fileTemplate= "${model}_${model2}_${yyyy}${MM}${dd}.scattering_coefficient_520nm.png";
		String fileName = "M9003Data_Pdm_20140504.scattering_coefficient_520nm.png";
		
		TemplatedStringParser parser = new TemplatedStringParser(fileTemplate);
		parser.parse(fileName);
		Assert.assertTrue(parser.matches(fileName));
		System.out.println(parser.getValue("model"));
		System.out.println(parser.getValue("model2"));
		System.out.println(parser.getValue("MM"));
		System.out.println(parser.getValue("dd"));
		System.out.println(parser.getValue("yyyy"));
				
	}

}
