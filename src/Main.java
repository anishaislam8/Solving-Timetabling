import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String[] crs = new String[5];
        crs[0] = "car-f-92.crs";
        crs[1] = "car-s-91.crs";
        crs[2] = "kfu-s-93.crs";
        crs[3] = "tre-s-92.crs";
        crs[4] = "yor-f-83.crs";
        String[] stu = new String[5];
        stu[0] = "car-f-92.stu";
        stu[1] = "car-s-91.stu";
        stu[2] = "kfu-s-93.stu";
        stu[3] = "tre-s-92.stu";
        stu[4] = "yor-f-83.stu";

        //Process course file
        for (int q = 0; q < 5; q++) {
            //File courseFile = new File("yor-f-83.crs");
            File courseFile = new File(crs[q]);

            //read the course file and find out the courses and number of students enrolled in them

            Scanner in = new Scanner(courseFile);

            int number_of_courses = 0;

            while (in.hasNextLine()) {
                String s = in.nextLine(); //next line na porle count barbe kamne
                number_of_courses += 1;

            }

            int[] numberOfStudentsInEachCourse = new int[number_of_courses];
            String eachLineInCourseFile;
            int i = 0;


            while (in.hasNextLine()) {
                eachLineInCourseFile = in.nextLine();
                String[] eachLine = eachLineInCourseFile.split(" ");
                numberOfStudentsInEachCourse[i] = Integer.parseInt(eachLine[1]);
                i += 1;

            }


            //done with course file


            //Process student file
            //File studentFile = new File("yor-f-83.stu");
            File studentFile = new File(stu[q]);

            //read the student file and find out the courses and number of students enrolled in them


            Scanner student = new Scanner(studentFile);
            int number_of_students = 0;

            while (student.hasNextLine()) {
                String s = student.nextLine(); //next line na porle count barbe kamne
                number_of_students += 1;

            }


            ArrayList<ArrayList<Integer>> coursesTakenByEachStudent =
                    new ArrayList<ArrayList<Integer>>(number_of_students);


            String eachLineInStudentFile;


            student = new Scanner(studentFile);

            while (student.hasNextLine()) {//for each student
                //System.out.println("student: " + f);
                ArrayList<Integer> temp = new ArrayList<>();

                eachLineInStudentFile = student.nextLine();
                String[] eachLine = eachLineInStudentFile.split(" ");

                int counter = eachLine.length;

                for (int j = 0; j < counter; j++) {
                    temp.add(Integer.parseInt(eachLine[j]));

                }

                coursesTakenByEachStudent.add(temp);

            }


            //create a graph
            Graph g = new Graph(number_of_courses, number_of_students, numberOfStudentsInEachCourse, coursesTakenByEachStudent);
            g.createGraph();


            //largest degree

            float[] answer = new float[3];

            answer = g.largestDegreeAlgo();

            System.out.println(crs[q] + " and " + stu[q] + " : ");
            System.out.println();

            System.out.println("Largest Degree: ");
            System.out.println();
            System.out.println("Timeslot: " + answer[0]);
            System.out.println("Penalty: " + answer[1]);
            System.out.println("Penalty (All Pairs) : " + answer[2]);





            float penalty = g.kempeChain("All");
            System.out.println("Penalty (All Pairs) After Kempe Chain : " + penalty);

            g.largestDegreeAlgo();
            float penalty2 = g.kempeChain("Consecutive");
            System.out.println("Penalty (Consecutive Pairs) After Kempe Chain : " + penalty2);

            answer = g.SWO();
            System.out.println();
            System.out.println("SWO: ");
            System.out.println();
            System.out.println("Timeslot: " + answer[0]);
            System.out.println("Penalty: " + answer[1]);
            System.out.println("Penalty (All Pairs) : " + answer[2]);



            g.largestDegreeAlgo();
            penalty = g.pairSwap("All");
            System.out.println("Penalty (All Pairs) After Pair Swap : " + penalty);

            penalty2 = g.pairSwap("Consecutive");
            System.out.println("Penalty (Consecutive Pairs) After Pair Swap : " + penalty2);

            System.out.println();
            System.out.println();

            System.out.println();
            System.out.println();





            //dsatur


            answer = g.dsatur(crs[q]);

            //System.out.println("yor-f-83.crs and yor-f-83.stu: ");
            //System.out.println(crs[q] + " and " + stu[q] + " : ");
            System.out.println("Dsatur: ");
            System.out.println();
            System.out.println("Timeslot: " + answer[0]);
            System.out.println("Penalty: " + answer[1]);
            System.out.println("Penalty (All Pairs) : " + answer[2]);

            penalty = g.kempeChain("All");
            System.out.println("Penalty (All Pairs) After Kempe Chain : " + penalty);

            g.dsatur(crs[q]);
            penalty2 = g.kempeChain("Consecutive");
            System.out.println("Penalty (Consecutive Pairs) After Kempe Chain : " + penalty2);


            g.dsatur(crs[q]);
            penalty = g.pairSwap("All");
            System.out.println("Penalty (All Pairs) After Pair Swap : " + penalty);

            penalty2 = g.pairSwap("Consecutive");
            System.out.println("Penalty (Consecutive Pairs) After Pair Swap : " + penalty2);






            System.out.println();
            System.out.println();
            System.out.println();



        }

    }
}
