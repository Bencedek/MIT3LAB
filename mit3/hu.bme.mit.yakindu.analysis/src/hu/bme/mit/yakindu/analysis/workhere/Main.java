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
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents(); 
		TreeIterator<EObject> nameiterator = s.eAllContents(); 
		Set<String> deadlocks = new HashSet<String>();
		Integer nameCounter = 1;
		while (nameiterator.hasNext()) {
			EObject content = nameiterator.next();
			if(content instanceof State) {
				State state = (State) content;
				if (state.getName() == "") {
					state.setName("Untitled State " + nameCounter++);
					System.out.println("New name: " + state.getName());
				}					
			}
		}
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				if (state.getOutgoingTransitions().size() == 0) {
					deadlocks.add(state.getName());
				} else {
					EList <Transition> tr = state.getOutgoingTransitions();
					for(Transition t: tr) {
						System.out.println(state.getName() + " -> " + t.getTarget().getName());
					}
				}
			}
		}
		System.out.println("Deadlocks:" + deadlocks);
		
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
