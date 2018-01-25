package no.hal.learning.exercise.standalone.samples;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import no.hal.learning.exercise.ExercisePackage;
import no.hal.learning.exercise.Proposal;
import no.hal.learning.exercise.jdt.JdtPackage;
import no.hal.learning.exercise.junit.JunitPackage;
import no.hal.learning.exercise.util.ExerciseResourceFactoryImpl;
import no.hal.learning.exercise.workbench.WorkbenchPackage;
import no.hal.learning.exercise.workspace.WorkspacePackage;

public class Samples {

	public static void main(String[] args) {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(ExercisePackage.eNS_URI, ExercisePackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(JdtPackage.eNS_URI, JdtPackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(JunitPackage.eNS_URI, JunitPackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(WorkspacePackage.eNS_URI, WorkspacePackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(WorkbenchPackage.eNS_URI, WorkbenchPackage.eINSTANCE);
		
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ex", new ExerciseResourceFactoryImpl());
		
		Resource resource = resourceSet.getResource(URI.createURI(Samples.class.getResource("Partner.ex").toString()), true);
		TreeIterator<EObject> it = resource.getAllContents();
		while (it.hasNext()) {
			EObject eObject = it.next();
			System.out.println(eObject);
			if (eObject instanceof Proposal<?>) {
//				it.prune();
			}
		}
	}
}
