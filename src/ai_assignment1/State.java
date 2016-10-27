package  ai_assignment1;

import static ai_assignment1.Movement.*;
import java.math.BigInteger;

enum Movement{LEFT,UP,RIGHT,DOWN}

public final class State implements Comparable{
    
    static int NoOfInstance=0;
    
    private final long Slots;
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
    }
    
    @Override public boolean equals(Object obj){
        State rhs=(State)obj;
        return Slots==rhs.Slots;
    }
    
    @Override
    public int compareTo(Object obj){
        return getEV()-((State)obj).getEV();
    }
    
    private State(State parent, long slots){
        Slots = slots;
        BlankPos = BlankPosition(Slots);
        Parent = parent;
        Level = Parent.Level + 1;
        HV = TotalMissPos(Slots);
    }
    
    public State(String d0,String d1,String d2,String d3,String d4,String d5,
    String d6,String d7,String d8,String d9,String d10 ,String d11 ,
    String d12 ,String d13 ,String d14 ,String d15){
        
        String HexString = Character.toString(toHex(d0).charAt(1)) +
        Character.toString(toHex(d1).charAt(1)) +
        Character.toString(toHex(d2).charAt(1)) +
        Character.toString(toHex(d3).charAt(1)) +
        Character.toString(toHex(d4).charAt(1)) +
        Character.toString(toHex(d5).charAt(1)) +
        Character.toString(toHex(d6).charAt(1)) +
        Character.toString(toHex(d7).charAt(1)) +
        Character.toString(toHex(d8).charAt(1)) +
        Character.toString(toHex(d9).charAt(1)) +
        Character.toString(toHex(d10).charAt(1)) +
        Character.toString(toHex(d11).charAt(1)) +
        Character.toString(toHex(d12).charAt(1)) +
        Character.toString(toHex(d13).charAt(1)) +
        Character.toString(toHex(d14).charAt(1)) +
        Character.toString(toHex(d15).charAt(1));
        
        Slots = Long.parseUnsignedLong(HexString, 16);
        
        //    assert(isWellFormed(Slots)):"Non Well-Formed!";
        BlankPos = BlankPosition(Slots);
        Parent = null;
        Level = 0;
        HV = TotalMissPos(Slots);
    }
    
    public static String toHex(String arg) {
        return String.format("%x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/)));
    }
    
    public static int[] charToIndex(String hs){
        
        int[] indexArray = new int[16];
        if(hs.length()<16)
        {
            indexArray[0] = 0;
            for(int i=0; i<15; i++){
                switch (hs.charAt(i)) {
                    case '1':
                    indexArray[i+1] = 1;
                    break;
                    case '2':
                    indexArray[i+1] = 2;
                    break;
                    case '3':
                    indexArray[i+1] = 3;
                    break;
                    case '4':
                    indexArray[i+1] = 4;
                    break;
                    case '5':
                    indexArray[i+1] = 5;
                    break;
                    case '6':
                    indexArray[i+1] = 6;
                    break;
                    case '7':
                    indexArray[i+1] = 7;
                    break;
                    case '8':
                    indexArray[i+1] = 8;
                    break;
                    case '9':
                    indexArray[i+1] = 9;
                    break;
                    case 'a':
                    indexArray[i+1] = 10;
                    break;
                    case 'b':
                    indexArray[i+1] = 11;
                    break;
                    case 'c':
                    indexArray[i+1] = 12;
                    break;
                    case 'd':
                    indexArray[i+1] = 13;
                    break;
                    case 'e':
                    indexArray[i+1] = 14;
                    break;
                    case 'f':
                    indexArray[i+1] = 15;
                    break;
                    default:
                    break;
                }
            }
        }else
        {
            for(int i=0; i<16; i++){
                switch (hs.charAt(i)) {
                    case '1':
                    indexArray[i] = 1;
                    break;
                    case '2':
                    indexArray[i] = 2;
                    break;
                    case '3':
                    indexArray[i] = 3;
                    break;
                    case '4':
                    indexArray[i] = 4;
                    break;
                    case '5':
                    indexArray[i] = 5;
                    break;
                    case '6':
                    indexArray[i] = 6;
                    break;
                    case '7':
                    indexArray[i] = 7;
                    break;
                    case '8':
                    indexArray[i] = 8;
                    break;
                    case '9':
                    indexArray[i] = 9;
                    break;
                    case 'a':
                    indexArray[i] = 10;
                    break;
                    case 'b':
                    indexArray[i] = 11;
                    break;
                    case 'c':
                    indexArray[i] = 12;
                    break;
                    case 'd':
                    indexArray[i] = 13;
                    break;
                    case 'e':
                    indexArray[i] = 14;
                    break;
                    case 'f':
                    indexArray[i] = 15;
                    break;
                    case '0':
                    indexArray[i] = 0;
                    break;
                    default:
                    break;
                }
            }
        }
        
        return indexArray;
    }
    
    private static byte TotalMissPos(long slots){
        byte totalMissed=0;
        
        String hs = Long.toHexString(slots);
        int[] indexArray = charToIndex(hs);
        for(int pos=0;pos<16;pos++){
            totalMissed += MissPosTable[indexArray[pos]][pos];
        }
        return totalMissed;
    }
    
    public boolean isGoal(){
        return (Slots == 1311768467463790320L);
    }
    
    public State[] NextStates(){
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
        return Level + 1*HV;
    }
    
    static byte BlankPosition(long slots){
        byte pos;
        String d = Long.toHexString(slots);
        char[] values = d.toCharArray();
        
        if(values.length == 15)
        return pos=0;
        
        for(pos=0;(pos<values.length)&&((values[pos]!='0'));pos++);
 
        return pos;
    }
    //p = position of number
    static long SwapDigits(long slots, int p1, int p2){
        long newSlots = 0;
        String d = Long.toHexString(slots);
        char[] values = d.toCharArray();
        
        if(d.length()<16){
            char[] BlankAtFirst = new char[16];
            BlankAtFirst[0] = '0';
            for(int i=1; i<16; i++){
                BlankAtFirst[i] = values[i-1];
            }
            char temp = BlankAtFirst[p1];
            BlankAtFirst[p1] = BlankAtFirst[p2];
            BlankAtFirst[p2] = temp;
            d = new String(BlankAtFirst);
            newSlots = Long.parseUnsignedLong(d, 16);
        }else {
            char temp = values[p1];
            values[p1] = values[p2];
            values[p2] = temp;
            d = new String(values);
            newSlots = Long.parseUnsignedLong(d, 16);
        }
        
        return newSlots;
    }
  
    //display array list
    public void Show(){
        //ArrayList<Integer> slots=Slots;
        char[] getChar = {'_','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o'};
        int i = 0;
        String hs = Long.toHexString(Slots);
        int[] d = charToIndex(hs);
        
        System.out.println("State:\n--------------");
        for(i=0;i<2;i++){            
            System.out.print("| ");
            System.out.print(getChar[d[i]]);
            //      int d = slots.get(i);
            //      System.out.print((d==0)?"_":d);
            //
            //      if(((i+1)%3)==0) System.out.println();
        }
        
        for(int j = 0; j<3 ; j++){
            System.out.print("| \n--------------\n");
            for(int k = 0; k<4; k++){
                System.out.print("| ");
                System.out.print(getChar[d[i]]);
                i++;
            }
        }
        System.out.print("| \n--------------\n");
        System.out.print("      ");
        for(int l = 14; l<16 ; l++){
            System.out.print("| ");
            System.out.print(getChar[d[i]]);
            i++;
        }
        System.out.print("| \n--------------\n");
        
        System.out.println();
    }
    
    private State Move(Movement m){
        long newSlots = 0;
        switch(m){
            case LEFT:
            if(BlankPos!=0 && BlankPos!=2 && BlankPos!=6 && BlankPos!=10 && BlankPos!=14)
            {
                newSlots = SwapDigits(Slots,BlankPos,BlankPos-1);
            }
            break;
         
            case UP:
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
            
            case RIGHT:
            if(BlankPos!=1 && BlankPos!=5 && BlankPos!=9 && BlankPos!=13 && BlankPos!=15){
                newSlots = SwapDigits(Slots,BlankPos,BlankPos+1);
            }
            break;
            
            case DOWN:
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
