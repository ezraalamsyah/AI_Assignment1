package ai_assignment1;
import java.util.*;

public class OCList{
  private ArrayList<State> list = new ArrayList<State>(1000);

  public int getNoOfStates(){ return list.size(); }

  public boolean isEmpty(){ return list.isEmpty(); }

  public State RemoveFront(){ return list.remove(0); }

  public void InsertBack(State s){ list.add(s);}

  public void InsertFront(State s){ list.add(0,s); }

  public void InsertSorted(State s){
    int i=0;
    while(i<list.size()){
      if(list.get(i).compareTo(s)>0) break;
      i++;
    }
    list.add(i,s);
  }
  
  public boolean isExist(State s){
    for(Object state:list) if (state.equals(s)) return true;
    return false;
  }
  
  public boolean ExistOrSubsitute(State s){
    for(int i=0;i<list.size();i++){
      State state =list.get(i);
      if(state.equals(s)) {
          if(s.getEV()<state.getEV()){
            list.remove(i);
            InsertSorted(s);
          }
          return true;
      }
    }
    return false;
  }

  public void Sort(){ Collections.sort(list);}
}