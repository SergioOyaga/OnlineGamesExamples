package org.soyaga.examples.Zip.ACO.Evaluable;

import lombok.AllArgsConstructor;
import org.soyaga.aco.Evaluable.Feasibility.FeasibilityFunction;
import org.soyaga.aco.Solution;
import org.soyaga.aco.world.Graph.Elements.Edge;
import org.soyaga.aco.world.Graph.Elements.Node;
import org.soyaga.aco.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class is responsible for providing an 'evaluate' method to compute the feasibility of a solution.
 * Specifically, it evaluates the feasibility of a solution.
 */
@AllArgsConstructor
public class ZipFeasibilityFunction implements FeasibilityFunction {
    /**
     * Integer with the number of rows in the problem
     */
    private Integer rowSize;
    /**
     * Integer with the number of cols in the problem
     */
    private Integer colSize;
    /**
     * HashSet with the fixed cells
     */
    private ArrayList<Node> priorityNodes;
    /**
     * HashMap with the priority by node.
     */
    private HashMap<Node, Integer> priorityByNode;

    // Uses merge sort to count inversions efficiently in O(n log n)
    private int countInversions(ArrayList<Node> list) {
        return 10*mergeSortAndCount(list, 0, list.size() - 1);
    }

    private int mergeSortAndCount(ArrayList<Node> arr, int left, int right) {
        if (left >= right) return 0;

        int mid = (left + right) / 2;
        int count = mergeSortAndCount(arr, left, mid);
        count += mergeSortAndCount(arr, mid + 1, right);
        count += merge(arr, left, mid, right);

        return count;
    }

    private int merge(ArrayList<Node> arr, int left, int mid, int right) {
        ArrayList<Node> temp = new ArrayList<>();
        int i = left, j = mid + 1, count = 0;

        while (i <= mid && j <= right) {
            if (this.priorityByNode.get(arr.get(i)) <= this.priorityByNode.get(arr.get(j))) {
                temp.add(arr.get(i++));
            } else {
                temp.add(arr.get(j++));
                count += (mid - i + 1); // Inversion count
            }
        }

        while (i <= mid) temp.add(arr.get(i++));
        while (j <= right) temp.add(arr.get(j++));

        for (int k = left; k <= right; k++) {
            arr.set(k, temp.get(k - left));
        }

        return count;
    }

    /**
     * This function computes the value of the evaluable object.
     *
     * @param world           world Object containing the "Graph" and "PheromoneContainer" information.
     * @param solution        Solution object to evaluate.
     * @param feasibilityArgs VarArgs containing the additional information needed to evaluate.
     * @return A double containing the value of the evaluation.
     */
    @Override
    public Double evaluate(World world, Solution solution, Object... feasibilityArgs) {
        ArrayList<Object> solutionNodes = solution.getNodesVisited();
        double feasibility = 0.;
        // Check start and end
        if(solutionNodes.get(0) != this.priorityNodes.get(0)){
            feasibility++;
        }
        if(solutionNodes.get(this.rowSize*this.colSize-1) != this.priorityNodes.get(this.priorityNodes.size()-1)){
            feasibility++;
        }
        // Check of the path and cell connection.
        ArrayList<Node> priorityActualOrder = new ArrayList<>();
        for(int i=0; i< solutionNodes.size()-1; i++){
            Node currentNode = (Node) solutionNodes.get(i);
            Node nextNode = (Node) solutionNodes.get(i+1);
            // Store the actual order of the priority cells.
            if (this.priorityNodes.contains(currentNode)) priorityActualOrder.add(currentNode);
            if(i==solutionNodes.size()-2 && this.priorityNodes.contains(nextNode)) priorityActualOrder.add(nextNode);
            //Check continuity
            boolean nextNotFound = true;
            for(Edge edge:currentNode.getOutputEdges()){
                if (edge.getDestination() == nextNode) {
                    nextNotFound = false;
                    break;
                }
            }
            if(nextNotFound) feasibility++;
        }
        // Count the number of inversions to match the priority order
        feasibility += countInversions(priorityActualOrder);
        //Check that all cells are used
        feasibility += Math.abs((this.rowSize*this.colSize - new HashSet<>(solutionNodes).size()));
        return feasibility;
    }
}