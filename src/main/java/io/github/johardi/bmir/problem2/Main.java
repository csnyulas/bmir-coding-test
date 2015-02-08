package io.github.johardi.bmir.problem2;

import static java.lang.String.format;

import java.io.File;
import java.io.PrintStream;

import org.apache.commons.lang3.StringUtils;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.XMLUtils;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

public class Main {

   private static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

   private static OWLDataFactory factory = manager.getOWLDataFactory();

   // Defining rdfs:label annotation
   private static OWLAnnotationProperty rdfsLabel =
         factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());

   // Defining skos:prefLabel annotation
   private static OWLAnnotationProperty skosPrefLabel =
         factory.getOWLAnnotationProperty(SKOSVocabulary.PREFLABEL.getIRI());

   public static void main(String[] args) {
      try {
         /*
          * Load ontology from a file
          */
         log(System.out, "Task 1: Load ontology from a file.");
         OWLOntology ont = loadOntology("src/main/resources/input.owl");
         
         /*
          * Print out the list of classes along with any rdfs:labels that is written in the Portuguese.
          */
         log(System.out, "Task 2: Print out classes in Portuguese label.");
         printClassesInPortuguese(ont);
         
         /*
          * Generate and add to a new ontology new labels for each class (using skos:prefLabel)
          * from its IRI
          */
         log(System.out, "Task 3: Create ontology with prefered labels.");
         createPrefLabelOntology(ont, "src/main/resources/out.owl");
      }
      catch (OWLException e) {
         log(System.err, e.getMessage());
      }
   }

   private static OWLOntology loadOntology(String path) throws OWLException {
      File fin = new File(path);
      return manager.loadOntologyFromOntologyDocument(fin);
   }

   private static void printClassesInPortuguese(OWLOntology ont) {
      for (OWLClass cls : ont.getClassesInSignature()) {
         // Find any rdfs:label annotation within the class
         for (OWLAnnotation annotation : cls.getAnnotations(ont, rdfsLabel)) {
            if (annotation.getValue() instanceof OWLLiteral) {
               OWLLiteral val = (OWLLiteral) annotation.getValue();
               if (val.hasLang("pt")) { // check for @pt language
                  log(System.out, format("%s \"%s\"", cls, val.getLiteral()));
               }
            }
         }
      }
   }

   private static void createPrefLabelOntology(OWLOntology ont, String path) throws OWLException {
      for (OWLClass cls : ont.getClassesInSignature()) {
         String localName = XMLUtils.getNCNameSuffix(cls.toStringID());
         OWLLiteral value = factory.getOWLLiteral(toTitleCase(localName), "en");
         OWLAnnotation annotation = factory.getOWLAnnotation(skosPrefLabel, value);
         OWLAxiom axiom = factory.getOWLAnnotationAssertionAxiom(cls.getIRI(), annotation);
         manager.applyChange(new AddAxiom(ont, axiom));
      }
      File fout = new File(path);
      manager.saveOntology(ont, IRI.create(fout));
   }

   private static String toTitleCase(String s) {
      return StringUtils.join(
            StringUtils.splitByCharacterTypeCamelCase(s),
            ' ').replaceAll("_+\\s?", ""); // remove underscores
   }

   private static void log(PrintStream stream, String message) {
      stream.println(message);
   }
}
