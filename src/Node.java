public class Node {
    int vertex;
    int degree;
    int saturation;
    int blame;


    Node(int v, int d, int s, int b){
        vertex = v;
        degree = d;
        saturation = s;
        blame = b;
    }

    public void setDegree(int i){
        this.degree = i;
    }

    public int getDegree(){
        return this.degree;
    }

    public int getVertex(){
        return this.vertex;
    }

    public void setSaturation(int s){
        this.saturation = s;
    }
    public int getSaturation(){
        return this.saturation;
    }

    public void setBlame(int s){
        this.blame = s;
    }
    public int getBlame(){
        return this.blame;
    }


}
