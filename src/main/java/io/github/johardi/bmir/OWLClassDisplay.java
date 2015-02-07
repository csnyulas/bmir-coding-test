package io.github.johardi.bmir;

import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;

public interface OWLClassDisplay extends Comparable<OWLClassDisplay> {

   /**
    * Gets the IRI of this class.
    * @return The {@link IRI} for this class.
    */
   IRI getIRI();

   /**
    * Gets the display name.
    * @return A string representing the human readable name for this class.
    */
   String getDisplayName();

   /**
    * Gets all axioms that reference the class.
    * @return A set of {@link OWLAxiom} objects.
    */
   Set<OWLAxiom> getReferencingAxioms();
}
