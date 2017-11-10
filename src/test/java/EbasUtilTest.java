
import org.junit.Assert;
import org.junit.Test;

import fr.aeris.actris.validation.rest.services.EbasConstants;
import fr.aeris.actris.validation.rest.services.EbasUtil;
import fr.aeris.actris.validation.rest.services.QualityCodeList;
import fr.aeris.commons.metadata.domain.identification.data.dataset.DataProcessingLevel;

public class EbasUtilTest {
	
	@Test 
	public void testNephelometerAndLevel0() {
		QualityCodeList result  = EbasUtil.getCodeListByInstrumentAndLevel(EbasConstants.NEPHELOMETER, DataProcessingLevel.L0.toString());
		Assert.assertTrue(result.containsCode("559"));
		Assert.assertTrue(result.containsCode("640"));
		Assert.assertEquals(result.size(), 6);
	}

}
