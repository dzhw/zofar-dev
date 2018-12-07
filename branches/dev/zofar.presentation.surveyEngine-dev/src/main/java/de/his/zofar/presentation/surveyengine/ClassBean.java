/**
 *
 */
package de.his.zofar.presentation.surveyengine;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.component.UINamingContainer;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * @author meisner
 *
 */
@Controller
@Scope("request")
public class ClassBean implements Serializable {


	private static final long serialVersionUID = 5113768690081569958L;


	@PostConstruct
	private void init() {
	}

	public String getType(final Object obj){
		if(obj == null)return null;
		final Map<String,Object> map = ((UINamingContainer)obj).getAttributes();
		String back = map.get("org.apache.myfaces.compositecomponent.location")+"";
		back = back.split(" ")[0];
		back = back.replaceAll("^/", "");
		back = back.replaceAll(".xhtml", "");
		return back;
	}

	public String getTemplate(final Object obj){
		if(obj == null)return null;
		final Map<String,Object> map = ((UINamingContainer)obj).getAttributes();
		String back = map.get("org.apache.myfaces.compositecomponent.location")+"";
		back = back.split(" ")[0];
		return back;
	}

}
