package  ai_assignment1;

import static ai_assignment1.Movement.*;
import java.util.*;

enum Movement{LEFT,UP,RIGHT,DOWN}

public class State implements Comparable{

  static int NoOfInstance=0;

  private final ArrayList<Integer> Slots;
  private final int Level;
  private final byte HV;//Heuristic Value
  private final byte BlankPos;
  public final State Parent;

  private static Movement[][] PossibleMovements={
    /*0*/ {RIGHT, DOWN},
    /*1*/ {LEFT,  DOWN},
    /*2*/ {UP,    RIGHT,  DOWN},  
    /*3*/ {LEFT,  UP,     RIGHT,  DOWN},
    /*4*/ {LEFT,  RIGHT,  DOWN},
    /*5*/ {LEFT,  DOWN},
    /*6*/ {UP,    RIGHT,  DOWN},
    /*7*/ {LEFT,  UP,     RIGHT,  DOWN},
    /*8*/ {LEFT,  UP,     RIGHT,  DOWN},
    /*9*/ {UP,    LEFT,  DOWN},
    /*10*/{UP,    RIGHT},
    /*11*/{LEFT,  UP,     RIGHT},
    /*12*/{LEFT,  UP,     RIGHT,  DOWN},
    /*13*/{UP,    LEFT,  DOWN},
    /*14*/{RIGHT,  UP},
    /*15*/{UP,    LEFT}
  };

  private static byte[][] MissPosTable={
    /*0*/	{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    /*1*/	{0,1,1,2,3,4,2,3,4,5,3,4,5,6,6,7},
    /*2*/	{1,0,2,1,2,3,3,2,3,4,4,3,4,5,5,6},
    /*3*/	{1,2,0,1,2,3,1,2,3,4,2,3,4,5,5,6},
    /*4*/	{2,1,1,0,1,2,2,1,2,3,3,2,3,4,4,5},
    /*5*/	{3,2,2,1,0,1,3,2,1,2,4,3,2,3,3,4},
    /*6*/	{4,3,3,2,1,0,4,3,2,1,5,4,3,2,4,3},
    /*7*/	{2,3,1,2,3,4,0,1,2,3,1,2,3,4,4,5},
    /*8*/	{3,2,2,1,2,3,1,0,1,2,2,1,2,3,3,4},
    /*9*/	{4,3,3,2,1,2,2,1,0,1,3,2,1,2,2,3},
    /*10*/	{5,4,4,3,2,1,3,2,1,0,4,3,2,1,3,2},
    /*11*/	{3,4,2,3,4,5,1,2,3,4,0,1,2,3,3,4},
    /*12*/	{4,3,3,2,3,4,2,1,2,3,1,0,1,2,2,3},
    /*13*/	{5,4,4,3,2,3,3,2,1,2,2,1,0,1,1,2},
    /*14*/	{6,5,5,4,3,2,4,3,2,1,3,2,1,0,2,1},
    /*15*/	{6,5,5,4,3,4,4,3,2,3,3,2,1,2,0,1}
  };

  static {//Non-static block
    NoOfInstance++;
    System.out.println(NoOfInstance);
  }

  @Override public boolean equals(Object obj){
    State rhs=(State)obj;
    return Slots==rhs.Slots;
  }


  @Override
  public int compareTo(Object obj){
    return getEV()-((State)obj).getEV();
  }

  private State(State parent, ArrayList<Integer> slots){
    Slots = slots;
    BlankPos = BlankPosition(Slots);
    Parent = parent;
    Level = Parent.Level + 1;
    System.out.println("Level="+Level);
    HV = TotalMissPos(Slots);
  }

  public State(int d0,int d1,int d2,int d3,int d4,int d5,int d6,int d7,int d8,int d9,int d10 , int d11 , int d12 , int d13 , int d14 , int d15){
    
      Slots= new ArrayList<Integer>();
      Slots.add(d0);
      Slots.add(d1);
      Slots.add(d2);
      Slots.add(d3);
      Slots.add(d4);
      Slots.add(d5);
      Slots.add(d6);
      Slots.add(d7);
      Slots.add(d8);
      Slots.add(d9);
      Slots.add(d10);
      Slots.add(d11);
      Slots.add(d12);
      Slots.add(d13);
      Slots.add(d14);
      Slots.add(d15);

//    assert(isWellFormed(Slots)):"Non Well-Formed!";
    BlankPos = BlankPosition(Slots);
    Parent = null;
    Level = 0;
    HV = TotalMissPos(Slots);
  }

  private static byte TotalMissPos(ArrayList<Integer> slots){
    byte totalMissed=0;
    for(int pos=0;pos<16;pos++){
      totalMissed += MissPosTable[slots.get(pos)][pos];
      
    }
    return totalMissed;
  }  

  public boolean isGoal(){

      boolean goal = true;
      int a = 1;
      for(int i=0;i<16;i++){
          if(i== 15){
              if(Slots.get(i) != 0){
                  goal=false;
              }
          }else if(Slots.get(i)!=a){
              goal = false;
          }
          a++;
      }
 return goal;
  }
//?
 public State[] NextStates(){
    //System.out.println("BlankPos is " + BlankPos);
    Movement[] ms;
    
    if(BlankPos >15)
        ms = PossibleMovements[BlankPos-1];
    else
        ms = PossibleMovements[BlankPos];
    
    int n=ms.length;
    State[] ss = new State[n];
    for(int i=0;i<n;i++) ss[i] = Move(ms[i]);
    return ss;	  
  }

  public int getEV(){
    return Level + 100*HV;
  }

//  static boolean isWellFormed(ArrayList<Integer> slots){
//    int flags=0x000001FF;
//    for(int i=0;i<9;i++){
//      flags &= ~(1<<(slots.get(i)));
//      
//    }
//    return (flags==0)&&(slots==0);
//  }

  static byte BlankPosition(ArrayList<Integer> slots){
    byte pos;
    for(pos=0;(pos<16)&&((slots.get(pos))!=0);pos++){
       // System.out.println("pos is not "  + pos);
    }	
    //System.out.println("pos is "  + pos);
    return pos;
  } 
 //p = position of number
  static ArrayList<Integer> SwapDigits(ArrayList<Integer> slots, int p1, int p2){
    
      int d1 =0;
      int d2 =0;
    for(int i=0; i<16;i++){
        if(i == p1){
            d1 = slots.get(i);
        }else if(i == p2){
            d2 = slots.get(i);
        }
    }  
    
//getDigit(slots,p1);
    //int d2 = slots.set(p1, p2);
            //getDigit(slots,p2);
    return setDigit(setDigit(slots,p1,d2),p2,d1);
  }
//get from arraylist
//  static int getDigit(ArrayList<Integer> slots, int p){
//    while(0!=p--) slots /= 10;
//    return slots%10;
//  }

 //set new value?
  static ArrayList<Integer> setDigit(ArrayList<Integer> slots, int position, int digit){
    ArrayList<Integer> newSlots= new ArrayList<Integer>();
    for(int i=0;i<16;i++){
      newSlots.add(((i==position)?digit:(slots.get(i)))); 

    }
    return newSlots;
  }

  //display array list
  public void Show(){
    //ArrayList<Integer> slots=Slots;
    char[] getChar = {'_','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o'};
    int i = 0;
    
    System.out.println("State:\n--------------");
    for(i=0;i<2;i++){
        int d = Slots.get(i);
        System.out.print("| ");
        System.out.print(getChar[d]);
//      int d = slots.get(i);
//      System.out.print((d==0)?"_":d);
//      
//      if(((i+1)%3)==0) System.out.println();
    }
    
    for(int j = 0; j<3 ; j++){
        System.out.print("| \n--------------\n");
        for(int k = 0; k<4; k++){
            int d = Slots.get((i++));
            System.out.print("| ");
            System.out.print(getChar[d]);
        }
    }
    System.out.print("| \n--------------\n");
    System.out.print("      ");
    for(int l = 14; l<16 ; l++){
        int d = Slots.get(l);
        System.out.print("| ");
        System.out.print(getChar[d]);
    }
    System.out.print("| \n--------------\n");
    
    System.out.println();
  }

  private State Move(Movement m){
    ArrayList<Integer> newSlots = null;
    switch(m){
      case LEFT: //assert((BlankPos%3)!=0):"Cannot move LEFT;";
          //
          if(BlankPos!=0 && BlankPos!=2 && BlankPos!=6 && BlankPos!=10 && BlankPos!=14)
          {
              newSlots = SwapDigits(Slots,BlankPos,BlankPos-1);
          }
          break;
          
          
      case UP: //assert(BlankPos>2):"Cannot move UP;";
          if(BlankPos!=0 && BlankPos!=1 && BlankPos!=4 && BlankPos!=5)
          {
              if(BlankPos==2 || BlankPos==3 || BlankPos==14 || BlankPos==15)
              {
                  newSlots = SwapDigits(Slots,BlankPos,BlankPos-2);
              }
              else
              {
                  newSlots = SwapDigits(Slots,BlankPos,BlankPos-4);
              }
          }
          break;
          
          
      case RIGHT: //assert(((BlankPos+1)%3)!=0):"Cannot move RIGHT;";
          if(BlankPos!=1 && BlankPos!=5 && BlankPos!=9 && BlankPos!=13 && BlankPos!=15){
              newSlots = SwapDigits(Slots,BlankPos,BlankPos+1);
          }
          break;
          
          
      case DOWN:// assert(BlankPos<6):"Cannot move DOWN;";
          
          
          if(BlankPos!=10 && BlankPos!=11 && BlankPos!=14 && BlankPos!=15)
          {
              if(BlankPos==0 || BlankPos==1 || BlankPos==12 || BlankPos==13)
              {
                  newSlots = SwapDigits(Slots,BlankPos,BlankPos+2);
              }
              else
              {
                  newSlots = SwapDigits(Slots,BlankPos,BlankPos+4);
              }
          }
          
          break;
    }
    return new State(this,newSlots);
  }
}
