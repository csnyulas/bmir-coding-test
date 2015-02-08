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
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

public class Main {

   private static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
   private static OWLDataFactory factory = manager.getOWLDataFactory();

   public static void main(String[] args) {
      try {
         /*
          * Load ontology from a file
          */
         OWLOntology ont = loadOntology("src/main/resources/input.owl");
         
         /*
          * Print out the list of classes along with any rdfs:labels that is written in the Portuguese.
          */
         printClassesInPortuguese(ont);
         
         /*
          * Generate and add to a new ontology new labels for each class (using skos:prefLabel)
          * from its IRI
          */
         createPrefLabelOntology(ont, "src/main/resources/out.owl");
      }
      catch (OWLOntologyCreationException e) {
         log(System.err, e.getMessage());
      }
      catch (OWLOntologyStorageException e) {
         log(System.err, e.getMessage());
      }
   }

   private static OWLOntology loadOntology(String path) throws OWLOntologyCreationException {
      File fin = new File(path);
      return manager.loadOntologyFromOntologyDocument(fin);
   }

   private static void printClassesInPortuguese(OWLOntology ont) {
      OWLAnnotationProperty rdfsLabel = factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());
      for (OWLClass cls : ont.getClassesInSignature()) {
         // Get the annotations on the class that use the label property
         for (OWLAnnotation annotation : cls.getAnnotations(ont, rdfsLabel)) {
            if (annotation.getValue() instanceof OWLLiteral) {
               OWLLiteral val = (OWLLiteral) annotation.getValue();
               if (val.hasLang("pt")) {
                  log(System.out, format("%s \"%s\"", cls, val.getLiteral()));
               }
            }
         }
      }
   }

   private static void createPrefLabelOntology(OWLOntology ont, String path) throws OWLOntologyStorageException, OWLOntologyCreationException {
      OWLAnnotationProperty prefLabel = factory.getOWLAnnotationProperty(SKOSVocabulary.PREFLABEL.getIRI());
      for (OWLClass cls : ont.getClassesInSignature()) {
         String localName = XMLUtils.getNCNameSuffix(cls.toStringID());
         OWLAnnotation annotation = factory.getOWLAnnotation(prefLabel,
               factory.getOWLLiteral(toTitleCase(localName), "en"));
         OWLAxiom axiom = factory.getOWLAnnotationAssertionAxiom(cls.getIRI(), annotation);
         manager.applyChange(new AddAxiom(ont, axiom));
      }
      File fout = new File(path);
      manager.saveOntology(ont, IRI.create(fout));
   }

   private static String toTitleCase(String s) {
      return StringUtils.join(
            StringUtils.splitByCharacterTypeCamelCase(s),
            ' ').replaceAll("_+\\s?", "");
   }

   private static void log(PrintStream stream, String message) {
      stream.println(message);
   }
}
