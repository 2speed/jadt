package griz.jadt.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests the functionality of the {@link DirectedGraph} class.
 * 
 * @author nichollsmc
 */
class DirectedGraphTest {

    @Test
    void should_add_edge() {
        final var graph = new DirectedGraph<Integer, Edge<Integer>>();

        graph.addEdge(new SimpleEdge<>(1, 2));
        graph.addEdge(new SimpleEdge<>(2, 3));
        graph.addEdge(new SimpleEdge<>(2, 4));
        graph.addEdge(new SimpleEdge<>(4, 5));

        assertTrue(graph.containsVertex(5));
        assertTrue(graph.containsEdge(1, 2));
        assertFalse(graph.containsEdge(2, 1));
        assertEquals(5, graph.numVertices());
    }

    @Test
    void should_remove_edge() {
        final var graph = new DirectedGraph<Integer, Edge<Integer>>();

        final var oneTwo   = new SimpleEdge<>(1, 2);
        final var twoThree = new SimpleEdge<>(2, 3);
        final var twoFour  = new SimpleEdge<>(2, 4);
        final var fourFive = new SimpleEdge<>(4, 4);

        graph.addEdge(oneTwo);
        graph.addEdge(twoThree);
        graph.addEdge(twoFour);
        graph.addEdge(fourFive);

        assertTrue(graph.removeEdge(oneTwo));
        assertFalse(graph.containsEdge(1, 2));
        assertFalse(graph.edgesFrom(1).hasNext());

        graph.addEdge(oneTwo);

        assertTrue(graph.containsEdge(1, 2));
        assertTrue(graph.edgesFrom(1).hasNext());
    }

    @Test
    void should_add_vertex() {
        final var graph = new DirectedGraph<Character, Edge<Character>>();

        assertTrue(graph.addVertex('a'));
        assertTrue(graph.addVertex('b'));
        assertTrue(graph.addVertex('c'));
        assertFalse(graph.addVertex('b'));
        assertFalse(graph.containsVertex('d'));
    }

    @Test
    void should_remove_vertices_without_edges() {
        final var graph = new DirectedGraph<Character, Edge<Character>>();

        graph.addVertex('a');
        graph.addVertex('b');
        graph.addVertex('c');

        assertEquals(3, graph.numVertices());
        assertTrue(graph.removeVertex('a'));
        assertFalse(graph.containsVertex('a'));
        assertEquals(2, graph.numVertices());
    }

    @Test
    void should_remove_vertices_with_edges() {
        final var graph = new DirectedGraph<Integer, Edge<Integer>>();

        final var oneTwo   = new SimpleEdge<>(1, 2);
        final var twoThree = new SimpleEdge<>(2, 3);
        final var twoFour  = new SimpleEdge<>(2, 4);
        final var fourFive = new SimpleEdge<>(4, 5);

        graph.addEdge(oneTwo);
        graph.addEdge(twoThree);
        graph.addEdge(twoFour);
        graph.addEdge(fourFive);

        graph.removeVertex(2);

        assertFalse(graph.edgesFrom(2).hasNext());
        assertFalse(graph.edgesFrom(1).hasNext());
        assertFalse(graph.edgesTo(3).hasNext());
        assertFalse(graph.edgesTo(4).hasNext());
    }

    @Test
    void should_create_edge_between_vertices() {
        final var graph = new DirectedGraph<Integer, Edge<Integer>>();

        graph.addVertex(0);
        graph.addVertex(6);
        graph.addEdge(new SimpleEdge<>(0, 6));

        assertTrue(graph.containsEdge(0, 6));
    }

    @Test
    void shouldRetrieveEdge() {
        final var graph = new DirectedGraph<Character, Edge<Character>>();

        final var edge = new SimpleEdge<>('e', 'f');

        graph.addEdge(edge);

        assertEquals(edge, graph.getEdge(edge.source(), edge.destination()));
    }

    @Test
    void should_throw_graph_exception() {
        final var graph = new DirectedGraph<Integer, Edge<Integer>>();

        graph.addEdge(new SimpleEdge<>(7, 8));
        graph.addEdge(new SimpleEdge<>(8, 9));
        graph.addEdge(new SimpleEdge<>(8, 7));

        assertThrows(GraphException.class, graph::topologicalOrder);
    }

    @Test
    @SuppressWarnings("unchecked")
    void cycle_exception_should_contain_directed_cycle() {
        final var graph = new DirectedGraph<Integer, Edge<Integer>>();

        final var sevenEight = new SimpleEdge<>(7, 8);
        final var eightNine  = new SimpleEdge<>(8, 9);
        final var eightSeven = new SimpleEdge<>(8, 7);

        graph.addEdge(sevenEight);
        graph.addEdge(eightNine);
        graph.addEdge(eightSeven);

        List<Edge<Integer>> cycle = null;
        try {
            graph.topologicalOrder();
        } catch (GraphException ge) {
            cycle = (List<Edge<Integer>>) ge.getCycle();
        }

        assertNotNull(cycle);
        assertEquals(1, cycle.size());
        assertEquals(eightSeven, cycle.get(0));
    }

    @Test
    void should_return_topological_order() {
        final var graph = new DirectedGraph<Character, Edge<Character>>();

        graph.addEdge(new SimpleEdge<>('a', 'b'));
        graph.addEdge(new SimpleEdge<>('b', 'd'));
        graph.addEdge(new SimpleEdge<>('d', 'c'));
        graph.addEdge(new SimpleEdge<>('c', 'e'));
        graph.addEdge(new SimpleEdge<>('e', 'z'));

        final var topologicalOrder = graph.topologicalOrder();

        assertEquals(6, topologicalOrder.size());

        final var expected = Arrays.asList('a', 'b', 'd', 'c', 'e', 'z');

        assertEquals(expected, topologicalOrder);
    }
}