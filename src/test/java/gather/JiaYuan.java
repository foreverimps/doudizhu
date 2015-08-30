package gather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.filters.XorFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;

public class JiaYuan {

	public static void test() throws Exception {
		String link_url = "http://www.jiayuan.com/51206448?fxly=search_v2_index";
		Parser parser = new Parser(link_url);

		NodeFilter[] filters = { new CssSelectorNodeFilter("div.img_box div.box a img"), new CssSelectorNodeFilter("div.my_information h1 span"),
		        new CssSelectorNodeFilter("div #showCon1"), new CssSelectorNodeFilter("div .my_information h2") };

		NodeFilter filter = new XorFilter(filters);

		NodeList nodelist = parser.parse(filter);
		for (int i = 0; i < nodelist.size(); i++) {
			Node node = nodelist.elementAt(i);
			if (node instanceof ImageTag) {
				ImageTag imageTag = (ImageTag) node;
				System.out.println(imageTag.getAttribute("src"));
				continue;
			}
			if (node instanceof Span) {
				Span span = (Span) nodelist.elementAt(i);
				System.out.println(span.getChildrenHTML().trim());
				continue;
			}
			if (node instanceof Div) {
				Div div = (Div) node;
				System.out.println(div.getChildrenHTML().trim());
				continue;
			}
			if (node instanceof Tag) {
				Tag h2 = (Tag) node;
				String a = h2.getChildren().elementAt(0).toHtml();
				String h2_all = h2.toHtml();
				String result = h2_all.replace(a, "").replace("<h2>", "").replace("</h2>", "");
				System.out.println(result);
				continue;
			}
		}

		// NodeList nodelist2 = parser.parse(new
		// CssSelectorNodeFilter("div.my_information h1 span"));
		// for (int i = 0; i < nodelist2.size(); i++) {
		// Span node = (Span) nodelist2.elementAt(i);
		// System.out.println(node.getChildrenHTML());
		// }
		// TextExtractingVisitor visitor = new TextExtractingVisitor();
		// parser.visitAllNodesWith(visitor);
		// String textInPage = visitor.getExtractedText();
		//
		// message(textInPage);
	}

	void jiayuan() throws Exception {
		String link_url = "http://www.jiayuan.com/51206448?fxly=search_v2_index";
		URL myUrl = new URL(link_url);
		BufferedReader br = new BufferedReader(new InputStreamReader(myUrl.openStream(), "UTF-8"));
		String s;
		boolean flag = false;
		while ((s = br.readLine()) != null) {
			if (s.contains("<ul id=\"urer_srcoll_box\"")) {
				flag = true;
			}
			if (flag) {
				if (s.contains("src")) {
					System.out.println(s);
				}
				if (s.contains("<div class=\"right_btn\"")) {
					flag = false;
					break;
				}
			}
		}

	}

	/**
	 * main函数.
	 * 
	 * @param args
	 *            启动参数
	 * @throws Exception
	 *             Exception
	 */
	public static void main(String... args) throws Exception {
		test();
	}
}
