import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Graph {
    int nVertices;
    int nStudents;
    int numberOfStudentsInEachCourse[];
    ArrayList<ArrayList<Integer> > coursesTakenByEachStudent;
    int[][] edges; // kon vertex er shathe kon ta added
    int[] colorMatrix;
    Node[] node;
    boolean[] isAvailable;



    Graph(int i, int j, int b[], ArrayList<ArrayList<Integer> > c){
        nVertices = i;
        nStudents = j;
        numberOfStudentsInEachCourse = b;
        coursesTakenByEachStudent = c;

        edges = new int[nVertices][nVertices];

        for(int m = 0; m < nVertices; m++){
            for(int n = 0; n < nVertices; n++){
                edges[m][n] = 0;
            }
        }
        isAvailable = new boolean[nVertices]; //initially we can color with any color
        for(int g = 0; g < nVertices; g++){
            isAvailable[g] = true; //available
        }

        colorMatrix = new int[nVertices];

        for(int d = 0; d < nVertices; d++){
            colorMatrix[d] = -1;
        }

        node = new Node[nVertices];
        for(int p = 0; p < nVertices; p++){
            node[p] = new Node(p,0,0,0);
        }

    }


    public void createGraph(){
        for(int i = 0; i < nStudents; i++){//for each student
            for(int j = 0; j < coursesTakenByEachStudent.get(i).size(); j++ ){//student er course list
                for(int m = 0; m < coursesTakenByEachStudent.get(i).size(); m++){//full length of first line
                    int vertex1 = coursesTakenByEachStudent.get(i).get(m);
                    for(int n = m+1; n < coursesTakenByEachStudent.get(i).size(); n++){
                        int vertex2 = coursesTakenByEachStudent.get(i).get(n);

                        if (edges[vertex1 - 1][vertex2 - 1] != 1 && edges[vertex2 - 1][vertex1 - 1] != 1) {
                            //if edge already exists, don't increase degree
                            int x = node[vertex1 - 1].getDegree();
                            x = x + 1;
                            node[vertex1 - 1].setDegree(x);

                            int y = node[vertex2 - 1].getDegree();
                            y = y + 1;
                            node[vertex2 - 1].setDegree(y);

                            edges[vertex1 - 1][vertex2 - 1] = 1;
                            edges[vertex2 - 1][vertex1 - 1] = 1;

                        }

                    }
                }
            }
        }

    }




    //Solution steps

    class descendingOrder implements Comparator<Node>
    {
        // Used for sorting in descending order

        public int compare(Node a, Node b)
        {
            return b.getDegree()-a.getDegree();
        }
    }


    public float[] largestDegreeAlgo(){
        float result[] = new float[3];
        ArrayList<Node> nodes = new ArrayList<>();

        for(int i = 0; i < nVertices; i++){
            nodes.add(node[i]);
        }
        //sort the degree
        Collections.sort(nodes, new descendingOrder());
        //sorted

        Node first = nodes.get(0);


        //color the first vertex
        colorMatrix[first.getVertex()] = 0;


        int vertex;

        int counter = 1; //how  many colors are used

        for(int a = 1; a < nVertices; a++){//for all the other vertices of the list

            Node next = nodes.get(a);//choose the next high degree node

            //check if it's neighbours are colored, if colored, make those colors unavailable
            vertex = next.getVertex();
            for(int i = 0; i < nVertices; i++) { //for each column in the vertex'th row in matrix
                if (edges[vertex][i] == 1) { //check if adjacent vertices have color, now i is the adjacent vertex
                    if (colorMatrix[i] != -1) { //yes? make the color unavailable
                        isAvailable[colorMatrix[i]] = false;
                    }
                }
            }


            //assign a color to this vertex
            for(int s = 0; s < nVertices; s++){
                if(isAvailable[s] == true){
                    colorMatrix[vertex] = s;
                    break;
                }
            }


            //now the next vertex can get any color they want, so make all the colors available again
            for(int u = 0; u < nVertices; u++){
                isAvailable[u] = true;
            }

        }
        int temp[] = new int[nVertices];
        for(int i = 0; i < nVertices; i++){
            temp[i] = colorMatrix[i];
        }
        //find how many unique elements are in the color matrix
        Arrays.sort(temp);

        for(int i = 0; i < nVertices-1; i++){
            if(temp[i] != temp[i+1]){
                counter+=1;
            }
        }

        result[0] = counter;
        result[1] = (float)findPenalty(colorMatrix) / (float)nStudents;
        result[2] = (float)findPenaltyAllPairs(colorMatrix) / (float)nStudents;
        return result;

    }

    class saturation implements Comparator<Node>
    {
        // Used for sorting in descending order

        public int compare(Node a, Node b)
        {
            if(b.getSaturation() - a.getSaturation() == 0){
                return b.getDegree() - a.getDegree();
            }
            return b.getSaturation() - a.getSaturation();

        }
    }

    public float[] dsatur(String fileName){
        float result[] = new float[3];
        try {

            String[] name = fileName.split("\\.");
            String base = name[0] + ".sol";

            Files.deleteIfExists(Paths.get(base));
            File solFile = new File(base);
            FileWriter fileWriter = new FileWriter(solFile, true);

            for (int g = 0; g < nVertices; g++) {
                isAvailable[g] = true; //available
            }

            for (int d = 0; d < nVertices; d++) {
                colorMatrix[d] = -1;
            }




            //choose the highest degree
            ArrayList<Node> nodes = new ArrayList<>();

            for (int i = 0; i < nVertices; i++) {
                nodes.add(node[i]);
            }
            //sort the degree
            Collections.sort(nodes, new descendingOrder());
            //sorted

            Node first = nodes.get(0);


            //color the first vertex
            colorMatrix[first.getVertex()] = 0;

            //highest degree chosen and colored

            int vertex;
            int counter = 1; //how  many colors are used
            nodes.remove(0);


            while (nodes.size() != 0) {
                setSaturation(nodes);

                Collections.sort(nodes, new saturation());


                vertex = nodes.get(0).getVertex();
                //assign a color
                for (int i = 0; i < nVertices; i++) { //for each column in the vertex'th row in matrix
                    if (edges[vertex][i] == 1) { //check if adjacent vertices have color, now i is the adjacent vertex
                        if (colorMatrix[i] != -1) { //yes? make the color unavailable
                            isAvailable[colorMatrix[i]] = false;
                        }
                    }
                }


                //assign a color to this vertex
                for (int s = 0; s < nVertices; s++) {
                    if (isAvailable[s] == true) {
                        colorMatrix[vertex] = s;
                        break;
                    }

                }

                //now the next vertex can get any color they want, so make all the colors available again
                for (int u = 0; u < nVertices; u++) {
                    isAvailable[u] = true;
                }

                nodes.remove(0);

            }

            int temp[] = new int[nVertices];
            for (int i = 0; i < nVertices; i++) {
                temp[i] = colorMatrix[i];
            }
            //find how many unique elements are in the color matrix
            Arrays.sort(temp);

            for (int i = 0; i < nVertices - 1; i++) {
                if (temp[i] != temp[i + 1]) {
                    counter += 1;
                }
            }
            fileWriter.write("Course Timeslot \n");
            for (int i = 0; i < nVertices; i++) {
                fileWriter.write((i + 1) + " " + colorMatrix[i] + "\n");
            }

            fileWriter.close();

            result[0] = counter;
            result[1] = (float) findPenalty(colorMatrix) / (float) nStudents;
            result[2] = (float) findPenaltyAllPairs(colorMatrix) / (float) nStudents;

        }catch (IOException e){
            //
        }
        return result;
    }


    public void setSaturation(ArrayList<Node> nodes){

        HashMap<Integer, Integer> unique = new HashMap<Integer, Integer>();
        for(int i = 0; i < nodes.size(); i++){//all nodes
           int vertex = nodes.get(i).getVertex();
           for(int j = 0; j < nVertices; j++){//check adjacent list of vertex
               if(edges[vertex][j] == 1){//if there is an edge
                   if(colorMatrix[j] != -1){ //the other vertex is colored
                       unique.put(colorMatrix[j], j);
                   }
               }
           }//one vertex done
           nodes.get(i).setSaturation(unique.size());
           unique.clear();
       }//all vertices done
    }

    public float kempeChain(String s){
        float penalty = 0;
        int[] kempeColor = new int[nVertices];
        int oldFirstVertex = -1;
        int oldSecondVertex = -1;

        //first copy the color matrix, cz you don't know if you'll be taking this graph

        for(int a = 0; a < 10000; a++) {//run the kempe chain 10000 times

            for (int i = 0; i < nVertices; i++) {
                kempeColor[i] = colorMatrix[i];
            }

            //randomly choose a vertex v

            Random rand = new Random(); //instance of random class
            int highest = nVertices;
            //generate random values from 0-nVertices-1
            int firstVertex = rand.nextInt(highest);
            int firstColor = kempeColor[firstVertex];
            int secondColor = 0;


            //for j randomly choose one of its adjacent edges

            int adjacentListLength = 0;
            for(int i = 0; i < nVertices; i++){
                if(edges[firstVertex][i] == 1){
                    adjacentListLength+=1;
                }
            }

            if(adjacentListLength == 0){
                continue;
            }


            int upperbound = adjacentListLength; //3 mane upperbound er shathe already 1 plus
            int adjacent = rand.nextInt(upperbound) + 1; //0,1 2 er bodole 1,2,3 ashbe

            adjacentListLength = 0;
            int secondVertex = -1;

            for(int i = 0; i < nVertices; i++){
                if(edges[firstVertex][i] == 1){
                    adjacentListLength+=1;
                    if(adjacentListLength == adjacent){
                        secondVertex = i;
                        secondColor = kempeColor[i];
                        break;
                    }
                }
            }

            //same color er kempe chain onno jaygay pete pari, same vertex por por pele jhamela

            if(firstVertex == oldFirstVertex && secondVertex == oldSecondVertex){
                continue;
            }else{
                oldFirstVertex = firstVertex;
                oldSecondVertex = secondVertex;
            }

//            //run bfs and make sure you alternate the main vertex and copy the taken vertices in an arraylist
//            //bfs
//            //from 2-1

            int[] colorBFS = new int[nVertices];
            for (int i = 0; i < nVertices; i++) { //assign all the colors white
                colorBFS[i] = 0;//white
            }

            ArrayList<Integer> kempeVertices = new ArrayList<>();

            colorBFS[firstVertex] = 1;//grey
            Queue<Integer> q = new LinkedList<>();
            q.add(firstVertex);
            kempeVertices.add(firstVertex);
            int u;
            int transitionColor;

            while(!q.isEmpty()){
                u = q.remove();

                if(kempeColor[u] == firstColor){
                    transitionColor = secondColor;
                }else{
                    transitionColor = firstColor;
                }

                for(int i = 0; i< nVertices; i++){
                    if( edges[u][i] == 1 && colorBFS[i] == 0 && kempeColor[i] == transitionColor){
                        colorBFS[i] = 1;
                        q.add(i);
                        kempeVertices.add(i);
                    }
                }
                colorBFS[u] = 2;
            }

            //now swap the colors
            for(int i = 0; i < kempeVertices.size(); i++){
                if(kempeColor[kempeVertices.get(i)] == firstColor){
                    kempeColor[kempeVertices.get(i)] = secondColor;
                }else{
                    kempeColor[kempeVertices.get(i)] = firstColor;
                }
            }
            float oldPenalty = 0;
            float newPenalty = 0;
            if(s.equalsIgnoreCase("All")) {
                oldPenalty = (float) findPenaltyAllPairs(colorMatrix) / (float) nStudents;
                newPenalty = (float) findPenaltyAllPairs(kempeColor) / (float) nStudents;
            }else if(s.equalsIgnoreCase("Consecutive")){
                oldPenalty = (float) findPenalty(colorMatrix) / (float) nStudents;
                newPenalty = (float) findPenalty(kempeColor) / (float) nStudents;
            }


            if(newPenalty < oldPenalty){//this one's better
                for(int i = 0; i < nVertices; i++){
                    colorMatrix[i] = kempeColor[i];

                }
                penalty = newPenalty;
            }else{
                penalty = oldPenalty;
            }


        }
        return penalty;
    }


    public float pairSwap(String s){

        float penalty = 0;
        int oldFirstVertex = -1;
        int oldSecondVertex = -1;
        int[] kempeColor = new int[nVertices];
        int[] temp = new int[nVertices];
        for (int i = 0; i < nVertices; i++) {
            temp[i] = colorMatrix[i];
        }


        for(int n = 0; n < 100; n++) {
            //loop through all the vertices 100 times
            for (int m = 0; m < nVertices; m++) {
                for (int a = m + 1; a < nVertices; a++) {

                    for (int i = 0; i < nVertices; i++) {
                        kempeColor[i] = temp[i];
                    }

                    /*......................................................*/

                    int firstVertex = m;
                    int secondVertex = a;
                    int firstColor = kempeColor[firstVertex];
                    int secondColor = kempeColor[secondVertex];

                    //first check duita same color vertex ki na
                    //othoba duita same random number generate hoise ki na
                    //dui khetrei ar agabo na
                    if (firstColor == secondColor || firstVertex == secondVertex) {
                        continue;
                    }

                    //na hole check same pair ager step eo paiso ki na
                    if (firstVertex == oldFirstVertex && secondVertex == oldSecondVertex) {
                        continue;
                    } else {
                        oldFirstVertex = firstVertex;
                        oldSecondVertex = secondVertex;
                    }

                    //check if each contain the other color in their adjacents

                    //first vertex
                    int k = 0; //free to swap second vertex
                    for (int i = 0; i < nVertices; i++) {
                        if (edges[firstVertex][i] == 1 && kempeColor[i] == secondColor && i != secondVertex) {
                            k = 1; // adjacent list a first color already ase, can't swap
                            break;
                        }
                    }

                    //second vertex
                    int l = 0; //free to swap second vertex
                    if (k == 0) {
                        for (int i = 0; i < nVertices; i++) {
                            if (edges[secondVertex][i] == 1 && kempeColor[i] == firstColor && i != firstVertex) {
                                l = 1; // adjacent list a first color already ase, can't swap
                                break;
                            }
                        }
                    }

                    /*......................................................*/

                    // k ar l 0 hole swap
                    if (k == 0 && l == 0) {
                        kempeColor[firstVertex] = secondColor; //first ta ke 2nd tar color dao
                        kempeColor[secondVertex] = firstColor; //2nd ta ke first tar color dao

                        //after swap, find penalty

                        float oldPenalty = 0;
                        float newPenalty = 0;
                        if (s.equalsIgnoreCase("All")) {
                            oldPenalty = (float) findPenaltyAllPairs(temp) / (float) nStudents;
                            newPenalty = (float) findPenaltyAllPairs(kempeColor) / (float) nStudents;
                        } else if (s.equalsIgnoreCase("Consecutive")) {
                            oldPenalty = (float) findPenalty(temp) / (float) nStudents;
                            newPenalty = (float) findPenalty(kempeColor) / (float) nStudents;
                        }


                        if (newPenalty < oldPenalty) {//this one's better
                            for (int i = 0; i < nVertices; i++) {
                                temp[i] = kempeColor[i];
                            }
                            penalty = newPenalty;
                        } else {
                            penalty = oldPenalty;
                        }

                    }

                }
            }
        }

        return penalty;
    }

    class blame implements Comparator<Node>
    {
        // Used for sorting in descending order

        public int compare(Node a, Node b)
        {
            if(b.getBlame() - a.getBlame() == 0){
                return b.getDegree() - a.getDegree();
            }
            return b.getBlame() - a.getBlame();

        }
    }

    public float[] SWO(){
        float[] result = new float[3];
        for (int g = 0; g < nVertices; g++) {
            isAvailable[g] = true; //available
        }
        //i have a color matrix value after running largest degree

        for(int k = 0; k < 100; k++) {
            ArrayList<Node> nodes = new ArrayList<>();
            for(int i  = 0; i < nVertices; i++){
                nodes.add(node[i]);

            }

            //i have to assign high blame to the high colors
            //analyzer
            setBlame(nodes);

            //then run constructive heuristic again for a certain amount of time
            //and keep changing the colors

            //prioritizer
            Collections.sort(nodes, new blame());

            for(int i =  0; i < nVertices; i++){
                colorMatrix[i] = -1;
            }
            colorMatrix[nodes.get(0).getVertex()] = 0;
            nodes.remove(0);

            while(nodes.size() != 0) {

                int vertex = nodes.get(0).getVertex();
                //assign a color
                for (int i = 0; i < nVertices; i++) { //for each column in the vertex'th row in matrix
                    if (edges[vertex][i] == 1) { //check if adjacent vertices have color, now i is the adjacent vertex
                        if (colorMatrix[i] != -1) { //yes? make the color unavailable
                            isAvailable[colorMatrix[i]] = false;
                        }
                    }
                }


                //assign a color to this vertex
                for (int s = 0; s < nVertices; s++) {
                    if (isAvailable[s] == true) {
                        colorMatrix[vertex] = s;
                        break;
                    }

                }

                //now the next vertex can get any color they want, so make all the colors available again
                for (int u = 0; u < nVertices; u++) {
                    isAvailable[u] = true;
                }
                nodes.remove(0);
            }
        }

        int temp[] = new int[nVertices];
        for (int i = 0; i < nVertices; i++) {
            temp[i] = colorMatrix[i];
        }
        int counter = 1;
        //find how many unique elements are in the color matrix
        Arrays.sort(temp);

        for (int i = 0; i < nVertices - 1; i++) {
            if (temp[i] != temp[i + 1]) {
                counter += 1;
            }
        }

        result[0] = counter;
        result[1] = (float) findPenalty(colorMatrix) / (float) nStudents;
        result[2] = (float) findPenaltyAllPairs(colorMatrix) / (float) nStudents;

        return  result;
    }

    public void setBlame(ArrayList<Node> nodes){
        for(int i = 0; i < nodes.size(); i++){
            nodes.get(i).setBlame(colorMatrix[nodes.get(i).getVertex()]);
        }
    }


    public int findPenalty(int[] colorMatrix){
        int totalPenalty = 0;
        for(int i = 0; i < nStudents; i++){
            int penalty = 0;
            int temp[] = new int[coursesTakenByEachStudent.get(i).size()];

            //make an array of student timetable
            for(int j = 0; j < coursesTakenByEachStudent.get(i).size(); j++){//first line
                temp[j] = colorMatrix[coursesTakenByEachStudent.get(i).get(j) - 1];
            }

            Arrays.sort(temp);

            for(int j = 0; j < temp.length -1; j++){
                int a = temp[j];
                int b = temp[j+1];
                if(b-a == 1){
                    penalty+=16;
                }
                else if (b-a == 2){
                    penalty+=8;
                }
                else if(b-a == 3){
                   penalty+=4;
                }
                else if(b-a == 4){
                    penalty+=2;
                }
                else if(b-a == 5){
                    penalty+=1;
                }
                else{
                    penalty+=0;
                }
            }

            totalPenalty+=penalty;
        }
        return totalPenalty;
    }

    public int findPenaltyAllPairs(int[] colorMatrix){
        int totalPenalty = 0;

        for(int i = 0; i < nStudents; i++){
            int penalty = 0;
            int temp[] = new int[coursesTakenByEachStudent.get(i).size()];

            //make an array of student timetable
            for(int j = 0; j < coursesTakenByEachStudent.get(i).size(); j++){//first line
                temp[j] = colorMatrix[coursesTakenByEachStudent.get(i).get(j) - 1];
            }

            Arrays.sort(temp);
//            System.out.println("Student: " + (i+1));
//            for(int p = 0; p <coursesTakenByEachStudent.get(i).size(); p++ ){
//                System.out.print(temp[p] + " ");
//            }
//            System.out.println();

            for(int j = 0; j < temp.length; j++) {
                for (int k = j + 1; k < temp.length; k++) {
                    int a = temp[j];
                    int b = temp[k];
                    if (b - a == 1) {
                        penalty += 16;
                    } else if (b - a == 2) {
                        penalty += 8;
                    } else if (b - a == 3) {
                        penalty += 4;
                    } else if (b - a == 4) {
                        penalty += 2;
                    } else if (b - a == 5) {
                        penalty += 1;
                    } else {
                        penalty += 0;
                    }
                }


            }
            totalPenalty += penalty;
        }

        return totalPenalty;
    }



}
