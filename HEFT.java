
package mygraph;

import mygraph.Graph;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Comparator;

class TaskProcessor{
    private int processor;
    private double AST;
    private double AFT;
    
    public TaskProcessor()
    {
        this.processor =-1;
        this.AST=-1;
        this.AFT=-1;
    
    }

    public int getProcessor() {
        return processor;
    }

    public void setProcessor(int processor) {
        this.processor = processor;
    }

    public double getAST() {
        return AST;
    }

    public void setAST(double AST) {
        this.AST = AST;
    }

    public double getAFT() {
        return AFT;
    }

    public void setAFT(double AFT) {
        this.AFT = AFT;
    }

    
}

public class HEFT{

private int P, V;
private double[][] MatComm;
private double[][] MatExeProc;
private Graph G;
private int[] affectationTacheProc;
private int entryNode;
private int exitNode;
private int noSlots;
private TaskProcessor[] scheduledTask;

	public HEFT(int V,int P,int entryNode,int exitNode,double[][] MatComm,double[][] MatExeProc,Graph G)
	{
		this.V = V;
		this.P = P;
		this.MatComm = MatComm;
		this.MatExeProc = MatExeProc;
		this.G = G;
		//this.affectationTacheProc = this.affectationTache(this.schedulingList());
                this.entryNode = entryNode;
                this.exitNode = exitNode;
                this.noSlots=0;
                
               
                this.scheduledTask = new TaskProcessor[V];
                for(int i=0;i<V;i++)
                {
                    this.scheduledTask[i] = new TaskProcessor();
                    this.scheduledTask[i].setProcessor(-1);
                    this.scheduledTask[i].setAFT(0.0);
                    this.scheduledTask[i].setAFT(0.0);
                }
	}
	public double makespan()
	{
            int task = this.exitNode;
               /* int task;
                TaskProcessor EST_EFT = new TaskProcessor(); 
                TaskProcessor tp = new TaskProcessor(); 
                double minEFT=1000000000;
                
                
		for(int i=0;i<readyList.length;i++)
		{
                   
                    double min =1000000000;
                    int proc = 0;
                    task = readyList[i];
                    
                    //check if the task is the entry task
                    if(task == this.entryNode)
                    {
                        //Looking for the processor minimizing entryNode
                        for(int j=0;j<this.P;j++)
                        {
                            if(min>this.MatExeProc[task][j])
                            {
                                min=this.MatExeProc[task][j];
                                proc = j;
                            }
                        
                        }
                        this.scheduledTask[task] = new TaskProcessor();
                        this.scheduledTask[task].setAST(0);
                        this.scheduledTask[task].setAFT(min);
                        this.scheduledTask[task].setProcessor(proc);
                        minEFT = min;
                        
                    
                    }
                    else
                    {
                        for(int j=0;j<this.P;j++)
                        {
                            minEFT = 99999.9;
                             System.out.println("IN"+i+" is "+tp.getAFT());
                            tp = this.calculate_EST_EFT(task,j,EST_EFT);

                                if(min>tp.getAFT())
                                {
                                                               
                          
                                    this.scheduledTask[task] =tp;
                                    minEFT = tp.getAFT();
                                    
                                }
                                
                            if(task ==this.exitNode)
                                return minEFT;
                        }
                    }
                    //j'affecte à la tache i le processeur j;
                    //System.out.println("task"+readyList[i]+" affected to processor "+proc+" EFT = "+min);
                    

		}*/
                return  this.scheduledTask[task].getAFT();
		
	}        
        



	public void makeSchedule(int[] readyList)
	{
		
                int task;
                TaskProcessor EST_EFT = new TaskProcessor(); 
                TaskProcessor tp = new TaskProcessor(); 
                
                
		for(int i=0;i<readyList.length;i++)
		{
                   
                    double min =1000000000,minEFT=1000000000;
                    int proc = 0;
                    task = readyList[i];
                    //check if the task is the entry task
                    if(task == this.entryNode)
                    {
                        //Looking for the processor minimizing entryNode
                        for(int j=0;j<this.P;j++)
                        {
                            if(min>this.MatExeProc[task][j])
                            {
                                min=this.MatExeProc[task][j];
                                proc = j;
                            }
                        
                        }
                        this.scheduledTask[task].setAST(0);
                        this.scheduledTask[task].setAFT(min);
                        this.scheduledTask[task].setProcessor(proc);
                    
                    }
                    else
                    {
                        for(int j=0;j<this.P;j++)
                        {
                            tp = this.calculate_EST_EFT(task,j);
                           //System.out.println("AST "+tp.getAST()+" AFT"+tp.getAFT());
                                if(minEFT>tp.getAFT())
                                {
                                    tp.setProcessor(j);
                                    
                                    
                                    minEFT = tp.getAFT();
                                    proc = j;
                                }
                        }
                        this.scheduledTask[task] =tp;
                    }
                    //j'affecte à la tache i le processeur j;
                   // System.out.println("task"+readyList[i]+" affected to processor "+proc+" EFT = "+minEFT);
                    

		}
		
	}

        
        
	double C(int i,int k)
	{
			//check the processor to which i is affectedTaskToProc
			//int m = this.affectationTacheProc[i];
			//int n = this.affectationTacheProc[k];
			
			//start time of processor Pm
			
			//int Lm = this.L(this.P)[m];
                        int Lm = 0;
			
			//return Lm + (this.Data(i,k)/this.B(m,n));
                        return this.Data(i, k);
                        
                        
	}
	private double Data (int i, int k)
	{
            return this.MatComm[i][k];			
	}
	private int[][] B()
	{
		int[][] myArray= new int[this.P][this.P];
		int p = this.P;
		for(int v=0; v<p; v++)
		{
			
			for(int w=0;w<p; w++)
			{
				if(v!=w)
                                    myArray[v][w] = 1;
				else 
                                    myArray[v][w] = 0;
			}
		}
		
		return myArray;		
	}
	//Transfert rate rate
        //We assume that given the high speed of processors, data transfert rate is 100%
       
	private int B(int m,int n)
	{
		int p = this.P;
		
                return 1;
		/*for(int v=0; v<p; v++)
		{
			
			for(int w=0;w<p; w++)
			{
				if(v==m && w ==n)
					return 1;
			}
		}
                return 0;*/
	}
	
	private double avgB ()
	{
		/*int s = 0;
		int p = this.P;
		for(int v=0; v<p; v++)
		{
			
			for(int w=0;w<p; w++)
			{
				s = s+B[v][w];
			}
		}
		
		return s/(p*p);*/
            return 1;
	}
	
        //We assume that the communication stratup cost is equal to zero
	private int[] L(int p)
	{
		int[] myArray = new int[p];
		for(int v=0;v<p; v++)
		{
			
				myArray[v] = 0;
			
		}
		return myArray;		
	}
	
	private double avgL(int[] L)
	{
		int s=0;
		for(int v=0;v<this.P; v++)
		{
			
			s=s+L[v];
			
		}
		return s/this.P;		
	}
	
	
	public double avgW(int i)
	{
		double s = 0;
		int q = this.P;
		for(int j=1;j<q;j++)
			s = s + this.w(i,j)/q;
		
		return s;

	}

	private double w(int i,int j)
	{

		return this.MatExeProc[i][j];

	}
	
	private double avgC(int i, int k)
	{
		int p = this.P;
		double L = this.avgL(this.L(p));
		double B = this.avgB();
		
		return  this.Data(i,k)/B ;
		
		
	}
	

	
	public double ranKu(int t)
	{
		double s = this.avgW(t);
		if(t==this.exitNode) return this.avgW(this.entryNode);
		double max = 0;
		for(int v:this.G.adj(t))
		{
			double m = (this.avgC(t,v)+ranKu(v));
			
			if (max < m) max = m;
			
		}
		
		return this.avgW(t)+max;
		
	}


//Definir la liste des taches suivant l'ordre croissant de la valeur du paramètre rankOCT	
	public int[] schedulingList()
	{
	double[]  myArray = new double[this.V];
	int[]  myArray1 = new int[this.V];
	int task=0; double max;
	
	//Compute ranku for each task;
		for(int k=0;k<this.V;k++)
		{
			myArray[k]=this.ranKu(k);

		}
		
		//sorting tasks based on descending order of ranku
		for(int i=0;i<this.V;i++)
		{
		  max=0;
			
			for(int j=0;j<this.V;j++)
			{
				if(max<myArray[j])
				{
                                    max = myArray[j];
                                    task = j;
                                       
				}

			}
			myArray1[i] = task;
                         myArray[task] = -1;

		}		
		return myArray1;
	}
        
	

        
        void findFreeSlot(Integer processor, Integer nbSlots, ArrayList<ArrayList<Double>> machineFreeTIme)
        {
            //
            double highestAFT = -99999.9, min = 99999.9;

            
            for(int i=0;i<this.V;i++)
            {
                //check wether the task is scheduled on the same processor or not
                
                 if(this.scheduledTask[i].getProcessor()==processor)
                 {
                    if(this.scheduledTask[i].getAFT()>highestAFT)
                        highestAFT=this.scheduledTask[i].getAFT();
                    for(int j=0;j<this.V;j++)
                    {
                        int flag =0;
                        if(this.scheduledTask[j].getProcessor()!=processor || i==j) continue;
                        if(this.scheduledTask[j].getAST()>this.scheduledTask[i].getAFT() && this.scheduledTask[j].getAST()<min )
                        {
                            

                            min = this.scheduledTask[j].getAST();
                            flag = 1;
                        }

                        if(flag==1)
                        {
                            
                            this.insertSlot( machineFreeTIme, nbSlots, this.scheduledTask[i].getAFT(), min);
                            nbSlots++;

                        }

                    }
                 }
                                               

              
                
            }
                this.insertSlot( machineFreeTIme, nbSlots, highestAFT,99999.9);
                nbSlots++;  
                          
       
                this.noSlots = nbSlots;
            
        }
	
	
        void insertSlot(ArrayList<ArrayList<Double>> machineFreeTime,int crtPosition, double start, double end)
        {
            int i;
            if(start<0) start =0;
            if(crtPosition>0)
            {
            for(i=crtPosition-1; i>=0; i--)
            {                   
                machineFreeTime.add(i,new ArrayList(2));
                machineFreeTime.get(i).add(0.0);
                machineFreeTime.get(i).add(0.0);
                if(start < machineFreeTime.get(i).get(0))
                {
                    machineFreeTime.get(i+1).add(0,machineFreeTime.get(i).get(0));
                    machineFreeTime.get(i+1).add(1,machineFreeTime.get(i).get(1));
                    
                }
                else
                    break;
            }
            
            machineFreeTime.add(i+1,new ArrayList());
            machineFreeTime.get(i+1).add(0,start);
            machineFreeTime.get(i+1).add(1,end);
            }
            else{
                
                    machineFreeTime.add(crtPosition,new ArrayList());
                    machineFreeTime.get(crtPosition).add(0,start);
                    machineFreeTime.get(crtPosition).add(1,end);
     
            }

        }	
	
        // Find EST
        Double findEST(int task,int processor)
        {
            int i;
            Double ST,EST=-99999.0,commCost;
            for(i=0; i<this.V; i++)
            {
                if(this.Data(i,task)!=-1)
                {
                    // we consider that the communication cost between processors is equal to 0,
                    //Given that now days processors are at very high capacities
                    commCost = 0.0;
                   /* if(data_transfer_rate[schedule[i].processor][processor]==0)
                        comm_cost=0;
                    // Otherwise
                    else
                        comm_cost=this.Data(i,task)/this.[schedule[i].processor][processor];*/
                    
                    ST=this.scheduledTask[i].getAFT() + commCost;
                    // Try to find the max EST
                    if(EST<ST)
                        EST=ST;
                }
            }
            return EST;
        }
        
        
        // Calculate the EST and EFT
        TaskProcessor calculate_EST_EFT(int task,int processor)
        {
            ArrayList<ArrayList<Double>> machineFreeTime;
            Double EST;
            int i;
            TaskProcessor EST_EFT = new TaskProcessor();
             
            machineFreeTime= new ArrayList( new ArrayList<Double>());
    
            this.noSlots=0;
            this.findFreeSlot(processor,this.noSlots,machineFreeTime);
            TaskProcessor tmpEST_EFT = new TaskProcessor();
            EST=this.findEST(task,processor);
            //printf("%lf\n",EST);
            EST_EFT.setAST(EST);
            EST_EFT.setProcessor(processor);
            tmpEST_EFT = EST_EFT;
           
            for(i=0; i<this.noSlots; i++)
            { 
                if(EST<machineFreeTime.get(i).get(0))
                {
                    if((machineFreeTime.get(i).get(0) + this.MatExeProc[task][processor]) <= machineFreeTime.get(i).get(1))
                    {//System.out.println("suis dedans");
                        EST_EFT.setAST(machineFreeTime.get(i).get(0));
                        EST_EFT.setAFT(machineFreeTime.get(i).get(0) + this.MatExeProc[task][processor]);
                        tmpEST_EFT= EST_EFT;
                        return tmpEST_EFT;
                    }
                }
                if(EST>=machineFreeTime.get(i).get(0))
                {
                    if((EST + this.MatExeProc[task][processor]) <= machineFreeTime.get(i).get(1))
                    {
                        EST_EFT.setAFT(EST_EFT.getAST() + this.MatExeProc[task][processor]);
                        tmpEST_EFT= EST_EFT;
                        return tmpEST_EFT;
                    }
                }
            }
            return tmpEST_EFT;
        }  
        
        private  ArrayList<Integer> readyList(int V,int level)
	{
		ArrayList<Integer> tasks  = new ArrayList<Integer>();
		for(int i=0;i<V;i++)
		{
                    if(this.level(i) == level)
                            tasks.add(i);
				
		}
		
		return tasks;
		
		
	}
                
        
       double CPmin()
       {
            double min = 999999;
            double cpmin = 0.0;
            double levelMin = 9999999;
            ArrayList<Integer> readyList = new ArrayList<Integer>();
            double [] octMatrix;
            for(int level = 0;level <= this.level(this.V-1);level++)
            {
                    readyList = this.readyList(this.V,level);

                for(int v=0 ;v<readyList.size();v++)
                {
                    for(int proc=0;proc<this.P;proc++)
                    {
                        if(min>this.w(readyList.get(v), proc))
                        {
                            min = this.w(readyList.get(v), proc);

                        }
                    }
                   if ( levelMin >=min) levelMin=min;
                }
                cpmin+=levelMin;
           
           }
            return cpmin;
       }
       
       public double slr()
       {
           return this.makespan()/this.CPmin();
       }
       
       public double speedup()
       {
           
           double sum=0.0;
           for(int v=0;v<this.V;v++)
           {
               double min = 999999;
                for(int proc=0;proc<this.P;proc++)
                {
                    if(min>this.w(v, proc))
                    {
                        min = this.w(v, proc);

                    }
                }  
                
                sum+=min;
           }
           
           return sum/this.makespan();
       
       }
       
    public double exeTime()
       {
            double min = 999999;
            double exeTime = 0.0;
            double levelMax = -9999999;
            ArrayList<Integer> readyList = new ArrayList<Integer>();
            double [] octMatrix;
            for(int level = 0;level <= this.level(this.V-1);level++)
            {
                    readyList = this.readyList(this.V,level);

                for(int v=0 ;v<readyList.size();v++)
                {
                   if ( levelMax <this.scheduledTask[readyList.get(v)].getAFT()) levelMax=this.scheduledTask[readyList.get(v)].getAFT();
                }
                exeTime+=levelMax;
           
           }
            return exeTime;
       }
              
       
        public int level(int t)
	{
            if (t == this.entryNode) return 1;
            int max = 0;
            for(int v :this.G.pred(t))
            {
                    int tmp = level(v);
                    if(max < tmp) max = tmp;

            }

            return max+1;
		
	}       

}