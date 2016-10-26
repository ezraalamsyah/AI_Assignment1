package  ai_assignment1;
import java.util.*;

public class Main {
  static enum SearchStrategy{DepthFirst,BreadthFirst,BestFirst}
  private static void ShowPath(State st){
    ArrayList<State> path=new ArrayList<State>();
    while(st!=null){
      path.add(0,st);
      st = st.Parent;
    }
    for(State s:path) s.Show();
    System.out.println("The no of steps="+path.size());
    System.out.println("The no of instances="+State.NoOfInstance);
  }

  public static void main(String[] args){
    SearchStrategy strategy=SearchStrategy.BestFirst;
    final int _=0;
//    State initialState = new State(
//            1,3,_,
//            5,2,8,
//            6,4,7);
//           State initialState = new State(
//            4,  10,
//           2,  14,  6,  5,
//            3,  7, 13,  11,
//            9, _, 1, 15,
//                    8, 12);
        State initialState = new State(
            "a",  "0",
           "d",  "b",  "i",  "e",
            "c",  "l", "h",  "f",
            "g", "k",  "n", "j",
                    "m", "o"); 

    OCList open = new OCList();
    OCList closed = new OCList();
    open.InsertFront(initialState);
    while(!open.isEmpty()){
      State st=open.RemoveFront(); //currentState
      closed.InsertBack(st);
      if(st.isGoal()){//Bingo!!
        ShowPath(st);
        break;//If we only interested in one goal just stop here. Otherwise don't break
      }
      State[] ss = st.NextStates();
      switch(strategy){
          case BestFirst://Sorted List
            for(State s:ss){
              if(!closed.isExist(s)){
                if(!open.ExistOrSubsitute(s)) open.InsertSorted(s);
              }
            }
            break;
//        case BreadthFirst://Queue
//            for(State s:ss){
//              if(!open.isExist(s) && !closed.isExist(s)) open.InsertBack(s);
//            }
//            break;
//        case DepthFirst://Stack
//            for(int i=0;i<ss.length;i++){
//              State s=ss[ss.length-i-1];
//              if(!open.isExist(s) && !closed.isExist(s)) open.InsertFront(s);
//            }
//            break;
        
      }
    }
  }
}