package analyzer.htmlparser;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class EnergyPlusHTMLParser {

    private File htmlFile;
    private Document doc;

    public EnergyPlusHTMLParser(File f) {
	htmlFile = f;

	try {
	    doc = Jsoup.parse(htmlFile, "UTF-8");
	    preprocessTable();
	} catch (IOException e) {
	    // do nothing
	}

    }

    private void preprocessTable() {
	String report = null;
	Elements htmlNodes = doc.getAllElements();
	for (int i = 0; i < htmlNodes.size(); i++) {
	    if (htmlNodes.get(i).text().contains("Report:")) {
		report = htmlNodes.get(i + 1).text();
	    }
	    if (htmlNodes.get(i).hasAttr("cellpadding")) {
		String tableName = htmlNodes.get(i - 3).text();
		htmlNodes.get(i).attr("tableID", report + ":" + tableName);
	    }
	}
    }
    

    public double getEUI() {
	Elements energyTable = doc
		.getElementsByAttributeValue("tableID",
			"Annual Building Utility Performance Summary:Site and Source Energy")
		.get(0).getElementsByTag("td");
	for (int i = 0; i < energyTable.size(); i++) {
	    if (energyTable.get(i).text().equalsIgnoreCase("Total Site Energy")) {
		return Double.parseDouble(energyTable.get(i + 2).text());
	    }
	}
	return 0.0;
    }
}
