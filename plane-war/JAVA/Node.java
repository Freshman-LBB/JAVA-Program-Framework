import java.awt.Graphics2D;
public class Node {
    protected boolean state;
    private Node proPosition;
    protected int getStrength(){return 0;}
    private Node nextPosition;
    protected CircularDoubleLinkedListWithFreeNode getNowWeapon(){return null;}
    protected int checkCollision(int x,int y,int width,int height,int strength){return 0;};
    protected void forward(int x,int y) {}
    protected void activate(int x,int y) { this.state = true;}
    protected void deactivate() { this.state = false; }
    public boolean checkAlive(){return this.state;}
    public Node(){
        state=false;
        proPosition = null;
        nextPosition = null;
    }
    protected int [] getNowPos(){return null;}
    protected void print(Graphics2D g2d) {}
    protected Node(Node T) 
    {
        this.state = T.state;
        this.proPosition = T.getProPosition();
        this.nextPosition = T.getNextPosition();
    }
    protected Node copy(){
        return new Node(this);
    }
    public Node getProPosition() {
        return proPosition;
    }
    public Node getNextPosition() {
        return nextPosition;
    }
    public void setNextPosition(Node next) 
    {
        nextPosition = next;
        if (next != null) 
        {
            next.setProPosition(this);
        }
    }
    public void setProPosition(Node pro) 
    {
        proPosition = pro;
    }
}
class NodeList{
    private Node nowPos;
    public void forward(int times)
    {
        for(int i=0;i<times;i++)
        {
            nowPos.forward(0,0);
            moveToNext();
        }
    }
    public int checkCollision(int times,int x,int y,int width,int height,int strength)
    {
        int a=0;
        for(int i=0;i<times;i++)
        {
            a= nowPos.checkCollision(x,y,width,height,strength);
            moveToNext();
            if(a!=0)return a;
        }
        return a;
    }
    public NodeList()
    {
        nowPos=null;
    }
    public NodeList(Node T)
    {
        nowPos = T.copy();
    }
    public Node moveToNext() 
    {
        if (nowPos.getNextPosition()!= null) 
        {
            nowPos=nowPos.getNextPosition();
        }
        return nowPos;
    }
    public Node moveToPro() 
    {
        if (nowPos.getProPosition() != null) 
        {
            nowPos=nowPos.getProPosition();
        }
        return nowPos;
    }
    public void setProPosition(Node pro) 
    {
        nowPos.setProPosition(pro);
        if (pro != null) 
        {
            pro.setNextPosition(nowPos);
        }
    }
    public void setNextPosition(Node next) 
    {
        nowPos.setNextPosition(next);
        if (next != null) 
        {
            next.setProPosition(nowPos);
        }
    }
    public Node getNowPosition() {
        return nowPos;
    }
    public Node getNextPosition() {
        return nowPos.getNextPosition();
    }
    public Node getProPosition() {
        return nowPos.getProPosition();
    }
    public void setNowPos(Node T)
    {
        nowPos=T;
    }
    public int [] getNowPos()
    {
        return nowPos.getNowPos();
    }
    public int getStrength()
    {
        return nowPos.getStrength();
    }
}