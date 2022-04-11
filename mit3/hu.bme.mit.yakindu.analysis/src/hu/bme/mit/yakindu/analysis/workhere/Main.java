package hu.bme.mit.yakindu.analysis.workhere;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}

	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		System.out.println("package hu.bme.mit.yakindu.analysis.workhere;\r\n" + 
				"\r\n" + 
				"import java.io.BufferedReader;\r\n" + 
				"import java.io.IOException;\r\n" + 
				"import java.io.InputStreamReader;\r\n" + 
				"\r\n" + 
				"import hu.bme.mit.yakindu.analysis.RuntimeService;\r\n" + 
				"import hu.bme.mit.yakindu.analysis.TimerService;\r\n" + 
				"import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;\r\n" + 
				"import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"public class RunStatechart {\r\n" + 
				"	\r\n" + 
				"	public static void main(String[] args) throws IOException {\r\n" + 
				"		ExampleStatemachine s = new ExampleStatemachine();\r\n" + 
				"		s.setTimer(new TimerService());\r\n" + 
				"		RuntimeService.getInstance().registerStatemachine(s, 200);\r\n" + 
				"		s.init();\r\n" + 
				"		s.enter();\r\n" + 
				"	    s.runCycle();\r\n" + 
				"	    \r\n" + 
				"	    BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));\r\n" + 
				"		while (true) {\r\n" + 
				"		    String input = bfr.readLine();\r\n" + 
				"		    switch (input) {\r\n");
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		System.out.println("	public static void print(IExampleStateMachine s) {");
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof EventDefinition) {
				EventDefinition e = (EventDefinition) content;
				System.out.println("		        case \"" + e.getName() + "\":\r\n" +
								   "					s.raise" + e.getName() + "());\r\n" +
								   "					s.runCycle();\r\n" +
								   "					break;\r\n");
			}
		}
			System.out.println(
				"		        case \"exit\":\r\n" + 
				"		            System.exit(0);\r\n" + 
				"		    }\r\n" + 
				"		    print(s);\r\n" + 
				"		}\r\n" + 
				"	}\r\n");
		
		// Reading model
		iterator = s.eAllContents();
		System.out.println("	public static void print(IExampleStateMachine s) {");
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof VariableDefinition) {
				VariableDefinition v = (VariableDefinition) content;
				String stateName = "";
				if (v.getName().contains("white")) stateName = "W";
				else if (v.getName().contains("black")) stateName = "B";
				System.out.println("		System.out.println(\""+ stateName + " = \" + s.getSCInterface().get" + v.getName() + "());");
			}
			
		}

		System.out.println("}\r\n" + "");
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
