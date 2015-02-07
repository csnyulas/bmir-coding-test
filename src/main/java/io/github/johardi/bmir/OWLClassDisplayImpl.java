package io.github.johardi.bmir;

import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;

public class OWLClassDisplayImpl implements OWLClassDisplay {

   private IRI classIri;
   private String displayName;
   private Set<OWLAxiom> referencingAxioms;

   public OWLClassDisplayImpl(IRI classIri, String displayName, Set<OWLAxiom> referencingAxioms) {
      this.classIri = classIri;
      this.displayName = displayName;
      this.referencingAxioms = referencingAxioms;
   }

   public IRI getIRI() {
      return classIri;
   }

   public String getDisplayName() {
      return displayName;
   }

   public Set<OWLAxiom> getReferencingAxioms() {
      return referencingAxioms;
   }

   public int compareTo(OWLClassDisplay other) {
      return getDisplayName().compareTo(other.getDisplayName());
   }

   @Override
   public String toString() {
      return classIri.toString();
   }
}
