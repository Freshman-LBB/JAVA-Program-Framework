import java.awt.*;

public class CircularDoubleLinkedListWithFreeNode{
    protected NodeList runList;
    protected NodeList freeList;
    protected int freeNum;
    protected int runNum;

    public int checkCollision(int x,int y,int width,int height,int strength)
    {
      if(runList!=null)
      {
        int a=runList.checkCollision(runNum,x,y,width,height,strength);
        checkAlive();
        return a;
      }
      return 0;
    }

    protected void forward() 
    {
      if(runList!=null)
      {
        runList.forward(runNum);
        checkAlive();
      }
    }
    public void takedamage() {}
    public void damage() {}
    public void collision() {}
    public CircularDoubleLinkedListWithFreeNode(int num,Node T)
    {
      // runList=new NodeList();
      freeList=new NodeList(T);

      if (num==1) 
      {
        freeList.setNextPosition(freeList.getNowPosition());
      }else
      {
        Node init = freeList.getNowPosition();
        for(int i =0;i<num-1;i++)
        {
          freeList.setNextPosition(T.copy());
          freeList.moveToNext();
        }
        freeList.setNextPosition(init);
        freeList.moveToNext();
      }
      freeNum=num;
      runNum=0;
    }
    // 添加节点
    public void activated(int x,int y) 
    {
      if (freeNum>3) 
      {
        if (runNum==0) 
        {
          Node temp = freeList.getNowPosition();
          freeList.moveToPro();
          freeList.setNextPosition(temp.getNextPosition());
          freeNum--;
          temp.activate(x,y);
          runList = new NodeList();
          runList.setNowPos(temp);
          runList.setNextPosition(temp);
          runNum++;
        }else
        {
          Node temp = freeList.getNowPosition();
          freeList.moveToPro();
          freeList.setNextPosition(temp.getNextPosition());
          freeNum--;
          temp.activate(x,y);
          temp.setNextPosition(runList.getNextPosition());
          runList.setNextPosition(temp);
          runNum++;
        }
      }else
      {
        if (runNum==0) 
        {
          Node temp = freeList.getNowPosition();
          freeList.moveToPro();
          freeList.setNextPosition(temp.getNextPosition());
          freeNum--;
          temp.activate(x,y);
          freeListAdd();
          runList = new NodeList();
          runList.setNowPos(temp);
          runList.setNextPosition(temp);
          runNum++;
        }else
        {
          Node temp = freeList.getNowPosition();
          freeList.moveToPro();
          freeList.setNextPosition(temp.getNextPosition());
          freeNum--;
          temp.activate(x,y);
          freeListAdd();
          temp.setNextPosition(runList.getNextPosition());
          runList.setNextPosition(temp);
          runNum++;
        }
      }
    }
    // 移除节点
    public void frozen() 
    {
      if (runNum==1) 
      {
        Node temp =runList.getNowPosition();
        runList = null;
        runNum--;
        temp.setNextPosition(freeList.getNextPosition());
        freeList.setNextPosition(temp);
        freeNum++;
      }else 
      {
        Node temp = runList.getNowPosition();
        runList.moveToPro();
        runList.setNextPosition(temp.getNextPosition());
        runNum--;
        temp.setNextPosition(freeList.getNextPosition());
        freeList.setNextPosition(temp);
        freeNum++;
      }
    }
    public void freeListAdd()
    {
      NodeList temp = new NodeList(freeList.getNowPosition());
      temp.setNextPosition(freeList.getNextPosition());;
      freeList.setNextPosition(temp.getNowPosition());
      freeNum++;
    }
    public void checkAlive()
    {
      int temp = runNum;
      for(int i=0;i<temp;i++)
      {
        if(!runList.getNowPosition().checkAlive())
        {
          frozen();
        }
        if (runList!=null) 
        {
          runList.moveToNext();
        }
      }
    }
    public int[] getNowPos()
    {
      if (runNum!=0) {
        return runList.getNowPos();
      }
      return null;
    }
    public void print(Graphics2D g2d) {
        if (runNum == 0 || runList == null) return;
        for (int i = 0; i < runNum; i++) {
            runList.getNowPosition().print(g2d);
            runList.moveToNext();
        }
    }
    public void deactivate()
    {
      runList.getNowPosition().deactivate();
      checkAlive();
    }
    public int getRunnum()
    {
      return runNum;
    }
    public void moveToNext()
    {
      if (runList!=null) {
        runList.moveToNext();
      }
    }
    public CircularDoubleLinkedListWithFreeNode getNowWeapon()
    {
      return runList.getNowPosition().getNowWeapon();
    }
}