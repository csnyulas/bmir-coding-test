package io.github.johardi.bmir.problem1;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import io.github.johardi.bmir.problem1.OWLClassDisplay;
import io.github.johardi.bmir.problem1.OWLClassDisplayImpl;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;

import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class OWLClassDisplayTest {

   @Mock
   private IRI iriA, iriB, iriC, iriD;

   @Mock
   private OWLAxiom ax1, ax2, ax3, ax4, ax5, ax6, ax7;

   private OWLClassDisplay clsA, clsB, clsC, clsD;

   @Before
   public void setup() {
      clsA = new OWLClassDisplayImpl(iriA, "Blastoma", Sets.newSet(ax1));
      clsB = new OWLClassDisplayImpl(iriB, "Lymphoma", Sets.newSet(ax2));
      clsC = new OWLClassDisplayImpl(iriC, "Sarcoma", Sets.newSet(ax3, ax5));
      clsD = new OWLClassDisplayImpl(iriD, "Carcicoma", Sets.newSet(ax4, ax6, ax7));
   }

   @Test
   public void shouldReturnCorrectIRI() {
      assertThat(clsA.getIRI(), is(equalTo(iriA)));
      assertThat(clsB.getIRI(), is(equalTo(iriB)));
      assertThat(clsC.getIRI(), is(equalTo(iriC)));
      assertThat(clsD.getIRI(), is(equalTo(iriD)));
   }

   @Test
   public void shouldReturnCorrectDisplayName() {
      assertThat(clsA.getDisplayName(), is(equalTo("Blastoma")));
      assertThat(clsB.getDisplayName(), is(equalTo("Lymphoma")));
      assertThat(clsC.getDisplayName(), is(equalTo("Sarcoma")));
      assertThat(clsD.getDisplayName(), is(equalTo("Carcicoma")));
   }

   @Test
   public void shouldReturnCorrectReferecingAxioms() {
      assertThat(clsA.getReferencingAxioms(), contains(ax1));
      assertThat(clsB.getReferencingAxioms(), contains(ax2));
      assertThat(clsC.getReferencingAxioms(), contains(ax3, ax5));
      assertThat(clsD.getReferencingAxioms(), contains(ax4, ax6, ax7));
   }

   @Test
   public void shouldOrderByDisplayName() {
      List<OWLClassDisplay> list = Lists.newArrayList(clsA, clsB, clsC, clsD);
      Collections.sort(list);
      assertThat(list, contains(clsA, clsD, clsB, clsC));
   }

   @Test
   public void shouldBePossibleAddIntoHashSet() {
      Set<OWLClassDisplay> set = Sets.newSet(clsA, clsB, clsC, clsD);
      assertThat(set, contains(clsA, clsB, clsC, clsD));
   }
}
